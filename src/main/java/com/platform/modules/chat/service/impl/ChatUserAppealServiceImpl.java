package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DateUtil;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatUserAppealDao;
import com.platform.modules.chat.domain.ChatUserAppeal;
import com.platform.modules.chat.service.ChatUserAppealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户申诉 服务层实现
 * </p>
 */
@Service("chatUserAppealService")
public class ChatUserAppealServiceImpl extends BaseServiceImpl<ChatUserAppeal> implements ChatUserAppealService {

    @Resource
    private ChatUserAppealDao chatUserAppealDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatUserAppealDao);
    }

    @Override
    public List<ChatUserAppeal> queryList(ChatUserAppeal t) {
        List<ChatUserAppeal> dataList = chatUserAppealDao.queryList(t);
        return dataList;
    }

    @Override
    public ChatUserAppeal queryAppeal(Long userId) {
        // 查询
        ChatUserAppeal chatUserAppeal = this.getById(userId);
        if (chatUserAppeal == null) {
            chatUserAppeal = new ChatUserAppeal(userId)
                    .setContent("-")
                    .setImages("[]")
                    .setCreateTime(DateUtil.date());
        }
        return chatUserAppeal;
    }
}
