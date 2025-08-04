package com.platform.modules.chat.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.service.ChatUserSignService;
import com.platform.modules.chat.dao.ChatUserSignDao;
import com.platform.modules.chat.domain.ChatUserSign;

/**
 * <p>
 * 用户按天签到记录 服务层实现
 * </p>
 */
@Service("chatUserSignService")
public class ChatUserSignServiceImpl extends BaseServiceImpl<ChatUserSign> implements ChatUserSignService {

    @Resource
    private ChatUserSignDao chatUserSignDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatUserSignDao);
    }

    @Override
    public List<ChatUserSign> queryList(ChatUserSign t) {
        List<ChatUserSign> dataList = chatUserSignDao.queryList(t);
        return dataList;
    }

}
