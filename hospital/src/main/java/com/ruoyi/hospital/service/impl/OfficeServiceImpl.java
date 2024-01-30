package com.ruoyi.hospital.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.hospital.mapper.OfficeMapper;
import com.ruoyi.hospital.domain.Office;
import com.ruoyi.hospital.service.IOfficeService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 科室Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-06-02
 */
@Service
@Transactional
public class OfficeServiceImpl implements IOfficeService 
{
    @Autowired
    private OfficeMapper officeMapper;

    /**
     * 查询科室
     * 
     * @param officeId 科室主键
     * @return 科室
     */
    @Override
    public Office selectOfficeByOfficeId(Long officeId)
    {
        return officeMapper.selectOfficeByOfficeId(officeId);
    }

    /**
     * 查询科室列表
     * 
     * @param office 科室
     * @return 科室
     */
    @Override
    public List<Office> selectOfficeList(Office office)
    {
        return officeMapper.selectOfficeList(office);
    }

    /**
     * 新增科室
     * 
     * @param office 科室
     * @return 结果
     */
    @Override
    public int insertOffice(Office office)
    {
        return officeMapper.insertOffice(office);
    }

    /**
     * 修改科室
     * 
     * @param office 科室
     * @return 结果
     */
    @Override
    public int updateOffice(Office office)
    {
        return officeMapper.updateOffice(office);
    }

    /**
     * 批量删除科室
     * 
     * @param officeIds 需要删除的科室主键
     * @return 结果
     */
    @Override
    public int deleteOfficeByOfficeIds(Long[] officeIds)
    {
        return officeMapper.deleteOfficeByOfficeIds(officeIds);
    }

    /**
     * 删除科室信息
     * 
     * @param officeId 科室主键
     * @return 结果
     */
    @Override
    public int deleteOfficeByOfficeId(Long officeId)
    {
        return officeMapper.deleteOfficeByOfficeId(officeId);
    }
}
