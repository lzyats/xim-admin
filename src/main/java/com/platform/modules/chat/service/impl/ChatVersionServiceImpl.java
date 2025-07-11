package com.platform.modules.chat.service.impl;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatVersionDao;
import com.platform.modules.chat.domain.ChatVersion;
import com.platform.modules.chat.service.ChatVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 版本 服务层实现
 * </p>
 */
@Service("chatVersionService")
public class ChatVersionServiceImpl extends BaseServiceImpl<ChatVersion> implements ChatVersionService {

    @Resource
    private ChatVersionDao chatVersionDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatVersionDao);
    }

    @Override
    public List<ChatVersion> queryList(ChatVersion t) {
        List<ChatVersion> dataList = chatVersionDao.queryList(t);
        return dataList;
    }

    @Override
    public void editVersion(ChatVersion chatVersion) {
        chatVersion.setDevice(null);
        this.updateById(chatVersion);
    }

}
