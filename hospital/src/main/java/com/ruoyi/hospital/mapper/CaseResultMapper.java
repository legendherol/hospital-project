package com.ruoyi.hospital.mapper;

import java.util.List;
import com.ruoyi.hospital.domain.CaseResult;

/**
 * 门诊Mapper接口
 * 
 * @author ruoyi
 * @date 2023-06-02
 */
public interface CaseResultMapper 
{
    /**
     * 查询门诊
     * 
     * @param caseHistoryId 门诊主键
     * @return 门诊
     */
    public CaseResult selectCaseResultByCaseHistoryId(Long caseHistoryId);

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
    public int insertCaseResult(CaseResult caseResult);

    /**
     * 修改门诊
     * 
     * @param caseResult 门诊
     * @return 结果
     */
    public int updateCaseResult(CaseResult caseResult);

    /**
     * 删除门诊
     * 
     * @param caseHistoryId 门诊主键
     * @return 结果
     */
    public int deleteCaseResultByCaseHistoryId(Long caseHistoryId);

    /**
     * 批量删除门诊
     * 
     * @param caseHistoryIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCaseResultByCaseHistoryIds(Long[] caseHistoryIds);
}
