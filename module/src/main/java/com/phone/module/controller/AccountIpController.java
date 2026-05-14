package com.phone.module.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.phone.module.domain.AccountIp;
import com.phone.module.service.IAccountIpService;
import com.phone.common.utils.poi.ExcelUtil;
import com.phone.common.core.page.TableDataInfo;

/**
 * 账号地址历史Controller
 * 
 * @author ruoyi
 * @date 2026-02-04
 */
@RestController
@RequestMapping("/module/ipAddress")
public class AccountIpController extends BaseController
{
    @Autowired
    private IAccountIpService accountIpService;

    /**
     * 查询账号地址历史列表
     */
    @GetMapping("/list")
    public TableDataInfo list(AccountIp accountIp)
    {
        startPage();
        List<AccountIp> list = accountIpService.selectAccountIpList(accountIp);
        return getDataTable(list);
    }

    /**
     * 导出账号地址历史列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, AccountIp accountIp)
    {
        List<AccountIp> list = accountIpService.selectAccountIpList(accountIp);
        ExcelUtil<AccountIp> util = new ExcelUtil<AccountIp>(AccountIp.class);
        util.exportExcel(list, "账号地址历史数据");
    }

    /**
     * 获取账号地址历史详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(accountIpService.selectAccountIpById(id));
    }

    /**
     * 新增账号地址历史
     */
    @PostMapping
    public AjaxResult add(@RequestBody AccountIp accountIp)
    {
        return toAjax(accountIpService.insertAccountIp(accountIp));
    }

    /**
     * 修改账号地址历史
     */
    @PutMapping
    public AjaxResult edit(@RequestBody AccountIp accountIp)
    {
        return toAjax(accountIpService.updateAccountIp(accountIp));
    }

    /**
     * 删除账号地址历史
     */
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(accountIpService.deleteAccountIpByIds(ids));
    }
}
