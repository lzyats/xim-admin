package com.platform.modules.ws.interceptor;

import cn.hutool.extra.spring.SpringUtil;
import com.platform.common.constant.HeadConstant;
import com.platform.common.redis.RedisUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

public class BootWebSocketInterceptor extends HttpSessionHandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        // 接收前端传来的参数
        String token = ((ServletServerHttpRequest) request).getServletRequest().getParameter(HeadConstant.TOKEN_HEADER_ADMIN);
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        RedisUtils redisUtils = SpringUtil.getBean(RedisUtils.class);
        if (!redisUtils.hasKey(HeadConstant.TOKEN_REDIS_ADMIN + token)) {
            return false;
        }
        // 将参数放到attributes
        attributes.put(HeadConstant.TOKEN_HEADER_ADMIN, token);
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

}
