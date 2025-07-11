package com.platform.modules.sys.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.constant.HeadConstant;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.sys.domain.SysMenu;
import com.platform.modules.sys.service.SysMenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单信息
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController {

    private final static String title = "菜单管理";

    @Resource
    private SysMenuService sysMenuService;

    /**
     * 菜单列表
     */
    @RequiresPermissions("sys:menu:list")
    @GetMapping("/list")
    public AjaxResult list(SysMenu menu) {
        menu.setRoleId(ShiroUtils.getRoleId());
        List<SysMenu> menus = sysMenuService.queryList(menu);
        return AjaxResult.success(menus);
    }

    /**
     * 新增菜单
     */
    @SubmitRepeat
    @RequiresPermissions("sys:menu:add")
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @PostMapping("/add")
    @DemoRepeat
    public AjaxResult add(@Validated @RequestBody SysMenu menu) {
        sysMenuService.addMenu(menu);
        return AjaxResult.success();
    }

    /**
     * 拷贝菜单
     */
    @SubmitRepeat
    @RequiresPermissions("sys:menu:copy")
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @GetMapping("/copy/{menuId}")
    @DemoRepeat
    public AjaxResult copy(@PathVariable Long menuId) {
        sysMenuService.copyMenu(menuId);
        return AjaxResult.success();
    }

    /**
     * 修改菜单
     */
    @SubmitRepeat
    @RequiresPermissions("sys:menu:edit")
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @PostMapping("/edit")
    @DemoRepeat
    public AjaxResult edit(@Validated @RequestBody SysMenu menu) {
        sysMenuService.editMenu(menu);
        return AjaxResult.success();
    }

    /**
     * 删除菜单
     */
    @SubmitRepeat
    @RequiresPermissions("sys:menu:remove")
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/delete/{menuId}")
    @DemoRepeat
    public AjaxResult remove(@PathVariable("menuId") Long menuId) {
        sysMenuService.deleteMenu(menuId);
        return AjaxResult.success();
    }

    /**
     * 菜单树
     */
    @RequiresPermissions(HeadConstant.PERM_ADMIN)
    @GetMapping("/menuTree")
    public AjaxResult getMenuTree() {
        return AjaxResult.success(sysMenuService.getMenuTree(new SysMenu()));
    }
}