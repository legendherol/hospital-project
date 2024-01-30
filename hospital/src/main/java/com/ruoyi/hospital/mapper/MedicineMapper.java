package com.ruoyi.hospital.mapper;

import java.util.List;
import com.ruoyi.hospital.domain.Medicine;

/**
 * 药品Mapper接口
 * 
 * @author ruoyi
 * @date 2023-06-02
 */
public interface MedicineMapper 
{
    /**
     * 查询药品
     * 
     * @param medicineMsgId 药品主键
     * @return 药品
     */
    public Medicine selectMedicineByMedicineMsgId(Long medicineMsgId);

    /**
     * 查询药品列表
     * 
     * @param medicine 药品
     * @return 药品集合
     */
    public List<Medicine> selectMedicineList(Medicine medicine);

    /**
     * 新增药品
     * 
     * @param medicine 药品
     * @return 结果
     */
    public int insertMedicine(Medicine medicine);

    /**
     * 修改药品
     * 
     * @param medicine 药品
     * @return 结果
     */
    public int updateMedicine(Medicine medicine);

    /**
     * 删除药品
     * 
     * @param medicineMsgId 药品主键
     * @return 结果
     */
    public int deleteMedicineByMedicineMsgId(Long medicineMsgId);

    /**
     * 批量删除药品
     * 
     * @param medicineMsgIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMedicineByMedicineMsgIds(Long[] medicineMsgIds);
}
