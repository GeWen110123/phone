package com.phone.module;

import com.phone.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    /**
     * 系统健康检查接口
     * 前端调用此接口可确认服务已启动
     */
    @GetMapping("/module/status")
    public AjaxResult getStatus() {

        return AjaxResult.success("调用成功");
    }
}
