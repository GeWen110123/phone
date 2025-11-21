package com.phone.module.mapper;

import java.util.List;
import com.phone.module.domain.DeviceCommand;

/**
 * 设备下发命令Mapper接口
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
public interface DeviceCommandMapper 
{
    /**
     * 查询设备下发命令
     * 
     * @param id 设备下发命令主键
     * @return 设备下发命令
     */
    public DeviceCommand selectDeviceCommandById(Long id);

    /**
     * 查询设备下发命令列表
     * 
     * @param deviceCommand 设备下发命令
     * @return 设备下发命令集合
     */
    public List<DeviceCommand> selectDeviceCommandList(DeviceCommand deviceCommand);

    /**
     * 新增设备下发命令
     * 
     * @param deviceCommand 设备下发命令
     * @return 结果
     */
    public int insertDeviceCommand(DeviceCommand deviceCommand);

    /**
     * 修改设备下发命令
     * 
     * @param deviceCommand 设备下发命令
     * @return 结果
     */
    public int updateDeviceCommand(DeviceCommand deviceCommand);

    /**
     * 删除设备下发命令
     * 
     * @param id 设备下发命令主键
     * @return 结果
     */
    public int deleteDeviceCommandById(Long id);

    /**
     * 批量删除设备下发命令
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDeviceCommandByIds(Long[] ids);
}
