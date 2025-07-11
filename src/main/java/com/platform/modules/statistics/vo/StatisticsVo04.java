package com.platform.modules.statistics.vo;

import com.platform.modules.wallet.enums.TradePayEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true) // 链式调用
@NoArgsConstructor
public class StatisticsVo04 {

    /**
     * 类型
     */
    private String type;
    /**
     * 金额
     */
    private BigDecimal amount = BigDecimal.ZERO;
    /**
     * 类型
     */
    private String label;
    /**
     * 金额
     */
    private BigDecimal value;

    public StatisticsVo04(String type, BigDecimal amount) {
        this.type = type;
        this.amount = amount;
    }

    public StatisticsVo04(TradePayEnum payType, BigDecimal value, BigDecimal amount) {
        this.type = "充值";
        this.label = payType.getInfo();
        this.value = value;
        this.amount = amount;
    }

}
