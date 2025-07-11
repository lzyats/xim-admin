package com.platform.modules.chat.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 回复类型
 */
@Getter
public enum RobotReplyEnum {

    /**
     * 关注回复
     */
    SUBSCRIBE("subscribe", "关注回复"),
    /**
     * 关键词回复
     */
    REPLY("reply", "关键词回复"),
    /**
     * 事件回复
     */
    EVEN("even", "事件回复"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;

    RobotReplyEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
