package com.platform.modules.sys.service;

import cn.hutool.core.lang.Dict;
import com.platform.common.web.service.BaseService;
import com.platform.modules.sys.domain.SysUser;

import java.util.List;

/**
 * 用户 业务层
 */
public interface SysUserService extends BaseService<SysUser> {

    /**
     * 查询全部记录
     */
    Dict getInfo();

    /**
     * 查询全部记录
     */
    List<SysUser> queryDateList(SysUser user);

    /**
     * 通过用户名查询用户
     */
    SysUser queryByUsername(String username);

    /**
     * 新增用户信息
     */
    Integer addUser(SysUser user);

    /**
     * 修改用户信息
     */
    Integer updateUser(SysUser user);

    /**
     * 修改用户密码
     */
    void updatePwd(Long userId, String password);

    /**
     * 通过用户ID删除用户
     */
    Integer deleteUser(Long userId);

}
