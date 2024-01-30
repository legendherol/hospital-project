package com.ruoyi.hospital.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ruoyi.hospital.anno.EncryptInter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 诊断对象 case_result
 *
 * @author ruoyi
 * @date 2023-06-09
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaseResult extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 诊断id */
    private Long caseHistoryId;

    /** 患者id */
    @Excel(name = "患者id")
    private Long patientId;

    /** 诊断医生id */
    @Excel(name = "诊断医生id")
    private Long createUserId;

    /** 主诉 */
    @Excel(name = "主诉")
    @EncryptInter
    private String chiefComplaint;

    /** 医生用户id */
    @Excel(name = "医生用户id")
    private Long userId;

    /** 既往史 */
    @Excel(name = "既往史")
    @EncryptInter
    private String previousHistory;

    /** 过敏史 */
    @Excel(name = "过敏史")
    @EncryptInter
    private String allergyHistory;

    /** 诊断结果 */
    @Excel(name = "诊断结果")
    @EncryptInter
    private String caseResult;

    /** 诊断时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "诊断时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date caseTime;

    /** 处方 */
    @Excel(name = "处方")
    @EncryptInter
    private String prescriptionDrug;

    /** 医嘱 */
    @Excel(name = "医嘱")
    @EncryptInter
    private String caseAdvice;

    /** 患者姓名 */
    @Excel(name = "患者姓名")
    @EncryptInter
    private String patienName;

    /** 医生姓名 */
    @Excel(name = "医生姓名")
    @EncryptInter
    private String doctorName;

    /** 就诊号 */
    @Excel(name = "就诊号")
    private String treatmentNum;

    public void setCaseHistoryId(Long caseHistoryId)
    {
        this.caseHistoryId = caseHistoryId;
    }

    public Long getCaseHistoryId()
    {
        return caseHistoryId;
    }
    public void setPatientId(Long patientId)
    {
        this.patientId = patientId;
    }

    public Long getPatientId()
    {
        return patientId;
    }
    public void setCreateUserId(Long createUserId)
    {
        this.createUserId = createUserId;
    }

    public Long getCreateUserId()
    {
        return createUserId;
    }
    public void setChiefComplaint(String chiefComplaint)
    {
        this.chiefComplaint = chiefComplaint;
    }

    public String getChiefComplaint()
    {
        return chiefComplaint;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setPreviousHistory(String previousHistory)
    {
        this.previousHistory = previousHistory;
    }

    public String getPreviousHistory()
    {
        return previousHistory;
    }
    public void setAllergyHistory(String allergyHistory)
    {
        this.allergyHistory = allergyHistory;
    }

    public String getAllergyHistory()
    {
        return allergyHistory;
    }
    public void setCaseResult(String caseResult)
    {
        this.caseResult = caseResult;
    }

    public String getCaseResult()
    {
        return caseResult;
    }
    public void setCaseTime(Date caseTime)
    {
        this.caseTime = caseTime;
    }

    public Date getCaseTime()
    {
        return caseTime;
    }
    public void setPrescriptionDrug(String prescriptionDrug)
    {
        this.prescriptionDrug = prescriptionDrug;
    }

    public String getPrescriptionDrug()
    {
        return prescriptionDrug;
    }
    public void setCaseAdvice(String caseAdvice)
    {
        this.caseAdvice = caseAdvice;
    }

    public String getCaseAdvice()
    {
        return caseAdvice;
    }
    public void setPatienName(String patienName)
    {
        this.patienName = patienName;
    }

    public String getPatienName()
    {
        return patienName;
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
                .append("caseHistoryId", getCaseHistoryId())
                .append("patientId", getPatientId())
                .append("createUserId", getCreateUserId())
                .append("chiefComplaint", getChiefComplaint())
                .append("userId", getUserId())
                .append("previousHistory", getPreviousHistory())
                .append("allergyHistory", getAllergyHistory())
                .append("caseResult", getCaseResult())
                .append("caseTime", getCaseTime())
                .append("prescriptionDrug", getPrescriptionDrug())
                .append("caseAdvice", getCaseAdvice())
                .append("patienName", getPatienName())
                .append("doctorName", getDoctorName())
                .append("treatmentNum", getTreatmentNum())
                .append("remark", getRemark())
                .toString();
    }
}
