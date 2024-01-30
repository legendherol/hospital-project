package com.ruoyi.hospital.service;

import java.io.IOException;
import java.util.List;
import com.ruoyi.hospital.domain.Doctor;

/**
 * doctorService接口
 * 
 * @author ruoyi
 * @date 2023-06-02
 */
public interface IDoctorService 
{
    /**
     * 查询doctor
     * 
     * @param id doctor主键
     * @return doctor
     */
    public Doctor selectDoctorById(Long id) throws Exception;

    /**
     * 查询doctor列表
     * 
     * @param doctor doctor
     * @return doctor集合
     */
    public List<Doctor> selectDoctorList(Doctor doctor);

    /**
     * 新增doctor
     * 
     * @param doctor doctor
     * @return 结果
     */
    public int insertDoctor(Doctor doctor) throws Exception;

    /**
     * 修改doctor
     * 
     * @param doctor doctor
     * @return 结果
     */
    public int updateDoctor(Doctor doctor);

    /**
     * 批量删除doctor
     * 
     * @param ids 需要删除的doctor主键集合
     * @return 结果
     */
    public int deleteDoctorByIds(Long[] ids) throws Exception;

    /**
     * 删除doctor信息
     * 
     * @param id doctor主键
     * @return 结果
     */
    public int deleteDoctorById(Long id);

    Doctor findByUserId() throws Exception;

    int updateRsa() throws Exception;
}
