package com.platform.modules.approve.service.impl;

import cn.hutool.core.lang.Dict;
import com.github.pagehelper.PageInfo;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.modules.approve.service.ApproveBannedService;
import com.platform.modules.approve.vo.ApproveVo00;
import com.platform.modules.approve.vo.ApproveVo03;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.service.ChatUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 解封审批 业务层处理
 */
@Service("approveBannedService")
public class ApproveBannedServiceImpl implements ApproveBannedService {

    @Resource
    private ChatUserService chatUserService;

    @Override
    public PageInfo queryDataList(ApproveVo00 approveVo) {
        ChatUser chatUser = new ChatUser()
                .setUserNo(approveVo.getUserNo())
                .setNickname(approveVo.getNickname())
                .setPhone(approveVo.getPhone())
                .setBanned(YesOrNoEnum.YES);
        return chatUserService.queryBannedList(chatUser);
    }

    @Override
    public Dict getInfo(Long userId) {
        return chatUserService.queryBannedInfo(userId);
    }

    @Override
    public void auth(ApproveVo03 approveVo) {
        Long userId = approveVo.getUserId();
        YesOrNoEnum status = approveVo.getStatus();
        chatUserService.approveBanned(userId, status);
    }

}
