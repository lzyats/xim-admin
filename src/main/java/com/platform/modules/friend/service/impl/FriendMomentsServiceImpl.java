package com.platform.modules.friend.service.impl;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.platform.common.exception.BaseException;
import com.platform.modules.friend.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.friend.service.FriendMomentsService;
import com.platform.modules.friend.dao.FriendMomentsDao;
import com.platform.modules.friend.domain.FriendMoments;
import org.springframework.transaction.annotation.Transactional;

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


    @Transactional
    @Override
    public void addMoments(FriendVo01 friendVo) {
        // 验证账号
        Long userid = friendVo.getUserId();
        String content = friendVo.getContent();
        String location = friendVo.getLocation();
        Integer visibility = friendVo.getVisibility();
        Date now = DateUtil.date();
        FriendMoments friendMoments = new FriendMoments(userid)
                .setUserId(userid)
                .setContent(content)
                .setLocation(location)
                .setVisibility(visibility)
                .setCreateTime(now)
                .setUpdateTime(now);
        try {
            // 新增朋友圈信息
            this.add(friendMoments);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            throw new BaseException("当前信息已存在，新增失败");
        }
        // 添加媒体信息
        //chatUserInfoService.addInfo(chatUser.getUserId());
        // 新增钱包
        //walletInfoService.addWallet(chatUser.getUserId());
    }

    @Transactional
    @Override
    public void editMoments(FriendVo02 friendVo) {
        Long momentid = friendVo.getMomentId();
        Long userid = friendVo.getUserId();
        String content = friendVo.getContent();
        String location = friendVo.getLocation();
        Integer visibility = friendVo.getVisibility();
        Date CreateTime=friendVo.getCreateTime();
        Date UpdateTime=friendVo.getUpdateTime();
        FriendMoments friendMoments = new FriendMoments(momentid)
                .setMomentId(momentid)
                .setUserId(userid)
                .setContent(content)
                .setLocation(location)
                .setVisibility(visibility)
                .setCreateTime(CreateTime)
                .setUpdateTime(UpdateTime);
        try {
            // 新增朋友圈信息
            this.updateById(friendMoments);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            throw new BaseException("当前信息已存在，新增失败");
        }
    }

}
