package com.phone;

import com.alibaba.fastjson.JSON;
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

        String lockKey = "douyin:device:lock:ec86e946";
        redisTemplate.delete(lockKey);
        Boolean isRunning = redisTemplate.hasKey(lockKey);
        System.out.println(isRunning);
        System.out.println("🚀 Douyin Task Loop 启动成功！");
//
//        // 启动独立线程执行循环任务
//        new Thread(this::taskLoop, "douyin-task-loop-thread").start();

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


//    /**
//     * 主循环逻辑：无任务 → sleep 10 分钟；有任务 → 立即执行
//     */
//    private void taskLoop() {
//
//        while (true) {
//            try {
//                runOnce();
//            } catch (Exception e) {
//                System.out.println("❌ 任务循环异常：" + e.getMessage());
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    /**
//     * 单轮任务调度
//     */
//    /**
//     * 单轮任务调度（每次只处理 1 条）
//     */
//    private void runOnce() throws InterruptedException {
//
//        // 1️⃣ 查询全部 tags
//        List<VideoTags> list = videoTagsService.selectVideoTagsList(new VideoTags());
//
//        // 2️⃣ 找到一个 status != 1 的任务
//        VideoTags task = list.stream()
//                .filter(t -> !"1".equals(t.getStatus()))
//                .findFirst()
//                .orElse(null);
//
//        // 3️⃣ 如果没有任务 → 休眠 10 分钟
//        if (task == null) {
//            System.out.println("😴 当前无任务，10 分钟后再次检查...");
//            Thread.sleep(10 * 60 * 1000);
//            return;
//        }
//
//        // 4️⃣ 有任务 → 检查 Redis 锁
//        String devId = task.getDevId();
//        String lockKey = "douyin:device:lock:" + devId;
//
//        Boolean isRunning = redisTemplate.hasKey(lockKey);
//
//        if (Boolean.TRUE.equals(isRunning)) {
//            System.out.println("⛔ 设备 " + devId + " 正在执行任务，跳过 tagsId=" + task.getId());
//            return;  // ❗ 注意：只跳过，不 sleep，立即下一轮
//        }
//
//        // 5️⃣ 执行任务
//        System.out.println("🚀 开始执行任务 tagsId=" + task.getId() + " 设备=" + devId);
//
//        videoService.asyncCrawlTask(devId, task.getDouyinId(), task);
//
//        // 设置 24 小时锁
//        redisTemplate.opsForValue().set(
//                lockKey,
//                "1",
//                86400,
//                TimeUnit.SECONDS
//        );
//
//        System.out.println("🔒 已对设备 " + devId + " 设置 24 小时锁");
//
//        // ❗ 执行完这一条之后直接退出本轮
//    }

}
