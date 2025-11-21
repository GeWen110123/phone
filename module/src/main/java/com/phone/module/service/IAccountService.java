package com.phone.module.service;

import java.util.List;
import com.phone.module.domain.Account;

/**
 * 抖音账号信息Service接口
 * 
 * @author ruoyi
 * @date 2025-11-10
 */
public interface IAccountService 
{
    /**
     * 查询抖音账号信息
     * 
     * @param id 抖音账号信息主键
     * @return 抖音账号信息
     */
    public Account selectAccountById(Long id);

    /**
     * 查询抖音账号信息列表
     * 
     * @param account 抖音账号信息
     * @return 抖音账号信息集合
     */
    public List<Account> selectAccountList(Account account);

    /**
     * 新增抖音账号信息
     * 
     * @param account 抖音账号信息
     * @return 结果
     */
    public int insertAccount(Account account);

    /**
     * 修改抖音账号信息
     * 
     * @param account 抖音账号信息
     * @return 结果
     */
    public int updateAccount(Account account);

    /**
     * 批量删除抖音账号信息
     * 
     * @param ids 需要删除的抖音账号信息主键集合
     * @return 结果
     */
    public int deleteAccountByIds(Long[] ids);

    /**
     * 删除抖音账号信息信息
     * 
     * @param id 抖音账号信息主键
     * @return 结果
     */
    public int deleteAccountById(Long id);
}
