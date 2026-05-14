package com.phone.module.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.phone.common.annotation.Excel;
import com.phone.common.core.domain.BaseEntity;

/**
 * video对象 video
 * 
 * @author ruoyi
 * @date 2025-12-08
 */
public class Video extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 视频ID */
    @Excel(name = "视频ID")
    private Long videoIndex;

    /** 设备id */
    @Excel(name = "设备id")
    private String devId;
    private String uid;

    /** 抖音ID */
    @Excel(name = "抖音ID")
    private String douyinId;

    /** 评论内容 */
    @Excel(name = "评论内容")
    private String content;

    /** 视频地址 */
    @Excel(name = "视频地址")
    private String videoPath;

    /** 页面截图地址 */
    @Excel(name = "页面截图地址")
    private String imagePath;

    /** json串（视频uuid，点赞，评论，分享，收藏数） */
    @Excel(name = "json串", readConverterExp = "视=频uuid，点赞，评论，分享，收藏数")
    private String jsonString;

    /** 评论文件地址 */
    @Excel(name = "评论文件地址")
    private String jsonPath;
    private String address;
    private Account userJson;

    public Account getUserJson() {
        return userJson;
    }

    public void setUserJson(Account userJson) {
        this.userJson = userJson;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setVideoIndex(Long videoIndex) 
    {
        this.videoIndex = videoIndex;
    }

    public Long getVideoIndex() 
    {
        return videoIndex;
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
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setVideoPath(String videoPath) 
    {
        this.videoPath = videoPath;
    }

    public String getVideoPath() 
    {
        return videoPath;
    }
    public void setImagePath(String imagePath) 
    {
        this.imagePath = imagePath;
    }

    public String getImagePath() 
    {
        return imagePath;
    }
    public void setJsonString(String jsonString) 
    {
        this.jsonString = jsonString;
    }

    public String getJsonString() 
    {
        return jsonString;
    }
    public void setJsonPath(String jsonPath) 
    {
        this.jsonPath = jsonPath;
    }

    public String getJsonPath() 
    {
        return jsonPath;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("videoIndex", getVideoIndex())
            .append("devId", getDevId())
            .append("douyinId", getDouyinId())
            .append("createTime", getCreateTime())
            .append("content", getContent())
            .append("videoPath", getVideoPath())
            .append("imagePath", getImagePath())
            .append("jsonString", getJsonString())
            .append("jsonPath", getJsonPath())
            .toString();
    }
}
