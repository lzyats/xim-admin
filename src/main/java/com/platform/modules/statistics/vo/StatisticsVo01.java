package com.platform.modules.statistics.vo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true) // 链式调用
public class StatisticsVo01 {

    /**
     * 日期
     */
    private Date createTime;
    /**
     * 总数
     */
    private Integer totalCount;
    /**
     * 总金额
     */
    private BigDecimal totalAmount;
    /**
     * 平台
     */
    private Integer platformCount;
    /**
     * 平台
     */
    private BigDecimal platformAmount;
    /**
     * 支付宝
     */
    private Integer aliCount;
    /**
     * 支付宝
     */
    private BigDecimal aliAmount;
    /**
     * 微信
     */
    private Integer wxCount;
    /**
     * 微信
     */
    private BigDecimal wxAmount;

    public StatisticsVo01(Date createTime) {
        this.createTime = createTime;
        this.totalCount = 0;
        this.totalAmount = BigDecimal.ZERO;
        this.platformCount = 0;
        this.platformAmount = BigDecimal.ZERO;
        this.aliCount = 0;
        this.aliAmount = BigDecimal.ZERO;
        this.wxCount = 0;
        this.wxAmount = BigDecimal.ZERO;
    }

    public StatisticsVo01() {
        this.totalCount = 0;
        this.totalAmount = BigDecimal.ZERO;
        this.platformCount = 0;
        this.platformAmount = BigDecimal.ZERO;
        this.aliCount = 0;
        this.aliAmount = BigDecimal.ZERO;
        this.wxCount = 0;
        this.wxAmount = BigDecimal.ZERO;
    }

    public String getLabel() {
        if (createTime == null) {
            return "当月汇总";
        }
        return DateUtil.format(createTime, DatePattern.NORM_DATE_FORMAT);
    }
}
