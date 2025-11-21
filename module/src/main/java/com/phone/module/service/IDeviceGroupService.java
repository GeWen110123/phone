package com.phone.module.service;

import java.util.List;
import com.phone.module.domain.DeviceGroup;

/**
 * DeviceGroupService接口
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
public interface IDeviceGroupService 
{
    /**
     * 查询DeviceGroup
     * 
     * @param id DeviceGroup主键
     * @return DeviceGroup
     */
    public DeviceGroup selectDeviceGroupById(Long id);

    /**
     * 查询DeviceGroup列表
     * 
     * @param deviceGroup DeviceGroup
     * @return DeviceGroup集合
     */
    public List<DeviceGroup> selectDeviceGroupList(DeviceGroup deviceGroup);

    /**
     * 新增DeviceGroup
     * 
     * @param deviceGroup DeviceGroup
     * @return 结果
     */
    public int insertDeviceGroup(DeviceGroup deviceGroup);

    /**
     * 修改DeviceGroup
     * 
     * @param deviceGroup DeviceGroup
     * @return 结果
     */
    public int updateDeviceGroup(DeviceGroup deviceGroup);

    /**
     * 批量删除DeviceGroup
     * 
     * @param ids 需要删除的DeviceGroup主键集合
     * @return 结果
     */
    public int deleteDeviceGroupByIds(Long[] ids);

    /**
     * 删除DeviceGroup信息
     * 
     * @param id DeviceGroup主键
     * @return 结果
     */
    public int deleteDeviceGroupById(Long id);
}
