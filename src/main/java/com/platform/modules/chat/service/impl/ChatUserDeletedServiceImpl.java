package com.platform.modules.chat.service.impl;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatUserDeletedDao;
import com.platform.modules.chat.domain.ChatUserDeleted;
import com.platform.modules.chat.service.ChatUserDeletedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 注销表 服务层实现
 * </p>
 */
@Service("chatUserDeletedService")
public class ChatUserDeletedServiceImpl extends BaseServiceImpl<ChatUserDeleted> implements ChatUserDeletedService {

    @Resource
    private ChatUserDeletedDao chatUserDeletedDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatUserDeletedDao);
    }

    @Override
    public List<ChatUserDeleted> queryList(ChatUserDeleted t) {
        List<ChatUserDeleted> dataList = chatUserDeletedDao.queryList(t);
        return dataList;
    }

    @Override
    public void deleted(Long userId, String phone) {
        this.add(new ChatUserDeleted(userId, phone));
    }

}
