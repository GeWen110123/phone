package com.phone.module.mapper;

import java.util.List;
import com.phone.module.domain.VideoTags;

/**
 * VideoTagsMapper接口
 * 
 * @author ruoyi
 * @date 2025-12-08
 */
public interface VideoTagsMapper 
{
    /**
     * 查询VideoTags
     * 
     * @param id VideoTags主键
     * @return VideoTags
     */
    public VideoTags selectVideoTagsById(Long id);

    /**
     * 查询VideoTags列表
     * 
     * @param videoTags VideoTags
     * @return VideoTags集合
     */
    public List<VideoTags> selectVideoTagsList(VideoTags videoTags);

    /**
     * 新增VideoTags
     * 
     * @param videoTags VideoTags
     * @return 结果
     */
    public int insertVideoTags(VideoTags videoTags);

    /**
     * 修改VideoTags
     * 
     * @param videoTags VideoTags
     * @return 结果
     */
    public int updateVideoTags(VideoTags videoTags);

    /**
     * 删除VideoTags
     * 
     * @param id VideoTags主键
     * @return 结果
     */
    public int deleteVideoTagsById(Long id);

    /**
     * 批量删除VideoTags
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteVideoTagsByIds(Long[] ids);
}
