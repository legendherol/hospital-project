package com.ruoyi.hospital.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 科室对象 office
 * 
 * @author ruoyi
 * @date 2023-06-02
 */
public class Office extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 科室id */
    private Long officeId;

    /** 科室名 */
    @Excel(name = "科室名")
    private String officeName;

    /** 科室代码 */
    @Excel(name = "科室代码")
    private String officeCode;

    /** 科室介绍 */
    @Excel(name = "科室介绍")
    private String officeIntroduction;

    /** 责任医生 */
    @Excel(name = "责任医生")
    private String doctor;

    public void setOfficeId(Long officeId) 
    {
        this.officeId = officeId;
    }

    public Long getOfficeId() 
    {
        return officeId;
    }
    public void setOfficeName(String officeName) 
    {
        this.officeName = officeName;
    }

    public String getOfficeName() 
    {
        return officeName;
    }
    public void setOfficeCode(String officeCode) 
    {
        this.officeCode = officeCode;
    }

    public String getOfficeCode() 
    {
        return officeCode;
    }
    public void setOfficeIntroduction(String officeIntroduction) 
    {
        this.officeIntroduction = officeIntroduction;
    }

    public String getOfficeIntroduction() 
    {
        return officeIntroduction;
    }
    public void setDoctor(String doctor) 
    {
        this.doctor = doctor;
    }

    public String getDoctor() 
    {
        return doctor;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("officeId", getOfficeId())
            .append("officeName", getOfficeName())
            .append("officeCode", getOfficeCode())
            .append("officeIntroduction", getOfficeIntroduction())
            .append("doctor", getDoctor())
            .append("remark", getRemark())
            .toString();
    }
}
