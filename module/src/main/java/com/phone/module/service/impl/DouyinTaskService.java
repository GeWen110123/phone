package com.phone.module.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phone.adb.DouyinCrawler;
import com.phone.module.domain.Account;
import com.phone.module.service.IAccountService;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@Service
public class DouyinTaskService {

    private static final Logger logger = Logger.getLogger(DouyinTaskService.class.getName());

    @Autowired
    private IAccountService accountService;

    private final ExecutorService dbExecutor = Executors.newFixedThreadPool(3);

    // =====================================================
    // ⭐ 改写 runTask —— 自动检测设备在线和授权状态
    // =====================================================
    @Async
    public void runTask(String devId, String accountName) {

        // 先检查设备是否在线且授权
        if (!isDeviceOnline(devId)) {
            logger.warning("设备未在线或未授权: '" + devId + "'");
            return;
        }

        Map<String, Object> result = new HashMap<>();
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

            DouyinCrawler crawler = new DouyinCrawler(driver);

            if (!crawler.startDouyin()) {
                logger.warning("启动抖音失败: " + accountName);
            } else if (!crawler.searchAndEnterAccount(accountName)) {
                logger.warning("进入账号失败: " + accountName);
            } else {
                result = crawler.fetchAccountInfo();
            }
            // 写入数据库
            storeAccountAsync(devId, accountName, result);

        } catch (Exception e) {
            logger.severe("爬取账号信息异常: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) driver.quit();
        }


    }

    // =====================================================
    // 检查设备是否在线且授权
    // =====================================================
    private boolean isDeviceOnline(String devId) {
        try {
            Process p = Runtime.getRuntime().exec("C:\\Android\\sdk\\platform-tools\\adb.exe devices");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith(devId) && line.endsWith("device")) {
                    return true;
                }
            }
        } catch (IOException e) {
            logger.severe("检查设备状态失败: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // =====================================================
    // 数据库写入（异步）
    // =====================================================
    private void storeAccountAsync(String devId, String accountName, Map<String, Object> resultMap) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(resultMap);

            Account account = new Account();
            account.setDevId(devId);
            account.setDouyinId(accountName);
            account.setJsonString(json);
            account.setCreateTime(new Date());
            try {
                accountService.insertAccount(account);
            } catch (Exception e) {
                account.setUpdateTime(new Date());
                accountService.updateAccount(account);
            }

            logger.info("账号数据写入成功: " + accountName);

        } catch (Exception ex) {
            logger.severe("DB 写入失败: " + ex.getMessage());
        }
    }
}
