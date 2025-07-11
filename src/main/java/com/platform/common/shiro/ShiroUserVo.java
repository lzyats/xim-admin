package com.platform.common.shiro;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * 登录用户身份权限
 */
@Data
@Accessors(chain = true) // 链式调用
public class ShiroUserVo {

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    /**
     * 用户信息
     */
    private Long userId;

    /**
     * 用户信息
     */
    private String username;

    /**
     * 用户签名
     */
    private String sign;

    /**
     * 角色信息
     */
    private Long roleId;

    /**
     * 角色信息
     */
    private String roleKey;

    /**
     * 登陆时间
     */
    private Long loginTime;

    /**
     * 登录IP地址
     */
    private String ipAddr;

    /**
     * 登录地点
     */
    private String location;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 加密盐
     */
    private String salt;

    /**
     * credentials
     */
    private String credentials;

    /**
     * 消息id
     */
    private String lastId;
}
