package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatNotice;

/**
 * <p>
 * 通知公告 服务层
 * </p>
 */
public interface ChatNoticeService extends BaseService<ChatNotice> {

    /**
     * 拷贝
     */
    void copy(Long noticeId);
}
