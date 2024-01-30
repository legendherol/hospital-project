package com.ruoyi.hospital.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.hospital.domain.BookRegistration;
import com.ruoyi.hospital.domain.Doctor;
import com.ruoyi.hospital.domain.Patient;
import com.ruoyi.hospital.mapper.BookRegistrationMapper;
import com.ruoyi.hospital.mapper.DoctorMapper;
import com.ruoyi.hospital.mapper.PatientMapper;
import com.ruoyi.hospital.util.AESUtil;
import com.ruoyi.hospital.util.AesKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.hospital.mapper.CaseResultMapper;
import com.ruoyi.hospital.domain.CaseResult;
import com.ruoyi.hospital.service.ICaseResultService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 门诊Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-06-02
 */
@Service
@Transactional
public class CaseResultServiceImpl implements ICaseResultService 
{
    @Autowired
    private CaseResultMapper caseResultMapper;
    @Autowired
    private BookRegistrationMapper bookRegistrationMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private PatientMapper patientMapper;

    /**
     * 查询门诊
     * 
     * @param caseHistoryId 门诊主键
     * @return 门诊
     */
    @Override
    public CaseResult selectCaseResultByCaseHistoryId(Long caseHistoryId) throws Exception {
        CaseResult caseResult = caseResultMapper.selectCaseResultByCaseHistoryId(caseHistoryId);
        caseResult.setPatienName(AESUtil.decrypt(caseResult.getPatienName(),AesKeyUtil.getEncryptKey()));
        caseResult.setDoctorName(AESUtil.decrypt(caseResult.getDoctorName(),AesKeyUtil.getEncryptKey()));
        caseResult.setPreviousHistory(AESUtil.decrypt(caseResult.getPreviousHistory(),AesKeyUtil.getEncryptKey()));
        caseResult.setCaseResult(AESUtil.decrypt(caseResult.getCaseResult(),AesKeyUtil.getEncryptKey()));
        caseResult.setPrescriptionDrug(AESUtil.decrypt(caseResult.getPrescriptionDrug(),AesKeyUtil.getEncryptKey()));
        caseResult.setAllergyHistory(AESUtil.decrypt(caseResult.getAllergyHistory(),AesKeyUtil.getEncryptKey()));
        caseResult.setCaseResult(AESUtil.decrypt(caseResult.getCaseResult(),AesKeyUtil.getEncryptKey()));
        caseResult.setCaseAdvice(AESUtil.decrypt(caseResult.getCaseAdvice(),AesKeyUtil.getEncryptKey()));
        return caseResult;
    }

    /**
     * 查询门诊列表
     * 
     * @param caseResult 门诊
     * @return 门诊
     */
    @Override
    public List<CaseResult> selectCaseResultList(CaseResult caseResult)
    {
        List<CaseResult> caseResults = caseResultMapper.selectCaseResultList(caseResult);
        List<CaseResult> collect = caseResults.stream().map(p -> {
            try {
                p.setPatienName(AESUtil.decrypt(p.getPatienName(),AesKeyUtil.getEncryptKey()));
                p.setDoctorName(AESUtil.decrypt(p.getDoctorName(),AesKeyUtil.getEncryptKey()));
                p.setPreviousHistory(AESUtil.decrypt(p.getPreviousHistory(),AesKeyUtil.getEncryptKey()));
                p.setCaseResult(AESUtil.decrypt(p.getCaseResult(),AesKeyUtil.getEncryptKey()));
                p.setPrescriptionDrug(AESUtil.decrypt(p.getPrescriptionDrug(),AesKeyUtil.getEncryptKey()));
                p.setAllergyHistory(AESUtil.decrypt(p.getAllergyHistory(),AesKeyUtil.getEncryptKey()));
                p.setCaseResult(AESUtil.decrypt(p.getCaseResult(),AesKeyUtil.getEncryptKey()));
                p.setCaseAdvice(AESUtil.decrypt(p.getCaseAdvice(),AesKeyUtil.getEncryptKey()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            return p;
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * 新增门诊
     * 
     * @param caseResult 门诊
     * @return 结果
     */
    @Override
    public int insertCaseResult(CaseResult caseResult) throws Exception {
        // 为了做校验医生重复点
        BookRegistration bookRegistration=bookRegistrationMapper.selectPatientByTreat(caseResult.getTreatmentNum());
        //
        caseResult.setPatientId(bookRegistration.getPatientId());
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        String username = sysUser.getUserName();
        caseResult.setDoctorName(username);

        String phonenumber = sysUser.getPhonenumber();
        String encrypt = AESUtil.encrypt(phonenumber, AesKeyUtil.getEncryptKey());
        // 设置医生id
        Doctor doctor = doctorMapper.findDocByPhone(encrypt);
        caseResult.setUserId(doctor.getId());
        bookRegistration.setPatientName(AESUtil.decrypt(bookRegistration.getPatientName(), AesKeyUtil.getEncryptKey()));
        bookRegistration.setDoctorName(AESUtil.decrypt(bookRegistration.getDoctorName(),AesKeyUtil.getEncryptKey()));
        caseResult.setPatienName(bookRegistration.getPatientName());

        // 判断redis30分钟内是否有人重复点
        CaseResult caseResultRe = (CaseResult)redisCache.getCacheObject("doctor");
        if (caseResultRe!=null){
            if (caseResultRe.getPatientId().equals(caseResult.getPatientId()) && caseResult.getUserId().equals(caseResultRe.getUserId())){
                throw new ServiceException("请不要在短时间内重复诊断同一个患者");
            }
        }
        // 更新此患者实际诊断时间
        Date date = new Date(); // 创建当前时间的 Date 对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
        String formattedDate = sdf.format(date); // 将 Date 对象转换为指定格式的字符串
        caseResult.setRemark("确认为医生姓名"+caseResult.getDoctorName()+"，用户"+caseResult.getPatienName()+"就诊，时间为"+formattedDate);
        BookRegistration bookRegistration1 = new BookRegistration();
        bookRegistration1.setRegistrationId(bookRegistration.getRegistrationId());
        bookRegistration1.setTreatmentTime(new Date());
        bookRegistration1.setPatientName(bookRegistration.getPatientName());
        bookRegistration1.setDoctorName(bookRegistration.getDoctorName());
        bookRegistrationMapper.updateBookRegistration(bookRegistration1);
        // TODO
        redisCache.setCacheObject("doctor",caseResult,3, TimeUnit.DAYS);
        return caseResultMapper.insertCaseResult(caseResult);
    }

    /**
     * 修改门诊
     * 
     * @param caseResult 门诊
     * @return 结果
     */
    @Override
    public int updateCaseResult(CaseResult caseResult)
    {
        return caseResultMapper.updateCaseResult(caseResult);
    }

    /**
     * 批量删除门诊
     * 
     * @param caseHistoryIds 需要删除的门诊主键
     * @return 结果
     */
    @Override
    public int deleteCaseResultByCaseHistoryIds(Long[] caseHistoryIds)
    {
        return caseResultMapper.deleteCaseResultByCaseHistoryIds(caseHistoryIds);
    }

    /**
     * 删除门诊信息
     * 
     * @param caseHistoryId 门诊主键
     * @return 结果
     */
    @Override
    public int deleteCaseResultByCaseHistoryId(Long caseHistoryId)
    {
        return caseResultMapper.deleteCaseResultByCaseHistoryId(caseHistoryId);
    }

    @Override
    public List<BookRegistration> getBookList() throws Exception {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        String phone = user.getPhonenumber();
        String encrypt = AESUtil.encrypt(phone, AesKeyUtil.getEncryptKey());
        Doctor doctor = doctorMapper.findDocByPhone(encrypt);
        List<BookRegistration> bookList = bookRegistrationMapper.getBookList(doctor.getId());
        List<BookRegistration> collect = bookList.stream().map(p -> {
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

    @Override
    public List<CaseResult> selectCaseResultListNow(CaseResult caseResult) throws Exception {
        // 设置用户id根据用户id查历史
        SysUser user = SecurityUtils.getLoginUser().getUser();
        String phone = user.getPhonenumber();
        String encrypt = AESUtil.encrypt(phone, AesKeyUtil.getEncryptKey());
        Patient patient = patientMapper.findByPhone(encrypt);
        caseResult.setPatientId(patient.getPatientId());
        List<CaseResult> caseResults = caseResultMapper.selectCaseResultList(caseResult);
        List<CaseResult> collect = caseResults.stream().map(p -> {
            try {
                p.setPatienName(AESUtil.decrypt(p.getPatienName(),AesKeyUtil.getEncryptKey()));
                p.setDoctorName(AESUtil.decrypt(p.getDoctorName(),AesKeyUtil.getEncryptKey()));
                p.setChiefComplaint(AESUtil.decrypt(p.getChiefComplaint(),AesKeyUtil.getEncryptKey()));
                p.setPreviousHistory(AESUtil.decrypt(p.getPreviousHistory(),AesKeyUtil.getEncryptKey()));
                p.setPrescriptionDrug(AESUtil.decrypt(p.getPrescriptionDrug(),AesKeyUtil.getEncryptKey()));
                p.setAllergyHistory(AESUtil.decrypt(p.getAllergyHistory(),AesKeyUtil.getEncryptKey()));
                p.setCaseResult(AESUtil.decrypt(p.getCaseResult(),AesKeyUtil.getEncryptKey()));
                p.setCaseAdvice(AESUtil.decrypt(p.getCaseAdvice(),AesKeyUtil.getEncryptKey()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            return p;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<CaseResult> selectCaseResultDocList(CaseResult caseResult) throws Exception {
        // 设置用户id根据用户id查历史
        SysUser user = SecurityUtils.getLoginUser().getUser();
        String phone = user.getPhonenumber();
        String encrypt = AESUtil.encrypt(phone, AesKeyUtil.getEncryptKey());
        Doctor docByPhone = doctorMapper.findDocByPhone(encrypt);
        caseResult.setCreateUserId(docByPhone.getId());
        List<CaseResult> caseResults = caseResultMapper.selectCaseResultList(caseResult);
        List<CaseResult> collect = caseResults.stream().map(p -> {
            try {
                p.setPatienName(AESUtil.decrypt(p.getPatienName(),AesKeyUtil.getEncryptKey()));
                p.setDoctorName(AESUtil.decrypt(p.getDoctorName(),AesKeyUtil.getEncryptKey()));
                p.setChiefComplaint(AESUtil.decrypt(p.getChiefComplaint(),AesKeyUtil.getEncryptKey()));
                p.setPreviousHistory(AESUtil.decrypt(p.getPreviousHistory(),AesKeyUtil.getEncryptKey()));
                p.setPrescriptionDrug(AESUtil.decrypt(p.getPrescriptionDrug(),AesKeyUtil.getEncryptKey()));
                p.setAllergyHistory(AESUtil.decrypt(p.getAllergyHistory(),AesKeyUtil.getEncryptKey()));
                p.setCaseResult(AESUtil.decrypt(p.getCaseResult(),AesKeyUtil.getEncryptKey()));
                p.setCaseAdvice(AESUtil.decrypt(p.getCaseAdvice(),AesKeyUtil.getEncryptKey()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            return p;
        }).collect(Collectors.toList());
        return collect;
    }
}
