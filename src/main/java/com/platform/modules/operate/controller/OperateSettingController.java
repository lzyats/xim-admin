package com.platform.modules.operate.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.operate.service.OperateSettingService;
import com.platform.modules.operate.vo.OperateVo08;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 数据中心 控制层
 * </p>
 */
@RestController
@RequestMapping("/operate/setting")
public class OperateSettingController extends BaseController {

    private final static String title = "数据中心";

    @Resource
    private OperateSettingService operateSettingService;

    /**
     * 清理头像
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:setting:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @PostMapping("/cleanPortrait")
    public AjaxResult cleanPortrait(@Validated @RequestBody OperateVo08 operateVo) {
        String data = operateSettingService.cleanPortrait(operateVo.getCreateTime());
        return AjaxResult.successMsg(data);
    }

    /**
     * 清理消息
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:setting:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @PostMapping("/cleanMessage")
    public AjaxResult cleanMessage(@Validated @RequestBody OperateVo08 operateVo) {
        String data = operateSettingService.cleanMessage(operateVo.getCreateTime());
        return AjaxResult.successMsg(data);
    }

    /**
     * 清理交易
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:setting:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @PostMapping("/cleanTrade")
    public AjaxResult cleanTrade(@Validated @RequestBody OperateVo08 operateVo) {
        String data = operateSettingService.cleanTrade(operateVo.getCreateTime());
        return AjaxResult.successMsg(data);
    }

}

