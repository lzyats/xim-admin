package com.platform.modules.work.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.exception.BaseException;
import com.platform.modules.chat.domain.ChatBanned;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.domain.ChatUserInfo;
import com.platform.modules.chat.service.ChatBannedService;
import com.platform.modules.chat.service.ChatUserInfoService;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.wallet.service.WalletInfoService;
import com.platform.modules.work.service.WorkUserService;
import com.platform.modules.work.vo.WorkVo05;
import com.platform.modules.work.vo.WorkVo06;
import com.platform.modules.work.vo.WorkVo07;
import com.platform.modules.work.vo.WorkVo11;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户 服务层实现
 * </p>
 */
@Slf4j
@Service("workUserService")
public class WorkUserServiceImpl implements WorkUserService {

    @Resource
    private ChatUserService chatUserService;

    @Resource
    private ChatUserInfoService chatUserInfoService;

    @Resource
    private WalletInfoService walletInfoService;

    @Resource
    private ChatBannedService chatBannedService;

    @Override
    public List<WorkVo05> getDataList(String param) {
        QueryWrapper<ChatUser> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", "0");
        if (!StringUtils.isEmpty(param)) {
            wrapper.and(qr ->
                    qr.eq("phone", param).or().eq("user_no", param)
            );
        }
        List<ChatUser> dataList = chatUserService.queryList(wrapper);
        // list转Obj
        return dataList.stream().collect(ArrayList::new, (x, y) -> {
            x.add(BeanUtil.toBean(y, WorkVo05.class));
        }, ArrayList::addAll);
    }

    @Override
    public WorkVo06 getInfo(Long userId) {
        // 查询
        ChatUser chatUser = chatUserService.getById(userId);
        if (chatUser == null) {
            throw new BaseException("当前用户不存在");
        }
        return BeanUtil.toBean(chatUser, WorkVo06.class)
                .setAuthLabel(chatUserService.getAuthLabel(chatUser.getAuth()))
                .setBalance(walletInfoService.getBalance(userId));
    }

    @Override
    public WorkVo07 getBanned(Long userId) {
        // 查询
        ChatBanned chatBanned = chatBannedService.getById(userId);
        if (chatBanned == null) {
            return new WorkVo07(userId);
        }
        return new WorkVo07(userId, chatBanned.getBannedReason(), chatBanned.getBannedTime());
    }

    @Override
    public WorkVo11 getAuth(Long userId) {
        // 查询
        ChatUser chatUser = chatUserService.findById(userId);
        // 查询
        ChatUserInfo chatUserInfo = chatUserInfoService.getById(userId);
        ApproveEnum auth = chatUser.getAuth();
        String reason = "-";
        if (ApproveEnum.REJECT.equals(auth)) {
            reason = chatUserInfo.getAuthReason();
        }
        return new WorkVo11(chatUserInfo).setReason(reason);
    }

}
