package com.platform.modules.sys.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 菜单类型
 */
@Getter
public enum SysMenuTypeEnum {

    /**
     * 目录
     */
    M("1", "目录"),
    /**
     * 菜单
     */
    C("2", "菜单"),
    /**
     * 按钮
     */
    F("3", "按钮"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;

    SysMenuTypeEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
