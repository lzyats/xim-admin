package com.platform.modules.statistics.vo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true) // 链式调用
public class StatisticsVo05 {

    /**
     * 汇总
     */
    private String label;
    /**
     * 总数量
     */
    private Long count;
    /**
     * 总金额
     */
    private BigDecimal amount;

    public StatisticsVo05() {
        this.label = "当月汇总";
        this.count = 0L;
        this.amount = BigDecimal.ZERO;
    }

    public StatisticsVo05(Date createTime, Long count, BigDecimal amount) {
        this.label = DateUtil.format(createTime, DatePattern.NORM_DATE_FORMAT);
        this.count = count;
        this.amount = amount;
    }

}
