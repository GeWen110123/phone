package com.phone.module.service.impl;

import java.util.List;
import com.phone.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.phone.module.mapper.DeviceLogMapper;
import com.phone.module.domain.DeviceLog;
import com.phone.module.service.IDeviceLogService;

/**
 * 设备活动日志Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
@Service
public class DeviceLogServiceImpl implements IDeviceLogService 
{
    @Autowired
    private DeviceLogMapper deviceLogMapper;

    /**
     * 查询设备活动日志
     * 
     * @param id 设备活动日志主键
     * @return 设备活动日志
     */
    @Override
    public DeviceLog selectDeviceLogById(Long id)
    {
        return deviceLogMapper.selectDeviceLogById(id);
    }

    /**
     * 查询设备活动日志列表
     * 
     * @param deviceLog 设备活动日志
     * @return 设备活动日志
     */
    @Override
    public List<DeviceLog> selectDeviceLogList(DeviceLog deviceLog)
    {
        return deviceLogMapper.selectDeviceLogList(deviceLog);
    }

    /**
     * 新增设备活动日志
     * 
     * @param deviceLog 设备活动日志
     * @return 结果
     */
    @Override
    public int insertDeviceLog(DeviceLog deviceLog)
    {
        deviceLog.setCreateTime(DateUtils.getNowDate());
        return deviceLogMapper.insertDeviceLog(deviceLog);
    }

    /**
     * 修改设备活动日志
     * 
     * @param deviceLog 设备活动日志
     * @return 结果
     */
    @Override
    public int updateDeviceLog(DeviceLog deviceLog)
    {
        return deviceLogMapper.updateDeviceLog(deviceLog);
    }

    /**
     * 批量删除设备活动日志
     * 
     * @param ids 需要删除的设备活动日志主键
     * @return 结果
     */
    @Override
    public int deleteDeviceLogByIds(Long[] ids)
    {
        return deviceLogMapper.deleteDeviceLogByIds(ids);
    }

    /**
     * 删除设备活动日志信息
     * 
     * @param id 设备活动日志主键
     * @return 结果
     */
    @Override
    public int deleteDeviceLogById(Long id)
    {
        return deviceLogMapper.deleteDeviceLogById(id);
    }
}
