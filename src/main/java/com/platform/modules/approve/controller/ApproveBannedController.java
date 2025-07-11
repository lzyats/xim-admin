package com.platform.modules.approve.controller;

import cn.hutool.core.lang.Dict;
import com.github.pagehelper.PageInfo;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.approve.service.ApproveBannedService;
import com.platform.modules.approve.vo.ApproveVo00;
import com.platform.modules.approve.vo.ApproveVo03;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 解封审批 控制层
 * </p>
 */
@RestController
@RequestMapping("/approve/banned")
public class ApproveBannedController extends BaseController {

    @Resource
    private ApproveBannedService approveBannedService;

    /**
     * 列表
     */
    @RequiresPermissions("approve:banned:list")
    @GetMapping("/list")
    public TableDataInfo approveList(ApproveVo00 approveVo) {
        startPage("b.create_time");
        PageInfo list = approveBannedService.queryDataList(approveVo);
        return getDataTable(list);
    }

    /**
     * 详情
     */
    @RequiresPermissions("approve:banned:edit")
    @GetMapping("/info/{userId}")
    public AjaxResult getInfo(@PathVariable Long userId) {
        Dict dict = approveBannedService.getInfo(userId);
        return AjaxResult.success(dict);
    }

    /**
     * 审批
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"approve:banned:edit"})
    @DemoRepeat
    @PostMapping(value = "/edit")
    public AjaxResult edit(@Validated @RequestBody ApproveVo03 approveVo) {
        approveBannedService.auth(approveVo);
        return AjaxResult.success();
    }

}

