package com.platform.modules.chat.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.redis.RedisUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatGroupDao;
import com.platform.modules.chat.domain.ChatGroup;
import com.platform.modules.chat.domain.ChatGroupMember;
import com.platform.modules.chat.enums.GroupLogEnum;
import com.platform.modules.chat.service.ChatGroupLogService;
import com.platform.modules.chat.service.ChatGroupMemberService;
import com.platform.modules.chat.service.ChatGroupService;
import com.platform.modules.chat.vo.ChatVo11;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 群组 服务层实现
 * </p>
 */
@Service("chatGroupService")
public class ChatGroupServiceImpl extends BaseServiceImpl<ChatGroup> implements ChatGroupService {

    @Resource
    private ChatGroupDao chatGroupDao;

    @Resource
    private ChatGroupMemberService chatGroupMemberService;

    @Resource
    private ChatGroupLogService chatGroupLogService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatGroupDao);
    }

    @Override
    public List<ChatGroup> queryList(ChatGroup t) {
        List<ChatGroup> dataList = chatGroupDao.queryList(t);
        return dataList;
    }

    @Override
    public PageInfo groupList(Long userId) {
        List<ChatGroup> dataList = chatGroupDao.groupList(userId);
        List<Dict> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            Dict dict = Dict.create().parseBean(y)
                    .filter(ChatGroup.LABEL_GROUP_ID
                            , ChatGroup.LABEL_GROUP_NAME
                            , ChatGroup.LABEL_GROUP_NO
                            , ChatGroup.LABEL_PORTRAIT
                            , ChatGroup.LABEL_CREATE_TIME);
            x.add(dict);
        }, ArrayList::addAll);
        return getPageInfo(dictList, dataList);
    }

    @Override
    public PageInfo queryDataList(ChatGroup chatGroup) {
        List<ChatGroup> dataList = chatGroupDao.queryDataList(chatGroup);
        List<Dict> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            x.add(format(y));
        }, ArrayList::addAll);
        return getPageInfo(dictList, dataList);
    }

    /**
     * 格式化详情
     */
    private Dict format(ChatGroup chatGroup) {
        YesOrNoEnum status = YesOrNoEnum.transform(chatGroup.getDeleted() != null);
        return Dict.create().parseBean(chatGroup)
                .filter(ChatGroup.LABEL_GROUP_ID
                        , ChatGroup.LABEL_GROUP_NAME
                        , ChatGroup.LABEL_GROUP_NO
                        , ChatGroup.LABEL_PORTRAIT
                        , ChatGroup.LABEL_MEMBER_COUNT
                        , ChatGroup.LABEL_BANNED
                        , ChatGroup.LABEL_CREATE_TIME
                )
                .set(ChatGroup.LABEL_LEVEL_COUNT, chatGroup.getGroupLevelCount())
                .set(ChatGroup.LABEL_BANNED_LABEL, YesOrNoEnum.transform(chatGroup.getBanned()) ? "封禁" : "正常")
                .set(ChatGroup.LABEL_STATUS, status)
                .set(ChatGroup.LABEL_STATUS_LABEL, YesOrNoEnum.transform(status) ? "正常" : "注销");
    }

    @Override
    public Dict getInfo(Long groupId) {
        ChatGroup chatGroup = this.findById(groupId);
        // 用户数量
        QueryWrapper<ChatGroupMember> wrapper = new QueryWrapper<>();
        wrapper.eq(ChatGroupMember.COLUMN_GROUP_ID, groupId);
        Long memberCount = chatGroupMemberService.queryCount(wrapper);
        chatGroup.setMemberCount(memberCount);
        // 格式化
        return format(chatGroup)
                .set(ChatGroup.LABEL_NOTICE, chatGroup.getNotice())
                .set(ChatGroup.LABEL_NOTICE_TOP, chatGroup.getNoticeTop())
                .set(ChatGroup.LABEL_CONFIG_MEMBER, chatGroup.getConfigMember())
                .set(ChatGroup.LABEL_CONFIG_INVITE, chatGroup.getConfigInvite())
                .set(ChatGroup.LABEL_CONFIG_SPEAK, chatGroup.getConfigSpeak())
                .set(ChatGroup.LABEL_CONFIG_TITLE, chatGroup.getConfigTitle())
                .set(ChatGroup.LABEL_CONFIG_AUDIT, chatGroup.getConfigAudit())
                .set(ChatGroup.LABEL_CONFIG_MEDIA, chatGroup.getConfigMedia())
                .set(ChatGroup.LABEL_CONFIG_ASSIGN, chatGroup.getConfigAssign())
                .set(ChatGroup.LABEL_CONFIG_NICKNAME, chatGroup.getConfigNickname())
                .set(ChatGroup.LABEL_CONFIG_PACKET, chatGroup.getConfigPacket())
                .set(ChatGroup.LABEL_CONFIG_AMOUNT, chatGroup.getConfigAmount())
                .set(ChatGroup.LABEL_CONFIG_SCAN, chatGroup.getConfigScan())
                .set(ChatGroup.LABEL_CONFIG_RECEIVE, chatGroup.getConfigReceive())
                .set(ChatGroup.LABEL_PRIVACY_NO, chatGroup.getPrivacyNo())
                .set(ChatGroup.LABEL_PRIVACY_SCAN, chatGroup.getPrivacyScan())
                .set(ChatGroup.LABEL_PRIVACY_NAME, chatGroup.getPrivacyName());
    }

    @Override
    public PageInfo memberList(Long groupId) {
        // 查询
        List<ChatGroupMember> memberList = chatGroupMemberService.queryList(new ChatGroupMember(groupId));
        // 处理
        List<Dict> dataList = memberList.stream().collect(ArrayList::new, (x, y) -> {
            Dict dict = Dict.create().parseBean(y)
                    .filter(ChatGroupMember.LABEL_USER_ID
                            , ChatGroupMember.LABEL_USER_NO
                            , ChatGroupMember.LABEL_NICKNAME
                            , ChatGroupMember.LABEL_PORTRAIT
                            , ChatGroupMember.LABEL_MEMBER_TYPE
                            , ChatGroupMember.LABEL_TOP
                            , ChatGroupMember.LABEL_DISTURB
                            , ChatGroupMember.LABEL_CREATE_TIME
                    )
                    .set(ChatGroupMember.LABEL_MEMBER_TYPE_LABEL, y.getMemberType().getInfo())
                    .set(ChatGroupMember.LABEL_SOURCE, y.getMemberSource().getInfo())
                    .set(ChatGroupMember.LABEL_TOP_LABEL, YesOrNoEnum.transform(y.getTop()) ? "置顶" : "未置顶")
                    .set(ChatGroupMember.LABEL_DISTURB_LABEL, YesOrNoEnum.transform(y.getDisturb()) ? "静默" : "未静默");
            x.add(dict);
        }, ArrayList::addAll);
        return getPageInfo(dataList, memberList);
    }

    @Transactional
    @Override
    public void banned(Long groupId, YesOrNoEnum banned) {
        // 更新
        ChatGroup chatGroup = new ChatGroup(groupId).setBanned(banned);
        this.updateById(chatGroup);
        // 日志
        GroupLogEnum logType = GroupLogEnum.BANNED_REMOVE;
        if (YesOrNoEnum.YES.equals(banned)) {
            logType = GroupLogEnum.BANNED;
        }
        chatGroupLogService.addLog(groupId, logType);
        // 移除缓存
        this.clearCache(groupId);
    }

    @Override
    public void levelCount(Long groupId, Integer levelCount) {
        // 更新
        ChatGroup chatGroup = new ChatGroup()
                .setGroupId(groupId)
                .setGroupLevelCount(levelCount);
        this.updateById(chatGroup);
        // 移除缓存
        this.clearCache(groupId);
    }

    @Transactional
    @Override
    public void editGroup(ChatVo11 chatVo) {
        Long groupId = chatVo.getGroupId();
        // 更新
        ChatGroup chatGroup = new ChatGroup(groupId)
                .setPortrait(chatVo.getPortrait())
                .setGroupName(chatVo.getGroupName())
                .setConfigMember(chatVo.getConfigMember())
                .setConfigInvite(chatVo.getConfigInvite())
                .setConfigSpeak(chatVo.getConfigSpeak())
                .setConfigTitle(chatVo.getConfigTitle())
                .setConfigAudit(chatVo.getConfigAudit())
                .setConfigMedia(chatVo.getConfigMedia())
                .setConfigAssign(chatVo.getConfigAssign())
                .setConfigNickname(chatVo.getConfigNickname())
                .setConfigPacket(chatVo.getConfigPacket())
                .setConfigAmount(chatVo.getConfigAmount())
                .setConfigScan(chatVo.getConfigScan())
                .setConfigReceive(chatVo.getConfigReceive())
                .setPrivacyNo(chatVo.getPrivacyNo())
                .setPrivacyScan(chatVo.getPrivacyScan())
                .setPrivacyName(chatVo.getPrivacyName())
                .setNoticeTop(chatVo.getNoticeTop())
                .setNotice(chatVo.getNotice());
        this.updateById(chatGroup);
        // 修改公告
        if (StringUtils.isEmpty(chatVo.getNotice())) {
            this.update(Wrappers.<ChatGroup>lambdaUpdate()
                    .set(ChatGroup::getNotice, null)
                    .eq(ChatGroup::getGroupId, groupId));
        }
        // 移除缓存
        this.clearCache(groupId);
    }

    /**
     * 移除缓存
     */
    private void clearCache(Long groupId) {
        redisUtils.delete(StrUtil.format(AppConstants.REDIS_CHAT_GROUP, groupId));
    }

}
