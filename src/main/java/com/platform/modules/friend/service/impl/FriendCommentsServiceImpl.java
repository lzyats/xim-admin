package com.platform.modules.friend.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.friend.service.FriendCommentsService;
import com.platform.modules.friend.dao.FriendCommentsDao;
import com.platform.modules.friend.domain.FriendComments;

/**
 * <p>
 * 朋友圈评论表 服务层实现
 * </p>
 */
@Service("friendCommentsService")
public class FriendCommentsServiceImpl extends BaseServiceImpl<FriendComments> implements FriendCommentsService {

    @Resource
    private FriendCommentsDao friendCommentsDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(friendCommentsDao);
    }

    @Override
    public List<FriendComments> queryList(FriendComments t) {
        List<FriendComments> dataList = friendCommentsDao.queryList(t);
        return dataList;
    }

}
