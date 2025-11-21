package com.phone.module.mapper;

import java.util.List;
import com.phone.module.domain.StrategyTemplate;

/**
 * 策略模板Mapper接口
 * 
 * @author ruoyi
 * @date 2025-11-07
 */
public interface StrategyTemplateMapper 
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
     * 删除策略模板
     * 
     * @param id 策略模板主键
     * @return 结果
     */
    public int deleteStrategyTemplateById(Long id);

    /**
     * 批量删除策略模板
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStrategyTemplateByIds(Long[] ids);
}
