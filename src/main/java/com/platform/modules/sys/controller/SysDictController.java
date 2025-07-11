package com.platform.modules.sys.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.sys.domain.SysDict;
import com.platform.modules.sys.service.SysDictService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据字典信息
 */
@RestController
@RequestMapping("/sys/dict")
public class SysDictController extends BaseController {

    private final static String title = "数据字典";

    @Resource
    private SysDictService sysDictService;

    /**
     * 列表
     */
    @RequiresPermissions("sys:dict:list")
    @GetMapping("/list")
    public TableDataInfo list(SysDict sysDict) {
        startPage("dictSort");
        List<SysDict> list = sysDictService.queryList(sysDict);
        return getDataTable(list);
    }

    /**
     * 新增
     */
    @SubmitRepeat
    @RequiresPermissions("sys:dict:add")
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysDict sysDict) {
        sysDictService.addDict(sysDict);
        return AjaxResult.success();
    }

    /**
     * 修改
     */
    @SubmitRepeat
    @RequiresPermissions("sys:dict:edit")
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @PostMapping("/edit")
    @DemoRepeat
    public AjaxResult edit(@Validated @RequestBody SysDict sysDict) {
        sysDictService.editDict(sysDict);
        return AjaxResult.success();
    }

    /**
     * 删除
     */
    @SubmitRepeat
    @RequiresPermissions("sys:dict:remove")
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @DemoRepeat
    @GetMapping("/delete/{dictId}")
    public AjaxResult remove(@PathVariable Long dictId) {
        sysDictService.deleteDict(dictId);
        return AjaxResult.success();
    }

}
