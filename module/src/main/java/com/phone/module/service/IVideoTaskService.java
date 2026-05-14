package com.phone.module.service;

import java.util.List;
import com.phone.module.domain.VideoTask;

/**
 * video_taskService接口
 * 
 * @author ruoyi
 * @date 2025-12-09
 */
public interface IVideoTaskService 
{
    /**
     * 查询video_task
     * 
     * @param id video_task主键
     * @return video_task
     */
    public VideoTask selectVideoTaskById(Long id);

    /**
     * 查询video_task列表
     * 
     * @param videoTask video_task
     * @return video_task集合
     */
    public List<VideoTask> selectVideoTaskList(VideoTask videoTask);

    /**
     * 新增video_task
     * 
     * @param videoTask video_task
     * @return 结果
     */
    public int insertVideoTask(VideoTask videoTask);

    /**
     * 修改video_task
     * 
     * @param videoTask video_task
     * @return 结果
     */
    public int updateVideoTask(VideoTask videoTask);

    /**
     * 批量删除video_task
     * 
     * @param ids 需要删除的video_task主键集合
     * @return 结果
     */
    public int deleteVideoTaskByIds(Long[] ids);

    /**
     * 删除video_task信息
     * 
     * @param id video_task主键
     * @return 结果
     */
    public int deleteVideoTaskById(Long id);
}
