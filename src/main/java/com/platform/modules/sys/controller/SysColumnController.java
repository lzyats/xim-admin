package com.platform.modules.sys.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.constant.HeadConstant;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.sys.domain.SysColumn;
import com.platform.modules.sys.service.SysColumnService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 表格信息
 */
@RestController
@RequestMapping("/sys/column")
public class SysColumnController extends BaseController {

    private final static String title = "表格管理";

    @Resource
    private SysColumnService sysColumnService;

    /**
     * 查询
     */
    @RequiresPermissions(HeadConstant.PERM_ADMIN)
    @GetMapping("/getColumns/{tableId}")
    public AjaxResult getColumns(@PathVariable Integer tableId) {
        return AjaxResult.success(sysColumnService.getTable(tableId));
    }

    /**
     * 修改
     */
    @SubmitRepeat
    @RequiresPermissions(HeadConstant.PERM_ADMIN)
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody SysColumn sysColumn) {
        sysColumnService.editTable(sysColumn);
        return AjaxResult.success();
    }

    /**
     * 删除
     */
    @SubmitRepeat
    @RequiresPermissions(HeadConstant.PERM_ADMIN)
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/delete/{tableId}")
    public AjaxResult remove(@PathVariable("tableId") Integer tableId) {
        sysColumnService.deleteTable(tableId);
        return AjaxResult.success();
    }

}