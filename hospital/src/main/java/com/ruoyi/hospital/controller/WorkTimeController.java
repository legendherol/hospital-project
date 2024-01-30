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
import com.ruoyi.hospital.domain.WorkTime;
import com.ruoyi.hospital.service.IWorkTimeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 放号Controller
 * 
 * @author ruoyi
 * @date 2023-06-02
 */
@RestController
@RequestMapping("/accountmanage/accountmanage")
public class WorkTimeController extends BaseController
{
    @Autowired
    private IWorkTimeService workTimeService;

    /**
     * 查询放号列表
     */
    @PreAuthorize("@ss.hasPermi('accountmanage:accountmanage:list')")
    @GetMapping("/list")
    public TableDataInfo list(WorkTime workTime)
    {
        startPage();
        List<WorkTime> list = workTimeService.selectWorkTimeList(workTime);
        return getDataTable(list);
    }

    /**
     * 导出放号列表
     */
    @PreAuthorize("@ss.hasPermi('accountmanage:accountmanage:export')")
    @Log(title = "放号", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WorkTime workTime)
    {
        List<WorkTime> list = workTimeService.selectWorkTimeList(workTime);
        ExcelUtil<WorkTime> util = new ExcelUtil<WorkTime>(WorkTime.class);
        util.exportExcel(response, list, "放号数据");
    }

    /**
     * 获取放号详细信息
     */
    @PreAuthorize("@ss.hasPermi('accountmanage:accountmanage:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(workTimeService.selectWorkTimeById(id));
    }

    /**
     * 新增放号
     */
    @PreAuthorize("@ss.hasPermi('accountmanage:accountmanage:add')")
    @Log(title = "放号", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WorkTime workTime)
    {
        return toAjax(workTimeService.insertWorkTime(workTime));
    }

    /**
     * 修改放号
     */
    @PreAuthorize("@ss.hasPermi('accountmanage:accountmanage:edit')")
    @Log(title = "放号", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WorkTime workTime)
    {
        return toAjax(workTimeService.updateWorkTime(workTime));
    }

    /**
     * 删除放号
     */
    @PreAuthorize("@ss.hasPermi('accountmanage:accountmanage:remove')")
    @Log(title = "放号", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(workTimeService.deleteWorkTimeByIds(ids));
    }
}
