package com.platform.modules.chat.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatMsgDao;
import com.platform.modules.chat.domain.ChatFriend;
import com.platform.modules.chat.domain.ChatGroupMember;
import com.platform.modules.chat.domain.ChatMsg;
import com.platform.modules.chat.enums.ChatTalkEnum;
import com.platform.modules.chat.service.ChatFriendService;
import com.platform.modules.chat.service.ChatGroupMemberService;
import com.platform.modules.chat.service.ChatMsgService;
import com.platform.modules.push.enums.PushMsgTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 聊天消息 服务层实现
 * </p>
 */
@Service("chatMsgService")
public class ChatMsgServiceImpl extends BaseServiceImpl<ChatMsg> implements ChatMsgService {

    @Resource
    private ChatMsgDao chatMsgDao;

    @Resource
    private ChatGroupMemberService chatGroupMemberService;

    @Resource
    private ChatFriendService chatFriendService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatMsgDao);
    }

    @Override
    public List<ChatMsg> queryList(ChatMsg t) {
        List<ChatMsg> dataList = chatMsgDao.queryList(t);
        return dataList;
    }

    @Override
    public PageInfo getMessage(Long groupId) {
        // 查询
        List<ChatMsg> messageList = chatMsgDao.queryMessage(groupId);
        // 集合判空
        if (CollectionUtils.isEmpty(messageList)) {
            return new PageInfo();
        }
        // 结果
        List<Dict> dataList = new ArrayList<>();
        // 群组
        if (ChatTalkEnum.GROUP.equals(messageList.get(0).getTalkType())) {
            Map<Long, ChatGroupMember> configMap = chatGroupMemberService.queryConfigMap(groupId);
            // 处理数据
            messageList.forEach(message -> {
                ChatGroupMember member = configMap.get(message.getUserId());
                Dict data = Dict.create()
                        .set(ChatMsg.LABEL_USER_ID, member.getUserId())
                        .set(ChatMsg.LABEL_USER_NO, member.getUserNo())
                        .set(ChatMsg.LABEL_PORTRAIT, member.getPortrait())
                        .set(ChatMsg.LABEL_NICKNAME, member.getDefaultRemark())
                        .set(ChatMsg.LABEL_TITLE, member.getMemberType().getInfo())
                        .set(ChatMsg.LABEL_MSG_TYPE, message.getMsgType())
                        .set(ChatMsg.LABEL_CONTENT, JSONUtil.parseObj(message.getContent()))
                        .set(ChatMsg.LABEL_CREATE_TIME, message.getCreateTime());
                if (PushMsgTypeEnum.RECALL.equals(message.getMsgType())) {
                    data.set(ChatMsg.LABEL_MSG_TYPE, PushMsgTypeEnum.TEXT);
                    data.set(ChatMsg.LABEL_CONTENT, JSONUtil.parseObj("{\"data\":\"[撤回了一条消息]\"}"));
                }
                dataList.add(0, data);
            });
        }
        // 好友
        else {
            Map<Long, ChatFriend> configMap = chatFriendService.queryConfigMap(groupId);
            // 处理数据
            messageList.forEach(message -> {
                ChatFriend friend = configMap.get(message.getUserId());
                Dict data = Dict.create()
                        .set(ChatMsg.LABEL_USER_ID, friend.getUserId())
                        .set(ChatMsg.LABEL_USER_NO, friend.getUserNo())
                        .set(ChatMsg.LABEL_PORTRAIT, friend.getPortrait())
                        .set(ChatMsg.LABEL_MSG_TYPE, message.getMsgType())
                        .set(ChatMsg.LABEL_NICKNAME, friend.getDefaultRemark())
                        .set(ChatMsg.LABEL_CONTENT, JSONUtil.parseObj(message.getContent()))
                        .set(ChatMsg.LABEL_CREATE_TIME, message.getCreateTime());
                if (PushMsgTypeEnum.RECALL.equals(message.getMsgType())) {
                    data.set(ChatMsg.LABEL_MSG_TYPE, PushMsgTypeEnum.TEXT);
                    data.set(ChatMsg.LABEL_CONTENT, JSONUtil.parseObj("{\"data\":\"[撤回了一条消息]\"}"));
                }
                dataList.add(0, data);
            });
        }
        return getPageInfo(dataList, messageList);
    }

}
