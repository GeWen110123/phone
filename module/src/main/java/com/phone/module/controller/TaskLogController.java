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
import com.phone.module.domain.TaskLog;
import com.phone.module.service.ITaskLogService;
import com.phone.common.utils.poi.ExcelUtil;
import com.phone.common.core.page.TableDataInfo;

/**
 * 任务执行日志Controller
 * 
 * @author ruoyi
 * @date 2025-11-07
 */
@RestController
@RequestMapping("/module/taskLog")
public class TaskLogController extends BaseController
{
    @Autowired
    private ITaskLogService taskLogService;

    /**
     * 查询任务执行日志列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TaskLog taskLog)
    {
        startPage();
        List<TaskLog> list = taskLogService.selectTaskLogList(taskLog);
        return getDataTable(list);
    }

    /**
     * 导出任务执行日志列表
     */
    @Log(title = "任务执行日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TaskLog taskLog)
    {
        List<TaskLog> list = taskLogService.selectTaskLogList(taskLog);
        ExcelUtil<TaskLog> util = new ExcelUtil<TaskLog>(TaskLog.class);
        util.exportExcel(list, "任务执行日志数据");
    }

    /**
     * 获取任务执行日志详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(taskLogService.selectTaskLogById(id));
    }

    /**
     * 新增任务执行日志
     */
    @Log(title = "任务执行日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TaskLog taskLog)
    {
        return toAjax(taskLogService.insertTaskLog(taskLog));
    }

    /**
     * 修改任务执行日志
     */
    @Log(title = "任务执行日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TaskLog taskLog)
    {
        return toAjax(taskLogService.updateTaskLog(taskLog));
    }

    /**
     * 删除任务执行日志
     */
    @Log(title = "任务执行日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(taskLogService.deleteTaskLogByIds(ids));
    }
}
