package com.phone.module.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.phone.common.annotation.Excel;
import com.phone.common.core.domain.BaseEntity;

/**
 * 设备安装应用对象 device_app
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
public class DeviceApp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 设备ID */
    @Excel(name = "设备ID")
    private Long deviceId;

    /** 应用名称 */
    @Excel(name = "应用名称")
    private String appName;

    /** 应用包名 */
    @Excel(name = "应用包名")
    private String packageName;

    /** 应用版本 */
    @Excel(name = "应用版本")
    private String version;
    private String status;

    /** 安装时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "安装时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date installTime;

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
    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }
    public void setAppName(String appName) 
    {
        this.appName = appName;
    }

    public String getAppName() 
    {
        return appName;
    }
    public void setPackageName(String packageName) 
    {
        this.packageName = packageName;
    }

    public String getPackageName() 
    {
        return packageName;
    }
    public void setVersion(String version) 
    {
        this.version = version;
    }

    public String getVersion() 
    {
        return version;
    }
    public void setInstallTime(Date installTime) 
    {
        this.installTime = installTime;
    }

    public Date getInstallTime() 
    {
        return installTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deviceId", getDeviceId())
            .append("appName", getAppName())
            .append("packageName", getPackageName())
            .append("version", getVersion())
            .append("installTime", getInstallTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
