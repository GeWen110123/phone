package com.phone.module.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.phone.module.mapper.TaskLogMapper;
import com.phone.module.domain.TaskLog;
import com.phone.module.service.ITaskLogService;

/**
 * 任务执行日志Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-07
 */
@Service
public class TaskLogServiceImpl implements ITaskLogService 
{
    @Autowired
    private TaskLogMapper taskLogMapper;

    /**
     * 查询任务执行日志
     * 
     * @param id 任务执行日志主键
     * @return 任务执行日志
     */
    @Override
    public TaskLog selectTaskLogById(Long id)
    {
        return taskLogMapper.selectTaskLogById(id);
    }

    /**
     * 查询任务执行日志列表
     * 
     * @param taskLog 任务执行日志
     * @return 任务执行日志
     */
    @Override
    public List<TaskLog> selectTaskLogList(TaskLog taskLog)
    {
        return taskLogMapper.selectTaskLogList(taskLog);
    }

    /**
     * 新增任务执行日志
     * 
     * @param taskLog 任务执行日志
     * @return 结果
     */
    @Override
    public int insertTaskLog(TaskLog taskLog)
    {
        return taskLogMapper.insertTaskLog(taskLog);
    }

    /**
     * 修改任务执行日志
     * 
     * @param taskLog 任务执行日志
     * @return 结果
     */
    @Override
    public int updateTaskLog(TaskLog taskLog)
    {
        return taskLogMapper.updateTaskLog(taskLog);
    }

    /**
     * 批量删除任务执行日志
     * 
     * @param ids 需要删除的任务执行日志主键
     * @return 结果
     */
    @Override
    public int deleteTaskLogByIds(Long[] ids)
    {
        return taskLogMapper.deleteTaskLogByIds(ids);
    }

    /**
     * 删除任务执行日志信息
     * 
     * @param id 任务执行日志主键
     * @return 结果
     */
    @Override
    public int deleteTaskLogById(Long id)
    {
        return taskLogMapper.deleteTaskLogById(id);
    }
}
