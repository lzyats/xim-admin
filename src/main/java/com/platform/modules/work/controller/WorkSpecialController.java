package com.platform.modules.work.controller;

import com.github.pagehelper.PageInfo;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.work.vo.WorkVo12;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 特殊账号 控制层
 * </p>
 */
@RestController
@RequestMapping("/work/special")
public class WorkSpecialController extends BaseController {

    @Resource
    private ChatUserService chatUserService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public TableDataInfo approveList() {
        startPage("on_line DESC");
        PageInfo pageInfo = chatUserService.querySpecialList();
        return getDataTable(pageInfo);
    }

    /**
     * 审批
     */
    @PostMapping(value = "/edit")
    public AjaxResult edit(@Validated @RequestBody WorkVo12 approveVo) {
        chatUserService.approveAbnormal(approveVo.getUserId(), approveVo.getStatus());
        return AjaxResult.success();
    }

}

