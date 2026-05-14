package com.phone.module.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.phone.module.domain.Account;
import com.phone.module.domain.AccountContent;
import com.phone.module.domain.VideoTags;
import com.phone.module.service.IAccountContentService;
import com.phone.module.service.IAccountService;
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
import com.phone.module.domain.Comment;
import com.phone.module.service.ICommentService;
import com.phone.common.utils.poi.ExcelUtil;
import com.phone.common.core.page.TableDataInfo;

/**
 * 抖音视频评论Controller
 * 
 * @author ruoyi
 * @date 2026-03-09
 */
@RestController
@RequestMapping("/module/comment")
public class CommentController extends BaseController
{
    @Autowired
    private ICommentService commentService;
    @Autowired
    private IAccountService accountService;

    @Autowired
    private IVideoTagsService videoTagsService;

    @Autowired
    private IAccountContentService accountContentService;
    /**
     * 查询抖音视频评论列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Comment comment)
    {
        List<AccountContent> listAccount= new ArrayList<>();
        startPage();
        List<Comment> list = commentService.selectCommentListByUser(comment);
        for (Comment c:list){
//            Account account = new Account();
//            account.setDouyinId(c.getUserUid());
//            List<Account> lista = accountService.selectAccountList(account);
//            if (lista.size() > 0) {
//                account = lista.get(0);
//                VideoTags tags =new VideoTags();
//                tags.setDouyinId(account.getDouyinId());
//                List<VideoTags> listTags = videoTagsService.selectVideoTagsList(tags);
//                if (listTags.size()>0){
//                    account.setStatus(listTags.get(0).getStatus());
//                }else {
//                    account.setStatus("9");
//                }
//            }
//            listAccount.add(account);

            AccountContent accountContent = new AccountContent();
            accountContent.setDouyinId(c.getUserUid());
            List<AccountContent> listAc = accountContentService.selectAccountSumList(accountContent.getDouyinId());
            AccountContent a = new AccountContent();
            if (listAc.size()>0){
                a = listAc.get(0);
                VideoTags tags =new VideoTags();
                tags.setDouyinId(c.getUserUid());
                List<VideoTags> listTags = videoTagsService.selectVideoTagsList(tags);
                if (listTags.size()>0){
                    a.setStatus(listTags.get(0).getStatus());
                }else {
                    a.setStatus("9");
                }
            }
            listAccount.add(a);

        }
        return getDataTable(listAccount);
    }

    /**
     * 导出抖音视频评论列表
     */
    @Log(title = "抖音视频评论", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Comment comment)
    {
        List<Comment> list = commentService.selectCommentList(comment);
        ExcelUtil<Comment> util = new ExcelUtil<Comment>(Comment.class);
        util.exportExcel( list, "抖音视频评论数据");
    }

    /**
     * 获取抖音视频评论详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(commentService.selectCommentById(id));
    }

    /**
     * 新增抖音视频评论
     */
    @Log(title = "抖音视频评论", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Comment comment)
    {
        return toAjax(commentService.insertComment(comment));
    }

    /**
     * 修改抖音视频评论
     */
    @Log(title = "抖音视频评论", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Comment comment)
    {
        try{
          commentService.updateCommentByUserId(comment);
        }catch (Exception e){
        }
        return AjaxResult.success();

    }

    /**
     * 删除抖音视频评论
     */
    @Log(title = "抖音视频评论", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(commentService.deleteCommentByIds(ids));
    }
}
