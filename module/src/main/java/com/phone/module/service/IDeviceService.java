package com.phone.module.service;

import java.util.List;
import com.phone.module.domain.Device;

/**
 * 设备信息综合Service接口
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
public interface IDeviceService 
{
    /**
     * 查询设备信息综合
     * 
     * @param id 设备信息综合主键
     * @return 设备信息综合
     */
    public Device selectDeviceById(Long id);

    /**
     * 查询设备信息综合列表
     * 
     * @param device 设备信息综合
     * @return 设备信息综合集合
     */
    public List<Device> selectDeviceList(Device device);

    /**
     * 新增设备信息综合
     * 
     * @param device 设备信息综合
     * @return 结果
     */
    public int insertDevice(Device device);

    /**
     * 修改设备信息综合
     * 
     * @param device 设备信息综合
     * @return 结果
     */
    public int updateDevice(Device device);

    /**
     * 批量删除设备信息综合
     * 
     * @param ids 需要删除的设备信息综合主键集合
     * @return 结果
     */
    public int deleteDeviceByIds(Long[] ids);

    /**
     * 删除设备信息综合信息
     * 
     * @param id 设备信息综合主键
     * @return 结果
     */
    public int deleteDeviceById(Long id);
}
