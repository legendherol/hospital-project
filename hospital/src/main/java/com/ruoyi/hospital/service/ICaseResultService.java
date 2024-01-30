package com.ruoyi.hospital.service;

import java.io.IOException;
import java.util.List;

import com.ruoyi.hospital.domain.BookRegistration;
import com.ruoyi.hospital.domain.CaseResult;

/**
 * 门诊Service接口
 * 
 * @author ruoyi
 * @date 2023-06-02
 */
public interface ICaseResultService 
{
    /**
     * 查询门诊
     * 
     * @param caseHistoryId 门诊主键
     * @return 门诊
     */
    public CaseResult selectCaseResultByCaseHistoryId(Long caseHistoryId) throws Exception;

    /**
     * 查询门诊列表
     * 
     * @param caseResult 门诊
     * @return 门诊集合
     */
    public List<CaseResult> selectCaseResultList(CaseResult caseResult);

    /**
     * 新增门诊
     * 
     * @param caseResult 门诊
     * @return 结果
     */
    public int insertCaseResult(CaseResult caseResult) throws Exception;

    /**
     * 修改门诊
     * 
     * @param caseResult 门诊
     * @return 结果
     */
    public int updateCaseResult(CaseResult caseResult);

    /**
     * 批量删除门诊
     * 
     * @param caseHistoryIds 需要删除的门诊主键集合
     * @return 结果
     */
    public int deleteCaseResultByCaseHistoryIds(Long[] caseHistoryIds);

    /**
     * 删除门诊信息
     * 
     * @param caseHistoryId 门诊主键
     * @return 结果
     */
    public int deleteCaseResultByCaseHistoryId(Long caseHistoryId);

    List<BookRegistration> getBookList() throws Exception;

    List<CaseResult> selectCaseResultListNow(CaseResult caseResult) throws Exception;

    List<CaseResult> selectCaseResultDocList(CaseResult caseResult) throws Exception;
}
