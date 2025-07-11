package com.platform.modules.work.controller;

import cn.hutool.core.lang.Dict;
import com.github.pagehelper.PageInfo;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.approve.service.ApproveBannedService;
import com.platform.modules.approve.vo.ApproveVo00;
import com.platform.modules.approve.vo.ApproveVo03;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 解封审批 控制层
 * </p>
 */
@RestController
@RequestMapping("/work/banned")
public class WorkBannedController extends BaseController {

    @Resource
    private ApproveBannedService approveBannedService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public TableDataInfo approveList(ApproveVo00 approveVo) {
        startPage("b.create_time");
        PageInfo pageInfo = approveBannedService.queryDataList(approveVo);
        return getDataTable(pageInfo);
    }

    /**
     * 详情
     */
    @GetMapping("/info/{userId}")
    public AjaxResult getInfo(@PathVariable Long userId) {
        Dict dict = approveBannedService.getInfo(userId);
        return AjaxResult.success(dict);
    }

    /**
     * 审批
     */
    @SubmitRepeat
    @PostMapping(value = "/edit")
    public AjaxResult edit(@Validated @RequestBody ApproveVo03 approveVo) {
        approveBannedService.auth(approveVo);
        return AjaxResult.success();
    }

}

