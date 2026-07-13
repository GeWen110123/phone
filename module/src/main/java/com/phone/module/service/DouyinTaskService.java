package com.phone.module.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phone.adb.DouyinCrawler;
import com.phone.common.utils.StringUtils;
import com.phone.module.domain.*;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class DouyinTaskService {

    private static final Logger logger = Logger.getLogger(DouyinTaskService.class.getName());

    @Autowired
    private IAccountService accountService;
    @Autowired
    @Lazy
    private DouyinTaskService douyinTaskService;

    @Autowired
    private IAccountContentService accountContentService;
    @Autowired
    private IAddressAccountContentService addressAccountContentService;
    @Autowired
    private IVideoService videoService;
    @Autowired
    private IVideoTagsService videoTagsService;
    @Autowired
    private IAccountIpService accountIpService;


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

            DouyinCrawler crawler = new DouyinCrawler(driver,
                    accountContentService,
                    addressAccountContentService,
                    accountService, douyinTaskService);

            if (!crawler.startDouyin()) {
                logger.warning("启动抖音失败: " + accountName);
            } else if (!crawler.searchAndEnterAccount(accountName)) {
                logger.warning("进入账号失败: " + accountName);
            } else {
                result = crawler.fetchAccountInfo("1","1", devId, accountName);
            }
            // 写入数据库
            storeAccountAsync(devId, accountName, result, "");

        } catch (Exception e) {
            logger.severe("爬取账号信息异常: " + e.getMessage());
        } finally {
            closeDriverQuietly(driver);
        }


    }

    // =====================================================
    // 检查设备是否在线且授权
    // =====================================================
    private boolean isDeviceOnline(String devId) {
        try {
            Process p = Runtime.getRuntime().exec("C:\\Android\\sdk\\platform-tools\\adb.exe devices");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith(devId) && line.endsWith("device")) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            logger.severe("检查设备状态失败: " + e.getMessage());
        }
        return false;
    }

    private void closeDriverQuietly(AndroidDriver<MobileElement> driver) {
        if (driver == null) {
            return;
        }

        try {
            driver.quit();
        } catch (Exception e) {
            logger.warning("关闭驱动时异常：" + e.getMessage());
        }
    }


    // =====================================================
    // 抖音账号信息对象 数据库写入（异步）
    // =====================================================
    public void storeAccountAsync(String devId, String accountName, Map<String, Object> resultMap, String tags) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(resultMap);

            Account account = new Account();
            account.setDevId(devId);
            account.setDouyinId(accountName);
            List<Account> list = accountService.selectAccountList(account);

            if (list.size() > 0) {
                account = list.get(0);
                account.setJsonString(json);
                account.setUpdateTime(new Date());
                accountService.updateAccount(account);

            } else {
                account.setJsonString(json);
                account.setCreateTime(new Date());


                Video video = new Video();
                video.setDouyinId(accountName);
                List<Video> listVoids = videoService.selectVideoList(video);
// ⭐ 把视频数量放进去（字段名你自己定）
                resultMap.put("video_count", listVoids.size());
// Map → JSON 字符串
                String newJson = mapper.writeValueAsString(resultMap);
// 如果需要回存
                account.setOldString(newJson); //存储上次获取到的信息
                account.setOldTime(account.getUpdateTime()); //存储上次获取到的时间


                accountService.insertAccount(account);

            }

            if (StringUtils.isNotEmpty((String) resultMap.get("id"))) {

                if (tags.contains("单用户前六条视频") || tags.contains("单用户所有视频")) {

                    VideoTags query = new VideoTags();
                    query.setDouyinId(accountName);
                    query.setTaskId(3L);

                    List<VideoTags> existList = videoTagsService.selectVideoTagsList(query);

                    // 已存在任务
                    if (!existList.isEmpty()) {

                        // 如果现在是“所有视频”，需要升级任务
                        if (tags.contains("单用户所有视频")) {
                            VideoTags exist = existList.get(0);
                            exist.setTags("单用户所有视频");
                            videoTagsService.updateVideoTags(exist);
                        }

                        return;
                    }

                    // 不存在任务才新增
                    VideoTags t = new VideoTags();
                    t.setDouyinId(accountName);
                    t.setDevId(devId);
                    t.setTaskId(3L);
                    t.setCreateTime(new Date());

                    if (tags.contains("单用户前六条视频")) {
                        t.setTags("视频,基本信息,评论,前六条");
                    } else if (tags.contains("单用户所有视频")) {
                        t.setTags("视频,基本信息,评论");
                    }
                    videoTagsService.insertVideoTags(t);
                }
            }



//          用户归属地单独表存储
            AccountIp ip = new AccountIp();
            ip.setDouyinId(accountName);
            String regionRaw = (String) resultMap.get("region");

            String region = "";
            if (regionRaw != null && !regionRaw.trim().isEmpty()) {
                region = regionRaw.replace("IP：", "").replace("IP:", "").trim();
            }

            ip.setIpAddress(region);
            accountIpService.insertAccountIp(ip);

            logger.info("账号数据写入成功: " + accountName);

        } catch (Exception ex) {
            logger.severe("DB 写入失败: " + ex.getMessage());
        }
    }


    // =====================================================
    // 视频评论账号信息对象 数据库写入（异步）
    // =====================================================
    public void storeAccountByAccount(String uid, String devId, String douyinId, String accountName, Map<String, Object> resultMap) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(resultMap);

            AccountContent account = new AccountContent();
            account.setDevId(devId);
            account.setDouyinId(douyinId);
            account.setAccountName(accountName);

            account.setvUid(uid);
            List<AccountContent> list = accountContentService.selectAccountContentList(account);

            if (list.size() > 0) {
                account = list.get(0);
//                account.setJsonString(json);
                account.setCount(account.getCount() + 1);
                account.setUpdateTime(new Date());
                accountContentService.updateAccountContent(account);
            } else {
                account.setJsonString(json);
                account.setCount(1l);
                account.setCreateTime(new Date());
                accountContentService.insertAccountContent(account);

            }

            logger.info("账号数据写入成功: " + accountName);

        } catch (Exception ex) {
            logger.severe("DB 写入失败: " + ex.getMessage());
        }
    }


    // =====================================================
    // 地址评论账号信息对象 数据库写入（异步）
    // =====================================================
    public void storeAccountByAddressAccount(String uid, String devId, String douyinId, String accountName, Map<String, Object> resultMap) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(resultMap);

            AddressAccountContent account = new AddressAccountContent();
            account.setDevId(devId);
            account.setDouyinId(douyinId);
            account.setAccountName(accountName);
            account.setaVId(uid);
            List<AddressAccountContent> list = addressAccountContentService.selectAddressAccountContentList(account);

            if (list.size() > 0) {
                account = list.get(0);
                account.setCount(account.getCount() + 1);
                account.setUpdateTime(new Date());
                addressAccountContentService.updateAddressAccountContent(account);
            } else {
                account.setJsonString(json);
                account.setCount(1l);
                account.setCreateTime(new Date());
                addressAccountContentService.insertAddressAccountContent(account);

            }

            logger.info("账号数据写入成功: " + accountName);

        } catch (Exception ex) {
            logger.severe("DB 写入失败: " + ex.getMessage());
        }
    }


    // =====================================================
    // 视频评论账号信息对象 数据库写入（异步）   主页点击查看到的关注人员
    // =====================================================
    public void storeAccountByFollow(String uid, String devId, String douyinId, String accountName, Map<String, Object> resultMap) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(resultMap);

            AccountContent account = new AccountContent();
            account.setDevId(devId);
            account.setDouyinId(douyinId);//查找的抖音id
            account.setAccountName(douyinId);//查找的抖音id
            account.setvUid(accountName);//关注人员id

            account.setJsonString(json);
            account.setCreateTime(new Date());
            accountContentService.insertFollow(account);

            logger.info("账号数据写入成功: " + accountName);

        } catch (Exception ex) {
            logger.severe("DB 写入失败: " + ex.getMessage());
        }
    }

    public void storeAccountByFans(String uid, String devId, String douyinId, String accountName, Map<String, Object> resultMap) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(resultMap);

            AccountContent account = new AccountContent();
            account.setDevId(devId);
            account.setDouyinId(douyinId);//查找的抖音id
            account.setAccountName(douyinId);//查找的抖音id
            account.setvUid(accountName);//关注人员id

            account.setJsonString(json);
            account.setCreateTime(new Date());
            accountContentService.insertFans(account);

            logger.info("账号数据写入成功: " + accountName);

        } catch (Exception ex) {
            logger.severe("DB 写入失败: " + ex.getMessage());
        }
    }


}
