package com.platform.modules.sys.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.sys.domain.SysUser;

import java.util.List;

/**
 * 用户表 数据层
 */
public interface SysUserDao extends BaseDao<SysUser> {

    /**
     * 根据条件分页查询用户列表
     */
    List<SysUser> queryList(SysUser sysUser);

}
