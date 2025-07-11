package com.platform.modules.operate.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.operate.service.OperateCashService;
import com.platform.modules.operate.vo.OperateVo04;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 提现管理 控制层
 * </p>
 */
@RestController
@RequestMapping("/operate/cash")
public class OperateCashController extends BaseController {

    private final static String title = "提现管理";

    @Resource
    private OperateCashService operateCashService;

    /**
     * 详细信息
     */
    @RequiresPermissions(value = {"operate:cash:list"})
    @GetMapping("/getConfig")
    public AjaxResult getConfig() {
        return AjaxResult.success(operateCashService.getInfo());
    }

    /**
     * 修改数据
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:cash:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @PostMapping("/editConfig")
    public AjaxResult editConfig(@Validated @RequestBody OperateVo04 operateVo) {
        operateCashService.update(operateVo);
        return AjaxResult.success();
    }

}

