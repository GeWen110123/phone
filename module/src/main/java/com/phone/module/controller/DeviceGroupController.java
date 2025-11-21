package com.phone.module.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletResponse;

import com.phone.common.core.domain.entity.SysUser;
import com.phone.common.utils.StringUtils;
import com.phone.module.domain.Device;
import com.phone.module.service.IDeviceService;
import com.phone.system.service.ISysUserService;
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
import com.phone.module.domain.DeviceGroup;
import com.phone.module.service.IDeviceGroupService;
import com.phone.common.utils.poi.ExcelUtil;
import com.phone.common.core.page.TableDataInfo;

/**
 * DeviceGroupController
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
@RestController
@RequestMapping("/module/deviceGroup")
public class DeviceGroupController extends BaseController
{
    @Autowired
    private IDeviceGroupService deviceGroupService;
    @Autowired
    private ISysUserService userService;
    /**
     * 查询DeviceGroup列表
     */
    @GetMapping("/list")
    public AjaxResult list(DeviceGroup deviceGroup)
    {
//        startPage();
        List<DeviceGroup> list = deviceGroupService.selectDeviceGroupList(deviceGroup);
        return AjaxResult.success(list);
    }

    @GetMapping("/listUser")
    public TableDataInfo listUser(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }


    /**
     * 导出DeviceGroup列表
     */
    @Log(title = "DeviceGroup", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DeviceGroup deviceGroup)
    {
        List<DeviceGroup> list = deviceGroupService.selectDeviceGroupList(deviceGroup);
        ExcelUtil<DeviceGroup> util = new ExcelUtil<DeviceGroup>(DeviceGroup.class);
        util.exportExcel( list, "DeviceGroup数据");
    }

    /**
     * 获取DeviceGroup详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(deviceGroupService.selectDeviceGroupById(id));
    }

    /**
     * 新增DeviceGroup
     */
    @Log(title = "设备分组日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DeviceGroup deviceGroup)
    {
        deviceGroup.setCreateTime(new Date());
        return toAjax(deviceGroupService.insertDeviceGroup(deviceGroup));
    }

    /**
     * 修改DeviceGroup
     */
    @Log(title = "设备分组日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DeviceGroup deviceGroup)
    {
        return toAjax(deviceGroupService.updateDeviceGroup(deviceGroup));
    }

    /**
     * 删除DeviceGroup
     */
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(deviceGroupService.deleteDeviceGroupByIds(ids));
    }


    @Autowired
    private IDeviceService deviceService;
    /**
     * 查询所有项目类别 项目 及其对应资产
     */
    @GetMapping("/groupAll")
    public AjaxResult groupAll() {

        List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
//      根据 project_type 查询字典

        List<DeviceGroup> list = deviceGroupService.selectDeviceGroupList(new  DeviceGroup());

        for (DeviceGroup d : list) {
            Map<String, Object> typeTemp = new HashMap<>();
            typeTemp.put("value", d.getGroupName());
            typeTemp.put("label", d.getGroupName());


            List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
            String devices = d.getDevId();
            // 用逗号分隔设备名
            String[] deviceArray = devices.split(",");

            // 创建一个 Set 来存储设备 ID
            Set<Long> deviceIds = new HashSet<>();

            // 正则表达式匹配 _ 后面的数字
            Pattern pattern = Pattern.compile("_(\\d+)");

            // 遍历设备名，提取 ID 并加入 Set
            for (String device : deviceArray) {
                Matcher matcher = pattern.matcher(device);
                if (matcher.find()) {
                    // 提取数字并转为 Long 类型加入 Set
                    deviceIds.add(Long.parseLong(matcher.group(1)));
                }
            }
            for (Long devId : deviceIds){
                Device d1 =deviceService.selectDeviceById(devId);
                if (StringUtils.isNotNull(d1)){
                    Map<String, Object> mapTemp = new HashMap<String, Object>();
                    mapTemp.put("value", d.getGroupName()+"_"+d1.getDeviceName()+"_"+d1.getId());
                    mapTemp.put("label", d1.getDeviceName()+"_"+d1.getId());
                    resList.add(mapTemp);
                }
            }

            typeTemp.put("children", resList);
            allList.add(typeTemp);
        }
        return AjaxResult.success(allList);
    }



}
