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
public class StatisticsVo02 {

    /**
     * 汇总
     */
    private String label = "当月汇总";
    /**
     * 通过
     */
    private Long passCount = 0L;
    /**
     * 通过
     */
    private BigDecimal passAmount = BigDecimal.ZERO;
    /**
     * 驳回
     */
    private Long rejectCount = 0L;
    /**
     * 驳回
     */
    private BigDecimal rejectAmount = BigDecimal.ZERO;

    public StatisticsVo02(Date createTime, Long passCount, BigDecimal passAmount, Long rejectCount, BigDecimal rejectAmount) {
        this.label = DateUtil.format(createTime, DatePattern.NORM_DATE_FORMAT);
        this.passCount = passCount;
        this.passAmount = passAmount;
        this.rejectCount = rejectCount;
        this.rejectAmount = rejectAmount;
    }

}
