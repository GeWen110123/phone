package com.phone.module.service;

import java.util.List;
import com.phone.module.domain.TaskTemplate;

/**
 * 任务模板Service接口
 * 
 * @author ruoyi
 * @date 2025-11-07
 */
public interface ITaskTemplateService 
{
    /**
     * 查询任务模板
     * 
     * @param id 任务模板主键
     * @return 任务模板
     */
    public TaskTemplate selectTaskTemplateById(Long id);

    /**
     * 查询任务模板列表
     * 
     * @param taskTemplate 任务模板
     * @return 任务模板集合
     */
    public List<TaskTemplate> selectTaskTemplateList(TaskTemplate taskTemplate);

    /**
     * 新增任务模板
     * 
     * @param taskTemplate 任务模板
     * @return 结果
     */
    public int insertTaskTemplate(TaskTemplate taskTemplate);

    /**
     * 修改任务模板
     * 
     * @param taskTemplate 任务模板
     * @return 结果
     */
    public int updateTaskTemplate(TaskTemplate taskTemplate);

    /**
     * 批量删除任务模板
     * 
     * @param ids 需要删除的任务模板主键集合
     * @return 结果
     */
    public int deleteTaskTemplateByIds(Long[] ids);

    /**
     * 删除任务模板信息
     * 
     * @param id 任务模板主键
     * @return 结果
     */
    public int deleteTaskTemplateById(Long id);
}
