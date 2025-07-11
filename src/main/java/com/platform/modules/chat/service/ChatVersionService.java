package com.platform.modules.chat.service;

import com.platform.modules.chat.domain.ChatVersion;
import com.platform.common.web.service.BaseService;

/**
 * <p>
 * 版本 服务层
 * </p>
 */
public interface ChatVersionService extends BaseService<ChatVersion> {

    /**
     * 修改版本
     */
    void editVersion(ChatVersion chatVersion);
}
