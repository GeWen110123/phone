package com.phone.module.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.phone.common.annotation.Excel;
import com.phone.common.core.domain.BaseEntity;

/**
 * 设备信息综合对象 device
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
public class Device extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 设备码 */
    @Excel(name = "设备码")
    private String deviceCode;

    /** 设备名（可修改） */
    @Excel(name = "设备名", readConverterExp = "可=修改")
    private String deviceName;

    /** 手机型号 */
    @Excel(name = "手机型号")
    private String model;

    /** 系统版本 */
    @Excel(name = "系统版本")
    private String systemVersion;

    /** 内核版本 */
    @Excel(name = "内核版本")
    private String kernelVersion;

    /** 硬盘占用 */
    @Excel(name = "硬盘占用")
    private String storageUsage;

    /** 状态（已连接/未连接） */
    @Excel(name = "状态", readConverterExp = "已=连接/未连接")
    private String status;

    /** 最后在线时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最后在线时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastOnlineTime;


//    选择分组的id
    private List<Long> groupIds;
    private List<Long> ids;
    private String groups;


    private String users;

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public List<Long> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<Long> groupIds) {
        this.groupIds = groupIds;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDeviceCode(String deviceCode) 
    {
        this.deviceCode = deviceCode;
    }

    public String getDeviceCode() 
    {
        return deviceCode;
    }
    public void setDeviceName(String deviceName) 
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName() 
    {
        return deviceName;
    }
    public void setModel(String model) 
    {
        this.model = model;
    }

    public String getModel() 
    {
        return model;
    }
    public void setSystemVersion(String systemVersion) 
    {
        this.systemVersion = systemVersion;
    }

    public String getSystemVersion() 
    {
        return systemVersion;
    }
    public void setKernelVersion(String kernelVersion) 
    {
        this.kernelVersion = kernelVersion;
    }

    public String getKernelVersion() 
    {
        return kernelVersion;
    }
    public void setStorageUsage(String storageUsage) 
    {
        this.storageUsage = storageUsage;
    }

    public String getStorageUsage() 
    {
        return storageUsage;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setLastOnlineTime(Date lastOnlineTime) 
    {
        this.lastOnlineTime = lastOnlineTime;
    }

    public Date getLastOnlineTime() 
    {
        return lastOnlineTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deviceCode", getDeviceCode())
            .append("deviceName", getDeviceName())
            .append("model", getModel())
            .append("systemVersion", getSystemVersion())
            .append("kernelVersion", getKernelVersion())
            .append("storageUsage", getStorageUsage())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("lastOnlineTime", getLastOnlineTime())
            .toString();
    }
}
