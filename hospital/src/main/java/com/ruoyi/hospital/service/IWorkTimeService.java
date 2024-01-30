package com.ruoyi.hospital.service;

import java.util.List;
import com.ruoyi.hospital.domain.WorkTime;

/**
 * 放号Service接口
 * 
 * @author ruoyi
 * @date 2023-06-02
 */
public interface IWorkTimeService 
{
    /**
     * 查询放号
     * 
     * @param id 放号主键
     * @return 放号
     */
    public WorkTime selectWorkTimeById(Long id);

    /**
     * 查询放号列表
     * 
     * @param workTime 放号
     * @return 放号集合
     */
    public List<WorkTime> selectWorkTimeList(WorkTime workTime);

    /**
     * 新增放号
     * 
     * @param workTime 放号
     * @return 结果
     */
    public int insertWorkTime(WorkTime workTime);

    /**
     * 修改放号
     * 
     * @param workTime 放号
     * @return 结果
     */
    public int updateWorkTime(WorkTime workTime);

    /**
     * 批量删除放号
     * 
     * @param ids 需要删除的放号主键集合
     * @return 结果
     */
    public int deleteWorkTimeByIds(Long[] ids);

    /**
     * 删除放号信息
     * 
     * @param id 放号主键
     * @return 结果
     */
    public int deleteWorkTimeById(Long id);
}
