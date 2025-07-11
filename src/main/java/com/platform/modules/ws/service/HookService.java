package com.platform.modules.ws.service;

import com.platform.common.web.vo.LabelVo;
import com.platform.modules.push.enums.PushAuditEnum;

/**
 * Hook服务
 */
public interface HookService {

    /**
     * 处理推送
     */
    void onMessage(LabelVo labelVo);

    /**
     * 处理推送
     */
    void handle(PushAuditEnum pushAudit, Long value);

    /**
     * 连接
     */
    void connection();

}
