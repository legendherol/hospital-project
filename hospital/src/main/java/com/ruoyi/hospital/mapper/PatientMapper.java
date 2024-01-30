package com.ruoyi.hospital.mapper;

import java.util.List;
import com.ruoyi.hospital.domain.Patient;

/**
 * 患者管理Mapper接口
 * 
 * @author liu
 * @date 2023-06-01
 */
public interface PatientMapper 
{
    /**
     * 查询患者管理
     * 
     * @param patientId 患者管理主键
     * @return 患者管理
     */
    public Patient selectPatientByPatientId(Long patientId);

    /**
     * 查询患者管理列表
     * 
     * @param patient 患者管理
     * @return 患者管理集合
     */
    public List<Patient> selectPatientList(Patient patient);

    /**
     * 新增患者管理
     * 
     * @param patient 患者管理
     * @return 结果
     */
    public int insertPatient(Patient patient);

    /**
     * 修改患者管理
     * 
     * @param patient 患者管理
     * @return 结果
     */
    public int updatePatient(Patient patient);

    /**
     * 删除患者管理
     * 
     * @param patientId 患者管理主键
     * @return 结果
     */
    public int deletePatientByPatientId(Long patientId);

    /**
     * 批量删除患者管理
     * 
     * @param patientIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePatientByPatientIds(Long[] patientIds);

    Patient findByPhone(String phone);
}
