package com.phone.module.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
    public AjaxResult getAccount(Video video) throws JsonProcessingException {
        AjaxResult ajax = AjaxResult.success();
        Account account = new Account();
        account.setDouyinId(video.getDouyinId());
        List<Account> lista = accountService.selectAccountList(account);
        if (lista.size() > 0) {
            account = lista.get(0);
            account.setCounts(accountContentService.selectAccountContentCountSum(account.getDouyinId()));
        }else {

            Comment comment = new Comment();
            comment.setUserUid(video.getDouyinId());

            List<Comment> listC = commentService.selectCommentList(comment);
            if (listC.size() > 0) {
                comment = listC.get(0);

                account.setDouyinId(comment.getUserUid());


// 2. 创建 JSON，严格使用你提供的原始字段名（下划线格式）
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode json = mapper.createObjectNode();

// ==============================
// 【严格对应你给的JSON原始字段名】
// ==============================
                json.put("id",           comment.getUserUid());     // 用户UID → id
                json.put("nickname",     comment.getNickname());    // 昵称
                json.put("gender",       comment.getGender());      // 性别
                json.put("age",          comment.getAge());         // 年龄
                json.put("real_name",    comment.getRealName());    // 真实姓名
                json.put("signature",    comment.getSignature());   // 签名
                json.put("fans_count",   comment.getFansCount());   // 粉丝数
                json.put("follow_count", comment.getFollowCount()); // 关注数
                json.put("likes_count",  comment.getLikesCount());  // 获赞数
                json.put("works_count",  comment.getWorksCount());  // 作品数
                json.put("profile_text", comment.getProfileText()); // 个人简介
                json.put("ip_location",  comment.getIpLocation());  // IP归属地
                json.put("region",       comment.getRegion());      // 地区
                json.put("touxiang",     comment.getTouxiang());    // 头像地址
                json.put("zhuye",        comment.getZhuye());       // 主页截图


//// 3. 转成 JSON 字符串
//                String jsonString = mapper.writeValueAsString(json);

// 4. 存入 account
                account.setJsonString(json.toString());
                account.setOldString(json.toString());

            }
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
