package com.platform.modules.chat.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.chat.domain.ChatGroup;
import com.platform.modules.chat.service.ChatGroupLogService;
import com.platform.modules.chat.service.ChatGroupService;
import com.platform.modules.chat.service.ChatMsgService;
import com.platform.modules.chat.vo.ChatVo05;
import com.platform.modules.chat.vo.ChatVo11;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 群组 控制层
 * </p>
 */
@RestController
@RequestMapping("/chat/group")
public class ChatGroupController extends BaseController {

    private final static String title = "群组管理";

    @Resource
    private ChatGroupService chatGroupService;

    @Resource
    private ChatMsgService chatMsgService;

    @Resource
    private ChatGroupLogService chatGroupLogService;

    /**
     * 列表数据
     */
    @RequiresPermissions(value = {"chat:group:list"})
    @GetMapping(value = "/list")
    public TableDataInfo list(ChatGroup chatGroup) {
        startPage("createTime desc");
        return getDataTable(chatGroupService.queryDataList(chatGroup));
    }

    /**
     * 详细信息
     */
    @RequiresPermissions(value = {"chat:group:query"})
    @GetMapping("/info/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(chatGroupService.getInfo(id));
    }

    /**
     * 修改群组
     */
    @RequiresPermissions("chat:group:edit")
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @PostMapping("/editGroup")
    @SubmitRepeat
    public AjaxResult editGroup(@Validated @RequestBody ChatVo11 chatVo) {
        chatGroupService.editGroup(chatVo);
        return AjaxResult.success();
    }

    /**
     * 群组成员
     */
    @RequiresPermissions(value = {"chat:group:query"})
    @GetMapping("/memberList/{groupId}")
    public TableDataInfo memberList(@PathVariable Long groupId) {
        startPage("CASE memberType WHEN 'master' THEN 1 WHEN 'manager' THEN 2 WHEN 'normal' THEN 3 END, createTime");
        return getDataTable(chatGroupService.memberList(groupId));
    }

    /**
     * 封禁群组
     */
    @RequiresPermissions("chat:group:banned")
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @PostMapping("/banned")
    @SubmitRepeat
    public AjaxResult banned(@RequestBody ChatGroup chatGroup) {
        chatGroupService.banned(chatGroup.getGroupId(), chatGroup.getBanned());
        return AjaxResult.success();
    }

    /**
     * 群组消息
     */
    @RequiresPermissions(value = {"chat:group:msg"})
    @GetMapping(value = "/message/{groupId}")
    public TableDataInfo getMessage(@PathVariable Long groupId) {
        startPage("msgId desc");
        return getDataTable(chatMsgService.getMessage(groupId));
    }

    /**
     * 群员数量
     */
    @RequiresPermissions("chat:group:edit")
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @PostMapping("/limitCount")
    @SubmitRepeat
    public AjaxResult limitCount(@Validated @RequestBody ChatVo05 chatVo) {
        chatGroupService.levelCount(chatVo.getGroupId(), chatVo.getLevelCount());
        return AjaxResult.success();
    }

    /**
     * 查询日志
     */
    @RequiresPermissions(value = {"chat:group:log"})
    @GetMapping("/log/{groupId}")
    public TableDataInfo getLog(@PathVariable Long groupId) {
        startPage("id desc");
        return getDataTable(chatGroupLogService.queryDataList(groupId));
    }

}

