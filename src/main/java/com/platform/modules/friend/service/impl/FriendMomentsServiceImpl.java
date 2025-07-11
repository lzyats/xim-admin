package com.platform.modules.friend.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.friend.service.FriendMomentsService;
import com.platform.modules.friend.dao.FriendMomentsDao;
import com.platform.modules.friend.domain.FriendMoments;

/**
 * <p>
 * 朋友圈动态表 服务层实现
 * </p>
 */
@Service("friendMomentsService")
public class FriendMomentsServiceImpl extends BaseServiceImpl<FriendMoments> implements FriendMomentsService {

    @Resource
    private FriendMomentsDao friendMomentsDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(friendMomentsDao);
    }

    @Override
    public List<FriendMoments> queryList(FriendMoments t) {
        List<FriendMoments> dataList = friendMomentsDao.queryList(t);
        return dataList;
    }

}
