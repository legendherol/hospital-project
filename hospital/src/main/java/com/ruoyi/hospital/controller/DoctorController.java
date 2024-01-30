package com.ruoyi.hospital.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.hospital.service.IDoctorService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.hospital.domain.Doctor;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * doctorController
 * 
 * @author ruoyi
 * @date 2023-06-02
 */
@RestController
@RequestMapping("/doctor/doctor")
public class DoctorController extends BaseController
{
    @Autowired
    private IDoctorService doctorService;

    /**
     * 查询doctor列表
     */
    @PreAuthorize("@ss.hasPermi('doctor:doctor:list')")
    @GetMapping("/list")
    public TableDataInfo list(Doctor doctor)
    {
        startPage();
        List<Doctor> list = doctorService.selectDoctorList(doctor);
        return getDataTable(list);
    }

    /**
     * 导出doctor列表
     */
    @PreAuthorize("@ss.hasPermi('doctor:doctor:export')")
    @Log(title = "doctor", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Doctor doctor)
    {
        List<Doctor> list = doctorService.selectDoctorList(doctor);
        ExcelUtil<Doctor> util = new ExcelUtil<Doctor>(Doctor.class);
        util.exportExcel(response, list, "doctor数据");
    }

    /**
     * 获取doctor详细信息
     */
    @PreAuthorize("@ss.hasPermi('doctor:doctor:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) throws Exception {
        return success(doctorService.selectDoctorById(id));
    }

    /**
     * 新增doctor
     */
    @PreAuthorize("@ss.hasPermi('doctor:doctor:add')")
    @Log(title = "doctor", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Doctor doctor) throws Exception {
        return toAjax(doctorService.insertDoctor(doctor));
    }

    /**
     * 修改doctor
     */
    @PreAuthorize("@ss.hasPermi('doctor:doctor:edit')")
    @Log(title = "doctor", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Doctor doctor)
    {
        return toAjax(doctorService.updateDoctor(doctor));
    }

    /**
     * 删除doctor
     */
    @PreAuthorize("@ss.hasPermi('doctor:doctor:remove')")
    @Log(title = "doctor", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) throws Exception {
        return toAjax(doctorService.deleteDoctorByIds(ids));
    }

    @GetMapping(value = "/find")
    public AjaxResult findByUserId() throws Exception {
        return success(doctorService.findByUserId());
    }

    @GetMapping("/updateRsa")
    public AjaxResult updateRsa() throws Exception {
        return toAjax(doctorService.updateRsa());
    }
}
