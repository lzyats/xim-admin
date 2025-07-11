package com.platform.modules.statistics.vo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true) // 链式调用
@NoArgsConstructor
public class StatisticsVo03 {

    /**
     * 汇总
     */
    private String label = "当月汇总";
    /**
     * 收入
     */
    private BigDecimal income = BigDecimal.ZERO;
    /**
     * 支出
     */
    private BigDecimal disburse = BigDecimal.ZERO;
    /**
     * 消费
     */
    private BigDecimal consume = BigDecimal.ZERO;
    /**
     * 服务
     */
    private BigDecimal charge = BigDecimal.ZERO;
    /**
     * 统计
     */
    private BigDecimal total = BigDecimal.ZERO;

    public StatisticsVo03(Date createTime, BigDecimal income, BigDecimal disburse, BigDecimal consume, BigDecimal charge) {
        this.label = DateUtil.format(createTime, DatePattern.NORM_DATE_FORMAT);
        this.income = income;
        this.disburse = disburse;
        this.consume = consume;
        this.charge = charge;
        this.total = consume.add(charge);
    }

}
