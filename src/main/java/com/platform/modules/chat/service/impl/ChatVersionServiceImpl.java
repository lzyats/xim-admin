package com.platform.modules.chat.service.impl;

import com.platform.common.constant.AppConstants;
import com.platform.common.redis.RedisUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatVersionDao;
import com.platform.modules.chat.domain.ChatVersion;
import com.platform.modules.chat.service.ChatVersionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private RedisUtils redisUtils;

    private static final Logger logger = LoggerFactory.getLogger(ChatNoticeServiceImpl.class);

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
        String redisKey = AppConstants.REDIS_COMMON_CONFIG + "version:upgrade:android";
        redisUtils.delete(redisKey);
        logger.info("清除缓存：key {}",redisKey);
        redisKey = AppConstants.REDIS_COMMON_CONFIG + "version:upgrade:ios";
        redisUtils.delete(redisKey);
        logger.info("清除缓存：key {}",redisKey);
        chatVersion.setDevice(null);
        this.updateById(chatVersion);
    }

}
