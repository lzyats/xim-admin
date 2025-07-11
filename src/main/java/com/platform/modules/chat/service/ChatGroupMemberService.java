package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatGroupMember;

import java.util.Map;

/**
 * <p>
 * 服务层
 * </p>
 */
public interface ChatGroupMemberService extends BaseService<ChatGroupMember> {

    /**
     * 查询配置
     */
    Map<Long, ChatGroupMember> queryConfigMap(Long groupId);
}
