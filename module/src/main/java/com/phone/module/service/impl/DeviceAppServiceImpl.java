package com.phone.module.service.impl;

import java.util.List;
import com.phone.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.phone.module.mapper.DeviceAppMapper;
import com.phone.module.domain.DeviceApp;
import com.phone.module.service.IDeviceAppService;

/**
 * 设备安装应用Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
@Service
public class DeviceAppServiceImpl implements IDeviceAppService 
{
    @Autowired
    private DeviceAppMapper deviceAppMapper;

    /**
     * 查询设备安装应用
     * 
     * @param id 设备安装应用主键
     * @return 设备安装应用
     */
    @Override
    public DeviceApp selectDeviceAppById(Long id)
    {
        return deviceAppMapper.selectDeviceAppById(id);
    }

    /**
     * 查询设备安装应用列表
     * 
     * @param deviceApp 设备安装应用
     * @return 设备安装应用
     */
    @Override
    public List<DeviceApp> selectDeviceAppList(DeviceApp deviceApp)
    {
        return deviceAppMapper.selectDeviceAppList(deviceApp);
    }

    /**
     * 新增设备安装应用
     * 
     * @param deviceApp 设备安装应用
     * @return 结果
     */
    @Override
    public int insertDeviceApp(DeviceApp deviceApp)
    {
        return deviceAppMapper.insertDeviceApp(deviceApp);
    }

    /**
     * 修改设备安装应用
     * 
     * @param deviceApp 设备安装应用
     * @return 结果
     */
    @Override
    public int updateDeviceApp(DeviceApp deviceApp)
    {
        deviceApp.setUpdateTime(DateUtils.getNowDate());
        return deviceAppMapper.updateDeviceApp(deviceApp);
    }

    /**
     * 批量删除设备安装应用
     * 
     * @param ids 需要删除的设备安装应用主键
     * @return 结果
     */
    @Override
    public int deleteDeviceAppByIds(Long[] ids)
    {
        return deviceAppMapper.deleteDeviceAppByIds(ids);
    }

    /**
     * 删除设备安装应用信息
     * 
     * @param id 设备安装应用主键
     * @return 结果
     */
    @Override
    public int deleteDeviceAppById(Long id)
    {
        return deviceAppMapper.deleteDeviceAppById(id);
    }
}
