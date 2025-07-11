package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatVoice;

/**
 * <p>
 * 声音表 服务层
 * </p>
 */
public interface ChatVoiceService extends BaseService<ChatVoice> {

    /**
     * 新增声音
     */
    void addVoice(Long msgId, String voicePath);

}
