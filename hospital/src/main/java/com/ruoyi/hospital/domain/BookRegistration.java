package com.ruoyi.hospital.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.hospital.anno.EncryptInter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 book_registration
 *
 * @author ruoyi
 * @date 2023-06-05
 */
public class BookRegistration extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 挂号id */
    private Long registrationId;

    /** 患者id */
    @Excel(name = "患者id")
    private Long patientId;

    /** 患者姓名 */
    @Excel(name = "患者姓名")
    @EncryptInter
    private String patientName;

    /** 挂号医生id */
    @Excel(name = "挂号医生id")
    private Long doctorId;

    private String doctorUserId;

    public String getDoctorUserId() {
        return doctorUserId;
    }

    public void setDoctorUserId(String doctorUserId) {
        this.doctorUserId = doctorUserId;
    }

    /** 就诊时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "就诊时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date treatmentTime;

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date day;

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    /** 挂号医生姓名 */
    @Excel(name = "挂号医生姓名")
    @EncryptInter
    private String doctorName;

    /** 就诊号 */
    @Excel(name = "就诊号")
    private String treatmentNum;

    private Patient patient;

    private Integer workTime;

    public Integer getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Integer workTime) {
        this.workTime = workTime;
    }

    public Boolean getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(Boolean showFlag) {
        this.showFlag = showFlag;
    }

    private Boolean showFlag = false;





    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setRegistrationId(Long registrationId)
    {
        this.registrationId = registrationId;
    }

    public Long getRegistrationId()
    {
        return registrationId;
    }
    public void setPatientId(Long patientId)
    {
        this.patientId = patientId;
    }

    public Long getPatientId()
    {
        return patientId;
    }
    public void setPatientName(String patientName)
    {
        this.patientName = patientName;
    }

    public String getPatientName()
    {
        return patientName;
    }
    public void setDoctorId(Long doctorId)
    {
        this.doctorId = doctorId;
    }

    public Long getDoctorId()
    {
        return doctorId;
    }
    public void setTreatmentTime(Date treatmentTime)
    {
        this.treatmentTime = treatmentTime;
    }

    public Date getTreatmentTime()
    {
        return treatmentTime;
    }
    public void setDoctorName(String doctorName)
    {
        this.doctorName = doctorName;
    }

    public String getDoctorName()
    {
        return doctorName;
    }
    public void setTreatmentNum(String treatmentNum)
    {
        this.treatmentNum = treatmentNum;
    }

    public String getTreatmentNum()
    {
        return treatmentNum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("registrationId", getRegistrationId())
                .append("patientId", getPatientId())
                .append("patientName", getPatientName())
                .append("doctorId", getDoctorId())
                .append("createTime", getCreateTime())
                .append("treatmentTime", getTreatmentTime())
                .append("doctorName", getDoctorName())
                .append("treatmentNum", getTreatmentNum())
                .toString();
    }
}
