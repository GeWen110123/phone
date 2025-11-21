package com.phone.module.douyin;

import okhttp3.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Douyin API 基础类
 * 封装 HTTP 请求逻辑与 JSON 工具，兼容 JDK 1.8
 */
public class DouyinApiBase {

    protected static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    protected final OkHttpClient client;
    protected final String baseUrl;

    public DouyinApiBase(String baseUrl) {
        // 自动补全 URL 结尾斜杠
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";

        this.client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(600, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    /**
     * 发送 JSON POST 请求
     */
    protected String postJson(String endpoint, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(baseUrl + endpoint)
                .post(body)
                .header("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                String err = response.body() != null ? response.body().string() : "无响应内容";
                throw new IOException("请求失败，状态码: " + response.code() + "，错误信息: " + err);
            }
        }
    }

    /**
     * String.repeat()
     */
    protected static String repeat(String str, int count) {
        StringBuilder sb = new StringBuilder(str.length() * count);
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * JSON 输出
     */
    protected static String prettyJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return "";
        }

        StringBuilder pretty = new StringBuilder();
        int indent = 0;
        boolean inQuotes = false;
        boolean escape = false;

        for (char c : json.toCharArray()) {
            if (c == '"' && !escape) {
                inQuotes = !inQuotes;
            }

            if (!inQuotes) {
                switch (c) {
                    case '{':
                    case '[':
                        pretty.append(c).append('\n').append(repeat("  ", ++indent));
                        continue;
                    case '}':
                    case ']':
                        pretty.append('\n').append(repeat("  ", --indent)).append(c);
                        continue;
                    case ',':
                        pretty.append(c).append('\n').append(repeat("  ", indent));
                        continue;
                    case ':':
                        pretty.append(": ");
                        continue;
                    default:
                        break;
                }
            }

            pretty.append(c);
            escape = (c == '\\') && !escape; // 修正引号转义逻辑
        }

        return pretty.toString();
    }
}
