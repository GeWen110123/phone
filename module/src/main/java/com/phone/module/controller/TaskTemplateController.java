package com.phone.module.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.phone.common.utils.StringUtils;
import com.phone.module.domain.Device;
import com.phone.module.domain.TaskLog;
import com.phone.module.service.IDeviceService;
import com.phone.module.service.ITaskLogService;
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
import com.phone.module.domain.TaskTemplate;
import com.phone.module.service.ITaskTemplateService;
import com.phone.common.utils.poi.ExcelUtil;
import com.phone.common.core.page.TableDataInfo;

/**
 * 任务模板Controller
 *
 * @author ruoyi
 * @date 2025-11-07
 */
@RestController
@RequestMapping("/module/taskTemplate")
public class TaskTemplateController extends BaseController {
    @Autowired
    private ITaskTemplateService taskTemplateService;

    /**
     * 查询任务模板列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TaskTemplate taskTemplate) {
        startPage();
        List<TaskTemplate> list = taskTemplateService.selectTaskTemplateList(taskTemplate);
        return getDataTable(list);
    }

    /**
     * 导出任务模板列表
     */
    @Log(title = "任务模板" , businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TaskTemplate taskTemplate) {
        List<TaskTemplate> list = taskTemplateService.selectTaskTemplateList(taskTemplate);
        ExcelUtil<TaskTemplate> util = new ExcelUtil<TaskTemplate>(TaskTemplate.class);
        util.exportExcel(list, "任务模板数据");
    }

    /**
     * 获取任务模板详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(taskTemplateService.selectTaskTemplateById(id));
    }

    /**
     * 新增任务模板
     */
    @Log(title = "任务模板" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TaskTemplate taskTemplate) {
        return toAjax(taskTemplateService.insertTaskTemplate(taskTemplate));
    }

    /**
     * 修改任务模板
     */
    @Log(title = "任务模板" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TaskTemplate taskTemplate) {
        return toAjax(taskTemplateService.updateTaskTemplate(taskTemplate));
    }

    /**
     * 删除任务模板
     */
    @Log(title = "任务模板" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(taskTemplateService.deleteTaskTemplateByIds(ids));
    }

    @Autowired
    private ITaskLogService taskLogService;
    @Autowired
    private IDeviceService deviceService;

    /**
     * 获取任务模板详细信息  后期需要更改为异步调用模式
     */
    @GetMapping(value = "/getToTask")
    public AjaxResult getToTask(TaskTemplate taskTemplate) {
        TaskTemplate task = taskTemplateService.selectTaskTemplateById(taskTemplate.getId());

        Set<Long> deviceIds = new HashSet<>();
        if (StringUtils.isNotEmpty(taskTemplate.getDevId())) {
            // 用逗号分隔设备名
            String[] deviceArray = taskTemplate.getDevId().split(",");
            // 创建一个 Set 来存储设备 ID
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
        }
        // 遍历 List<Long> 执行操作
        for (Long devId : deviceIds) {
            // 处理 Long 类型的 devId
            Device device = deviceService.selectDeviceById(devId);
            System.out.println("设备 ID: " + devId);
            TaskLog taskLog = new TaskLog();
            taskLog.setExecuteTime(new Date());
            taskLog.setStrategyId(task.getId());
            taskLog.setTargetId(devId);
            taskLog.setStatus("success");//为返回结果数据
            taskLog.setLogDetail("设备" + device.getDeviceName() + "进行:" + task.getStrategyName() + "任务操作");

            taskLogService.insertTaskLog(taskLog);
        }

        return AjaxResult.success();
    }


}
