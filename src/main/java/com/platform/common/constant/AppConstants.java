package com.platform.common.constant;

/**
 * 通用常量信息
 */
public class AppConstants {

    /**
     * 机器人-在线客服
     */
    public static final Long ROBOT_ID = 10001L;

    /**
     * 机器人-支付助手
     */
    public static final Long ROBOT_PAY = 10002L;

    /**
     * 机器人-羊驼助手
     */
    public static final Long ROBOT_PUSH = 10003L;

    /**
     * 管理员
     */
    public static String USERNAME = "eUuGT4eS";

    /**
     * 管理员
     */
    public static String PASSWORD = "Y06FHDki";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_KEY = "captcha:";

    /**
     * chat:no
     */
    public static final String REDIS_CHAT_NO = "chat:no";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_TIMEOUT = 2;

    /**
     * redis_chat_robot
     */
    public static final String REDIS_CHAT_ROBOT = "chat:robot::";

    /**
     * redis_admin
     */
    public static final String REDIS_CHAT_ADMIN = "chat:admin";

    /**
     * redis_notice
     */
    public static final String REDIS_CHAT_NOTICE = "chat:notice";

    /**
     * 注销聊天号码
     */
    public static final String DELETED_CHAT_NO = "00000000";

    /**
     * redis_user
     */
    public static final String REDIS_CHAT_USER = "chat:user::{}";

    /**
     * redis_group
     */
    public static final String REDIS_CHAT_GROUP = "chat:group::{}";

    /**
     * chat:special:{type}
     */
    public static final String REDIS_CHAT_SPECIAL = "chat:special:{}";

    /**
     * chat:wallet:{20240101}:{userId}
     */
    public static final String REDIS_CHAT_WALLET = "chat:wallet:{}:{}";

    // 添加缓存清除
    public static final String REDIS_WALLET_ROBOT = "wallet:cash";


    public static final String REDIS_USER_LISTALL= "user:listall";

    /**
     * 朋友圈缓存
     */
    public static final String REDIS_PUSH_MOMENT= "push:moment:";
    /**
     * 公告缓存
     */
    public static final String REDIS_COMMON_NOTIC= "common:notic";

    public static final String REDIS_COMMON_CONFIG= "common:config";

    /**
     * 系统缓存
     */
    public static final String REDIS_COMMON_SYS= "common:sys";
    public static final String REDIS_COMMON_PORTRAIT= "common:portrait";
}
