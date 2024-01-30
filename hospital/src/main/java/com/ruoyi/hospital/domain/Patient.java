package com.ruoyi.hospital.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.hospital.anno.EncryptInter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * patient对象 patient
 *
 * @author ruoyi
 * @date 2023-06-07
 */
public class Patient extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 患者id */
    private Long patientId;

    /** 患者名字 */
    @Excel(name = "患者名字")
    @EncryptInter
    private String patientName;

    /** 性别 */
    @Excel(name = "性别")
    private String gender;

    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthday;

    /** 身份证号 */
    @Excel(name = "身份证号")
    @EncryptInter
    private String idCard;

    /** 电话号码 */
    @Excel(name = "电话号码")
    @EncryptInter
    private String phone;

    /** 用户id */
    @Excel(name = "用户id")
    private String userId;

    /** 年龄 */
    @Excel(name = "年龄")
    private Long age;

    /** 地址 */
    @Excel(name = "地址")
    @EncryptInter
    private String address;

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
    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getGender()
    {
        return gender;
    }
    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }

    public Date getBirthday()
    {
        return birthday;
    }
    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    public String getIdCard()
    {
        return idCard;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserId()
    {
        return userId;
    }
    public void setAge(Long age)
    {
        this.age = age;
    }

    public Long getAge()
    {
        return age;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("patientId", getPatientId())
                .append("patientName", getPatientName())
                .append("gender", getGender())
                .append("birthday", getBirthday())
                .append("idCard", getIdCard())
                .append("phone", getPhone())
                .append("userId", getUserId())
                .append("age", getAge())
                .append("address", getAddress())
                .toString();
    }
}
