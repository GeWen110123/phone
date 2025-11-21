package com.phone.module.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.phone.common.annotation.Excel;
import com.phone.common.core.domain.BaseEntity;

/**
 * 策略模板对象 strategy_template
 * 
 * @author ruoyi
 * @date 2025-11-07
 */
public class StrategyTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 策略名称 */
    @Excel(name = "策略名称")
    private String strategyName;

    /** 策略方法 */
    @Excel(name = "策略方法")
    private String scriptPath;

    /** 策略描述 */
    @Excel(name = "策略描述")
    private String description;

    /** 选择的设备IDs */
    @Excel(name = "选择的设备IDs")
    private String devId;

    /** 选择的分组信息 */
    @Excel(name = "选择的分组信息")
    private String groups;

    /** 设备IDs + 分组中的 设备id 去重整合  单独的 id */
    @Excel(name = "设备IDs + 分组中的 设备id 去重整合  单独的 id")
    private String allDevIds;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setStrategyName(String strategyName) 
    {
        this.strategyName = strategyName;
    }

    public String getStrategyName() 
    {
        return strategyName;
    }
    public void setScriptPath(String scriptPath) 
    {
        this.scriptPath = scriptPath;
    }

    public String getScriptPath() 
    {
        return scriptPath;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setDevId(String devId) 
    {
        this.devId = devId;
    }

    public String getDevId() 
    {
        return devId;
    }
    public void setGroups(String groups) 
    {
        this.groups = groups;
    }

    public String getGroups() 
    {
        return groups;
    }
    public void setAllDevIds(String allDevIds) 
    {
        this.allDevIds = allDevIds;
    }

    public String getAllDevIds() 
    {
        return allDevIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("strategyName", getStrategyName())
            .append("scriptPath", getScriptPath())
            .append("description", getDescription())
            .append("createTime", getCreateTime())
            .append("devId", getDevId())
            .append("groups", getGroups())
            .append("allDevIds", getAllDevIds())
            .toString();
    }
}
