package com.platform.modules.chat.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 菜单类型枚举
 */
@Getter
public enum RobotMenuEnum {

    /**
     * 点击
     */
    EVEN("even", "点击"),
    /**
     * 网址
     */
    VIEW("view", "网址"),
    /**
     * 页面
     */
    PAGE("page", "页面"),
    /**
     * 小程序
     */
    MINI("mini", "小程序"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;

    RobotMenuEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
