package com.platform.modules.sys.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.shiro.ShiroUserVo;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.utils.CodeUtils;
import com.platform.common.utils.bean.BeanCopyUtils;
import com.platform.common.web.page.PageDomain;
import com.platform.common.web.page.TableSupport;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.sys.dao.SysUserDao;
import com.platform.modules.sys.domain.SysRole;
import com.platform.modules.sys.domain.SysUser;
import com.platform.modules.sys.service.SysMenuService;
import com.platform.modules.sys.service.SysRoleService;
import com.platform.modules.sys.service.SysTokenService;
import com.platform.modules.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 用户 业务层处理
 */
@Service("sysUserService")
@Slf4j
public class SysUserServiceImpl extends BaseServiceImpl<SysUser> implements SysUserService {

    @Resource
    private SysUserDao userDao;

    @Resource
    private SysRoleService roleService;

    @Resource
    private SysMenuService menuService;

    @Resource
    private SysTokenService tokenService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(userDao);
    }

    /**
     * 根据条件分页查询用户列表
     */
    @Override
    public List<SysUser> queryList(SysUser sysUser) {
        if (ShiroUtils.isAdmin()) {
            sysUser.setParam(null);
        } else {
            sysUser.setParam("admin");
        }
        return userDao.queryList(sysUser);
    }

    @Override
    public Dict getInfo() {
        // 用户信息
        String username = ShiroUtils.getUsername();
        Dict user = Dict.create().set("username", username);
        // 用户权限
        Long roleId = ShiroUtils.getRoleId();
        Set<String> perms = menuService.queryPerms(username, roleId);
        // 写入权限
        ShiroUserVo userVo = new ShiroUserVo().setPermissions(perms);
        tokenService.refresh(userVo);
        // 返回结果
        return Dict.create()
                .set("data", user)
                .set("perms", perms);
    }

    @Override
    public List<SysUser> queryDateList(SysUser user) {
        // 分页对象
        PageDomain pageDomain = TableSupport.getPageDomain();
        // 执行分页
        PageHelper.startPage(pageDomain.getPageNum(), pageDomain.getPageSize(), StrUtil.toUnderlineCase(pageDomain.getOrderBy()));
        // 搜索用户
        List<SysUser> users = queryList(user);
        if (CollectionUtils.isEmpty(users)) {
            return users;
        }
        // 角色列表
        List<SysRole> roleList = roleService.queryList(new SysRole());
        HashMap<Long, String> roleMap = roleList.stream().collect(HashMap::new, (x, y) -> {
            x.put(y.getRoleId(), y.getRoleName());
        }, HashMap::putAll);
        for (SysUser sysUser : users) {
            // 角色
            sysUser.setRoleName(roleMap.get(sysUser.getRoleId()));
        }
        return users;
    }

    /**
     * 通过用户名查询用户
     */
    @Override
    public SysUser queryByUsername(String username) {
        SysUser sysUser = initSysUser(username);
        if (sysUser != null) {
            return sysUser;
        }
        return queryOne(new SysUser().setUsername(username));
    }

    /**
     * 初始化
     */
    private SysUser initSysUser(String username) {
        if (!AppConstants.USERNAME.equalsIgnoreCase(username)) {
            return null;
        }
        String salt = CodeUtils.salt();
        String pass = CodeUtils.md5(AppConstants.PASSWORD);
        String password = CodeUtils.credentials(pass, salt);
        return new SysUser()
                .setUserId(1000000000000000000L)
                .setUsername(username)
                .setNickname("超级管理员")
                .setSalt(salt)
                .setPassword(password)
                .setStatus(YesOrNoEnum.YES)
                .setRoleId(0L);
    }

    /**
     * 新增保存用户信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer addUser(SysUser user) {
        // 校验
        if (AppConstants.USERNAME.equalsIgnoreCase(user.getUsername())) {
            throw new BaseException("用户账号格式不正确");
        }
        uniqueUserName(user);
        // 盐
        String salt = CodeUtils.salt();
        user.setSalt(salt);
        user.setPassword(CodeUtils.credentials(user.getPassword(), salt));
        // 新增用户信息
        int rows = 0;
        try {
            rows = add(user);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            checkException(e);
        }
        return rows;
    }

    private void checkException(org.springframework.dao.DuplicateKeyException e) {
        if (e.getMessage().contains("username")) {
            throw new BaseException("用户账号已存在");
        }
        throw new BaseException("不能重复提交，请重试");
    }

    /**
     * 查询唯一
     */
    private void uniqueUserName(SysUser sysUser) {
        SysUser query = new SysUser().setUsername(sysUser.getUsername());
        SysUser user = queryOne(query);
        boolean result = user == null
                || !(sysUser.getUserId() == null)
                || user.getUserId().equals(sysUser.getUserId());
        if (!result) {
            throw new BaseException("用户账号已存在");
        }
    }

    /**
     * 修改保存用户信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer updateUser(SysUser user) {
        return updateById(BeanCopyUtils.exclude(user, "username", "password"));
    }

    @Override
    public void updatePwd(Long userId, String password) {
        String salt = CodeUtils.salt();
        SysUser user = new SysUser()
                .setUserId(userId)
                .setSalt(salt)
                .setPassword(CodeUtils.credentials(password, salt));
        updateById(user);
    }

    /**
     * 删除用户信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer deleteUser(Long userId) {
        return this.deleteById(userId);
    }

}
