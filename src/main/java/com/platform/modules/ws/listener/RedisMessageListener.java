package com.platform.modules.ws.listener;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.platform.common.web.vo.LabelVo;
import com.platform.modules.ws.service.HookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class RedisMessageListener implements MessageListener {

    @Resource
    private HookService hookService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 消息
        String content = StrUtil.utf8Str(message.getBody());
        // 消息
        if (JSONUtil.isTypeJSON(content)) {
            hookService.onMessage(JSONUtil.toBean(content, LabelVo.class));
        }
    }

}
