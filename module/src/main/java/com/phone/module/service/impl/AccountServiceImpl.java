package com.phone.module.service.impl;

import java.util.List;
import com.phone.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.phone.module.mapper.AccountMapper;
import com.phone.module.domain.Account;
import com.phone.module.service.IAccountService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 抖音账号信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-10
 */
@Service
public class AccountServiceImpl implements IAccountService 
{
    @Autowired
    private AccountMapper accountMapper;

    /**
     * 查询抖音账号信息
     * 
     * @param id 抖音账号信息主键
     * @return 抖音账号信息
     */
    @Override
    public Account selectAccountById(Long id)
    {
        return accountMapper.selectAccountById(id);
    }
    public Account selectByDouyinId(String douyinId)
    {
        return accountMapper.selectByDouyinId(douyinId);
    }

    /**
     * 查询抖音账号信息列表
     * 
     * @param account 抖音账号信息
     * @return 抖音账号信息
     */
    @Override
    public List<Account> selectAccountList(Account account)
    {
        return accountMapper.selectAccountList(account);
    }

    /**
     * 新增抖音账号信息
     * 
     * @param account 抖音账号信息
     * @return 结果
     */
    @Override
    public int insertAccount(Account account)
    {

        Account exists = accountMapper.selectByDouyinId(account.getDouyinId());
        if (exists == null) {
            account.setCreateTime(DateUtils.getNowDate());
            return accountMapper.insertAccount(account);
        }else {
            return 0;
        }

    }

    /**
     * 修改抖音账号信息
     * 
     * @param account 抖音账号信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateAccount(Account account)
    {
        return accountMapper.updateAccount(account);
    }

    /**
     * 批量删除抖音账号信息
     * 
     * @param ids 需要删除的抖音账号信息主键
     * @return 结果
     */
    @Override
    public int deleteAccountByIds(Long[] ids)
    {
        return accountMapper.deleteAccountByIds(ids);
    }

    /**
     * 删除抖音账号信息信息
     * 
     * @param id 抖音账号信息主键
     * @return 结果
     */
    @Override
    public int deleteAccountById(Long id)
    {
        return accountMapper.deleteAccountById(id);
    }
}
