package com.platform.modules.chat.service;

import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatUserToken;

/**
 * <p>
 * 用户token 服务层
 * </p>
 */
public interface ChatUserTokenService extends BaseService<ChatUserToken> {

    /**
     * 注销
     */
    void deleted(Long userId);

    /**
     * 刷新封禁
     */
    void refreshBanned(Long userId, YesOrNoEnum banned);
}
