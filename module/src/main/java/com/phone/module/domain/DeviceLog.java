package com.phone.module.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.phone.common.annotation.Excel;
import com.phone.common.core.domain.BaseEntity;

/**
 * 设备活动日志对象 device_log
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
public class DeviceLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 设备ID */
    @Excel(name = "设备ID")
    private Long deviceId;

    /** 操作类型（安装、卸载、重启等） */
    @Excel(name = "操作类型", readConverterExp = "安=装、卸载、重启等")
    private String actionType;

    /** 操作详情 */
    @Excel(name = "操作详情")
    private String actionDetail;

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
    public void setActionType(String actionType) 
    {
        this.actionType = actionType;
    }

    public String getActionType() 
    {
        return actionType;
    }
    public void setActionDetail(String actionDetail) 
    {
        this.actionDetail = actionDetail;
    }

    public String getActionDetail() 
    {
        return actionDetail;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deviceId", getDeviceId())
            .append("actionType", getActionType())
            .append("actionDetail", getActionDetail())
            .append("createTime", getCreateTime())
            .toString();
    }
}
