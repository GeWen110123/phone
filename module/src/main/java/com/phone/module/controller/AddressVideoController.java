package com.phone.module.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * 地址视频总Controller
 *
 * @author ruoyi
 * @date 2026-01-09
 */
@RestController
@RequestMapping("/module/AddressVideo")
public class AddressVideoController extends BaseController {
    @Autowired
    private IAddressVideoService addressVideoService;
    @Autowired
    private IAccountService accountService;

    @Autowired
    private IVideoTagsService videoTagsService;

    @Autowired
    private ICommentService commentService;
    @Autowired
    private IVideoService videoService;

    /**
     * 查询地址视频总列表
     */
    @GetMapping("/list")
    public TableDataInfo list(AddressVideo addressVideo) throws JsonProcessingException {
        startPage();
        List<AddressVideo> list = addressVideoService.selectAddressVideoList(addressVideo);
        if (StringUtils.isNotEmpty(addressVideo.getAddress())) {
            for (AddressVideo v : list) {
                Account account = new Account();
                account.setDouyinId(v.getDouyinId());
                List<Account> lista = accountService.selectAccountList(account);
                if (lista.size() > 0) {
                    account = lista.get(0);
                    v.setUserJson(account);
                }

                VideoTags tags = new VideoTags();
                tags.setDouyinId(v.getDouyinId());
                List<VideoTags> listTags = videoTagsService.selectVideoTagsList(tags);
                if (listTags.size() > 0) {
                    v.setStatus(listTags.get(0).getStatus());
                } else {
                    v.setStatus("9");
                }


                Video video = new Video();
                video.setDouyinId(v.getDouyinId());
                List<Video> listVideo = videoService.selectVideoList(video);
                if ("0".equals(v.getStatus())||"9".equals(v.getStatus())){
                    if (listVideo.size() > 0) {
                        v.setStatus("10");
                    }
                }


                ObjectMapper mapper = new ObjectMapper();

                Comment comment = new Comment();
                comment.setUid(v.getUid());

                List<Comment> listC = commentService.selectCommentList(comment);

// List 转 JSON
                String json = mapper.writeValueAsString(listC);

// 放入 content
                v.setContent(json);

            }
        }
        return getDataTable(list);
    }

    /**
     * 查询地址视频总列表
     */
    @GetMapping("/listByIp")
    public AjaxResult listByIp(AddressVideo addressVideo) {

        List<String> list = addressVideoService.selectAddressListByIp(addressVideo);

        return AjaxResult.success(list);
    }

    /**
     * 导出地址视频总列表
     */
    @Log(title = "地址视频总", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AddressVideo addressVideo) {
        List<AddressVideo> list = addressVideoService.selectAddressVideoList(addressVideo);
        ExcelUtil<AddressVideo> util = new ExcelUtil<AddressVideo>(AddressVideo.class);
        util.exportExcel(list, "地址视频总数据");
    }

    /**
     * 获取地址视频总详细信息
     */
    @GetMapping(value = "/{uid}")
    public AjaxResult getInfo(@PathVariable("uid") String uid) {
        return AjaxResult.success(addressVideoService.selectAddressVideoByUid(uid));
    }

    /**
     * 新增地址视频总
     */
    @Log(title = "地址视频总", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AddressVideo addressVideo) {
        return toAjax(addressVideoService.insertAddressVideo(addressVideo));
    }

    /**
     * 修改地址视频总
     */
    @Log(title = "地址视频总", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AddressVideo addressVideo) {
        return toAjax(addressVideoService.updateAddressVideo(addressVideo));
    }

    /**
     * 删除地址视频总
     */
    @Log(title = "地址视频总", businessType = BusinessType.DELETE)
    @DeleteMapping("/{uids}")
    public AjaxResult remove(@PathVariable String[] uids) {
        return toAjax(addressVideoService.deleteAddressVideoByUids(uids));
    }
}
