package com.platform.modules.chat.service;

import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.BaseService;
import com.platform.common.web.vo.LabelVo;
import com.platform.modules.chat.domain.ChatUserLog;
import com.platform.modules.chat.enums.UserLogEnum;

import java.util.List;

/**
 * <p>
 * 用户日志 服务层
 * </p>
 */
public interface ChatUserLogService extends BaseService<ChatUserLog> {

    /**
     * 增加日志
     */
    void addLog(Long userId, UserLogEnum logType);

    /**
     * 日活
     */
    Long visit();

    /**
     * 版本
     */
    List<LabelVo> version();

    /**
     * 设备
     */
    List<LabelVo> device();

    /**
     * 查询日志
     */
    PageInfo queryDataList(Long userId);

}
