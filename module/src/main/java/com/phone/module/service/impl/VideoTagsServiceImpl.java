package com.phone.module.service.impl;

import java.util.Date;
import java.util.List;
import com.phone.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.phone.module.mapper.VideoTagsMapper;
import com.phone.module.domain.VideoTags;
import com.phone.module.service.IVideoTagsService;

/**
 * VideoTagsService业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-08
 */
@Service
public class VideoTagsServiceImpl implements IVideoTagsService 
{
    @Autowired
    private VideoTagsMapper videoTagsMapper;

    /**
     * 查询VideoTags
     * 
     * @param id VideoTags主键
     * @return VideoTags
     */
    @Override
    public VideoTags selectVideoTagsById(Long id)
    {
        return videoTagsMapper.selectVideoTagsById(id);
    }

    /**
     * 查询VideoTags列表
     * 
     * @param videoTags VideoTags
     * @return VideoTags
     */
    @Override
    public List<VideoTags> selectVideoTagsList(VideoTags videoTags)
    {
        return videoTagsMapper.selectVideoTagsList(videoTags);
    }

    /**
     * 新增VideoTags
     * 
     * @param videoTags VideoTags
     * @return 结果
     */
    @Override
    public int insertVideoTags(VideoTags videoTags)
    {
        videoTags.setCreateTime(DateUtils.getNowDate());
        return videoTagsMapper.insertVideoTags(videoTags);
    }

    /**
     * 修改VideoTags
     * 
     * @param videoTags VideoTags
     * @return 结果
     */
    @Override
    public int updateVideoTags(VideoTags videoTags)
    {
        videoTags.setUpdateTime(new Date());
        return videoTagsMapper.updateVideoTags(videoTags);
    }

    /**
     * 批量删除VideoTags
     * 
     * @param ids 需要删除的VideoTags主键
     * @return 结果
     */
    @Override
    public int deleteVideoTagsByIds(Long[] ids)
    {
        return videoTagsMapper.deleteVideoTagsByIds(ids);
    }

    /**
     * 删除VideoTags信息
     * 
     * @param id VideoTags主键
     * @return 结果
     */
    @Override
    public int deleteVideoTagsById(Long id)
    {
        return videoTagsMapper.deleteVideoTagsById(id);
    }
}
