package com.phone.adb;

import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DouyinCrawler {

    private static final Logger logger = Logger.getLogger(DouyinCrawler.class.getName());
    private final AndroidDriver driver;
    private final int screenWidth;
    private final int screenHeight;

    public DouyinCrawler(AndroidDriver driver) {
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

        return verifyProfilePage();
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
        // 兜底坐标点击
        clickByCoordinates((int) (screenWidth * 0.9), (int) (screenHeight * 0.05));
        sleep(2000);
        return true;
    }

    private boolean inputSearchText(String accountName) {
        String[] inputXpaths = {"//*[contains(@resource-id, 'search_edit_text')]", "//android.widget.EditText"};
        for (String xp : inputXpaths) {
            try {
                List<WebElement> elements = driver.findElements(By.xpath(xp));
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

    private boolean clickFirstUser() {
        // 可替换成精准 XPath
        clickByCoordinates(250, 520);
        sleep(3000);
        return true;
    }

    private boolean verifyProfilePage() {
        // 候选 XPath 列表（无中文，全为 resource-id 或 UI 结构）
        String[] profileXpaths = new String[]{
                "//*[contains(@resource-id, 'nickname')]",                    // 昵称
                "//android.widget.TextView[contains(@resource-id, 'nickname')]",
                "//*[contains(@resource-id, 'follow')]",                      // 关注
                "//*[contains(@resource-id, 'fans')]",                        // 粉丝
                "//*[contains(@resource-id, 'like')]",                        // 获赞
                "//android.view.ViewGroup[contains(@resource-id, 'user_profile')]", // 用户主页结构
                "//*[contains(@resource-id, 'title')]",                       // 标题区域（一般是昵称）
                "//android.widget.ImageView[contains(@resource-id, 'avatar')]", // 头像
                "//android.view.ViewGroup[contains(@resource-id, 'header')]"   // 主页头部模块
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
            List<WebElement> elements = driver.findElements(By.xpath(xpath));
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
    // 调用 AccountInfoFetcher 获取账号信息
    // ===========================
    public Map<String, Object> fetchAccountInfo() {
        AccountInfoFetcher fetcher = new AccountInfoFetcher(driver, "D:/douyin_output");
//        return fetcher.getAccountBasicInfo();

        // ⭐ 自动执行后续：获取账号详细信息
        Map<String, Object> info = fetcher.getAccountBasicInfo();

        System.out.println("========== 抖音主页解析结果 ==========");
        info.forEach((k, v) -> System.out.println(k + " : " + v));
        System.out.println("===================================");


        return info;

    }

    public void quit() {
        if (driver != null) driver.quit();
    }

    /**
     * 启动抖音并爬取指定账号信息
     *
     * @param devId       设备ID（UDID）
     * @param accountName 抖音账号名称
     * @return 是否成功
     */
    public static Map<String, Object> crawlAccount(String devId, String accountName) {
        Map<String, Object> info = new HashMap<>();
        AndroidDriver driver = null;
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("udid", devId);
            capabilities.setCapability("noReset", true);
            capabilities.setCapability("appPackage", "com.ss.android.ugc.aweme"); // 抖音包名
            capabilities.setCapability("appActivity", ".main.MainActivity"); // 启动Activity

            driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
            DouyinCrawler crawler = new DouyinCrawler(driver);

            if (!crawler.startDouyin())
                return info;

            if (!crawler.searchAndEnterAccount(accountName)) return info;

            crawler.fetchAccountInfo();
            return info;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return info;
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    // 如果需要保留 main 调试入口
    public static void main(String[] args) {
        String devId = "ec86e946";
        String accountName = "Becarefulleea";

        Map<String,Object> info = crawlAccount(devId, accountName);
        System.out.println(info);
    }
}
