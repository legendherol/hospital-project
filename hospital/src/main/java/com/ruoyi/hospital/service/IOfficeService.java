package com.ruoyi.hospital.service;

import java.util.List;
import com.ruoyi.hospital.domain.Office;

/**
 * 科室Service接口
 * 
 * @author ruoyi
 * @date 2023-06-02
 */
public interface IOfficeService 
{
    /**
     * 查询科室
     * 
     * @param officeId 科室主键
     * @return 科室
     */
    public Office selectOfficeByOfficeId(Long officeId);

    /**
     * 查询科室列表
     * 
     * @param office 科室
     * @return 科室集合
     */
    public List<Office> selectOfficeList(Office office);

    /**
     * 新增科室
     * 
     * @param office 科室
     * @return 结果
     */
    public int insertOffice(Office office);

    /**
     * 修改科室
     * 
     * @param office 科室
     * @return 结果
     */
    public int updateOffice(Office office);

    /**
     * 批量删除科室
     * 
     * @param officeIds 需要删除的科室主键集合
     * @return 结果
     */
    public int deleteOfficeByOfficeIds(Long[] officeIds);

    /**
     * 删除科室信息
     * 
     * @param officeId 科室主键
     * @return 结果
     */
    public int deleteOfficeByOfficeId(Long officeId);
}
