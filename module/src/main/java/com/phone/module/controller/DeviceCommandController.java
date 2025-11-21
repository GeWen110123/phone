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
import com.phone.module.domain.DeviceCommand;
import com.phone.module.service.IDeviceCommandService;
import com.phone.common.utils.poi.ExcelUtil;
import com.phone.common.core.page.TableDataInfo;

/**
 * 设备下发命令Controller
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
@RestController
@RequestMapping("/module/DeviceCommand")
public class DeviceCommandController extends BaseController
{
    @Autowired
    private IDeviceCommandService deviceCommandService;

    /**
     * 查询设备下发命令列表
     */
    @PreAuthorize("@ss.hasPermi('module:DeviceCommand:list')")
    @GetMapping("/list")
    public TableDataInfo list(DeviceCommand deviceCommand)
    {
        startPage();
        List<DeviceCommand> list = deviceCommandService.selectDeviceCommandList(deviceCommand);
        return getDataTable(list);
    }

    /**
     * 导出设备下发命令列表
     */
    @PreAuthorize("@ss.hasPermi('module:DeviceCommand:export')")
    @Log(title = "设备下发命令", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DeviceCommand deviceCommand)
    {
        List<DeviceCommand> list = deviceCommandService.selectDeviceCommandList(deviceCommand);
        ExcelUtil<DeviceCommand> util = new ExcelUtil<DeviceCommand>(DeviceCommand.class);
        util.exportExcel( list, "设备下发命令数据");
    }

    /**
     * 获取设备下发命令详细信息
     */
    @PreAuthorize("@ss.hasPermi('module:DeviceCommand:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(deviceCommandService.selectDeviceCommandById(id));
    }

    /**
     * 新增设备下发命令
     */
    @PreAuthorize("@ss.hasPermi('module:DeviceCommand:add')")
    @Log(title = "设备下发命令", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DeviceCommand deviceCommand)
    {
        return toAjax(deviceCommandService.insertDeviceCommand(deviceCommand));
    }

    /**
     * 修改设备下发命令
     */
    @PreAuthorize("@ss.hasPermi('module:DeviceCommand:edit')")
    @Log(title = "设备下发命令", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DeviceCommand deviceCommand)
    {
        return toAjax(deviceCommandService.updateDeviceCommand(deviceCommand));
    }

    /**
     * 删除设备下发命令
     */
    @PreAuthorize("@ss.hasPermi('module:DeviceCommand:remove')")
    @Log(title = "设备下发命令", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(deviceCommandService.deleteDeviceCommandByIds(ids));
    }
}
