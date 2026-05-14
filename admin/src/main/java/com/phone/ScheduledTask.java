package com.phone;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phone.common.core.redis.RedisCache;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.phone.common.utils.StringUtils;
import com.phone.module.domain.*;
import com.phone.module.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;


@EnableScheduling
@Configuration
public class ScheduledTask {
    @Autowired
    private IStrategyTemplateService strategyTemplateService;
    @Autowired
    private IStrategyLogService strategyLogService;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IVideoTagsService videoTagsService;
    @Autowired
    private DouyinVideoService douyinVideoService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IVideoService videoService;
    @Autowired
    private IRunLogService runLogService;
    @Autowired
    private ICommentService commentService;
    @Autowired
    private IAddressVideoService addressVideoService;

    /**
     * 策略执行表
     */
    @Scheduled(cron = "0 0 0 * * ?")
    private void configureTasks() {
        StrategyTemplate strategyTemplate = strategyTemplateService.selectStrategyTemplateById(1l);
        Set<String> devIdList = new HashSet<>();
        if (StringUtils.isNotEmpty(strategyTemplate.getAllDevIds())) {
            devIdList.addAll(Arrays.asList(strategyTemplate.getAllDevIds().split(",")));
        }

        // 将 List<String> 转换为 List<Long>
        List<Long> devIdLongList = devIdList.stream()
                .map(devId -> Long.parseLong(devId.trim()))  // 转换为 Long
                .collect(Collectors.toList());

        // 遍历 List<Long> 执行操作
        for (Long devId : devIdLongList) {
            // 处理 Long 类型的 devId
            System.out.println("设备 ID: " + devId);


            // 处理 Long 类型的 devId
            Device device = deviceService.selectDeviceById(devId);
            System.out.println("设备 ID: " + devId);
            StrategyLog taskLog = new StrategyLog();
            taskLog.setExecuteTime(new Date());
            taskLog.setStrategyId(strategyTemplate.getId());
            taskLog.setTargetId(devId);
            taskLog.setStatus("success");//为返回结果数据
            taskLog.setLogDetail("设备" + device.getDeviceName() + "进行:" + strategyTemplate.getStrategyName() + "任务操作");

            strategyLogService.insertStrategyLog(taskLog);

        }
    }


    /**
     * 视频执行表
     */
    @Scheduled(cron = "0 */3 * * * ?")
    private void douyinTasks() throws JsonProcessingException {
        VideoTags videoTags = new VideoTags();
        videoTags.setStatus("0");
        List<VideoTags> list = videoTagsService.selectVideoTagsListASC(videoTags);

        for (VideoTags tags : list) {
            if (!"0".equals(tags.getStatus())) {
                continue;
            }
            if (tags.getRunNum() >= 30) {
                // 标记为完成状态
                tags.setStatus("2");
                videoTagsService.updateVideoTags(tags);
                continue;
            }

            String devId = tags.getDevId();
            String lockKey = "douyin:device:lock:" + devId;
            String lockValue = UUID.randomUUID().toString();

            Boolean locked = false;

            try {
                // 🔒 只做一次 Redis 原子操作
                locked = redisTemplate.opsForValue()
                        .setIfAbsent(lockKey, lockValue, 2, TimeUnit.HOURS);

            } catch (Exception e) {
                // Redis 异常不能影响调度线程
                System.out.println("⚠ Redis异常，跳过设备: " + devId);
                continue;
            }

            if (!Boolean.TRUE.equals(locked)) {
                System.out.println("⛔ 设备占用中: " + devId);
                continue;   // ❗ 不能 return
            }

            System.out.println("🚀 设备锁成功，开始任务 tagsId=" + tags.getId());
            System.out.println("🔒 已使用 RedisTemplate 对设备加锁：" + devId);


            RunLog runLog = new RunLog();
            runLog.setDevId(devId);
            runLog.setDouyinId(tags.getDouyinId());
            runLog.setType("启动抖音获取数据");
            runLog.setRuningDetail("启动抖音获取" + tags.getDouyinId() + "数据");
            runLogService.insertRunLog(runLog);

            // 异步执行
            douyinVideoService.asyncCrawlTask(devId, tags.getDouyinId(), tags, lockKey, locked);

            // 标记执行中（先改状态，防止并发）
            tags.setStatus("1");
            tags.setRunNum(tags.getRunNum() + 1);
            if (StringUtils.isNull(tags.getTaskId())) {
                tags.setTaskId(3L);
            }
            videoTagsService.updateVideoTags(tags);


            updateAccountCache(tags.getDevId(), tags.getDouyinId());
        }

        // =============================
        // ⭐ 统一兜底机制（循环外）
        // =============================
        recoverAbnormalTasks();
    }

    /**
     * 无可执行任务，恢复异常任务为待执行
     */
    private void recoverAbnormalTasks() {

        List<VideoTags> listStatus = videoTagsService.selectVideoTagsList(new VideoTags());

        boolean hasRunnable = listStatus.stream()
                .anyMatch(v -> "0".equals(v.getStatus()) || "1".equals(v.getStatus()));

        if (!hasRunnable) {
            System.out.println("♻ 无可执行任务，恢复异常任务为待执行");

            listStatus.stream()
                    .filter(v -> "3".equals(v.getStatus()))
                    .forEach(v -> {
                        v.setStatus("0");
                        videoTagsService.updateVideoTags(v);
                    });
        }
    }

    /**
     * 回存 把视频数量放进去
     */
    private void updateAccountCache(String devId, String douyinId) throws JsonProcessingException {


        Account account = new Account();
        account.setDevId(devId);
        account.setDouyinId(douyinId);
        List<Account> listAcc = accountService.selectAccountList(account);

        if (listAcc.size() > 0) {
            account = listAcc.get(0);
            ObjectMapper mapper = new ObjectMapper();
            Video video = new Video();
            video.setDouyinId(douyinId);
            List<Video> listVoids = videoService.selectVideoList(video);
            Map<String, Object> map = mapper.readValue(
                    account.getJsonString(),
                    new TypeReference<Map<String, Object>>() {
                    }
            );
// 把视频数量放进去
            map.put("video_count", listVoids.size());
// Map → JSON 字符串
            String newJson = mapper.writeValueAsString(map);
// 如果需要回存
            account.setOldString(newJson); //存储上次获取到的信息
            account.setOldTime(account.getUpdateTime()); //存储上次获取到的时间
            accountService.updateAccount(account);
        }
    }


    // 开机执行一次
    @PostConstruct
    public void runOnStartup() throws JsonProcessingException {
        getDouyinidToComment();
    }

    // 30分钟执行一次
    @Scheduled(cron = "0 */30 * * * ?")
    public void getJson() throws JsonProcessingException {
        getJsonToMysql();
    }

    /**
     * 视频执行表
     */
    private void getDouyinidToComment() throws JsonProcessingException {
        while (true) {

            List<Comment> list = commentService.selectCommentList200(new Comment());

            if (list == null || list.size() == 0) {
                break;
            }

            for (Comment c : list) {
                Video v = videoService.selectVideoByUId(c.getUid());

                if (StringUtils.isNotNull(v)) {
                    c.setUserDouyin(v.getDouyinId());
                } else {
                    AddressVideo av = addressVideoService.selectAddressVideoByUid(c.getUid());
                    if (StringUtils.isNotNull(av)) {
                        c.setUserDouyin(av.getDouyinId());
                    }
                }
                commentService.updateComment(c);
            }
        }

        System.out.println("结束了");
    }

    private void getJsonToMysql() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        while (true) {

            List<Video> list = videoService.selectVideoList200(new Video());

            if (list == null || list.size() == 0) {
                break;
            }

            for (Video v : list) {

                String content = v.getContent();
                if (content == null || content.equals("") || content.equals("[]")) {
                    continue;
                }

                JsonNode array = mapper.readTree(content);

                for (JsonNode node : array) {

                    Comment c = new Comment();

                    c.setUid(v.getUid());
                    c.setUserDouyin(v.getDouyinId());

                    c.setUserUid(node.path("user_uid").asText());
                    c.setNickname(node.path("nickname").asText());
                    c.setGender(node.path("gender").asText());
                    c.setAge(node.path("age").asText());

                    c.setRealName(node.path("real_name").asText());
                    c.setSignature(node.path("signature").asText());

                    c.setFansCount(node.path("fans_count").asText());
                    c.setFollowCount(node.path("follow_count").asText());
                    c.setLikesCount(node.path("likes_count").asText());
                    c.setWorksCount(node.path("works_count").asText());

                    c.setProfileText(node.path("profile_text").asText());

                    c.setIpLocation(node.path("ip_location").asText());
                    c.setRegion(node.path("region").asText());

                    c.setTouxiang(node.path("touxiang").asText());
                    c.setZhuye(node.path("zhuye").asText());

                    c.setCommentText(node.path("text").asText());
                    c.setReplyTo(node.path("replyTo").asText());
                    c.setCommentTime(node.path("time").asText());

                    c.setStatus("0");

                    commentService.insertComment(c);
                }

                // 清空content避免重复解析
                v.setContent("");
                videoService.updateVideo(v);
            }
        }


        // 第二个表
        while (true) {

            List<AddressVideo> list1 = addressVideoService.selectAddressVideoList200(new AddressVideo());

            if (list1 == null || list1.size() == 0) {
                break;
            }

            for (AddressVideo v : list1) {

                String content = v.getContent();
                if (content == null || content.equals("") || content.equals("[]")) {
                    continue;
                }

                JsonNode array = mapper.readTree(content);

                for (JsonNode node : array) {

                    Comment c = new Comment();

                    c.setUid(v.getUid());
                    c.setUserDouyin(v.getDouyinId());
                    c.setUserUid(node.path("user_uid").asText());
                    c.setNickname(node.path("nickname").asText());
                    c.setGender(node.path("gender").asText());
                    c.setAge(node.path("age").asText());

                    c.setRealName(node.path("real_name").asText());
                    c.setSignature(node.path("signature").asText());

                    c.setFansCount(node.path("fans_count").asText());
                    c.setFollowCount(node.path("follow_count").asText());
                    c.setLikesCount(node.path("likes_count").asText());
                    c.setWorksCount(node.path("works_count").asText());

                    c.setProfileText(node.path("profile_text").asText());

                    c.setIpLocation(node.path("ip_location").asText());
                    c.setRegion(node.path("region").asText());

                    c.setTouxiang(node.path("touxiang").asText());
                    c.setZhuye(node.path("zhuye").asText());

                    c.setCommentText(node.path("text").asText());
                    c.setReplyTo(node.path("replyTo").asText());
                    c.setCommentTime(node.path("time").asText());

                    c.setStatus("0");

                    commentService.insertComment(c);
                }

                v.setContent("");
                addressVideoService.updateAddressVideo(v);
            }
        }


        System.out.println("结束了");
    }
}
