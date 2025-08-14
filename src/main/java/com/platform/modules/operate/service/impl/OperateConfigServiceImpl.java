package com.platform.modules.operate.service.impl;

import com.platform.common.constant.AppConstants;
import com.platform.common.redis.RedisUtils;
import com.platform.modules.chat.domain.ChatConfig;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.service.ChatConfigService;
import com.platform.modules.chat.service.impl.ChatNoticeServiceImpl;
import com.platform.modules.operate.service.OperateConfigService;
import com.platform.modules.operate.vo.OperateVo06;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 提现管理 服务层实现
 * </p>
 */
@Service("operateConfigService")
public class OperateConfigServiceImpl implements OperateConfigService {

    @Resource
    private ChatConfigService chatConfigService;

    @Autowired
    private RedisUtils redisUtils;

    private static final Logger logger = LoggerFactory.getLogger(ChatNoticeServiceImpl.class);

    @Override
    public OperateVo06 getInfo() {
        // 查询
        Map<ChatConfigEnum, ChatConfig> configMap = chatConfigService.queryConfig();
        // 转换
        return new OperateVo06()
                .setApplyFriend(configMap.get(ChatConfigEnum.APPLY_FRIEND).getInt())
                .setApplyGroup(configMap.get(ChatConfigEnum.APPLY_GROUP).getInt())
                .setDeleted(configMap.get(ChatConfigEnum.USER_DELETED).getInt())
                .setRegister(configMap.get(ChatConfigEnum.USER_REGISTER).getYesOrNo())
                .setHoldCard(configMap.get(ChatConfigEnum.USER_HOLD).getYesOrNo())
                .setSms(configMap.get(ChatConfigEnum.USER_SMS).getYesOrNo())
                .setProject(configMap.get(ChatConfigEnum.SYS_PROJECT).getStr())
                .setBeian(configMap.get(ChatConfigEnum.SYS_BEIAN).getStr())
                .setNickname(configMap.get(ChatConfigEnum.SYS_NICKNAME).getStr())
                .setScreenshot(configMap.get(ChatConfigEnum.SYS_SCREENSHOT).getYesOrNo())
                .setWatermark(configMap.get(ChatConfigEnum.SYS_WATERMARK).getStr())
                .setCaptcha(configMap.get(ChatConfigEnum.SYS_CAPTCHA).getStr())
                .setPacket(configMap.get(ChatConfigEnum.SYS_PACKET).getBigDecimal())
                .setShare(configMap.get(ChatConfigEnum.SYS_SHARE).getStr())
                .setHook(configMap.get(ChatConfigEnum.SYS_HOOK).getStr())
                .setRecall(configMap.get(ChatConfigEnum.SYS_RECALL).getInt())
                .setSigntoal(configMap.get(ChatConfigEnum.SYS_SIGNTOAL).getYesOrNo())
                .setSign(configMap.get(ChatConfigEnum.SYS_SIGN).getBigDecimal())
                .setInvo(configMap.get(ChatConfigEnum.SYS_INVO).getBigDecimal())
                .setInvoadus(configMap.get(ChatConfigEnum.SYS_INVOADUS).getYesOrNo())
                .setSendmoment(configMap.get(ChatConfigEnum.SYS_SENDMOMENT).getYesOrNo())
                ;
    }

    @Transactional
    @Override
    public void update(OperateVo06 operateVo) {

        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.APPLY_FRIEND).setValue(operateVo.getApplyFriend()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.APPLY_GROUP).setValue(operateVo.getApplyGroup()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.USER_DELETED).setValue(operateVo.getDeleted()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.USER_REGISTER).setValue(operateVo.getRegister()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.USER_HOLD).setValue(operateVo.getHoldCard()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.USER_SMS).setValue(operateVo.getSms()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.SYS_PROJECT).setValue(operateVo.getProject()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.SYS_BEIAN).setValue(operateVo.getBeian()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.SYS_NICKNAME).setValue(operateVo.getNickname()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.SYS_SCREENSHOT).setValue(operateVo.getScreenshot()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.SYS_WATERMARK).setValue(operateVo.getWatermark()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.SYS_CAPTCHA).setValue(operateVo.getCaptcha()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.SYS_PACKET).setValue(operateVo.getPacket()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.SYS_SHARE).setValue(operateVo.getShare()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.SYS_HOOK).setValue(operateVo.getHook()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.SYS_RECALL).setValue(operateVo.getRecall()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.SYS_SIGNTOAL).setValue(operateVo.getSigntoal()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.SYS_SIGN).setValue(operateVo.getSign()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.SYS_INVO).setValue(operateVo.getInvo()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.SYS_INVOADUS).setValue(operateVo.getInvoadus()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.SYS_SENDMOMENT).setValue(operateVo.getSendmoment()));
        //清除缓存
        String redisKey = AppConstants.REDIS_COMMON_CONFIG; // 假设配置缓存KEY
        redisUtils.delete(redisKey);
        logger.info("清除缓存：key {}",redisKey);
    }

}
