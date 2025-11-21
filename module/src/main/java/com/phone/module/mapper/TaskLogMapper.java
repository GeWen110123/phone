package com.phone.module.mapper;

import java.util.List;
import com.phone.module.domain.TaskLog;

/**
 * 任务执行日志Mapper接口
 * 
 * @author ruoyi
 * @date 2025-11-07
 */
public interface TaskLogMapper 
{
    /**
     * 查询任务执行日志
     * 
     * @param id 任务执行日志主键
     * @return 任务执行日志
     */
    public TaskLog selectTaskLogById(Long id);

    /**
     * 查询任务执行日志列表
     * 
     * @param taskLog 任务执行日志
     * @return 任务执行日志集合
     */
    public List<TaskLog> selectTaskLogList(TaskLog taskLog);

    /**
     * 新增任务执行日志
     * 
     * @param taskLog 任务执行日志
     * @return 结果
     */
    public int insertTaskLog(TaskLog taskLog);

    /**
     * 修改任务执行日志
     * 
     * @param taskLog 任务执行日志
     * @return 结果
     */
    public int updateTaskLog(TaskLog taskLog);

    /**
     * 删除任务执行日志
     * 
     * @param id 任务执行日志主键
     * @return 结果
     */
    public int deleteTaskLogById(Long id);

    /**
     * 批量删除任务执行日志
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTaskLogByIds(Long[] ids);
}
