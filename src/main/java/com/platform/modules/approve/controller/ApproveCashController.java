package com.platform.modules.approve.controller;

import cn.hutool.core.lang.Dict;
import com.alibaba.excel.ExcelWriter;
import com.github.pagehelper.PageInfo;
import com.platform.common.aspectj.DemoRepeat;
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
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 提现审批 控制层
 * </p>
 */
@RestController
@RequestMapping("/approve/cash")
public class ApproveCashController extends BaseController {

    @Resource
    private ApproveCashService approveCashService;

    /**
     * 列表数据
     */
    @RequiresPermissions("approve:cash:list")
    @GetMapping("/list")
    public TableDataInfo list(ApproveVo00 approveVo) {
        startPage("trade_id");
        PageInfo list = approveCashService.queryDataList(approveVo);
        return getDataTable(list);
    }

    /**
     * 详细信息
     */
    @RequiresPermissions("approve:cash:edit")
    @GetMapping("/info/{tradeId}")
    public AjaxResult getInfo(@PathVariable Long tradeId) {
        Dict dict = approveCashService.getInfo(tradeId);
        return AjaxResult.success(dict);
    }

    /**
     * 审批
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"approve:cash:edit"})
    @DemoRepeat
    @PostMapping(value = "/edit")
    public AjaxResult edit(@Validated @RequestBody ApproveVo01 approveVo) {
        YesOrNoEnum status = approveVo.getStatus();
        if (YesOrNoEnum.YES.equals(status)) {
            ValidationUtil.verify(approveVo, ValidateGroup.ONE.class);
        }
        if (YesOrNoEnum.NO.equals(status)) {
            ValidationUtil.verify(approveVo, ValidateGroup.TWO.class);
        }
        approveCashService.auth(approveVo);
        return AjaxResult.success();
    }

    /**
     * 导出
     */
    @RequiresPermissions(value = {"approve:cash:export"})
    @GetMapping(value = "/export")
    public void export() {
        // 设置请求excel请求
        ExcelWriter excelWriter = startExcel("template/export.xlsx", "支付宝批量付款文件上传模板");
        // 执行导出数据
        approveCashService.export(excelWriter);
        // 别忘记关闭流
        excelWriter.finish();
    }

}

