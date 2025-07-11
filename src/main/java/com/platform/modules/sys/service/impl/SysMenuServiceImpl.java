package com.platform.modules.sys.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.platform.common.constant.HeadConstant;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.sys.dao.SysMenuDao;
import com.platform.modules.sys.domain.SysMenu;
import com.platform.modules.sys.enums.SysMenuTypeEnum;
import com.platform.modules.sys.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 菜单 业务层处理
 */
@Service("sysMenuService")
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenu> implements SysMenuService {

    @Resource
    private SysMenuDao menuDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(menuDao);
    }

    /**
     * 根据用户查询系统菜单列表
     */
    @Override
    public List<SysMenu> queryList(SysMenu menu) {
        List<SysMenu> menuList = menuDao.queryList(menu);
        return menuList;
    }

    @Override
    public Set<String> queryPerms(String username, Long roleId) {
        List<SysMenu> dataList;
        if (ShiroUtils.isAdmin(username)) {
            dataList = this.queryList(new SysMenu().setStatus(YesOrNoEnum.YES));
        } else {
            List<SysMenu> permList = menuDao.queryDataList(new SysMenu().setStatus(YesOrNoEnum.YES).setRoleId(roleId));
            dataList = buildMenus(new SysMenu(), permList);
        }
        Set<String> permissions = new HashSet<>();
        for (SysMenu sysMenu : dataList) {
            if (!StringUtils.isEmpty(sysMenu.getPerms())) {
                permissions.add(sysMenu.getPerms().trim());
            }
        }
        permissions.add(HeadConstant.PERM_ADMIN);
        return permissions;
    }

    /**
     * 根据角色ID查询菜单树信息
     */
    @Override
    public List<Long> queryPerms(Long roleId) {
        // 查询
        List<SysMenu> menuList = menuDao.queryDataList(new SysMenu().setStatus(YesOrNoEnum.YES).setRoleId(roleId));
        if (CollectionUtils.isEmpty(menuList)) {
            return new ArrayList();
        }
        // 转换
        List<Long> dataList = menuList.stream().map(SysMenu::getMenuId).collect(Collectors.toList());
        // list转map
        HashMap<Long, Long> menuMap = this.queryList(new SysMenu().setStatus(YesOrNoEnum.YES)).stream().collect(HashMap::new, (x, y) -> {
            x.put(y.getMenuId(), y.getParentId());
        }, HashMap::putAll);
        // 循环map
        for (Map.Entry<Long, Long> entry : menuMap.entrySet()) {
            Long menuId = entry.getKey();
            if (!dataList.contains(menuId)) {
                // 获取列表
                dataList.removeAll(queryParent(menuMap, menuId));
            }
        }
        return dataList;
    }

    /**
     * 根据menuId查询parentId
     */
    private static List<Long> queryParent(Map<Long, Long> menuMap, Long menuId) {
        Long parentId = menuMap.get(menuId);
        if (parentId == null) {
            return new ArrayList<>();
        }
        List<Long> dataList = new ArrayList<>();
        dataList.add(menuId);
        dataList.addAll(queryParent(menuMap, parentId));
        return dataList;
    }

    @Override
    public List<Tree<String>> getPermsTree() {
        List<SysMenu> dataList;
        if (ShiroUtils.isAdmin()) {
            dataList = this.queryList(new SysMenu().setStatus(YesOrNoEnum.YES));
        } else {
            dataList = menuDao.queryDataList(new SysMenu().setStatus(YesOrNoEnum.YES).setRoleId(ShiroUtils.getRoleId()));
            dataList = buildMenus(new SysMenu(), dataList);
        }
        return buildPermsTree(dataList);
    }

    private List<Tree<String>> buildPermsTree(List<SysMenu> menus) {
        if (CollectionUtils.isEmpty(menus)) {
            return null;
        }
        // 定义返回值
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("key");
        treeNodeConfig.setNameKey("title");
        treeNodeConfig.setWeightKey("sort");
        List<Tree<String>> treeList = TreeUtil.build(menus, "0", treeNodeConfig, (sysMenu, tree) -> {
            tree.setId(NumberUtil.toStr(sysMenu.getMenuId()));
            tree.setParentId(NumberUtil.toStr(sysMenu.getParentId()));
            tree.setName(sysMenu.getMenuName());
            tree.setWeight(sysMenu.getSort());
        });
        return treeList;
    }

    @Override
    public List<Dict> getRouters() {
        List<SysMenu> dataList;
        if (ShiroUtils.isAdmin()) {
            dataList = this.queryList(new SysMenu().setStatus(YesOrNoEnum.YES));
        } else {
            List<SysMenu> permList = menuDao.queryDataList(new SysMenu().setStatus(YesOrNoEnum.YES).setRoleId(ShiroUtils.getRoleId()));
            dataList = buildMenus(new SysMenu(), permList);
        }
        if (CollectionUtils.isEmpty(dataList)) {
            return new ArrayList<>();
        }
        dataList = dataList.stream().filter(menu -> SysMenuTypeEnum.C.equals(menu.getMenuType())).collect(Collectors.toList());
        return buildRouters(dataList);
    }

    private List<Dict> buildRouters(List<SysMenu> menus) {
        // list转Obj
        return menus.stream().collect(ArrayList::new, (x, menu) -> {
            String path = menu.getPath();
            if (StringUtils.isEmpty(path)) {
                path = "/";
            } else if (!path.startsWith("/")) {
                path = "/" + path;
            }
            Dict dict = Dict.create()
                    .set("path", path)
                    .set("name", NumberUtil.toStr(menu.getMenuId()))
                    .set("meta", Dict.create()
                            .set("menuId", NumberUtil.toStr(menu.getMenuId()))
                            .set("component", menu.getComponent())
                            .set("title", menu.getMenuName())
                            .set("icon", menu.getIcon())
                            .set("frameFlag", YesOrNoEnum.transform(menu.getFrameFlag()))
                            .set("frameUrl", menu.getPath())
                    );
            x.add(dict);
        }, ArrayList::addAll);
    }

    @Override
    public List<Tree<String>> getMenuTree(SysMenu sysMenu) {
        List<SysMenu> dataList;
        if (ShiroUtils.isAdmin()) {
            dataList = this.queryList(sysMenu.setStatus(YesOrNoEnum.YES));
        } else {
            List<SysMenu> permList = menuDao.queryDataList(sysMenu.setStatus(YesOrNoEnum.YES).setRoleId(ShiroUtils.getRoleId()));
            dataList = buildMenus(sysMenu, permList);
        }
        // 过滤
        if (sysMenu.getMenuType() == null) {
            dataList = dataList.stream().filter(menu -> SysMenuTypeEnum.M.equals(menu.getMenuType()) || SysMenuTypeEnum.C.equals(menu.getMenuType())).collect(Collectors.toList());
        }
        return buildMenuTree(dataList);
    }

    // 过滤菜单
    private List<SysMenu> buildMenus(SysMenu query, List<SysMenu> permList) {
        // 查询全部
        List<SysMenu> menuList = this.queryList(query.setStatus(YesOrNoEnum.YES));
        // 去重
        Map<Long, SysMenu> group = menuList.stream().collect(Collectors.toMap(SysMenu::getMenuId, a -> a, (k1, k2) -> k1));
        // 结果
        List<SysMenu> dataList = new ArrayList<>();
        // 对每个权限菜单进行处理
        permList.forEach(menu -> _buildMenus(menu, group, dataList));
        return dataList;
    }

    // 过滤菜单
    private void _buildMenus(SysMenu menu, Map<Long, SysMenu> group, List<SysMenu> dataList) {
        if (menu == null) {
            return;
        }
        // 添加当前菜单
        dataList.add(menu);
        // 获取并添加父级菜单
        SysMenu parentMenu = group.get(menu.getParentId());
        if (parentMenu != null) {
            _buildMenus(parentMenu, group, dataList);
        }
    }

    private List<Tree<String>> buildMenuTree(List<SysMenu> menus) {
        if (CollectionUtils.isEmpty(menus)) {
            return null;
        }
        // 定义返回值
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("menuId");
        treeNodeConfig.setParentIdKey("parentId");
        treeNodeConfig.setNameKey("menuName");
        treeNodeConfig.setWeightKey("sort");
        List<Tree<String>> treeList = TreeUtil.build(menus, "0", treeNodeConfig, (sysMenu, tree) -> {
            tree.setId(NumberUtil.toStr(sysMenu.getMenuId()));
            tree.setParentId(NumberUtil.toStr(sysMenu.getParentId()));
            tree.setName(sysMenu.getMenuName());
            tree.setWeight(sysMenu.getSort());
            String path = sysMenu.getPath();
            if (StringUtils.isEmpty(path)) {
                path = "/";
            } else if (!path.startsWith("/")) {
                path = "/" + path;
            }
            tree.putExtra("path", path);
            tree.putExtra("icon", sysMenu.getIcon());
        });
        return treeList;
    }


    /**
     * 新增保存菜单信息
     */
    @Override
    public Integer addMenu(SysMenu sysMenu) {
        sysMenu.setMenuId(DateUtil.currentSeconds());
        if (!SysMenuTypeEnum.C.equals(sysMenu.getMenuType())) {
            sysMenu.setPath(null);
            sysMenu.setComponent(null);
            sysMenu.setFrameFlag(YesOrNoEnum.NO);
            sysMenu.setFrameUrl(null);
            if (SysMenuTypeEnum.F.equals(sysMenu.getMenuType())) {
                sysMenu.setIcon("#");
            }
        }
        if (SysMenuTypeEnum.M.equals(sysMenu.getMenuType())) {
            sysMenu.setPerms(null);
        }
        return this.add(sysMenu);
    }

    @Transactional
    @Override
    public Integer editMenu(SysMenu sysMenu) {
        Integer result = this.updateById(sysMenu);
        if (!SysMenuTypeEnum.C.equals(sysMenu.getMenuType())) {
            String perms = sysMenu.getPerms();
            String icon = sysMenu.getIcon();
            if (SysMenuTypeEnum.M.equals(sysMenu.getMenuType())) {
                perms = null;
            } else {
                icon = "#";
            }
            this.update(Wrappers.<SysMenu>lambdaUpdate()
                    .set(SysMenu::getPath, null)
                    .set(SysMenu::getComponent, null)
                    .set(SysMenu::getPerms, perms)
                    .set(SysMenu::getIcon, icon)
                    .set(SysMenu::getFrameFlag, YesOrNoEnum.NO)
                    .set(SysMenu::getFrameUrl, null)
                    .eq(SysMenu::getMenuId, sysMenu.getMenuId()));
        }
        return result;
    }

    private static AtomicInteger count;

    private static Long countId;

    private void initCount() {
        count = new AtomicInteger(0);
        countId = DateUtil.currentSeconds();
    }

    private Long getCount() {
        return countId + count.incrementAndGet();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer copyMenu(Long menuId) {
        SysMenu sysMenu = findById(menuId);
        initCount();
        // 查询所有菜单
        List<SysMenu> menuList = this.queryList(new SysMenu());
        // copy
        sysMenu.setMenuId(getCount());
        sysMenu.setMenuName(sysMenu.getMenuName() + "-copy");
        add(sysMenu);
        // 制作数结构
        List<Tree<Long>> treeList = buildMenuTree(menuList, menuId);
        // copy
        doCopy(treeList, sysMenu.getMenuId());
        return 1;
    }

    private void doCopy(List<Tree<Long>> treeList, Long parentId) {
        if (CollectionUtils.isEmpty(treeList)) {
            return;
        }
        for (Tree<Long> tree : treeList) {
            // copy
            SysMenu sysMenu = getById(tree.getId());
            Long menuId = getCount();
            if (sysMenu != null) {
                sysMenu.setMenuId(menuId);
                sysMenu.setParentId(parentId);
                sysMenu.setMenuName(sysMenu.getMenuName() + "-copy");
                add(sysMenu);
            }
            // 递归
            List<Tree<Long>> children = tree.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                doCopy(children, menuId);
            }
        }
    }

    private List<Tree<Long>> buildMenuTree(List<SysMenu> menus, Long parentId) {
        if (CollectionUtils.isEmpty(menus)) {
            return null;
        }
        // 定义返回值
        List<Tree<Long>> treeList = TreeUtil.build(menus, parentId, (sysMenu, tree) -> {
            tree.setId(sysMenu.getMenuId());
            tree.setParentId(sysMenu.getParentId());
            tree.setName(sysMenu.getMenuName());
            tree.setWeight(sysMenu.getSort());
        });
        return treeList;
    }

    /**
     * 删除菜单
     */
    @Override
    public int deleteMenu(Long menuId) {
        if (this.queryCount(new SysMenu().setParentId(menuId)) > 0) {
            throw new BaseException("存在子菜单,不允许删除");
        }
        return deleteById(menuId);
    }

}
