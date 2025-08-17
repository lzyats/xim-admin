package com.platform.modules.friend.service.impl;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.common.exception.BaseException;
import com.platform.common.shiro.ShiroUtils;
import com.platform.modules.chat.domain.ChatUserInfo;
import com.platform.modules.friend.vo.*;
import com.platform.modules.push.dto.PushComments;
import com.platform.modules.push.dto.PushFrom;
import com.platform.modules.push.dto.PushMedias;
import com.platform.modules.push.dto.PushMoment;
import com.platform.modules.push.enums.PushMomentEnum;
import com.platform.modules.push.service.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Resource
    private PushService pushService;

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
        Long momentId=IdWorker.getId();;
        String content = friendVo.getContent();
        String location = friendVo.getLocation();
        Integer visibility = friendVo.getVisibility();
        Date now = DateUtil.date();
        FriendMoments friendMoments = new FriendMoments(momentId)
                .setUserId(userid)
                .setContent(content)
                .setLocation(location)
                .setVisibility(visibility)
                .setCreateTime(now)
                .setVisuser(friendVo.getVisuser())
                .setUpdateTime(now);
        try {
            // 新增朋友圈信息
            this.add(friendMoments);
            // 发送广播
            this.getmoments(friendMoments.getMomentId());
        } catch (org.springframework.dao.DuplicateKeyException e) {
            throw new BaseException("当前信息已存在，新增失败");
        }
        // 添加媒体信息
        //chatUserInfoService.addInfo(chatUser.getUserId());
        // 新增钱包
        //walletInfoService.addWallet(chatUser.getUserId());
    }

    /**
     * 编辑朋友圈信息
     * @param friendVo
     */
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
        // 发送广播
        this.getmoments(momentid);
    }

    /**
     * 条件查询朋友圈列表（支持分页）
     * @param friendMoments 包含查询条件的对象（moment_id、is_deleted、user_id）
     * @return 分页查询结果
     */
    @Transactional(readOnly = true) // 查询方法建议设置readOnly=true优化性能
    @Override
    public PageInfo<FriendMoments> queryLists(FriendMoments friendMoments) {
        //log.info("查询条件：{}", friendMoments);
        List<FriendMoments> list = this.queryList(friendMoments);
        return new PageInfo(list);
    }

    /**
     * 查询朋友圈信息并向所有人发送
     * @param momentId
     */
    @Transactional
    public void getmoments(Long momentId){
        Long current = ShiroUtils.getUserId();
        MomentVo03 momentVo03 = friendMomentsDao.getMomentsByMomentId(momentId);
        int isDeleted=momentVo03.getIsdelete();
        //log.info("最后的查询结果：{}",momentVo03);
        // 查询出符合条件的接收人ID
        List<Long> userlist = new ArrayList<Long>();
        if(momentVo03.getVisibility() ==3){
            List<String> visuser=momentVo03.getVisuser();
            //log.info(visuser.toString());
            // 处理空值或空字符串
            if (visuser == null || visuser.isEmpty()) {
                //log.info("visuser is null");
            }else{
                // 转换为Long类型并收集（处理可能的格式异常）
                for (String userIdStr : visuser) {
                    //log.info(userIdStr);
                    // 去除前后空格（避免字符串中包含空格导致转换失败）
                    String trimmed = userIdStr.trim();
                    if (!trimmed.isEmpty()) {
                        userlist.add(Long.parseLong(trimmed));
                    }
                }
            }

        }else{
            userlist=friendMomentsDao.getQualifiedUserIdsByMomentId(momentId);
        }
        //接收人列表中应该加上自己
        if(!userlist.contains(momentVo03.getUserId()))
            userlist.add(momentVo03.getUserId());
        if(!userlist.contains(current))
            userlist.add(current);
        //查询其他信息
        List<PushMedias> pushMediasList=new ArrayList<>();
        List<MediasVo01> mediasVo01s=friendMomentsDao.getMediasByMomentId(momentId);
        for (int i = 0; i < mediasVo01s.size(); i++) {
            PushMedias pushMedias=new PushMedias();
            pushMedias.setType(mediasVo01s.get(i).getType());
            pushMedias.setUrl(mediasVo01s.get(i).getUrl());
            pushMedias.setThumbnail(mediasVo01s.get(i).getThumbnail());
            pushMediasList.add(pushMedias);
        }
        List<PushComments> pushCommentsList=new ArrayList<>();
        List<CommentsVo01> commentsVo01s= friendMomentsDao.getCommentsByMomentId(momentId);
        for (int i = 0; i < commentsVo01s.size(); i++) {
            PushComments pushComments=new PushComments();
            pushComments.setContent(commentsVo01s.get(i).getContent());
            pushComments.setFromUser(commentsVo01s.get(i).getFromUser());
            pushComments.setToUser(commentsVo01s.get(i).getToUser());
            boolean Source=true;
            if(Objects.equals(commentsVo01s.get(i).getFromUser(), momentVo03.getNickname()) && !Objects.equals(commentsVo01s.get(i).getFromUser(), commentsVo01s.get(i).getToUser())){
                Source=false;
            }
            pushComments.setSource(Source);
            pushCommentsList.add(pushComments);
        }
        List<String> likes= friendMomentsDao.getLikesNicknamesByMomentId(momentId);
        //组装消息
        Long MsgId= IdWorker.getId();
        Long SyncId= IdWorker.getId();
        PushMoment pushMoment=new PushMoment()
                .setMsgId(MsgId.toString())
                .setMomentId(momentId)
                .setUserId(momentVo03.getUserId())
                .setPortrait(momentVo03.getPortrait())
                .setNickname(momentVo03.getNickname())
                .setContent(momentVo03.getContent())
                .setLocation(momentVo03.getLocation())
                .setType(PushMomentEnum.MOMENT.getCode())
                .setCreateTime(momentVo03.getCreateTime())
                .setIsDeleted(momentVo03.getIsdelete())
                .setImages(pushMediasList)
                .setComments(pushCommentsList)
                .setLikes(likes)
                ;
        if(isDeleted!=0){
            pushMoment.setIsDeleted(1);
        }
        log.info("接收人列表："+userlist);
        PushFrom pushFrom = new PushFrom()
                .setMsgId(MsgId)
                .setSyncId(MsgId)
                .setUserId(momentVo03.getUserId())
                .setNickname(momentVo03.getNickname())
                .setPortrait(momentVo03.getPortrait())
                .setSign(ShiroUtils.getSign());
        //pushService.pushMoment(pushFrom,pushMoment,userlist);
        pushMoment.setSyncId(MsgId.toString());
        // 同步消息
        //log.info("发送内容：{}",pushMoment);
        pushService.pushMomentSync(pushFrom, pushMoment,userlist,PushMomentEnum.MOMENT);
        //发送朋友圈计数器
        //wpushService.pushBadger(userlist, PushBadgerEnum.MOMENT,true);
    }

    /**
     * 查询朋友圈信息并向所有人发送
     * @param momentId
     */
    @Transactional
    public void getmoments(Long momentId,int delete){
        Long current = ShiroUtils.getUserId();
        MomentVo03 momentVo03 = friendMomentsDao.getMomentsByMomentId(momentId);
        int isDeleted=momentVo03.getIsdelete();
        //log.info("最后的查询结果：{}",momentVo03);
        // 查询出符合条件的接收人ID
        List<Long> userlist = new ArrayList<Long>();
        if(momentVo03.getVisibility() ==3){
            List<String> visuser=momentVo03.getVisuser();
            //log.info(visuser.toString());
            // 处理空值或空字符串
            if (visuser == null || visuser.isEmpty()) {
                //log.info("visuser is null");
            }else{
                // 转换为Long类型并收集（处理可能的格式异常）
                for (String userIdStr : visuser) {
                    //log.info(userIdStr);
                    // 去除前后空格（避免字符串中包含空格导致转换失败）
                    String trimmed = userIdStr.trim();
                    if (!trimmed.isEmpty()) {
                        userlist.add(Long.parseLong(trimmed));
                    }
                }
            }

        }else{
            userlist=friendMomentsDao.getQualifiedUserIdsByMomentId(momentId);
        }
        //接收人列表中应该加上自己
        if(!userlist.contains(momentVo03.getUserId()))
            userlist.add(momentVo03.getUserId());
        if(!userlist.contains(current))
            userlist.add(current);
        //查询其他信息
        List<PushMedias> pushMediasList=new ArrayList<>();
        List<MediasVo01> mediasVo01s=friendMomentsDao.getMediasByMomentId(momentId);
        for (int i = 0; i < mediasVo01s.size(); i++) {
            PushMedias pushMedias=new PushMedias();
            pushMedias.setType(mediasVo01s.get(i).getType());
            pushMedias.setUrl(mediasVo01s.get(i).getUrl());
            pushMedias.setThumbnail(mediasVo01s.get(i).getThumbnail());
            pushMediasList.add(pushMedias);
        }
        List<PushComments> pushCommentsList=new ArrayList<>();
        List<CommentsVo01> commentsVo01s= friendMomentsDao.getCommentsByMomentId(momentId);
        for (int i = 0; i < commentsVo01s.size(); i++) {
            PushComments pushComments=new PushComments();
            pushComments.setContent(commentsVo01s.get(i).getContent());
            pushComments.setFromUser(commentsVo01s.get(i).getFromUser());
            pushComments.setToUser(commentsVo01s.get(i).getToUser());
            boolean Source=true;
            if(Objects.equals(commentsVo01s.get(i).getFromUser(), momentVo03.getNickname()) && !Objects.equals(commentsVo01s.get(i).getFromUser(), commentsVo01s.get(i).getToUser())){
                Source=false;
            }
            pushComments.setSource(Source);
            pushCommentsList.add(pushComments);
        }
        List<String> likes= friendMomentsDao.getLikesNicknamesByMomentId(momentId);
        //组装消息
        Long MsgId= IdWorker.getId();
        Long SyncId= IdWorker.getId();
        PushMoment pushMoment=new PushMoment()
                .setMsgId(MsgId.toString())
                .setMomentId(momentId)
                .setUserId(momentVo03.getUserId())
                .setPortrait(momentVo03.getPortrait())
                .setNickname(momentVo03.getNickname())
                .setContent(momentVo03.getContent())
                .setLocation(momentVo03.getLocation())
                .setType(PushMomentEnum.MOMENT.getCode())
                .setCreateTime(momentVo03.getCreateTime())
                .setIsDeleted(momentVo03.getIsdelete())
                .setImages(pushMediasList)
                .setComments(pushCommentsList)
                .setLikes(likes)
                ;
        if(delete!=0){
            pushMoment.setIsDeleted(1);
        }
        log.info("接收人列表："+userlist);
        PushFrom pushFrom = new PushFrom()
                .setMsgId(MsgId)
                .setSyncId(MsgId)
                .setUserId(momentVo03.getUserId())
                .setNickname(momentVo03.getNickname())
                .setPortrait(momentVo03.getPortrait())
                .setSign(ShiroUtils.getSign());
        pushMoment.setSyncId(MsgId.toString());
        // 同步消息
        pushService.pushMomentSync(pushFrom, pushMoment,userlist,PushMomentEnum.MOMENT);
    }
}
