package com.platform.modules.operate.service.impl;

import cn.hutool.core.lang.Validator;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.modules.chat.domain.ChatConfig;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.enums.BannedTimeEnum;
import com.platform.modules.chat.enums.BannedTypeEnum;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.service.ChatConfigService;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.chat.vo.ChatVo01;
import com.platform.modules.operate.service.OperateVersionService;
import com.platform.modules.operate.vo.OperateVo02;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 升级版本 服务层实现
 * </p>
 */
@Service("operateVersionService")
public class OperateVersionServiceImpl implements OperateVersionService {

    @Resource
    private ChatConfigService chatConfigService;

    @Resource
    private ChatUserService chatUserService;

    @Override
    public OperateVo02 getInfo() {
        // 查询
        Map<ChatConfigEnum, ChatConfig> configMap = chatConfigService.queryConfig();
        // 转换
        return new OperateVo02()
                .setPhone(configMap.get(ChatConfigEnum.SYS_PHONE).getStr())
                .setAudit(configMap.get(ChatConfigEnum.SYS_AUDIT).getYesOrNo());
    }

    @Transactional
    @Override
    public void update(OperateVo02 operateVo) {
        // 验证账号
        String phone = operateVo.getPhone();
        if (!Validator.isMobile(phone)) {
            throw new BaseException("请输入正确的账号");
        }
        YesOrNoEnum audit = operateVo.getAudit();
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.SYS_PHONE).setValue(phone));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.SYS_AUDIT).setValue(audit));
        // 查询
        ChatUser chatUser = chatUserService.getByPhone(phone);
        if (chatUser == null) {
            return;
        }
        // 封禁账号
        ChatVo01 chatVo = new ChatVo01()
                .setUserId(chatUser.getUserId())
                .setBannedType(BannedTypeEnum.ABUSE)
                .setReason(BannedTypeEnum.ABUSE.getInfo())
                .setBannedTime(YesOrNoEnum.transform(audit) ? BannedTimeEnum.DAY_0 : BannedTimeEnum.FOREVER);
        chatUserService.banned(chatVo);
        // 特殊账号
        chatUserService.resetSpecial(chatUser.getUserId(), audit);
    }

}
