package com.phone.module.service.impl;

import java.util.List;

import com.phone.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.phone.module.mapper.AddressVideoMapper;
import com.phone.module.domain.AddressVideo;
import com.phone.module.service.IAddressVideoService;

/**
 * 地址视频总Service业务层处理
 *
 * @author ruoyi
 * @date 2026-01-09
 */
@Service
public class AddressVideoServiceImpl implements IAddressVideoService {
    @Autowired
    private AddressVideoMapper addressVideoMapper;

    /**
     * 查询地址视频总
     *
     * @param uid 地址视频总主键
     * @return 地址视频总
     */
    @Override
    public AddressVideo selectAddressVideoByUid(String uid) {
        return addressVideoMapper.selectAddressVideoByUid(uid);
    }

    /**
     * 查询地址视频总列表
     *
     * @param addressVideo 地址视频总
     * @return 地址视频总
     */
    @Override
    public List<AddressVideo> selectAddressVideoList(AddressVideo addressVideo) {
        return addressVideoMapper.selectAddressVideoList(addressVideo);
    }
    @Override
    public List<AddressVideo> selectAddressVideoList200(AddressVideo addressVideo) {
        return addressVideoMapper.selectAddressVideoList200(addressVideo);
    }

    @Override
    public List<String> selectAddressListByIp(AddressVideo addressVideo) {
        return addressVideoMapper.selectAddressListByIp(addressVideo);
    }

    @Override
    public List<AddressVideo> selectAllVideoDId(AddressVideo addressVideo) {
        return addressVideoMapper.selectAllVideoDId(addressVideo);
    }

    /**
     * 新增地址视频总
     *
     * @param addressVideo 地址视频总
     * @return 结果
     */
    @Override
    public int insertAddressVideo(AddressVideo addressVideo) {
        addressVideo.setCreateTime(DateUtils.getNowDate());
        return addressVideoMapper.insertAddressVideo(addressVideo);
    }

    /**
     * 修改地址视频总
     *
     * @param addressVideo 地址视频总
     * @return 结果
     */
    @Override
    public int updateAddressVideo(AddressVideo addressVideo) {
        return addressVideoMapper.updateAddressVideo(addressVideo);
    }

    /**
     * 批量删除地址视频总
     *
     * @param uids 需要删除的地址视频总主键
     * @return 结果
     */
    @Override
    public int deleteAddressVideoByUids(String[] uids) {
        return addressVideoMapper.deleteAddressVideoByUids(uids);
    }

    /**
     * 删除地址视频总信息
     *
     * @param uid 地址视频总主键
     * @return 结果
     */
    @Override
    public int deleteAddressVideoByUid(String uid) {
        return addressVideoMapper.deleteAddressVideoByUid(uid);
    }
}
