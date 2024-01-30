package com.ruoyi.hospital.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.hospital.domain.*;
import com.ruoyi.hospital.mapper.AesDataMapper;
import com.ruoyi.hospital.mapper.DoctorMapper;
import com.ruoyi.hospital.mapper.PatientMapper;
import com.ruoyi.hospital.util.AESUtil;
import com.ruoyi.hospital.util.AesKeyUtil;
import com.ruoyi.hospital.util.RSAUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.hospital.mapper.UserRsaKeyMapper;
import com.ruoyi.hospital.service.IUserRsaKeyService;
import org.springframework.transaction.annotation.Transactional;

/**
 * rsaService业务层处理
 *
 * @author ruoyi
 * @date 2023-06-07
 */
@Service
@Transactional
public class UserRsaKeyServiceImpl implements IUserRsaKeyService {
    @Autowired
    private UserRsaKeyMapper userRsaKeyMapper;
    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private AesDataMapper aesDataMapper;
    @Autowired
    private PatientMapper patientMapper;

    /**
     * 查询rsa
     *
     * @param id rsa主键
     * @return rsa
     */
    @Override
    public UserRsaKey selectUserRsaKeyById(Long id) {
        return userRsaKeyMapper.selectUserRsaKeyById(id);
    }

    /**
     * 查询rsa列表
     *
     * @param userRsaKey rsa
     * @return rsa
     */
    @Override
    public List<UserRsaKey> selectUserRsaKeyList(UserRsaKey userRsaKey) {
        return userRsaKeyMapper.selectUserRsaKeyList(userRsaKey);
    }

    /**
     * 新增rsa
     *
     * @param userRsaKey rsa
     * @return 结果
     */
    @Override
    public int insertUserRsaKey(UserRsaKey userRsaKey) {
        return userRsaKeyMapper.insertUserRsaKey(userRsaKey);
    }

    /**
     * 修改rsa
     *
     * @param userRsaKey rsa
     * @return 结果
     */
    @Override
    public int updateUserRsaKey(UserRsaKey userRsaKey) {
        return userRsaKeyMapper.updateUserRsaKey(userRsaKey);
    }

    /**
     * 批量删除rsa
     *
     * @param ids 需要删除的rsa主键
     * @return 结果
     */
    @Override
    public int deleteUserRsaKeyByIds(Long[] ids) {
        return userRsaKeyMapper.deleteUserRsaKeyByIds(ids);
    }

    /**
     * 删除rsa信息
     *
     * @param id rsa主键
     * @return 结果
     */
    @Override
    public int deleteUserRsaKeyById(Long id) {
        return userRsaKeyMapper.deleteUserRsaKeyById(id);
    }

    @Override
    public AesRsaKeyDto getPrivateKey() throws Exception {
        // 获取登录用户
        SysUser user = SecurityUtils.getLoginUser().getUser();
        String phonenumber = user.getPhonenumber();
        String encrypt = AESUtil.encrypt(phonenumber, AesKeyUtil.getEncryptKey());
        Doctor docByPhone = doctorMapper.findDocByPhone(encrypt);
        Patient patient = patientMapper.findByPhone(encrypt);

        String userId = null;
        if (docByPhone != null) {
            userId = docByPhone.getUserId();
        } else {
            userId = patient.getUserId();
        }
        // 获取用户私钥和aes传输密钥
        UserRsaKey userRsaKey = userRsaKeyMapper.selectUserRsaKeyByUserId(userId);
        String userPrivateKey = userRsaKey.getUserPrivateKey();
        AesData aesData = aesDataMapper.selectAesDataByUserId(userId);
        String aesTransferKey = aesData.getAesTransferKey();
        // 解密公钥存的aeskey
        byte[] bytes = RSAUtil.decryptByPrivateKey(Base64.decodeBase64(aesTransferKey), userPrivateKey);
        return new AesRsaKeyDto(Base64.encodeBase64String(bytes), userPrivateKey);
    }
}
