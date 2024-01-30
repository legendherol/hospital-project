package com.ruoyi.hospital.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.hospital.domain.BookRegistration;
import com.ruoyi.hospital.domain.Doctor;
import com.ruoyi.hospital.domain.Office;
import com.ruoyi.hospital.domain.WorkTime;
import com.ruoyi.hospital.service.IBookRegistrationService;
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
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 预约挂号Controller
 * 
 * @author liu
 * @date 2023-06-02
 */
@RestController
@RequestMapping("/book/registration")
public class BookRegistrationController extends BaseController
{
    @Autowired
    private IBookRegistrationService bookRegistrationService;

    /**
     * 查询预约挂号列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BookRegistration bookRegistration)
    {
        startPage();
        List<BookRegistration> list = bookRegistrationService.selectBookRegistrationList(bookRegistration);
        return getDataTable(list);
    }

    /**
     * 导出预约挂号列表
     */
    @Log(title = "预约挂号", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BookRegistration bookRegistration)
    {
        List<BookRegistration> list = bookRegistrationService.selectBookRegistrationList(bookRegistration);
        ExcelUtil<BookRegistration> util = new ExcelUtil<BookRegistration>(BookRegistration.class);
        util.exportExcel(response, list, "预约挂号数据");
    }

    /**
     * 获取预约挂号详细信息
     */
    @GetMapping(value = "/{registrationId}")
    public AjaxResult getInfo(@PathVariable("registrationId") Long registrationId) throws Exception {
        return success(bookRegistrationService.selectBookRegistrationByRegistrationId(registrationId));
    }

    /**
     * 新增预约挂号
     */
    @Log(title = "预约挂号", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BookRegistration bookRegistration)
    {
        try {
            return toAjax(bookRegistrationService.insertBookRegistration(bookRegistration));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改预约挂号
     */
    @Log(title = "预约挂号", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BookRegistration bookRegistration)
    {
        return toAjax(bookRegistrationService.updateBookRegistration(bookRegistration));
    }

    /**
     * 删除预约挂号
     */
    @Log(title = "预约挂号", businessType = BusinessType.DELETE)
	@DeleteMapping("/{registrationIds}")
    public AjaxResult remove(@PathVariable Long[] registrationIds)
    {
        return toAjax(bookRegistrationService.deleteBookRegistrationByRegistrationIds(registrationIds));
    }

    /**
     * 查询预约挂号列表
     */
    @PostMapping("/doc")
    public List<WorkTime> getWorkTime(@RequestBody Office office)
    {
        return bookRegistrationService.getWorkTime(office);
    }

    /**
     * 查询预约挂号列表
     */
    @GetMapping("/listNow")
    public TableDataInfo getNowBook(BookRegistration bookRegistration) throws Exception {
        startPage();
        List<BookRegistration> list = bookRegistrationService.selectNowBookRegistrationList(bookRegistration);
        return getDataTable(list);
    }
}
