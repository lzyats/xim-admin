package com.platform.common.shiro;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.platform.common.config.PlatformConfig;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.ResultEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.LoginException;
import com.platform.common.utils.EncryptUtils;
import com.platform.common.utils.IpUtils;
import com.platform.common.utils.ServletUtils;
import com.platform.modules.sys.domain.SysRole;
import com.platform.modules.sys.domain.SysUser;
import com.platform.modules.sys.service.SysMenuService;
import com.platform.modules.sys.service.SysRoleService;
import com.platform.modules.sys.service.SysTokenService;
import com.platform.modules.sys.service.SysUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;
import java.util.Set;

/**
 * ShiroRealm
 */
public class ShiroRealm extends AuthorizingRealm {

    @Lazy
    @Resource
    private SysUserService sysUserService;

    @Lazy
    @Resource
    private SysRoleService sysRoleService;

    @Lazy
    @Resource
    private SysMenuService sysMenuService;

    @Lazy
    @Resource
    private SysTokenService sysTokenService;

    /**
     * 提供用户信息，返回权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Object object = ShiroUtils.getSubject().getPrincipal();
        if (object == null) {
            return null;
        }
        // 后台管理
        if (object instanceof ShiroUserVo) {
            ShiroUserVo shiroUserVo = (ShiroUserVo) object;
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            // 添加角色
            info.addRole(shiroUserVo.getRoleKey());
            // 添加权限
            info.addStringPermissions(shiroUserVo.getPermissions());
            return info;
        }
        return null;
    }

    /**
     * 必须重写此方法，不然会报错
     */
    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof ShiroLoginToken
                || authenticationToken instanceof ShiroLoginAuth;
    }

    /**
     * 身份认证/登录，验证用户是不是拥有相应的身份； 用于登陆认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        ShiroUserVo shiroUserVo = null;
        // token登录
        if (authenticationToken instanceof ShiroLoginToken) {
            ShiroLoginToken auth = (ShiroLoginToken) authenticationToken;
            if ((shiroUserVo = sysTokenService.queryToken(auth.getToken())) == null) {
                throw new LoginException(ResultEnum.UNAUTHORIZED);
            }
        }
        // 账号密码登录
        if (authenticationToken instanceof ShiroLoginAuth) {
            ShiroLoginAuth auth = (ShiroLoginAuth) authenticationToken;
            shiroUserVo = makeShiroUserVo(auth.getUsername());
        }
        if (shiroUserVo == null) {
            return null;
        }
        return new SimpleAuthenticationInfo(shiroUserVo, shiroUserVo.getCredentials(), ByteSource.Util.bytes(shiroUserVo.getSalt()), getName());
    }

    /**
     * 组装登录对象
     */
    private ShiroUserVo makeShiroUserVo(String username) {
        // 查询用户
        SysUser sysUser = initUser(username);
        // 查询角色
        SysRole sysRole = initRole(sysUser);
        // 查询权限
        Set<String> permissions = sysMenuService.queryPerms(username, sysUser.getRoleId());
        // 组装对象
        JSONObject jsonObject = new JSONObject()
                .set("timestamp", RandomUtil.randomString(14))
                .set("userId", AppConstants.ROBOT_ID.intValue());
        ShiroUserVo shiroUserVo = new ShiroUserVo()
                .setToken(EncryptUtils.encrypt(JSONUtil.toJsonStr(jsonObject), PlatformConfig.SECRET))
                .setUserId(sysUser.getUserId())
                .setUsername(sysUser.getUsername())
                .setRoleId(sysRole.getRoleId())
                .setRoleKey(sysRole.getRoleKey())
                .setPermissions(permissions)
                .setSign(RandomUtil.randomString(32))
                .setSalt(sysUser.getSalt())
                .setCredentials(sysUser.getPassword());
        // 设置代理信息
        return makeUserAgent(shiroUserVo);
    }

    /**
     * 查询用户
     */
    private SysUser initUser(String username) {
        SysUser sysUser = sysUserService.queryByUsername(username);
        // 处理用户
        if (sysUser == null) {
            throw new AuthenticationException("用户不存在"); // 账号不存在
        }
        if (!YesOrNoEnum.YES.equals(sysUser.getStatus())) {
            throw new LoginException("账号已停用"); // 账号禁用
        }
        return sysUser;
    }

    /**
     * 查询角色
     */
    private SysRole initRole(SysUser sysUser) {
        if (ShiroUtils.isAdmin(sysUser.getUsername())) {
            return SysRole.initPlatform();
        }
        // 查询角色
        SysRole sysRole = sysRoleService.getById(sysUser.getRoleId());
        if (sysRole == null) {
            throw new LoginException("角色不存在");
        }
        if (!YesOrNoEnum.YES.equals(sysRole.getStatus())) {
            throw new LoginException("角色被禁用");
        }
        return sysRole;
    }

    /**
     * 设置用户代理信息
     */
    private ShiroUserVo makeUserAgent(ShiroUserVo shiroUserVo) {
        UserAgent userAgent = UserAgentUtil.parse(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        // 登录ip
        shiroUserVo.setIpAddr(ip);
        // 登录地点
        shiroUserVo.setLocation(IpUtils.getIpAddr(ip));
        // 浏览器类型
        shiroUserVo.setBrowser(userAgent.getBrowser().getName());
        // 操作系统
        shiroUserVo.setOs(userAgent.getOs().getName());
        // 登录时间
        shiroUserVo.setLoginTime(DateUtil.currentSeconds());
        return shiroUserVo;
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        return ShiroUtils.isAdmin() || super.isPermitted(principals, permission);
    }

}
