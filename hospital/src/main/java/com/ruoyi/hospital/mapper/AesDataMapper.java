package com.ruoyi.hospital.mapper;

import java.util.List;
import com.ruoyi.hospital.domain.AesData;

/**
 * aesMapper接口
 * 
 * @author ruoyi
 * @date 2023-06-07
 */
public interface AesDataMapper 
{
    /**
     * 查询aes
     * 
     * @param id aes主键
     * @return aes
     */
    public AesData selectAesDataById(Long id);

    /**
     * 查询aes列表
     * 
     * @param aesData aes
     * @return aes集合
     */
    public List<AesData> selectAesDataList(AesData aesData);

    /**
     * 新增aes
     * 
     * @param aesData aes
     * @return 结果
     */
    public int insertAesData(AesData aesData);

    /**
     * 修改aes
     * 
     * @param aesData aes
     * @return 结果
     */
    public int updateAesData(AesData aesData);

    /**
     * 删除aes
     * 
     * @param id aes主键
     * @return 结果
     */
    public int deleteAesDataById(Long id);

    /**
     * 批量删除aes
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAesDataByIds(Long[] ids);

    AesData selectAesDataByUserId(String userId);

    int delByUserId(String userId);
}
