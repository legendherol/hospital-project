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
import com.ruoyi.hospital.domain.KeyUpLog;
import com.ruoyi.hospital.service.IKeyUpLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 密钥修改日志Controller
 * 
 * @author ruoyi
 * @date 2023-06-07
 */
@RestController
@RequestMapping("/keyUpLog/keyUpLog")
public class KeyUpLogController extends BaseController
{
    @Autowired
    private IKeyUpLogService keyUpLogService;

    /**
     * 查询密钥修改日志列表
     */
    @PreAuthorize("@ss.hasPermi('keyUpLog:keyUpLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(KeyUpLog keyUpLog)
    {
        startPage();
        List<KeyUpLog> list = keyUpLogService.selectKeyUpLogList(keyUpLog);
        return getDataTable(list);
    }

    /**
     * 导出密钥修改日志列表
     */
    @PreAuthorize("@ss.hasPermi('keyUpLog:keyUpLog:export')")
    @Log(title = "密钥修改日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KeyUpLog keyUpLog)
    {
        List<KeyUpLog> list = keyUpLogService.selectKeyUpLogList(keyUpLog);
        ExcelUtil<KeyUpLog> util = new ExcelUtil<KeyUpLog>(KeyUpLog.class);
        util.exportExcel(response, list, "密钥修改日志数据");
    }

    /**
     * 获取密钥修改日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('keyUpLog:keyUpLog:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(keyUpLogService.selectKeyUpLogById(id));
    }

    /**
     * 新增密钥修改日志
     */
    @PreAuthorize("@ss.hasPermi('keyUpLog:keyUpLog:add')")
    @Log(title = "密钥修改日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody KeyUpLog keyUpLog)
    {
        return toAjax(keyUpLogService.insertKeyUpLog(keyUpLog));
    }

    /**
     * 修改密钥修改日志
     */
    @PreAuthorize("@ss.hasPermi('keyUpLog:keyUpLog:edit')")
    @Log(title = "密钥修改日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody KeyUpLog keyUpLog)
    {
        return toAjax(keyUpLogService.updateKeyUpLog(keyUpLog));
    }

    /**
     * 删除密钥修改日志
     */
    @PreAuthorize("@ss.hasPermi('keyUpLog:keyUpLog:remove')")
    @Log(title = "密钥修改日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(keyUpLogService.deleteKeyUpLogByIds(ids));
    }
}
