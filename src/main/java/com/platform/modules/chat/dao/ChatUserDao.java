package com.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatUser;
import org.springframework.stereotype.Repository;
import com.platform.modules.chat.vo.ChatUserVo01;

import java.util.List;

/**
 * <p>
 * 用户表 数据库访问层
 * </p>
 */
@Repository
public interface ChatUserDao extends BaseDao<ChatUser> {

    /**
     * 查询列表
     */
    List<ChatUser> queryList(ChatUser chatUser);

    /**
     * 查询列表
     */
    List<ChatUser> queryDataList(ChatUser chatUser);

    /**
     * 查询列表
     */
    List<ChatUserVo01> queryListonly();

    /**
     * 查询认证列表
     */
    List<ChatUser> queryAuthList(ChatUser chatUser);

    /**
     * 查询封禁列表
     */
    List<ChatUser> queryBannedList(ChatUser chatUser);

    /**
     * 查询封禁数量
     */
    Long queryBannedCount();

}
