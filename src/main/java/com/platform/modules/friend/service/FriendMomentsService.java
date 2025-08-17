package com.platform.modules.friend.service;

import cn.hutool.core.lang.Dict;
import com.github.pagehelper.PageInfo;
import com.platform.modules.friend.domain.FriendMoments;
import com.platform.common.web.service.BaseService;
import com.platform.modules.friend.vo.FriendVo01;
import com.platform.modules.friend.vo.FriendVo02;

import java.util.List;

/**
 * <p>
 * 朋友圈动态表 服务层
 * </p>
 */
public interface FriendMomentsService extends BaseService<FriendMoments> {

     /**
     * 新增
     */
    void addMoments(FriendVo01 friendVo);

    /**
     * 编辑朋友圈信息
     * @param friendVo
     */
    void editMoments(FriendVo02 friendVo);

    /**
     * 列表
     */
    PageInfo queryLists(FriendMoments friendMoments);

}
