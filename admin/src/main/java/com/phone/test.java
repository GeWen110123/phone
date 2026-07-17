package com.phone;

import com.alibaba.fastjson.JSON;
import com.phone.adb.PlaceMockLocationHelper;
import com.phone.module.domain.Account;
import com.phone.module.domain.AccountIp;
import com.phone.module.domain.AddressVideo;
import com.phone.module.domain.VideoTags;
import com.phone.module.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Order(1)
@Component
public class test implements ApplicationRunner {

    @Autowired
    private IVideoTagsService videoTagsService;

    @Autowired
    private DouyinVideoService videoService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IAddressVideoService addressVideoService;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IAccountIpService accountIpService;

    @Override
    public void run(ApplicationArguments args) {

//        项目启动时清楚全部相关的devid
        String lockKey = "douyin:device:lock:ec86e946";
        redisTemplate.delete(lockKey);
        Boolean isRunning = redisTemplate.hasKey(lockKey);
        System.out.println(isRunning);
        System.out.println("🚀 Douyin Task Loop 启动成功！");

        PlaceMockLocationHelper.ensureInstalledOnConnectedDevices();
        getFollowCount();
        getIpAddress();
    }

    private void getFollowCount() {

        List<AddressVideo> list = addressVideoService.selectAllVideoDId(new AddressVideo());

        if (list == null || list.isEmpty()) {
            return;
        }

        for (AddressVideo addressVideo : list) {

            if (addressVideo == null || addressVideo.getDouyinId() == null) {
                continue;
            }

            Account account = accountService.selectByDouyinId(addressVideo.getDouyinId());
            if (account == null || account.getJsonString() == null) {
                continue;
            }

            Map<String, Object> map;
            try {
                map = JSON.parseObject(account.getJsonString());
            } catch (Exception e) {
                continue;
            }

            long followCount = parseLong(map.get("follow_count"));
            long fansCount = parseLong(map.get("fans_count"));
            long likesCount = parseLong(map.get("likes_count"));
            String regionRaw = (String) map.get("region");

            String region = "";
            if (regionRaw != null && !regionRaw.trim().isEmpty()) {
                region = regionRaw.replace("IP：", "").replace("IP:", "").trim();
            }


            addressVideo.setFollowCount(followCount);
            addressVideo.setFansCount(fansCount);
            addressVideo.setLikesCount(likesCount);
            addressVideo.setIpAddress(region);


            addressVideoService.updateAddressVideo(addressVideo);
        }


    }

    private void getIpAddress() {

        List<AccountIp> list = accountIpService.selectAccountIpList(new AccountIp());

        if (list == null || list.isEmpty()) {
            return;
        }

        for (AccountIp addressVideo : list) {

            if (addressVideo == null || addressVideo.getDouyinId() == null) {
                continue;
            }

            Account account = accountService.selectByDouyinId(addressVideo.getDouyinId());
            if (account == null || account.getJsonString() == null) {
                continue;
            }

            Map<String, Object> map;
            try {
                map = JSON.parseObject(account.getJsonString());
            } catch (Exception e) {
                continue;
            }


            String regionRaw = (String) map.get("region");

            String region = "";
            if (regionRaw != null && !regionRaw.trim().isEmpty()) {
                region = regionRaw.replace("IP：", "").replace("IP:", "").trim();
            }

            addressVideo.setIpAddress(region);

            accountIpService.updateAccountIp(addressVideo);
        }


    }

    private long parseLong(Object value) {
        if (value == null) return 0L;

        String str = value.toString().trim();
        if (str.isEmpty()) return 0L;

        try {
            // 纯数字
            if (str.matches("^\\d+$")) {
                return Long.parseLong(str);
            }

            // 万
            if (str.contains("万")) {
                str = str.replace("万", "");
                double num = Double.parseDouble(str);
                return (long) (num * 10_000);
            }

            // 亿
            if (str.contains("亿")) {
                str = str.replace("亿", "");
                double num = Double.parseDouble(str);
                return (long) (num * 100_000_000);
            }

            // 兜底
            return (long) Double.parseDouble(str);

        } catch (Exception e) {
            return 0L;
        }
    }
}
