package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatBanned;
import com.platform.modules.chat.enums.BannedTimeEnum;

/**
 * <p>
 * 封禁状态 服务层
 * </p>
 */
public interface ChatBannedService extends BaseService<ChatBanned> {

    /**
     * 新增
     */
    void addBanned(Long userId, BannedTimeEnum bannedTime, String reason);

}
