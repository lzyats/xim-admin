
package com.platform.modules.approve.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.chat.domain.ChatHelp;
import com.platform.modules.chat.service.ChatHelpService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 聊天帮助 控制层
 * </p>
 */
@RestController
@RequestMapping("/operate/help")
public class OperateHelpController extends BaseController {

    private final static String title = "聊天帮助";

    @Resource
    private ChatHelpService chatHelpService;

    /**
     * 列表数据
     */
    @RequiresPermissions(value = {"operate:help:list"})
    @GetMapping(value = "/list")
    public TableDataInfo list(ChatHelp chatHelp) {
        startPage("sort");
        List<ChatHelp> list = chatHelpService.queryList(chatHelp);
        return getDataTable(list);
    }

    /**
     * 新增数据
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:help:add"})
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @DemoRepeat
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody ChatHelp chatHelp) {
        chatHelpService.add(chatHelp);
        return AjaxResult.success();
    }

    /**
     * 修改数据
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:help:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody ChatHelp chatHelp) {
        chatHelpService.updateById(chatHelp);
        return AjaxResult.success();
    }

    /**
     * 删除数据
     */
    @SubmitRepeat
    @DemoRepeat
    @RequiresPermissions(value = {"operate:help:remove"})
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        chatHelpService.deleteById(id);
        return AjaxResult.success();
    }

}

