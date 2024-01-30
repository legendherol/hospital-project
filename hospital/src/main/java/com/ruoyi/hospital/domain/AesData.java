package com.ruoyi.hospital.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * aes对象 aes_data
 * 
 * @author ruoyi
 * @date 2023-06-07
 */
public class AesData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private String userId;

    /** 表名称 */
    @Excel(name = "表名称")
    private String tableName;

    /** 字段名称 */
    @Excel(name = "字段名称")
    private String fieldName;

    /** aes数据加密密钥 */
    @Excel(name = "aes数据加密密钥")
    private String aesKey;

    /** aes数据传输密钥 */
    @Excel(name = "aes数据传输密钥")
    private String aesTransferKey;

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
    public void setTableName(String tableName) 
    {
        this.tableName = tableName;
    }

    public String getTableName() 
    {
        return tableName;
    }
    public void setFieldName(String fieldName) 
    {
        this.fieldName = fieldName;
    }

    public String getFieldName() 
    {
        return fieldName;
    }
    public void setAesKey(String aesKey) 
    {
        this.aesKey = aesKey;
    }

    public String getAesKey() 
    {
        return aesKey;
    }
    public void setAesTransferKey(String aesTransferKey) 
    {
        this.aesTransferKey = aesTransferKey;
    }

    public String getAesTransferKey() 
    {
        return aesTransferKey;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("tableName", getTableName())
            .append("fieldName", getFieldName())
            .append("aesKey", getAesKey())
            .append("aesTransferKey", getAesTransferKey())
            .toString();
    }
}
