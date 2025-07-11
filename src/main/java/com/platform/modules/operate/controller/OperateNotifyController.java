package com.platform.modules.operate.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.operate.service.OperateNotifyService;
import com.platform.modules.operate.vo.OperateVo01;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 首页公告 控制层
 * </p>
 */
@RestController
@RequestMapping("/operate/notify")
public class OperateNotifyController extends BaseController {

    private final static String title = "首页公告";

    @Resource
    private OperateNotifyService operateNotifyService;

    /**
     * 详细信息
     */
    @RequiresPermissions(value = {"operate:notify:list"})
    @GetMapping(value = "/info")
    public AjaxResult getInfo() {
        OperateVo01 data = operateNotifyService.getInfo();
        return AjaxResult.success(data);
    }

    /**
     * 修改数据
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:notify:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody OperateVo01 operateVo) {
        operateNotifyService.updateNotify(operateVo);
        return AjaxResult.success();
    }

    /**
     * 推送数据
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:notify:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @PostMapping("/push")
    @DemoRepeat
    public AjaxResult push(@Validated @RequestBody OperateVo01 operateVo) {
        operateNotifyService.pushNotify(operateVo);
        return AjaxResult.success();
    }

    /**
     * demo
     */
    @RequiresPermissions(value = {"operate:notify:list"})
    @GetMapping(value = "/getDemo")
    public AjaxResult getDemo() {
        List<String> dataList = operateNotifyService.getDemo();
        return AjaxResult.success(dataList);
    }

}

