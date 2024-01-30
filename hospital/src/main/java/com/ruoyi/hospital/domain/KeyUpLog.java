package com.ruoyi.hospital.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 密钥修改日志对象 key_up_log
 * 
 * @author ruoyi
 * @date 2023-06-07
 */
public class KeyUpLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private String userId;

    /** 修改密钥对人名字 */
    @Excel(name = "修改密钥对人名字")
    private String name;

    /** 原密钥对描述 */
    @Excel(name = "原密钥对描述")
    private String des;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setDes(String des) 
    {
        this.des = des;
    }

    public String getDes() 
    {
        return des;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("name", getName())
            .append("des", getDes())
            .append("createTime", getCreateTime())
            .toString();
    }
}
