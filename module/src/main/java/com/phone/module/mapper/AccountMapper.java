package com.phone.module.mapper;

import java.util.List;
import com.phone.module.domain.Account;

/**
 * 抖音账号信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-11-10
 */
public interface AccountMapper 
{
    /**
     * 查询抖音账号信息
     * 
     * @param id 抖音账号信息主键
     * @return 抖音账号信息
     */
    public Account selectAccountById(Long id);
    public Account selectByDouyinId(String douyinId);

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
     * 删除抖音账号信息
     * 
     * @param id 抖音账号信息主键
     * @return 结果
     */
    public int deleteAccountById(Long id);

    /**
     * 批量删除抖音账号信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAccountByIds(Long[] ids);
}
