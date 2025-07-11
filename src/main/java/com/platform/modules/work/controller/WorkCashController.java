package com.platform.modules.work.controller;

import cn.hutool.core.lang.Dict;
import com.github.pagehelper.PageInfo;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.validation.ValidateGroup;
import com.platform.common.validation.ValidationUtil;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.approve.service.ApproveCashService;
import com.platform.modules.approve.vo.ApproveVo00;
import com.platform.modules.approve.vo.ApproveVo01;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 提现审批 控制层
 * </p>
 */
@RestController
@RequestMapping("/work/cash")
public class WorkCashController extends BaseController {

    @Resource
    private ApproveCashService approveCashService;

    /**
     * 列表数据
     */
    @GetMapping("/list")
    public TableDataInfo list(ApproveVo00 approveVo) {
        startPage("trade_id");
        PageInfo pageInfo = approveCashService.queryDataList(approveVo);
        return getDataTable(pageInfo);
    }

    /**
     * 详细信息
     */
    @GetMapping("/info/{tradeId}")
    public AjaxResult getInfo(@PathVariable Long tradeId) {
        Dict dict = approveCashService.getInfo(tradeId);
        return AjaxResult.success(dict);
    }

    /**
     * 审批
     */
    @SubmitRepeat
    @PostMapping(value = "/edit")
    public AjaxResult edit(@Validated @RequestBody ApproveVo01 approveVo) {
        YesOrNoEnum status = approveVo.getStatus();
        if (YesOrNoEnum.YES.equals(status)) {
            ValidationUtil.verify(approveVo, ValidateGroup.ONE.class);
        } else {
            ValidationUtil.verify(approveVo, ValidateGroup.TWO.class);
        }
        approveCashService.auth(approveVo);
        return AjaxResult.success();
    }

}

