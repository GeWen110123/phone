package com.phone.module.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.phone.module.domain.DeviceLog;
import com.phone.module.service.IDeviceLogService;
import com.phone.common.utils.poi.ExcelUtil;
import com.phone.common.core.page.TableDataInfo;

/**
 * 设备活动日志Controller
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
@RestController
@RequestMapping("/module/DeviceLog")
public class DeviceLogController extends BaseController
{
    @Autowired
    private IDeviceLogService deviceLogService;

    /**
     * 查询设备活动日志列表
     */
//    @PreAuthorize("@ss.hasPermi('module:DeviceLog:list')")
    @GetMapping("/list")
    public AjaxResult list(DeviceLog deviceLog)
    {
//        startPage();
        List<DeviceLog> list = deviceLogService.selectDeviceLogList(deviceLog);
        return AjaxResult.success(list);
    }

    /**
     * 导出设备活动日志列表
     */
    @PreAuthorize("@ss.hasPermi('module:DeviceLog:export')")
    @Log(title = "设备活动日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DeviceLog deviceLog)
    {
        List<DeviceLog> list = deviceLogService.selectDeviceLogList(deviceLog);
        ExcelUtil<DeviceLog> util = new ExcelUtil<DeviceLog>(DeviceLog.class);
        util.exportExcel(list, "设备活动日志数据");
    }

    /**
     * 获取设备活动日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('module:DeviceLog:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(deviceLogService.selectDeviceLogById(id));
    }

    /**
     * 新增设备活动日志
     */
    @PreAuthorize("@ss.hasPermi('module:DeviceLog:add')")
    @Log(title = "设备活动日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DeviceLog deviceLog)
    {
        return toAjax(deviceLogService.insertDeviceLog(deviceLog));
    }

    /**
     * 修改设备活动日志
     */
    @PreAuthorize("@ss.hasPermi('module:DeviceLog:edit')")
    @Log(title = "设备活动日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DeviceLog deviceLog)
    {
        return toAjax(deviceLogService.updateDeviceLog(deviceLog));
    }

    /**
     * 删除设备活动日志
     */
    @PreAuthorize("@ss.hasPermi('module:DeviceLog:remove')")
    @Log(title = "设备活动日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(deviceLogService.deleteDeviceLogByIds(ids));
    }
}
