package com.platform.modules.ws.config;

import com.platform.common.constant.HeadConstant;
import com.platform.modules.ws.listener.RedisMessageListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * redis配置
 */
@Configuration
public class WebSocketContainer {

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory factory, RedisMessageListener listener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(listener, new ChannelTopic(HeadConstant.REDIS_CHANNEL_ADMIN));
        return container;
    }

}
