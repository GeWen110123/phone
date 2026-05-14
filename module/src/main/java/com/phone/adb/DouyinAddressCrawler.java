package com.phone.adb;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.logging.Logger;

public class DouyinAddressCrawler {

    private static final Logger logger = Logger.getLogger(DouyinAddressCrawler.class.getName());
    private AndroidDriver<MobileElement> driver;

    private final int screenWidth;
    private final int screenHeight;

    public DouyinAddressCrawler(AndroidDriver<MobileElement> driver) {
        this.driver = driver;
        this.screenWidth = driver.manage().window().getSize().width;
        this.screenHeight = driver.manage().window().getSize().height;
    }

    // ===========================
    // 启动抖音
    // ===========================
    public boolean startDouyin() {
        try {
            logger.info("启动抖音应用...");
            Activity activity = new Activity("com.ss.android.ugc.aweme",
                    "com.ss.android.ugc.aweme.main.MainActivity");
            driver.startActivity(activity);
            Thread.sleep(5000);

            boolean success = "com.ss.android.ugc.aweme".equals(driver.getCurrentPackage());
            logger.info(success ? "抖音启动成功" : "抖音启动失败");
            return success;
        } catch (Exception e) {
            logger.severe("启动抖音失败: " + e.getMessage());
            return false;
        }
    }

    // ===========================
    // 搜索账号并进入主页
    // ===========================
    public boolean searchAndEnterAccount(String accountName) {
        logger.info("开始搜索账号: " + accountName);

        if (!isDeviceConnected()) return false;
        if (!clickSearchButton()) return false;
        if (!inputSearchText(accountName)) return false;
        if (!clickSearchSubmit()) return false;
        if (!selectUserTab()) return false;
        if (!clickFirstUser()) return false;

//        return verifyProfilePage();
        return true;
    }

    // ===========================
    // 搜索账号并进入地址视频分类
    // ===========================
    public boolean searchAndEnterAddress(String accountName) {
        logger.info("开始搜索地址: " + accountName);

        if (!isDeviceConnected()) return false;
        if (!clickSearchButton()) return false;
        if (!inputSearchText(accountName)) return false;
        if (!clickSearchSubmit()) return false;
        sleep(2000);
        if (!selectZongHeTab()) return false;
        if (!clickFirstAddress()) return false;


        int maxSwipe = 40;          // ✅ 最大滑动次数（你可调）
        int swipeCount = 0;

        while (swipeCount <= maxSwipe) {

            logger.info("第 " + (swipeCount + 1) + " 次尝试匹配地址");

            if (findAndClickAddress(accountName)) {
                logger.info("成功进入地址视频页: " + accountName);
                swipeUp();
                sleep(800);
                return true;
            }

            // ===== 未匹配，向下滑动加载更多 =====
            logger.info("未匹配到地址，向下滑动加载更多");
            swipeUp();
            sleep(800);

            swipeCount++;
        }

        logger.warning("滑动达到上限，仍未找到地址: " + accountName);
        return false;
    }

    public boolean swipeUp() {
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

    /**
     * 根据 "台湾省|台北101大楼" 匹配地址并点击【点击字段本身】
     */
    public boolean findAndClickAddress(String accountName) {

        if (accountName == null || accountName.trim().isEmpty()) {
            logger.info("地址为空");
            return false;
        }

        String region = null;
        String place;

        // ⭐ 情况1：包含 |
        if (accountName.contains("|")) {
            String[] parts = accountName.split("\\|");

            if (parts.length < 2) {
                logger.info("地址格式异常: " + accountName);
                return false;
            }

            region = parts[0].trim();
            place  = parts[1].trim();

            logger.info("匹配地址(区域+地点) -> 区域: " + region + ", 地点: " + place);

        }
        // ⭐ 情况2：只有地点
        else {
            place = accountName.trim();
            logger.info("匹配地址(仅地点) -> " + place);
        }
        List<MobileElement> parents;

        try {
            String parentXpath;

            // ✅ 有区域时 → 双条件匹配（最精准）
            if (region != null) {
                parentXpath =
                        "//android.widget.LinearLayout" +
                                "[.//android.widget.TextView[@text='" + region + "']" +
                                " and .//android.widget.TextView[@text='" + place + "']]";
            }
            // ✅ 没区域 → 只匹配 place
            else {
                parentXpath =
                        "//android.widget.LinearLayout" +
                                "[.//android.widget.TextView[contains(@text,'" + place + "')]]";
            }

            parents = driver.findElements(By.xpath(parentXpath));

        } catch (Exception e) {
            logger.warning("地址父节点查找异常: " + e.getMessage());
            return false;
        }

        if (parents == null || parents.isEmpty()) {
            logger.info("页面中未找到匹配地址");
            return false;
        }

        MobileElement parent = parents.get(0);

        try {
            // ⭐ 永远点击 place（真正可点的是地点）
            MobileElement placeTv = parent.findElement(
                    By.xpath(".//android.widget.TextView[@text='" + place + "']")
            );

            placeTv.click();
            logger.info("已点击地址字段: " + place);
            sleep(800);
            return true;

        } catch (Exception e) {
            logger.warning("点击地址字段失败: " + e.getMessage());
            return false;
        }
    }


    // ===========================
    // 核心 UI 操作封装
    // ===========================
    private boolean isDeviceConnected() {
        try {
            driver.getPageSource();
            return true;
        } catch (Exception e) {
            logger.severe("设备未连接: " + e.getMessage());
            return false;
        }
    }

    private void clickByCoordinates(int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(tap));
    }

    private boolean clickSearchButton() {
        String[] searchButtonXpaths = {
                "//android.widget.Button[@content-desc='搜索' and @clickable='true']",
                "//android.widget.ImageView[contains(@resource-id, 'search')]"
        };
        for (String xp : searchButtonXpaths) {
            if (clickIfExists(xp)) return true;
        }
        clickByCoordinates((int) (screenWidth * 0.9), (int) (screenHeight * 0.05));
        sleep(2000);
        return true;
    }

    private boolean inputSearchText(String accountName) {
        String[] inputXpaths = {
                "//*[contains(@resource-id, 'search_edit_text')]",
                "//android.widget.EditText"
        };
        for (String xp : inputXpaths) {
            try {
                List<MobileElement> elements = driver.findElementsByXPath(xp);
                if (!elements.isEmpty()) {
                    elements.get(0).sendKeys(accountName);
                    return true;
                }
            } catch (Exception ignored) {
            }
        }
        logger.severe("搜索输入框未找到");
        return false;
    }

    private boolean clickSearchSubmit() {
        try {
            driver.findElement(By.id("com.ss.android.ugc.aweme:id/3i9")).click();
            return true;
        } catch (Exception e) {
            try {
                driver.findElement(By.xpath("//android.widget.TextView[@content-desc='搜索']")).click();
                return true;
            } catch (Exception ex) {
                logger.warning("搜索提交失败: " + ex.getMessage());
                return false;
            }
        }
    }

    private boolean selectUserTab() {
        String[] tabXpaths = {
                "//*[contains(@resource-id, 'tab_user')]",
                "//*[contains(@text,'用户')]",
                "//android.widget.TextView[@clickable='true' and contains(@text,'用户')]"
        };
        for (String xp : tabXpaths) {
            if (clickIfExists(xp)) {
                sleep(2000);
                return true;
            }
        }
        logger.warning("用户分类Tab未找到");
        return false;
    }

    private boolean selectZongHeTab() {
        String[] tabXpaths = {

                "//*[contains(@text,'综合')]",
                "//android.widget.TextView[@clickable='true' and contains(@text,'综合')]"
        };
        for (String xp : tabXpaths) {
            if (clickIfExists(xp)) {
                sleep(2000);
                return true;
            }
        }
        logger.warning("综合分类Tab未找到");
        return false;
    }

    private boolean clickFirstUser() {
        clickByCoordinates(250, 520);
        sleep(3000);
        return true;
    }

    /**
     * 点击搜索结果里的第一个【视频】
     * 自动跳过小程序 / 卡片
     */
//    private boolean clickFirstAddress() {
//        try {
//
//            System.out.println(driver.getPageSource());
//            // 查找当前页面所有“可播放的视频层”
//            List<MobileElement> videoPlays = driver.findElements(
//                    By.xpath("//android.view.View[@resource-id='com.ss.android.ugc.aweme:id/p=1']")
//            );
//
//            if (videoPlays == null || videoPlays.isEmpty()) {
//                logger.warning("当前页面未找到可播放视频（p=1）");
//                return false;
//            }
//
//            // 点击第一个视频
//            MobileElement firstVideo = videoPlays.get(0);
//            firstVideo.click();
//
//            logger.info("已点击第一个视频");
//            sleep(3000);
//
//            return true;
//
//        } catch (Exception e) {
//            logger.severe("点击第一个视频失败: " + e.getMessage());
//            return false;
//        }
//    }

    /**
     * 点击搜索结果里的第一个【视频】
     * 自动跳过小程序 / 卡片
     */
    private boolean clickFirstAddress() {
        try {

            List<MobileElement> playViews = driver.findElements(
                    By.xpath("//android.view.View[@resource-id='com.ss.android.ugc.aweme:id/p=1']")
            );

            if (playViews == null || playViews.isEmpty()) {
                logger.warning("当前页面未找到 p=1 播放层");
                return false;
            }

            for (int i = 0; i < playViews.size(); i++) {

                try {
                    MobileElement play = playViews.get(i);
                    logger.info("尝试点击第 " + (i + 1) + " 个卡片");
                    play.click();
                    sleep(2500);

                    // 判断是否为视频
                    if (isVideoPage()) {
                        logger.info("确认是视频，已进入");
                        return true;
                    }

                    // 不是视频，返回
                    logger.info("不是视频，返回继续查找");
                    driver.navigate().back();
                    sleep(1500);

                } catch (Exception inner) {
                    logger.warning("当前卡片判断异常，跳过: " + inner.getMessage());
                    driver.navigate().back();
                    sleep(1500);
                }
            }

            logger.warning("未找到符合条件的视频");
            return false;

        } catch (Exception e) {
            logger.severe("点击视频过程失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 判断当前页面是否为【真正的视频页】
     */
    private boolean isVideoPage() {
        try {
            Dimension size = driver.manage().window().getSize();
            int screenHeight = size.getHeight();
            int screenWidth = size.getWidth();

            // 1️⃣ 查找所有“点赞按钮”（不限定 id）
            List<MobileElement> likeButtons = driver.findElements(
                    By.xpath("//*[contains(@content-desc,'喜欢')]")
            );

            if (likeButtons == null || likeButtons.isEmpty()) {
                return false;
            }

            for (MobileElement btn : likeButtons) {

                if (!btn.isDisplayed()) continue;

                Point p = btn.getLocation();

                boolean isRightSide = p.getX() > screenWidth * 0.6;
                boolean notBottomBar = p.getY() < screenHeight * 0.75;

                // ✅ 视频页点赞按钮特征
                if (isRightSide && notBottomBar) {
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }

//
//    private boolean clickFirstAddress() {
//        clickByCoordinates(250, 520);
//        sleep(3000);
//        return true;
//    }

    private boolean verifyProfilePage() {
        String[] profileXpaths = new String[]{
                "//*[contains(@resource-id, 'nickname')]",
                "//android.widget.TextView[contains(@resource-id, 'nickname')]",
                "//*[contains(@resource-id, 'follow')]",
                "//*[contains(@resource-id, 'fans')]",
                "//*[contains(@resource-id, 'like')]",
                "//android.view.ViewGroup[contains(@resource-id, 'user_profile')]",
                "//*[contains(@resource-id, 'title')]",
                "//android.widget.ImageView[contains(@resource-id, 'avatar')]",
                "//android.view.ViewGroup[contains(@resource-id, 'header')]"
        };
        for (String xp : profileXpaths) {
            try {
                if (!driver.findElements(By.xpath(xp)).isEmpty()) {
                    logger.info("成功进入主页, XPath: " + xp);
                    Map<String, Object> info = fetchAccountInfo();
                    info.forEach((k, v) -> logger.info(k + " : " + v));
                    return true;
                }
            } catch (Exception ignored) {
            }
        }
        logger.warning("未能验证进入主页");
        return false;
    }

    private boolean clickIfExists(String xpath) {
        try {
            List<MobileElement> elements = driver.findElementsByXPath(xpath);
            if (!elements.isEmpty()) {
                elements.get(0).click();
                sleep(1500);
                return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    // ===========================
    // 获取地址信息
    // ===========================
    public Map<String, Object> fetchAccountInfo() {
        AddressInfoFetcher fetcher = new AddressInfoFetcher(driver, "F:/douyin_output");
        Map<String, Object> info = fetcher.getAccountBasicInfo();

        System.out.println("========== 抖音地址解析结果 ==========");
        info.forEach((k, v) -> System.out.println(k + " : " + v));
        System.out.println("===================================");

        return info;
    }

    public void quit() {
        if (driver != null) driver.quit();
    }

    // ===========================
    // 静态入口：爬取账号信息
    // ===========================
    public static Map<String, Object> crawlAccount(String devId, String accountName) {
        Map<String, Object> info = new HashMap<>();
        AndroidDriver<MobileElement> driver = null;

        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("deviceName", "Android Device");
            capabilities.setCapability("udid", devId);
            capabilities.setCapability("appPackage", "com.ss.android.ugc.aweme");
            capabilities.setCapability("appActivity", ".main.MainActivity");
            capabilities.setCapability("noReset", true);

            driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

            DouyinAddressCrawler crawler = new DouyinAddressCrawler(driver);

            if (!crawler.startDouyin()) return info;
            if (!crawler.searchAndEnterAccount(accountName)) return info;

            info = crawler.fetchAccountInfo();
            return info;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return info;
        } finally {
            if (driver != null) driver.quit();
        }
    }

//    public static void main(String[] args) {
//        String devId = "ec86e946";
//        String accountName = "Becarefulleea";
//
//        Map<String,Object> info = crawlAccount(devId, accountName);
//        System.out.println(info);
//    }
//
//    public static void main(String[] args) {
//        String devId = "ec86e946";
//
//        // 想抓多个账号，填在这里
//        String[] accountNames = new String[]{
//                "40691348894",
//                "EQLQ",
//                "37912855169",
//                "chunbuwanyes"
//        };
//
//        AndroidDriver<MobileElement> driver = null;
//
//        try {
//            DesiredCapabilities caps = new DesiredCapabilities();
//            caps.setCapability("platformName", "Android");
//            caps.setCapability("deviceName", "Android Device");
//            caps.setCapability("udid", devId);
//            caps.setCapability("appPackage", "com.ss.android.ugc.aweme");
//            caps.setCapability("appActivity", ".main.MainActivity");
//            caps.setCapability("noReset", true);
//            caps.setCapability("automationName", "UiAutomator2"); // 必填
//
//            driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), caps);
//
//            DouyinCrawler crawler = new DouyinCrawler(driver);
//
//            if (!crawler.startDouyin()) return;
//
//            for (String accountName : accountNames) {
//
//                System.out.println("\n==============================");
//                System.out.println("开始处理账号: " + accountName);
//                System.out.println("==============================");
//
//                if (!crawler.searchAndEnterAccount(accountName)) {
//                    System.out.println("❌ 无法进入账号: " + accountName + "，跳过");
//                    continue;
//                }
//
//                AccountInfoFetcherVoid fetcher = new AccountInfoFetcherVoid(driver);
//
//                // 开始录屏
//                System.out.println("开始录屏...");
//                driver.startRecordingScreen();
//
//                List<Map<String, Object>> allVideos = fetcher.recordAllVideosAndComments(devId, accountName);
//
//                // 你需要的话也可以打印
//                // allVideos.forEach(System.out::println);
//
//                System.out.println("⭐ 账号完成: " + accountName);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (driver != null) driver.quit();
//        }
//    }


}
