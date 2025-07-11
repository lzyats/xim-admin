package com.platform.modules.chat.service.impl;

import cn.hutool.core.lang.Dict;
import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatFriendDao;
import com.platform.modules.chat.domain.ChatFriend;
import com.platform.modules.chat.service.ChatFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 好友表 服务层实现
 * </p>
 */
@Service("chatFriendService")
public class ChatFriendServiceImpl extends BaseServiceImpl<ChatFriend> implements ChatFriendService {

    @Resource
    private ChatFriendDao chatFriendDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatFriendDao);
    }

    @Override
    public List<ChatFriend> queryList(ChatFriend t) {
        List<ChatFriend> dataList = chatFriendDao.queryList(t);
        return dataList;
    }

    @Override
    public PageInfo queryDataList(Long userId) {
        // 查询
        List<ChatFriend> dataList = this.queryList(new ChatFriend(userId));
        // 转换
        List<Dict> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            Dict dict = Dict.create().parseBean(y)
                    .filter(ChatFriend.LABEL_GROUP_ID
                            ,ChatFriend.LABEL_USER_ID
                            ,ChatFriend.LABEL_USER_NO
                            ,ChatFriend.LABEL_NICKNAME
                            ,ChatFriend.LABEL_PORTRAIT
                            ,ChatFriend.LABEL_CREATE_TIME)
                    .set(ChatFriend.LABEL_SOURCE, y.getSource().getInfo())
                    .set(ChatFriend.LABEL_REMARK, y.getRemark());
            x.add(dict);
        }, ArrayList::addAll);
        return getPageInfo(dictList, dataList);
    }

    @Override
    public Map<Long, ChatFriend> queryConfigMap(Long groupId) {
        List<ChatFriend> dataList = chatFriendDao.queryGroup(groupId);
        // 转换
        return dataList.stream().collect(HashMap::new, (x, y) -> {
            x.put(y.getUserId(), y);
        }, HashMap::putAll);
    }

}
