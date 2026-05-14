package com.phone.module.service.impl;

import java.util.List;

import com.phone.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.phone.module.mapper.AccountContentMapper;
import com.phone.module.domain.AccountContent;
import com.phone.module.service.IAccountContentService;

/**
 * 视频评论账号信息Service业务层处理
 *
 * @author ruoyi
 * @date 2026-01-09
 */
@Service
public class AccountContentServiceImpl implements IAccountContentService {
    @Autowired
    private AccountContentMapper accountContentMapper;

    /**
     * 查询视频评论账号信息
     *
     * @param id 视频评论账号信息主键
     * @return 视频评论账号信息
     */
    @Override
    public AccountContent selectAccountContentById(Long id) {
        return accountContentMapper.selectAccountContentById(id);
    }

    @Override
    public Long selectAccountContentCountSum(String accountName) {
        return accountContentMapper.selectAccountContentCountSum(accountName);
    }

    /**
     * 查询视频评论账号信息列表
     *
     * @param accountContent 视频评论账号信息
     * @return 视频评论账号信息
     */
    @Override
    public List<AccountContent> selectAccountContentList(AccountContent accountContent) {
        return accountContentMapper.selectAccountContentList(accountContent);
    }

    @Override
    public List<AccountContent> selectAccountSumList(String accountName) {
        return accountContentMapper.selectAccountSumList(accountName);
    }

    @Override
    public List<AccountContent> selectFollowList(String accountName) {
        return accountContentMapper.selectFollowList(accountName);
    }

    @Override
    public List<AccountContent> selectFansList(String accountName) {
        return accountContentMapper.selectFansList(accountName);
    }

    @Override
    public List<AccountContent> selectFansListByUid(String accountName) {
        return accountContentMapper.selectFansListByUid(accountName);
    }

    @Override
    public List<AccountContent> selectFollowListByUid(String accountName) {
        return accountContentMapper.selectFollowListByUid(accountName);
    }

    /**
     * 新增视频评论账号信息
     *
     * @param accountContent 视频评论账号信息
     * @return 结果
     */
    @Override
    public int insertAccountContent(AccountContent accountContent) {
        accountContent.setCreateTime(DateUtils.getNowDate());
        return accountContentMapper.insertAccountContent(accountContent);
    }

    @Override
    public int insertFollow(AccountContent accountContent) {
        accountContent.setCreateTime(DateUtils.getNowDate());
        return accountContentMapper.insertFollow(accountContent);
    }
    @Override
    public int insertFans(AccountContent accountContent) {
        accountContent.setCreateTime(DateUtils.getNowDate());
        return accountContentMapper.insertFans(accountContent);
    }

    /**
     * 修改视频评论账号信息
     *
     * @param accountContent 视频评论账号信息
     * @return 结果
     */
    @Override
    public int updateAccountContent(AccountContent accountContent) {
        accountContent.setUpdateTime(DateUtils.getNowDate());
        return accountContentMapper.updateAccountContent(accountContent);
    }

    /**
     * 批量删除视频评论账号信息
     *
     * @param ids 需要删除的视频评论账号信息主键
     * @return 结果
     */
    @Override
    public int deleteAccountContentByIds(Long[] ids) {
        return accountContentMapper.deleteAccountContentByIds(ids);
    }

    /**
     * 删除视频评论账号信息信息
     *
     * @param id 视频评论账号信息主键
     * @return 结果
     */
    @Override
    public int deleteAccountContentById(Long id) {
        return accountContentMapper.deleteAccountContentById(id);
    }
}
