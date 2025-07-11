package com.platform.modules.chat.service.impl;

import com.github.pagehelper.PageInfo;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatFeedbackDao;
import com.platform.modules.chat.domain.ChatFeedback;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.service.ChatFeedbackService;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.chat.vo.ChatVo07;
import com.platform.modules.push.enums.PushAuditEnum;
import com.platform.modules.ws.service.HookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 建议反馈 服务层实现
 * </p>
 */
@Service("chatFeedbackService")
public class ChatFeedbackServiceImpl extends BaseServiceImpl<ChatFeedback> implements ChatFeedbackService {

    @Resource
    private ChatFeedbackDao chatFeedbackDao;

    @Resource
    private ChatUserService chatUserService;

    @Resource
    private HookService hookService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatFeedbackDao);
    }

    @Override
    public List<ChatFeedback> queryList(ChatFeedback t) {
        List<ChatFeedback> dataList = chatFeedbackDao.queryList(t);
        return dataList;
    }

    @Override
    public PageInfo queryDataList(ChatFeedback chatFeedback) {
        // 查询
        List<ChatFeedback> dataList = this.queryList(chatFeedback);
        // list转Obj
        List<ChatVo07> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            x.add(format(y));
        }, ArrayList::addAll);
        // 通知
        Long count = this.queryCount(chatFeedback.setStatus(YesOrNoEnum.NO));
        hookService.handle(PushAuditEnum.USER_FEEDBACK, count);
        // 返回
        return getPageInfo(dictList, dataList);
    }

    @Override
    public ChatVo07 getInfo(Long feedbackId) {
        ChatFeedback feedback = findById(feedbackId);
        return format(feedback);
    }

    /**
     * 格式化数据
     */
    private ChatVo07 format(ChatFeedback feedback) {
        ChatUser chatUser = chatUserService.queryCache(feedback.getUserId());
        return new ChatVo07(feedback, chatUser);
    }
}
