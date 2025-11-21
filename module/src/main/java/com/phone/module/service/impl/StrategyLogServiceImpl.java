package com.phone.module.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.phone.module.mapper.StrategyLogMapper;
import com.phone.module.domain.StrategyLog;
import com.phone.module.service.IStrategyLogService;

/**
 * 策略执行日志Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-07
 */
@Service
public class StrategyLogServiceImpl implements IStrategyLogService 
{
    @Autowired
    private StrategyLogMapper strategyLogMapper;

    /**
     * 查询策略执行日志
     * 
     * @param id 策略执行日志主键
     * @return 策略执行日志
     */
    @Override
    public StrategyLog selectStrategyLogById(Long id)
    {
        return strategyLogMapper.selectStrategyLogById(id);
    }

    /**
     * 查询策略执行日志列表
     * 
     * @param strategyLog 策略执行日志
     * @return 策略执行日志
     */
    @Override
    public List<StrategyLog> selectStrategyLogList(StrategyLog strategyLog)
    {
        return strategyLogMapper.selectStrategyLogList(strategyLog);
    }

    /**
     * 新增策略执行日志
     * 
     * @param strategyLog 策略执行日志
     * @return 结果
     */
    @Override
    public int insertStrategyLog(StrategyLog strategyLog)
    {
        return strategyLogMapper.insertStrategyLog(strategyLog);
    }

    /**
     * 修改策略执行日志
     * 
     * @param strategyLog 策略执行日志
     * @return 结果
     */
    @Override
    public int updateStrategyLog(StrategyLog strategyLog)
    {
        return strategyLogMapper.updateStrategyLog(strategyLog);
    }

    /**
     * 批量删除策略执行日志
     * 
     * @param ids 需要删除的策略执行日志主键
     * @return 结果
     */
    @Override
    public int deleteStrategyLogByIds(Long[] ids)
    {
        return strategyLogMapper.deleteStrategyLogByIds(ids);
    }

    /**
     * 删除策略执行日志信息
     * 
     * @param id 策略执行日志主键
     * @return 结果
     */
    @Override
    public int deleteStrategyLogById(Long id)
    {
        return strategyLogMapper.deleteStrategyLogById(id);
    }
}
