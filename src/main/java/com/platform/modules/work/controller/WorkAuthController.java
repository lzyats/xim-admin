package com.platform.modules.work.controller;

import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.validation.ValidateGroup;
import com.platform.common.validation.ValidationUtil;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.approve.service.ApproveAuthService;
import com.platform.modules.approve.vo.ApproveVo00;
import com.platform.modules.approve.vo.ApproveVo02;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 认证审批 控制层
 * </p>
 */
@RestController
@RequestMapping("/work/auth")
public class WorkAuthController extends BaseController {

    @Resource
    private ApproveAuthService approveAuthService;

    /**
     * 列表数据
     */
    @GetMapping(value = "/list")
    public TableDataInfo list(ApproveVo00 approveVo) {
        startPage("b.authTime");
        return getDataTable(approveAuthService.queryDataList(approveVo));
    }

    /**
     * 详细信息
     */
    @GetMapping("/info/{userId}")
    public AjaxResult getInfo(@PathVariable Long userId) {
        return AjaxResult.success(approveAuthService.getInfo(userId));
    }

    /**
     * 审批
     */
    @PostMapping(value = "/edit")
    @SubmitRepeat
    public AjaxResult edit(@Validated @RequestBody ApproveVo02 approveVo) {
        if (YesOrNoEnum.NO.equals(approveVo.getStatus())) {
            ValidationUtil.verify(approveVo, ValidateGroup.ONE.class);
        }
        approveAuthService.auth(approveVo);
        return AjaxResult.success();
    }

}

