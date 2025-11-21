package com.phone.module.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.phone.common.annotation.Excel;
import com.phone.common.core.domain.BaseEntity;

/**
 * 任务执行日志对象 task_log
 * 
 * @author ruoyi
 * @date 2025-11-07
 */
public class TaskLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 策略ID */
    @Excel(name = "任务ID")
    private Long strategyId;

    /** 目标ID */
    @Excel(name = "目标ID")
    private Long targetId;

    /** 执行结果 */
    @Excel(name = "执行结果")
    private String status;

    /** 执行日志详情 */
    @Excel(name = "执行日志详情")
    private String logDetail;

    /** 执行时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "执行时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date executeTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setStrategyId(Long strategyId) 
    {
        this.strategyId = strategyId;
    }

    public Long getStrategyId() 
    {
        return strategyId;
    }
    public void setTargetId(Long targetId) 
    {
        this.targetId = targetId;
    }

    public Long getTargetId() 
    {
        return targetId;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setLogDetail(String logDetail) 
    {
        this.logDetail = logDetail;
    }

    public String getLogDetail() 
    {
        return logDetail;
    }
    public void setExecuteTime(Date executeTime) 
    {
        this.executeTime = executeTime;
    }

    public Date getExecuteTime() 
    {
        return executeTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("strategyId", getStrategyId())
            .append("targetId", getTargetId())
            .append("status", getStatus())
            .append("logDetail", getLogDetail())
            .append("executeTime", getExecuteTime())
            .toString();
    }
}
