package com.phone.module.service.impl;

import java.util.List;
import com.phone.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.phone.module.mapper.DeviceMapper;
import com.phone.module.domain.Device;
import com.phone.module.service.IDeviceService;

/**
 * 设备信息综合Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
@Service
public class DeviceServiceImpl implements IDeviceService 
{
    @Autowired
    private DeviceMapper deviceMapper;

    /**
     * 查询设备信息综合
     * 
     * @param id 设备信息综合主键
     * @return 设备信息综合
     */
    @Override
    public Device selectDeviceById(Long id)
    {
        return deviceMapper.selectDeviceById(id);
    }

    /**
     * 查询设备信息综合列表
     * 
     * @param device 设备信息综合
     * @return 设备信息综合
     */
    @Override
    public List<Device> selectDeviceList(Device device)
    {
        return deviceMapper.selectDeviceList(device);
    }

    /**
     * 新增设备信息综合
     * 
     * @param device 设备信息综合
     * @return 结果
     */
    @Override
    public int insertDevice(Device device)
    {
        device.setCreateTime(DateUtils.getNowDate());
        return deviceMapper.insertDevice(device);
    }

    /**
     * 修改设备信息综合
     * 
     * @param device 设备信息综合
     * @return 结果
     */
    @Override
    public int updateDevice(Device device)
    {
        device.setUpdateTime(DateUtils.getNowDate());
        return deviceMapper.updateDevice(device);
    }

    /**
     * 批量删除设备信息综合
     * 
     * @param ids 需要删除的设备信息综合主键
     * @return 结果
     */
    @Override
    public int deleteDeviceByIds(Long[] ids)
    {
        return deviceMapper.deleteDeviceByIds(ids);
    }

    /**
     * 删除设备信息综合信息
     * 
     * @param id 设备信息综合主键
     * @return 结果
     */
    @Override
    public int deleteDeviceById(Long id)
    {
        return deviceMapper.deleteDeviceById(id);
    }
}
