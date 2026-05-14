package com.phone.module.service.impl;

import java.util.List;
import com.phone.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.phone.module.mapper.AddressAccountContentMapper;
import com.phone.module.domain.AddressAccountContent;
import com.phone.module.service.IAddressAccountContentService;

/**
 * 地址评论账号信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-09
 */
@Service
public class AddressAccountContentServiceImpl implements IAddressAccountContentService 
{
    @Autowired
    private AddressAccountContentMapper addressAccountContentMapper;

    /**
     * 查询地址评论账号信息
     * 
     * @param id 地址评论账号信息主键
     * @return 地址评论账号信息
     */
    @Override
    public AddressAccountContent selectAddressAccountContentById(Long id)
    {
        return addressAccountContentMapper.selectAddressAccountContentById(id);
    }

    /**
     * 查询地址评论账号信息列表
     * 
     * @param addressAccountContent 地址评论账号信息
     * @return 地址评论账号信息
     */
    @Override
    public List<AddressAccountContent> selectAddressAccountContentList(AddressAccountContent addressAccountContent)
    {
        return addressAccountContentMapper.selectAddressAccountContentList(addressAccountContent);
    }

    /**
     * 新增地址评论账号信息
     * 
     * @param addressAccountContent 地址评论账号信息
     * @return 结果
     */
    @Override
    public int insertAddressAccountContent(AddressAccountContent addressAccountContent)
    {
        addressAccountContent.setCreateTime(DateUtils.getNowDate());
        return addressAccountContentMapper.insertAddressAccountContent(addressAccountContent);
    }

    /**
     * 修改地址评论账号信息
     * 
     * @param addressAccountContent 地址评论账号信息
     * @return 结果
     */
    @Override
    public int updateAddressAccountContent(AddressAccountContent addressAccountContent)
    {
        addressAccountContent.setUpdateTime(DateUtils.getNowDate());
        return addressAccountContentMapper.updateAddressAccountContent(addressAccountContent);
    }

    /**
     * 批量删除地址评论账号信息
     * 
     * @param ids 需要删除的地址评论账号信息主键
     * @return 结果
     */
    @Override
    public int deleteAddressAccountContentByIds(Long[] ids)
    {
        return addressAccountContentMapper.deleteAddressAccountContentByIds(ids);
    }

    /**
     * 删除地址评论账号信息信息
     * 
     * @param id 地址评论账号信息主键
     * @return 结果
     */
    @Override
    public int deleteAddressAccountContentById(Long id)
    {
        return addressAccountContentMapper.deleteAddressAccountContentById(id);
    }
}
