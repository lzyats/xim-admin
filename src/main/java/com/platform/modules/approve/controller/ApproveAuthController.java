package com.platform.modules.approve.controller;

import com.platform.common.aspectj.DemoRepeat;
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
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 认证审批 控制层
 * </p>
 */
@RestController
@RequestMapping("/approve/auth")
public class ApproveAuthController extends BaseController {

    @Resource
    private ApproveAuthService approveAuthService;

    /**
     * 列表数据
     */
    @RequiresPermissions(value = {"approve:auth:list"})
    @GetMapping(value = "/list")
    public TableDataInfo list(ApproveVo00 approveVo) {
        startPage("b.authTime");
        return getDataTable(approveAuthService.queryDataList(approveVo));
    }

    /**
     * 详细信息
     */
    @RequiresPermissions(value = {"approve:auth:edit"})
    @GetMapping("/info/{userId}")
    public AjaxResult getInfo(@PathVariable Long userId) {
        return AjaxResult.success(approveAuthService.getInfo(userId));
    }

    /**
     * 审批
     */
    @RequiresPermissions(value = {"approve:auth:edit"})
    @PostMapping(value = "/edit")
    @DemoRepeat
    @SubmitRepeat
    public AjaxResult edit(@Validated @RequestBody ApproveVo02 approveVo) {
        if (YesOrNoEnum.NO.equals(approveVo.getStatus())) {
            ValidationUtil.verify(approveVo, ValidateGroup.ONE.class);
        }
        approveAuthService.auth(approveVo);
        return AjaxResult.success();
    }

}

