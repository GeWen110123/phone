package com.phone.module.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.phone.common.utils.StringUtils;
import com.phone.module.domain.*;
import com.phone.module.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.phone.common.annotation.Log;
import com.phone.common.core.controller.BaseController;
import com.phone.common.core.domain.AjaxResult;
import com.phone.common.enums.BusinessType;
import com.phone.common.utils.poi.ExcelUtil;
import com.phone.common.core.page.TableDataInfo;

/**
 * VideoTagsController
 *
 * @author ruoyi
 * @date 2025-12-08
 */
@RestController
@RequestMapping("/module/VideoTags")
public class VideoTagsController extends BaseController {
    @Autowired
    private IVideoTagsService videoTagsService;
    @Autowired
    private IAccountService accountService;

    @Autowired
    private IAccountContentService accountContentService;
    @Autowired
    private IVideoService videoService;
    @Autowired
    private IAddressVideoService addressVideoService;

    /**
     * 查询VideoTags列表
     */
    @GetMapping("/list")
    public TableDataInfo list(VideoTags videoTags) {
        startPage();
        List<VideoTags> list = videoTagsService.selectVideoTagsList(videoTags);
        for (VideoTags tags : list) {
            Account account = new Account();
            account.setDouyinId(tags.getDouyinId());
            List<Account> lista = accountService.selectAccountList(account);
            if (lista.size() > 0) {
                account = lista.get(0);
            }

            tags.setAccount(account);

            if (!tags.getTags().contains("地址")) {
                Video video= new Video();
                video.setDouyinId(tags.getDouyinId());
                tags.setCounts(accountContentService.selectAccountContentCountSum(tags.getDouyinId()));
                tags.setVideoCount(videoService.selectVideoList(video).size());
            }else {
                AddressVideo video= new AddressVideo();
                video.setAddress(tags.getDouyinId());
                tags.setVideoCount(addressVideoService.selectAddressVideoList(video).size());
            }
        }
        return getDataTable(list);
    }

    /**
     * 导出VideoTags列表
     */
    @Log(title = "VideoTags", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(VideoTags videoTags) {
        List<VideoTags> list = videoTagsService.selectVideoTagsList(videoTags);
        ExcelUtil<VideoTags> util = new ExcelUtil<VideoTags>(VideoTags.class);
        util.exportExcel(list, "VideoTags数据");
    }

    /**
     * 获取VideoTags详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(videoTagsService.selectVideoTagsById(id));
    }


    @Autowired
    private IRunLogService runLogService;
    /**
     * 新增VideoTags
     */
    @Log(title = "VideoTags", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody VideoTags videoTags) {
        String douyinId = videoTags.getDouyinId();
// 转为 List<String>
        List<String> douyinIdList = Arrays.stream(douyinId.split(","))
                .map(String::trim)     // 去空格
                .filter(s -> !s.isEmpty())   // 过滤空字符串
                .collect(Collectors.toList());

        System.out.println(douyinIdList);
        for (String name : douyinIdList) {
            VideoTags tags = new VideoTags();
            tags.setDouyinId(name);
            List<VideoTags> list = videoTagsService.selectVideoTagsList(tags);
            if (list.size()>0){
                VideoTags tags1 = list.get(0);
                tags1.setDevId(videoTags.getDevId());
                tags1.setTags(videoTags.getTags());
                tags1.setStatus("0");
                videoTagsService.updateVideoTags(tags1);
            }else {
                tags.setDevId(videoTags.getDevId());
                tags.setTags(videoTags.getTags());
                videoTagsService.insertVideoTags(tags);
            }

            RunLog runLog = new RunLog();
            runLog.setDevId(videoTags.getDevId());
            runLog.setDouyinId(name);
            if (StringUtils.isNotEmpty(videoTags.getUserId())){

                runLog.setRuningDetail("新增抖音"+tags.getUserId()+"数据到"+name+"任务列表");

            }else{
                runLog.setRuningDetail("新增抖音"+name+"数据到任务列表");
            }
            runLog.setType("新增");
            runLogService.insertRunLog(runLog);

        }



        return AjaxResult.success();
    }

    /**
     * 修改VideoTags
     */
    @Log(title = "VideoTags", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VideoTags videoTags) {
        return toAjax(videoTagsService.updateVideoTags(videoTags));
    }
    @Autowired
    private IVideoTaskService videoTaskService;
    /**
     * 删除VideoTags
     */
    @Log(title = "VideoTags", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {

        for (Long id : ids) {
            // ===== 1. 查当前 tag =====
            VideoTags videoTags = videoTagsService.selectVideoTagsById(id);
            if (videoTags == null) {
                continue;
            }
            Long taskId = videoTags.getTaskId();

            // ===== 2. 删除 tag =====
            videoTagsService.deleteVideoTagsById(id);

            // ===== 3. 查询该 task 剩余 tags =====
            VideoTags ta = new VideoTags();
            ta.setTaskId(taskId);
            List<VideoTags> newTags = videoTagsService.selectVideoTagsList(ta);

            // ===== 4. 拼接 douyinId =====
            String douyinIds = newTags.stream()
                    .map(VideoTags::getDouyinId)
                    .filter(s -> s != null && !s.isEmpty())
                    .distinct() // 去重（建议）
                    .collect(Collectors.joining(","));

            // ===== 5. 更新 task =====
            VideoTask task = videoTaskService.selectVideoTaskById(taskId);
            if (task != null) {
                task.setDouyinId(douyinIds);
                videoTaskService.updateVideoTask(task);
            }
        }

        return AjaxResult.success();
    }
}
