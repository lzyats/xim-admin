package com.platform.modules.chat.dao;

import com.platform.modules.chat.domain.ChatFriend;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 好友表 数据库访问层
 * </p>
 */
@Repository
public interface ChatFriendDao extends BaseDao<ChatFriend> {

    /**
     * 查询列表
     */
    List<ChatFriend> queryList(ChatFriend chatFriend);

    /**
     * 查询列表
     */
    List<ChatFriend> queryGroup(Long groupId);

}
