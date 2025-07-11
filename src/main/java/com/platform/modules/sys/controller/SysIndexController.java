package com.platform.modules.sys.controller;

import cn.hutool.core.lang.Dict;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.constant.HeadConstant;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.utils.CodeUtils;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.sys.domain.SysMenu;
import com.platform.modules.sys.domain.SysUser;
import com.platform.modules.sys.service.SysMenuService;
import com.platform.modules.sys.service.SysUserService;
import com.platform.modules.sys.vo.LoginVo03;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 登录验证
 */
@RestController
@Slf4j
@RequestMapping("/sys")
public class SysIndexController extends BaseController {

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysUserService sysUserService;

    /**
     * 信息
     */
    @RequiresPermissions(HeadConstant.PERM_ADMIN)
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        Dict data = sysUserService.getInfo();
        return AjaxResult.success(data);
    }

    /**
     * 路由信息
     */
    @RequiresPermissions(HeadConstant.PERM_ADMIN)
    @GetMapping("/getRouters")
    public AjaxResult getRouters() {
        return AjaxResult.success(sysMenuService.getRouters());
    }

    /**
     * 菜单信息
     */
    @RequiresPermissions(HeadConstant.PERM_ADMIN)
    @GetMapping("/getMenus")
    public AjaxResult getMenus() {
        return AjaxResult.success(sysMenuService.getMenuTree(new SysMenu().setVisible(YesOrNoEnum.YES)));
    }

    /**
     * 修改密码
     */
    @SubmitRepeat
    @RequiresPermissions(HeadConstant.PERM_ADMIN)
    @DemoRepeat
    @PostMapping("/updatePwd")
    public AjaxResult updatePwd(@Validated @RequestBody LoginVo03 loginVo) {
        String oldPwd = loginVo.getOldPwd();
        String newPwd = loginVo.getNewPwd();
        if (oldPwd.equals(newPwd)) {
            return AjaxResult.fail("新旧密码不能相同");
        }
        if (ShiroUtils.isAdmin()) {
            return AjaxResult.successMsg("密码修改成功");
        }
        SysUser sysUser = sysUserService.getById(ShiroUtils.getUserId());
        // 检验旧密码
        if (!sysUser.getPassword().equals(CodeUtils.credentials(oldPwd, sysUser.getSalt()))) {
            return AjaxResult.fail("旧密码不正确");
        }
        sysUserService.updatePwd(sysUser.getUserId(), newPwd);
        return AjaxResult.successMsg("密码修改成功");
    }

}
