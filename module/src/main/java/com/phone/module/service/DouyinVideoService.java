package com.phone.module.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phone.adb.AccountInfoFetcherVoid;
import com.phone.adb.DouyinAddressCrawler;
import com.phone.adb.DouyinCrawler;
import com.phone.adb.XPathRegistry;
import com.phone.common.utils.StringUtils;
import com.phone.module.domain.*;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Service
public class DouyinVideoService {

    private static final Logger logger = Logger.getLogger(DouyinVideoService.class.getName());

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IVideoTagsService videoTagsService;
    @Autowired
    private DouyinTaskService douyinTaskService;
    @Autowired
    private IAddressVideoService addressVideoService;
    @Autowired
    private IVideoService videoService;
    @Autowired
    private IAccountContentService accountContentService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IAddressAccountContentService addressAccountContentService;
    @Autowired
    private IRunLogService runLogService;
    /**
     * 用于 DB 写入
     */
    private final ExecutorService dbExecutor = Executors.newFixedThreadPool(3);

    /**
     * 异步入口
     */
    @Async
    public void asyncCrawlTask(String devId, String accountName, VideoTags tags,String lockKey,Boolean locked) {
        crawlWithRedisTemplateLock(devId, accountName, tags,lockKey,locked);

        Account account = new Account();
        account.setDouyinId(accountName);
        List<Account> accountList = accountService.selectAccountList(account);

        RunLog runLog = new RunLog();

        if (accountList.size()>0){
            account = accountService.selectAccountList(account).get(0);
            int count = getRealWorksCount(account.getJsonString());


            runLog.setDevId(devId);
            runLog.setDouyinId(tags.getDouyinId());

            if (tags.getTags().contains("前六条")){

                runLog.setType("人员");
                Video v = new Video();
                v.setDouyinId(accountName);
                List<Video>videoList =videoService.selectVideoList(v);
                if (videoList.size()>=count||videoList.size()>=6){
                    tags.setStatus("2");
                    runLog.setRuningDetail("完成对"+tags.getDouyinId()+"前六条数据获取");
                }else {
                    tags.setStatus("3");
                    runLog.setRuningDetail(tags.getDouyinId()+"数据获取结束，存在异常，等待再次处理");
                }


            }else if (tags.getTags().contains("地址")) {
                runLog.setType("地址");
                AddressVideo v = new AddressVideo();
                v.setAddress(accountName);
                if (addressVideoService.selectAddressVideoList(v).size()>=count){
                    runLog.setRuningDetail("完成对地址"+tags.getDouyinId()+"数据获取");
                    tags.setStatus("2");
                }else {
//                   如果地址存在问题 进行修改为未执行状态  继续执行
                    tags.setStatus("0");
                    runLog.setRuningDetail(tags.getDouyinId()+"数据获取结束，存在异常，等待再次处理");
                }
            } else {
                count =getUserWorksCount(account.getJsonString());
                runLog.setType("人员");
                Video v = new Video();
                v.setDouyinId(accountName);
                if (videoService.selectVideoList(v).size()>=count){
                    tags.setStatus("2");
                    runLog.setRuningDetail("完成对"+tags.getDouyinId()+"前六条数据获取");
                }else {
                    tags.setStatus("3");
                    runLog.setRuningDetail(tags.getDouyinId()+"数据获取结束，存在异常，等待再次处理");
                }
            }


        }else {
            if (tags.getTags().contains("地址")) {
                tags.setStatus("0");
            }else {
                tags.setStatus("3");
            }
            runLog.setType("异常");
            runLog.setRuningDetail(tags.getDouyinId()+"数据获取结束，存在异常，等待再次处理");
            runLogService.insertRunLog(runLog);

        }
        runLogService.insertRunLog(runLog);

        videoTagsService.updateVideoTags(tags);
    }

    public static int getRealWorksCount(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(
                    jsonString,
                    new TypeReference<Map<String, Object>>() {}
            );

            // ① 先拿 works_count
            int worksCount = Optional.ofNullable(map.get("works_count"))
                    .map(Object::toString)
                    .map(Integer::parseInt)
                    .orElse(0);

            // ② 再看 works 文本是不是“打卡”类型
            String worksText = Optional.ofNullable(map.get("works"))
                    .map(Object::toString)
                    .orElse("");

            if (worksText.contains("打卡")) {
                worksCount = worksCount / 10;
            }else {
                worksCount = worksCount / 5;
            }

            return worksCount;

        } catch (Exception e) {
            logger.warning("解析 works_count 失败: " + e.getMessage());
            return 0;
        }
    }
    public static int getUserWorksCount(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(
                    jsonString,
                    new TypeReference<Map<String, Object>>() {}
            );

            // ① 先拿 works_count
            int worksCount = Optional.ofNullable(map.get("works_count"))
                    .map(Object::toString)
                    .map(Integer::parseInt)
                    .orElse(0);

            return worksCount;

        } catch (Exception e) {
            logger.warning("解析 works_count 失败: " + e.getMessage());
            return 0;
        }
    }

    /**
     * 使用 RedisTemplate 实现设备排它锁
     * 执行抓取操作
     */
    private void crawlWithRedisTemplateLock(String devId,
                                            String accountName,
                                            VideoTags tags,
                                            String lockKey,
                                            boolean locked) {
        try {
            // 执行抓取
            crawlSingleAccount(devId, accountName, tags.getTags());
        } catch (Exception e) {
            logger.severe("❌ Redis 分布式锁错误：" + e.getMessage());
        } finally {
            if (Boolean.TRUE.equals(redisTemplate.hasKey(lockKey)) && Boolean.TRUE.equals(locked)) {
                redisTemplate.delete(lockKey);
                logger.info("🔓 Redis 锁已释放：" + devId);
            }
        }
    }

    /**
     * 单设备抓取（修复版：解决 socket hang up 错误）
     */
    public List<Map<String, Object>> crawlSingleAccount(String devId, String accountName, String tags) {
        AndroidDriver<MobileElement> driver = null;

        try {
            DesiredCapabilities caps = new DesiredCapabilities();

// 基础设备绑定（只写一次，不重复）
            caps.setCapability("platformName", "Android");
            caps.setCapability("deviceName", devId);
            caps.setCapability("udid", devId);

// 抖音包名与启动页（正确官方）
            caps.setCapability("appPackage", "com.ss.android.ugc.aweme");
            caps.setCapability("appActivity", "com.ss.android.ugc.aweme.splash.SplashActivity");

// 核心：不重置、保登录
            caps.setCapability("noReset", true);
            caps.setCapability("dontStopAppOnReset", true);

// 驱动引擎（唯一）
            caps.setCapability("automationName", "UiAutomator2");

// ==================== 【极速核心配置】 ====================
// 1. 端口正确配置（不搞反！PC端systemPort，设备端固定6790）
            caps.setCapability("systemPort", 8200);        // PC端口，多开设备要递增：8201/8202...
            caps.setCapability("uiautomator2ServerPort", 6790); // 设备固定端口

// 2. 协议（Appium2 + 稳定不卡顿）
            caps.setCapability("automationProtocol", "UiAutomator2");

// 3. 合理超时（不卡死、不等待）
            caps.setCapability("uiautomator2ServerLaunchTimeout", 30000);
            caps.setCapability("uiautomator2ServerInstallTimeout", 30000);
            caps.setCapability("adbExecTimeout", 10000);
            caps.setCapability("newCommandTimeout", 60);

// 4. 关闭卡顿来源：动画、弹窗、监听
            caps.setCapability("disableWindowAnimation", true);
//            caps.setCapability("ignoreUnimportantViews", true);  // 加速页面解析
            caps.setCapability("waitForIdleTimeout", 500);       // 最重要：减少等待页面静止
            caps.setCapability("waitForAppLaunch", 5);            // 不傻等

// 5. 关闭键盘、权限弹窗干扰
            caps.setCapability("unicodeKeyboard", true);
            caps.setCapability("resetKeyboard", true);
            caps.setCapability("autoGrantPermissions", true);     // 自动允许权限，不卡顿

//            caps.setCapability("dontStopAppOnReset", true);

//            caps.setCapability("ignoreUnimportantViews", true);

// ========== 驱动创建：增加重试+异常捕获 ==========
            driver = null;
            int retryCount = 0;
            while (retryCount < 3) { // 最多重试3次
                try {
                    retryCount++;
                    driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), caps);
                    logger.info("驱动创建成功！");
                    break;
                } catch (Exception e) {
                    if (retryCount >= 3) {
                        throw new RuntimeException("驱动创建失败（已重试3次）", e);
                    }
                    // 重试前清理残留进程
                    try {
                        Runtime.getRuntime().exec(String.format("adb -s %s shell am force-stop io.appium.uiautomator2.server", devId));
                        Thread.sleep(2000);
                    } catch (Exception ex) {
                    }
                }
            }
//            // 隐式等待，提升元素查找稳定性
//            driver.manage().timeouts().implicitlyWait(10, java.util.concurrent.TimeUnit.SECONDS);

            // ========== 原有业务逻辑：完全保留，无修改 ==========
            if (tags.contains("地址")) {
                DouyinAddressCrawler crawler = new DouyinAddressCrawler(driver);
                if (!crawler.startDouyin()) return null;
                if (!crawler.searchAndEnterAddress(accountName)) return null;

                AccountInfoFetcherVoid fetcher = new AccountInfoFetcherVoid(driver,
                        videoService,
                        addressVideoService,
                        douyinTaskService,
                        accountContentService,
                        addressAccountContentService,
                        accountService
                );
                driver.startRecordingScreen();

                Account account = new Account();
                account.setDouyinId(accountName);
                List<Account> lists = accountService.selectAccountList(account);
                String workesCount = "15"; // 兜底默认值，避免空值

                if (lists != null && lists.size() > 0) {
                    account = lists.get(0);
                    String json = account.getJsonString();
                    if (StringUtils.isNotBlank(json)) {
                        JSONObject obj = JSONObject.parseObject(json);
                        workesCount = obj.getString("works_count");
                    }
                } else {
                    // 基础信息查询
                    Map<String, Object> result = crawler.fetchAccountInfo();
                    douyinTaskService.storeAccountAsync(devId, accountName, result,tags);
                    workesCount = StringUtils.isNotBlank((String) result.get("works_count"))
                            ? (String) result.get("works_count")
                            : "15";
                }

                Thread.sleep(800); // 页面稳定
                // 判断并点击“地点内容数量”
                if (clickAddressContentIfPresent(driver)) {
                    logger.info("已进入地点内容列表页");
                    Thread.sleep(800); // 页面稳定
                } else {
                    // 退出到初始页面
                    driver.navigate().back();
                    driver.navigate().back();
                    Thread.sleep(1000);
                }

                return fetcher.addressVideosAndComments(workesCount, devId, accountName, tags);

            } else if (tags.contains("综合视频")) {
                DouyinAddressCrawler crawler = new DouyinAddressCrawler(driver);
                if (!crawler.startDouyin()) return null;
                if (!crawler.searchAndEnterVideoTab(accountName)) return null;

                AccountInfoFetcherVoid fetcher = new AccountInfoFetcherVoid(driver,
                        videoService,
                        addressVideoService,
                        douyinTaskService,
                        accountContentService,
                        addressAccountContentService,
                        accountService
                );
                driver.startRecordingScreen();

                int[] videoCountResult = countSearchVideosUntilNoMore(driver);
                String workesCount = String.valueOf(videoCountResult[0] > 0 ? videoCountResult[0] : 100);
                logger.info("综合视频列表统计数量: " + workesCount);

                driver.navigate().back();
                Thread.sleep(1000);
                if (!crawler.searchAndEnterVideoTab(accountName)) return null;
                Thread.sleep(800); // 页面稳定

                if (!clickFirstSearchVideoIfPresent(driver)) {
                    logger.warning("click first search video failed");
                    return null;
                }
                Thread.sleep(1500);


                return fetcher.addressVideosAndComments(workesCount, devId, accountName, tags);

            } else {
                DouyinCrawler crawler = new DouyinCrawler(driver,
                        accountContentService,
                        addressAccountContentService,
                        accountService,douyinTaskService);
                if (!crawler.startDouyin()) return null;
                if (!crawler.searchAndEnterAccount(accountName)) return null;

                AccountInfoFetcherVoid fetcher = new AccountInfoFetcherVoid(driver,
                        videoService,
                        addressVideoService,
                        douyinTaskService,
                        accountContentService,
                        addressAccountContentService,
                        accountService);
                driver.startRecordingScreen();

                // 基础信息查询
                if (tags.contains("基本信息")) {
                    Map<String, Object> result = crawler.fetchAccountInfo("1", "1", devId, accountName);
                    douyinTaskService.storeAccountAsync(devId, accountName, result,tags);
                }

                return fetcher.recordAllVideosAndComments(devId, accountName, tags);
            }

        } catch (Exception e) {
            logger.severe("❌ crawlSingleAccount 异常：" + e.getMessage());
            // 打印完整堆栈，方便定位问题（补充原有缺失）
            e.printStackTrace();
            return null;
        } finally {
            // ========== 修复：优雅关闭驱动，避免端口残留 ==========
            if (driver != null) {
                try {
                    driver.stopRecordingScreen(); // 停止录屏（避免资源泄漏）
                    driver.quit(); // 关闭驱动
                } catch (Exception e) {
                    logger.warning("关闭驱动时异常：" + e.getMessage());
                }
            }
        }
    }
    /**
     * 如果存在“地点内容数量”，则点击进入地点内容页
     */
    private boolean clickAddressContentIfPresent(AndroidDriver<MobileElement> driver) {
        for (String xp : XPathRegistry.ADDRESS_COUNT_XPATHS) {
            try {
                List<MobileElement> elements = driver.findElements(By.xpath(xp));
                if (elements.isEmpty()) continue;

                MobileElement countText = elements.get(0);
                tapCenter(driver, countText);
                logger.warning("⚠ 使用兜底方式点击地点内容文本");
                return true;

            } catch (Exception ignored) {
            }
        }
        return false;
    }

    private MobileElement findClickableParent(AndroidDriver<MobileElement> driver, MobileElement el, int maxLevel) {
        MobileElement current = el;
        for (int i = 0; i < maxLevel; i++) {
            try {
                if ("true".equals(current.getAttribute("clickable"))) return current;
                current = (MobileElement) current.findElement(By.xpath(".."));
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private int[] countSearchVideosUntilNoMore(AndroidDriver<MobileElement> driver) {
        Set<String> videoSigns = new LinkedHashSet<>();
        int swipeCount = 0;
        int sameCountTimes = 0;
        int maxSwipe = 100;

        while (swipeCount <= maxSwipe) {
            int before = videoSigns.size();
            collectVisibleSearchVideos(driver, videoSigns);

            if (hasNoMoreSearchVideos(driver)) {
                logger.info("综合视频列表已到底，统计数量: " + videoSigns.size());
                break;
            }

            if (videoSigns.size() == before) {
                sameCountTimes++;
            } else {
                sameCountTimes = 0;
            }

            if (sameCountTimes >= 5) {
                logger.warning("综合视频列表连续多次没有新增，停止统计，当前数量: " + videoSigns.size());
                break;
            }

            if (!swipeSearchResults(driver, true)) {
                break;
            }

            swipeCount++;
            sleepQuietly(1000);
        }

        collectVisibleSearchVideos(driver, videoSigns);
        return new int[]{videoSigns.size(), swipeCount};
    }

    private void collectVisibleSearchVideos(AndroidDriver<MobileElement> driver, Set<String> videoSigns) {
        try {
            Dimension size = driver.manage().window().getSize();
            List<MobileElement> cards = driver.findElements(By.xpath(
                    "//android.widget.LinearLayout[@resource-id='com.ss.android.ugc.aweme:id/c7f']"
            ));

            for (MobileElement card : cards) {
                if (!card.isDisplayed()) continue;

                Rectangle rect = card.getRect();
                if (rect.getY() < 320 || rect.getY() > size.getHeight() - 80) {
                    continue;
                }

                String sign = buildSearchVideoSign(card);
                if (StringUtils.isNotBlank(sign)) {
                    videoSigns.add(sign);
                }
            }
        } catch (Exception e) {
            logger.warning("collect search videos failed: " + e.getMessage());
        }
    }

    private String buildSearchVideoSign(MobileElement card) {
        StringBuilder sign = new StringBuilder();
        String[] xpaths = {
                ".//*[contains(@resource-id,'desc')]",
                ".//*[contains(@resource-id,'title')]",
                ".//android.widget.TextView"
        };

        for (String xpath : xpaths) {
            try {
                List<MobileElement> texts = card.findElements(By.xpath(xpath));
                for (MobileElement text : texts) {
                    String value = Optional.ofNullable(text.getText()).orElse("").trim();
                    if (StringUtils.isNotBlank(value) && sign.indexOf(value) < 0) {
                        sign.append(value).append("|");
                    }
                }
            } catch (Exception ignored) {
            }
        }

        return sign.toString();
    }

    private boolean hasNoMoreSearchVideos(AndroidDriver<MobileElement> driver) {
        try {
            return !driver.findElements(By.xpath(
                    "//*[contains(@text,'暂时没有更多') or contains(@text,'没有更多') " +
                            "or contains(@content-desc,'暂时没有更多') or contains(@content-desc,'没有更多')]"
            )).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean swipeSearchResults(AndroidDriver<MobileElement> driver, boolean up) {
        try {
            Dimension size = driver.manage().window().getSize();
            int x = size.getWidth() / 2;
            int startY = up ? (int) (size.getHeight() * 0.78) : (int) (size.getHeight() * 0.35);
            int endY = up ? (int) (size.getHeight() * 0.35) : (int) (size.getHeight() * 0.78);

            new TouchAction<>(driver)
                    .press(PointOption.point(x, startY))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(350)))
                    .moveTo(PointOption.point(x, endY))
                    .release()
                    .perform();
            return true;
        } catch (Exception e) {
            logger.warning("swipe search results failed: " + e.getMessage());
            return false;
        }
    }

    private void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    private boolean clickFirstSearchVideoIfPresent(AndroidDriver<MobileElement> driver) {
        String[] xpaths = {
                "//android.widget.LinearLayout[@resource-id='com.ss.android.ugc.aweme:id/c7f' and @clickable='true']",
                "//android.view.View[@resource-id='com.ss.android.ugc.aweme:id/p=1' and @clickable='true']",
                "//android.widget.ImageView[@resource-id='com.ss.android.ugc.aweme:id/cover']"
        };

        for (String xpath : xpaths) {
            try {
                List<MobileElement> elements = driver.findElements(By.xpath(xpath));
                for (MobileElement element : elements) {
                    if (!element.isDisplayed()) continue;

                    Rectangle rect = element.getRect();
                    if (rect.getY() < 320 || rect.getHeight() <= 0 || rect.getWidth() <= 0) {
                        continue;
                    }

                    MobileElement clickable = findClickableParent(driver, element, 3);
                    tapCenter(driver, clickable != null ? clickable : element);
                    logger.info("clicked first search video by xpath: " + xpath);
                    return true;
                }
            } catch (Exception ignored) {
            }
        }

        return false;
    }

    private void tapCenter(AndroidDriver<MobileElement> driver, MobileElement el) {
        Rectangle r = el.getRect();
        int x = r.getX() + r.getWidth() / 2;
        int y = r.getY() + r.getHeight() / 2;

        new TouchAction<>(driver)
                .tap(PointOption.point(x, y))
                .perform();
    }
}
