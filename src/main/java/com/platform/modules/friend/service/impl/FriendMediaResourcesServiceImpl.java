package com.platform.modules.friend.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.friend.service.FriendMediaResourcesService;
import com.platform.modules.friend.dao.FriendMediaResourcesDao;
import com.platform.modules.friend.domain.FriendMediaResources;

/**
 * <p>
 * 朋友圈媒体资源表 服务层实现
 * </p>
 */
@Service("friendMediaResourcesService")
public class FriendMediaResourcesServiceImpl extends BaseServiceImpl<FriendMediaResources> implements FriendMediaResourcesService {

    @Resource
    private FriendMediaResourcesDao friendMediaResourcesDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(friendMediaResourcesDao);
    }

    @Override
    public List<FriendMediaResources> queryList(FriendMediaResources t) {
        List<FriendMediaResources> dataList = friendMediaResourcesDao.queryList(t);
        return dataList;
    }

}
