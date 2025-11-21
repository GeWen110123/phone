package com.phone.module.mapper;

import java.util.List;
import com.phone.module.domain.DeviceApp;

/**
 * 设备安装应用Mapper接口
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
public interface DeviceAppMapper 
{
    /**
     * 查询设备安装应用
     * 
     * @param id 设备安装应用主键
     * @return 设备安装应用
     */
    public DeviceApp selectDeviceAppById(Long id);

    /**
     * 查询设备安装应用列表
     * 
     * @param deviceApp 设备安装应用
     * @return 设备安装应用集合
     */
    public List<DeviceApp> selectDeviceAppList(DeviceApp deviceApp);

    /**
     * 新增设备安装应用
     * 
     * @param deviceApp 设备安装应用
     * @return 结果
     */
    public int insertDeviceApp(DeviceApp deviceApp);

    /**
     * 修改设备安装应用
     * 
     * @param deviceApp 设备安装应用
     * @return 结果
     */
    public int updateDeviceApp(DeviceApp deviceApp);

    /**
     * 删除设备安装应用
     * 
     * @param id 设备安装应用主键
     * @return 结果
     */
    public int deleteDeviceAppById(Long id);

    /**
     * 批量删除设备安装应用
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDeviceAppByIds(Long[] ids);
}
