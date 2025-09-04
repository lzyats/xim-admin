package com.platform.modules.chat.service;

import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatMsg;

/**
 * <p>
 * 聊天消息 服务层
 * </p>
 */
public interface ChatMsgService extends BaseService<ChatMsg> {

    /**
     * 数据消息
     */
    PageInfo getMessage(Long groupId,int todb);

}
