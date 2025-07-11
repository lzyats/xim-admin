package com.platform.modules.operate.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.platform.common.web.domain.JsonDecimalDeserializer;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true) // 链式调用
public class OperateVo03 {

    /**
     * 充值总金额
     */
    @Digits(integer = 8, fraction = 2, message = "充值总金额格式不正确")
    @DecimalMin(value = "0.00", message = "充值总金额不能小于0.00元")
    @DecimalMax(value = "999999.00", message = "充值总金额不能大于999999.00元")
    @NotNull(message = "充值总金额不能为空")
    @JsonDeserialize(using = JsonDecimalDeserializer.class)
    private BigDecimal amount;
    /**
     * 充值时间
     */
    @NotNull(message = "充值时间不能为空")
    private List<String> timeRange;
    /**
     * 充值次数
     */
    @NotNull(message = "充值次数不能为空")
    @Min(value = 1, message = "充值次数不能小于1")
    @Max(value = 9999, message = "充值次数不能超过9999")
    private Integer count;
    /**
     * 安卓支付方式
     */
    private String android;
    /**
     * ios支付方式
     */
    private String ios;

}
