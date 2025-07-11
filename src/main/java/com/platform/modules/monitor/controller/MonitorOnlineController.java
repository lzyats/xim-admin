package com.platform.modules.monitor.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.monitor.service.MonitorOnlineService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 在线用户监控
 */
@RestController
@RequestMapping("/monitor/online")
public class MonitorOnlineController extends BaseController {

    private final static String title = "在线用户";

    @Resource
    private MonitorOnlineService monitorOnlineService;

    /**
     * 列表
     */
    @RequiresPermissions("monitor:online:list")
    @GetMapping("/list")
    public TableDataInfo list() {
        return getDataTable(monitorOnlineService.queryDataList(false));
    }

    /**
     * 强退用户
     */
    @SubmitRepeat
    @RequiresPermissions("monitor:online:logout")
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @DemoRepeat
    @GetMapping("/logout/{tokenId}")
    public AjaxResult logout(@PathVariable String tokenId) {
        monitorOnlineService.logout(tokenId);
        return AjaxResult.success();
    }
}
