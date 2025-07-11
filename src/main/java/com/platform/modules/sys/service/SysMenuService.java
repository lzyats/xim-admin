package com.platform.modules.sys.service;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.tree.Tree;
import com.platform.common.web.service.BaseService;
import com.platform.modules.sys.domain.SysMenu;

import java.util.List;
import java.util.Set;

/**
 * 菜单 业务层
 */
public interface SysMenuService extends BaseService<SysMenu> {

    /**
     * 查询权限
     */
    Set<String> queryPerms(String username, Long roleId);

    /**
     * 角色菜单
     */
    List<Long> queryPerms(Long roleId);

    /**
     * 树结构
     */
    List<Tree<String>> getPermsTree();

    /**
     * 路由信息
     */
    List<Dict> getRouters();

    /**
     * 树结构
     */
    List<Tree<String>> getMenuTree(SysMenu sysMenu);

    /**
     * 新增
     */
    Integer addMenu(SysMenu menu);

    /**
     * 修改
     */
    Integer editMenu(SysMenu menu);

    /**
     * 拷贝
     */
    Integer copyMenu(Long menuId);

    /**
     * 删除
     */
    int deleteMenu(Long menuId);

}
