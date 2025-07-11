package com.platform.modules.sys.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.sys.domain.SysRole;

/**
 * 角色业务层
 */
public interface SysRoleService extends BaseService<SysRole> {

    /**
     * 新增角色
     */
    Integer addRole(SysRole sysRole);

    /**
     * 修改角色
     */
    Integer updateRole(SysRole sysRole);

    /**
     * 删除角色
     */
    Integer deleteRole(Long roleId);

}
