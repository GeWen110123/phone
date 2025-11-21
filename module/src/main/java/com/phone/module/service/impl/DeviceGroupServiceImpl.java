package com.phone.module.service.impl;

import java.util.List;
import com.phone.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.phone.module.mapper.DeviceGroupMapper;
import com.phone.module.domain.DeviceGroup;
import com.phone.module.service.IDeviceGroupService;

/**
 * DeviceGroupService业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
@Service
public class DeviceGroupServiceImpl implements IDeviceGroupService 
{
    @Autowired
    private DeviceGroupMapper deviceGroupMapper;

    /**
     * 查询DeviceGroup
     * 
     * @param id DeviceGroup主键
     * @return DeviceGroup
     */
    @Override
    public DeviceGroup selectDeviceGroupById(Long id)
    {
        return deviceGroupMapper.selectDeviceGroupById(id);
    }

    /**
     * 查询DeviceGroup列表
     * 
     * @param deviceGroup DeviceGroup
     * @return DeviceGroup
     */
    @Override
    public List<DeviceGroup> selectDeviceGroupList(DeviceGroup deviceGroup)
    {
        return deviceGroupMapper.selectDeviceGroupList(deviceGroup);
    }

    /**
     * 新增DeviceGroup
     * 
     * @param deviceGroup DeviceGroup
     * @return 结果
     */
    @Override
    public int insertDeviceGroup(DeviceGroup deviceGroup)
    {
        deviceGroup.setCreateTime(DateUtils.getNowDate());
        return deviceGroupMapper.insertDeviceGroup(deviceGroup);
    }

    /**
     * 修改DeviceGroup
     * 
     * @param deviceGroup DeviceGroup
     * @return 结果
     */
    @Override
    public int updateDeviceGroup(DeviceGroup deviceGroup)
    {
        deviceGroup.setUpdateTime(DateUtils.getNowDate());
        return deviceGroupMapper.updateDeviceGroup(deviceGroup);
    }

    /**
     * 批量删除DeviceGroup
     * 
     * @param ids 需要删除的DeviceGroup主键
     * @return 结果
     */
    @Override
    public int deleteDeviceGroupByIds(Long[] ids)
    {
        return deviceGroupMapper.deleteDeviceGroupByIds(ids);
    }

    /**
     * 删除DeviceGroup信息
     * 
     * @param id DeviceGroup主键
     * @return 结果
     */
    @Override
    public int deleteDeviceGroupById(Long id)
    {
        return deviceGroupMapper.deleteDeviceGroupById(id);
    }
}
