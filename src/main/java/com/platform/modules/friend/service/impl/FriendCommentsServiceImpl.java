package com.platform.modules.friend.service.impl;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import com.github.pagehelper.PageInfo;
import com.platform.common.exception.BaseException;
import com.platform.modules.friend.domain.FriendComments;
import com.platform.modules.friend.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.friend.service.FriendCommentsService;
import com.platform.modules.friend.dao.FriendCommentsDao;
import com.platform.modules.friend.domain.FriendComments;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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


    /**
     * 获取某条信息的所有评论
     * @param momentId
     * @return
     */
    @Override
    public PageInfo queryListall(Long momentId) {
        // 查询
        List<FriendComments> dataList = this.queryList(new FriendComments(momentId));
        // 转换
        List<Dict> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            Dict dict = Dict.create().parseBean(y)
                    .filter(FriendComments.LABEL_MOMENT_ID
                            , FriendComments.LABEL_USER_ID
                            , FriendComments.LABEL_COMMENT_ID
                            , FriendComments.LABEL_REPLY_TO
                            , FriendComments.LABEL_CONTENT
                            , FriendComments.LABEL_IS_DELETED
                            , FriendComments.LABEL_SOURCE
                            , FriendComments.LABEL_CREATE_TIME);
            x.add(dict);
        }, ArrayList::addAll);
        return getPageInfo(dictList, dataList);
    }
    /**
     * 添加新评论
     */
    @Transactional
    @Override
   public void addComments(FriendVo03 friendVo) {
    // 验证评论字段
    Long momentId = friendVo.getMomentId();
    Long userId = friendVo.getUserId();
    Long replyTo = friendVo.getReplyTo();
    String content = friendVo.getContent();
    Date now = DateUtil.date();
    Integer source=0;
    //判断source的值
    if (replyTo==userId)source=1;
    FriendComments friendComments = new FriendComments(momentId)
            .setMomentId(momentId)
            .setUserId(userId)
            .setReplyTo(replyTo)
            .setContent(content)
            .setSource(source)
            .setCreateTime(now)
            .setIsDeleted(0); // 默认为未删除状态
    
    try {
        // 新增评论信息
        this.add(friendComments);
    } catch (org.springframework.dao.DuplicateKeyException e) {
        throw new BaseException("评论ID已存在，新增失败");
    }
}
}
