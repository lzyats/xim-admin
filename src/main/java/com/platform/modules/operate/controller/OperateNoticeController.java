package com.platform.modules.operate.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.chat.domain.ChatNotice;
import com.platform.modules.chat.service.ChatNoticeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 通知公告 控制层
 * </p>
 */
@RestController
@RequestMapping("/operate/notice")
public class OperateNoticeController extends BaseController {

    private final static String title = "通知公告";

    @Resource
    private ChatNoticeService chatNoticeService;

    /**
     * 列表数据
     */
    @RequiresPermissions(value = {"operate:notice:list"})
    @GetMapping(value = "/list")
    public TableDataInfo list(ChatNotice chatNotice) {
        startPage("createTime desc");
        return getDataTable(chatNoticeService.queryList(chatNotice));
    }

    /**
     * 新增数据
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:notice:add"})
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @DemoRepeat
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody ChatNotice chatNotice) {
        chatNoticeService.add(chatNotice);
        return AjaxResult.success();
    }

    /**
     * 修改数据
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:notice:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody ChatNotice chatNotice) {
        chatNoticeService.updateById(chatNotice);
        return AjaxResult.success();
    }

    /**
     * 删除数据
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:notice:remove"})
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/delete/{id}")
    @DemoRepeat
    public AjaxResult delete(@PathVariable Long id) {
        chatNoticeService.deleteById(id);
        return AjaxResult.success();
    }

    /**
     * 拷贝
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:notice:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @GetMapping("/copy/{noticeId}")
    public AjaxResult copy(@PathVariable Long noticeId) {
        chatNoticeService.copy(noticeId);
        return AjaxResult.success();
    }

}

