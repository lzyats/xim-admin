package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.common.web.vo.LabelVo;
import com.platform.modules.chat.domain.ChatRobotReply;
import com.platform.modules.chat.enums.RobotReplyEnum;

import java.util.List;

/**
 * <p>
 * 服务号 服务层
 * </p>
 */
public interface ChatRobotReplyService extends BaseService<ChatRobotReply> {

    /**
     * 查询列表
     */
    List<ChatRobotReply> queryDataList(Long robotId, RobotReplyEnum replyType);

    /**
     * 事件列表
     */
    List<LabelVo> queryEvenList(Long robotId);

    /**
     * 新增
     */
    void addReply(ChatRobotReply chatRobotReply);

    /**
     * 修改
     */
    void editReply(ChatRobotReply chatRobotReply);

    /**
     * 复制
     */
    void copyReply(Long replyId);

}
