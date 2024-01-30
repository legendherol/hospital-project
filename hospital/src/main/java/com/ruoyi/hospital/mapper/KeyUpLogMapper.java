package com.ruoyi.hospital.mapper;

import java.util.List;
import com.ruoyi.hospital.domain.KeyUpLog;

/**
 * 密钥修改日志Mapper接口
 * 
 * @author ruoyi
 * @date 2023-06-07
 */
public interface KeyUpLogMapper 
{
    /**
     * 查询密钥修改日志
     * 
     * @param id 密钥修改日志主键
     * @return 密钥修改日志
     */
    public KeyUpLog selectKeyUpLogById(Long id);

    /**
     * 查询密钥修改日志列表
     * 
     * @param keyUpLog 密钥修改日志
     * @return 密钥修改日志集合
     */
    public List<KeyUpLog> selectKeyUpLogList(KeyUpLog keyUpLog);

    /**
     * 新增密钥修改日志
     * 
     * @param keyUpLog 密钥修改日志
     * @return 结果
     */
    public int insertKeyUpLog(KeyUpLog keyUpLog);

    /**
     * 修改密钥修改日志
     * 
     * @param keyUpLog 密钥修改日志
     * @return 结果
     */
    public int updateKeyUpLog(KeyUpLog keyUpLog);

    /**
     * 删除密钥修改日志
     * 
     * @param id 密钥修改日志主键
     * @return 结果
     */
    public int deleteKeyUpLogById(Long id);

    /**
     * 批量删除密钥修改日志
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteKeyUpLogByIds(Long[] ids);
}
