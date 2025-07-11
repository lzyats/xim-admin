package com.platform.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.modules.sys.domain.SysLog;
import com.platform.modules.sys.domain.SysLogin;
import com.platform.modules.sys.service.SysLogService;
import com.platform.modules.sys.service.SysLoginService;
import lombok.extern.slf4j.Slf4j;

/**
 * 日志工具类
 */
@Slf4j
public class LogUtils {

    /**
     * 记录登陆信息
     */
    public static void recordLogin(final String username, final YesOrNoEnum status, final String message,
                                   final Object... args) {
        final UserAgent userAgent = UserAgentUtil.parse(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ipAddr = IpUtils.getIpAddr(ServletUtils.getRequest());
        StringBuilder sb = new StringBuilder();
        sb.append(getBlock(ipAddr));
        sb.append(ipAddr);
        sb.append(getBlock(username));
        sb.append(status.getInfo());
        sb.append(getBlock(message));
        // 客户端操作系统
        String os = userAgent.getOs().getName();
        // 客户端浏览器
        String browser = userAgent.getBrowser().getName();
        // 封装对象
        SysLogin sysLogin = new SysLogin();
        sysLogin.setUsername(username);
        sysLogin.setIpAddr(ipAddr);
        sysLogin.setLocation(IpUtils.getIpAddr(ipAddr));
        sysLogin.setBrowser(browser);
        sysLogin.setOs(os);
        sysLogin.setMessage(message);
        // 日志状态
        sysLogin.setStatus(status);
        // 时间
        sysLogin.setCreateTime(DateUtil.date());
        // 测试用户-忽略
        if (AppConstants.USERNAME.equalsIgnoreCase(username)) {
            return;
        }
        // 插入数据
        ThreadUtil.execAsync(() -> {
            // 打印信息到日志
            log.info(sb.toString(), args);
            // 写入日志
            SpringUtil.getBean(SysLoginService.class).add(sysLogin);
        });
    }

    /**
     * 操作日志记录
     */
    public static void recordOper(final SysLog sysLog) {
        // 测试用户-忽略
        if (AppConstants.USERNAME.equalsIgnoreCase(sysLog.getUsername())) {
            return;
        }
        ThreadUtil.execAsync(() -> {
            SpringUtil.getBean(SysLogService.class).add(sysLog);
        });
    }

    private static String getBlock(Object msg) {
        if (msg == null) {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }
}
