package com.phone.module.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phone.common.annotation.Log;
import com.phone.common.core.controller.BaseController;
import com.phone.common.core.domain.AjaxResult;
import com.phone.common.core.page.TableDataInfo;
import com.phone.common.enums.BusinessType;
import com.phone.common.utils.StringUtils;
import com.phone.common.utils.poi.ExcelUtil;
import com.phone.module.domain.*;
import com.phone.module.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * videoController
 *
 * @author ruoyi
 * @date 2025-12-08
 */
@RestController
@RequestMapping("/module/video")
public class VideoController extends BaseController {
    @Autowired
    private IVideoService videoService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private ICommentService commentService;
    /**
     * 查询video列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Video video) throws JsonProcessingException {
        startPage();
        List<Video> list = videoService.selectVideoList(video);
        if (StringUtils.isNotEmpty(video.getAddress())) {
            for (Video v : list) {
                Account account = new Account();
                account.setDouyinId(v.getDouyinId());
                List<Account> lista = accountService.selectAccountList(account);
                if (lista.size() > 0) {
                    account = lista.get(0);
                    v.setUserJson(account);
                }
            }
        }

        for (Video v:list){

            ObjectMapper mapper = new ObjectMapper();

            Comment comment = new Comment();
            comment.setUid(v.getUid());

            List<Comment> listC = commentService.selectCommentList(comment);

// List 转 JSON
            String json = mapper.writeValueAsString(listC);

// 放入 content
            v.setContent(json);
        }


        return getDataTable(list);
    }

    @Autowired
    private IAccountContentService accountContentService;
    @Autowired
    private IVideoTagsService videoTagsService;

    @GetMapping("/getAccount")
    public AjaxResult getAccount(Video video) {
        AjaxResult ajax = AjaxResult.success();
        Account account = new Account();
        account.setDouyinId(video.getDouyinId());
        List<Account> lista = accountService.selectAccountList(account);
        if (lista.size() > 0) {
            account = lista.get(0);
            account.setCounts(accountContentService.selectAccountContentCountSum(account.getDouyinId()));
        }

//        ==============================================
        List<AccountContent> listFollow = accountContentService.selectFollowList(video.getDouyinId());
        List<AccountContent> listFans = accountContentService.selectFansList(video.getDouyinId());

        if (listFollow.size()>0){
            account.setFollowStatus("1");
        }else {
            account.setFollowStatus("0");
        }
        if (listFans.size()>0){
            account.setFansStatus("1");
        }else {
            account.setFansStatus("0");
        }

        Comment comment = new Comment();
        comment.setUserDouyin(account.getDouyinId());
        comment.setStatus("1");
        List<Comment> listComment = commentService.selectCommentListByUser(comment);
        account.setCommentCount(listComment.size());



        VideoTags tags =new VideoTags();
        tags.setDouyinId(account.getDouyinId());
        List<VideoTags> listTags = videoTagsService.selectVideoTagsList(tags);
        if (listTags.size()>0){
            account.setTags(listTags.get(0).getTags());
        }

//        ==============================================
        ajax.put("account", account);
        return ajax;
    }

    /**
     * 导出video列表
     */
    @Log(title = "video", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(Video video) {
        List<Video> list = videoService.selectVideoList(video);
        ExcelUtil<Video> util = new ExcelUtil<Video>(Video.class);
        util.exportExcel(list, "video数据");
    }

    /**
     * 获取video详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(videoService.selectVideoById(id));
    }

    /**
     * 新增video
     */
    @Log(title = "video", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Video video) {
        return toAjax(videoService.insertVideo(video));
    }

    /**
     * 修改video
     */
    @Log(title = "video", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Video video) {
        return toAjax(videoService.updateVideo(video));
    }

    /**
     * 删除video
     */
    @Log(title = "video", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(videoService.deleteVideoByIds(ids));
    }
}
