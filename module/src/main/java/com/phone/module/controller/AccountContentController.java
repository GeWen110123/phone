package com.phone.module.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

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
import com.phone.module.domain.AccountContent;
import com.phone.module.service.IAccountContentService;
import com.phone.common.utils.poi.ExcelUtil;
import com.phone.common.core.page.TableDataInfo;

/**
 * 视频评论账号信息Controller
 * 
 * @author ruoyi
 * @date 2026-01-09
 */
@RestController
@RequestMapping("/module/accountContent")
public class AccountContentController extends BaseController
{
    @Autowired
    private IAccountContentService accountContentService;
    @Autowired
    private IVideoTagsService videoTagsService;
    /**
     * 查询视频评论账号信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(AccountContent accountContent)
    {
        startPage();
        List<AccountContent> list = accountContentService.selectAccountContentList(accountContent);

        for (AccountContent acc:list){
            VideoTags tags =new VideoTags();
            tags.setDouyinId(acc.getDouyinId());
            List<VideoTags> listTags = videoTagsService.selectVideoTagsList(tags);
            if (listTags.size()>0){
                acc.setStatus(listTags.get(0).getStatus());
            }else {
                acc.setStatus("9");
            }
        }

        return getDataTable(list);
    }
//    =========================================================================

    /**
     * 查询用户关注列表
     */
    @GetMapping("/listByFollow")
    public TableDataInfo listByFollow(AccountContent accountContent)
    {
        startPage();
        List<AccountContent> list = accountContentService.selectFollowList(accountContent.getDouyinId());

        for (AccountContent acc:list){
            VideoTags tags =new VideoTags();
            tags.setDouyinId(acc.getvUid());
            List<VideoTags> listTags = videoTagsService.selectVideoTagsList(tags);
            if (listTags.size()>0){
                acc.setStatus(listTags.get(0).getStatus());
            }else {
                acc.setStatus("9");
            }
        }

        return getDataTable(list);
    }
    /**
     * 查询用户粉丝列表
     */
    @GetMapping("/listByFans")
    public TableDataInfo listByFans(AccountContent accountContent)
    {
        startPage();
        List<AccountContent> list = accountContentService.selectFansList(accountContent.getDouyinId());

        for (AccountContent acc:list){
            VideoTags tags =new VideoTags();
            tags.setDouyinId(acc.getvUid());
            List<VideoTags> listTags = videoTagsService.selectVideoTagsList(tags);
            if (listTags.size()>0){
                acc.setStatus(listTags.get(0).getStatus());
            }else {
                acc.setStatus("9");
            }
        }

        return getDataTable(list);
    }

//    =========================================================================
    /**
     * 查询视频评论账号信息列表
     */
    @GetMapping("/listByDid")
    public TableDataInfo listByDid(AccountContent accountContent)
    {
        startPage();
        List<AccountContent> list = accountContentService.selectAccountSumList(accountContent.getDouyinId());

        for (AccountContent acc:list){
            VideoTags tags =new VideoTags();
            tags.setDouyinId(acc.getDouyinId());
            List<VideoTags> listTags = videoTagsService.selectVideoTagsList(tags);
            if (listTags.size()>0){
                acc.setStatus(listTags.get(0).getStatus());
            }else {
                acc.setStatus("9");
            }
        }
        return getDataTable(list);
    }

    /**
     * 导出视频评论账号信息列表
     */
    @Log(title = "视频评论账号信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export( AccountContent accountContent)
    {
        List<AccountContent> list = accountContentService.selectAccountContentList(accountContent);
        ExcelUtil<AccountContent> util = new ExcelUtil<AccountContent>(AccountContent.class);
        util.exportExcel(list, "视频评论账号信息数据");
    }

    /**
     * 获取视频评论账号信息详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(accountContentService.selectAccountContentById(id));
    }

    /**
     * 新增视频评论账号信息
     */
    @Log(title = "视频评论账号信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AccountContent accountContent)
    {
        return toAjax(accountContentService.insertAccountContent(accountContent));
    }

    /**
     * 修改视频评论账号信息
     */
    @Log(title = "视频评论账号信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AccountContent accountContent)
    {
        return toAjax(accountContentService.updateAccountContent(accountContent));
    }

    /**
     * 删除视频评论账号信息
     */
    @Log(title = "视频评论账号信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(accountContentService.deleteAccountContentByIds(ids));
    }
}
