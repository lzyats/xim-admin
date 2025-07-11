package com.platform.modules.chat.controller;

import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.chat.domain.ChatFriendInform;
import com.platform.modules.chat.service.ChatFriendInformService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
@RequestMapping("/inform/user")
public class InformUserController extends BaseController {

    @Resource
    private ChatFriendInformService chatFriendInformService;

    /**
     * 列表数据
     */
    @RequiresPermissions(value = {"inform:user:list"})
    @GetMapping(value = "/list")
    public TableDataInfo list(ChatFriendInform chatInform) {
        startPage("informId desc");
        return getDataTable(chatFriendInformService.queryDataList(chatInform));
    }

    /**
     * 忽略
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"inform:user:banned"})
    @GetMapping("/ignore/{informId}")
    public AjaxResult ignore(@PathVariable Long informId) {
        chatFriendInformService.ignore(informId);
        return AjaxResult.success();
    }

}

