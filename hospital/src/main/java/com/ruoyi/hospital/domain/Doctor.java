package com.ruoyi.hospital.domain;

import com.ruoyi.hospital.anno.EncryptInter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * doctor对象 doctor
 * 
 * @author ruoyi
 * @date 2023-06-02
 */
public class Doctor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 医生id */
    private Long id;

    /** 医生名字 */
    @Excel(name = "医生名字")
    @EncryptInter
    private String username;

    /** 性别 */
    @Excel(name = "性别")
    private String gender;

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

    /** 科室 */
    @Excel(name = "科室")
    private Long deptId;

    /** 用户简介 */
    @Excel(name = "用户简介")
    private String position;

    /** 用户简介 */
    @Excel(name = "科室")
    private Office office;

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getUsername() 
    {
        return username;
    }
    public void setGender(String gender) 
    {
        this.gender = gender;
    }

    public String getGender() 
    {
        return gender;
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

    public String getUserId() {
        return userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAge(Long age)
    {
        this.age = age;
    }

    public Long getAge() 
    {
        return age;
    }
    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }
    public void setPosition(String position) 
    {
        this.position = position;
    }

    public String getPosition() 
    {
        return position;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("username", getUsername())
            .append("gender", getGender())
            .append("idCard", getIdCard())
            .append("phone", getPhone())
            .append("userId", getUserId())
            .append("age", getAge())
            .append("deptId", getDeptId())
            .append("position", getPosition())
            .toString();
    }
}
