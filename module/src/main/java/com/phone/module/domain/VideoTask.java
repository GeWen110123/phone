package com.phone.module.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.phone.common.annotation.Excel;
import com.phone.common.core.domain.BaseEntity;

/**
 * video_task对象 video_task
 * 
 * @author ruoyi
 * @date 2025-12-09
 */
public class VideoTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 任务名 */
    @Excel(name = "任务名")
    private String taskName;

    /** 标签 */
    @Excel(name = "标签")
    private String tags;

    /** 抖音号 */
    @Excel(name = "抖音号")
    private String douyinId;
    /** 设备id */
    @Excel(name = "设备id")
    private String devId;
    private String type;
    private String statusSummary;

    public String getStatusSummary() {
        return statusSummary;
    }

    public void setStatusSummary(String statusSummary) {
        this.statusSummary = statusSummary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTaskName(String taskName) 
    {
        this.taskName = taskName;
    }

    public String getTaskName() 
    {
        return taskName;
    }
    public void setTags(String tags) 
    {
        this.tags = tags;
    }

    public String getTags() 
    {
        return tags;
    }
    public void setDouyinId(String douyinId) 
    {
        this.douyinId = douyinId;
    }

    public String getDouyinId() 
    {
        return douyinId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("taskName", getTaskName())
            .append("tags", getTags())
            .append("douyinId", getDouyinId())
            .append("remark", getRemark())
            .toString();
    }
}
