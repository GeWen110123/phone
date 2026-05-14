package com.phone.module.service.impl;

import java.util.List;

import com.phone.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.phone.module.mapper.VideoMapper;
import com.phone.module.domain.Video;
import com.phone.module.service.IVideoService;

/**
 * videoService业务层处理
 *
 * @author ruoyi
 * @date 2025-12-08
 */
@Service
public class VideoServiceImpl implements IVideoService {
    @Autowired
    private VideoMapper videoMapper;

    /**
     * 查询video
     *
     * @param id video主键
     * @return video
     */
    @Override
    public Video selectVideoById(Long id) {
        return videoMapper.selectVideoById(id);
    }

    @Override
    public Video selectVideoByUId(String uid) {
        return videoMapper.selectVideoByUId(uid);
    }

    /**
     * 查询video列表
     *
     * @param video video
     * @return video
     */
    @Override
    public List<Video> selectVideoList(Video video) {
        return videoMapper.selectVideoList(video);
    }    @Override
    public List<Video> selectVideoList200(Video video) {
        return videoMapper.selectVideoList200(video);
    }

    /**
     * 新增video
     *
     * @param video video
     * @return 结果
     */
    @Override
    public int insertVideo(Video video) {
        video.setCreateTime(DateUtils.getNowDate());
        return videoMapper.insertVideo(video);
    }

    /**
     * 修改video
     *
     * @param video video
     * @return 结果
     */
    @Override
    public int updateVideo(Video video) {
        return videoMapper.updateVideo(video);
    }

    /**
     * 批量删除video
     *
     * @param ids 需要删除的video主键
     * @return 结果
     */
    @Override
    public int deleteVideoByIds(Long[] ids) {
        return videoMapper.deleteVideoByIds(ids);
    }

    /**
     * 删除video信息
     *
     * @param id video主键
     * @return 结果
     */
    @Override
    public int deleteVideoById(Long id) {
        return videoMapper.deleteVideoById(id);
    }
}
