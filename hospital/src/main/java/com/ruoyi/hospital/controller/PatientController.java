package com.ruoyi.hospital.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.hospital.anno.Decrypt;
import com.ruoyi.hospital.dto.RequestDataDto;
import com.ruoyi.hospital.service.IPatientService;
import com.ruoyi.hospital.util.AESUtil;
import com.ruoyi.hospital.util.AesKeyUtil;
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
import com.ruoyi.hospital.domain.Patient;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 患者管理Controller
 * 
 * @author liu
 * @date 2023-06-01
 */
@RestController
@RequestMapping("/patient/patient")
public class PatientController extends BaseController
{
    @Autowired
    private IPatientService patientService;

    private static final String priavateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIhyFhq4/cbH1qbRYrNSkhkzHSiD65EFPfcVk3TZ3isYaGFy/Y4+egkLoX4kwW1VW9z7/MyCKVUdJI0hSuSknKiCmcX3tGnOLZGZqzQBfJ4n134+76OYxRlioEh+BI8Zjx28MMwCDwR9+c1DMF0psF2m5bL14pLt6iiDJYeQXGZZAgMBAAECgYBbRW2R3JMtqDnnBwNuDtrZ7n4fvvcR4B7OLGmh7acWztHr9d60iwhZCqWxWubkuwejMBCvwJXjcIYlvdJ6Vb7QXyjeZJ11K9KRbiRRaCqZg1u3HXPNi3xWHJGtevdjDnoS+ZC5uRKjhsucp180591XeKppru8zznIIKKAfl0JSMQJBAOLHyN0QWXSMwYoOXxUDbGwEfy912pY6j4LpW6xKj3eJ5f5KBNCYnxQgnNZrzSyoM9+sSKMfta5S6C7nAxX+j00CQQCaBqn+JozCk/zUI2akGRa+WHZZi4gXCGgUuipttp+IP+uLK1Iv9ajnib1N2SUuwROPBm8cFI+hlFP2sWdNJsU9AkBRtmbbsI5q/mSmF/OOoMMqUJx7P13Zj5QyOV88v0jea6Ohco41kyiOmgmpAQLWumymhW9Ox5gxDdJ3Q+nKvQRpAkAYNtn8sZTJdjh2JUaan7Mao4+fjjcL3+906ruG2gIEin/+NgZKseUm2VmgZnXY/tOTVaCcGoNmFIqKquMhlunFAkEA4cQQUnyAax3ABpNkRxBaQtXO2ZImudT1r3JoEes7JPu9L1jdPf8V+GU2wb97gr9P1y0e14sDEFIBgJ3mWFH9Hw==";

    /**
     * 查询患者管理列表
     */
    @PreAuthorize("@ss.hasPermi('patient:patient:list')")
    @GetMapping("/list")
    public TableDataInfo list(Patient patient) throws Exception {
        startPage();
        List<Patient> list = patientService.selectPatientList(patient);
        return getDataTable(list);
    }

    /**
     * 导出患者管理列表
     */
    @PreAuthorize("@ss.hasPermi('patient:patient:export')")
    @Log(title = "患者管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Patient patient) throws Exception {
        List<Patient> list = patientService.selectPatientList(patient);
        ExcelUtil<Patient> util = new ExcelUtil<Patient>(Patient.class);
        util.exportExcel(response, list, "患者管理数据");
    }

    /**
     * 获取患者管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('patient:patient:query')")
    @GetMapping(value = "/{patientId}")
    public AjaxResult getInfo(@PathVariable("patientId") Long patientId) throws Exception {
        return success(patientService.selectPatientByPatientId(patientId));
    }

    /**
     * 新增患者管理
     */
    @PreAuthorize("@ss.hasPermi('patient:patient:add')")
    @Log(title = "患者管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Patient patient) throws Exception {
        return toAjax(patientService.insertPatient(patient));
    }

    /**
     * 修改患者管理
     */
    @PreAuthorize("@ss.hasPermi('patient:patient:edit')")
    @Log(title = "患者管理", businessType = BusinessType.UPDATE)
    @PostMapping("/put")
    public AjaxResult edit(@RequestBody RequestDataDto requestDataDto) throws Exception {
        String data = requestDataDto.getData();
        String decrypt = AESUtil.decrypt(data, AesKeyUtil.getTansferKey());
        Patient patient = JSONObject.parseObject(decrypt, Patient.class);
        return toAjax(patientService.updatePatient(patient));
    }

    /**
     * 删除患者管理
     */
    @PreAuthorize("@ss.hasPermi('patient:patient:remove')")
    @Log(title = "患者管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{patientIds}")
    public AjaxResult remove(@PathVariable Long[] patientIds) throws Exception {
        return toAjax(patientService.deletePatientByPatientIds(patientIds));
    }

    @GetMapping("/updateRsa")
    public AjaxResult updateRsa() throws Exception {
        return toAjax(patientService.updateRsa());
    }


    @GetMapping(value = "/find")
    public AjaxResult findByUserId() throws Exception {
        return success(patientService.findByUserId());
    }
}
