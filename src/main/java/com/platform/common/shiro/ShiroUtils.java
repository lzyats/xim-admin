
package com.platform.common.shiro;

import com.platform.common.constant.AppConstants;
import com.platform.common.constant.HeadConstant;
import com.platform.common.utils.ServletUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Shiro工具类
 */
public class ShiroUtils {

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static ShiroUserVo getLoginUser() {
        return (ShiroUserVo) getSubject().getPrincipal();
    }

    /**
     * getToken
     */
    public static String getToken() {
        ShiroUserVo loginUser = getLoginUser();
        if (loginUser != null) {
            return loginUser.getToken();
        }
        return ServletUtils.getRequest().getHeader(HeadConstant.TOKEN_HEADER_ADMIN);
    }

    /**
     * 用户信息
     */
    public static Long getUserId() {
        return getLoginUser().getUserId();
    }

    /**
     * 用户信息
     */
    public static String getUsername() {
        return getLoginUser().getUsername();
    }

    /**
     * 角色信息
     */
    public static Long getRoleId() {
        return getLoginUser().getRoleId();
    }

    public static String getLastId() {
        return getLoginUser().getLastId();
    }

    /**
     * 管理员
     */
    public static boolean isAdmin() {
        return isAdmin(getUsername());
    }

    public static String getSign() {
        return getLoginUser().getSign();
    }

    /**
     * 管理员
     */
    public static boolean isAdmin(String username) {
        return AppConstants.USERNAME.equals(username);
    }

    /**
     * 是否登录
     */
    public static boolean isLogin() {
        Subject subject = getSubject();
        return subject != null && subject.getPrincipal() != null;
    }

}
