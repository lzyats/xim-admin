package com.platform.modules.sys.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.quartz.domain.QuartzJob;
import com.platform.modules.quartz.service.QuartzJobService;
import com.platform.modules.quartz.utils.CronUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 调度任务信息操作处理
 */
@RestController
@RequestMapping("/quartz/job")
public class QuartzJobController extends BaseController {

    private final static String title = "定时任务";

    @Resource
    private QuartzJobService quartzJobService;

    /**
     * 查询定时任务列表
     */
    @RequiresPermissions("quartz:job:list")
    @GetMapping("/list")
    public TableDataInfo list(QuartzJob quartzJob) {
        startPage();
        List<QuartzJob> list = quartzJobService.queryList(quartzJob);
        return getDataTable(list);
    }

    /**
     * 新增定时任务
     */
    @SubmitRepeat
    @RequiresPermissions("quartz:job:add")
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @DemoRepeat
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody QuartzJob quartzJob) {
        validCron(quartzJob);
        quartzJobService.addJob(quartzJob);
        return AjaxResult.success();
    }

    /**
     * 校验cron表达式
     */
    private void validCron(QuartzJob quartzJob) {
        if (!CronUtils.isValid(quartzJob.getCronExpression())) {
            throw new BaseException("Cron执行表达式格式不正确");
        }
    }

    /**
     * 修改定时任务
     */
    @SubmitRepeat
    @RequiresPermissions("quartz:job:edit")
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody QuartzJob quartzJob) {
        validCron(quartzJob);
        quartzJobService.updateJob(quartzJob);
        return AjaxResult.success();
    }

    /**
     * 定时任务立即执行一次
     */
    @SubmitRepeat
    @RequiresPermissions("quartz:job:run")
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @GetMapping("/run/{jobId}")
    public AjaxResult run(@PathVariable Long jobId) {
        quartzJobService.run(jobId);
        return AjaxResult.success();
    }

    /**
     * 删除定时任务
     */
    @SubmitRepeat
    @RequiresPermissions("quartz:job:remove")
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @DemoRepeat
    @GetMapping("/delete/{jobId}")
    public AjaxResult remove(@PathVariable Long jobId) {
        quartzJobService.deleteJob(jobId);
        return AjaxResult.success();
    }

}
