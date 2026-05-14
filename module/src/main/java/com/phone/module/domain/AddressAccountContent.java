package com.phone.module.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.phone.common.annotation.Excel;
import com.phone.common.core.domain.BaseEntity;

/**
 * 地址评论账号信息对象 address_account_content
 * 
 * @author ruoyi
 * @date 2026-01-09
 */
public class AddressAccountContent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 地址视频uid */
    @Excel(name = "地址视频uid")
    private String aVId;

    /** 抖音账号ID */
    @Excel(name = "抖音账号ID")
    private String douyinId;

    private String jsonString;

    /** 设备吗 */
    @Excel(name = "设备吗")
    private String devId;
    private String accountName;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /** 评论次数 */
    @Excel(name = "评论次数")
    private Long count;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setaVId(String aVId) 
    {
        this.aVId = aVId;
    }

    public String getaVId() 
    {
        return aVId;
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
    public void setDevId(String devId) 
    {
        this.devId = devId;
    }

    public String getDevId() 
    {
        return devId;
    }
    public void setCount(Long count) 
    {
        this.count = count;
    }

    public Long getCount() 
    {
        return count;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("aVId", getaVId())
            .append("douyinId", getDouyinId())
            .append("createTime", getCreateTime())
            .append("jsonString", getJsonString())
            .append("updateTime", getUpdateTime())
            .append("devId", getDevId())
            .append("count", getCount())
            .toString();
    }
}
