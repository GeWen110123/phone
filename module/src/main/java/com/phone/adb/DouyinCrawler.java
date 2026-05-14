package com.phone.adb;

import com.phone.module.service.DouyinTaskService;
import com.phone.module.service.IAccountContentService;
import com.phone.module.service.IAccountService;
import com.phone.module.service.IAddressAccountContentService;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

public class DouyinCrawler {

    private static final Logger logger = Logger.getLogger(DouyinCrawler.class.getName());
    private AndroidDriver<MobileElement> driver;

    private final int screenWidth;
    private final int screenHeight;

    public final IAccountContentService accountContentService;
    public final IAddressAccountContentService addressAccountContentService;
    public final IAccountService accountService;

    private final DouyinTaskService douyinTaskService;
    public DouyinCrawler(AndroidDriver<MobileElement> driver,
                         IAccountContentService accountContentService,
                         IAddressAccountContentService addressAccountContentService,
                         IAccountService accountService,DouyinTaskService douyinTaskService) {
        this.driver = driver;
        this.screenWidth = driver.manage().window().getSize().width;
        this.screenHeight = driver.manage().window().getSize().height;
        this.accountContentService = accountContentService;
        this.addressAccountContentService = addressAccountContentService;
        this.accountService = accountService;
        this.douyinTaskService = douyinTaskService;
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
        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }
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

    private boolean clickFirstUser() {
        clickByCoordinates(250, 520);
        sleep(3000);
        return true;
    }

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
                    Map<String, Object> info = fetchAccountInfo("1","1","","");
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
    // 获取账号信息
    // ===========================
    public Map<String, Object> fetchAccountInfo(String type ,String status,String devId,String douyinId) {
        AccountInfoFetcher fetcher = new AccountInfoFetcher(driver, "F:/douyin_output"
                , accountContentService,
                addressAccountContentService,
                accountService,douyinTaskService);
        Map<String, Object> info = fetcher.getAccountBasicInfo(type,status,devId,douyinId);

        System.out.println("========== 抖音主页解析结果 ==========");
        info.forEach((k, v) -> System.out.println(k + " : " + v));
        System.out.println("===================================");

        return info;
    }

//    public void quit() {
//        if (driver != null) driver.quit();
//    }

//    // ===========================
//    // 静态入口：爬取账号信息
//    // ===========================
//    public static Map<String, Object> crawlAccount(String devId, String accountName) {
//        Map<String, Object> info = new HashMap<>();
//        AndroidDriver<MobileElement> driver = null;
//
//        try {
//            DesiredCapabilities capabilities = new DesiredCapabilities();
//            capabilities.setCapability("platformName", "Android");
//            capabilities.setCapability("deviceName", "Android Device");
//            capabilities.setCapability("udid", devId);
//            capabilities.setCapability("appPackage", "com.ss.android.ugc.aweme");
//            capabilities.setCapability("appActivity", ".main.MainActivity");
//            capabilities.setCapability("noReset", true);
//
//            driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
//
//            DouyinCrawler crawler = new DouyinCrawler(driver,
//
//                    accountContentService,
//                    addressAccountContentService,
//                    accountService);
//
//            if (!crawler.startDouyin()) return info;
//            if (!crawler.searchAndEnterAccount(accountName)) return info;
//
//            info = crawler.fetchAccountInfo();
//            return info;
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            return info;
//        } finally {
//            if (driver != null) driver.quit();
//        }
//    }

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
