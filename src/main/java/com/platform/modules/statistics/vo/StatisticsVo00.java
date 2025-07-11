package com.platform.modules.statistics.vo;

import com.platform.modules.wallet.enums.TradePayEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true) // 链式调用
public class StatisticsVo00 {

    /**
     * 汇总
     */
    private String totalLabel;
    /**
     * 总数量
     */
    private Integer totalCount;
    /**
     * 总金额
     */
    private BigDecimal totalAmount;
    /**
     * 渠道
     */
    private String label;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 金额
     */
    private BigDecimal amount;

    public StatisticsVo00(StatisticsVo01 dataVo, TradePayEnum tradePay) {
        this.totalLabel = dataVo.getLabel();
        this.totalCount = dataVo.getTotalCount();
        this.totalAmount = dataVo.getTotalAmount();
        this.label = tradePay.getInfo();
        switch (tradePay) {
            case SYS_PAY:
                this.count = dataVo.getPlatformCount();
                this.amount = dataVo.getPlatformAmount();
                break;
            case ALI_PAY:
                this.count = dataVo.getAliCount();
                this.amount = dataVo.getAliAmount();
                break;
            case WX_PAY:
                this.count = dataVo.getWxCount();
                this.amount = dataVo.getWxAmount();
                break;
        }
    }

}
