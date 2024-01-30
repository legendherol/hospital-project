package com.ruoyi.hospital.service;

import java.io.IOException;
import java.util.List;
import com.ruoyi.hospital.domain.Patient;

/**
 * 患者管理Service接口
 * 
 * @author liu
 * @date 2023-06-01
 */
public interface IPatientService 
{
    /**
     * 查询患者管理
     * 
     * @param patientId 患者管理主键
     * @return 患者管理
     */
    public Patient selectPatientByPatientId(Long patientId) throws Exception;

    /**
     * 查询患者管理列表
     * 
     * @param patient 患者管理
     * @return 患者管理集合
     */
    public List<Patient> selectPatientList(Patient patient) throws Exception;

    /**
     * 新增患者管理
     * 
     * @param patient 患者管理
     * @return 结果
     */
    public int insertPatient(Patient patient) throws Exception;

    /**
     * 修改患者管理
     * 
     * @param patient 患者管理
     * @return 结果
     */
    public int updatePatient(Patient patient);

    /**
     * 批量删除患者管理
     * 
     * @param patientIds 需要删除的患者管理主键集合
     * @return 结果
     */
    public int deletePatientByPatientIds(Long[] patientIds) throws Exception;

    /**
     * 删除患者管理信息
     * 
     * @param patientId 患者管理主键
     * @return 结果
     */
    public int deletePatientByPatientId(Long patientId);

    int updateRsa() throws Exception;

    Patient findByUserId() throws Exception;
}
