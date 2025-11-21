package com.phone.module.service.impl;

import java.util.List;
import com.phone.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.phone.module.mapper.StrategyTemplateMapper;
import com.phone.module.domain.StrategyTemplate;
import com.phone.module.service.IStrategyTemplateService;

/**
 * 策略模板Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-07
 */
@Service
public class StrategyTemplateServiceImpl implements IStrategyTemplateService 
{
    @Autowired
    private StrategyTemplateMapper strategyTemplateMapper;

    /**
     * 查询策略模板
     * 
     * @param id 策略模板主键
     * @return 策略模板
     */
    @Override
    public StrategyTemplate selectStrategyTemplateById(Long id)
    {
        return strategyTemplateMapper.selectStrategyTemplateById(id);
    }

    /**
     * 查询策略模板列表
     * 
     * @param strategyTemplate 策略模板
     * @return 策略模板
     */
    @Override
    public List<StrategyTemplate> selectStrategyTemplateList(StrategyTemplate strategyTemplate)
    {
        return strategyTemplateMapper.selectStrategyTemplateList(strategyTemplate);
    }

    /**
     * 新增策略模板
     * 
     * @param strategyTemplate 策略模板
     * @return 结果
     */
    @Override
    public int insertStrategyTemplate(StrategyTemplate strategyTemplate)
    {
        strategyTemplate.setCreateTime(DateUtils.getNowDate());
        return strategyTemplateMapper.insertStrategyTemplate(strategyTemplate);
    }

    /**
     * 修改策略模板
     * 
     * @param strategyTemplate 策略模板
     * @return 结果
     */
    @Override
    public int updateStrategyTemplate(StrategyTemplate strategyTemplate)
    {
        return strategyTemplateMapper.updateStrategyTemplate(strategyTemplate);
    }

    /**
     * 批量删除策略模板
     * 
     * @param ids 需要删除的策略模板主键
     * @return 结果
     */
    @Override
    public int deleteStrategyTemplateByIds(Long[] ids)
    {
        return strategyTemplateMapper.deleteStrategyTemplateByIds(ids);
    }

    /**
     * 删除策略模板信息
     * 
     * @param id 策略模板主键
     * @return 结果
     */
    @Override
    public int deleteStrategyTemplateById(Long id)
    {
        return strategyTemplateMapper.deleteStrategyTemplateById(id);
    }
}
