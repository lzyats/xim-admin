package com.platform.modules.chat.controller;

import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.chat.domain.ChatGroupInform;
import com.platform.modules.chat.service.ChatGroupInformService;
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
@RequestMapping("/inform/group")
public class InformGroupController extends BaseController {

    @Resource
    private ChatGroupInformService chatGroupInformService;

    /**
     * 列表数据
     */
    @RequiresPermissions(value = {"inform:group:list"})
    @GetMapping(value = "/list")
    public TableDataInfo list(ChatGroupInform chatInform) {
        startPage("informId desc");
        return getDataTable(chatGroupInformService.queryDataList(chatInform));
    }

    /**
     * 忽略
     */
    @RequiresPermissions(value = {"inform:group:banned"})
    @GetMapping("/ignore/{informId}")
    @SubmitRepeat
    public AjaxResult ignore(@PathVariable Long informId) {
        chatGroupInformService.ignore(informId);
        return AjaxResult.success();
    }

}

