package com.phone.module.service;

import java.util.List;
import com.phone.module.domain.StrategyTemplate;

/**
 * 策略模板Service接口
 * 
 * @author ruoyi
 * @date 2025-11-07
 */
public interface IStrategyTemplateService 
{
    /**
     * 查询策略模板
     * 
     * @param id 策略模板主键
     * @return 策略模板
     */
    public StrategyTemplate selectStrategyTemplateById(Long id);

    /**
     * 查询策略模板列表
     * 
     * @param strategyTemplate 策略模板
     * @return 策略模板集合
     */
    public List<StrategyTemplate> selectStrategyTemplateList(StrategyTemplate strategyTemplate);

    /**
     * 新增策略模板
     * 
     * @param strategyTemplate 策略模板
     * @return 结果
     */
    public int insertStrategyTemplate(StrategyTemplate strategyTemplate);

    /**
     * 修改策略模板
     * 
     * @param strategyTemplate 策略模板
     * @return 结果
     */
    public int updateStrategyTemplate(StrategyTemplate strategyTemplate);

    /**
     * 批量删除策略模板
     * 
     * @param ids 需要删除的策略模板主键集合
     * @return 结果
     */
    public int deleteStrategyTemplateByIds(Long[] ids);

    /**
     * 删除策略模板信息
     * 
     * @param id 策略模板主键
     * @return 结果
     */
    public int deleteStrategyTemplateById(Long id);
}
