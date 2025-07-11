package com.platform.modules.sys.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.sys.domain.SysRole;

import java.util.List;

/**
 * 角色表 数据层
 */
public interface SysRoleDao extends BaseDao<SysRole> {

    /**
     * 根据条件分页查询角色数据
     */
    List<SysRole> queryList(SysRole sysRole);

}
