package com.platform.modules.approve.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdcardUtil;
import com.github.pagehelper.PageInfo;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.modules.approve.service.ApproveAuthService;
import com.platform.modules.approve.vo.ApproveVo00;
import com.platform.modules.approve.vo.ApproveVo02;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.service.ChatUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 认证审批 业务层处理
 */
@Service("approveAuthService")
public class ApproveAuthServiceImpl implements ApproveAuthService {

    @Resource
    private ChatUserService chatUserService;

    @Override
    public PageInfo queryDataList(ApproveVo00 approveVo) {
        ChatUser chatUser = new ChatUser()
                .setUserNo(approveVo.getUserNo())
                .setNickname(approveVo.getNickname())
                .setPhone(approveVo.getPhone())
                .setAuth(ApproveEnum.APPLY);
        return chatUserService.queryAuthList(chatUser);
    }

    @Override
    public Dict getInfo(Long userId) {
        return chatUserService.getAuth(userId);
    }

    @Override
    public void auth(ApproveVo02 approveVo) {
        Long userId = approveVo.getUserId();
        ApproveEnum auth = ApproveEnum.REJECT;
        String idCard = approveVo.getIdCard();
        String name = approveVo.getName();
        String reason = approveVo.getReason();
        if (YesOrNoEnum.YES.equals(approveVo.getStatus())) {
            auth = ApproveEnum.PASS;
            reason = "-";
        }
        if (!IdcardUtil.isValidCard(idCard)) {
            throw new BaseException("请输入有效的身份证号码");
        }
        chatUserService.approveAuth(userId, name, idCard, auth, reason);
    }

}
