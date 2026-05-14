package com.phone.module.service.impl;

import java.util.List;
import com.phone.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.phone.module.mapper.CommentMapper;
import com.phone.module.domain.Comment;
import com.phone.module.service.ICommentService;

/**
 * 抖音视频评论Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-09
 */
@Service
public class CommentServiceImpl implements ICommentService 
{
    @Autowired
    private CommentMapper commentMapper;

    /**
     * 查询抖音视频评论
     * 
     * @param id 抖音视频评论主键
     * @return 抖音视频评论
     */
    @Override
    public Comment selectCommentById(Long id)
    {
        return commentMapper.selectCommentById(id);
    }

    /**
     * 查询抖音视频评论列表
     * 
     * @param comment 抖音视频评论
     * @return 抖音视频评论
     */
    @Override
    public List<Comment> selectCommentList(Comment comment)
    {
        return commentMapper.selectCommentList(comment);
    }    @Override
    public List<Comment> selectCommentListByUser(Comment comment)
    {
        return commentMapper.selectCommentListByUser(comment);
    }
    @Override
    public List<Comment> selectCommentList200(Comment comment)
    {
        return commentMapper.selectCommentList200(comment);
    }

    /**
     * 新增抖音视频评论
     * 
     * @param comment 抖音视频评论
     * @return 结果
     */
    @Override
    public int insertComment(Comment comment)
    {
        comment.setCreateTime(DateUtils.getNowDate());
        return commentMapper.insertComment(comment);
    }

    /**
     * 修改抖音视频评论
     * 
     * @param comment 抖音视频评论
     * @return 结果
     */
    @Override
    public int updateComment(Comment comment)
    {
        return commentMapper.updateComment(comment);
    }
    @Override
    public int updateCommentByUserId(Comment comment)
    {
        return commentMapper.updateCommentByUserId(comment);
    }

    /**
     * 批量删除抖音视频评论
     * 
     * @param ids 需要删除的抖音视频评论主键
     * @return 结果
     */
    @Override
    public int deleteCommentByIds(Long[] ids)
    {
        return commentMapper.deleteCommentByIds(ids);
    }

    /**
     * 删除抖音视频评论信息
     * 
     * @param id 抖音视频评论主键
     * @return 结果
     */
    @Override
    public int deleteCommentById(Long id)
    {
        return commentMapper.deleteCommentById(id);
    }
}
