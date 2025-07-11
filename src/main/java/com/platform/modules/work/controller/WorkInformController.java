package com.platform.modules.work.controller;

import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.chat.domain.ChatFriendInform;
import com.platform.modules.chat.domain.ChatGroupInform;
import com.platform.modules.chat.service.ChatFriendInformService;
import com.platform.modules.chat.service.ChatGroupInformService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 骚扰举报 控制层
 * </p>
 */
@RestController
@RequestMapping("/work/inform")
public class WorkInformController extends BaseController {

    @Resource
    private ChatFriendInformService chatFriendInformService;

    @Resource
    private ChatGroupInformService chatGroupInformService;

    /**
     * 列表数据
     */
    @GetMapping(value = "/user/list")
    public TableDataInfo userList() {
        startPage("informId desc");
        ChatFriendInform chatInform = new ChatFriendInform().setStatus(YesOrNoEnum.NO);
        return getDataTable(chatFriendInformService.queryDataList(chatInform));
    }

    /**
     * 忽略
     */
    @SubmitRepeat
    @GetMapping("/user/ignore/{informId}")
    public AjaxResult userIgnore(@PathVariable Long informId) {
        chatFriendInformService.ignore(informId);
        return AjaxResult.success();
    }

    /**
     * 列表数据
     */
    @GetMapping(value = "/group/list")
    public TableDataInfo groupList() {
        startPage("informId desc");
        ChatGroupInform chatInform = new ChatGroupInform().setStatus(YesOrNoEnum.NO);
        return getDataTable(chatGroupInformService.queryDataList(chatInform));
    }

    /**
     * 忽略
     */
    @SubmitRepeat
    @GetMapping("/group/ignore/{informId}")
    public AjaxResult groupIgnore(@PathVariable Long informId) {
        chatGroupInformService.ignore(informId);
        return AjaxResult.success();
    }

}

