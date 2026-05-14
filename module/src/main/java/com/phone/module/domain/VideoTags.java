package com.phone.module.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.phone.common.annotation.Excel;
import com.phone.common.core.domain.BaseEntity;

/**
 * VideoTags对象 video_tags
 * 
 * @author ruoyi
 * @date 2025-12-08
 */
public class VideoTags extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 设备id */
    @Excel(name = "设备id")
    private String devId;

    /** 抖音ID */
    @Excel(name = "抖音ID")
    private String douyinId;

    /** 勾选相关需要运行的项目 */
    @Excel(name = "勾选相关需要运行的项目")
    private String tags;


    private String status;
    private Long taskId;
    private Long counts;
    private int videoCount;
    private Long runNum;
    private String taskName;
    private String userId;
    private  Account account;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(int videoCount) {
        this.videoCount = videoCount;
    }

    public Long getRunNum() {
        return runNum;
    }

    public void setRunNum(Long runNum) {
        this.runNum = runNum;
    }

    public Long getCounts() {
        return counts;
    }

    public void setCounts(Long counts) {
        this.counts = counts;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDevId(String devId) 
    {
        this.devId = devId;
    }

    public String getDevId() 
    {
        return devId;
    }
    public void setDouyinId(String douyinId) 
    {
        this.douyinId = douyinId;
    }

    public String getDouyinId() 
    {
        return douyinId;
    }
    public void setTags(String tags) 
    {
        this.tags = tags;
    }

    public String getTags() 
    {
        return tags;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("devId", getDevId())
            .append("douyinId", getDouyinId())
            .append("createTime", getCreateTime())
            .append("tags", getTags())
            .toString();
    }
}
