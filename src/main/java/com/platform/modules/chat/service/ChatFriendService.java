package com.platform.modules.chat.service;

import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatFriend;

import java.util.Map;

/**
 * <p>
 * 好友表 服务层
 * </p>
 */
public interface ChatFriendService extends BaseService<ChatFriend> {

    /**
     * 列表
     */
    PageInfo queryDataList(Long userId);

    /**
     * 查询配置
     */
    Map<Long, ChatFriend> queryConfigMap(Long groupId);

}
