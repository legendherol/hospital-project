package com.ruoyi.hospital.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * rsa对象 user_rsa_key
 * 
 * @author ruoyi
 * @date 2023-06-07
 */
public class UserRsaKey extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private String userId;

    /** 用户私钥 */
    @Excel(name = "用户私钥")
    private String userPrivateKey;

    /** 用户公钥 */
    @Excel(name = "用户公钥")
    private String userPublicKey;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPrivateKey(String userPrivateKey)
    {
        this.userPrivateKey = userPrivateKey;
    }

    public String getUserPrivateKey() 
    {
        return userPrivateKey;
    }
    public void setUserPublicKey(String userPublicKey) 
    {
        this.userPublicKey = userPublicKey;
    }

    public String getUserPublicKey() 
    {
        return userPublicKey;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("userPrivateKey", getUserPrivateKey())
            .append("userPublicKey", getUserPublicKey())
            .toString();
    }
}
