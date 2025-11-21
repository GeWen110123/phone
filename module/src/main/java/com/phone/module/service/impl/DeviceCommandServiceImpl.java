package com.phone.module.service.impl;

import java.util.List;
import com.phone.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.phone.module.mapper.DeviceCommandMapper;
import com.phone.module.domain.DeviceCommand;
import com.phone.module.service.IDeviceCommandService;

/**
 * 设备下发命令Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
@Service
public class DeviceCommandServiceImpl implements IDeviceCommandService 
{
    @Autowired
    private DeviceCommandMapper deviceCommandMapper;

    /**
     * 查询设备下发命令
     * 
     * @param id 设备下发命令主键
     * @return 设备下发命令
     */
    @Override
    public DeviceCommand selectDeviceCommandById(Long id)
    {
        return deviceCommandMapper.selectDeviceCommandById(id);
    }

    /**
     * 查询设备下发命令列表
     * 
     * @param deviceCommand 设备下发命令
     * @return 设备下发命令
     */
    @Override
    public List<DeviceCommand> selectDeviceCommandList(DeviceCommand deviceCommand)
    {
        return deviceCommandMapper.selectDeviceCommandList(deviceCommand);
    }

    /**
     * 新增设备下发命令
     * 
     * @param deviceCommand 设备下发命令
     * @return 结果
     */
    @Override
    public int insertDeviceCommand(DeviceCommand deviceCommand)
    {
        deviceCommand.setCreateTime(DateUtils.getNowDate());
        return deviceCommandMapper.insertDeviceCommand(deviceCommand);
    }

    /**
     * 修改设备下发命令
     * 
     * @param deviceCommand 设备下发命令
     * @return 结果
     */
    @Override
    public int updateDeviceCommand(DeviceCommand deviceCommand)
    {
        return deviceCommandMapper.updateDeviceCommand(deviceCommand);
    }

    /**
     * 批量删除设备下发命令
     * 
     * @param ids 需要删除的设备下发命令主键
     * @return 结果
     */
    @Override
    public int deleteDeviceCommandByIds(Long[] ids)
    {
        return deviceCommandMapper.deleteDeviceCommandByIds(ids);
    }

    /**
     * 删除设备下发命令信息
     * 
     * @param id 设备下发命令主键
     * @return 结果
     */
    @Override
    public int deleteDeviceCommandById(Long id)
    {
        return deviceCommandMapper.deleteDeviceCommandById(id);
    }
}
