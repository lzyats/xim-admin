package com.platform.modules.monitor.domain;

import lombok.Data;

import java.util.Date;

/**
 * 当前在线会话
 */
@Data
public class MonitorOnline {
    /**
     * 会话编号
     */
    private String token;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 登录IP地址
     */
    private String ipAddr;

    /**
     * 登录地址
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
     * 登录时间
     */
    private Date loginTime;

}
