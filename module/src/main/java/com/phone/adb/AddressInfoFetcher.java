package com.phone.adb;

import com.phone.common.constant.Constants;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class AddressInfoFetcher {

    private final AndroidDriver driver;
    private final String outputDir;
    private final Logger logger;
    private final ElementFinder finder;

    public AddressInfoFetcher(AndroidDriver driver, String outputDir) {
        this.driver = driver;
        this.outputDir = outputDir;
        this.logger = Logger.getLogger("DouyinParser");
        this.finder = new ElementFinder(driver, logger);
    }

    public Map<String, Object> getAccountBasicInfo() {
        Map<String, Object> info = new HashMap<>();
        initEmptyInfo(info);

        if (!ensureDeviceConnected()) return info;

        // 截图
        String imagePath = "address/" + UUID.randomUUID() + "_profile.png";
        takeScreenshot(imagePath);
        info.put("address_image", toWebPath("F:\\douyin_output\\" + imagePath));

        // 地点名
        String addressName = finder.findTextWithRetry(
                XPathRegistry.ADDRESS_TEXT_XPATHS, 3, null, "address_name");
        info.put("address", addressName);

        // 作品数
        String works = finder.findTextWithRetry(
                XPathRegistry.ADDRESS_COUNT_XPATHS, 3, null, "works_count");
        info.put("works_count", cleanNumberString(works));
        info.put("works", works);

        // 评论数
        String commentsCount = finder.findTextWithRetry(
                XPathRegistry.COMMENTS_COUNT_XPATHS, 3, null, "comments_count");
        info.put("comments_count", cleanNumberString(commentsCount));

        // 评分
        String score = finder.findTextWithRetry(
                XPathRegistry.SCORE_XPATHS, 3, null, "score");
        info.put("score", score);


//        放置点击评价事件按钮  然后继续后续操作
        if (clickCommentTab()) {
            System.out.println("进入评价页面");
            System.out.println("开始获取评论");
            // 指定保存评论的目录
            String videosDir = "F:\\douyin_output\\videosDir";

            // 初始化抓取器
            AddressCommentsFetcher fetcher = new AddressCommentsFetcher(driver);
            // 抓取当前视频的全部评论（包含 totalComments + comments + json_path）
            Map<String, Object> commentResult = fetcher.fetchAllPoiComments(videosDir);
            // 取出评论数组
            List<Map<String, Object>> comments = (List<Map<String, Object>>) commentResult.get("comments");
            // 写入视频数据
            info.put("comments", comments);
            info.put("json_path", commentResult.get("json_path"));       // ✔ 正确：保存路径字符串
            info.put("totalComments", commentResult.get("totalComments")); // ✔ 正确：真实总评论数
        } else {
            logger.warning("未能进入评价页，跳过评价相关操作");
        }

        return info;
    }

    private boolean clickCommentTab() {
        try {
            driver.findElement(
                    By.xpath(XPathRegistry.CLICK_COMMENT_TAB_XPATH)
            ).click();

            Thread.sleep(600); // 或换成你稳态等待
            logger.info("已点击【评价】Tab");
            return true;
        } catch (Exception e) {
            logger.warning("点击【评价】Tab 失败: " + e.getMessage());
            return false;
        }
    }


    private void initEmptyInfo(Map<String, Object> info) {
        info.put("address", "");
        info.put("works_count", "");
        info.put("comments_count", "");
        info.put("address_image", "");
        info.put("score", "");
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
            Files.copy(screenshot.toPath(),
                    Paths.get(outputDir, filename),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            logger.warning("保存截图失败: " + e.getMessage());
        }
    }

    private String toWebPath(String fullPath) {
        if (fullPath == null) return null;
        String prefix = "F:\\\\douyin_output\\\\";
        String[] arr = fullPath.split(prefix, 2);
        String relative = arr.length > 1 ? arr[1] : fullPath;
        return Constants.RESOURCE_PREFIX + "/" + relative.replace("\\", "/");
    }

    private String cleanNumberString(String txt) {
        if (txt == null) return "";
        return txt.replaceAll("[^0-9\\.万亿]", "");
    }
}
