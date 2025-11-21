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

}
