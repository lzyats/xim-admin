package com.platform.modules.sys.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.sys.domain.SysMenu;

import java.util.List;

/**
 * 菜单表 数据层
 */
public interface SysMenuDao extends BaseDao<SysMenu> {

    List<SysMenu> queryList(SysMenu sysMenu);

    List<SysMenu> queryDataList(SysMenu sysMenu);

}
