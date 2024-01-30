package com.ruoyi.hospital.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.hospital.domain.*;
import com.ruoyi.hospital.mapper.AesDataMapper;
import com.ruoyi.hospital.mapper.DoctorMapper;
import com.ruoyi.hospital.mapper.KeyUpLogMapper;
import com.ruoyi.hospital.mapper.UserRsaKeyMapper;
import com.ruoyi.hospital.service.IDoctorService;
import com.ruoyi.hospital.util.AESUtil;
import com.ruoyi.hospital.util.AesKeyUtil;
import com.ruoyi.hospital.util.RSAUtil;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.mapper.SysUserRoleMapper;
import com.ruoyi.system.service.ISysUserService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * doctorService业务层处理
 * 
 * @author ruoyi
 * @date 2023-06-02
 */
@Service
@Transactional
public class DoctorServiceImpl implements IDoctorService
{
    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private UserRsaKeyMapper userRsaKeyMapper;
    @Autowired
    private AesDataMapper aesDataMapper;
    @Autowired
    private KeyUpLogMapper keyUpLogMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private ISysUserService userService;
    /**
     * 查询doctor
     * 
     * @param id doctor主键
     * @return doctor
     */
    @Override
    public Doctor selectDoctorById(Long id) throws Exception {
        Doctor doctor = doctorMapper.selectDoctorById(id);
        doctor.setUsername(AESUtil.decrypt(doctor.getUsername(),AesKeyUtil.getEncryptKey()));
        doctor.setIdCard(AESUtil.decrypt(doctor.getIdCard(),AesKeyUtil.getEncryptKey()));
        doctor.setPhone(AESUtil.decrypt(doctor.getPhone(),AesKeyUtil.getEncryptKey()));

        return doctor;
    }

    /**
     * 查询doctor列表
     * 
     * @param doctor doctor
     * @return doctor
     */
    @Override
    public List<Doctor> selectDoctorList(Doctor doctor)
    {
        List<Doctor> doctors = doctorMapper.selectDoctorList(doctor);
        List<Doctor> collect = doctors.stream().map(p -> {
            String patientName = p.getUsername();
            String decrypt = null;
            try {
                decrypt = AESUtil.decrypt(patientName, AesKeyUtil.getEncryptKey());
                p.setIdCard(AESUtil.decrypt(p.getIdCard(),AesKeyUtil.getEncryptKey()));
                p.setPhone(AESUtil.decrypt(p.getPhone(),AesKeyUtil.getEncryptKey()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            p.setUsername(decrypt);
            return p;
        }).collect(Collectors.toList());

        return collect;
    }

    /**
     * 新增doctor
     * 
     * @param doctor doctor
     * @return 结果
     */
    @Override
    @Transactional
    public int insertDoctor(Doctor doctor) throws Exception {

        if (!isValidID(doctor.getIdCard())){
            throw new ServiceException("身份证格式错误");
        }

        if (!validatePhoneNumber(doctor.getPhone())){
            throw new ServiceException("电话号码格式错误");
        }


        List<Doctor> doctors = doctorMapper.selectDoctorList(new Doctor());
        List<Doctor> collect = doctors.stream().map(p -> {
            String patientName = p.getUsername();
            String decrypt = null;
            try {
                decrypt = AESUtil.decrypt(patientName, AesKeyUtil.getEncryptKey());
                p.setIdCard(AESUtil.decrypt(p.getIdCard(),AesKeyUtil.getEncryptKey()));
                p.setPhone(AESUtil.decrypt(p.getPhone(),AesKeyUtil.getEncryptKey()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            p.setUsername(decrypt);
            return p;
        }).collect(Collectors.toList());


        if (collect.size() != 0){

            List<String> collect2 = collect.stream().map(Doctor::getIdCard).collect(Collectors.toList());
            List<String> collect1 = collect.stream().map(Doctor::getUserId).collect(Collectors.toList());

            if (collect1.contains(doctor.getUserId())){
                throw new ServiceException("用户id已存在");
            }
            if (collect2.contains(doctor.getIdCard())){
                throw new ServiceException("身份证已存在");
            }


        }





        // 系统验证
        SysUser user = new SysUser();
        user.setUserName(doctor.getUsername());
        user.setPhonenumber(doctor.getPhone());
        user.setNickName(doctor.getUserId());
        Long[] roleIds={4L};
        user.setRoleIds(roleIds);
        user.setPassword(SecurityUtils.encryptPassword(doctor.getIdCard().substring(doctor.getIdCard().length() - 6)));

        // 校验
        if (!userService.checkUserNameUnique(user))
        {
            throw new ServiceException("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user))
        {
            throw new ServiceException("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        }

        // 新增用户
        sysUserMapper.insertUser(user);


        // 新增用户与角色管理
        List<SysUserRole> list = new ArrayList<SysUserRole>(roleIds.length);
        for (Long roleId : roleIds)
        {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(user.getUserId());
            ur.setRoleId(roleId);
            list.add(ur);
        }
        sysUserRoleMapper.batchUserRole(list);


        // 生成密钥对
        String publicKey = RSAUtil.getPublicKey();
        String privateKey = RSAUtil.getPrivateKey();
        String userId = doctor.getUserId();
        // 生成密钥表
        UserRsaKey userRsaKey = new UserRsaKey();
        userRsaKey.setUserId(userId);
        userRsaKey.setUserPrivateKey(privateKey);
        userRsaKey.setUserPublicKey(publicKey);
        userRsaKeyMapper.insertUserRsaKey(userRsaKey);
        // 加密后的aes密钥
        AesData aesData = new AesData();
        aesData.setUserId(userId);
        aesData.setAesTransferKey(Base64.encodeBase64String(RSAUtil.encryptByPublicKey(AesKeyUtil.getTansferKey().getBytes(), publicKey)));
        aesData.setAesKey(Base64.encodeBase64String(RSAUtil.encryptByPublicKey(AesKeyUtil.getEncryptKey().getBytes(),publicKey)));
        aesDataMapper.insertAesData(aesData);
        return doctorMapper.insertDoctor(doctor);
    }

    /**
     * 修改doctor
     * 
     * @param doctor doctor
     * @return 结果
     */
    @Override
    public int updateDoctor(Doctor doctor)
    {
        String userId = doctor.getUserId();
        SysUser sysUser = sysUserMapper.findByNickName(userId);
        sysUser.setUserName(doctor.getUsername());
        sysUser.setPhonenumber(doctor.getPhone());
        if (StringUtils.isNotEmpty(sysUser.getPhonenumber()) && !userService.checkPhoneUnique(sysUser))
        {
            throw new ServiceException("新增用户'" + sysUser.getUserName() + "'失败，手机号码已存在");
        }

        sysUserMapper.updateUser(sysUser);
        return doctorMapper.updateDoctor(doctor);
    }

    /**
     * 批量删除doctor
     * 
     * @param ids 需要删除的doctor主键
     * @return 结果
     */
    @Override
    public int deleteDoctorByIds(Long[] ids) throws Exception {
        Doctor doctor = selectDoctorById(ids[0]);
        String phonenumber = doctor.getPhone();
        String encrypt = AESUtil.encrypt(phonenumber, AesKeyUtil.getEncryptKey());
        SysUser sysUser = sysUserMapper.findByPhone(encrypt);
        // 删除用户与角色关联
        sysUserRoleMapper.deleteUserRoleByUserId(sysUser.getUserId());
        sysUserMapper.deleteUserById(sysUser.getUserId());

        // 删除公钥私钥
        userRsaKeyMapper.delByUserId(doctor.getUserId());
        // 删除aes
        aesDataMapper.delByUserId(doctor.getUserId());
        return doctorMapper.deleteDoctorByIds(ids);
    }

    /**
     * 删除doctor信息
     * 
     * @param id doctor主键
     * @return 结果
     */
    @Override
    public int deleteDoctorById(Long id)
    {

        return doctorMapper.deleteDoctorById(id);
    }

    @Override
    public Doctor findByUserId() throws Exception {
        // 获取登录用户
        SysUser user = SecurityUtils.getLoginUser().getUser();
        String phonenumber = user.getPhonenumber();
        String encrypt = AESUtil.encrypt(phonenumber, AesKeyUtil.getEncryptKey());
        Doctor doctor = doctorMapper.findByPhoneAndDep(encrypt);
        doctor.setUsername(AESUtil.decrypt(doctor.getUsername(),AesKeyUtil.getEncryptKey()));
        doctor.setIdCard(AESUtil.decrypt(doctor.getIdCard(),AesKeyUtil.getEncryptKey()));
        doctor.setPhone(AESUtil.decrypt(doctor.getPhone(),AesKeyUtil.getEncryptKey()));
        return doctor;
    }

    @Override
    public int updateRsa() throws Exception {
        {

            // 更新到新的密钥对
            String publicKey = RSAUtil.getPublicKey();
            String privateKey = RSAUtil.getPrivateKey();
            // 获取登录用户
            SysUser user = SecurityUtils.getLoginUser().getUser();
            String phonenumber = user.getPhonenumber();
            String encrypt = AESUtil.encrypt(phonenumber, AesKeyUtil.getEncryptKey());
            Doctor doctor = doctorMapper.findDocByPhone(encrypt);
            String userId = doctor.getUserId();
            // 获取aes表中的用户的加密后的aes
            AesData aesData = aesDataMapper.selectAesDataByUserId(userId);
            // 获取rsa密钥对
            Long id = aesData.getId();
            UserRsaKey userRsaKey = userRsaKeyMapper.selectUserRsaKeyByUserId(userId);
            String userPrivateKey = userRsaKey.getUserPrivateKey();
            String userPublicKey = userRsaKey.getUserPublicKey();
            // 解密原来当前用户公钥加密过后的aes加密和传输Key
            byte[] transferKeybytes = RSAUtil.decryptByPrivateKey(Base64.decodeBase64(aesData.getAesTransferKey()), userPrivateKey);
            byte[] aesKeyBytes = RSAUtil.decryptByPrivateKey(Base64.decodeBase64(aesData.getAesKey()), userPrivateKey);
            // 数据库操作
            KeyUpLog keyUpLog = new KeyUpLog();
            keyUpLog.setUserId(userId);
            doctor.setUsername(AESUtil.decrypt(doctor.getUsername(),AesKeyUtil.getEncryptKey()));
            keyUpLog.setName(doctor.getUsername());
            keyUpLog.setCreateTime(new Date());
            keyUpLog.setDes("("+ userPublicKey+","+userPrivateKey+")");
            keyUpLogMapper.insertKeyUpLog(keyUpLog);

            //     String encryptKey = AesKeyUtil.getEncryptKey(); TODO
//        System.out.println(new String(encryptKey));
            // 更新rsa密钥对
            UserRsaKey userRsaKey1 = new UserRsaKey();
            userRsaKey1.setUserPublicKey(publicKey);
            userRsaKey1.setUserPrivateKey(privateKey);
            userRsaKey1.setUserId(userId);
            userRsaKey1.setId(userRsaKey.getId());
            userRsaKeyMapper.updateUserRsaKey(userRsaKey1);
            // 更新aes加密后的key
            AesData aesData1 = new AesData();
            aesData1.setAesTransferKey(Base64.encodeBase64String(RSAUtil.encryptByPublicKey(transferKeybytes,publicKey)));
            aesData1.setAesKey(Base64.encodeBase64String(RSAUtil.encryptByPublicKey(aesKeyBytes,publicKey)));
            aesData1.setUserId(userId);
            aesData1.setId(id);
            return aesDataMapper.updateAesData(aesData1);
        }
    }

    public static boolean isValidID(String id) {
        // 判断身份证号码长度是否合法
        if (id.length() != 18) {
            return false;
        }
        // 判断身份证号码前17位是否全部为数字
        String id17 = id.substring(0, 17);
        if (!id17.matches("\\d+")) {
            return false;
        }
        // 判断身份证号码最后一位是否为数字或字母X
        char last = id.charAt(17);
        if (!Character.isDigit(last) && last != 'X') {
            return false;
        }
        // 根据GB11643-1999标准计算身份证号码的校验码
        int[] weight = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] digit = new char[]{'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            int num = Character.digit(id.charAt(i), 10);
            sum += num * weight[i];
        }
        int mod = sum % 11;
        char checkDigit = digit[mod];
        // 判断身份证号码校验码是否正确
        return checkDigit == last;
    }

    public boolean validatePhoneNumber(String phoneNumber) {
        // 电话号码正则表达式
        String phoneRegex = "^1[3456789]\\d{9}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
