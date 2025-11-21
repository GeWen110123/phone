package com.phone.module.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.phone.common.annotation.Excel;
import com.phone.common.core.domain.BaseEntity;

/**
 * DeviceGroup对象 device_group
 * 
 * @author ruoyi
 * @date 2025-11-04
 */
public class DeviceGroup extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 分组名称 */
    @Excel(name = "分组名称")
    private String groupName;

    /** 设备数量 */
    @Excel(name = "设备数量")
    private Long deviceCount;

    /** 设备名_id ,拼接 */
    @Excel(name = "设备名_id ,拼接")
    private String devId;
    private String users;

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setGroupName(String groupName) 
    {
        this.groupName = groupName;
    }

    public String getGroupName() 
    {
        return groupName;
    }
    public void setDeviceCount(Long deviceCount) 
    {
        this.deviceCount = deviceCount;
    }

    public Long getDeviceCount() 
    {
        return deviceCount;
    }
    public void setDevId(String devId) 
    {
        this.devId = devId;
    }

    public String getDevId() 
    {
        return devId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("groupName", getGroupName())
            .append("deviceCount", getDeviceCount())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("devId", getDevId())
            .toString();
    }
}
