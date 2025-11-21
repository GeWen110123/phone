package com.phone.module.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.phone.common.annotation.Excel;
import com.phone.common.core.domain.BaseEntity;

/**
 * 设备下发命令对象 device_command
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
public class DeviceCommand
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 设备ID */
    @Excel(name = "设备ID")
    private Long deviceId;

    /** 命令（reboot、factory_reset、install_app等） */
    @Excel(name = "命令", readConverterExp = "r=eboot、factory_reset、install_app等")
    private String command;

    /** 命令参数 */
    @Excel(name = "命令参数")
    private String params;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 执行时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "执行时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date executeTime;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

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
    public void setCommand(String command) 
    {
        this.command = command;
    }

    public String getCommand() 
    {
        return command;
    }
    public void setParams(String params) 
    {
        this.params = params;
    }

    public String getParams() 
    {
        return params;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setExecuteTime(Date executeTime) 
    {
        this.executeTime = executeTime;
    }

    public Date getExecuteTime() 
    {
        return executeTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deviceId", getDeviceId())
            .append("command", getCommand())
            .append("params", getParams())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("executeTime", getExecuteTime())
            .toString();
    }
}
