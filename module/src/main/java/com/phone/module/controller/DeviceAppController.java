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
import com.phone.module.domain.DeviceApp;
import com.phone.module.service.IDeviceAppService;
import com.phone.common.utils.poi.ExcelUtil;
import com.phone.common.core.page.TableDataInfo;

/**
 * 设备安装应用Controller
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
@RestController
@RequestMapping("/module/DeviceApp")
public class DeviceAppController extends BaseController
{
    @Autowired
    private IDeviceAppService deviceAppService;

    /**
     * 查询设备安装应用列表
     */
//    @PreAuthorize("@ss.hasPermi('module:DeviceApp:list')")
    @GetMapping("/list")
    public TableDataInfo list(DeviceApp deviceApp)
    {
        startPage();
        List<DeviceApp> list = deviceAppService.selectDeviceAppList(deviceApp);
        return getDataTable(list);
    }

    /**
     * 导出设备安装应用列表
     */
//    @PreAuthorize("@ss.hasPermi('module:Devicesys_deptApp:export')")
    @Log(title = "设备安装应用", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DeviceApp deviceApp)
    {
        List<DeviceApp> list = deviceAppService.selectDeviceAppList(deviceApp);
        ExcelUtil<DeviceApp> util = new ExcelUtil<DeviceApp>(DeviceApp.class);
        util.exportExcel(list, "设备安装应用数据");
    }

    /**
     * 获取设备安装应用详细信息
     */
//    @PreAuthorize("@ss.hasPermi('module:DeviceApp:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(deviceAppService.selectDeviceAppById(id));
    }

    /**
     * 新增设备安装应用
     */
//    @PreAuthorize("@ss.hasPermi('module:DeviceApp:add')")
    @Log(title = "设备安装应用", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DeviceApp deviceApp)
    {
        return toAjax(deviceAppService.insertDeviceApp(deviceApp));
    }

    /**
     * 修改设备安装应用
     */
//    @PreAuthorize("@ss.hasPermi('module:DeviceApp:edit')")
    @Log(title = "设备安装应用", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DeviceApp deviceApp)
    {
        return toAjax(deviceAppService.updateDeviceApp(deviceApp));
    }

    /**
     * 删除设备安装应用
     */
//    @PreAuthorize("@ss.hasPermi('module:DeviceApp:remove')")
    @Log(title = "设备安装应用", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(deviceAppService.deleteDeviceAppByIds(ids));
    }
}
