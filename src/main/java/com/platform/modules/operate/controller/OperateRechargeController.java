package com.platform.modules.operate.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.operate.service.OperateRechargeService;
import com.platform.modules.operate.vo.OperateVo03;
import com.platform.modules.operate.vo.OperateVo07;
import com.platform.modules.wallet.service.WalletConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 充值管理 控制层
 * </p>
 */
@RestController
@RequestMapping("/operate/recharge")
public class OperateRechargeController extends BaseController {

    private final static String title = "充值管理";

    @Resource
    private WalletConfigService walletConfigService;

    @Resource
    private OperateRechargeService operateRechargeService;

    /**
     * 列表数据
     */
    @RequiresPermissions(value = {"operate:recharge:list"})
    @GetMapping(value = "/list")
    public AjaxResult list() {
        List<OperateVo07> dataList = walletConfigService.queryList();
        return AjaxResult.success(dataList);
    }

    /**
     * 修改数据
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:recharge:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody OperateVo07 operateVo) {
        walletConfigService.update(operateVo);
        return AjaxResult.success();
    }

    /**
     * 详细信息
     */
    @RequiresPermissions(value = {"operate:recharge:edit"})
    @GetMapping("/getConfig")
    public AjaxResult getConfig() {
        return AjaxResult.success(operateRechargeService.getInfo());
    }

    /**
     * 修改数据
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:recharge:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @PostMapping("/editConfig")
    public AjaxResult editConfig(@Validated @RequestBody OperateVo03 operateVo) {
        operateRechargeService.update(operateVo);
        return AjaxResult.success();
    }

}

