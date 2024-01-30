package com.ruoyi.hospital.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 【请填写功能名称】对象 work_time
 *
 * @author ruoyi
 * @date 2023-06-03
 */
public class WorkTime extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 医生id */
    @Excel(name = "医生id")
    private String docId;

    /** 诊断限额 */
    @Excel(name = "诊断限额")
    private Long caseNumer;

    /** 排班时间 */
    @Excel(name = "排班时间")
    private Long workTime;

    /** 科室id */
    @Excel(name = "科室id")
    private Long offId;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date day;

    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    private Doctor doctor;


    private  Office office;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setDocId(String docId)
    {
        this.docId = docId;
    }

    public String getDocId()
    {
        return docId;
    }
    public void setCaseNumer(Long caseNumer)
    {
        this.caseNumer = caseNumer;
    }

    public Long getCaseNumer()
    {
        return caseNumer;
    }
    public void setWorkTime(Long workTime)
    {
        this.workTime = workTime;
    }

    public Long getWorkTime()
    {
        return workTime;
    }
    public void setOffId(Long offId)
    {
        this.offId = offId;
    }

    public Long getOffId()
    {
        return offId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("docId", getDocId())
                .append("caseNumer", getCaseNumer())
                .append("workTime", getWorkTime())
                .append("offId", getOffId())
                .toString();
    }
}
