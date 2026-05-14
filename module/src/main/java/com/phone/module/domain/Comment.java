package com.phone.module.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.phone.common.annotation.Excel;
import com.phone.common.core.domain.BaseEntity;

/**
 * 抖音视频评论对象 comment
 * 
 * @author ruoyi
 * @date 2026-03-09
 */
public class Comment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 视频ID */
    @Excel(name = "视频ID")
    private String uid;
    private String douyinId;
    private String userDouyin;

    /** 用户UID */
    @Excel(name = "用户UID")
    private String userUid;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String nickname;

    /** 性别 */
    @Excel(name = "性别")
    private String gender;

    /** 年龄 */
    @Excel(name = "年龄")
    private String age;

    /** 真实姓名 */
    @Excel(name = "真实姓名")
    private String realName;

    /** 签名 */
    @Excel(name = "签名")
    private String signature;

    /** 粉丝数 */
    @Excel(name = "粉丝数")
    private String fansCount;

    /** 关注数 */
    @Excel(name = "关注数")
    private String followCount;

    /** 获赞数 */
    @Excel(name = "获赞数")
    private String likesCount;

    /** 作品数 */
    @Excel(name = "作品数")
    private String worksCount;

    /** 个人简介 */
    @Excel(name = "个人简介")
    private String profileText;

    /** IP归属地 */
    @Excel(name = "IP归属地")
    private String ipLocation;

    /** 地区 */
    @Excel(name = "地区")
    private String region;

    /** 头像地址 */
    @Excel(name = "头像地址")
    private String touxiang;

    /** 主页截图 */
    @Excel(name = "主页截图")
    private String zhuye;

    /** 评论内容 */
    @Excel(name = "评论内容")
    private String commentText;

    /** 回复对象 */
    @Excel(name = "回复对象")
    private String replyTo;

    /** 评论时间 */
    @Excel(name = "评论时间")
    private String commentTime;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    private Account userJson;

    public String getDouyinId() {
        return douyinId;
    }

    public void setDouyinId(String douyinId) {
        this.douyinId = douyinId;
    }

    public Account getUserJson() {
        return userJson;
    }

    public void setUserJson(Account userJson) {
        this.userJson = userJson;
    }


    public String getUserDouyin() {
        return userDouyin;
    }

    public void setUserDouyin(String userDouyin) {
        this.userDouyin = userDouyin;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getUid()
    {
        return uid;
    }
    public void setUserUid(String userUid)
    {
        this.userUid = userUid;
    }

    public String getUserUid()
    {
        return userUid;
    }
    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getNickname()
    {
        return nickname;
    }
    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getGender()
    {
        return gender;
    }
    public void setAge(String age)
    {
        this.age = age;
    }

    public String getAge()
    {
        return age;
    }
    public void setRealName(String realName)
    {
        this.realName = realName;
    }

    public String getRealName()
    {
        return realName;
    }
    public void setSignature(String signature)
    {
        this.signature = signature;
    }

    public String getSignature()
    {
        return signature;
    }
    public void setFansCount(String fansCount)
    {
        this.fansCount = fansCount;
    }

    public String getFansCount()
    {
        return fansCount;
    }
    public void setFollowCount(String followCount)
    {
        this.followCount = followCount;
    }

    public String getFollowCount()
    {
        return followCount;
    }
    public void setLikesCount(String likesCount)
    {
        this.likesCount = likesCount;
    }

    public String getLikesCount()
    {
        return likesCount;
    }
    public void setWorksCount(String worksCount)
    {
        this.worksCount = worksCount;
    }

    public String getWorksCount()
    {
        return worksCount;
    }
    public void setProfileText(String profileText)
    {
        this.profileText = profileText;
    }

    public String getProfileText()
    {
        return profileText;
    }
    public void setIpLocation(String ipLocation)
    {
        this.ipLocation = ipLocation;
    }

    public String getIpLocation()
    {
        return ipLocation;
    }
    public void setRegion(String region)
    {
        this.region = region;
    }

    public String getRegion()
    {
        return region;
    }
    public void setTouxiang(String touxiang)
    {
        this.touxiang = touxiang;
    }

    public String getTouxiang()
    {
        return touxiang;
    }
    public void setZhuye(String zhuye)
    {
        this.zhuye = zhuye;
    }

    public String getZhuye()
    {
        return zhuye;
    }
    public void setCommentText(String commentText)
    {
        this.commentText = commentText;
    }

    public String getCommentText()
    {
        return commentText;
    }
    public void setReplyTo(String replyTo)
    {
        this.replyTo = replyTo;
    }

    public String getReplyTo()
    {
        return replyTo;
    }
    public void setCommentTime(String commentTime)
    {
        this.commentTime = commentTime;
    }

    public String getCommentTime()
    {
        return commentTime;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("uid", getUid())
            .append("userUid", getUserUid())
            .append("nickname", getNickname())
            .append("gender", getGender())
            .append("age", getAge())
            .append("realName", getRealName())
            .append("signature", getSignature())
            .append("fansCount", getFansCount())
            .append("followCount", getFollowCount())
            .append("likesCount", getLikesCount())
            .append("worksCount", getWorksCount())
            .append("profileText", getProfileText())
            .append("ipLocation", getIpLocation())
            .append("region", getRegion())
            .append("touxiang", getTouxiang())
            .append("zhuye", getZhuye())
            .append("commentText", getCommentText())
            .append("replyTo", getReplyTo())
            .append("commentTime", getCommentTime())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .toString();
    }
}
