package com.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatMsg;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 聊天消息 数据库访问层
 * </p>
 */
@Repository
public interface ChatMsgDao extends BaseDao<ChatMsg> {

    /**
     * 查询列表
     */
    List<ChatMsg> queryList(ChatMsg chatMsg);

    /**
     * 查询列表
     */
    List<ChatMsg> queryMessage(Long groupId);

}
