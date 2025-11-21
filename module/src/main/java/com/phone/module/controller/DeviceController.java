package com.phone.module.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.phone.common.utils.StringUtils;
import com.phone.module.domain.DeviceGroup;
import com.phone.module.domain.DeviceLog;
import com.phone.module.service.IDeviceGroupService;
import com.phone.module.service.IDeviceLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.phone.common.annotation.Log;
import com.phone.common.core.controller.BaseController;
import com.phone.common.core.domain.AjaxResult;
import com.phone.common.enums.BusinessType;
import com.phone.module.domain.Device;
import com.phone.module.service.IDeviceService;
import com.phone.common.utils.poi.ExcelUtil;
import com.phone.common.core.page.TableDataInfo;

/**
 * 设备信息综合Controller
 *
 * @author ruoyi
 * @date 2025-11-04
 */
@RestController
@RequestMapping("/module/device")
public class DeviceController extends BaseController {
    @Autowired
    private IDeviceService deviceService;

    /**
     * 查询设备信息综合列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Device device) {
        startPage();
        List<Device> list = deviceService.selectDeviceList(device);
        for (Device dev : list) {
            DeviceGroup group = new DeviceGroup();
            group.setDevId(dev.getDeviceName() + "_" + dev.getId());
            List<DeviceGroup> groupList = deviceGroupService.selectDeviceGroupList(group);
            if (groupList.size() > 0) {
                dev.setGroups(groupList.stream()
                        .map(DeviceGroup::getGroupName)
                        .collect(Collectors.joining(",")));
                dev.setGroupIds(groupList.stream()
                        .map(DeviceGroup::getId)
                        .collect(Collectors.toList()));
            }
        }
        return getDataTable(list);
    }

    /**
     * 导出设备信息综合列表
     */
    @Log(title = "设备信息综合" , businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Device device) {
        List<Device> list = deviceService.selectDeviceList(device);
        ExcelUtil<Device> util = new ExcelUtil<Device>(Device.class);
        util.exportExcel(list, "设备信息综合数据");
    }

    /**
     * 获取设备信息综合详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(deviceService.selectDeviceById(id));
    }

    /**
     * 新增设备信息综合
     */
    @Log(title = "设备信息综合" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Device device) {
        return toAjax(deviceService.insertDevice(device));
    }
    @Autowired
    private IDeviceLogService deviceLogService;
    /**
     * 修改设备信息综合
     */
    @Log(title = "设备信息综合" , businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody Device dev) {

        for (Long id : dev.getIds()) {
            Device device = deviceService.selectDeviceById(id);

            // ---------- 定义旧名与新名 ----------
            String oldDevNameTmp = device.getDeviceName() + "_" + device.getId();
            String newDevNameTmp = oldDevNameTmp;

            // 如果传入了新名称，则更新设备名
            if (StringUtils.isNotEmpty(dev.getDeviceName())) {
                device.setDeviceName(dev.getDeviceName());
                newDevNameTmp = dev.getDeviceName() + "_" + device.getId();
            }

            // lambda 用 final 修饰避免编译错误
            final String oldDevName = oldDevNameTmp;
            final String newDevName = newDevNameTmp;

            List<Long> newGroupIds = dev.getGroupIds();

            // ---------- 更新或新增设备分组 ----------
            for (Long groupId : newGroupIds) {
                DeviceGroup group = deviceGroupService.selectDeviceGroupById(groupId);
                if (group == null) continue;

                String devIds = group.getDevId();
                List<String> devIdList = new ArrayList<>();
                if (StringUtils.isNotEmpty(devIds)) {
                    devIdList.addAll(Arrays.asList(devIds.split(",")));
                }

                boolean existed = devIdList.contains(oldDevName) || devIdList.contains(newDevName);
                boolean replaced = false;

                // 精确匹配替换旧名称
                for (int i = 0; i < devIdList.size(); i++) {
                    if (devIdList.get(i).equals(oldDevName)) {
                        devIdList.set(i, newDevName);
                        replaced = true;
                        break;
                    }
                }

                // 如果不存在旧名也不存在新名 → 属于“新增”
                if (!replaced && !existed) {
                    devIdList.add(newDevName);

                    // ---------- 新增日志记录 ----------
                    DeviceLog deviceLog = new DeviceLog();
                    deviceLog.setCreateTime(new Date());
                    deviceLog.setDeviceId(device.getId());
                    deviceLog.setActionType("分组新增");
                    deviceLog.setActionDetail("此设备添加至 " + group.getGroupName() + " 分组");
                    deviceLogService.insertDeviceLog(deviceLog);
                }

                // 去重 + 重新拼接
                Set<String> uniqueIds = new LinkedHashSet<>(devIdList);
                group.setDevId(String.join(",", uniqueIds));
                group.setDeviceCount((long) uniqueIds.size());

                // 更新设备组
                deviceGroupService.updateDeviceGroup(group);
            }


            // ---------- 去除旧分组中已不存在的设备 ----------
            DeviceGroup query = new DeviceGroup();
            query.setDevId("%" + oldDevName + "%"); // 支持模糊匹配
            List<DeviceGroup> allGroups = deviceGroupService.selectDeviceGroupList(query);

            for (DeviceGroup group : allGroups) {
                if (newGroupIds.contains(group.getId())) {
                    continue; // 当前还属于此分组，跳过
                }

                String devIds = group.getDevId();
                if (StringUtils.isEmpty(devIds)) {
                    continue;
                }

                List<String> devIdList = new ArrayList<>(Arrays.asList(devIds.split(",")));
                boolean removed = devIdList.removeIf(name ->
                        name.equals(oldDevName) || name.equals(newDevName)
                );

                if (removed) {
                    Set<String> uniqueIds = new LinkedHashSet<>(devIdList);
                    group.setDevId(String.join(",", uniqueIds));
                    group.setDeviceCount((long) uniqueIds.size());
                    deviceGroupService.updateDeviceGroup(group);

                    DeviceLog deviceLog = new DeviceLog();
                    deviceLog.setCreateTime(new Date());
                    deviceLog.setDeviceId(device.getId());
                    deviceLog.setActionType("分组去除");
                    deviceLog.setActionDetail("此设备从"+group.getGroupName()+"分组中去除");
                    deviceLogService.insertDeviceLog(deviceLog);
                }
            }

            // ---------- 更新设备本身 ----------

            if (!dev.getDeviceName().equals(device.getDeviceName())){
                DeviceLog deviceLog = new DeviceLog();
                deviceLog.setCreateTime(new Date());
                deviceLog.setDeviceId(device.getId());
                deviceLog.setActionType("修改");
                deviceLog.setActionDetail("原设备:"+device.getDeviceName()+"设备名修改为:"+dev.getDeviceName());
                deviceLogService.insertDeviceLog(deviceLog);

            }
            deviceService.updateDevice(device);
        }

        return AjaxResult.success();
    }

    /**
     * 删除设备信息综合
     */
    @Log(title = "设备信息综合" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(deviceService.deleteDeviceByIds(ids));
    }


    @Autowired
    private IDeviceGroupService deviceGroupService;


//    /**
//     * 修改设备信息综合
//     */
//    @Log(title = "设备信息综合" , businessType = BusinessType.UPDATE)
//    @PutMapping("/devGroup")
//    public AjaxResult devGroup(@RequestBody Device device) {
//
//        return AjaxResult.success();
//    }
//

}
