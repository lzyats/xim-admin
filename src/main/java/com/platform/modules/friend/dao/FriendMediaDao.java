package com.platform.modules.friend.dao;

import com.platform.modules.friend.domain.FriendMedia;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 朋友圈媒体资源表 数据库访问层
 * </p>
 */
@Repository
public interface FriendMediaDao extends BaseDao<FriendMedia> {

    /**
     * 查询列表
     */
    List<FriendMedia> queryList(FriendMedia friendMedia);

}
