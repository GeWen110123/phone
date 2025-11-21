package com.phone.adb;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class AccountInfoFetcher {

    private final AndroidDriver driver;
    private final String outputDir;
    private final Logger logger;
    private final ElementFinder finder;

    public AccountInfoFetcher(AndroidDriver driver, String outputDir) {
        this.driver = driver;
        this.outputDir = outputDir;
        this.logger = Logger.getLogger("DouyinParser");
        this.finder = new ElementFinder(driver, logger);
    }

    public Map<String, Object> getAccountBasicInfo() {
        Map<String, Object> info = new HashMap<>();
        initEmptyInfo(info);

        if (!ensureDeviceConnected()) return info;

        takeScreenshot("profile_screen.png");

        info.put("nickname", getNickname());
        info.put("id", getDouyinId(info.get("nickname").toString()));
        info.put("follow_count", getFollowCount());
        info.put("fans_count", getFansCount());
        info.put("likes_count", getLikesCount());
        info.put("works_count", getWorksCount());
        info.put("signature", getSignature(info.get("nickname").toString()));
        info.put("region", getRegion());
        info.put("gender", getGender());
        info.put("age", getAge());
        info.put("school", getSchool());
        info.put("real_name", getRealName());

        return info;
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
    private String getNickname() {
        return finder.findTextWithRetry(XPathRegistry.NICKNAME_XPATHS, 3,
                txt -> txt != null && !txt.trim().isEmpty(), "nickname");
    }

    private String getDouyinId(String nickname) {
        String id = finder.findTextWithRetry(XPathRegistry.DOUYIN_ID_XPATHS, 3,
                txt -> txt != null && !txt.trim().isEmpty(), "douyin_id");
        if (id == null || id.isEmpty()) return "NICKNAME_" + nickname;
        return TextCleaner.cleanDouyinId(id);
    }

    private String getFollowCount() {
        String txt = finder.findTextWithRetry(XPathRegistry.FOLLOW_COUNT_XPATHS, 3, null, "follow_count");
        return cleanNumberString(txt);
    }

    private String getFansCount() {
        String txt = finder.findTextWithRetry(XPathRegistry.FANS_COUNT_XPATHS, 3, null, "fans_count");
        return cleanNumberString(txt);
    }

    private String getLikesCount() {
        String txt = finder.findTextWithRetry(XPathRegistry.LIKES_COUNT_XPATHS, 3, null, "likes_count");
        return cleanNumberString(txt);
    }

    private String getWorksCount() {
        String txt = finder.findTextWithRetry(XPathRegistry.WORKS_COUNT_XPATHS, 3, null, "works_count");
        return cleanNumberString(txt);
    }

    private String getSignature(String nickname) {
        return finder.findTextWithRetry(XPathRegistry.SIGNATURE_XPATHS, 3,
                txt -> txt.length() > 2 && !txt.equals(nickname), "signature");
    }

    private String getRegion() {
        return finder.findTextWithRetry(XPathRegistry.IP_XPATHS, 2, txt -> !txt.isEmpty(), "region");
    }

    private String getGender() {
        return finder.findTextWithRetry(XPathRegistry.GENDER_XPATHS, 2, txt -> txt.contains("男") || txt.contains("女"), "gender");
    }

    private String getAge() {
        return finder.findTextWithRetry(XPathRegistry.AGE_XPATHS, 2, txt -> !txt.isEmpty(), "age");
    }

    private String getSchool() {
        return finder.findTextWithRetry(XPathRegistry.SCHOOL_XPATHS, 2, txt -> !txt.isEmpty(), "school");
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
                    } catch (Exception ignored) {}
                }
            }
            try { driver.navigate().back(); } catch (Exception ignored) {}
        }

        return realName != null ? extractName(realName) : "";
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
