package com.platform.modules.ws.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import com.platform.common.config.PlatformConfig;
import com.platform.common.constant.AppConstants;
import com.platform.common.core.EnumUtils;
import com.platform.common.redis.RedisUtils;
import com.platform.common.utils.timer.TimerUtils;
import com.platform.common.web.vo.LabelVo;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.push.dto.PushSetting;
import com.platform.modules.push.enums.PushAuditEnum;
import com.platform.modules.push.enums.PushSettingEnum;
import com.platform.modules.push.service.PushService;
import com.platform.modules.ws.handler.BootWebSocketHandler;
import com.platform.modules.ws.service.HookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service("hookService")
public class HookServiceImpl implements HookService {

    @Resource
    private BootWebSocketHandler bootWebSocketHandler;

    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private ChatUserService chatUserService;

    @Resource
    private PushService pushService;

    @Override
    public void onMessage(LabelVo labelVo) {
        // 转换
        PushAuditEnum pushAudit = EnumUtils.toEnum(PushAuditEnum.class, labelVo.getLabel());
        if (pushAudit == null) {
            return;
        }
        // 拉取
        Long value = redisUtils.hIncrement(AppConstants.REDIS_CHAT_ADMIN, pushAudit.getCode(), 0);
        // 发送
        this.sendMsg(pushAudit, value);
    }

    @Override
    public void handle(PushAuditEnum pushAudit, Long value) {
        // 存储
        redisUtils.hPut(AppConstants.REDIS_CHAT_ADMIN, pushAudit.getCode(), NumberUtil.toStr(value), PlatformConfig.TIMEOUT, TimeUnit.DAYS);
        // 发送
        this.sendMsg(pushAudit, value);
    }

    @Override
    public void connection() {
        // 拉取
        Map<Object, Object> maps = redisUtils.hEntries(AppConstants.REDIS_CHAT_ADMIN);
        // 循环
        for (PushAuditEnum pushAudit : PushAuditEnum.values()) {
            Object object = maps.get(pushAudit.getCode());
            Long value = 0L;
            if (object != null) {
                value = NumberUtil.parseLong(object.toString());
            }
            // 发送
            this.sendMsg(pushAudit, value);
        }
        // 异常用户
        chatUserService.querySpecialCount();
    }

    /**
     * 发送push
     */
    private void sendMsg(PushAuditEnum pushAudit, Long value) {
        switch (pushAudit) {
            case APPLY_AUTH:
            case APPLY_CASH:
            case APPLY_BANNED:
            case APPLY_SPECIAL:
            case INFORM_USER:
            case INFORM_GROUP:
            case USER_FEEDBACK:
                break;
            default:
                return;
        }
        if (value.intValue() < 0) {
            value = 0L;
        }
        LabelVo labelVo = new LabelVo(pushAudit.getCode(), value);
        // 异步发送
        TimerUtils.instance().addTask((timeout) -> {
            bootWebSocketHandler.sendMsg(JSONUtil.toJsonStr(labelVo));
        }, 500, TimeUnit.MILLISECONDS);
        // 异步发送
        PushSetting setting = new PushSetting(PushSettingEnum.BADGER, pushAudit.getCode(), NumberUtil.toStr(value));
        TimerUtils.instance().addTask((timeout) -> {
            pushService.pushSetting(setting, Arrays.asList(AppConstants.ROBOT_ID));
        }, 500, TimeUnit.MILLISECONDS);
    }

}
