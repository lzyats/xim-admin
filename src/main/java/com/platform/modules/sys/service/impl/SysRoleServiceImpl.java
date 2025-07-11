package com.platform.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.common.exception.BaseException;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.sys.dao.SysRoleDao;
import com.platform.modules.sys.dao.SysRoleMenuDao;
import com.platform.modules.sys.dao.SysUserDao;
import com.platform.modules.sys.domain.SysRole;
import com.platform.modules.sys.domain.SysRoleMenu;
import com.platform.modules.sys.domain.SysUser;
import com.platform.modules.sys.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色 业务层处理
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends BaseServiceImpl<SysRole> implements SysRoleService {

    @Resource
    private SysRoleDao roleDao;

    @Resource
    private SysRoleMenuDao roleMenuDao;

    @Resource
    private SysUserDao sysUserDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(roleDao);
    }

    /**
     * 根据条件分页查询角色数据
     */
    @Override
    public List<SysRole> queryList(SysRole sysRole) {
        if (ShiroUtils.isAdmin()){
            sysRole.setRoleKey(null);
        } else {
            sysRole.setRoleKey("admin");
        }
        return roleDao.queryList(sysRole);
    }

    /**
     * 校验名称是否唯一
     */
    private void uniqueName(SysRole sysRole) {
        SysRole query = new SysRole();
        query.setRoleName(sysRole.getRoleName());
        SysRole role = queryOne(query);
        boolean result = role == null
                || !(sysRole.getRoleId() == null)
                || role.getRoleId().equals(sysRole.getRoleId());
        if (!result) {
            throw new BaseException("角色名称已存在");
        }
    }

    /**
     * 校验标识是否唯一
     */
    private void uniqueKey(SysRole sysRole) {
        SysRole query = new SysRole();
        query.setRoleKey(sysRole.getRoleKey());
        SysRole role = queryOne(query);
        boolean result = role == null
                || !(sysRole.getRoleId() == null)
                || role.getRoleId().equals(sysRole.getRoleId());
        if (!result) {
            throw new BaseException("权限字符已存在");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer addRole(SysRole sysRole) {
        uniqueName(sysRole);
        uniqueKey(sysRole);
        // 新增角色信息
        Integer result = 0;
        try {
            result = this.add(sysRole);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            checkException(e);
        }
        // 权限
        this.saveRoleMenu(sysRole);
        return result;
    }

    /**
     * 修改保存角色信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer updateRole(SysRole sysRole) {
        uniqueName(sysRole);
        uniqueKey(sysRole);
        Integer result = 0;
        try {
            result = this.updateById(sysRole);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            checkException(e);
        }
        // 权限
        this.saveRoleMenu(sysRole);
        return result;
    }

    private void checkException(org.springframework.dao.DuplicateKeyException e) {
        if (e.getMessage().contains("role_name")) {
            throw new BaseException("角色名称已存在");
        }
        if (e.getMessage().contains("role_key")) {
            throw new BaseException("权限字符已存在");
        }
        throw new BaseException("不能重复提交，请重试");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer deleteRole(Long roleId) {
        // 校验
        Integer count = sysUserDao.selectCount(new QueryWrapper<>(new SysUser().setRoleId(roleId)));
        if (count > 0) {
            throw new BaseException("角色已分配,不能删除");
        }
        // 删除关联
        roleMenuDao.deleteByRoleId(roleId);
        // 删除角色
        return this.deleteById(roleId);
    }

    /**
     * 保存权限
     */
    private void saveRoleMenu(SysRole sysRole) {
        // 删除
        roleMenuDao.deleteByRoleId(sysRole.getRoleId());
        //
        // 新增用户与角色管理
        List<SysRoleMenu> dataList = new ArrayList<>();
        for (Long menuId : sysRole.getPerms()) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(sysRole.getRoleId());
            rm.setMenuId(menuId);
            dataList.add(rm);
        }
        if (!CollectionUtils.isEmpty(dataList)) {
            roleMenuDao.insertBatchSomeColumn(dataList);
        }
    }

}
