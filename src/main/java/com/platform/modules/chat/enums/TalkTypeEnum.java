package com.platform.modules.chat.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 消息聊天枚举
 */
@Getter
public enum TalkTypeEnum {

    /**
     * 单聊
     */
    USER("1", "单聊"),
    /**
     * 群聊
     */
    GROUP("2", "群聊"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;

    TalkTypeEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
