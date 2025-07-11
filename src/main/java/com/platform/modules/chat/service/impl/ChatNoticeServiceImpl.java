package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DateUtil;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatNoticeDao;
import com.platform.modules.chat.domain.ChatNotice;
import com.platform.modules.chat.service.ChatNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 通知公告 服务层实现
 * </p>
 */
@Service("chatNoticeService")
public class ChatNoticeServiceImpl extends BaseServiceImpl<ChatNotice> implements ChatNoticeService {

    @Resource
    private ChatNoticeDao chatNoticeDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatNoticeDao);
    }

    @Override
    public List<ChatNotice> queryList(ChatNotice t) {
        List<ChatNotice> dataList = chatNoticeDao.queryList(t);
        return dataList;
    }

    @Override
    public void copy(Long noticeId) {
        ChatNotice notice = this.findById(noticeId)
                .setNoticeId(null)
                .setStatus(YesOrNoEnum.NO)
                .setCreateTime(DateUtil.date());
        this.add(notice);
    }
}
