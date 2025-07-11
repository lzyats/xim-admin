package com.platform.modules.chat.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 成员类型
 */
@Getter
public enum MemberTypeEnum {

    /**
     * 群主
     */
    MASTER("master", "群主"),
    /**
     * 管理员
     */
    MANAGER("manager", "管理员"),
    /**
     * 成员
     */
    NORMAL("normal", "成员"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;

    MemberTypeEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
