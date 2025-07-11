package com.platform.modules.operate.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.utils.VersionUtils;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.chat.domain.ChatVersion;
import com.platform.modules.chat.service.ChatVersionService;
import com.platform.modules.operate.service.OperateVersionService;
import com.platform.modules.operate.vo.OperateVo02;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 升级版本 控制层
 * </p>
 */
@RestController
@RequestMapping("/operate/version")
public class OperateVersionController extends BaseController {

    private final static String title = "版本管理";

    @Resource
    private ChatVersionService chatVersionService;

    @Resource
    private OperateVersionService operateVersionService;

    /**
     * 列表数据
     */
    @RequiresPermissions(value = {"operate:version:list"})
    @GetMapping(value = "/list")
    public AjaxResult list(ChatVersion chatVersion) {
        List<ChatVersion> list = chatVersionService.queryList(chatVersion);
        return AjaxResult.success(list);
    }

    /**
     * 详细信息
     */
    @RequiresPermissions(value = {"operate:version:edit"})
    @GetMapping("/info/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(chatVersionService.getById(id));
    }

    /**
     * 修改数据
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:version:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody ChatVersion chatVersion) {
        // 验证版本
        if (!VersionUtils.matchVersion(chatVersion.getVersion())) {
            throw new BaseException("版本格式不正确");
        }
        chatVersionService.editVersion(chatVersion);
        return AjaxResult.success();
    }

    /**
     * 详细信息
     */
    @RequiresPermissions(value = {"operate:version:edit"})
    @GetMapping("/getConfig")
    public AjaxResult getConfig() {
        return AjaxResult.success(operateVersionService.getInfo());
    }

    /**
     * 修改数据
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:version:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @PostMapping("/editConfig")
    public AjaxResult editConfig(@Validated @RequestBody OperateVo02 operateVo) {
        operateVersionService.update(operateVo);
        return AjaxResult.success();
    }

}

