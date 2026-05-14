package com.phone.module.service;

import java.util.List;
import com.phone.module.domain.Video;

/**
 * videoService接口
 * 
 * @author ruoyi
 * @date 2025-12-08
 */
public interface IVideoService 
{
    /**
     * 查询video
     * 
     * @param id video主键
     * @return video
     */
    public Video selectVideoById(Long id);
    public Video selectVideoByUId(String uid);

    /**
     * 查询video列表
     * 
     * @param video video
     * @return video集合
     */
    public List<Video> selectVideoList(Video video);
    public List<Video> selectVideoList200(Video video);

    /**
     * 新增video
     * 
     * @param video video
     * @return 结果
     */
    public int insertVideo(Video video);

    /**
     * 修改video
     * 
     * @param video video
     * @return 结果
     */
    public int updateVideo(Video video);

    /**
     * 批量删除video
     * 
     * @param ids 需要删除的video主键集合
     * @return 结果
     */
    public int deleteVideoByIds(Long[] ids);

    /**
     * 删除video信息
     * 
     * @param id video主键
     * @return 结果
     */
    public int deleteVideoById(Long id);
}
