package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatUserAppeal;

/**
 * <p>
 * 用户申诉 服务层
 * </p>
 */
public interface ChatUserAppealService extends BaseService<ChatUserAppeal> {

    /**
     * 查询记录
     */
    ChatUserAppeal queryAppeal(Long userId);
}
