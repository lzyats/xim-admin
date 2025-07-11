package com.platform.modules.work.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.common.exception.BaseException;
import com.platform.modules.chat.domain.ChatGroup;
import com.platform.modules.chat.domain.ChatGroupMember;
import com.platform.modules.chat.service.ChatGroupMemberService;
import com.platform.modules.chat.service.ChatGroupService;
import com.platform.modules.work.service.WorkGroupService;
import com.platform.modules.work.vo.WorkVo09;
import com.platform.modules.work.vo.WorkVo10;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 群组 服务层实现
 * </p>
 */
@Slf4j
@Service("workGroupService")
public class WorkGroupServiceImpl implements WorkGroupService {

    @Resource
    private ChatGroupService chatGroupService;

    @Resource
    private ChatGroupMemberService chatGroupMemberService;

    @Override
    public List<WorkVo09> getDataList(String param) {
        QueryWrapper<ChatGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", "0");
        if (!StringUtils.isEmpty(param)) {
            wrapper.eq("group_no", param);
        }
        List<ChatGroup> dataList = chatGroupService.queryList(wrapper);
        // list转Obj
        return dataList.stream().collect(ArrayList::new, (x, y) -> {
            x.add(BeanUtil.toBean(y, WorkVo09.class));
        }, ArrayList::addAll);
    }

    @Override
    public WorkVo10 getInfo(Long groupId) {
        // 查询
        ChatGroup chatGroup = chatGroupService.getById(groupId);
        if (chatGroup == null) {
            throw new BaseException("当前群聊不存在");
        }
        QueryWrapper<ChatGroupMember> wrapper = new QueryWrapper<>();
        wrapper.eq(ChatGroupMember.COLUMN_GROUP_ID, groupId);
        wrapper.eq("deleted", "0");
        Long memberCount = chatGroupMemberService.queryCount(wrapper);
        chatGroup.setMemberCount(memberCount);
        return BeanUtil.toBean(chatGroup, WorkVo10.class)
                .setLevelCount(chatGroup.getGroupLevelCount().longValue())
                .setMemberCount(memberCount);
    }

}
