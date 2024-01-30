package com.ruoyi.hospital.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.hospital.mapper.AesDataMapper;
import com.ruoyi.hospital.domain.AesData;
import com.ruoyi.hospital.service.IAesDataService;

/**
 * aesService业务层处理
 * 
 * @author ruoyi
 * @date 2023-06-07
 */
@Service
public class AesDataServiceImpl implements IAesDataService 
{
    @Autowired
    private AesDataMapper aesDataMapper;

    /**
     * 查询aes
     * 
     * @param id aes主键
     * @return aes
     */
    @Override
    public AesData selectAesDataById(Long id)
    {
        return aesDataMapper.selectAesDataById(id);
    }

    /**
     * 查询aes列表
     * 
     * @param aesData aes
     * @return aes
     */
    @Override
    public List<AesData> selectAesDataList(AesData aesData)
    {
        return aesDataMapper.selectAesDataList(aesData);
    }

    /**
     * 新增aes
     * 
     * @param aesData aes
     * @return 结果
     */
    @Override
    public int insertAesData(AesData aesData)
    {
        return aesDataMapper.insertAesData(aesData);
    }

    /**
     * 修改aes
     * 
     * @param aesData aes
     * @return 结果
     */
    @Override
    public int updateAesData(AesData aesData)
    {
        return aesDataMapper.updateAesData(aesData);
    }

    /**
     * 批量删除aes
     * 
     * @param ids 需要删除的aes主键
     * @return 结果
     */
    @Override
    public int deleteAesDataByIds(Long[] ids)
    {
        return aesDataMapper.deleteAesDataByIds(ids);
    }

    /**
     * 删除aes信息
     * 
     * @param id aes主键
     * @return 结果
     */
    @Override
    public int deleteAesDataById(Long id)
    {
        return aesDataMapper.deleteAesDataById(id);
    }
}
