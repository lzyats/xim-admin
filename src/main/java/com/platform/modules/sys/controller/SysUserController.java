package com.platform.modules.sys.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.common.web.vo.LabelVo;
import com.platform.modules.sys.domain.SysRole;
import com.platform.modules.sys.domain.SysUser;
import com.platform.modules.sys.service.SysRoleService;
import com.platform.modules.sys.service.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {

    private final static String title = "用户管理";

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysRoleService sysRoleService;

    /**
     * 用户列表
     */
    @RequiresPermissions("sys:user:list")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = sysUserService.queryDateList(user);
        return getDataTable(list);
    }

    /**
     * 新增用户
     */
    @SubmitRepeat
    @RequiresPermissions("sys:user:add")
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        sysUserService.addUser(user);
        return AjaxResult.success();
    }

    /**
     * 修改用户
     */
    @SubmitRepeat
    @RequiresPermissions("sys:user:edit")
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @PostMapping("/edit")
    @DemoRepeat
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        sysUserService.updateUser(user);
        return AjaxResult.success();
    }

    /**
     * 删除用户
     */
    @SubmitRepeat
    @DemoRepeat
    @RequiresPermissions("sys:user:remove")
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/delete/{userId}")
    public AjaxResult remove(@PathVariable Long userId) {
        sysUserService.deleteUser(userId);
        return AjaxResult.success();
    }

    /**
     * 重置密码
     */
    @SubmitRepeat
    @RequiresPermissions("sys:user:edit")
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @PostMapping("/resetPwd")
    @DemoRepeat
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        sysUserService.updatePwd(user.getUserId(), user.getPassword());
        return AjaxResult.successMsg("重置成功");
    }

    /**
     * 角色列表
     */
    @RequiresPermissions("sys:user:list")
    @GetMapping("/roleList")
    public AjaxResult roleList() {
        orderBy("roleSort");
        List<SysRole> roleList = sysRoleService.queryList(new SysRole());
        List<LabelVo> dataList = new ArrayList<>();
        roleList.forEach(data -> {
            dataList.add(new LabelVo(data.getRoleName(), data.getRoleId()));
        });
        return AjaxResult.success(dataList);
    }

}