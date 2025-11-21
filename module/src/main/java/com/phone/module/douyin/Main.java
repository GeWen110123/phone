package com.phone.module.douyin;
public class Main {
    public static void main(String[] args) {
        DouyinApiClient client = new DouyinApiClient("http://localhost:8000/api/douyin/");

        // 获取账号基本信息
        client.getAccountInfo("抖音小助手");

        // 获取粉丝列表
        client.getFansList("抖音小助手", 100);

        // 获取关注列表
        client.getFollowingList("抖音小助手", 100);

        // 录制视频并抓取评论
        client.recordVideos("抖音小助手", 5, 50);
    }
}
