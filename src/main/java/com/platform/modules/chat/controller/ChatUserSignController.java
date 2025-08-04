package com.platform.modules.chat.controller;

import java.util.List;

import javax.annotation.Resource;

import com.platform.common.aspectj.AppLog;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.web.page.TableDataInfo;
import com.platform.common.web.domain.AjaxResult;
import org.springframework.web.bind.annotation.*;
import com.platform.modules.chat.service.ChatUserSignService;
import com.platform.modules.chat.domain.ChatUserSign;
import com.platform.common.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;

/**
 * <p>
 * 用户按天签到记录 控制层
 * </p>
 */
@RestController
@RequestMapping("/chat/user/sign")
public class ChatUserSignController extends BaseController {

    private final static String title = "用户按天签到记录";

    @Resource
    private ChatUserSignService chatUserSignService;

    /**
     * 列表数据 TODO
     */
    @RequiresPermissions(value = {"chat:user:sign:list"})
    @GetMapping(value = "/list")
    public TableDataInfo list(ChatUserSign chatUserSign) {
        startPage();
        List<ChatUserSign> list = chatUserSignService.queryList(chatUserSign);
        return getDataTable(list);
    }

    /**
     * 详细信息 TODO
     */
    @RequiresPermissions(value = {"chat:user:sign:query"})
    @GetMapping("/info/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(chatUserSignService.getById(id));
    }

    /**
     * 新增数据 TODO
     */
    @RequiresPermissions(value = {"chat:user:sign:add"})
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody ChatUserSign chatUserSign) {
        chatUserSignService.add(chatUserSign);
        return AjaxResult.successMsg("新增成功");
    }

    /**
     * 修改数据 TODO
     */
    @RequiresPermissions(value = {"chat:user:sign:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody ChatUserSign chatUserSign) {
        chatUserSignService.updateById(chatUserSign);
        return AjaxResult.successMsg("修改成功");
    }

    /**
     * 删除数据 TODO
     */
    @RequiresPermissions(value = {"chat:user:sign:remove"})
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        chatUserSignService.deleteById(id);
        return AjaxResult.successMsg("删除成功");
    }

    /**
     * 批量删除数据 TODO
     */
    @RequiresPermissions(value = {"chat:user:sign:remove"})
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/deleteBatch/{ids}")
    public AjaxResult deleteBatch(@PathVariable Long[] ids) {
        chatUserSignService.deleteByIds(ids);
        return AjaxResult.successMsg("删除成功");
    }

    /**
     * 状态修改 TODO
     */
    @RequiresPermissions(value = {"chat:user:sign:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @PostMapping("/status")
    public AjaxResult status(@RequestBody ChatUserSign chatUserSign) {
        chatUserSignService.status(chatUserSign);
        return AjaxResult.success();
    }

}

