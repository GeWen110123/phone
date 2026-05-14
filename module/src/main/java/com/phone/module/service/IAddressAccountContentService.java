package com.phone.module.service;

import java.util.List;
import com.phone.module.domain.AddressAccountContent;

/**
 * 地址评论账号信息Service接口
 * 
 * @author ruoyi
 * @date 2026-01-09
 */
public interface IAddressAccountContentService 
{
    /**
     * 查询地址评论账号信息
     * 
     * @param id 地址评论账号信息主键
     * @return 地址评论账号信息
     */
    public AddressAccountContent selectAddressAccountContentById(Long id);

    /**
     * 查询地址评论账号信息列表
     * 
     * @param addressAccountContent 地址评论账号信息
     * @return 地址评论账号信息集合
     */
    public List<AddressAccountContent> selectAddressAccountContentList(AddressAccountContent addressAccountContent);

    /**
     * 新增地址评论账号信息
     * 
     * @param addressAccountContent 地址评论账号信息
     * @return 结果
     */
    public int insertAddressAccountContent(AddressAccountContent addressAccountContent);

    /**
     * 修改地址评论账号信息
     * 
     * @param addressAccountContent 地址评论账号信息
     * @return 结果
     */
    public int updateAddressAccountContent(AddressAccountContent addressAccountContent);

    /**
     * 批量删除地址评论账号信息
     * 
     * @param ids 需要删除的地址评论账号信息主键集合
     * @return 结果
     */
    public int deleteAddressAccountContentByIds(Long[] ids);

    /**
     * 删除地址评论账号信息信息
     * 
     * @param id 地址评论账号信息主键
     * @return 结果
     */
    public int deleteAddressAccountContentById(Long id);
}
