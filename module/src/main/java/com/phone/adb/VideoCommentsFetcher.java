package com.phone.adb;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import com.alibaba.fastjson.JSON;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.text.SimpleDateFormat;
import java.util.*;

public class VideoCommentsFetcher {
    private AppiumDriver<MobileElement> driver;

    public VideoCommentsFetcher(AppiumDriver<MobileElement> driver) {
        this.driver = driver;
    }

    public List<Map<String, Object>> fetchAllComments(int videoCount, String videosDir) {
        List<Map<String, Object>> commentsList = new ArrayList<>();
        Set<String> existingComments = new HashSet<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int totalComments = 0;

        try {
//          打开评论区
            String[] commentsBtnXpaths = {"//*[contains(@resource-id, 'comment') or contains(@text, '评论')][1]",
                    "//*[@content-desc='评论']"};
            boolean opened = false;
            for (String xpath : commentsBtnXpaths) {
                List<MobileElement> btns = driver.findElements(By.xpath(xpath));
                if (!btns.isEmpty()) {
                    btns.get(0).click();
                    Thread.sleep(2000);
                    opened = true;
                    System.out.println("成功打开视频 " + (videoCount + 1) + " 的评论区");
                    break;
                }
            }
            if (!opened) {
                System.err.println("无法打开视频 " + (videoCount + 1) + " 的评论区");
                return commentsList;
            }
//
//
//            System.out.println(driver.getPageSource());
////             1. 点击 FrameLayout 展开完整评论区
//            try {
//                List<MobileElement> commentFrames = driver.findElements(By.xpath(
//                        "//android.widget.FrameLayout[@resource-id='com.ss.android.ugc.aweme:id/i17']"
//                ));
//                if (!commentFrames.isEmpty()) {
//                    commentFrames.get(0).click();
//                    Thread.sleep(1500);
//                    System.out.println("点击展开评论区 FrameLayout");
//                }
//                driver.findElement(By.id("com.ss.android.ugc.aweme:id/i17")).click();
//                System.out.println("点击展开评论区 FrameLayout");
//            } catch (Exception ignored) {
//            }

            // 2. 获取总评论条数
            try {
                MobileElement totalCommentsEl = driver.findElement(By.xpath(
                        "//android.widget.TextView[@resource-id='com.ss.android.ugc.aweme:id/title']"
                ));
                String totalText = totalCommentsEl.getText().trim(); // 如 "2875条评论"
                totalComments = Integer.parseInt(totalText.replaceAll("\\D+", ""));
                System.out.println("视频总评论数: " + totalComments);
            } catch (Exception e) {
                System.err.println("获取总评论数失败: " + e.getMessage());
            }

            // 3. 滑动加载评论循环
            int estimatedCommentsPerScroll = 2;
            int maxScrolls = (int) Math.ceil(totalComments / (double) estimatedCommentsPerScroll);
            int scrollCount = 0;
            boolean hasNewComments = true;

//           开始获取评论
            int noNewCount = 0; // 连续滑动没有新评论计数

//            while (scrollCount < maxScrolls && commentsList.size() < totalComments) {
//                boolean hasNewThisScroll = false;
//
//                // 3a. 获取每条评论 FrameLayout
//                List<MobileElement> commentElements = driver.findElements(By.xpath(
//                        "//android.widget.FrameLayout[starts-with(@resource-id,'com.ss.android.ugc.aweme:id/e1-')]"
//                ));
//
//                for (MobileElement el : commentElements) {
//                    // 抓取主评论
//                    String nickname = "", text = "", time = "";
//                    try {
//                        nickname = el.findElement(By.xpath(".//android.widget.TextView[@resource-id='com.ss.android.ugc.aweme:id/title']")).getText().trim();
//                    } catch (Exception ignored) {
//                    }
//                    try {
//                        text = el.findElement(By.xpath(".//android.widget.TextView[@resource-id='com.ss.android.ugc.aweme:id/content']")).getText().trim();
//                    } catch (Exception ignored) {
//                    }
//                    try {
//                        time = el.findElement(By.xpath(".//android.widget.TextView[@resource-id='com.ss.android.ugc.aweme:id/egj']")).getText().trim();
//                    } catch (Exception ignored) {
//                    }
//
//                    if (!text.isEmpty() && !existingComments.contains(nickname + text)) {
//                        Map<String, Object> commentMap = new HashMap<>();
//                        commentMap.put("id", commentsList.size());
//                        commentMap.put("nickname", nickname);
//                        commentMap.put("text", text);
//                        commentMap.put("time", time.isEmpty() ? sdf.format(new Date()) : time);
//                        commentsList.add(commentMap);
//                        existingComments.add(nickname + text);
//                        hasNewThisScroll = true;
//                    }
//
////                     ----------- 抓取回复 -----------
//                    boolean hasExpand = true;
//                    while (hasExpand) {
//                        hasExpand = false;
//                        try {
//                            String[] EXPAND_REPLY_XPATHS = {
//                                    "//android.widget.TextView[contains(@text,'展开') and @resource-id='com.ss.android.ugc.aweme:id/3f7']",
//                                    "//android.widget.TextView[contains(@content-desc,'展开') and @resource-id='com.ss.android.ugc.aweme:id/3f7']"
//                            };
//                            for (String xpath : EXPAND_REPLY_XPATHS) {
//                                List<MobileElement> expandBtns = driver.findElements(By.xpath(xpath));
//                                for (MobileElement expandBtn : expandBtns) {
//                                    if (expandBtn.isDisplayed()) {
//                                        expandBtn.click();
//                                        System.out.println(driver.getPageSource());
//                                        Thread.sleep(500);
//                                        hasExpand = true;
//                                        hasNewThisScroll = true;
//                                    }
//                                }
//                            }
//
//// 1. 所有回复节点：e1- 是评论/回复的共同父容器
//                            List<MobileElement> replyElements = driver.findElements(
//                                    By.xpath("//android.view.ViewGroup[starts-with(@resource-id,'com.ss.android.ugc.aweme:id/e1')]")
//                            );
//
//                            Set<String> seen = new HashSet<>();
//
//                            for (MobileElement replyEl : replyElements) {
//                                String rNickname = "";
//                                String rText = "";
//                                String rTime = "";
//                                String rReplyTo = ""; // 有些是“回复 xxx”，有些没有
//
//                                // 2. 提取昵称（title）
//                                try {
//                                    rNickname = replyEl
//                                            .findElement(By.xpath(".//android.widget.TextView[@resource-id='com.ss.android.ugc.aweme:id/title']"))
//                                            .getText().trim();
//                                } catch (Exception ignored) {}
//
//                                // 3. 提取正文内容（content）
//                                try {
//                                    rText = replyEl
//                                            .findElement(By.xpath(".//android.widget.TextView[@resource-id='com.ss.android.ugc.aweme:id/content']"))
//                                            .getText().trim();
//                                } catch (Exception ignored) {}
//
//                                // 4. 提取回复对象（02w）可能有可能没有
//                                try {
//                                    rReplyTo = replyEl
//                                            .findElement(By.xpath(".//android.widget.TextView[@resource-id='com.ss.android.ugc.aweme:id/02w']"))
//                                            .getText().trim();
//                                } catch (Exception ignored) {}
//
//                                // 5. 提取时间
//                                try {
//                                    rTime = replyEl
//                                            .findElement(By.xpath(".//android.widget.TextView[@resource-id='com.ss.android.ugc.aweme:id/egj']"))
//                                            .getText().trim();
//                                } catch (Exception ignored) {}
//
//                                // 6. 无文本的跳过
//                                if (rText.isEmpty()) continue;
//
//                                // 7. 查重
//                                String key = rNickname + rText;
//                                if (seen.contains(key) || existingComments.contains(key)) continue;
//
//                                // 8. 写入
//                                Map<String, Object> replyMap = new HashMap<>();
//                                replyMap.put("id", commentsList.size());
//                                replyMap.put("nickname", rNickname);
//                                replyMap.put("replyTo", rReplyTo);
//                                replyMap.put("text", rText);
//                                replyMap.put("time", rTime.isEmpty() ? sdf.format(new Date()) : rTime);
//
//                                commentsList.add(replyMap);
//                                existingComments.add(key);
//                                seen.add(key);
//                                hasNewThisScroll = true;
//                            }
//
//                        } catch (Exception ignored) {
//                        }
//                    }
//                }
//
//                // 3c. 检查是否有新评论
//                if (!hasNewThisScroll) {
//                    noNewCount++; // 连续无新评论计数
//                } else {
//                    noNewCount = 0; // 有新评论则重置计数
//                }
//
//                if (noNewCount >= 5) { // 连续3次没有新评论，退出
//                    break;
//                }
//
//                // 3d. 滑动屏幕加载更多评论
//                int startX = driver.manage().window().getSize().width / 2;
//                int startY = (int) (driver.manage().window().getSize().height * 0.55);
//                int endY = (int) (driver.manage().window().getSize().height * 0.45);
//
//                new TouchAction<>(driver)
//                        .press(PointOption.point(startX, startY))
//                        .waitAction(WaitOptions.waitOptions(Duration.ofMillis(300)))
//                        .moveTo(PointOption.point(startX, endY))
//                        .release()
//                        .perform();
//
//                Thread.sleep(1500);
//                scrollCount++;
//            }
//




            // 4. 保存 JSON，包括 totalComments

            while (scrollCount < maxScrolls && commentsList.size() < totalComments) {
                boolean hasNewThisScroll = false;
                Set<String> seenRepliesThisScroll = new HashSet<>();

                // --------------------------
                // 1. 抓取主评论（FrameLayout/e1-）
                // --------------------------
                List<MobileElement> commentElements = driver.findElements(By.xpath(
                        "//android.widget.FrameLayout[starts-with(@resource-id,'com.ss.android.ugc.aweme:id/e1-')]"
                ));

                for (MobileElement el : commentElements) {
                    String nickname = "", text = "", time = "";

                    try {
                        nickname = el.findElement(By.xpath(".//android.widget.TextView[@resource-id='com.ss.android.ugc.aweme:id/title']")).getText().trim();
                    } catch (Exception ignored) {}
                    try {
                        text = el.findElement(By.xpath(".//android.widget.TextView[@resource-id='com.ss.android.ugc.aweme:id/content']")).getText().trim();
                    } catch (Exception ignored) {}
                    try {
                        time = el.findElement(By.xpath(".//android.widget.TextView[@resource-id='com.ss.android.ugc.aweme:id/egj']")).getText().trim();
                    } catch (Exception ignored) {}

                    if (!text.isEmpty()) {
                        String key = nickname + text;

                        if (!existingComments.contains(key)) {
                            Map<String, Object> commentMap = new HashMap<>();
                            commentMap.put("id", commentsList.size());
                            commentMap.put("nickname", nickname);
                            commentMap.put("text", text);
                            commentMap.put("time", time.isEmpty() ? sdf.format(new Date()) : time);

                            commentsList.add(commentMap);
                            existingComments.add(key);
                            hasNewThisScroll = true;
                        }
                    }
                }

                // ----------------------------------------
                // 2. 展开回复 & 抓取子回复（ViewGroup/e1）
                // ----------------------------------------
                boolean hasExpand = true;
                Set<String> clickedExpandNodes = new HashSet<>();

                while (hasExpand) {
                    hasExpand = false;

                    try {
                        String[] EXPAND_REPLY_XPATHS = {
                                "//android.widget.TextView[contains(@text,'展开') and @resource-id='com.ss.android.ugc.aweme:id/3f7']",
                                "//android.widget.TextView[contains(@content-desc,'展开') and @resource-id='com.ss.android.ugc.aweme:id/3f7']"
                        };

                        for (String xpath : EXPAND_REPLY_XPATHS) {
                            List<MobileElement> expandBtns = driver.findElements(By.xpath(xpath));

                            for (MobileElement expandBtn : expandBtns) {
                                try {
                                    if (!expandBtn.isDisplayed()) continue;

                                    // 防止重复点击
                                    String pos = expandBtn.getCenter().toString();
                                    if (clickedExpandNodes.contains(pos)) continue;

                                    expandBtn.click();
                                    Thread.sleep(500);

                                    clickedExpandNodes.add(pos);
                                    hasExpand = true;
                                    hasNewThisScroll = true;

                                } catch (Exception ignore2) {}
                            }
                        }

                        // ================================
                        // 抓取全部子回复（ViewGroup/e1）
                        // ================================
                        List<MobileElement> replyElements = driver.findElements(
                                By.xpath("//android.view.ViewGroup[starts-with(@resource-id,'com.ss.android.ugc.aweme:id/e1')]")
                        );

                        for (MobileElement replyEl : replyElements) {

                            // 过滤掉主评论（FrameLayout 的才是主评论）
                            if (replyEl.getTagName().equals("android.widget.FrameLayout")) continue;

                            String rNickname = "", rText = "", rTime = "", rReplyTo = "";

                            try {
                                rNickname = replyEl.findElement(By.xpath(".//android.widget.TextView[@resource-id='com.ss.android.ugc.aweme:id/title']")).getText().trim();
                            } catch (Exception ignored) {}

                            try {
                                rText = replyEl.findElement(By.xpath(".//android.widget.TextView[@resource-id='com.ss.android.ugc.aweme:id/content']")).getText().trim();
                            } catch (Exception ignored) {}

                            try {
                                rReplyTo = replyEl.findElement(By.xpath(".//android.widget.TextView[@resource-id='com.ss.android.ugc.aweme:id/02w']")).getText().trim();
                            } catch (Exception ignored) {}

                            try {
                                rTime = replyEl.findElement(By.xpath(".//android.widget.TextView[@resource-id='com.ss.android.ugc.aweme:id/egj']")).getText().trim();
                            } catch (Exception ignored) {}

                            if (rText.isEmpty()) continue;

                            // 回复的查重逻辑
                            String key = rNickname + rReplyTo + rText;
                            if (existingComments.contains(key) || seenRepliesThisScroll.contains(key)) continue;

                            Map<String, Object> replyMap = new HashMap<>();
                            replyMap.put("id", commentsList.size());
                            replyMap.put("nickname", rNickname);
                            replyMap.put("replyTo", rReplyTo);
                            replyMap.put("text", rText);
                            replyMap.put("time", rTime.isEmpty() ? sdf.format(new Date()) : rTime);

                            commentsList.add(replyMap);
                            existingComments.add(key);
                            seenRepliesThisScroll.add(key);

                            hasNewThisScroll = true;
                        }

                    } catch (Exception ignored) {}
                }

                // ======================
                // 3. 判断是否结束
                // ======================
//                if (!hasNewThisScroll) {
//                    noNewCount++;
//                } else {
//                    noNewCount = 0;
//                }
//                if (noNewCount >= 5) break;

                // ---------------------------------------------------
                // 3. 判断是否到底（替换掉你的 noNewCount 逻辑）
                // ---------------------------------------------------
                try {
                    List<MobileElement> endTips = driver.findElements(
                            By.xpath("//android.widget.TextView[contains(@text,'没有更多') or contains(@text,'暂时没有更多了')]")
                    );

                    if (!endTips.isEmpty()) {
                        System.out.println(">>> 已到底部，停止滚动");
                        break;
                    }
                } catch (Exception ignore) {}

                // ======================
                // 4. 滑动加载更多
                // ======================
                int startX = driver.manage().window().getSize().width / 2;
                int startY = (int) (driver.manage().window().getSize().height * 0.55);
                int endY = (int) (driver.manage().window().getSize().height * 0.45);

                new TouchAction<>(driver)
                        .press(PointOption.point(startX, startY))
                        .waitAction(WaitOptions.waitOptions(Duration.ofMillis(300)))
                        .moveTo(PointOption.point(startX, endY))
                        .release()
                        .perform();

                Thread.sleep(1500);
                scrollCount++;
            }

            if (!commentsList.isEmpty()) {
                String timestamp = String.valueOf(System.currentTimeMillis());
                String commentsFile = videosDir + "/video_" + timestamp+videoCount + "_comments.json";
                try (FileWriter file = new FileWriter(commentsFile)) {
                    Map<String, Object> output = new HashMap<>();
                    output.put("totalComments", totalComments);
                    output.put("comments", commentsList);
                    file.write(JSON.toJSONString(output, true));
                    System.out.println("评论信息已保存至: " + commentsFile);
                } catch (IOException e) {
                    System.err.println("保存评论时出错: " + e.getMessage());
                }
            } else {
                System.out.println("未获取到视频 " + (videoCount + 1) + " 的评论");
            }

            // 5. 关闭评论区
            driver.navigate().back();
            Thread.sleep(1000);

        } catch (Exception e) {
            System.err.println("获取评论时出错: " + e.getMessage());
        }

        return commentsList;

    }

}
