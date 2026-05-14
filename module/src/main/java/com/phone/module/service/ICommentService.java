package com.phone.module.service;

import java.util.List;
import com.phone.module.domain.Comment;

/**
 * 抖音视频评论Service接口
 * 
 * @author ruoyi
 * @date 2026-03-09
 */
public interface ICommentService 
{
    /**
     * 查询抖音视频评论
     * 
     * @param id 抖音视频评论主键
     * @return 抖音视频评论
     */
    public Comment selectCommentById(Long id);

    /**
     * 查询抖音视频评论列表
     * 
     * @param comment 抖音视频评论
     * @return 抖音视频评论集合
     */
    public List<Comment> selectCommentList(Comment comment);
    public List<Comment> selectCommentListByUser(Comment comment);
    public List<Comment> selectCommentList200(Comment comment);

    /**
     * 新增抖音视频评论
     * 
     * @param comment 抖音视频评论
     * @return 结果
     */
    public int insertComment(Comment comment);

    /**
     * 修改抖音视频评论
     * 
     * @param comment 抖音视频评论
     * @return 结果
     */
    public int updateComment(Comment comment);
    public int updateCommentByUserId(Comment comment);

    /**
     * 批量删除抖音视频评论
     * 
     * @param ids 需要删除的抖音视频评论主键集合
     * @return 结果
     */
    public int deleteCommentByIds(Long[] ids);

    /**
     * 删除抖音视频评论信息
     * 
     * @param id 抖音视频评论主键
     * @return 结果
     */
    public int deleteCommentById(Long id);
}
