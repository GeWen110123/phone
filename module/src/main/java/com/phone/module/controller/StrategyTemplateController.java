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
import com.phone.module.domain.StrategyTemplate;
import com.phone.module.service.IStrategyTemplateService;
import com.phone.common.utils.poi.ExcelUtil;
import com.phone.common.core.page.TableDataInfo;

/**
 * 策略模板Controller
 * 
 * @author ruoyi
 * @date 2025-11-07
 */
@RestController
@RequestMapping("/module/template")
public class StrategyTemplateController extends BaseController
{
    @Autowired
    private IStrategyTemplateService strategyTemplateService;

    /**
     * 查询策略模板列表
     */
    @GetMapping("/list")
    public TableDataInfo list(StrategyTemplate strategyTemplate)
    {
        startPage();
        List<StrategyTemplate> list = strategyTemplateService.selectStrategyTemplateList(strategyTemplate);
        return getDataTable(list);
    }

    /**
     * 导出策略模板列表
     */
    @Log(title = "策略模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StrategyTemplate strategyTemplate)
    {
        List<StrategyTemplate> list = strategyTemplateService.selectStrategyTemplateList(strategyTemplate);
        ExcelUtil<StrategyTemplate> util = new ExcelUtil<StrategyTemplate>(StrategyTemplate.class);
        util.exportExcel(list, "策略模板数据");
    }

    /**
     * 获取策略模板详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(strategyTemplateService.selectStrategyTemplateById(id));
    }

    /**
     * 新增策略模板
     */
    @Log(title = "策略模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StrategyTemplate strategyTemplate)
    {
        return toAjax(strategyTemplateService.insertStrategyTemplate(strategyTemplate));
    }

    /**
     * 修改策略模板
     */
    @Log(title = "策略模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StrategyTemplate strategyTemplate)
    {
        return toAjax(strategyTemplateService.updateStrategyTemplate(strategyTemplate));
    }

    /**
     * 删除策略模板
     */
    @Log(title = "策略模板", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(strategyTemplateService.deleteStrategyTemplateByIds(ids));
    }
}
