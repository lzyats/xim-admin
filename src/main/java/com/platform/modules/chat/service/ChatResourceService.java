package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatResource;

import java.util.List;

/**
 * <p>
 * 聊天资源 服务层
 * </p>
 */
public interface ChatResourceService extends BaseService<ChatResource> {

    /**
     * 批量删除
     */
    void batchDelete(List<ChatResource> dataList);

}
