package com.phone.module.service;

import java.util.List;
import com.phone.module.domain.AccountIp;

/**
 * 账号地址历史Service接口
 * 
 * @author ruoyi
 * @date 2026-02-04
 */
public interface IAccountIpService 
{
    /**
     * 查询账号地址历史
     * 
     * @param id 账号地址历史主键
     * @return 账号地址历史
     */
    public AccountIp selectAccountIpById(Long id);

    /**
     * 查询账号地址历史列表
     * 
     * @param accountIp 账号地址历史
     * @return 账号地址历史集合
     */
    public List<AccountIp> selectAccountIpList(AccountIp accountIp);

    /**
     * 新增账号地址历史
     * 
     * @param accountIp 账号地址历史
     * @return 结果
     */
    public int insertAccountIp(AccountIp accountIp);

    /**
     * 修改账号地址历史
     * 
     * @param accountIp 账号地址历史
     * @return 结果
     */
    public int updateAccountIp(AccountIp accountIp);

    /**
     * 批量删除账号地址历史
     * 
     * @param ids 需要删除的账号地址历史主键集合
     * @return 结果
     */
    public int deleteAccountIpByIds(Long[] ids);

    /**
     * 删除账号地址历史信息
     * 
     * @param id 账号地址历史主键
     * @return 结果
     */
    public int deleteAccountIpById(Long id);
}
