package com.phone.module.mapper;

import java.util.List;
import com.phone.module.domain.AddressVideo;

/**
 * 地址视频总Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-09
 */
public interface AddressVideoMapper 
{
    /**
     * 查询地址视频总
     * 
     * @param uid 地址视频总主键
     * @return 地址视频总
     */
    public AddressVideo selectAddressVideoByUid(String uid);

    /**
     * 查询地址视频总列表
     * 
     * @param addressVideo 地址视频总
     * @return 地址视频总集合
     */
    public List<AddressVideo> selectAddressVideoList(AddressVideo addressVideo);
    public List<AddressVideo> selectAddressVideoList200(AddressVideo addressVideo);
    public List<String> selectAddressListByIp(AddressVideo addressVideo);
    public List<AddressVideo> selectAllVideoDId(AddressVideo addressVideo);

    /**
     * 新增地址视频总
     * 
     * @param addressVideo 地址视频总
     * @return 结果
     */
    public int insertAddressVideo(AddressVideo addressVideo);

    /**
     * 修改地址视频总
     * 
     * @param addressVideo 地址视频总
     * @return 结果
     */
    public int updateAddressVideo(AddressVideo addressVideo);

    /**
     * 删除地址视频总
     * 
     * @param uid 地址视频总主键
     * @return 结果
     */
    public int deleteAddressVideoByUid(String uid);

    /**
     * 批量删除地址视频总
     * 
     * @param uids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAddressVideoByUids(String[] uids);
}
