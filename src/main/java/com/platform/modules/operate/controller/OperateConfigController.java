package com.platform.modules.operate.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.operate.service.OperateConfigService;
import com.platform.modules.operate.vo.OperateVo06;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 配置中心 控制层
 * </p>
 */
@RestController
@RequestMapping("/operate/config")
public class OperateConfigController extends BaseController {

    private final static String title = "配置中心";

    @Resource
    private OperateConfigService operateConfigService;

    /**
     * 详细信息
     */
    @RequiresPermissions(value = {"operate:config:list"})
    @GetMapping("/getConfig")
    public AjaxResult getConfig() {
        return AjaxResult.success(operateConfigService.getInfo());
    }

    /**
     * 修改数据
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:config:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @PostMapping("/editConfig")
    public AjaxResult editConfig(@Validated @RequestBody OperateVo06 operateVo) {
        operateConfigService.update(operateVo);
        return AjaxResult.success();
    }

}

