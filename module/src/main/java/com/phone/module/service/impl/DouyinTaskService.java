

package com.phone.module.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phone.adb.DouyinCrawler;
import com.phone.module.domain.Account;
import com.phone.module.service.IAccountService;
import io.appium.java_client.android.AndroidDriver;
//import io.appium.java_client.android.options.UiAutomator2Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


import org.openqa.selenium.remote.DesiredCapabilities;



@Service
public class DouyinTaskService {

    private static final Logger logger = Logger.getLogger(DouyinTaskService.class.getName());

    @Autowired
    private IAccountService accountService;

    private final ExecutorService dbExecutor = Executors.newFixedThreadPool(3);

    // =====================================================
    // ⭐ 重写 runTask —— 使用匿名内部类执行爬虫逻辑
    // =====================================================
    public void runTask(String devId, String accountName) {

        Map<String, Object> info = new Object() {

            public Map<String, Object> crawl() {
                Map<String, Object> result = new HashMap<>();
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

                    if (!crawler.startDouyin()) return result;
                    if (!crawler.searchAndEnterAccount(accountName)) return result;

                    result = crawler.fetchAccountInfo();

                    return result;

                } catch (Exception e) {
                    e.printStackTrace();
                    return result;
                } finally {
                    if (driver != null) driver.quit();
                }
            }

        }.crawl();

        storeAccountAsync(devId, accountName, info);
    }


    // =====================================================
    // 数据库写入（保持不变）
    // =====================================================
    private void storeAccountAsync(String devId, String accountName, Map<String, Object> resultMap) {

        dbExecutor.submit(() -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(resultMap);

                Account account = new Account();
                account.setDevId(devId);
                account.setDouyinId(accountName);
                account.setJsonString(json);
                account.setUpdateTime(new Date());

                try {
                    accountService.insertAccount(account);
                } catch (Exception e) {
                    accountService.updateAccount(account);
                }

                logger.info("账号数据写入成功: " + accountName);

            } catch (Exception ex) {
                logger.severe("DB 写入失败: " + ex.getMessage());
            }
        });
    }
}


