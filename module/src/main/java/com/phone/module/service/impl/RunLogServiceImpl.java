package com.phone.module.service.impl;

import java.util.List;
import com.phone.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.phone.module.mapper.RunLogMapper;
import com.phone.module.domain.RunLog;
import com.phone.module.service.IRunLogService;

/**
 * 运行日志Service业务层处理
 * 
 * @author phone
 * @date 2026-03-06
 */
@Service
public class RunLogServiceImpl implements IRunLogService 
{
    @Autowired
    private RunLogMapper runLogMapper;

    /**
     * 查询运行日志
     * 
     * @param id 运行日志主键
     * @return 运行日志
     */
    @Override
    public RunLog selectRunLogById(Long id)
    {
        return runLogMapper.selectRunLogById(id);
    }

    /**
     * 查询运行日志列表
     * 
     * @param runLog 运行日志
     * @return 运行日志
     */
    @Override
    public List<RunLog> selectRunLogList(RunLog runLog)
    {
        return runLogMapper.selectRunLogList(runLog);
    }

    /**
     * 新增运行日志
     * 
     * @param runLog 运行日志
     * @return 结果
     */
    @Override
    public int insertRunLog(RunLog runLog)
    {
        runLog.setCreateTime(DateUtils.getNowDate());
        return runLogMapper.insertRunLog(runLog);
    }

    /**
     * 修改运行日志
     * 
     * @param runLog 运行日志
     * @return 结果
     */
    @Override
    public int updateRunLog(RunLog runLog)
    {
        return runLogMapper.updateRunLog(runLog);
    }

    /**
     * 批量删除运行日志
     * 
     * @param ids 需要删除的运行日志主键
     * @return 结果
     */
    @Override
    public int deleteRunLogByIds(Long[] ids)
    {
        return runLogMapper.deleteRunLogByIds(ids);
    }

    /**
     * 删除运行日志信息
     * 
     * @param id 运行日志主键
     * @return 结果
     */
    @Override
    public int deleteRunLogById(Long id)
    {
        return runLogMapper.deleteRunLogById(id);
    }
}
