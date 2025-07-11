package com.platform.modules.sys.controller;

import cn.hutool.core.lang.Dict;
import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.sys.domain.SysRole;
import com.platform.modules.sys.service.SysMenuService;
import com.platform.modules.sys.service.SysRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色信息
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController {

    private final static String title = "角色管理";

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysMenuService sysMenuService;

    /**
     * 列表
     */
    @RequiresPermissions("sys:role:list")
    @GetMapping("/list")
    public TableDataInfo list(SysRole sysRole) {
        startPage("roleSort");
        List<SysRole> list = sysRoleService.queryList(sysRole);
        return getDataTable(list);
    }

    /**
     * 新增角色
     */
    @SubmitRepeat
    @RequiresPermissions("sys:role:add")
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysRole sysRole) {
        sysRoleService.addRole(sysRole);
        return AjaxResult.success();
    }

    /**
     * 保存角色
     */
    @SubmitRepeat
    @RequiresPermissions("sys:role:edit")
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody SysRole sysRole) {
        sysRoleService.updateRole(sysRole);
        return AjaxResult.success();
    }

    /**
     * 删除角色
     */
    @SubmitRepeat
    @RequiresPermissions("sys:role:remove")
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/delete/{roleId}")
    @DemoRepeat
    public AjaxResult remove(@PathVariable Long roleId) {
        sysRoleService.deleteRole(roleId);
        return AjaxResult.success();
    }

    /**
     * 角色菜单
     */
    @RequiresPermissions("sys:role:list")
    @GetMapping("/getMenuTree/{roleId}")
    public AjaxResult rolePerms(@PathVariable("roleId") Long roleId) {
        Dict dict = Dict.create()
                .set("perms", sysMenuService.queryPerms(roleId))
                .set("menus", sysMenuService.getPermsTree());
        return AjaxResult.success(dict);
    }

}