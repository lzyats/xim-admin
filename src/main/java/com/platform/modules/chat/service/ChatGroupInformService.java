package com.platform.modules.chat.service;

import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatGroupInform;

/**
 * <p>
 * 骚扰举报 服务层
 * </p>
 */
public interface ChatGroupInformService extends BaseService<ChatGroupInform> {

    /**
     * 列表
     */
    PageInfo queryDataList(ChatGroupInform chatGroupInform);

    /**
     * 忽略
     */
    void ignore(Long informId);

}
