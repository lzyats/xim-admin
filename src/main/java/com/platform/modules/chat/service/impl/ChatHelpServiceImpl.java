package com.platform.modules.chat.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.service.ChatHelpService;
import com.platform.modules.chat.dao.ChatHelpDao;
import com.platform.modules.chat.domain.ChatHelp;

/**
 * <p>
 * 聊天帮助 服务层实现
 * </p>
 */
@Service("chatHelpService")
public class ChatHelpServiceImpl extends BaseServiceImpl<ChatHelp> implements ChatHelpService {

    @Resource
    private ChatHelpDao chatHelpDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatHelpDao);
    }

    @Override
    public List<ChatHelp> queryList(ChatHelp t) {
        List<ChatHelp> dataList = chatHelpDao.queryList(t);
        return dataList;
    }

}
