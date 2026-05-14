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
import com.phone.module.domain.RunLog;
import com.phone.module.service.IRunLogService;
import com.phone.common.utils.poi.ExcelUtil;
import com.phone.common.core.page.TableDataInfo;

/**
 * 运行日志Controller
 * 
 * @author phone
 * @date 2026-03-06
 */
@RestController
@RequestMapping("/module/RunLog")
public class RunLogController extends BaseController
{
    @Autowired
    private IRunLogService runLogService;

    /**
     * 查询运行日志列表
     */
    @GetMapping("/list")
    public TableDataInfo list(RunLog runLog)
    {
        startPage();
        List<RunLog> list = runLogService.selectRunLogList(runLog);
        return getDataTable(list);
    }

    /**
     * 导出运行日志列表
     */
    @Log(title = "运行日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RunLog runLog)
    {
        List<RunLog> list = runLogService.selectRunLogList(runLog);
        ExcelUtil<RunLog> util = new ExcelUtil<RunLog>(RunLog.class);
        util.exportExcel(list, "运行日志数据");
    }

    /**
     * 获取运行日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('module:RunLog:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(runLogService.selectRunLogById(id));
    }

    /**
     * 新增运行日志
     */
    @PreAuthorize("@ss.hasPermi('module:RunLog:add')")
    @Log(title = "运行日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RunLog runLog)
    {
        return toAjax(runLogService.insertRunLog(runLog));
    }

    /**
     * 修改运行日志
     */
    @PreAuthorize("@ss.hasPermi('module:RunLog:edit')")
    @Log(title = "运行日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RunLog runLog)
    {
        return toAjax(runLogService.updateRunLog(runLog));
    }

    /**
     * 删除运行日志
     */
    @PreAuthorize("@ss.hasPermi('module:RunLog:remove')")
    @Log(title = "运行日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(runLogService.deleteRunLogByIds(ids));
    }
}
