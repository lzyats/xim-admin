package com.platform.modules.chat.service;

import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatGroupLog;
import com.platform.modules.chat.enums.GroupLogEnum;

/**
 * <p>
 * 群组日志 服务层
 * </p>
 */
public interface ChatGroupLogService extends BaseService<ChatGroupLog> {

    /**
     * 增加日志
     */
    void addLog(Long groupId, GroupLogEnum logType);

    /**
     * 查询日志
     */
    PageInfo queryDataList(Long groupId);

}
