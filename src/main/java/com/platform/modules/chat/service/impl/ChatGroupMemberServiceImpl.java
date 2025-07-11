package com.platform.modules.chat.service.impl;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatGroupMemberDao;
import com.platform.modules.chat.domain.ChatGroupMember;
import com.platform.modules.chat.service.ChatGroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务层实现
 * </p>
 */
@Service("chatGroupMemberService")
public class ChatGroupMemberServiceImpl extends BaseServiceImpl<ChatGroupMember> implements ChatGroupMemberService {

    @Resource
    private ChatGroupMemberDao chatGroupMemberDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatGroupMemberDao);
    }

    @Override
    public List<ChatGroupMember> queryList(ChatGroupMember t) {
        List<ChatGroupMember> dataList = chatGroupMemberDao.queryList(t);
        return dataList;
    }


    @Override
    public Map<Long, ChatGroupMember> queryConfigMap(Long groupId) {
        List<ChatGroupMember> dataList = chatGroupMemberDao.queryGroup(groupId);
        // 转换
        return dataList.stream().collect(HashMap::new, (x, y) -> {
            x.put(y.getUserId(), y);
        }, HashMap::putAll);
    }

}
