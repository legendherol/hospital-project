package com.ruoyi.hospital.mapper;

import java.util.List;
import com.ruoyi.hospital.domain.UserRsaKey;

/**
 * rsaMapper接口
 * 
 * @author ruoyi
 * @date 2023-06-07
 */
public interface UserRsaKeyMapper 
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
     * 删除rsa
     * 
     * @param id rsa主键
     * @return 结果
     */
    public int deleteUserRsaKeyById(Long id);

    /**
     * 批量删除rsa
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserRsaKeyByIds(Long[] ids);

    UserRsaKey selectUserRsaKeyByUserId(String userId);

    int delByUserId(String userId);
}
