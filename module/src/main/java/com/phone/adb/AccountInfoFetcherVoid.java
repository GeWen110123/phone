package com.phone.adb;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.phone.common.constant.Constants;
import com.phone.common.utils.StringUtils;
import com.phone.module.domain.Account;
import com.phone.module.domain.AddressVideo;
import com.phone.module.domain.Video;
import com.phone.module.mapper.VideoMapper;
import com.phone.module.service.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.codec.digest.DigestUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

    private final String videoOutputFolder = "F:\\douyin_output\\video";
    private final String screenshotFolder = "F:\\douyin_output\\images";

    private static final String ADB = "C:\\Android\\sdk\\platform-tools\\adb.exe";

    private final IAddressVideoService addressVideoService;
    private final IVideoService videoService;

    private final DouyinTaskService douyinTaskService;

    private final IAccountContentService accountContentService;
    private final IAddressAccountContentService addressAccountContentService;
    private final IAccountService accountService;

    public AccountInfoFetcherVoid(AppiumDriver<MobileElement> driver,
                                  IVideoService videoService,
                                  IAddressVideoService addressVideoService,
                                  DouyinTaskService douyinTaskService,
                                  IAccountContentService accountContentService,
                                  IAddressAccountContentService addressAccountContentService,
                                  IAccountService accountService
    ) {
        this.driver = driver;
        this.videoService = videoService;
        this.addressVideoService = addressVideoService;
        this.douyinTaskService = douyinTaskService;
        this.accountContentService = accountContentService;
        this.addressAccountContentService = addressAccountContentService;
        this.accountService = accountService;
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
// ---------------------------------------------------------
//  数字字符串清洗工具（最强版）
// ---------------------------------------------------------
    public static String cleanNumberString(String text) {
        if (text == null) return "0";

        // 去除空格、逗号
        text = text.replace(",", "").trim();

        // 去掉前后的非数字字符（如：点赞1.2w）
        text = text.replaceAll("^[^0-9]+", "");
        text = text.replaceAll("[^0-9a-zA-Z万亿\\.]+$", "");

        try {
            // w / W 处理（默认 *10000）
            if (text.endsWith("w") || text.endsWith("W")) {
                double v = Double.parseDouble(text.substring(0, text.length() - 1));
                return String.valueOf((long) (v * 10000));
            }

            // 万 处理
            if (text.endsWith("万")) {
                double v = Double.parseDouble(text.replace("万", ""));
                return String.valueOf((long) (v * 10000));
            }

            // k / K 处理
            if (text.endsWith("k") || text.endsWith("K")) {
                double v = Double.parseDouble(text.substring(0, text.length() - 1));
                return String.valueOf((long) (v * 1000));
            }

            // 亿 处理
            if (text.endsWith("亿")) {
                double v = Double.parseDouble(text.replace("亿", ""));
                return String.valueOf((long) (v * 100000000));
            }

            // b / B（海外常用 billion）
            if (text.endsWith("b") || text.endsWith("B")) {
                double v = Double.parseDouble(text.substring(0, text.length() - 1));
                return String.valueOf((long) (v * 1000000000));
            }

            // 纯数字
            return String.valueOf(Long.parseLong(text));

        } catch (Exception e) {
            // fallback：保留数字
            String digits = text.replaceAll("\\D", "");
            return digits.isEmpty() ? "0" : digits;
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


    private boolean enterAuthorProfile() {
        try {
            By authorBtn = By.xpath(
                    "//android.widget.TextView[@resource-id='com.ss.android.ugc.aweme:id/title' and starts-with(@text,'@')]"
            );

            MobileElement el = driver.findElement(authorBtn);
            tapCenter(el);
            return true;

        } catch (Exception e) {
            logger.warning("enterAuthorProfile failed: " + e.getMessage());
            return false;
        }
    }

    private boolean waitUntilProfilePage() {
        long end = System.currentTimeMillis() + 8000;

        while (System.currentTimeMillis() < end) {
            boolean inProfile = !driver.findElements(
                    By.xpath("//*[contains(@text,'作品') or contains(@text,'粉丝')]")
            ).isEmpty();

            if (inProfile) {
                return true;
            }

            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {
            }
        }
        return false;
    }


    private void tapCenter(MobileElement el) {
        Rectangle r = el.getRect();
        int x = r.getX() + r.getWidth() / 2;
        int y = r.getY() + r.getHeight() / 2;

        new TouchAction<>(driver)
                .tap(PointOption.point(x, y))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(150)))
                .perform();
    }

    private boolean enterVideoList() {
        String[] xps = {
                // 优先级1：直接点击第一个可点击的视频项（核心目标元素）
                "//androidx.recyclerview.widget.RecyclerView[@resource-id='com.ss.android.ugc.aweme:id/il6']//android.view.View[@resource-id='com.ss.android.ugc.aweme:id/p=z'][1]",
                // 优先级2：作品Tab入口（确保先进入作品列表页）
                "//androidx.appcompat.app.ActionBar.Tab[contains(@content-desc, '作品')]",
                "//android.widget.TextView[contains(@text, '作品') and @resource-id='android:id/text1']",
                // 优先级3：原有兼容XPath
                "//*[@resource-id='com.ss.android.ugc.aweme:id/feu']",
                "//*[contains(@resource-id, 'work') or contains(@resource-id, 'video')]",
                "//android.view.ViewGroup[contains(@resource-id, 'work')]",
                "//*[contains(@text, '置顶')]",
                "//*[@resource-id='com.ss.android.ugc.aweme:id/container']"
        };

        for (String xp : xps) {
            Optional<MobileElement> opt = findFirst(xp);
            if (opt.isPresent()) {
                try {
                    opt.get().click();
                    Thread.sleep(800);
                    return true;
                } catch (Exception ignored) {
                }
            }
        }
        return false;
    }

    private boolean goVideoList() {

        try {
            // 只找视频封面 UIImage
            List<MobileElement> covers = driver.findElements(
                    By.xpath("//com.lynx.tasm.ui.image.UIImage[@clickable='true']")
            );

            for (MobileElement el : covers) {
                Rectangle r = el.getRect();
                int centerY = r.getY() + r.getHeight() / 2;

                // 视频封面只会出现在这个 Y 区间（经验值，极稳）
                if (centerY > 1000 && centerY < 1700) {
                    int centerX = r.getX() + r.getWidth() / 2;

                    new TouchAction<>(driver)
                            .tap(PointOption.point(centerX, centerY))
                            .waitAction(WaitOptions.waitOptions(Duration.ofMillis(200)))
                            .perform();

                    Thread.sleep(600);
                    return true;
                }
            }

        } catch (Exception e) {
            logger.warning("goVideoList failed: " + e.getMessage());
        }

        return false;
    }

    private boolean isOnVideoDetailPage() {
        String[] xps = {
                "//*[@resource-id='com.ss.android.ugc.aweme:id/desc']",
                "//*[@resource-id='com.ss.android.ugc.aweme:id/title']",
                "//*[contains(@content-desc,'喜欢')]",
                "//*[contains(@content-desc,'评论')]"
        };
        return anyExists(xps);
    }


    // ---------------------------------------------------------
    // 单个视频数据读取：UID/描述/计数
    // ---------------------------------------------------------
    /**
     * UI层视频唯一指纹（非 aweme_id）
     * 规则：author + desc + publishTime → MD5
     */
    /**
     * UI层视频唯一指纹（非 aweme_id）
     * 规则：author + desc + publishTime → MD5
     */
    private Map<String, Object> getVideoUniqueId() {
        Map<String, Object> map = new HashMap<>();
        MobileElement authorEl = null;
        MobileElement descEl = null;
        MobileElement timeEl = null;

        try {
            authorEl = driver.findElement(
                    By.id("com.ss.android.ugc.aweme:id/title")
            );
        } catch (Exception ignored) {
        }

        try {
            descEl = driver.findElement(
                    By.id("com.ss.android.ugc.aweme:id/desc")
            );
        } catch (Exception ignored) {
        }

        try {
            timeEl = driver.findElement(
                    By.id("com.ss.android.ugc.aweme:id/bxh")
            );
        } catch (Exception ignored) {
        }

        // ---------- author ----------
        String author = normalize(safeText(authorEl));


        // ---------- desc（主来源 + 兜底） ----------
        String desc = normalize(safeText(descEl));
        if (desc.isEmpty()) {
            // 使用你原来的 XPath 兜底方案
            desc = getVideoDescription();
        }

        // ---------- publish time ----------
        String rawTime = normalize(safeText(timeEl));
        String publishTime = rawTime;


        if (!rawTime.isEmpty() && rawTime.contains("发布时间")) {
            publishTime = rawTime
                    .replace("发布时间：", "")
                    .split("IP属地")[0]
                    .trim();
        }

        publishTime = normalize(publishTime);

        // ---------- 稳态兜底 ----------
        // 三个关键字段全空，说明页面尚未稳定，不生成 hash
        if (author.isEmpty() && desc.isEmpty() && publishTime.isEmpty()) {
            return new HashMap<>();
        }


        // ---------- 组合唯一键 ----------
        String videoKey = author + "|" + desc + "|" + publishTime;
        String uid = DigestUtils.md5Hex(videoKey);
        map.put("author", author);
        map.put("rawTime", rawTime);
        map.put("uid", uid);
        map.put("desc", desc);

        return map;
    }

    private String normalize(String s) {
        if (s == null) return "";
        return s
                .replace('\u200b', ' ')
                .replaceAll("\\s+", " ")
                .trim();
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
                System.out.println("xp::===" + xp);
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

        String[] xpaths = xpathMap.getOrDefault(statType, new String[]{});
        for (String xp : xpaths) {
            List<MobileElement> eles = findAll(xp);
            for (MobileElement e : eles) {
                String text = safeText(e);
                if (text != null && text.matches(".*\\d.*"))
                    System.out.println("p::==" + xp);
                return cleanNumberString(text);
            }
        }
        return "0";
    }

    //    收藏
    public String getFavoriteCount() {
        try {
            MobileElement ele = driver.findElement(
                    By.xpath("//*[contains(@content-desc,'收藏')]")
            );
            String desc = ele.getAttribute("content-desc");

            Matcher m = Pattern.compile("收藏(\\d+)").matcher(desc);
            if (m.find()) {
                return m.group(1);
            }
        } catch (Exception ignored) {
        }

        // fallback：UI 上的数字 TextView
        try {
            MobileElement tv = driver.findElement(By.xpath("//*[@resource-id='com.ss.android.ugc.aweme:id/d8d']"));
            return cleanNumberString(tv.getText());
        } catch (Exception ignored) {
        }

        return "0";
    }

    //    分享
    public String getShareCount() {
        try {
            MobileElement ele = driver.findElement(
                    By.xpath("//*[contains(@content-desc,'分享')]")
            );
            String desc = ele.getAttribute("content-desc");

            Matcher m = Pattern.compile("分享(\\d+)").matcher(desc);
            if (m.find()) {
                return m.group(1);
            }
        } catch (Exception ignored) {
        }

        // fallback
        try {
            MobileElement tv = driver.findElement(
                    By.xpath("//*[@resource-id='com.ss.android.ugc.aweme:id/ybe']")
            );
            return cleanNumberString(tv.getText());
        } catch (Exception ignored) {
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
                } catch (Exception ignored) {
                }
            }
        }
        return false;
    }

    private void closeComments() {
        try {
            driver.navigate().back();
            Thread.sleep(400);
        } catch (Exception ignored) {
        }
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
                    } catch (Exception ignored) {
                    }
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
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    private String recordVideoToFile(int index, int durationSec, String deviceId, String douyinId) {

        int segmentLength = 30; // 每段30秒
        int totalSegments = (int) Math.ceil(durationSec / (double) segmentLength);

        String timestamp = String.valueOf(System.currentTimeMillis());

        // === 1️⃣ 构建 douyinId 目录 ===
        Path douyinDir = Paths.get(videoOutputFolder, douyinId);

        try {
            if (!Files.exists(douyinDir)) {
                Files.createDirectories(douyinDir);
                logger.info("创建视频目录: " + douyinDir);
            }

            // 最终合并文件
            String finalFilename = douyinDir.resolve(
                    String.format("video_%03d_%s.mp4", index + 1, timestamp)
            ).toString();

            List<String> segmentFiles = new ArrayList<>();

            AndroidDriver<?> androidDriver = (AndroidDriver<?>) driver;

            for (int part = 1; part <= totalSegments; part++) {

                int startTime = (part - 1) * segmentLength;
                int thisDuration = Math.min(segmentLength, durationSec - startTime);
                if (thisDuration <= 0) break;

                String partFile = douyinDir.resolve(
                        String.format("video_%03d_%s_part%02d.mp4",
                                index + 1, timestamp, part)
                ).toString();

                logger.info("开始录制分段 " + part + "/" + totalSegments +
                        " → " + thisDuration + " 秒 → " + partFile);

                androidDriver.startRecordingScreen();

                Thread.sleep((thisDuration + 1) * 1000L);

                String base64 = androidDriver.stopRecordingScreen();
                Files.write(Paths.get(partFile), Base64.getDecoder().decode(base64));

                segmentFiles.add(partFile);
            }

            // 只有一段，直接返回
            if (segmentFiles.size() == 1) {
                return segmentFiles.get(0);
            }

            // 多段合并
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
    public List<Map<String, Object>> recordAllVideosAndComments(String deviceId, String accountName, String tags) {

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
                    } catch (NumberFormatException ignored) {
                        logger.warning("无法解析作品数量");
                    }
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

        if (!enterVideoList()) {
            logger.warning("无法进入作品区");
            return allVideoData;
        }
        if (tags.contains("前六条")){
            maxVideos=7;
        }

        while (index < maxVideos) {
            try {
                // 确保最新 PageSource
                driver.getPageSource();
                logger.info("处理第 " + (index + 1) + " 个视频");

                MobileElement descEl = null;
                try {

                    descEl = driver.findElement(
                            By.id("com.ss.android.ugc.aweme:id/desc")
                    );
                } catch (Exception ignored) {
                }

                // ---------- desc（主来源 + 兜底） ----------
                String desc = normalize(safeText(descEl));
                if (desc.contains("广告")) {
                    logger.info("取到视频为 广告 ，跳过");
                    if (!swipeToNextVideo()) break;
                    continue;
                }


                Map<String, Object> uidMap = getVideoUniqueId();
                String uid = (String) uidMap.get("uid");
                String author = (String) uidMap.get("author");
                String rawTime = (String) uidMap.get("rawTime");
                String descCon = (String) uidMap.get("desc");

// UID 为空，直接跳过（非常重要）
                if (uid == null || uid.isEmpty()) {
                    logger.info("未获取到视频 UID，跳过");
                    if (!swipeToNextVideo()) break;
                    index++;
                    continue;
                }
// 内存去重
                if (processed.contains(uid)) {
                    logger.info("视频已在本轮处理过，跳过");
                    if (!swipeToNextVideo()) break;
                    index++;
                    continue;
                }
// 数据库去重
                if (videoService.selectVideoByUId(uid) != null) {
                    logger.info("视频已存在数据库，跳过");
                    processed.add(uid); // 可选：避免后面再次查询 DB
                    if (!swipeToNextVideo()) break;
                    index++;
                    continue;
                }
// 标记已处理
                processed.add(uid);

// ↓↓↓ 这里才是真正的“新视频处理逻辑” ↓↓↓


//                ===========================================

                // ◆ 保存视频信息
                Map<String, Object> video = new LinkedHashMap<>();
                video.put("video_index", index + 1);
                video.put("uid", uid);
                video.put("author", author);
                video.put("rawTime", rawTime);
                video.put("descCon", descCon);

                video.put("timestamp", LocalDateTime.now().toString());
                video.put("description", getVideoDescription());
                video.put("likes_count", getVideoStat("赞"));
                video.put("comments_count", getVideoStat("评论"));
                video.put("share_count", getShareCount());
                video.put("collect_count", getFavoriteCount());


                if (tags.contains("视频")) {
                    //                 ◆ 线程版解析视频时长（增加重试，确保获取到）
                    int duration = 0;
                    int maxRetry = 5;
                    for (int attempt = 1; attempt <= maxRetry; attempt++) {
                        duration = extractVideoDuration(deviceId);
                        Thread.sleep(1000);
                        if (duration > 0) break;
                        logger.info("⚠️ 视频时长获取失败，重试 " + attempt + "/" + maxRetry);
                        Thread.sleep(500); // 等待0.5秒再试
                    }
                    if (duration <= 0) duration = 30; // 防止解析失败时，给默认50秒
                    if (duration > 600) duration = 600; // 防止解析失败时，给默认50秒
                    logger.info("🎬 解析到视频时长: " + duration + " 秒");

//                // ◆ 录制视频
                    String videoPath = recordVideoToFile(index, duration, deviceId, accountName);
                    video.put("video_path", videoPath);
                } else {
                    video.put("video_path", "");
                }

                // ◆ 截图
                String screenshotPath = saveScreenshot(index);
                video.put("screenshot_path", screenshotPath);

                if (tags.contains("评论")) {

                    if (StringUtils.isNotEmpty((String) video.get("comments_count"))
                            && !video.get("comments_count").equals("0")) {
                        // 指定保存评论的目录
                        String videosDir = "F:\\douyin_output\\videosDir";

                        // 初始化抓取器
                        VideoCommentsFetcher fetcher = new VideoCommentsFetcher(driver, douyinTaskService,
                                accountContentService,
                                addressAccountContentService,
                                accountService);
// 抓取当前视频的全部评论（包含 totalComments + comments + json_path）
                        Map<String, Object> commentResult = fetcher.fetchAllComments("1", accountName, uid, deviceId, index, videosDir, (String) video.get("comments_count"));
// 取出评论数组
                        List<Map<String, Object>> comments = (List<Map<String, Object>>) commentResult.get("comments");
// 写入视频数据
                        video.put("comments", comments);
                        video.put("json_path", commentResult.get("json_path"));       // ✔ 正确：保存路径字符串
                        video.put("totalComments", commentResult.get("totalComments")); // ✔ 正确：真实总评论数

                        Thread.sleep(1000); // 等待0.5秒确保下一个视频加载完成
                    }

                } else {
                    List<Map<String, Object>> comments = new ArrayList<>();
                    video.put("comments", comments);
                    video.put("json_path", "");       // ✔ 正确：保存路径字符串
                    video.put("totalComments", ""); // ✔ 正确：真实总评论数

                }
                allVideoData.add(video);

                saveSingleVideo(video, deviceId, accountName);


                logger.info("第 " + (index + 1) + " 个视频处理完成");

                index++;

                // ◆ 滑到下一个视频，确保 UI 渲染完成
                if (!swipeToNextVideo()) break;
                Thread.sleep(500); // 等待0.5秒确保下一个视频加载完成

            } catch (Exception e) {
                logger.warning("处理视频异常: " + e);
                index++;
            }
        }

        // 核心：处理完视频后检查是否需要重启
        checkAndRestartAppium();

        logger.info("总共处理视频数量: " + allVideoData.size());
        return allVideoData;
    }


    private void checkAndRestartAppium() {
        try {
            logger.info("=====================================");
            logger.info("🎯开始重启Appium...");
            logger.info("=====================================");

            // 1. 关闭旧Driver（释放资源）
            if (driver != null) {
                try {
                    driver.quit();
                    logger.info("✅ 已关闭旧的Appium Driver");
                } catch (Exception e) {
                    logger.warning("⚠️ 关闭旧Driver异常（忽略）: " + e.getMessage());
                }
                Thread.sleep(2000); // 等待2秒确保关闭完成
            }

            // 2. 调用Appium重启工具类
            AppiumRestartUtil.restartAppium();
            logger.info("✅ Appium服务重启完成");
            Thread.sleep(5000); // 等待5秒让Appium完全启动

            logger.info("=====================================");
            logger.info("🎉 Appium重启完成，继续处理视频");
            logger.info("=====================================");

        } catch (Exception e) {
            logger.severe("❌ 重启Appium失败！" + e.getMessage());
            throw new RuntimeException("Appium重启失败，终止视频采集", e);
        }
    }


    public void saveSingleVideo(Map<String, Object> map, String devId, String douyinId) {

        Video video = new Video();

        video.setVideoIndex(map.get("video_index") == null ? null :
                Long.valueOf(map.get("video_index").toString()));

//      录入视频uid
        video.setUid(map.get("uid") == null ? null :
                map.get("uid").toString());

        video.setDevId(devId);
        video.setDouyinId(douyinId);
        video.setCreateTime(new Date());

        // ==========================
        // comments → JSON 字符串存储
        // ==========================
        List<Map<String, Object>> comments = (List<Map<String, Object>>) map.get("comments");

        String commentsJson = (comments != null)
                ? JSONObject.toJSONString(comments)
                : "[]";

        video.setContent(commentsJson);


        // ==========================
        // 其他字段保持不变
        // ==========================
        video.setVideoPath(toWebPath((String) map.get("video_path")));
        video.setImagePath(toWebPath((String) map.get("screenshot_path")));

        Map<String, Object> json = new LinkedHashMap<>();
        json.put("uid", map.get("uid"));
        json.put("likes", map.get("likes_count"));
        json.put("comments", map.get("comments_count"));
        json.put("shares", map.get("share_count"));
        json.put("collects", map.get("collect_count"));
        json.put("author", map.get("author"));
        json.put("rawTime", map.get("rawTime"));
        json.put("descCon", map.get("descCon"));


        video.setJsonPath(toWebPath((String) map.get("json_path")));
        video.setJsonString(JSONObject.toJSONString(json));

        videoService.insertVideo(video);
    }


    public void saveSingleVideoByAddress(Map<String, Object> map, String devId, String douyinId, String address) {

        AddressVideo video = new AddressVideo();

        video.setVideoIndex(map.get("video_index") == null ? null :
                Long.valueOf(map.get("video_index").toString()));

        video.setUid(map.get("uid") == null ? null : map.get("uid").toString());
        video.setDevId(devId);
        video.setAddress(address);
        video.setDouyinId(douyinId);
        video.setCreateTime(new Date());

        // 评论 JSON
        List<Map<String, Object>> comments = (List<Map<String, Object>>) map.get("comments");
        video.setContent(comments != null ? JSONObject.toJSONString(comments) : "[]");

        video.setVideoPath(toWebPath((String) map.get("video_path")));
        video.setImagePath(toWebPath((String) map.get("screenshot_path")));
        video.setJsonPath(toWebPath((String) map.get("json_path")));

        // 视频基础 JSON
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("uid", map.get("uid"));
        json.put("likes", map.get("likes_count"));
        json.put("comments", map.get("comments_count"));
        json.put("shares", map.get("share_count"));
        json.put("collects", map.get("collect_count"));
        json.put("author", map.get("author"));
        json.put("rawTime", map.get("rawTime"));
        json.put("descCon", map.get("descCon"));
        video.setJsonString(JSONObject.toJSONString(json));

        // =========================
        // 补账号粉丝数据（安全写法）
        // =========================
        try {
            Account account = accountService.selectByDouyinId(douyinId);
            if (account != null && account.getJsonString() != null) {

                Map<String, Object> accMap = JSON.parseObject(account.getJsonString());

                long followCount = parseLong(accMap.get("follow_count"));
                long fansCount = parseLong(accMap.get("fans_count"));
                long likesCount = parseLong(map.get("likes_count")); // 视频点赞数在原 map

                video.setFollowCount(followCount);
                video.setFansCount(fansCount);
                video.setLikesCount(likesCount);
            } else {
                video.setFollowCount(0l);
                video.setFansCount(0l);
                video.setLikesCount(0l);
            }
        } catch (Exception ignored) {
        }

        // 入库
        addressVideoService.insertAddressVideo(video);
    }

    private long parseLong(Object value) {
        if (value == null) return 0L;

        String str = value.toString().trim();
        if (str.isEmpty()) return 0L;

        try {
            // 纯数字
            if (str.matches("^\\d+$")) {
                return Long.parseLong(str);
            }

            // 万
            if (str.contains("万")) {
                str = str.replace("万", "");
                double num = Double.parseDouble(str);
                return (long) (num * 10_000);
            }

            // 亿
            if (str.contains("亿")) {
                str = str.replace("亿", "");
                double num = Double.parseDouble(str);
                return (long) (num * 100_000_000);
            }

            // 兜底
            return (long) Double.parseDouble(str);

        } catch (Exception e) {
            return 0L;
        }
    }



    private String toWebPath(String fullPath) {
        if (fullPath == null) return null;

        // Windows 绝对路径前缀（注意双反斜杠）
        String prefix = "F:\\\\douyin_output\\\\";

        // 按前缀切分
        String[] arr = fullPath.split(prefix, 2);

        // 相对路径（若 split 失败则用原始值）
        String relative = arr.length > 1 ? arr[1] : fullPath;

        // 替换 \ 为 /
        relative = relative.replace("\\", "/");

        // 拼接 RuoYi 可访问路径
        return Constants.RESOURCE_PREFIX + "/" + relative;
    }

    // ---------------------------------------------------------
    // ⭐ 主流程：录制所有视频 + 获取评论 + 截图 + 保存数据
    // ---------------------------------------------------------
    public List<Map<String, Object>> addressVideosAndComments(String count, String deviceId, String address, String tags) {

        logger.info("开始录制所有视频并获取评论");

        List<Map<String, Object>> allVideoData = new ArrayList<>();
        Set<String> processed = new HashSet<>();
        boolean comprehensiveVideo = tags != null && tags.contains("综合视频");

        int index = 0;
        int maxVideos = 0;

        if (!count.isEmpty()) {
            maxVideos = Integer.parseInt(count);
            logger.info("解析到作品数量: " + maxVideos);
        }


        if (maxVideos <= 0) {
            logger.warning("无法解析作品数量，使用默认 100");
            maxVideos = 100;
        }

        logger.info("总共需要处理的视频数量: " + maxVideos);


//        点击进入第一条视频

        if (!isOnVideoDetailPage() && !goVideoList()) {
            logger.warning("无法进入作品区");
            return allVideoData;
        }
        String uidByCopy = null;
        int sameUidCount = 0;

        while (index < maxVideos) {
            try {
                // 确保最新 PageSource
                driver.getPageSource();
                logger.info("处理第 " + (index + 1) + " 个视频");

                MobileElement descEl = null;
                try {

                    descEl = driver.findElement(
                            By.id("com.ss.android.ugc.aweme:id/desc")
                    );
                } catch (Exception ignored) {
                }

                // ---------- desc（主来源 + 兜底） ----------
                String desc = normalize(safeText(descEl));
                if (desc.contains("广告")) {
                    logger.info("取到视频为 广告 ，跳过");
                    if (!swipeToNextVideo()) break;
                    continue;
                }

                Map<String, Object> uidMap = getVideoUniqueId();
                String uid = (String) uidMap.get("uid");
                String author = (String) uidMap.get("author");
                String rawTime = (String) uidMap.get("rawTime");
                String descCon = (String) uidMap.get("desc");


// UID 为空，直接跳过（非常重要）
                if (uid == null || uid.isEmpty()) {
                    logger.info("未获取到视频 UID，跳过");
                    if (!swipeToNextVideo()) break;
                    index++;
                    continue;
                }

                // ✅ UI 是否变化（必须无条件执行）
                if (uid.equals(uidByCopy)) {
                    sameUidCount++;
                } else {
                    sameUidCount = 0;
                    uidByCopy = uid;
                }

                if (sameUidCount >= 10) {
                    logger.warning("连续 10 次 UID 未变化，判定到底部或卡死");
                    break;
                }

// 内存去重
                if (processed.contains(uid)) {
                    logger.info("视频已在本轮处理过，跳过");
                    if (!swipeToNextVideo()) break;
                    index++;
                    continue;
                }
// 数据库去重
                boolean existsInDb = comprehensiveVideo
                        ? videoService.selectVideoByUId(uid) != null
                        : addressVideoService.selectAddressVideoByUid(uid) != null;
                if (existsInDb) {
                    logger.info("视频已存在数据库，跳过");
                    processed.add(uid); // 可选：避免后面再次查询 DB
                    if (!swipeToNextVideo()) break;
                    index++;
                    continue;
                }

// 标记已处理
                processed.add(uid);

// ↓↓↓ 这里才是真正的“新视频处理逻辑” ↓↓↓    进入人员主页 进行人员信息获取


                String douyinId = "";
                if (tags.contains("基本信息")) {
//                    点击进入主页
                    // 1️⃣ 点击进入作者主页
                    if (!enterAuthorProfile()) {
                        logger.warning("进入作者主页失败");
                        return allVideoData;
                    }

                    // 2️⃣ 等待主页加载完成（稳态）
                    if (!waitUntilProfilePage()) {
                        logger.warning("不在个人主页");
                        return allVideoData;
                    }

                    // 必须进入主页 & 作品区
                    if (!isOnProfilePage()) {
                        logger.warning("不在个人主页");
                        return allVideoData;
                    }


                    AccountInfoFetcher fetcher1 =
                            new AccountInfoFetcher((AndroidDriver<MobileElement>) driver,
                                    "F:/douyin_output"
                                    , accountContentService,
                                    addressAccountContentService,
                                    accountService,douyinTaskService);
                    Map<String, Object> result = fetcher1.getAccountBasicInfo("2","0",deviceId,douyinId);
                    douyinId = (String) result.get("id");
                    douyinTaskService.storeAccountAsync(deviceId, douyinId, result,tags);
                    driver.navigate().back();
                    Thread.sleep(1000);
                }

//                ===========================================

                // ◆ 保存视频信息
                Map<String, Object> video = new LinkedHashMap<>();
                video.put("video_index", index + 1);
                video.put("uid", uid);
                video.put("author", author);
                video.put("rawTime", rawTime);
                video.put("descCon", descCon);
                video.put("timestamp", LocalDateTime.now().toString());
                video.put("description", getVideoDescription());
                video.put("likes_count", getVideoStat("赞"));
                video.put("comments_count", getVideoStat("评论"));
                video.put("share_count", getShareCount());
                video.put("collect_count", getFavoriteCount());

                if (tags.contains("视频")) {
                    //                 ◆ 线程版解析视频时长（增加重试，确保获取到）
                    int duration = 0;
                    int maxRetry = 5;
                    for (int attempt = 1; attempt <= maxRetry; attempt++) {
                        duration = extractVideoDuration(deviceId);
                        Thread.sleep(1000);
                        if (duration > 0) break;
                        logger.info("⚠️ 视频时长获取失败，重试 " + attempt + "/" + maxRetry);
                        Thread.sleep(500); // 等待0.5秒再试
                    }
                    if (duration <= 0) duration = 30; // 防止解析失败时，给默认50秒
                    if (duration > 600) duration = 600; // 防止解析失败时，给默认50秒
                    logger.info("🎬 解析到视频时长: " + duration + " 秒");

//                // ◆ 录制视频
                    String videoPath = recordVideoToFile(index, duration, deviceId,
                            comprehensiveVideo ? address : douyinId);
                    video.put("video_path", videoPath);
                } else {
                    video.put("video_path", "");
                }

                // ◆ 截图
                String screenshotPath = saveScreenshot(index);
                video.put("screenshot_path", screenshotPath);

                if (tags.contains("评论")) {

                    if (StringUtils.isNotEmpty((String) video.get("comments_count"))
                            && !video.get("comments_count").equals("0")) {
                        // 指定保存评论的目录
                        String videosDir = "F:\\douyin_output\\videosDir";

                        // 初始化抓取器
                        VideoCommentsFetcher fetcher = new VideoCommentsFetcher(driver, douyinTaskService,
                                accountContentService,
                                addressAccountContentService,
                                accountService);
// 抓取当前视频的全部评论（包含 totalComments + comments + json_path）
                        Map<String, Object> commentResult = fetcher.fetchAllComments(comprehensiveVideo ? "1" : "2", address, uid, deviceId, index, videosDir, (String) video.get("comments_count"));
// 取出评论数组
                        List<Map<String, Object>> comments = (List<Map<String, Object>>) commentResult.get("comments");
// 写入视频数据
                        video.put("comments", comments);
                        video.put("json_path", commentResult.get("json_path"));       // ✔ 正确：保存路径字符串
                        video.put("totalComments", commentResult.get("totalComments")); // ✔ 正确：真实总评论数

                        Thread.sleep(1000); // 等待0.5秒确保下一个视频加载完成
                    }

                } else {
                    List<Map<String, Object>> comments = new ArrayList<>();
                    video.put("comments", comments);
                    video.put("json_path", "");       // ✔ 正确：保存路径字符串
                    video.put("totalComments", ""); // ✔ 正确：真实总评论数

                }
                allVideoData.add(video);

                if (comprehensiveVideo) {
                    saveSingleVideo(video, deviceId, address);
                } else {
                    saveSingleVideoByAddress(video, deviceId, douyinId, address);
                }


                logger.info("第 " + (index + 1) + " 个视频处理完成");

                index++;

                // ◆ 滑到下一个视频，确保 UI 渲染完成
                if (!swipeToNextVideo()) break;
                Thread.sleep(500); // 等待0.5秒确保下一个视频加载完成

            } catch (Exception e) {
                logger.warning("处理视频异常: " + e);
                index++;
            }
        }

        // 核心：处理完视频后检查是否需要重启
        checkAndRestartAppium();

        logger.info("总共处理视频数量: " + allVideoData.size());
        return allVideoData;
    }


}
