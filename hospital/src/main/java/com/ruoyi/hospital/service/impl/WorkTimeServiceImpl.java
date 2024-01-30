package com.ruoyi.hospital.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.hospital.domain.BookRegistration;
import com.ruoyi.hospital.domain.Doctor;
import com.ruoyi.hospital.util.AESUtil;
import com.ruoyi.hospital.util.AesKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.hospital.mapper.WorkTimeMapper;
import com.ruoyi.hospital.domain.WorkTime;
import com.ruoyi.hospital.service.IWorkTimeService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 放号Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-06-02
 */
@Service
@Transactional
public class WorkTimeServiceImpl implements IWorkTimeService 
{
    @Autowired
    private WorkTimeMapper workTimeMapper;

    /**
     * 查询放号
     * 
     * @param id 放号主键
     * @return 放号
     */
    @Override
    public WorkTime selectWorkTimeById(Long id)
    {
        return workTimeMapper.selectWorkTimeById(id);
    }

    /**
     * 查询放号列表
     * 
     * @param workTime 放号
     * @return 放号
     */
    @Override
    public List<WorkTime> selectWorkTimeList(WorkTime workTime)
    {
        List<WorkTime> workTimes = workTimeMapper.selectWorkTimeList(workTime);
        List<WorkTime> collect = workTimes.stream().map(p -> {
            try {
                Doctor doctor = p.getDoctor();
                String decrypt = AESUtil.decrypt(doctor.getUsername(), AesKeyUtil.getEncryptKey());
                doctor.setUsername(decrypt);
                p.setDoctor(doctor);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return p;
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * 新增放号
     * 
     * @param workTime 放号
     * @return 结果
     */
    @Override
    public int insertWorkTime(WorkTime workTime)
    {
        return workTimeMapper.insertWorkTime(workTime);
    }

    /**
     * 修改放号
     * 
     * @param workTime 放号
     * @return 结果
     */
    @Override
    public int updateWorkTime(WorkTime workTime)
    {
        return workTimeMapper.updateWorkTime(workTime);
    }

    /**
     * 批量删除放号
     * 
     * @param ids 需要删除的放号主键
     * @return 结果
     */
    @Override
    public int deleteWorkTimeByIds(Long[] ids)
    {
        return workTimeMapper.deleteWorkTimeByIds(ids);
    }

    /**
     * 删除放号信息
     * 
     * @param id 放号主键
     * @return 结果
     */
    @Override
    public int deleteWorkTimeById(Long id)
    {
        return workTimeMapper.deleteWorkTimeById(id);
    }
}
