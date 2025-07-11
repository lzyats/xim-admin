package com.platform.modules.chat.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 封禁类型
 */
@Getter
public enum BannedTimeEnum {

    /**
     * 0天
     */
    DAY_0("0", "清除", 0),
    /**
     * 1天
     */
    DAY_1("1", "1天", 1),
    /**
     * 3天
     */
    DAY_3("2", "3天", 3),
    /**
     * 一周
     */
    DAY_7("3", "一周", 7),
    /**
     * 1月
     */
    MONTH_1("4", "1个月", 30),
    /**
     * 3月
     */
    MONTH_3("5", "3个月", 90),
    /**
     * 永久
     */
    FOREVER("6", "永久", 999),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String name;
    private Integer value;

    BannedTimeEnum(String code, String name, Integer value) {
        this.code = code;
        this.name = name;
        this.value = value;
    }

}
