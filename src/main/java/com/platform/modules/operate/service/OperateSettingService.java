package com.platform.modules.operate.service;

import java.util.Date;

/**
 * <p>
 * 数据中心 服务层
 * </p>
 */
public interface OperateSettingService {

    /**
     * 清理头像
     */
    String cleanPortrait(Date createTime);

    /**
     * 清理消息
     */
    String cleanMessage(Date createTime);

    /**
     * 清理交易
     */
    String cleanTrade(Date createTime);

}
