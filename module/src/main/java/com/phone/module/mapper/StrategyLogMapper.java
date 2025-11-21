package com.phone.module.mapper;

import java.util.List;
import com.phone.module.domain.StrategyLog;

/**
 * 策略执行日志Mapper接口
 * 
 * @author ruoyi
 * @date 2025-11-07
 */
public interface StrategyLogMapper 
{
    /**
     * 查询策略执行日志
     * 
     * @param id 策略执行日志主键
     * @return 策略执行日志
     */
    public StrategyLog selectStrategyLogById(Long id);

    /**
     * 查询策略执行日志列表
     * 
     * @param strategyLog 策略执行日志
     * @return 策略执行日志集合
     */
    public List<StrategyLog> selectStrategyLogList(StrategyLog strategyLog);

    /**
     * 新增策略执行日志
     * 
     * @param strategyLog 策略执行日志
     * @return 结果
     */
    public int insertStrategyLog(StrategyLog strategyLog);

    /**
     * 修改策略执行日志
     * 
     * @param strategyLog 策略执行日志
     * @return 结果
     */
    public int updateStrategyLog(StrategyLog strategyLog);

    /**
     * 删除策略执行日志
     * 
     * @param id 策略执行日志主键
     * @return 结果
     */
    public int deleteStrategyLogById(Long id);

    /**
     * 批量删除策略执行日志
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStrategyLogByIds(Long[] ids);
}
