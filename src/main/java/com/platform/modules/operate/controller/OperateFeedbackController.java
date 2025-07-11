package com.platform.modules.operate.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.chat.domain.ChatFeedback;
import com.platform.modules.chat.service.ChatFeedbackService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
@RequestMapping("/operate/feedback")
public class OperateFeedbackController extends BaseController {

    private final static String title = "建议反馈";

    @Resource
    private ChatFeedbackService chatFeedbackService;

    /**
     * 列表数据
     */
    @RequiresPermissions(value = {"operate:feedback:list"})
    @GetMapping(value = "/list")
    public TableDataInfo list(ChatFeedback chatFeedback) {
        startPage("createTime desc");
        return getDataTable(chatFeedbackService.queryDataList(chatFeedback));
    }

    /**
     * 详细信息
     */
    @RequiresPermissions(value = {"operate:feedback:query"})
    @GetMapping("/info/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(chatFeedbackService.getInfo(id));
    }

    /**
     * 删除数据
     */
    @SubmitRepeat
    @DemoRepeat
    @RequiresPermissions(value = {"operate:feedback:remove"})
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        chatFeedbackService.deleteById(id);
        return AjaxResult.success();
    }

    /**
     * 处理数据
     */
    @DemoRepeat
    @RequiresPermissions(value = {"operate:feedback:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @GetMapping("/edit/{id}")
    public AjaxResult edit(@PathVariable Long id) {
        chatFeedbackService.update(new ChatFeedback().setStatus(YesOrNoEnum.YES).setId(id));
        return AjaxResult.success();
    }

}

