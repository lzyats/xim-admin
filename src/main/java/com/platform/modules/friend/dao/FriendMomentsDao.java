package com.platform.modules.friend.dao;


import com.platform.modules.friend.domain.FriendMoments;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.Constants; // 关键：Constants 类的正确包路径
import org.apache.ibatis.annotations.Param; // 关键：MyBatis 的 @Param 注解



/**
 * <p>
 * 朋友圈动态表 数据库访问层
 * </p>
 */
@Repository
public interface FriendMomentsDao extends BaseDao<FriendMoments> {

    /**
     * 查询列表
     */
    List<FriendMoments> queryList(FriendMoments friendMoments);

    /**
     * 查询列表
     */
    List<FriendMoments> queryDataList(FriendMoments friendMoments);


}
