package com.platform.modules.approve.controller;

import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.approve.vo.ApproveVo04;
import com.platform.modules.chat.service.ChatUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 特殊账号 控制层
 * </p>
 */
@RestController
@RequestMapping("/approve/special")
public class ApproveSpecialController extends BaseController {

    @Resource
    private ChatUserService chatUserService;

    /**
     * 列表数据
     */
    @RequiresPermissions(value = {"approve:special:list"})
    @GetMapping(value = "/list")
    public TableDataInfo list() {
        startPage("on_line DESC");
        return getDataTable(chatUserService.querySpecialList());
    }

    /**
     * 审批
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"approve:special:edit"})
    @DemoRepeat
    @PostMapping(value = "/edit")
    public AjaxResult edit(@Validated @RequestBody ApproveVo04 approveVo) {
        chatUserService.approveAbnormal(approveVo.getUserId(), approveVo.getStatus());
        return AjaxResult.success();
    }

}

