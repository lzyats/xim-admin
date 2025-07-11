package com.platform.modules.work.controller;

import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.chat.domain.ChatFeedback;
import com.platform.modules.chat.service.ChatFeedbackService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 建议反馈 控制层
 * </p>
 */
@RestController
@RequestMapping("/work/feedback")
public class WorkFeedbackController extends BaseController {

    @Resource
    private ChatFeedbackService chatFeedbackService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public TableDataInfo approveList() {
        startPage("createTime desc");
        ChatFeedback feedback = new ChatFeedback().setStatus(YesOrNoEnum.NO);
        return getDataTable(chatFeedbackService.queryDataList(feedback));
    }

    /**
     * 审批
     */
    @GetMapping(value = "/edit/{id}")
    public AjaxResult edit(@PathVariable Long id) {
        chatFeedbackService.update(new ChatFeedback().setStatus(YesOrNoEnum.YES).setId(id));
        return AjaxResult.success();
    }

}

