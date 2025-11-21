package com.phone.module.service.impl;

import java.util.List;
import com.phone.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.phone.module.mapper.TaskTemplateMapper;
import com.phone.module.domain.TaskTemplate;
import com.phone.module.service.ITaskTemplateService;

/**
 * 任务模板Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-07
 */
@Service
public class TaskTemplateServiceImpl implements ITaskTemplateService 
{
    @Autowired
    private TaskTemplateMapper taskTemplateMapper;

    /**
     * 查询任务模板
     * 
     * @param id 任务模板主键
     * @return 任务模板
     */
    @Override
    public TaskTemplate selectTaskTemplateById(Long id)
    {
        return taskTemplateMapper.selectTaskTemplateById(id);
    }

    /**
     * 查询任务模板列表
     * 
     * @param taskTemplate 任务模板
     * @return 任务模板
     */
    @Override
    public List<TaskTemplate> selectTaskTemplateList(TaskTemplate taskTemplate)
    {
        return taskTemplateMapper.selectTaskTemplateList(taskTemplate);
    }

    /**
     * 新增任务模板
     * 
     * @param taskTemplate 任务模板
     * @return 结果
     */
    @Override
    public int insertTaskTemplate(TaskTemplate taskTemplate)
    {
        taskTemplate.setCreateTime(DateUtils.getNowDate());
        return taskTemplateMapper.insertTaskTemplate(taskTemplate);
    }

    /**
     * 修改任务模板
     * 
     * @param taskTemplate 任务模板
     * @return 结果
     */
    @Override
    public int updateTaskTemplate(TaskTemplate taskTemplate)
    {
        return taskTemplateMapper.updateTaskTemplate(taskTemplate);
    }

    /**
     * 批量删除任务模板
     * 
     * @param ids 需要删除的任务模板主键
     * @return 结果
     */
    @Override
    public int deleteTaskTemplateByIds(Long[] ids)
    {
        return taskTemplateMapper.deleteTaskTemplateByIds(ids);
    }

    /**
     * 删除任务模板信息
     * 
     * @param id 任务模板主键
     * @return 结果
     */
    @Override
    public int deleteTaskTemplateById(Long id)
    {
        return taskTemplateMapper.deleteTaskTemplateById(id);
    }
}
