package com.platform.modules.friend.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.friend.service.FriendMediaService;
import com.platform.modules.friend.dao.FriendMediaDao;
import com.platform.modules.friend.domain.FriendMedia;

/**
 * <p>
 * 朋友圈媒体资源表 服务层实现
 * </p>
 */
@Service("friendMediaService")
public class FriendMediaServiceImpl extends BaseServiceImpl<FriendMedia> implements FriendMediaService {

    @Resource
    private FriendMediaDao friendMediaDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(friendMediaDao);
    }

    @Override
    public List<FriendMedia> queryList(FriendMedia t) {
        List<FriendMedia> dataList = friendMediaDao.queryList(t);
        return dataList;
    }

}
