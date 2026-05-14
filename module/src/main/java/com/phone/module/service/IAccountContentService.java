package com.phone.module.service;

import java.util.List;
import com.phone.module.domain.AccountContent;

/**
 * 视频评论账号信息Service接口
 * 
 * @author ruoyi
 * @date 2026-01-09
 */
public interface IAccountContentService 
{
    /**
     * 查询视频评论账号信息
     * 
     * @param id 视频评论账号信息主键
     * @return 视频评论账号信息
     */
    public AccountContent selectAccountContentById(Long id);
    public Long selectAccountContentCountSum(String accountName);

    /**
     * 查询视频评论账号信息列表
     * 
     * @param accountContent 视频评论账号信息
     * @return 视频评论账号信息集合
     */
    public List<AccountContent> selectAccountContentList(AccountContent accountContent);
    public List<AccountContent> selectAccountSumList(String accountName);
    public List<AccountContent> selectFollowList(String accountName);

    public List<AccountContent> selectFansList(String accountName);
    public List<AccountContent> selectFansListByUid(String accountName);
    public List<AccountContent> selectFollowListByUid(String accountName);
    /**
     * 新增视频评论账号信息
     * 
     * @param accountContent 视频评论账号信息
     * @return 结果
     */
    public int insertAccountContent(AccountContent accountContent);
    public int insertFollow(AccountContent accountContent);
    public int insertFans(AccountContent accountContent);

    /**
     * 修改视频评论账号信息
     * 
     * @param accountContent 视频评论账号信息
     * @return 结果
     */
    public int updateAccountContent(AccountContent accountContent);

    /**
     * 批量删除视频评论账号信息
     * 
     * @param ids 需要删除的视频评论账号信息主键集合
     * @return 结果
     */
    public int deleteAccountContentByIds(Long[] ids);

    /**
     * 删除视频评论账号信息信息
     * 
     * @param id 视频评论账号信息主键
     * @return 结果
     */
    public int deleteAccountContentById(Long id);
}
