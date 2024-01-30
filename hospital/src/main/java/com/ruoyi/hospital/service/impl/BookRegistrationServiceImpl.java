package com.ruoyi.hospital.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.hospital.domain.*;
import com.ruoyi.hospital.enu.WorkTimeEnum;
import com.ruoyi.hospital.mapper.DoctorMapper;
import com.ruoyi.hospital.mapper.PatientMapper;
import com.ruoyi.hospital.mapper.WorkTimeMapper;
import com.ruoyi.hospital.util.AESUtil;
import com.ruoyi.hospital.util.AesKeyUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.hospital.mapper.BookRegistrationMapper;
import com.ruoyi.hospital.service.IBookRegistrationService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 预约挂号Service业务层处理
 *
 * @author liu
 * @date 2023-06-02
 */
@Service
@Transactional
public class BookRegistrationServiceImpl implements IBookRegistrationService {
    @Autowired
    private BookRegistrationMapper bookRegistrationMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private PatientMapper patientMapper;
    @Autowired
    private WorkTimeMapper workTimeMapper;


    /**
     * 查询预约挂号
     *
     * @param registrationId 预约挂号主键
     * @return 预约挂号
     */
    @Override
    public BookRegistration selectBookRegistrationByRegistrationId(Long registrationId) throws Exception {
        BookRegistration bookRegistration = bookRegistrationMapper.selectBookRegistrationByRegistrationId(registrationId);

        bookRegistration.setPatientName(AESUtil.decrypt(bookRegistration.getPatientName(),AesKeyUtil.getEncryptKey()));
        bookRegistration.setDoctorName(AESUtil.decrypt(bookRegistration.getDoctorName(),AesKeyUtil.getEncryptKey()));
//        bookRegistration.setCreateTime(AESUtil.decrypt(doctor.getPhone(),AesKeyUtil.getEncryptKey()));
        return bookRegistration;
    }

    /**
     * 查询预约挂号列表
     *
     * @param bookRegistration 预约挂号
     * @return 预约挂号
     */
    @Override
    public List<BookRegistration> selectBookRegistrationList(BookRegistration bookRegistration) {

        List<BookRegistration> bookRegistrations = bookRegistrationMapper.selectBookRegistrationList(bookRegistration);
        List<BookRegistration> collect = bookRegistrations.stream().map(p -> {
            try {
                p.setDoctorName(AESUtil.decrypt(p.getDoctorName(),AesKeyUtil.getEncryptKey()));
                p.setPatientName(AESUtil.decrypt(p.getPatientName(),AesKeyUtil.getEncryptKey()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return p;
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * 新增预约挂号
     *
     * @param bookRegistration 预约挂号
     * @return 结果
     */
    @Override
    public int insertBookRegistration(BookRegistration bookRegistration) throws Exception {

        // 判断日期是否在今天之前
//        if (bookRegistration.getDay().before(new Date()) &&
//                bookRegistration.getWorkTime()<WorkTimeEnum.getCurrentWorkTimeEnum(new Date())){
//            throw new ServiceException("请您不要选之前的日期");
//        }
        SysUser user = SecurityUtils.getLoginUser().getUser();
        String phone = user.getPhonenumber();
        String encrypt = AESUtil.encrypt(phone, AesKeyUtil.getEncryptKey());
        Patient patient = patientMapper.findByPhone(encrypt);
        if (patient == null) {
            throw new ServiceException("用户不存在");
        }
        // 判断redis30分钟内是否有人重复点
        BookRegistration bookRegistrationRe = (BookRegistration)redisCache.getCacheObject("bookRegistration");
        if (bookRegistrationRe!=null){
            if (bookRegistrationRe.getPatientId().equals(patient.getPatientId()) && bookRegistration.getDoctorId().equals(bookRegistrationRe.getDoctorId())) {
                throw new ServiceException("在30分钟内不要重复预约同一个医生");
            }
        }
        bookRegistration.setTreatmentNum(makeTreateNum());
        bookRegistration.setPatientId(patient.getPatientId());
        bookRegistration.setCreateTime(new Date());
        // 预约成功,医生预约数量减一
        List<WorkTime> workTime = workTimeMapper.selectWorkTimeByDocId(bookRegistration.getDoctorUserId(),bookRegistration.getWorkTime());
        int sum = workTime.stream().mapToInt(wt -> wt.getCaseNumer().intValue()).sum();
        if (sum==0){
            throw new ServiceException("该医生诊断名额已经没有了,请耐心等待");
        }else {
            sum = sum - 1;
        }
        // 这里更新医生预约数
        WorkTime workTime1 = workTime.get(0);
        workTime1.setCaseNumer((long) sum);
        workTimeMapper.updateWorkTime(workTime1);
        // 控制用户不能连续点预约
        redisCache.setCacheObject("bookRegistration", bookRegistration, 30, TimeUnit.MINUTES);

        // 解密
        String patientName = patient.getPatientName();
        String decrypt = AESUtil.decrypt(patientName, AesKeyUtil.getEncryptKey());
        patient.setPatientName(decrypt);
        bookRegistration.setPatientName(patient.getPatientName());

        return bookRegistrationMapper.insertBookRegistration(bookRegistration);
    }

    private String makeTreateNum() {
        // 生成当前日期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateStr = dateFormat.format(new Date());
        // 生成6位随机数字和字符组合字符串
        Random random = new Random();
        String randomStr = "";
        for (int i = 0; i < 6; i++) {
            int num = random.nextInt(36);
            if (num < 10) {
                // 数字
                randomStr += num;
            } else {
                // 字符
                char ch = (char) ('a' + num - 10);
                randomStr += ch;
            }
        }
        return dateStr + randomStr;
    }

    /**
     * 修改预约挂号
     *
     * @param bookRegistration 预约挂号
     * @return 结果
     */
    @Override
    public int updateBookRegistration(BookRegistration bookRegistration) {
        return bookRegistrationMapper.updateBookRegistration(bookRegistration);
    }

    /**
     * 批量删除预约挂号
     *
     * @param registrationIds 需要删除的预约挂号主键
     * @return 结果
     */
    @Override
    public int deleteBookRegistrationByRegistrationIds(Long[] registrationIds) {
        return bookRegistrationMapper.deleteBookRegistrationByRegistrationIds(registrationIds);
    }

    /**
     * 删除预约挂号信息
     *
     * @param registrationId 预约挂号主键
     * @return 结果
     */
    @Override
    public int deleteBookRegistrationByRegistrationId(Long registrationId) {
        return bookRegistrationMapper.deleteBookRegistrationByRegistrationId(registrationId);
    }

    @Override
    public List<WorkTime> getWorkTime(Office office) {
        List<WorkTime> workTimeList = bookRegistrationMapper.getWorkTime(office);
        List<WorkTime> collect = workTimeList.stream().map(p -> {
            Doctor doctor = p.getDoctor();
            String username = doctor.getUsername();

            try {
                doctor.setUsername(AESUtil.decrypt(username,AesKeyUtil.getEncryptKey()));
                p.setDoctor(doctor);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return p;
        }).collect(Collectors.toList());
        //  时间转换
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" yyyy-MM-dd");
        Date date = new Date();
        // 转为成了时间
        String format = simpleDateFormat.format(date);

        // 获取今天是星期几
        Calendar today = Calendar.getInstance();
        int dayOfWeek = today.get(Calendar.DAY_OF_WEEK); // 获取星期几
        int offset = dayOfWeek - Calendar.MONDAY; // 计算星期一的偏移量
        today.add(Calendar.DATE, -offset); // 设置为星期一的日期
        Date monday = today.getTime(); // 获取星期一时间
        today.add(Calendar.DATE, 6); // 周日的日期
        Date sunday = today.getTime();
        // 开始设置数据
        collect.stream().forEach(wt -> {
            Long workTime = wt.getWorkTime();
            if (workTime == 1 || workTime == 2) {
                wt.setDay(monday);
            } else if (workTime == 3 || workTime == 4) {
                // 将Date对象转换成Java 8的LocalDate对象
                LocalDate localDate = monday.toInstant().atZone(ZoneId.of("GMT+8")).toLocalDate();
                // 在LocalDate上加1天
                LocalDate tomorrow = localDate.plusDays(1);
                // 将LocalDate转换成Date对象
                Date newTime = Date.from(tomorrow.atStartOfDay(ZoneId.of("GMT+8")).toInstant());
                wt.setDay(newTime);
            } else if (workTime == 5 || workTime == 6) {
                // 将Date对象转换成Java 8的LocalDate对象
                LocalDate localDate = monday.toInstant().atZone(ZoneId.of("GMT+8")).toLocalDate();
                // 在LocalDate上加2天
                LocalDate tomorrow = localDate.plusDays(2);
                // 将LocalDate转换成Date对象
                Date newTime = Date.from(tomorrow.atStartOfDay(ZoneId.of("GMT+8")).toInstant());
                wt.setDay(newTime);
            } else if (workTime == 7 || workTime == 8) {
                // 将Date对象转换成Java 8的LocalDate对象
                LocalDate localDate = monday.toInstant().atZone(ZoneId.of("GMT+8")).toLocalDate();
                // 在LocalDate上加3天
                LocalDate tomorrow = localDate.plusDays(3);
                // 将LocalDate转换成Date对象
                Date newTime = Date.from(tomorrow.atStartOfDay(ZoneId.of("GMT+8")).toInstant());
                wt.setDay(newTime);
            } else if (workTime == 9 || workTime == 10) {
                // 将Date对象转换成Java 8的LocalDate对象
                LocalDate localDate = monday.toInstant().atZone(ZoneId.of("GMT+8")).toLocalDate();
                // 在LocalDate上加4天
                LocalDate tomorrow = localDate.plusDays(4);
                // 将LocalDate转换成Date对象
                Date newTime = Date.from(tomorrow.atStartOfDay(ZoneId.of("GMT+8")).toInstant());
                wt.setDay(newTime);
            } else if (workTime == 11 || workTime == 12) {
                // 将Date对象转换成Java 8的LocalDate对象
                LocalDate localDate = monday.toInstant().atZone(ZoneId.of("GMT+8")).toLocalDate();
                // 在LocalDate上加5天
                LocalDate tomorrow = localDate.plusDays(5);
                // 将LocalDate转换成Date对象
                Date newTime = Date.from(tomorrow.atStartOfDay(ZoneId.of("GMT+8")).toInstant());
                wt.setDay(newTime);
            } else if (workTime == 13 || workTime == 14) {
                // 将Date对象转换成Java 8的LocalDate对象
                LocalDate localDate = monday.toInstant().atZone(ZoneId.of("GMT+8")).toLocalDate();
                // 在LocalDate上加6天
                LocalDate tomorrow = localDate.plusDays(6);
                // 将LocalDate转换成Date对象
                Date newTime = Date.from(tomorrow.atStartOfDay(ZoneId.of("GMT+8")).toInstant());
                wt.setDay(newTime);
            }

        });


        // 获取前一个周的日期
        List<WorkTime> workTimes = new ArrayList<>();
        today.add(Calendar.DATE, -14); // 周日的日期
//        for (int i = 1; i <= 7; i++) {
//            WorkTime workTime = new WorkTime();
//            workTime.setWorkTime(0L);  // 设置工作时间为 0
//            Optional<WorkTime> optionalWorkTime = workTimeList.stream().filter(w -> simpleDateFormat.format(w.getDay()).equals(simpleDateFormat.format(today.getTime()))).findFirst();
//            if (optionalWorkTime.isPresent()) { // 如果当天有记录，则复制并更新日期，添加到结果集中
//                WorkTime original = optionalWorkTime.get();
//                BeanUtils.copyProperties(original, workTime);
//                workTime.setDay(today.getTime());
//                workTimes.add(workTime);
//            } else { // 如果当天没有记录，则将工作时间设为 0，设置日期，并添加到结果集中
//                workTime.setDay(today.getTime());
//                workTimes.add(workTime);
//            }
//            today.add(Calendar.DATE, 1); // 设置为下一个日期
//        }
        for (int i = 0; i < 7; i++) {
            System.out.println("前一个周:"+today.get(Calendar.DAY_OF_WEEK));
            today.add(Calendar.DATE, 1);

            collect.stream().forEach(wk -> {
                Calendar instance = Calendar.getInstance();
                instance.setTime(wk.getDay());
                // 获取星期几
                int i1 = instance.get(Calendar.DAY_OF_WEEK);
                instance.setTime(today.getTime());
                // 获取星期几
                int i2 = instance.get(Calendar.DAY_OF_WEEK);
                if (i1 == i2) {
                    WorkTime workTime = new WorkTime();
                    // 大坑,beautil 浅拷贝问题 不能用同一个对象
                    BeanUtils.copyProperties(wk, workTime);
                    workTime.setDay(today.getTime());
                    workTimes.add(workTime);
                }
            });
            // 如果上面加过了就不加了
//            if (workTime.getDay() == null) {
//                workTime.setDay(today.getTime());
//                workTimes.add(workTime);
//            }

        }
//        // 下个周日
        today.add(Calendar.DATE, 7);
//        for (int i = 1; i <= 7; i++) {
//            WorkTime workTime = new WorkTime();
//            workTime.setWorkTime(0L);  // 设置工作时间为 0
//            Optional<WorkTime> optionalWorkTime = workTimeList.stream().filter(w -> simpleDateFormat.format(w.getDay()).equals(simpleDateFormat.format(today.getTime()))).findFirst();
//            if (optionalWorkTime.isPresent()) { // 如果当天有记录，则复制并更新日期，添加到结果集中
//                WorkTime original = optionalWorkTime.get();
//                BeanUtils.copyProperties(original, workTime);
//                workTime.setDay(today.getTime());
//                workTimes.add(workTime);
//            } else { // 如果当天没有记录，则将工作时间设为 0，设置日期，并添加到结果集中
//                workTime.setDay(today.getTime());
//                workTimes.add(workTime);
//            }
//            today.add(Calendar.DATE, 1); // 设置为下一个日期
//        }
        for (int i = 0; i < 7; i++) {
            System.out.println("下一个周:"+today.get(Calendar.DAY_OF_WEEK));
            today.add(Calendar.DATE, 1);

            collect.stream().forEach(wk -> {
                Calendar instance = Calendar.getInstance();
                instance.setTime(wk.getDay());
                // 获取星期几
                int i1 = instance.get(Calendar.DAY_OF_WEEK);
                instance.setTime(today.getTime());
                // 获取星期几
                int i2 = instance.get(Calendar.DAY_OF_WEEK);
                if (i1 == i2) {
                    WorkTime workTime = new WorkTime();
                    BeanUtils.copyProperties(wk, workTime);
                    workTime.setDay(today.getTime());
                    workTimes.add(workTime);
                }
            });
            // 如果上面加过了就不加了
//            if (workTime.getDay() == null) {
//                workTime.setDay(today.getTime());
//                workTimes.add(workTime);
//            }

        }

        workTimes.addAll(collect);
        return workTimes;
    }

    /**
     * 查询历史预约挂号列表
     *
     * @param bookRegistration 预约挂号
     * @return 预约挂号
     */
    @Override
    public List<BookRegistration> selectNowBookRegistrationList(BookRegistration bookRegistration) throws Exception {
        // 设置用户id根据用户id查历史
        SysUser user = SecurityUtils.getLoginUser().getUser();
        String phone = user.getPhonenumber();
        String encrypt = AESUtil.encrypt(phone, AesKeyUtil.getEncryptKey());
        Patient patient = patientMapper.findByPhone(encrypt);
        bookRegistration.setPatientId(patient.getPatientId());
        List<BookRegistration> bookRegistrations = bookRegistrationMapper.selectBookRegistrationList(bookRegistration);
        List<BookRegistration> collect = bookRegistrations.stream().map(p -> {
            try {
                p.setDoctorName(AESUtil.decrypt(p.getDoctorName(),AesKeyUtil.getEncryptKey()));
                p.setPatientName(AESUtil.decrypt(p.getPatientName(),AesKeyUtil.getEncryptKey()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return p;
        }).collect(Collectors.toList());
        return collect;
    }

}
