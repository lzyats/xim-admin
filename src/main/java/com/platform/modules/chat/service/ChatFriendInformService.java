package com.platform.modules.chat.service;

import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatFriendInform;

/**
 * <p>
 * 骚扰举报 服务层
 * </p>
 */
public interface ChatFriendInformService extends BaseService<ChatFriendInform> {

    /**
     * 列表
     */
    PageInfo queryDataList(ChatFriendInform chatFriendInform);

    /**
     * 忽略
     */
    void ignore(Long informId);

}
