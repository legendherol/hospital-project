package com.ruoyi.hospital.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 药品对象 medicine
 * 
 * @author ruoyi
 * @date 2023-06-02
 */
public class Medicine extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 药品信息id */
    private Long medicineMsgId;

    /** 药品名称 */
    @Excel(name = "药品名称")
    private String medicineName;

    /** 药品规格 */
    @Excel(name = "药品规格")
    private String medicineNorm;

    /** 生产厂家 */
    @Excel(name = "生产厂家")
    private String productVender;

    /** 用法 */
    @Excel(name = "用法")
    private String userWay;

    /** 药品成分 */
    @Excel(name = "药品成分")
    private String medicineMaterial;

    /** 药品代码 */
    @Excel(name = "药品代码")
    private String medicineCode;

    /** 库存 */
    @Excel(name = "库存")
    private Long stock;

    public void setMedicineMsgId(Long medicineMsgId) 
    {
        this.medicineMsgId = medicineMsgId;
    }

    public Long getMedicineMsgId() 
    {
        return medicineMsgId;
    }
    public void setMedicineName(String medicineName) 
    {
        this.medicineName = medicineName;
    }

    public String getMedicineName() 
    {
        return medicineName;
    }
    public void setMedicineNorm(String medicineNorm) 
    {
        this.medicineNorm = medicineNorm;
    }

    public String getMedicineNorm() 
    {
        return medicineNorm;
    }
    public void setProductVender(String productVender) 
    {
        this.productVender = productVender;
    }

    public String getProductVender() 
    {
        return productVender;
    }
    public void setUserWay(String userWay) 
    {
        this.userWay = userWay;
    }

    public String getUserWay() 
    {
        return userWay;
    }
    public void setMedicineMaterial(String medicineMaterial) 
    {
        this.medicineMaterial = medicineMaterial;
    }

    public String getMedicineMaterial() 
    {
        return medicineMaterial;
    }
    public void setMedicineCode(String medicineCode) 
    {
        this.medicineCode = medicineCode;
    }

    public String getMedicineCode() 
    {
        return medicineCode;
    }
    public void setStock(Long stock) 
    {
        this.stock = stock;
    }

    public Long getStock() 
    {
        return stock;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("medicineMsgId", getMedicineMsgId())
            .append("medicineName", getMedicineName())
            .append("medicineNorm", getMedicineNorm())
            .append("productVender", getProductVender())
            .append("userWay", getUserWay())
            .append("medicineMaterial", getMedicineMaterial())
            .append("medicineCode", getMedicineCode())
            .append("stock", getStock())
            .toString();
    }
}
