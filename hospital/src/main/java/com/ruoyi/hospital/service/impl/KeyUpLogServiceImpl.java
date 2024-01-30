package com.ruoyi.hospital.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.hospital.mapper.KeyUpLogMapper;
import com.ruoyi.hospital.domain.KeyUpLog;
import com.ruoyi.hospital.service.IKeyUpLogService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 密钥修改日志Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-06-07
 */
@Service
@Transactional
public class KeyUpLogServiceImpl implements IKeyUpLogService 
{
    @Autowired
    private KeyUpLogMapper keyUpLogMapper;

    /**
     * 查询密钥修改日志
     * 
     * @param id 密钥修改日志主键
     * @return 密钥修改日志
     */
    @Override
    public KeyUpLog selectKeyUpLogById(Long id)
    {
        return keyUpLogMapper.selectKeyUpLogById(id);
    }

    /**
     * 查询密钥修改日志列表
     * 
     * @param keyUpLog 密钥修改日志
     * @return 密钥修改日志
     */
    @Override
    public List<KeyUpLog> selectKeyUpLogList(KeyUpLog keyUpLog)
    {
        return keyUpLogMapper.selectKeyUpLogList(keyUpLog);
    }

    /**
     * 新增密钥修改日志
     * 
     * @param keyUpLog 密钥修改日志
     * @return 结果
     */
    @Override
    public int insertKeyUpLog(KeyUpLog keyUpLog)
    {
        keyUpLog.setCreateTime(DateUtils.getNowDate());
        return keyUpLogMapper.insertKeyUpLog(keyUpLog);
    }

    /**
     * 修改密钥修改日志
     * 
     * @param keyUpLog 密钥修改日志
     * @return 结果
     */
    @Override
    public int updateKeyUpLog(KeyUpLog keyUpLog)
    {
        return keyUpLogMapper.updateKeyUpLog(keyUpLog);
    }

    /**
     * 批量删除密钥修改日志
     * 
     * @param ids 需要删除的密钥修改日志主键
     * @return 结果
     */
    @Override
    public int deleteKeyUpLogByIds(Long[] ids)
    {
        return keyUpLogMapper.deleteKeyUpLogByIds(ids);
    }

    /**
     * 删除密钥修改日志信息
     * 
     * @param id 密钥修改日志主键
     * @return 结果
     */
    @Override
    public int deleteKeyUpLogById(Long id)
    {
        return keyUpLogMapper.deleteKeyUpLogById(id);
    }
}
