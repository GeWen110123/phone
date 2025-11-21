package com.phone.module.mapper;

import java.util.List;
import com.phone.module.domain.DeviceLog;

/**
 * 设备活动日志Mapper接口
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
public interface DeviceLogMapper 
{
    /**
     * 查询设备活动日志
     * 
     * @param id 设备活动日志主键
     * @return 设备活动日志
     */
    public DeviceLog selectDeviceLogById(Long id);

    /**
     * 查询设备活动日志列表
     * 
     * @param deviceLog 设备活动日志
     * @return 设备活动日志集合
     */
    public List<DeviceLog> selectDeviceLogList(DeviceLog deviceLog);

    /**
     * 新增设备活动日志
     * 
     * @param deviceLog 设备活动日志
     * @return 结果
     */
    public int insertDeviceLog(DeviceLog deviceLog);

    /**
     * 修改设备活动日志
     * 
     * @param deviceLog 设备活动日志
     * @return 结果
     */
    public int updateDeviceLog(DeviceLog deviceLog);

    /**
     * 删除设备活动日志
     * 
     * @param id 设备活动日志主键
     * @return 结果
     */
    public int deleteDeviceLogById(Long id);

    /**
     * 批量删除设备活动日志
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDeviceLogByIds(Long[] ids);
}
