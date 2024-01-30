package com.ruoyi.hospital.mapper;

import java.util.List;
import com.ruoyi.hospital.domain.Doctor;

/**
 * doctorMapper接口
 * 
 * @author ruoyi
 * @date 2023-06-02
 */
public interface DoctorMapper 
{
    /**
     * 查询doctor
     * 
     * @param id doctor主键
     * @return doctor
     */
    public Doctor selectDoctorById(Long id);

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
    public int insertDoctor(Doctor doctor);

    /**
     * 修改doctor
     * 
     * @param doctor doctor
     * @return 结果
     */
    public int updateDoctor(Doctor doctor);

    /**
     * 删除doctor
     * 
     * @param id doctor主键
     * @return 结果
     */
    public int deleteDoctorById(Long id);

    /**
     * 批量删除doctor
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDoctorByIds(Long[] ids);

    Doctor findDocByPhone(String phonenumber);

    Doctor findByPhoneAndDep(String phone);
}
