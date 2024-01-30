package com.ruoyi.hospital.service;

import java.util.List;

import com.ruoyi.hospital.domain.AesRsaKeyDto;
import com.ruoyi.hospital.domain.UserRsaKey;

/**
 * rsaService接口
 * 
 * @author ruoyi
 * @date 2023-06-07
 */
public interface IUserRsaKeyService 
{
    /**
     * 查询rsa
     * 
     * @param id rsa主键
     * @return rsa
     */
    public UserRsaKey selectUserRsaKeyById(Long id);

    /**
     * 查询rsa列表
     * 
     * @param userRsaKey rsa
     * @return rsa集合
     */
    public List<UserRsaKey> selectUserRsaKeyList(UserRsaKey userRsaKey);

    /**
     * 新增rsa
     * 
     * @param userRsaKey rsa
     * @return 结果
     */
    public int insertUserRsaKey(UserRsaKey userRsaKey);

    /**
     * 修改rsa
     * 
     * @param userRsaKey rsa
     * @return 结果
     */
    public int updateUserRsaKey(UserRsaKey userRsaKey);

    /**
     * 批量删除rsa
     * 
     * @param ids 需要删除的rsa主键集合
     * @return 结果
     */
    public int deleteUserRsaKeyByIds(Long[] ids);

    /**
     * 删除rsa信息
     * 
     * @param id rsa主键
     * @return 结果
     */
    public int deleteUserRsaKeyById(Long id);

    AesRsaKeyDto getPrivateKey() throws Exception;
}
