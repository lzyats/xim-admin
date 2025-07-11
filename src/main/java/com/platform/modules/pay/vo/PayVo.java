package com.platform.modules.pay.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true) // 链式调用
public class PayVo {

    /**
     * 交易ID
     */
    private Long tradeId;
    /**
     * 支付金额
     */
    private BigDecimal amount;
    /**
     * 账号
     */
    private String wallet;
    /**
     * 姓名
     */
    private String name;

}
