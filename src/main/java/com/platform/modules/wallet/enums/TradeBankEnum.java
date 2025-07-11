package com.platform.modules.wallet.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 交易银行类型
 */
@Getter
public enum TradeBankEnum {

    /**
     * 支付宝
     */
    ALI_PAY("1", "支付宝"),
    /**
     * 银行卡
     */
    BANK("2", "银行卡"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;

    TradeBankEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
