package com.platform.common.config;

import com.platform.common.constant.AppConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "platform")
public class PlatformConfig {

    /**
     * 上传路径
     */
    public static String ROOT_PATH;

    /**
     * token超时时间（小时）
     */
    public static Integer TIMEOUT;

    /**
     * 消息秘钥
     */
    public static String SECRET;

    public void setRootPath(String rootPath) {
        PlatformConfig.ROOT_PATH = rootPath;
    }

    public void setTimeout(Integer timeout) {
        PlatformConfig.TIMEOUT = timeout;
    }

    public void setSecret(String secret) {
        PlatformConfig.SECRET = secret;
    }

    public void setUsername(String username) {
        AppConstants.USERNAME = username;
    }

    public void setPassword(String password) {
        AppConstants.PASSWORD = password;
    }

}