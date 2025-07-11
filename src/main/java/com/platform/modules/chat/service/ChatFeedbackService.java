package com.platform.modules.chat.service;

import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatFeedback;
import com.platform.modules.chat.vo.ChatVo07;

/**
 * <p>
 * 建议反馈 服务层
 * </p>
 */
public interface ChatFeedbackService extends BaseService<ChatFeedback> {

    /**
     * 查询
     */
    PageInfo queryDataList(ChatFeedback chatFeedback);

    /**
     * 查询
     */
    ChatVo07 getInfo(Long feedbackId);
}
