package com.platform.modules.sys.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.sys.domain.SysRoleMenu;

/**
 * 角色与菜单关联表 数据层
 */
public interface SysRoleMenuDao extends BaseDao<SysRoleMenu> {

    /**
     * 通过角色ID删除角色和菜单关联
     */
    Integer deleteByRoleId(Long roleId);

}
