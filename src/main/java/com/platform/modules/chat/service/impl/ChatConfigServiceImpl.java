package com.platform.modules.chat.service.impl;

import com.platform.common.exception.BaseException;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatConfigDao;
import com.platform.modules.chat.domain.ChatConfig;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.service.ChatConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设置表 服务层实现
 * </p>
 */
@Service("chatConfigService")
public class ChatConfigServiceImpl extends BaseServiceImpl<ChatConfig> implements ChatConfigService {

    @Resource
    private ChatConfigDao chatConfigDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatConfigDao);
    }

    @Override
    public Map<ChatConfigEnum, ChatConfig> queryConfig() {
        // 查询
        List<ChatConfig> dataList = chatConfigDao.queryList(new ChatConfig());
        // list转map
        return dataList.stream().collect(HashMap::new, (x, y) -> {
            x.put(y.getConfigKey(), y);
        }, HashMap::putAll);
    }

    @Override
    public List<ChatConfig> queryList(ChatConfig t) {
        List<ChatConfig> dataList = chatConfigDao.queryList(t);
        return dataList;
    }

    @Override
    public ChatConfig queryConfig(ChatConfigEnum configType) {
        ChatConfig chatConfig = chatConfigDao.getConfig(configType);
        if (chatConfig == null) {
            throw new BaseException("设置数据不存在，请检查配置");
        }
        return chatConfig;
    }

}
