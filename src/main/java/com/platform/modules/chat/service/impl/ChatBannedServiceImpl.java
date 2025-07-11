package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatBannedDao;
import com.platform.modules.chat.domain.ChatBanned;
import com.platform.modules.chat.enums.BannedTimeEnum;
import com.platform.modules.chat.service.ChatBannedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 封禁状态 服务层实现
 * </p>
 */
@Service("chatBannedService")
public class ChatBannedServiceImpl extends BaseServiceImpl<ChatBanned> implements ChatBannedService {

    @Resource
    private ChatBannedDao chatBannedDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatBannedDao);
    }

    @Override
    public List<ChatBanned> queryList(ChatBanned t) {
        List<ChatBanned> dataList = chatBannedDao.queryList(t);
        return dataList;
    }

    @Override
    public void addBanned(Long userId, BannedTimeEnum bannedTime, String reason) {
        // 删除
        this.deleteById(userId);
        if (BannedTimeEnum.DAY_0.equals(bannedTime)) {
            return;
        }
        DateTime now = DateUtil.date();
        // 增加
        ChatBanned entity = new ChatBanned(userId)
                .setBannedReason(reason)
                .setBannedTime(DateUtil.offsetDay(now, bannedTime.getValue()));
        this.add(entity);
    }

}
