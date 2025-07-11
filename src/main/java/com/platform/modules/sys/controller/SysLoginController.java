package com.platform.modules.sys.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.sys.domain.SysLogin;
import com.platform.modules.sys.service.SysLoginService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统访问记录
 */
@RestController
@RequestMapping("/sys/login")
public class SysLoginController extends BaseController {

    private final static String title = "登陆日志";

    @Resource
    private SysLoginService sysLoginService;

    /**
     * 列表
     */
    @RequiresPermissions("sys:login:list")
    @GetMapping("/list")
    public TableDataInfo list(SysLogin sysLogin) {
        startPage("logId desc");
        List<SysLogin> list = sysLoginService.queryList(sysLogin);
        return getDataTable(list);
    }

    /**
     * 删除
     */
    @SubmitRepeat
    @RequiresPermissions("sys:login:remove")
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @DemoRepeat
    @GetMapping("/delete/{dataList}")
    public AjaxResult remove(@PathVariable Long[] dataList) {
        sysLoginService.deleteByIds(dataList);
        return AjaxResult.success();
    }

}
