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
import com.ruoyi.hospital.domain.AesData;
import com.ruoyi.hospital.service.IAesDataService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * aesController
 * 
 * @author ruoyi
 * @date 2023-06-07
 */
@RestController
@RequestMapping("/aes/data")
public class AesDataController extends BaseController
{
    @Autowired
    private IAesDataService aesDataService;

    /**
     * 查询aes列表
     */
    @PreAuthorize("@ss.hasPermi('aes:data:list')")
    @GetMapping("/list")
    public TableDataInfo list(AesData aesData)
    {
        startPage();
        List<AesData> list = aesDataService.selectAesDataList(aesData);
        return getDataTable(list);
    }

    /**
     * 导出aes列表
     */
    @PreAuthorize("@ss.hasPermi('aes:data:export')")
    @Log(title = "aes", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AesData aesData)
    {
        List<AesData> list = aesDataService.selectAesDataList(aesData);
        ExcelUtil<AesData> util = new ExcelUtil<AesData>(AesData.class);
        util.exportExcel(response, list, "aes数据");
    }

    /**
     * 获取aes详细信息
     */
    @PreAuthorize("@ss.hasPermi('aes:data:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(aesDataService.selectAesDataById(id));
    }

    /**
     * 新增aes
     */
    @PreAuthorize("@ss.hasPermi('aes:data:add')")
    @Log(title = "aes", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AesData aesData)
    {
        return toAjax(aesDataService.insertAesData(aesData));
    }

    /**
     * 修改aes
     */
    @PreAuthorize("@ss.hasPermi('aes:data:edit')")
    @Log(title = "aes", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AesData aesData)
    {
        return toAjax(aesDataService.updateAesData(aesData));
    }

    /**
     * 删除aes
     */
    @PreAuthorize("@ss.hasPermi('aes:data:remove')")
    @Log(title = "aes", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(aesDataService.deleteAesDataByIds(ids));
    }
}
