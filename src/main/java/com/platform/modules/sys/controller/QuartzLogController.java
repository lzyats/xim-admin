package com.platform.modules.sys.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.quartz.domain.QuartzLog;
import com.platform.modules.quartz.service.QuartzLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 调度日志操作处理
 */
@RestController
@RequestMapping("/quartz/log")
public class QuartzLogController extends BaseController {

    private final static String title = "定时任务调度日志";

    @Resource
    private QuartzLogService quartzLogService;

    /**
     * 查询定时任务调度日志列表
     */
    @RequiresPermissions("quartz:log:list")
    @GetMapping("/list")
    public TableDataInfo list(QuartzLog quartzLog) {
        startPage("logId desc");
        List<QuartzLog> list = quartzLogService.queryList(quartzLog);
        return getDataTable(list);
    }

    /**
     * 删除定时任务调度日志
     */
    @SubmitRepeat
    @RequiresPermissions("quartz:log:remove")
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @DemoRepeat
    @GetMapping("/delete/{dataList}")
    public AjaxResult remove(@PathVariable Long[] dataList) {
        quartzLogService.deleteByIds(dataList);
        return AjaxResult.success();
    }

}
