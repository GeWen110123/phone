package com.phone.module.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.phone.common.annotation.Excel;
import com.phone.common.core.domain.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * 抖音账号信息对象 account
 * 
 * @author ruoyi
 * @date 2025-11-10
 */
public class Account extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 抖音账号ID */
    @Excel(name = "抖音账号ID")
    private String douyinId;

    /** {
"id":"id",
  "nickname": "昵称",
  "avatar": "头像URL",
  "like_count": "获赞数",
  "following_count": "关注数",
  "follower_count": "粉丝数",
  "video_count": "作品数",
  "region": "地区",
  "gender": "性别",
  "age": "年龄",
  "school": "学校",
  "bio": "个人简介",
  "verified_info": "实名信息"
} */

    private String jsonString;
    private String oldString;
    private String devId;
    private String followStatus;
    private String fansStatus;
    private String tags;
    private String status;




    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date oldTime;
    private long counts;
    private long commentCount;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public String getFollowStatus() {
        return followStatus;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setFollowStatus(String followStatus) {
        this.followStatus = followStatus;
    }

    public String getFansStatus() {
        return fansStatus;
    }

    public void setFansStatus(String fansStatus) {
        this.fansStatus = fansStatus;
    }

    public long getCounts() {
        return counts;
    }

    public void setCounts(long counts) {
        this.counts = counts;
    }

    public Date getOldTime() {
        return oldTime;
    }

    public void setOldTime(Date oldTime) {
        this.oldTime = oldTime;
    }

    public String getOldString() {
        return oldString;
    }

    public void setOldString(String oldString) {
        this.oldString = oldString;
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
    public void setDouyinId(String douyinId) 
    {
        this.douyinId = douyinId;
    }

    public String getDouyinId() 
    {
        return douyinId;
    }
    public void setJsonString(String jsonString) 
    {
        this.jsonString = jsonString;
    }

    public String getJsonString() 
    {
        return jsonString;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("douyinId", getDouyinId())
            .append("createTime", getCreateTime())
            .append("jsonString", getJsonString())
            .toString();
    }
}
