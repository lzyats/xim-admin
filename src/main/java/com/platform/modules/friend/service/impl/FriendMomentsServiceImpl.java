package com.platform.modules.friend.service.impl;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.common.exception.BaseException;
import com.platform.modules.chat.domain.ChatUserInfo;
import com.platform.modules.friend.vo.*;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.util.ObjectUtils;

/**
 * <p>
 * 朋友圈动态表 服务层实现
 * </p>
 */
@Slf4j
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

    /**
     * 条件查询朋友圈列表（支持分页）
     * @param friendMoments 包含查询条件的对象（moment_id、is_deleted、user_id）
     * @return 分页查询结果
     */
    @Transactional(readOnly = true) // 查询方法建议设置readOnly=true优化性能
    @Override
    public PageInfo<FriendMoments> queryLists(FriendMoments friendMoments) {
        log.info("查询条件：{}", friendMoments);
        // 1. 初始化分页（假设前端传递pageNum和pageSize参数，这里从friendMoments中获取）

        // 2. 构建动态查询条件
        QueryWrapper<FriendMoments> wrapper = new QueryWrapper<>();

        // 当moment_id不为空时，添加精确匹配条件
        if (!ObjectUtils.isEmpty(friendMoments.getMomentId())) {
            wrapper.eq("moment_id", friendMoments.getMomentId());
        }

        // 当is_deleted不为空时，添加精确匹配条件（逻辑删除标识）
        if (friendMoments.getIsDeleted() != null) {
            wrapper.eq("is_deleted", friendMoments.getIsDeleted());
        }

        // 当user_id不为空时，添加精确匹配条件（所属用户）
        if (!ObjectUtils.isEmpty(friendMoments.getUserId())) {
            wrapper.eq("user_id", friendMoments.getUserId());
        }

        // 3. 可添加默认排序（如按创建时间倒序）
        wrapper.orderByDesc("create_time");

        // 4. 执行查询（假设使用MyBatis-Plus的baseMapper）
        List<FriendMoments> list = this.queryList(friendMoments);

        // 5. 包装成分页对象返回
        return new PageInfo<>(list);
    }

}
