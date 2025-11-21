package com.phone.module.douyin;

public class DouyinApiClient extends DouyinApiBase {

    public DouyinApiClient(String baseUrl) {
        super(baseUrl);
    }

    /**
     * 1. 获取抖音账号信息
     */
    public String getAccountInfo(String accountName) {
        String json = "{\n" +
                "  \"account_name\": \"" + accountName + "\",\n" +
                "  \"log_level\": \"INFO\"\n" +
                "}";
        try {
            String response = postJson("account-info", json);
            System.out.println("✅ 获取账号信息成功:\n" + prettyJson(response));
            return response;
        } catch (Exception e) {
            System.err.println("❌ 获取账号信息失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 2. 获取抖音账号粉丝列表
     */
    public String getFansList(String accountName, int maxUsers) {
        String json = "{\n" +
                "  \"account_name\": \"" + accountName + "\",\n" +
                "  \"max_users\": " + maxUsers + ",\n" +
                "  \"log_level\": \"INFO\"\n" +
                "}";
        try {
            String response = postJson("fans-list", json);
            System.out.println("✅ 获取粉丝列表成功:\n" + prettyJson(response));
            return response;
        } catch (Exception e) {
            System.err.println("❌ 获取粉丝列表失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 3. 获取抖音账号关注列表
     */
    public String getFollowingList(String accountName, int maxUsers) {
        String json = "{\n" +
                "  \"account_name\": \"" + accountName + "\",\n" +
                "  \"max_users\": " + maxUsers + ",\n" +
                "  \"log_level\": \"INFO\"\n" +
                "}";
        try {
            String response = postJson("following-list", json);
            System.out.println("✅ 获取关注列表成功:\n" + prettyJson(response));
            return response;
        } catch (Exception e) {
            System.err.println("❌ 获取关注列表失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 4. 录制抖音视频并获取评论
     */
    public String recordVideos(String accountName, int maxVideos, int maxComments) {
        String json = "{\n" +
                "  \"account_name\": \"" + accountName + "\",\n" +
                "  \"max_videos\": " + maxVideos + ",\n" +
                "  \"max_comments\": " + maxComments + ",\n" +
                "  \"log_level\": \"INFO\"\n" +
                "}";
        try {
            String response = postJson("record-videos", json);
            System.out.println("✅ 录制视频并获取评论成功:\n" + prettyJson(response));
            return response;
        } catch (Exception e) {
            System.err.println("❌ 录制视频失败: " + e.getMessage());
            return null;
        }
    }
}
