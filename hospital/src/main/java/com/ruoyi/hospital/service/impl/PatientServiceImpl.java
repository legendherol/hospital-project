package com.ruoyi.hospital.service.impl;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.hospital.domain.AesData;
import com.ruoyi.hospital.domain.KeyUpLog;
import com.ruoyi.hospital.domain.UserRsaKey;
import com.ruoyi.hospital.mapper.AesDataMapper;
import com.ruoyi.hospital.mapper.KeyUpLogMapper;
import com.ruoyi.hospital.mapper.PatientMapper;
import com.ruoyi.hospital.mapper.UserRsaKeyMapper;
import com.ruoyi.hospital.util.AESUtil;
import com.ruoyi.hospital.util.AesKeyUtil;
import com.ruoyi.hospital.util.RSAUtil;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.mapper.SysUserRoleMapper;
import com.ruoyi.system.service.ISysUserService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.hospital.domain.Patient;
import com.ruoyi.hospital.service.IPatientService;
import org.springframework.transaction.annotation.Transactional;
import sun.security.krb5.internal.PAData;

/**
 * 患者管理Service业务层处理
 * 
 * @author liu
 * @date 2023-06-01
 */
@Service
@Transactional
public class PatientServiceImpl implements IPatientService 
{
    @Autowired
    private PatientMapper patientMapper;
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
     * 查询患者管理
     * 
     * @param patientId 患者管理主键
     * @return 患者管理
     */
    @Override
    public Patient selectPatientByPatientId(Long patientId) throws Exception {
        Patient patient = patientMapper.selectPatientByPatientId(patientId);
        String patientName = patient.getPatientName();
        String decrypt = AESUtil.decrypt(patientName, AesKeyUtil.getEncryptKey());
        patient.setPatientName(decrypt);
        patient.setIdCard(AESUtil.decrypt(patient.getIdCard(),AesKeyUtil.getEncryptKey()));
        patient.setAddress(AESUtil.decrypt(patient.getAddress(),AesKeyUtil.getEncryptKey()));
        patient.setPhone(AESUtil.decrypt(patient.getPhone(),AesKeyUtil.getEncryptKey()));

        return patient;
    }

    /**
     * 查询患者管理列表
     * 
     * @param patient 患者管理
     * @return 患者管理
     */
    @Override
    public List<Patient> selectPatientList(Patient patient) throws Exception {
        List<Patient> patients = patientMapper.selectPatientList(patient);
        List<Patient> collect = patients.stream().map(p -> {
            String patientName = p.getPatientName();
            String decrypt = null;
            try {
                decrypt = AESUtil.decrypt(patientName, AesKeyUtil.getEncryptKey());
                p.setPatientName(decrypt);
                p.setIdCard(AESUtil.decrypt(p.getIdCard(),AesKeyUtil.getEncryptKey()));
                p.setAddress(AESUtil.decrypt(p.getAddress(),AesKeyUtil.getEncryptKey()));
                p.setPhone(AESUtil.decrypt(p.getPhone(),AesKeyUtil.getEncryptKey()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            p.setPatientName(decrypt);
            return p;
        }).collect(Collectors.toList());

        return collect;
    }

    /**
     * 新增患者管理
     * 
     * @param patient 患者管理
     * @return 结果
     */
    @Override
    @Transactional
    public int insertPatient(Patient patient) throws Exception {

        if (!isValidID(patient.getIdCard())) {
            throw new ServiceException("身份证格式错误");
        }

        if (!validatePhoneNumber(patient.getPhone())){
            throw new ServiceException("电话号码格式错误");
        }

        List<Patient> patients = patientMapper.selectPatientList(new Patient());
        if (patients!=null && patients.size()!=0) {
            List<String> collect1 = patients.stream().map(Patient::getUserId).collect(Collectors.toList());

            if (collect1.contains(patient.getUserId())) {
                throw new ServiceException("用户id已存在");
            }
            List<String> collect = patients.stream().map(Patient::getIdCard).collect(Collectors.toList());
            if (collect.contains(patient.getIdCard())) {
                throw new ServiceException("身份证已存在");
            }

        }





        // 系统验证
        SysUser user = new SysUser();
        user.setUserName(patient.getPatientName());
        user.setPhonenumber(patient.getPhone());
        user.setNickName(patient.getUserId());
        Long[] roleIds={3L};
        user.setRoleIds(roleIds);
        user.setPassword(SecurityUtils.encryptPassword(patient.getIdCard().substring(patient.getIdCard().length() - 6)));

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
        String userId = patient.getUserId();
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

//        patient.setPatientName(AESUtil.encrypt(patient.getPatientName(),AesKeyUtil.getEncryptKey()));
//        patient.setIdCard(AESUtil.encrypt(patient.getIdCard(),AesKeyUtil.getEncryptKey()));
//        patient.setAESUtil.encrypt(patient.getPhone(),AesKeyUtil.getEncryptKey());
//        AESUtil.encrypt(patient.getAddress(),AesKeyUtil.getEncryptKey());
        return patientMapper.insertPatient(patient);
    }

    /**
     * 修改患者管理
     * 
     * @param patient 患者管理
     * @return 结果
     */
    @Override
    public int updatePatient(Patient patient)
    {

        String userId = patient.getUserId();
        SysUser sysUser = sysUserMapper.findByNickName(userId);
        sysUser.setUserName(patient.getPatientName());
        sysUser.setPhonenumber(patient.getPhone());
        if (StringUtils.isNotEmpty(sysUser.getPhonenumber()) && !userService.checkPhoneUnique(sysUser))
        {
            throw new ServiceException("更新用户'" + sysUser.getUserName() + "'失败，手机号码已存在");
        }

        sysUserMapper.updateUser(sysUser);
        return patientMapper.updatePatient(patient);
    }

    /**
     * 批量删除患者管理
     * 
     * @param patientIds 需要删除的患者管理主键
     * @return 结果
     */
    @Override
    public int deletePatientByPatientIds(Long[] patientIds) throws Exception {

        Patient patient = selectPatientByPatientId(patientIds[0]);
        SysUser sysUser = sysUserMapper.findByPhone(patient.getPhone());
        // 删除用户与角色关联
        sysUserRoleMapper.deleteUserRoleByUserId(sysUser.getUserId());
        sysUserMapper.deleteUserById(sysUser.getUserId());
        // 删除公钥私钥
        userRsaKeyMapper.delByUserId(patient.getUserId());
        // 删除aes
        aesDataMapper.delByUserId(patient.getUserId());
        return patientMapper.deletePatientByPatientIds(patientIds);
    }

    /**
     * 删除患者管理信息
     * 
     * @param patientId 患者管理主键
     * @return 结果
     */
    @Override
    public int deletePatientByPatientId(Long patientId)
    {

        // 删除用户表
        return patientMapper.deletePatientByPatientId(patientId);
    }

    @Override
    public int updateRsa() throws Exception {

        // 更新到新的密钥对
        String publicKey = RSAUtil.getPublicKey();
        String privateKey = RSAUtil.getPrivateKey();
        // 获取登录用户
        SysUser user = SecurityUtils.getLoginUser().getUser();
        String phonenumber = user.getPhonenumber();
        String encrypt = AESUtil.encrypt(phonenumber, AesKeyUtil.getEncryptKey());
        Patient patient = patientMapper.findByPhone(encrypt);
        String patientName = patient.getPatientName();
        String decrypt = AESUtil.decrypt(patientName, AesKeyUtil.getEncryptKey());
        patient.setPatientName(decrypt);
        String userId = patient.getUserId();
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
        keyUpLog.setName(patient.getPatientName());
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

    @Override
    public Patient findByUserId() throws Exception {
        // 获取登录用户
        SysUser user = SecurityUtils.getLoginUser().getUser();
        String phonenumber = user.getPhonenumber();
        String encrypt = AESUtil.encrypt(phonenumber, AesKeyUtil.getEncryptKey());
        Patient patient = patientMapper.findByPhone(encrypt);
        String patientName = patient.getPatientName();
        String decrypt = null;
        try {
            decrypt = AESUtil.decrypt(patientName, AesKeyUtil.getEncryptKey());
            patient.setIdCard(AESUtil.decrypt(patient.getIdCard(),AesKeyUtil.getEncryptKey()));
            patient.setPhone(AESUtil.decrypt(patient.getPhone(),AesKeyUtil.getEncryptKey()));
            patient.setAddress(AESUtil.decrypt(patient.getAddress(),AesKeyUtil.getEncryptKey()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        patient.setPatientName(decrypt);
        return patient;
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
