package com.phone.adb;

import com.alibaba.fastjson.JSON;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.io.FileWriter;
import java.time.Duration;
import java.util.*;
import java.util.logging.Logger;

/**
 * 地点（POI）评价抓取器
 * 适配 Lynx POI 评论（UIView / LynxTextUI 混合结构）
 */
public class AddressCommentsFetcher {

    private final AppiumDriver<MobileElement> driver;
    private final Logger logger = Logger.getLogger(AddressCommentsFetcher.class.getName());

    public AddressCommentsFetcher(AppiumDriver<MobileElement> driver) {
        this.driver = driver;
    }

    /**
     * 主入口
     */
    public Map<String, Object> fetchAllPoiComments(String saveDir) {

        List<Map<String, Object>> comments = new ArrayList<>();
        Set<String> dedup = new HashSet<>();

        int noNewRound = 0;
        int maxNoNewRound = 4;

        boolean hasClickedViewAll = false;

        while (noNewRound < maxNoNewRound) {

            int beforeSize = comments.size();



            // ⭐ 检测是否出现“已为你折叠无效评论”
            if (detectInvalidFoldTip()) {
                if (hasClickedViewAll){
                    driver.navigate().back();
                    driver.navigate().back();
                }
                logger.info("⚠ 检测到「已为你折叠无效评论」，提前结束抓取");
                break;
            }

            // ① 先抓当前屏数据
            List<Map<String, Object>> pageData = collectCurrentPoiComments(dedup);
            comments.addAll(pageData);

            // ② 如果已抓到评论，但还没点「查看全部评价」，尝试展开
            if (!hasClickedViewAll && !pageData.isEmpty()) {
                hasClickedViewAll = tryClickViewAllOnce();
            }

            // ③ 判断是否有新增
            if (comments.size() == beforeSize) {
                noNewRound++;
            } else {
                noNewRound = 0;
            }

            // ④ 稳态滑动
            swipeUp();
        }

        String jsonPath = saveJson(comments, saveDir);

        Map<String, Object> result = new HashMap<>();
        result.put("total", comments.size());
        result.put("comments", comments);
        result.put("json_path", jsonPath);

        return result;
    }

    /**
     * 检测页面是否出现“已为你折叠无效评论”
     */
    private boolean detectInvalidFoldTip() {
        try {
            List<MobileElement> tips = driver.findElements(By.xpath(
                    "//*[contains(@text,'已折叠对你帮助不大的评价') " +
                            "or contains(@content-desc,'已折叠对你帮助不大的评价')]"
            ));

            for (MobileElement tip : tips) {
                if (tip.isDisplayed()) {
                    logger.info("🛑 命中折叠无效评论区域，停止抓取");
                    return true;
                }
            }

        } catch (Exception e) {
            logger.fine("detectInvalidFoldTip error: " + e.getMessage());
        }
        return false;
    }


    // =====================================================
    // 核心：POI 评论采集（UIView / content-desc 适配）
    // =====================================================
    private List<Map<String, Object>> collectCurrentPoiComments(Set<String> dedup) {

        List<Map<String, Object>> result = new ArrayList<>();

        List<MobileElement> cards = driver.findElements(By.xpath(
                "//com.lynx.tasm.behavior.ui.view.UIView" +
                        "[@clickable='true' and (" +
                        "string-length(normalize-space(@text)) > 10 or " +
                        "string-length(normalize-space(@content-desc)) > 10" +
                        ")]"
        ));

        for (MobileElement card : cards) {
            try {
                // 优先 content-desc
                String content = card.getAttribute("content-desc");
                if (content == null || content.trim().isEmpty()) {
                    content = card.getText();
                }
                if (content == null) continue;

                content = content.trim();

                // 过滤非评论
                if (content.contains("查看全部")) continue;
                if (content.length() < 10) continue;

                // 去重
                if (!dedup.add(content)) continue;

                Map<String, Object> item = new HashMap<>();
                item.put("content", content);

//                // 星级
//                item.put("star", card.findElements(By.xpath(
//                        ".//com.ss.android.ugc.aweme.poi.lynx.element.rateimage.PoiLynxRateImage"
//                )).size());

//                // 是否有图片
//                item.put("has_image", !card.findElements(By.xpath(
//                        ".//com.lynx.tasm.ui.image.FlattenUIImage"
//                )).isEmpty());

                result.add(item);

            } catch (Exception ignore) {}
        }

        return result;
    }

    // =====================================================
    // 点击「查看全部评价」（仅在检测到时触发）
    // =====================================================
    private boolean tryClickViewAllOnce() {
        try {
            List<MobileElement> buttons = driver.findElements(By.xpath(
                    "//com.lynx.tasm.behavior.ui.LynxFlattenUI" +
                            "[@clickable='true' and (" +
                            "contains(@text,'查看全部') or " +
                            "contains(@content-desc,'查看全部')" +
                            ")]"
            ));

            for (MobileElement btn : buttons) {
                if (btn.isDisplayed()) {
                    btn.click();
                    Thread.sleep(1200);
                    return true;
                }
            }
        } catch (Exception ignore) {}
        return false;
    }

    // =====================================================
    // 稳态滑动
    // =====================================================
    private void swipeUp() {
        try {
            int w = driver.manage().window().getSize().width;
            int h = driver.manage().window().getSize().height;
            swipe(w / 2, (int) (h * 0.45), w / 2, (int) (h * 0.25), 300);
        } catch (Exception e) {
            logger.fine("swipeUp error: " + e.getMessage());
        }
    }

    private void swipe(int startX, int startY, int endX, int endY, int durationMs) {
        try {
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 1);

            swipe.addAction(finger.createPointerMove(Duration.ZERO,
                    PointerInput.Origin.viewport(), startX, startY));
            swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(durationMs),
                    PointerInput.Origin.viewport(), endX, endY));
            swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Collections.singletonList(swipe));
            Thread.sleep(800);
        } catch (Exception ignore) {}
    }

    // =====================================================
    // 保存 JSON
    // =====================================================
    private String saveJson(List<Map<String, Object>> data, String dir) {
        if (data.isEmpty()) return null;

        String path = dir + "/address_comments_" + System.currentTimeMillis() + ".json";
        try (FileWriter fw = new FileWriter(path)) {
            fw.write(JSON.toJSONString(data, true));
            return path;
        } catch (Exception e) {
            return null;
        }
    }
}
