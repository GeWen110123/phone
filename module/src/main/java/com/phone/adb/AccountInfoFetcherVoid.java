package com.phone.adb;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ⭐⭐⭐
 * 抖音账号作品采集器（录制视频、解析统计数据、抓取评论）
 * — 全程基于 Appium + ADB —
 * — 所有方法逻辑保持原样，仅进行排版/注释整理 —
 * ⭐⭐⭐
 */
public class AccountInfoFetcherVoid {

    private final AppiumDriver<MobileElement> driver;
    private final Logger logger = Logger.getLogger(AccountInfoFetcherVoid.class.getName());

    private final String videoOutputFolder = "D:\\douyin_output\\video";
    private final String screenshotFolder = "D:\\douyin_output\\images";

    private static final String ADB = "C:\\Android\\sdk\\platform-tools\\adb.exe";

    public AccountInfoFetcherVoid(AppiumDriver<MobileElement> driver) {
        this.driver = driver;
        try {
            Files.createDirectories(Paths.get(videoOutputFolder));
            Files.createDirectories(Paths.get(screenshotFolder));
        } catch (Exception e) {
            logger.warning("创建输出目录失败: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // 数字字符串清洗工具（1.2w、3k → 12000 / 3000）
    // ---------------------------------------------------------
    public static String cleanNumberString(String text) {
        if (text == null) return "0";
        text = text.replace(",", "").trim();
        try {
            if (text.endsWith("w") || text.endsWith("W"))
                return String.valueOf((int) (Double.parseDouble(text.substring(0, text.length() - 1)) * 10000));
            if (text.endsWith("万"))
                return String.valueOf((int) (Double.parseDouble(text.replace("万", "")) * 10000));
            if (text.endsWith("k") || text.endsWith("K"))
                return String.valueOf((int) (Double.parseDouble(text.substring(0, text.length() - 1)) * 1000));
            return String.valueOf(Integer.parseInt(text));
        } catch (Exception e) {
            return text.replaceAll("\\D", "");
        }
    }

    // ---------------------------------------------------------
    // 页面元素查找封装
    // ---------------------------------------------------------
    private List<MobileElement> findAll(String xpath) {
        try {
            return driver.findElements(By.xpath(xpath));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private Optional<MobileElement> findFirst(String xpath) {
        List<MobileElement> list = findAll(xpath);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    private boolean exists(String xpath) {
        return !findAll(xpath).isEmpty();
    }

    private boolean anyExists(String[] xps) {
        if (xps == null) return false;
        for (String xp : xps)
            if (xp != null && exists(xp)) return true;
        return false;
    }

    private String safeText(MobileElement e) {
        try {
            return Optional.ofNullable(e.getText()).orElse("").trim();
        } catch (Exception ex) {
            try {
                return Optional.ofNullable(e.getAttribute("text")).orElse("").trim();
            } catch (Exception ignore) {
                return "";
            }
        }
    }

    // ---------------------------------------------------------
    // 滑动逻辑 (Appium 滑动 + 统一坐标)
    // ---------------------------------------------------------
    private void swipe(int startX, int startY, int endX, int endY, int durationMs) {
        try {
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
            Sequence swipe = new Sequence(finger, 1);

            swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
            swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), endX, endY));
            swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Collections.singletonList(swipe));
            Thread.sleep(200);
        } catch (Exception e) {
            logger.fine("swipe failed: " + e.getMessage());
        }
    }

    private boolean swipeUp() {
        try {
            int w = driver.manage().window().getSize().width;
            int h = driver.manage().window().getSize().height;
            swipe(w / 2, (int) (h * 0.75), w / 2, (int) (h * 0.25), 500);
            return true;
        } catch (Exception e) {
            logger.fine("swipeUp error: " + e.getMessage());
            return false;
        }
    }

    private boolean swipeToNextVideo() {
        return swipeUp();
    }

    // ---------------------------------------------------------
    // 页面判断 & 点击进入作品区
    // ---------------------------------------------------------
    private boolean isOnProfilePage() {
        String[] xps = {
                "//*[contains(@resource-id, 'nickname')]",
                "//*[contains(@resource-id, 'user_profile')]",
                "//*[contains(@text, '关注') and contains(@text, '粉丝')]",
                "//*[contains(@text, '获赞')]"
        };
        return anyExists(xps);
    }

    private boolean enterVideoList() {
        String[] xps = {
                "//*[@resource-id='com.ss.android.ugc.aweme:id/feu']",
                "//*[contains(@resource-id, 'work') or contains(@resource-id, 'video')]",
                "//android.view.ViewGroup[contains(@resource-id, 'work')]",
                "//*[contains(@text, '置顶')]"
        };

        for (String xp : xps) {
            Optional<MobileElement> opt = findFirst(xp);
            if (opt.isPresent()) {
                try {
                    opt.get().click();
                    Thread.sleep(800);
                    return true;
                } catch (Exception ignored) {}
            }
        }
        return false;
    }

    // ---------------------------------------------------------
    // 单个视频数据读取：UID/描述/计数
    // ---------------------------------------------------------
    private String getVideoUniqueId() {
        String[] xps = {"//*[contains(@resource-id,'video_id')]",
                "//*[contains(@resource-id,'player')]"};
        for (String xp : xps) {
            List<MobileElement> eles = findAll(xp);
            if (!eles.isEmpty()) {
                String t = safeText(eles.get(0));
                if (!t.isEmpty()) {
                    System.out.println("xp:::==="+xp);
                    return t;
                }
            }
        }
        return UUID.randomUUID().toString();
    }

    private String getVideoDescription() {
        String[] xps = {
                "//*[contains(@resource-id,'desc')]",
                "//*[contains(@text,'#')]",
                "//*[contains(@resource-id,'title')]"
        };
        for (String xp : xps) {
            List<MobileElement> eles = findAll(xp);
            if (!eles.isEmpty()) {
                System.out.println("xp::==="+xp);
                return safeText(eles.get(0));
            }
        }
        return "";
    }

    private String getVideoStat(String statType) {
        Map<String, String[]> xpathMap = new HashMap<>();

        xpathMap.put("赞", new String[]{
                "//*[@resource-id='com.ss.android.ugc.aweme:id/f9g']",
                "//*[@resource-id='com.ss.android.ugc.aweme:id/f94']",
                "//*[contains(@resource-id,'like_count')]"
        });

        xpathMap.put("评论", new String[]{
                "//*[@resource-id='com.ss.android.ugc.aweme:id/d+_']//android.widget.TextView",
                "//*[contains(@content-desc, '评论')]",
                "//*[contains(@resource-id, 'comment')]"
        });

        xpathMap.put("分享", new String[]{
                "//*[@resource-id='com.ss.android.ugc.aweme:id/yea']",
                "//*[@resource-id='com.ss.android.ugc.aweme:id/x+j']",
                "//*[contains(@resource-id,'share_count')]"
        });

        xpathMap.put("收藏", new String[]{
                "//*[@resource-id='com.ss.android.ugc.aweme:id/d9n']",
                "//*[@resource-id='com.ss.android.ugc.aweme:id/d8d']",
                "//*[contains(@resource-id,'collect_count')]"
        });

        String[] xpaths = xpathMap.getOrDefault(statType, new String[]{});
        for (String xp : xpaths) {
            List<MobileElement> eles = findAll(xp);
            for (MobileElement e : eles) {
                String text = safeText(e);
                if (text != null && text.matches(".*\\d.*"))
                    System.out.println("p::=="+xp);
                    return cleanNumberString(text);
            }
        }
        return "0";
    }

    // ---------------------------------------------------------
    // 评论区抓取
    // ---------------------------------------------------------
    private boolean openComments() {
        String[] xps = {
                "//*[@resource-id='com.ss.android.ugc.aweme:id/ap_']",
                "//*[contains(@resource-id,'comments')]"
        };
        for (String xp : xps) {
            Optional<MobileElement> opt = findFirst(xp);
            if (opt.isPresent()) {
                try {
                    opt.get().click();
                    Thread.sleep(600);
                    return true;
                } catch (Exception ignored) {}
            }
        }
        return false;
    }

    private void closeComments() {
        try {
            driver.navigate().back();
            Thread.sleep(400);
        } catch (Exception ignored) {}
    }

    private List<String> getAllComments() {
        List<String> out = new ArrayList<>();
        String xp = "//*[contains(@resource-id,'comment')]";

        for (int i = 0; i < 20; i++) {
            List<MobileElement> eles = findAll(xp);
            for (MobileElement e : eles) {
                String t = safeText(e);
                if (!t.isEmpty()) out.add(t);
            }
            if (!swipeUp()) break;
        }
        return out;
    }

    // ---------------------------------------------------------
    // ⭐ 视频时长解析（线程版）— 滑动保持进度条可见 + pagSource 解析 mm:ss
    // ---------------------------------------------------------
    private int extractVideoDuration(String deviceId) {
        final int maxAttempts = 3;
        final int[] videoDurationHolder = {0};

        try {
            // 点击让进度条出现
            adbTap(deviceId, 540, 1200);

            // 启动解析线程
            Thread extractor = new Thread(() -> {
                int attempts = 0;
                while (attempts < maxAttempts && videoDurationHolder[0] <= 0) {
                    attempts++;
                    try {
                        Thread.sleep(450);

                        String xml = driver.getPageSource();
                        Integer parsed = parseDurationFromXml(xml);

                        if (parsed != null && parsed > 0) {
                            videoDurationHolder[0] = parsed;
                            System.out.println("解析到视频时长: " + parsed + " 秒");
                            return;
                        }
                        Thread.sleep(150);
                    } catch (Exception ignored) {}
                }
            });

            extractor.start();

            // 慢滑动保持视频进度条活跃
            adbSwipeSlow(deviceId, 300, 2088, 900, 2088, 3000);

            // 等待线程
            extractor.join(3500);
            Thread.sleep(500);

            if (videoDurationHolder[0] <= 0) {
                System.out.println("未获取到视频时长");
                return 0;
            }
            return videoDurationHolder[0];

        } catch (Exception e) {
            System.out.println("extractVideoDuration 异常: " + e.getMessage());
            return 14;
        }
    }

    private void adbTap(String deviceId, int x, int y) throws Exception {
        new ProcessBuilder(ADB, "-s", deviceId, "shell", "input", "tap",
                String.valueOf(x), String.valueOf(y))
                .start().waitFor();
    }

    private void adbSwipeSlow(String deviceId, int startX, int startY,
                              int endX, int endY, int durationMs) throws Exception {
        new ProcessBuilder(ADB, "-s", deviceId, "shell", "input", "swipe",
                String.valueOf(startX), String.valueOf(startY),
                String.valueOf(endX), String.valueOf(endY),
                String.valueOf(durationMs))
                .start().waitFor();
    }

    private Integer parseDurationFromXml(String xml) {
        if (xml == null) return null;

        Pattern p = Pattern.compile("text=\"(\\d{1,2}:\\d{1,2})\"");
        Matcher m = p.matcher(xml);

        while (m.find()) {
            String time = m.group(1);
            String[] parts = time.split(":");
            try {
                int minutes = Integer.parseInt(parts[0]);
                int seconds = Integer.parseInt(parts[1]);
                return minutes * 60 + seconds;
            } catch (Exception ignored) {}
        }
        return null;
    }


    private String recordVideoToFile(int index, int durationSec, String deviceId) {

        int segmentLength = 50; // 每段50秒
        int totalSegments = (int) Math.ceil(durationSec / (double) segmentLength); // 向上取整

        String timestamp = String.valueOf(System.currentTimeMillis());

        // 最终输出文件名（无 part）
        String finalFilename = String.format(
                "%s/video_%03d_%s.mp4", videoOutputFolder, index + 1, timestamp);

        List<String> segmentFiles = new ArrayList<>();

        try {
            AndroidDriver<?> androidDriver = (AndroidDriver<?>) driver;

            for (int part = 1; part <= totalSegments; part++) {

                int startTime = (part - 1) * segmentLength;
                int thisDuration = Math.min(segmentLength, durationSec - startTime);

                if (thisDuration <= 0) break; // 防止最后一段时间为0或负数

                String partFile = String.format(
                        "%s/video_%03d_%s_part%02d.mp4",
                        videoOutputFolder, index + 1, timestamp, part);

                logger.info("开始录制分段 " + part + "/" + totalSegments +
                        " → " + thisDuration + " 秒 → " + partFile);

                // 开始录屏
                androidDriver.startRecordingScreen();

                // 等待录屏结束 (+1秒缓冲)
                Thread.sleep((thisDuration + 1) * 1000L);

                String base64 = androidDriver.stopRecordingScreen();

                // 写入文件
                Files.write(Paths.get(partFile), Base64.getDecoder().decode(base64));

                segmentFiles.add(partFile);
            }

            // 如果只有一段，直接返回文件
            if (segmentFiles.size() == 1) {
                return segmentFiles.get(0);
            }else {

            }

            // 多段视频 → 异步合并
            VideoMergeTest.mergeVideoSegments(segmentFiles, finalFilename);

            return finalFilename;

        } catch (Exception e) {
            logger.warning("分段录制失败: " + e.getMessage());
            return "";
        }
    }

    // ---------------------------------------------------------
    // 截图保存
    // ---------------------------------------------------------
    private String saveScreenshot(int index) {
        try {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String filename = String.format("%s\\screenshot_%03d_%s.png",
                    screenshotFolder, index + 1, timestamp);

            File src = driver.getScreenshotAs(org.openqa.selenium.OutputType.FILE);
            File dest = new File(filename);

            if (!dest.getParentFile().exists()) dest.getParentFile().mkdirs();
            src.renameTo(dest);

            return filename;

        } catch (Exception e) {
            logger.warning("截图保存失败: " + e.getMessage());
            return "";
        }
    }


    public static final String[] WORKS_COUNT_XPATHS = {
            "//*[contains(@text, '作品')]",
            "//*[contains(@content-desc, '作品')]",
            "//*[contains(@text, '作品') and not(contains(@resource-id, 'tab'))]/preceding-sibling::android.widget.TextView[1]",
            "//*[contains(@text, '作品') and not(contains(@resource-id, 'tab'))]/following-sibling::android.widget.TextView[1]",
            "//*[contains(@resource-id, 'works') and not(contains(@resource-id, 'tab'))]",
            "//*[contains(@resource-id, 'video') and not(contains(@resource-id, 'tab'))]",
            "//*[@resource-id='com.ss.android.ugc.aweme:id/f73']",
            "//*[contains(@resource-id, 'tab_title') and contains(@text, '作品')]/../preceding-sibling::*[1]",
            "//*[@text='作品']/../android.widget.TextView[1]",
            "//*[contains(@text, '作品')]/..//android.widget.TextView[1]",
            "//*[contains(@text, '作品')]/ancestor::*[1]/android.widget.TextView[1]"
    };


    // ---------------------------------------------------------
    // ⭐ 主流程：录制所有视频 + 获取评论 + 截图 + 保存数据
    // ---------------------------------------------------------
    public List<Map<String, Object>> recordAllVideosAndComments(String deviceId) {

        logger.info("开始录制所有视频并获取评论");

        List<Map<String, Object>> allVideoData = new ArrayList<>();
        Set<String> processed = new HashSet<>();

        int index = 0;
        int maxVideos = 0;

        // 必须进入主页 & 作品区
        if (!isOnProfilePage()) {
            logger.warning("不在个人主页");
            return allVideoData;
        }
        if (!enterVideoList()) {
            logger.warning("无法进入作品区");
            return allVideoData;
        }

        for (String xpath : WORKS_COUNT_XPATHS) {
            try {
                MobileElement el = (MobileElement) driver.findElement(By.xpath(xpath));
                String text = el.getText().trim();

                if (!text.isEmpty()) {
                    try {
                        // 去掉非数字字符，只取数字
                        String numStr = text.replaceAll("[^0-9]", "");
                        if (!numStr.isEmpty()) {
                            maxVideos = Integer.parseInt(numStr);
                            logger.info("解析到作品数量: " + maxVideos);
                            break; // 找到第一个有效的就用
                        }
                    } catch (NumberFormatException ignored) {}
                }
            } catch (Exception e) {
                // 找不到元素就跳到下一个 xpath
            }
        }

        if (maxVideos <= 0) {
            logger.warning("无法解析作品数量，使用默认 100");
            maxVideos = 100;
        }

        logger.info("总共需要处理的视频数量: " + maxVideos);

        while (index < maxVideos) {
            try {
                // 确保最新 PageSource
                driver.getPageSource();

                logger.info("处理第 " + (index + 1) + " 个视频");

                // ◆ 视频 UID 防重复
                String uid = getVideoUniqueId();
                if (uid != null && processed.contains(uid)) {
                    logger.info("视频已处理过，跳过");
                    if (!swipeToNextVideo()) break; // 滑动失败就退出循环
                    continue; // 跳到下一条视频
                }
                if (uid != null) processed.add(uid);

                // ◆ 保存视频信息
                Map<String, Object> video = new LinkedHashMap<>();
                video.put("video_index", index + 1);
                video.put("timestamp", LocalDateTime.now().toString());
                video.put("description", getVideoDescription());
                video.put("likes_count", getVideoStat("赞"));
                video.put("comments_count", getVideoStat("评论"));
                video.put("share_count", getVideoStat("分享"));
                video.put("collect_count", getVideoStat("收藏"));

                // ◆ 线程版解析视频时长（增加重试，确保获取到）
                int duration = 0;
                int maxRetry = 5;
                for (int attempt = 1; attempt <= maxRetry; attempt++) {
                    duration = extractVideoDuration(deviceId);
                    if (duration > 0) break;
                    logger.info("⚠️ 视频时长获取失败，重试 " + attempt + "/" + maxRetry);
                    Thread.sleep(500); // 等待0.5秒再试
                }
                if (duration <= 0) duration = 50; // 防止解析失败时，给默认50秒
                logger.info("🎬 解析到视频时长: " + duration + " 秒");

                // ◆ 录制视频
                String videoPath = recordVideoToFile(index, duration, deviceId);
                video.put("video_path", videoPath);

                // ◆ 截图
                String screenshotPath = saveScreenshot(index);
                video.put("screenshot_path", screenshotPath);

                // ◆ 评论
                if (openComments()) {
                    List<String> comments = getAllComments();
                    video.put("comments", comments);
                    video.put("comments_count_actual", comments.size());
                    closeComments();
                } else {
                    video.put("comments", Collections.emptyList());
                    video.put("comments_count_actual", 0);
                }

                allVideoData.add(video);
                logger.info("第 " + (index + 1) + " 个视频处理完成");

                // ◆ 滑到下一个视频，确保 UI 渲染完成
                if (!swipeToNextVideo()) break;
                Thread.sleep(500); // 等待0.5秒确保下一个视频加载完成

                index++;

            } catch (Exception e) {
                logger.warning("处理视频异常: " + e.getMessage());
                index++;
            }
        }

        logger.info("总共处理视频数量: " + allVideoData.size());
        return allVideoData;
    }

}
