package com.phone.module.mapper;

import java.util.List;
import com.phone.module.domain.DeviceGroup;

/**
 * DeviceGroupMapper接口
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
public interface DeviceGroupMapper 
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
     * 删除DeviceGroup
     * 
     * @param id DeviceGroup主键
     * @return 结果
     */
    public int deleteDeviceGroupById(Long id);

    /**
     * 批量删除DeviceGroup
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDeviceGroupByIds(Long[] ids);
}
