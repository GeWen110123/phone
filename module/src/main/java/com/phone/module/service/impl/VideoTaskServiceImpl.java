package com.phone.module.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.phone.module.mapper.VideoTaskMapper;
import com.phone.module.domain.VideoTask;
import com.phone.module.service.IVideoTaskService;

/**
 * video_taskService业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-09
 */
@Service
public class VideoTaskServiceImpl implements IVideoTaskService 
{
    @Autowired
    private VideoTaskMapper videoTaskMapper;

    /**
     * 查询video_task
     * 
     * @param id video_task主键
     * @return video_task
     */
    @Override
    public VideoTask selectVideoTaskById(Long id)
    {
        return videoTaskMapper.selectVideoTaskById(id);
    }

    /**
     * 查询video_task列表
     * 
     * @param videoTask video_task
     * @return video_task
     */
    @Override
    public List<VideoTask> selectVideoTaskList(VideoTask videoTask)
    {
        return videoTaskMapper.selectVideoTaskList(videoTask);
    }

    /**
     * 新增video_task
     * 
     * @param videoTask video_task
     * @return 结果
     */
    @Override
    public int insertVideoTask(VideoTask videoTask)
    {
        return videoTaskMapper.insertVideoTask(videoTask);
    }

    /**
     * 修改video_task
     * 
     * @param videoTask video_task
     * @return 结果
     */
    @Override
    public int updateVideoTask(VideoTask videoTask)
    {
        return videoTaskMapper.updateVideoTask(videoTask);
    }

    /**
     * 批量删除video_task
     * 
     * @param ids 需要删除的video_task主键
     * @return 结果
     */
    @Override
    public int deleteVideoTaskByIds(Long[] ids)
    {
        return videoTaskMapper.deleteVideoTaskByIds(ids);
    }

    /**
     * 删除video_task信息
     * 
     * @param id video_task主键
     * @return 结果
     */
    @Override
    public int deleteVideoTaskById(Long id)
    {
        return videoTaskMapper.deleteVideoTaskById(id);
    }
}
