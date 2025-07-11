package com.platform.modules.chat.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.platform.common.constant.HeadConstant;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.redis.RedisUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatUserTokenDao;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.domain.ChatUserToken;
import com.platform.modules.chat.service.ChatUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户token 服务层实现
 * </p>
 */
@Service("chatUserTokenService")
public class ChatUserTokenServiceImpl extends BaseServiceImpl<ChatUserToken> implements ChatUserTokenService {

    @Resource
    private ChatUserTokenDao chatUserTokenDao;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatUserTokenDao);
    }

    @Override
    public List<ChatUserToken> queryList(ChatUserToken t) {
        List<ChatUserToken> dataList = chatUserTokenDao.queryList(t);
        return dataList;
    }

    @Override
    public void deleted(Long userId) {
        // 查询
        List<ChatUserToken> dataList = this.queryList(new ChatUserToken(userId));
        // 集合判空
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        // 删除登录
        this.delete(Wrappers.<ChatUserToken>lambdaUpdate()
                .eq(ChatUserToken::getUserId, userId));
        // 清理token
        dataList.forEach(data -> {
            String redisKey = makeKey(data.getToken());
            redisUtils.delete(redisKey);
        });
    }

    @Override
    public void refreshBanned(Long userId, YesOrNoEnum banned) {
        // 查询
        List<ChatUserToken> dataList = this.queryList(new ChatUserToken(userId));
        // 组装
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put(ChatUser.LABEL_BANNED, banned.getCode());
        // 循环
        dataList.forEach(data -> {
            String redisKey = makeKey(data.getToken());
            if (redisUtils.hasKey(redisKey)) {
                redisUtils.hPutAll(redisKey, objectMap, 7, TimeUnit.DAYS);
            }
        });
    }

    private String makeKey(String token) {
        return HeadConstant.TOKEN_REDIS_APP + token;
    }

}
