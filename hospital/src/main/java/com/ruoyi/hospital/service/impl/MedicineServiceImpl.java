package com.ruoyi.hospital.service.impl;

import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.hospital.mapper.MedicineMapper;
import com.ruoyi.hospital.domain.Medicine;
import com.ruoyi.hospital.service.IMedicineService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 药品Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-06-02
 */
@Service
@Transactional
public class MedicineServiceImpl implements IMedicineService 
{
    @Autowired
    private MedicineMapper medicineMapper;

    /**
     * 查询药品
     * 
     * @param medicineMsgId 药品主键
     * @return 药品
     */
    @Override
    public Medicine selectMedicineByMedicineMsgId(Long medicineMsgId)
    {
        return medicineMapper.selectMedicineByMedicineMsgId(medicineMsgId);
    }

    /**
     * 查询药品列表
     * 
     * @param medicine 药品
     * @return 药品
     */
    @Override
    public List<Medicine> selectMedicineList(Medicine medicine)
    {
        return medicineMapper.selectMedicineList(medicine);
    }

    /**
     * 新增药品
     * 
     * @param medicine 药品
     * @return 结果
     */
    @Override
    public int insertMedicine(Medicine medicine)
    {
        return medicineMapper.insertMedicine(medicine);
    }

    /**
     * 修改药品
     * 
     * @param medicine 药品
     * @return 结果
     */
    @Override
    public int updateMedicine(Medicine medicine)
    {
        return medicineMapper.updateMedicine(medicine);
    }

    /**
     * 批量删除药品
     * 
     * @param medicineMsgIds 需要删除的药品主键
     * @return 结果
     */
    @Override
    public int deleteMedicineByMedicineMsgIds(Long[] medicineMsgIds)
    {
        return medicineMapper.deleteMedicineByMedicineMsgIds(medicineMsgIds);
    }

    /**
     * 删除药品信息
     * 
     * @param medicineMsgId 药品主键
     * @return 结果
     */
    @Override
    public int deleteMedicineByMedicineMsgId(Long medicineMsgId)
    {
        return medicineMapper.deleteMedicineByMedicineMsgId(medicineMsgId);
    }

    @Override
    public int buy(Long medicineMsgId) {
        Medicine medicine = medicineMapper.selectMedicineByMedicineMsgId(medicineMsgId);
        Long stock = medicine.getStock();
        if (stock==0){
            throw new ServiceException("不好意思,药品库存已经不足了");
        }else {
            stock = stock -1;
        }
        medicine.setStock(stock);
        return medicineMapper.updateMedicine(medicine);
    }
}
