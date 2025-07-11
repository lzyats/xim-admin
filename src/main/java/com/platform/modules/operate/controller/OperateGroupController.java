package com.platform.modules.operate.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.chat.service.ChatGroupLevelService;
import com.platform.modules.chat.vo.ChatVo09;
import com.platform.modules.operate.service.OperateGroupService;
import com.platform.modules.operate.vo.OperateVo05;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 群组扩容 控制层
 * </p>
 */
@RestController
@RequestMapping("/operate/group")
public class OperateGroupController extends BaseController {

    private final static String title = "群组扩容";

    @Resource
    private ChatGroupLevelService chatGroupLevelService;

    @Resource
    private OperateGroupService operateGroupService;

    /**
     * 列表数据
     */
    @RequiresPermissions(value = {"operate:group:list"})
    @GetMapping(value = "/list")
    public AjaxResult list() {
        List<ChatVo09> dataList = chatGroupLevelService.queryList();
        return AjaxResult.success(dataList);
    }

    /**
     * 修改数据
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:group:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody ChatVo09 chatVo) {
        chatGroupLevelService.update(chatVo);
        return AjaxResult.success();
    }

    /**
     * 详细信息
     */
    @RequiresPermissions(value = {"operate:group:edit"})
    @GetMapping("/getConfig")
    public AjaxResult getConfig() {
        return AjaxResult.success(operateGroupService.getInfo());
    }

    /**
     * 修改数据
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:group:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @PostMapping("/editConfig")
    public AjaxResult editConfig(@Validated @RequestBody OperateVo05 operateVo) {
        operateGroupService.update(operateVo);
        return AjaxResult.success();
    }

}

