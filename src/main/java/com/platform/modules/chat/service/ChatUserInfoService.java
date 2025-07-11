package com.platform.modules.chat.service;

import com.platform.modules.chat.domain.ChatUserInfo;
import com.platform.common.web.service.BaseService;

/**
 * <p>
 * 用户详情 服务层
 * </p>
 */
public interface ChatUserInfoService extends BaseService<ChatUserInfo> {

    /**
     * 新增用户
     */
    void addInfo(Long userId);

}
