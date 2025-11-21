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
import com.phone.module.domain.StrategyLog;
import com.phone.module.service.IStrategyLogService;
import com.phone.common.utils.poi.ExcelUtil;
import com.phone.common.core.page.TableDataInfo;

/**
 * 策略执行日志Controller
 * 
 * @author ruoyi
 * @date 2025-11-07
 */
@RestController
@RequestMapping("/module/strategy_log")
public class StrategyLogController extends BaseController
{
    @Autowired
    private IStrategyLogService strategyLogService;

    /**
     * 查询策略执行日志列表
     */
    @GetMapping("/list")
    public TableDataInfo list(StrategyLog strategyLog)
    {
        startPage();
        List<StrategyLog> list = strategyLogService.selectStrategyLogList(strategyLog);
        return getDataTable(list);
    }

    /**
     * 导出策略执行日志列表
     */
    @Log(title = "策略执行日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StrategyLog strategyLog)
    {
        List<StrategyLog> list = strategyLogService.selectStrategyLogList(strategyLog);
        ExcelUtil<StrategyLog> util = new ExcelUtil<StrategyLog>(StrategyLog.class);
        util.exportExcel( list, "策略执行日志数据");
    }

    /**
     * 获取策略执行日志详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(strategyLogService.selectStrategyLogById(id));
    }

    /**
     * 新增策略执行日志
     */
    @Log(title = "策略执行日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StrategyLog strategyLog)
    {
        return toAjax(strategyLogService.insertStrategyLog(strategyLog));
    }

    /**
     * 修改策略执行日志
     */
    @Log(title = "策略执行日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StrategyLog strategyLog)
    {
        return toAjax(strategyLogService.updateStrategyLog(strategyLog));
    }

    /**
     * 删除策略执行日志
     */
    @Log(title = "策略执行日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(strategyLogService.deleteStrategyLogByIds(ids));
    }
}
