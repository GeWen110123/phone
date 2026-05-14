package com.phone.module.controller;

import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.phone.common.utils.StringUtils;
import com.phone.module.domain.VideoTags;
import com.phone.module.service.IVideoTagsService;
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
import com.phone.module.domain.VideoTask;
import com.phone.module.service.IVideoTaskService;
import com.phone.common.utils.poi.ExcelUtil;
import com.phone.common.core.page.TableDataInfo;

/**
 * video_taskController
 * 
 * @author ruoyi
 * @date 2025-12-09
 */
@RestController
@RequestMapping("/module/video_task")
public class VideoTaskController extends BaseController
{
    @Autowired
    private IVideoTaskService videoTaskService;
    @Autowired
    private IVideoTagsService videoTagsService;
    /**
     * 查询video_task列表
     */
    @GetMapping("/list")
    public TableDataInfo list(VideoTask videoTask)
    {
        startPage();
        List<VideoTask> list = videoTaskService.selectVideoTaskList(videoTask);
        for (VideoTask task :list){
            VideoTags videoTags = new VideoTags();
            videoTags.setTaskId(task.getId());
            List<VideoTags> listTags = videoTagsService.selectVideoTagsList(videoTags);
            int unFinish = 0;
            int finish   = 0;
            int error    = 0;

            for (VideoTags tag : listTags) {
                String status = tag.getStatus();

                if ("1".equals(status)||"0".equals(status)) {
                    unFinish++;
                } else if ("2".equals(status)) {
                    finish++;
                } else if ("3".equals(status)) {
                    error++;
                }
            }

            String summary = "未完成" + unFinish + "条"
                    + " / 已完成" + finish + "条"
                    + " / 异常" + error + "条";

            task.setStatusSummary(summary);
        }
        return getDataTable(list);
    }

    /**
     * 导出video_task列表
     */
    @Log(title = "video_task", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, VideoTask videoTask)
    {
        List<VideoTask> list = videoTaskService.selectVideoTaskList(videoTask);
        ExcelUtil<VideoTask> util = new ExcelUtil<VideoTask>(VideoTask.class);
        util.exportExcel( list, "video_task数据");
    }

    /**
     * 获取video_task详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(videoTaskService.selectVideoTaskById(id));
    }


    /**
     * 新增video_task
     */
    @Log(title = "video_task", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody VideoTask videoTask)
    {
        videoTask.setCreateTime(new Date());
        videoTaskService.insertVideoTask(videoTask);
        if (StringUtils.isNotEmpty(videoTask.getDouyinId())) {

            List<String> douyinIdList = Arrays.stream(videoTask.getDouyinId().split(","))
                    .map(String::trim)     // 去空格
                    .filter(s -> !s.isEmpty())   // 过滤空字符串
                    .collect(Collectors.toList());


            for (String name :douyinIdList){
                VideoTags tags = new VideoTags();
                tags.setDevId(videoTask.getDevId());
                tags.setTags(videoTask.getTags());
                tags.setDouyinId(name);
                tags.setTaskId(videoTask.getId());
                tags.setTaskName(videoTask.getTaskName());
                videoTagsService.insertVideoTags(tags);
            }
        }

        return AjaxResult.success();
    }

    /**
     * 修改video_task
     */
    @Log(title = "video_task", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VideoTask videoTask)
    {
        videoTaskService.updateVideoTask(videoTask);

        VideoTags ta = new VideoTags();
        ta.setTaskId(videoTask.getId());

        // ===== 1. 查询旧数据 =====
        List<VideoTags> oldTags = videoTagsService.selectVideoTagsList(ta);

        // ===== 2. 如果前端清空 =====
        if (StringUtils.isEmpty(videoTask.getDouyinId())) {
            for (VideoTags tags : oldTags) {
                videoTagsService.deleteVideoTagsById(tags.getId());
            }
            return AjaxResult.success();
        }

        // ===== 3. 新数据（去重）=====
        Set<String> newSet = Arrays.stream(videoTask.getDouyinId().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        // ===== 4. 旧数据 =====
        Map<String, VideoTags> oldMap = oldTags.stream()
                .filter(t -> t.getDouyinId() != null)
                .collect(Collectors.toMap(VideoTags::getDouyinId, t -> t, (a, b) -> a));

        Set<String> oldSet = oldMap.keySet();

        // ===== 5. 删除（old - new）=====
        for (String oldId : oldSet) {
            if (!newSet.contains(oldId)) {
                VideoTags tags = oldMap.get(oldId);
                videoTagsService.deleteVideoTagsById(tags.getId());
            }
        }

        // ===== 6. 新增（new - old）=====
        for (String newId : newSet) {
            if (!oldSet.contains(newId)) {
                VideoTags tags = new VideoTags();
                tags.setDevId(videoTask.getDevId());
                tags.setTags(videoTask.getTags());
                tags.setDouyinId(newId);
                tags.setTaskId(videoTask.getId());
                tags.setTaskName(videoTask.getTaskName());
                videoTagsService.insertVideoTags(tags);
            }
        }

        // ===== 7. 更新已有数据（tags/taskName变化）=====
        for (VideoTags tags : oldTags) {
            if (newSet.contains(tags.getDouyinId())) {
                tags.setTags(videoTask.getTags());
                tags.setTaskName(videoTask.getTaskName());
                tags.setDevId(videoTask.getDevId());
                videoTagsService.updateVideoTags(tags);
            }
        }

        return AjaxResult.success();
    }
    /**
     * 删除video_task
     */
    @PreAuthorize("@ss.hasPermi('module:video_task:remove')")
    @Log(title = "video_task", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(videoTaskService.deleteVideoTaskByIds(ids));
    }
}
