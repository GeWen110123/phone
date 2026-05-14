package com.phone.module.service;

import java.util.List;
import com.phone.module.domain.RunLog;

/**
 * 运行日志Service接口
 * 
 * @author phone
 * @date 2026-03-06
 */
public interface IRunLogService 
{
    /**
     * 查询运行日志
     * 
     * @param id 运行日志主键
     * @return 运行日志
     */
    public RunLog selectRunLogById(Long id);

    /**
     * 查询运行日志列表
     * 
     * @param runLog 运行日志
     * @return 运行日志集合
     */
    public List<RunLog> selectRunLogList(RunLog runLog);

    /**
     * 新增运行日志
     * 
     * @param runLog 运行日志
     * @return 结果
     */
    public int insertRunLog(RunLog runLog);

    /**
     * 修改运行日志
     * 
     * @param runLog 运行日志
     * @return 结果
     */
    public int updateRunLog(RunLog runLog);

    /**
     * 批量删除运行日志
     * 
     * @param ids 需要删除的运行日志主键集合
     * @return 结果
     */
    public int deleteRunLogByIds(Long[] ids);

    /**
     * 删除运行日志信息
     * 
     * @param id 运行日志主键
     * @return 结果
     */
    public int deleteRunLogById(Long id);
}
