package com.platform.modules.chat.dao;

import com.platform.modules.chat.domain.ChatGroup;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;

import java.util.List;

/**
 * <p>
 * 群组 数据库访问层
 * </p>
 */
@Repository
public interface ChatGroupDao extends BaseDao<ChatGroup> {

    /**
     * 查询列表
     */
    List<ChatGroup> queryList(ChatGroup chatGroup);

    /**
     * 查询列表
     */
    List<ChatGroup> queryDataList(ChatGroup chatGroup);

    /**
     * 群组列表
     */
    List<ChatGroup> groupList(Long userId);

}
