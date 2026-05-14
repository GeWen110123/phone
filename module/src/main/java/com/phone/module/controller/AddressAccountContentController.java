package com.phone.module.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.phone.module.domain.AccountContent;
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
import com.phone.module.domain.AddressAccountContent;
import com.phone.module.service.IAddressAccountContentService;
import com.phone.common.utils.poi.ExcelUtil;
import com.phone.common.core.page.TableDataInfo;

/**
 * 地址评论账号信息Controller
 * 
 * @author ruoyi
 * @date 2026-01-09
 */
@RestController
@RequestMapping("/module/addressAccountContent")
public class AddressAccountContentController extends BaseController
{
    @Autowired
    private IAddressAccountContentService addressAccountContentService;
    @Autowired
    private IVideoTagsService videoTagsService;
    /**
     * 查询地址评论账号信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(AddressAccountContent addressAccountContent)
    {
        startPage();
        List<AddressAccountContent> list = addressAccountContentService.selectAddressAccountContentList(addressAccountContent);


        for (AddressAccountContent acc:list){
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
     * 导出地址评论账号信息列表
     */
    @Log(title = "地址评论账号信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AddressAccountContent addressAccountContent)
    {
        List<AddressAccountContent> list = addressAccountContentService.selectAddressAccountContentList(addressAccountContent);
        ExcelUtil<AddressAccountContent> util = new ExcelUtil<AddressAccountContent>(AddressAccountContent.class);
        util.exportExcel(list, "地址评论账号信息数据");
    }

    /**
     * 获取地址评论账号信息详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(addressAccountContentService.selectAddressAccountContentById(id));
    }

    /**
     * 新增地址评论账号信息
     */
    @Log(title = "地址评论账号信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AddressAccountContent addressAccountContent)
    {
        return toAjax(addressAccountContentService.insertAddressAccountContent(addressAccountContent));
    }

    /**
     * 修改地址评论账号信息
     */
    @Log(title = "地址评论账号信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AddressAccountContent addressAccountContent)
    {
        return toAjax(addressAccountContentService.updateAddressAccountContent(addressAccountContent));
    }

    /**
     * 删除地址评论账号信息
     */
    @Log(title = "地址评论账号信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(addressAccountContentService.deleteAddressAccountContentByIds(ids));
    }
}
