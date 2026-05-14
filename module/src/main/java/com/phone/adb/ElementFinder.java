package com.phone.adb;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class ElementFinder {

    private final AndroidDriver driver;
    private final Logger logger;

    public ElementFinder(AndroidDriver driver, Logger logger) {
        this.driver = driver;
        this.logger = logger;
    }

    public boolean exists(String xpath) {
        try {
            driver.findElement(By.xpath(xpath));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String findTextWithRetry(String[] xpaths, int retries, Predicate<String> validator, String fieldName) {
        for (int r = 0; r < retries; r++) {
            for (String xp : xpaths) {
                if (!exists(xp)) continue;
                try {
                    List<WebElement> elements = driver.findElements(By.xpath(xp));
                    for (WebElement el : elements) {
                        String txt = el.getText();
                        if (txt != null) txt = txt.trim();
                        if (txt != null && !txt.isEmpty() && (validator == null || validator.test(txt))) {
                            logger.info("获取到 " + fieldName + ": " + txt);
                            return txt;
                        }
                    }
                } catch (Exception e) {
                    logger.warning(fieldName + " 获取失败 [" + xp + "]: " + e.getMessage());
                }
            }
            sleep(1000);
        }
        return "";
    }

    public void sleep(long ms) {
        try { Thread.sleep(ms); } catch (Exception ignored) {}
    }


    // ⭐ 从 pageSource(XML) 中解析文本，而不再访问 driver
    public String findTextFromSource(String pageSource,
                                     String[] xpaths,
                                     java.util.function.Predicate<String> filter,
                                     String tag) {

        if (pageSource == null || pageSource.isEmpty()) return "";

        try {
            javax.xml.parsers.DocumentBuilderFactory factory =
                    javax.xml.parsers.DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);

            org.w3c.dom.Document doc = factory.newDocumentBuilder()
                    .parse(new java.io.ByteArrayInputStream(
                            pageSource.getBytes(java.nio.charset.StandardCharsets.UTF_8)));

            javax.xml.xpath.XPath xpath =
                    javax.xml.xpath.XPathFactory.newInstance().newXPath();

            for (String xp : xpaths) {
                try {
                    org.w3c.dom.Node node =
                            (org.w3c.dom.Node) xpath.evaluate(xp, doc, javax.xml.xpath.XPathConstants.NODE);

                    if (node == null) continue;

                    String text = "";

                    // 优先取 text 属性
                    if (node.getAttributes() != null &&
                            node.getAttributes().getNamedItem("text") != null) {
                        text = node.getAttributes().getNamedItem("text").getNodeValue();
                    } else {
                        text = node.getTextContent();
                    }

                    if (text != null) {
                        text = text.trim();
                        if (!text.isEmpty() && (filter == null || filter.test(text))) {
                            logger.info("[XML解析-" + tag + "] 命中: " + text);
                            return text;
                        }
                    }

                } catch (Exception ignore) {}
            }

        } catch (Exception e) {
            logger.warning("XML 解析失败: " + e.getMessage());
        }

        return "";
    }

}
