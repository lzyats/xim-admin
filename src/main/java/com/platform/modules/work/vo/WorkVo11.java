package com.platform.modules.work.vo;

import com.platform.modules.chat.domain.ChatUserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true) // 链式调用
@NoArgsConstructor
public class WorkVo11 {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证
     */
    private String idCard;
    /**
     * 正面
     */
    private String identity1;
    /**
     * 反面
     */
    private String identity2;
    /**
     * 手持
     */
    private String holdCard;
    /**
     * 认证原因
     */
    private String reason;
    /**
     * 认证时间
     */
    private Date createTime;

    public WorkVo11(ChatUserInfo userInfo) {
        this.userId = userInfo.getUserId();
        this.name = userInfo.getName();
        this.idCard = userInfo.getIdCard();
        this.identity1 = userInfo.getIdentity1();
        this.identity2 = userInfo.getIdentity2();
        this.holdCard = userInfo.getHoldCard();
        this.reason = userInfo.getAuthReason();
        this.createTime = userInfo.getAuthTime();
    }

}
