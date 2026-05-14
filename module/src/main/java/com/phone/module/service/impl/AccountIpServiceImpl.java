package com.phone.module.service.impl;

import java.util.List;
import com.phone.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.phone.module.mapper.AccountIpMapper;
import com.phone.module.domain.AccountIp;
import com.phone.module.service.IAccountIpService;

/**
 * 账号地址历史Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-02-04
 */
@Service
public class AccountIpServiceImpl implements IAccountIpService 
{
    @Autowired
    private AccountIpMapper accountIpMapper;

    /**
     * 查询账号地址历史
     * 
     * @param id 账号地址历史主键
     * @return 账号地址历史
     */
    @Override
    public AccountIp selectAccountIpById(Long id)
    {
        return accountIpMapper.selectAccountIpById(id);
    }

    /**
     * 查询账号地址历史列表
     * 
     * @param accountIp 账号地址历史
     * @return 账号地址历史
     */
    @Override
    public List<AccountIp> selectAccountIpList(AccountIp accountIp)
    {
        return accountIpMapper.selectAccountIpList(accountIp);
    }

    /**
     * 新增账号地址历史
     * 
     * @param accountIp 账号地址历史
     * @return 结果
     */
    @Override
    public int insertAccountIp(AccountIp accountIp)
    {
        accountIp.setCreateTime(DateUtils.getNowDate());
        return accountIpMapper.insertAccountIp(accountIp);
    }

    /**
     * 修改账号地址历史
     * 
     * @param accountIp 账号地址历史
     * @return 结果
     */
    @Override
    public int updateAccountIp(AccountIp accountIp)
    {
        return accountIpMapper.updateAccountIp(accountIp);
    }

    /**
     * 批量删除账号地址历史
     * 
     * @param ids 需要删除的账号地址历史主键
     * @return 结果
     */
    @Override
    public int deleteAccountIpByIds(Long[] ids)
    {
        return accountIpMapper.deleteAccountIpByIds(ids);
    }

    /**
     * 删除账号地址历史信息
     * 
     * @param id 账号地址历史主键
     * @return 结果
     */
    @Override
    public int deleteAccountIpById(Long id)
    {
        return accountIpMapper.deleteAccountIpById(id);
    }
}
