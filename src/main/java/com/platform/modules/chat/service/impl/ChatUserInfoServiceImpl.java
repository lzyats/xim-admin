package com.platform.modules.chat.service.impl;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatUserInfoDao;
import com.platform.modules.chat.domain.ChatUserInfo;
import com.platform.modules.chat.service.ChatUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户详情 服务层实现
 * </p>
 */
@Service("chatUserInfoService")
public class ChatUserInfoServiceImpl extends BaseServiceImpl<ChatUserInfo> implements ChatUserInfoService {

    @Resource
    private ChatUserInfoDao chatUserInfoDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatUserInfoDao);
    }

    @Override
    public List<ChatUserInfo> queryList(ChatUserInfo t) {
        List<ChatUserInfo> dataList = chatUserInfoDao.queryList(t);
        return dataList;
    }

    @Override
    public void addInfo(Long userId) {
        this.add(new ChatUserInfo(userId));
    }
}
