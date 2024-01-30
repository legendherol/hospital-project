package com.ruoyi.hospital.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.ruoyi.hospital.domain.Office;
import com.ruoyi.hospital.service.IOfficeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 科室Controller
 * 
 * @author ruoyi
 * @date 2023-06-02
 */
@RestController
@RequestMapping("/office/office")
public class OfficeController extends BaseController
{
    @Autowired
    private IOfficeService officeService;

    /**
     * 查询科室列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Office office)
    {
        startPage();
        List<Office> list = officeService.selectOfficeList(office);
        return getDataTable(list);
    }

    /**
     * 导出科室列表
     */
    @PreAuthorize("@ss.hasPermi('office:office:export')")
    @Log(title = "科室", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Office office)
    {
        List<Office> list = officeService.selectOfficeList(office);
        ExcelUtil<Office> util = new ExcelUtil<Office>(Office.class);
        util.exportExcel(response, list, "科室数据");
    }

    /**
     * 获取科室详细信息
     */
    @PreAuthorize("@ss.hasPermi('office:office:query')")
    @GetMapping(value = "/{officeId}")
    public AjaxResult getInfo(@PathVariable("officeId") Long officeId)
    {
        return success(officeService.selectOfficeByOfficeId(officeId));
    }

    /**
     * 新增科室
     */
    @PreAuthorize("@ss.hasPermi('office:office:add')")
    @Log(title = "科室", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Office office)
    {
        return toAjax(officeService.insertOffice(office));
    }

    /**
     * 修改科室
     */
    @PreAuthorize("@ss.hasPermi('office:office:edit')")
    @Log(title = "科室", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Office office)
    {
        return toAjax(officeService.updateOffice(office));
    }

    /**
     * 删除科室
     */
    @PreAuthorize("@ss.hasPermi('office:office:remove')")
    @Log(title = "科室", businessType = BusinessType.DELETE)
	@DeleteMapping("/{officeIds}")
    public AjaxResult remove(@PathVariable Long[] officeIds)
    {
        return toAjax(officeService.deleteOfficeByOfficeIds(officeIds));
    }
}
