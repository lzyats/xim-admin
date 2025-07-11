package com.platform.modules.sys.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.sys.domain.SysLog;
import com.platform.modules.sys.service.SysLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 操作日志记录
 */
@RestController
@RequestMapping("/sys/log")
public class SysLogController extends BaseController {

    private final static String title = "操作日志";

    @Resource
    private SysLogService sysLogService;

    /**
     * 列表
     */
    @RequiresPermissions("sys:log:list")
    @GetMapping("/list")
    public TableDataInfo list(SysLog sysLog) {
        startPage("logId desc");
        List<SysLog> list = sysLogService.queryList(sysLog);
        return getDataTable(list);
    }

    /**
     * 删除
     */
    @SubmitRepeat
    @RequiresPermissions("sys:log:remove")
    @GetMapping("/delete/{dataList}")
    @DemoRepeat
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    public AjaxResult remove(@PathVariable Long[] dataList) {
        sysLogService.deleteByIds(dataList);
        return AjaxResult.success();
    }

}
