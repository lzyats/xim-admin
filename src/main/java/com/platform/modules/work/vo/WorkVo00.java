package com.platform.modules.work.vo;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true) // 链式调用
public class WorkVo00 {

    /**
     * 消息id
     */
    private Long msgId;
    /**
     * 同步id
     */
    private Long syncId;
    /**
     * 发送状态
     */
    private String status = "0";
    /**
     * 时间
     */
    private Long createTime;

    public WorkVo00(Long msgId, Long syncId) {
        this.msgId = msgId;
        this.syncId = syncId;
        this.createTime = DateUtil.current();
    }
}
