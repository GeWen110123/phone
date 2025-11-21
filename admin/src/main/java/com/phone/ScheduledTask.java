package com.phone;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.phone.common.core.redis.RedisCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.phone.common.utils.StringUtils;
import com.phone.module.domain.Device;
import com.phone.module.domain.StrategyLog;
import com.phone.module.domain.StrategyTemplate;
import com.phone.module.domain.TaskLog;
import com.phone.module.service.IDeviceService;
import com.phone.module.service.IStrategyLogService;
import com.phone.module.service.IStrategyTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@EnableScheduling
@Configuration
public class ScheduledTask {
    @Autowired
    private IStrategyTemplateService strategyTemplateService;
    @Autowired
    private IStrategyLogService strategyLogService;
    @Autowired
    private IDeviceService deviceService;


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





}
