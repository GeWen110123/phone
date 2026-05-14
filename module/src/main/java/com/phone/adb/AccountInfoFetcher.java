package com.phone.adb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phone.common.constant.Constants;
import com.phone.module.domain.Account;
import com.phone.module.domain.AccountContent;
import com.phone.module.domain.AddressAccountContent;
import com.phone.module.service.DouyinTaskService;
import com.phone.module.service.IAccountContentService;
import com.phone.module.service.IAccountService;
import com.phone.module.service.IAddressAccountContentService;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountInfoFetcher {

    /**
     * 获取主页基本信息
     */
    private final AndroidDriver driver;
    private final String outputDir;
    private final Logger logger;
    private final ElementFinder finder;

    private final IAccountContentService accountContentService;
    private final IAddressAccountContentService addressAccountContentService;
    private final IAccountService accountService;
    private final DouyinTaskService douyinTaskService;

    public AccountInfoFetcher(AndroidDriver driver,
                              String outputDir,
                              IAccountContentService accountContentService,
                              IAddressAccountContentService addressAccountContentService,
                              IAccountService accountService,DouyinTaskService douyinTaskService) {

        this.driver = driver;
        this.outputDir = outputDir;
        this.logger = Logger.getLogger("DouyinParser");
        this.finder = new ElementFinder(driver, logger);
        this.douyinTaskService = douyinTaskService;
        this.accountContentService = accountContentService;
        this.addressAccountContentService = addressAccountContentService;
        this.accountService = accountService;
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


    private Map<String, Object> getCacheFromDB(String douyinId) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            // 1️⃣ account_content 表
            List<AccountContent> listFollows = accountContentService.selectFollowListByUid(douyinId);
            if (listFollows != null && !listFollows.isEmpty() && listFollows.get(0).getJsonString() != null) {
                return mapper.readValue(listFollows.get(0).getJsonString(), Map.class);
            }
            List<AccountContent> listFans = accountContentService.selectFollowListByUid(douyinId);
            if (listFans != null && !listFans.isEmpty() && listFans.get(0).getJsonString() != null) {
                return mapper.readValue(listFans.get(0).getJsonString(), Map.class);
            }
            // 1️⃣ account_content 表
            AccountContent ac = new AccountContent();
            ac.setDouyinId(douyinId);
            List<AccountContent> list1 = accountContentService.selectAccountContentList(ac);
            if (list1 != null && !list1.isEmpty() && list1.get(0).getJsonString() != null) {
                return mapper.readValue(list1.get(0).getJsonString(), Map.class);
            }

            // 2️⃣ address_account_content 表
            AddressAccountContent aac = new AddressAccountContent();
            aac.setDouyinId(douyinId);
            List<AddressAccountContent> list2 = addressAccountContentService.selectAddressAccountContentList(aac);
            if (list2 != null && !list2.isEmpty() && list2.get(0).getJsonString() != null) {
                return mapper.readValue(list2.get(0).getJsonString(), Map.class);
            }

            // 3️⃣ account 表
            Account acc = new Account();
            acc.setDouyinId(douyinId);
            List<Account> list3 = accountService.selectAccountList(acc);
            if (list3 != null && !list3.isEmpty() && list3.get(0).getJsonString() != null) {
                return mapper.readValue(list3.get(0).getJsonString(), Map.class);
            }

        } catch (Exception e) {
            logger.warning("缓存读取异常: " + e.getMessage());
        }

        return null;
    }

    public Map<String, Object> getAccountBasicInfo(String type, String status, String devId, String accountName) {
        Map<String, Object> info = new HashMap<>();
        initEmptyInfo(info);

        if (!ensureDeviceConnected()) return info;

        // ⭐⭐⭐ 只获取一次页面源码
        String pageSource;
        try {
            pageSource = driver.getPageSource();
        } catch (Exception e) {
            logger.severe("获取页面源码失败: " + e.getMessage());
            return info;
        }


        // ⭐ 先解析最关键字段：昵称 + 抖音号
        String nickname = getNickname(pageSource);
        String douyinId = getDouyinId(pageSource, nickname);

        info.put("nickname", nickname);
        info.put("id", douyinId);

        // ==============================
        // ⭐⭐ 优先从数据库读取缓存数据
        // ==============================
        // ==============================
        // ⭐ 多表缓存查询
        // ==============================
        if (type.equals("2")||status.equals("0")) {
            Map<String, Object> cacheMap = getCacheFromDB(douyinId);
            if (cacheMap != null) {
                logger.info("命中数据库缓存(多表之一)，直接返回: " + douyinId);
                return cacheMap;
            }
        }


        // 1. 截图
        String accountImage = "zhuye/" + UUID.randomUUID() + "_profile_screen.png";
        takeScreenshot(accountImage);
        info.put("zhuye", toWebPath("F:\\douyin_output\\" + accountImage));


        info.put("follow_count", getFollowCount(pageSource));
        info.put("fans_count", getFansCount(pageSource));
        info.put("likes_count", getLikesCount(pageSource));
        info.put("works_count", getWorksCount(pageSource));
        info.put("signature", getSignature(pageSource, info.get("nickname").toString()));
        info.put("region", getRegion(pageSource));
        info.put("gender", getGender(pageSource));
        info.put("age", getAge(pageSource));
        info.put("school", getSchool(pageSource));


        // ==============================
// ⭐ 新增：主页简介正文（多行）
// ==============================
        String profileRawText = getProfileText(pageSource);
        info.put("profile_text", profileRawText);

// ==============================
// ⭐ 新增：IP & 地区
// ==============================
        String ipLocation = getIpLocation(pageSource);
        info.put("ip_location", ipLocation);

        String regionFull = getRegionFull(pageSource);
        info.put("region_full", regionFull);

        if (regionFull.contains("·")) {
            String[] parts = regionFull.split("·");
            info.put("province", parts[0]);
            info.put("city", parts.length > 1 ? parts[1] : "");
        }
// ==============================
        info.put("real_name", getRealName());


        if (openUserProfileFromAvatar()) {
            String screenshotFileName = "touxiang/" + UUID.randomUUID() + "_profile_screen.png";
            takeScreenshot(screenshotFileName);
            info.put("touxiang", toWebPath(("F:\\douyin_output\\" + screenshotFileName)));
            try {
                driver.navigate().back();
            } catch (Exception ignored) {
                driver.navigate().back();
            }
        }

//        if ("1".equals(status)) {
//            douyinTaskService.storeAccountAsync(devId, accountName, info,"");
//
//            int followCount = parseCount((String) info.get("follow_count"));
//            int fansCount = parseCount((String) info.get("fans_count"));
//
//
//            // ⭐ 采集关注
//            if (followCount > 0 && clickFollowButtonAndCheck()) {
//                int count =accountContentService.selectFollowList(douyinId).size();
//                if (count<=followCount/2){
//                    collectRelationList("follow", devId, douyinId,count);
//                }
//                driver.navigate().back();
//            }
//
//            // ⭐ 采集粉丝
//            if (fansCount > 0 && clickFansButtonAndCheck()) {
//                int count =accountContentService.selectFansList(douyinId).size();
//                if (count<=fansCount/2){
//                    collectRelationList("fans", devId, douyinId,count);
//                }
//                driver.navigate().back();
//            }
//        }

        return info;
    }

    private void collectRelationList(String type, String devId, String douyinId,int count) {

        logger.info("进入" + (type.equals("follow") ? "关注" : "粉丝") + "列表页");

        if (count / 10 > 1) {
            int loops = count / 10;
            for (int i = 0; i < loops; i++) {  // 条件 i < loops
                scrollList();
                try {
                    Thread.sleep(500); // 可以适当加个短延时
                } catch (InterruptedException ignored) {}
            }
        }

        Set<String> processedSet = new HashSet<>();
        int noChangeCount = 0;
        String lastHash = "";

        while (true) {

            List<WebElement> items = driver.findElements(
                    By.id("com.ss.android.ugc.aweme:id/root_layout")
            );

            boolean hasNew = false;

            for (int i = 0; i < items.size(); i++) {

                try {
                    List<WebElement> currentItems = driver.findElements(
                            By.id("com.ss.android.ugc.aweme:id/root_layout")
                    );

                    if (i >= currentItems.size()) break;

                    WebElement item = currentItems.get(i);

                    String nicknameItem = item.findElement(
                            By.id("com.ss.android.ugc.aweme:id/337")
                    ).getText();

                    if (nicknameItem == null || nicknameItem.isEmpty()) continue;
                    if (processedSet.contains(nicknameItem)) continue;

                    processedSet.add(nicknameItem);
                    hasNew = true;

                    // ⭐ 点击头像进入子主页
                    clickAvatar(item);
                    Thread.sleep(1500);

//                    // ⭐ 子主页采集（status=0 防止递归）
//                    Map<String, Object> subInfo = getAccountBasicInfo("1", "0");
//
//                    AccountContent content = convertToAccount(subInfo);
//
//                    if ("follow".equals(type)) {
//                        accountContentService.insertFollow(content);
//                    } else {
//                        accountContentService.insertFans(content);
//                    }

                    Map<String, Object> subInfo = getAccountBasicInfo("1", "0", devId, douyinId);

                    String subDouyinId = String.valueOf(subInfo.get("id"));

                    saveRelationAccount(
                            type,
                            devId,
                            douyinId,        // 当前主页id
                            subDouyinId,     // 子账号id
                            subInfo
                    );

                    driver.navigate().back();
                    Thread.sleep(1500);

                } catch (Exception e) {
                    logger.warning("采集异常，跳过当前条目：" + e.getMessage());
                    try {
                        driver.navigate().back();
                    } catch (Exception ignored) {
                    }
                }
            }

// ⭐ 判断是否出现隐私提示
            if (isPrivacyLimitShown()) {
                logger.info(type + " 列表因隐私限制结束采集");
                break;
            }

// ⭐ 判断是否到底（页面无变化）
            String currentHash = String.valueOf(driver.getPageSource().hashCode());

            if (!hasNew || currentHash.equals(lastHash)) {
                noChangeCount++;
            } else {
                noChangeCount = 0;
            }

            if (noChangeCount >= 5) {
                logger.info(type + " 列表采集完成");
                break;
            }

            lastHash = currentHash;

            scrollList();
            sleep(1500);
        }
    }


    private boolean isPrivacyLimitShown() {
        try {
            List<WebElement> elements = driver.findElements(
                    By.xpath("//*[contains(@text,'隐私设置') or contains(@text,'暂不支持查看')]")
            );
            return !elements.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }



    private void saveRelationAccount(String relationType,
                                     String devId,
                                     String douyinId,
                                     String accountName,
                                     Map<String, Object> resultMap) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(resultMap);

            AccountContent account = new AccountContent();
            account.setDevId(devId);
            account.setDouyinId(douyinId);       // 主账号id
            account.setAccountName(douyinId);    // 主账号
            account.setvUid(accountName);        // 子账号id（被关注/粉丝）
            account.setJsonString(json);
            account.setCreateTime(new Date());

            if ("follow".equals(relationType)) {
                accountContentService.insertFollow(account);
            } else {
                accountContentService.insertFans(account);
            }

        } catch (Exception e) {
            logger.severe("保存关系数据失败：" + e.getMessage());
        }
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }


    private AccountContent convertToAccount(Map<String, Object> info) {

        AccountContent content = new AccountContent();

        // ⭐ 可选：记录采集时间
        content.setCreateTime(new Date());

        return content;
    }


    private void scrollList() {

        int startX = 540;
        int startY = 1700;
        int endY = 600;

        new TouchAction<>(driver)
                .press(PointOption.point(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(800)))
                .moveTo(PointOption.point(startX, endY))
                .release()
                .perform();
    }

    public boolean clickFollowButtonAndCheck() {
        try {

            // ⭐ 等待关注按钮出现
            WebDriverWait wait = new WebDriverWait(driver, 10);

            List<WebElement> elements = wait.until(driver ->
                    driver.findElements(By.xpath("//*[contains(@text,'关注')]"))
            );

            boolean clicked = false;

            for (WebElement element : elements) {
                String text = element.getText();

                // 避免点到“已关注”
                if ("关注".equals(text) || text.matches("关注\\s*\\d+")) {
                    element.click();
                    clicked = true;
                    break;
                }
            }

            if (!clicked) {
                logger.warning("未找到真正的关注按钮");
                return false;
            }

            // ⭐ 等待页面跳转
            Thread.sleep(1500);


            boolean inFansPage = isInFansPage();

            if (inFansPage) {
                logger.info("成功进入关注列表页");
                return true;
            } else {
                logger.warning("点击后未成功进入关注页");
                return false;
            }

        } catch (Exception e) {
            logger.severe("点击关注按钮异常: " + e.getMessage());
            return false;
        }
    }

    public boolean clickFansButtonAndCheck() {
        try {

            WebDriverWait wait = new WebDriverWait(driver, 10);

            // ⭐ 等待“粉丝”元素出现
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(@text,'粉丝')]")
            ));

            List<WebElement> elements = driver.findElements(
                    By.xpath("//*[contains(@text,'粉丝')]")
            );

            boolean clicked = false;

            for (WebElement element : elements) {

                String text = element.getText();

                // 精准匹配：避免误点其它包含“粉丝”的文案
                if ("粉丝".equals(text) || text.matches("粉丝\\s*\\d+")) {
                    element.click();
                    clicked = true;
                    break;
                }
            }

            if (!clicked) {
                logger.warning("未找到真正的粉丝按钮");
                return false;
            }

            // ⭐ 等待页面跳转
            Thread.sleep(1500);

            boolean inFansPage = isInFansPage();

            if (inFansPage) {
                logger.info("成功进入粉丝列表页");
                return true;
            } else {
                logger.warning("点击后未成功进入粉丝页");
                return false;
            }

        } catch (Exception e) {
            logger.severe("点击粉丝按钮异常: " + e.getMessage());
            return false;
        }
    }

    private boolean isInFansPage() {
        try {
            // 查找顶部 Tab
            List<WebElement> tabs = driver.findElements(By.id("android:id/text1"));
            boolean hasFansTab = false;
            for (WebElement tab : tabs) {
                String text = tab.getText();
                if (text != null && text.contains("粉丝")) {
                    hasFansTab = true;
                    break;
                }
            }

            // 查找粉丝列表 RecyclerView
            List<WebElement> listView = driver.findElements(By.id("com.ss.android.ugc.aweme:id/sp2"));

            if (hasFansTab && !listView.isEmpty()) {
                logger.info("确认进入粉丝列表页");
                return true;
            }
        } catch (Exception e) {
            logger.warning("粉丝页校验异常: " + e.getMessage());
        }

        logger.warning("未进入粉丝列表页");
        return false;
    }


    public void clickAvatar(WebElement item) {

        try {

            // 在当前条目里找头像 ImageView
            WebElement avatar = item.findElement(
                    By.xpath(".//android.widget.ImageView")
            );

            avatar.click();
            Thread.sleep(2000);

            System.out.println("已点击头像进入主页");

        } catch (Exception e) {
            System.out.println("点击头像失败，改为点击整条记录");
            item.click();
        }
    }

    private int parseCount(String countStr) {
        if (countStr == null || countStr.trim().isEmpty()) {
            return 0;
        }

        countStr = countStr.replaceAll("[^0-9.万]", "");

        try {
            if (countStr.contains("万")) {
                double num = Double.parseDouble(countStr.replace("万", ""));
                return (int) (num * 10000);
            }
            return Integer.parseInt(countStr);
        } catch (Exception e) {
            return 0;
        }
    }

    private void initEmptyInfo(Map<String, Object> info) {
        info.put("id", "");
        info.put("nickname", "");
        info.put("avatar", "");
        info.put("likes_count", "");
        info.put("follow_count", "");
        info.put("fans_count", "");
        info.put("works_count", "");
        info.put("region", "");
        info.put("gender", "");
        info.put("age", "");
        info.put("school", "");
        info.put("signature", "");
        info.put("custom_bio_items", new HashMap<>());
        info.put("real_name", "");

        // ⭐ 新增字段（只加，不动原有）
        info.put("profile_text", "");
        info.put("ip_location", "");
        info.put("province", "");
        info.put("city", "");
        info.put("region_full", "");
    }


    private String getIpLocation(String pageSource) {
        Matcher m = Pattern.compile("IP：([^\"<]+)").matcher(pageSource);
        return m.find() ? m.group(1).trim() : "";
    }

    private String getRegionFull(String pageSource) {
        Matcher m = Pattern.compile("([\\u4e00-\\u9fa5]+[省自治区市]·[\\u4e00-\\u9fa5]+市)")
                .matcher(pageSource);
        return m.find() ? m.group(1) : "";
    }

    private String getProfileText(String pageSource) {
        String longestText = "";

        try {
            Pattern p = Pattern.compile(
                    "<android\\.widget\\.TextView[^>]*text=\"([^\"]+)\"[^>]*focusable=\"true\"[^>]*/?>"
            );
            Matcher m = p.matcher(pageSource);

            while (m.find()) {
                String text = m.group(1)
                        .replace("&#10;", "\n")
                        .trim();

                // ❌ 排除抖音号
                if (text.startsWith("抖音号")) {
                    continue;
                }
//
//                // ❌ 太短的不要
//                if (text.length() < 10) {
//                    continue;
//                }
//
//                // ❌ 没中文的一般不是简介
//                if (!text.matches(".*[\\u4e00-\\u9fa5].*")) {
//                    continue;
//                }

                // ⭐ 取最长的一条
                if (text.length() > longestText.length()) {
                    longestText = text;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return longestText;
    }


    private boolean ensureDeviceConnected() {
        try {
            driver.getPageSource();
            return true;
        } catch (Exception e) {
            logger.severe("设备连接失败: " + e.getMessage());
            return false;
        }
    }

    private void takeScreenshot(String filename) {
        try {
            File screenshot = driver.getScreenshotAs(org.openqa.selenium.OutputType.FILE);
            Files.copy(screenshot.toPath(), Paths.get(outputDir, filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            logger.warning("保存截图失败: " + e.getMessage());
        }
    }

    // -------------------------
    // 各字段单独方法
    // -------------------------
//    private String getNickname() {
//        return finder.findTextWithRetry(XPathRegistry.NICKNAME_XPATHS, 3,
//                txt -> txt != null && !txt.trim().isEmpty(), "nickname");
//    }
    private String getNickname(String src) {
        return finder.findTextFromSource(
                src,
                XPathRegistry.NICKNAME_XPATHS,
                txt -> !txt.isEmpty(),
                "nickname");
    }

    private String getDouyinId(String src, String nickname) {
        String id = finder.findTextFromSource(
                src,
                XPathRegistry.DOUYIN_ID_XPATHS,
                txt -> !txt.isEmpty(),
                "douyin_id");

        return id.isEmpty() ? "NICKNAME_" + nickname : TextCleaner.cleanDouyinId(id);
    }
//    private String getDouyinId(String nickname) {
//        String id = finder.findTextWithRetry(XPathRegistry.DOUYIN_ID_XPATHS, 3,
//                txt -> txt != null && !txt.trim().isEmpty(), "douyin_id");
//        if (id == null || id.isEmpty()) return "NICKNAME_" + nickname;
//        return TextCleaner.cleanDouyinId(id);
//    }

    private String getFollowCount(String src) {
        return cleanNumberString(
                finder.findTextFromSource(src, XPathRegistry.FOLLOW_COUNT_XPATHS, null, "follow_count"));
    }

    private String getFansCount(String src) {
        return cleanNumberString(
                finder.findTextFromSource(src, XPathRegistry.FANS_COUNT_XPATHS, null, "fans_count"));
    }

    private String getLikesCount(String src) {
        return cleanNumberString(
                finder.findTextFromSource(src, XPathRegistry.LIKES_COUNT_XPATHS, null, "likes_count"));
    }

    private String getWorksCount(String src) {
        return cleanNumberString(
                finder.findTextFromSource(src, XPathRegistry.WORKS_COUNT_XPATHS, null, "works_count"));
    }


    private String getSignature(String src, String nickname) {
        return finder.findTextFromSource(
                src,
                XPathRegistry.SIGNATURE_XPATHS,
                txt -> txt != null && txt.length() > 2 && !txt.equals(nickname),
                "signature");
    }

    private String getRegion(String src) {
        return finder.findTextFromSource(
                src,
                XPathRegistry.IP_XPATHS,
                txt -> txt != null && !txt.isEmpty(),
                "region");
    }

    private String getGender(String src) {
        return finder.findTextFromSource(
                src,
                XPathRegistry.GENDER_XPATHS,
                txt -> txt != null && (txt.contains("男") || txt.contains("女")),
                "gender");
    }

    private String getAge(String src) {
        return finder.findTextFromSource(
                src,
                XPathRegistry.AGE_XPATHS,
                txt -> txt != null && !txt.isEmpty(),
                "age");
    }

    private String getSchool(String src) {
        return finder.findTextFromSource(
                src,
                XPathRegistry.SCHOOL_XPATHS,
                txt -> txt != null && !txt.isEmpty(),
                "school");
    }


    private String getRealName() {
        // 1️⃣ 尝试直接从主页获取
        String[] homepageXPs = new String[]{
                "//*[contains(@text, '真实姓名')]",
                "//*[contains(@text, '真实姓名：')]",
                "//*[contains(@text, '真实姓名:')]",
                "//*[contains(@text, '姓名：') and not(contains(@text, '未设置'))]",
                "//*[contains(@text, '姓名:') and not(contains(@text, '未设置'))]",
                "//*[contains(@resource-id, 'real_name')]",
                "//*[contains(@resource-id, 'id_card_name')]"
        };

        String realName = finder.findTextWithRetry(homepageXPs, 1,
                txt -> txt != null && !txt.trim().isEmpty(), "real_name");

        if (realName != null && !realName.isEmpty()) {
            return extractName(realName);
        }

        logger.info("未直接获取到实名信息，尝试点击已实名按钮...");

        // 2️⃣ 点击“已实名”按钮或其他实名入口
        String[] verifiedButtonXPs = new String[]{
                "//*[contains(@text, '已实名')]",
                "//*[contains(@text, '实名认证')]",
                "//*[contains(@resource-id, 'real_name')]",
                "//*[contains(@content-desc, '实名认证')]",
                "//*[contains(@text, '实名信息')]",
                "//*[contains(@resource-id, 'verified') and contains(@text, '实名')]",
                "//*[@resource-id='com.ss.android.ugc.aweme:id/b8p']",
                "//*[contains(@resource-id, 'authentication')]",
                "//*[contains(@text, '认证说明')]"
        };

        boolean clicked = false;
        for (String xp : verifiedButtonXPs) {
            if (finder.exists(xp)) {
                try {
                    driver.findElement(By.xpath(xp)).click();
                    finder.sleep(1500); // 等待弹窗/页面加载
                    clicked = true;
                    break;
                } catch (Exception e) {
                    logger.warning("点击已实名按钮失败: " + e.getMessage());
                }
            }
        }

        // 3️⃣ 点击后弹窗/页面再次抓取实名信息
        if (clicked) {
            String[] popupXPs = new String[]{
                    "//*[contains(@text, '真实姓名')]/following-sibling::*",
                    "//*[contains(@text, '姓名') and not(contains(@text, '未设置'))]",
                    "//*[contains(@resource-id, 'real_name')]",
                    "//*[contains(@resource-id, 'id_card_name')]"
            };

            realName = finder.findTextWithRetry(popupXPs, 3,
                    txt -> txt != null && !txt.trim().isEmpty(), "real_name_after_click");

            // 尝试点击“我知道了”或返回上一页
            String[] closeXPs = new String[]{"//*[@text='我知道了']"};
            for (String xp : closeXPs) {
                if (finder.exists(xp)) {
                    try {
                        driver.findElement(By.xpath(xp)).click();
                        break;
                    } catch (Exception ignored) {
                        driver.navigate().back();
                    }
                }
            }
        }

        return realName != null ? extractName(realName) : "";
    }

    private boolean openUserProfileFromAvatar() {
        String[] avatarXPs = new String[]{
                "//*[@content-desc='用户头像']",
                "//*[@resource-id='com.ss.android.ugc.aweme:id/kru']",
                "//android.widget.ImageView[contains(@content-desc,'头像')]",
                "//android.widget.ImageView[@clickable='true']"
        };

        // 1️⃣ 找头像
        for (String xp : avatarXPs) {
            if (finder.exists(xp)) {
                try {
                    driver.findElement(By.xpath(xp)).click();
                    finder.sleep(1500); // 等待跳转
                    logger.info("已点击用户头像，进入个人主页");
                    return true;
                } catch (Exception e) {
                    logger.warning("点击用户头像失败: " + e.getMessage());
                }
            }
        }

        logger.warning("未找到用户头像元素");
        return false;
    }

    // ======================
    // 工具方法：提取姓名
    // ======================
    private String extractName(String text) {
        if (text == null || text.trim().isEmpty()) return "";
        String name = text;
        if (text.contains("真实姓名：")) name = text.split("真实姓名：")[1].trim();
        else if (text.contains("真实姓名:")) name = text.split("真实姓名:")[1].trim();
        else if (text.contains("姓名：")) name = text.split("姓名：")[1].trim();
        else if (text.contains("姓名:")) name = text.split("姓名:")[1].trim();
        else if (text.contains("：")) name = text.split("：")[1].trim();
        else if (text.contains(":")) name = text.split(":", 2)[1].trim();
        else name = text.trim();
        return name;
    }

    // ======================
    // 工具方法：保留原始数字+小数点+单位字符串
    // ======================
    private String cleanNumberString(String txt) {
        if (txt == null) return "";
        return txt.replaceAll("[^0-9\\.亿万]", "");
    }
}
