package com.ruoyi.hospital.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.hospital.anno.DecryptInterAnno;
import com.ruoyi.hospital.anno.DecryptedRequestBody;
import com.ruoyi.hospital.domain.BookRegistration;
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
import com.ruoyi.hospital.domain.CaseResult;
import com.ruoyi.hospital.service.ICaseResultService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 门诊Controller
 * 
 * @author ruoyi
 * @date 2023-06-02
 */
@RestController
@RequestMapping("/case/case")
public class CaseResultController extends BaseController
{
    @Autowired
    private ICaseResultService caseResultService;

    /**
     * 查询门诊列表
     */
    @PreAuthorize("@ss.hasPermi('case:case:list')")
    @GetMapping("/list")
    public TableDataInfo list(CaseResult caseResult)
    {
        startPage();
        List<CaseResult> list = caseResultService.selectCaseResultList(caseResult);
        return getDataTable(list);
    }

    /**
     * 导出门诊列表
     */
    @PreAuthorize("@ss.hasPermi('case:case:export')")
    @Log(title = "门诊", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CaseResult caseResult)
    {
        List<CaseResult> list = caseResultService.selectCaseResultList(caseResult);
        ExcelUtil<CaseResult> util = new ExcelUtil<CaseResult>(CaseResult.class);
        util.exportExcel(response, list, "门诊数据");
    }

    /**
     * 获取门诊详细信息
     */
    @PreAuthorize("@ss.hasPermi('case:case:query')")
    @GetMapping(value = "/{caseHistoryId}")
    public AjaxResult getInfo(@PathVariable("caseHistoryId") Long caseHistoryId) throws Exception {
        return success(caseResultService.selectCaseResultByCaseHistoryId(caseHistoryId));
    }

    /**
     * 新增门诊
     */
    @PreAuthorize("@ss.hasPermi('case:case:add')")
    @Log(title = "门诊", businessType = BusinessType.INSERT)
    @PostMapping
    @DecryptInterAnno       // @DecryptedRequestBody  @DecryptInterAnno
    public AjaxResult add(@DecryptedRequestBody CaseResult caseResult) throws Exception {
        return toAjax(caseResultService.insertCaseResult(caseResult));
    }

    /**
     * 修改门诊
     */
    @PreAuthorize("@ss.hasPermi('case:case:edit')")
    @Log(title = "门诊", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CaseResult caseResult)
    {
        return toAjax(caseResultService.updateCaseResult(caseResult));
    }

    /**
     * 删除门诊
     */
    @PreAuthorize("@ss.hasPermi('case:case:remove')")
    @Log(title = "门诊", businessType = BusinessType.DELETE)
	@DeleteMapping("/{caseHistoryIds}")
    public AjaxResult remove(@PathVariable Long[] caseHistoryIds)
    {
        return toAjax(caseResultService.deleteCaseResultByCaseHistoryIds(caseHistoryIds));
    }

    /**
     * 查询预约列表
     */
    @GetMapping("/bookPatient")
    public List<BookRegistration> getBookList() throws Exception {
        return caseResultService.getBookList();
    }

    /**
     * 查询门诊列表
     */
    @GetMapping("/listNow")
    public TableDataInfo listNow(CaseResult caseResult) throws Exception {
        startPage();
        List<CaseResult> list = caseResultService.selectCaseResultListNow(caseResult);
        return getDataTable(list);
    }

    /**
     * 查询门诊列表
     */
    @GetMapping("/listDoc")
    public TableDataInfo listDoc(CaseResult caseResult) throws Exception {
        startPage();
        List<CaseResult> list = caseResultService.selectCaseResultDocList(caseResult);
        return getDataTable(list);
    }

}
