package com.platform.modules.operate.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.JsonDecimalDeserializer;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@Accessors(chain = true) // 链式调用
public class OperateVo04 {

    /**
     * 提现实名
     */
    @NotNull(message = "提现实名不能为空")
    private YesOrNoEnum auth;
    /**
     * 提现次数
     */
    @NotNull(message = "提现次数不能为空")
    @Min(value = 1, message = "提现次数不能小于1")
    @Max(value = 9999, message = "提现次数不能超过9999")
    private Integer count;
    /**
     * 提现服务费
     */
    @Digits(integer = 8, fraction = 2, message = "提现服务费格式不正确")
    @DecimalMin(value = "0.00", message = "提现服务费不能小于0.00元")
    @DecimalMax(value = "999999.00", message = "提现服务费不能大于999999.00元")
    @NotNull(message = "提现服务费不能为空")
    @JsonDeserialize(using = JsonDecimalDeserializer.class)
    private BigDecimal cost;
    /**
     * 最大金额
     */
    @Digits(integer = 8, fraction = 2, message = "最大金额格式不正确")
    @DecimalMin(value = "0.01", message = "最大金额不能小于0.01元")
    @DecimalMax(value = "999999.00", message = "最大金额不能大于999999.00元")
    @NotNull(message = "最大金额不能为空")
    @JsonDeserialize(using = JsonDecimalDeserializer.class)
    private BigDecimal max;
    /**
     * 最小金额
     */
    @Digits(integer = 8, fraction = 2, message = "最小金额格式不正确")
    @DecimalMin(value = "0.01", message = "最小金额不能小于0.01元")
    @DecimalMax(value = "999999.00", message = "最小金额不能大于999999.00元")
    @NotNull(message = "最小金额不能为空")
    @JsonDeserialize(using = JsonDecimalDeserializer.class)
    private BigDecimal min;
    /**
     * 提现利率
     */
    @Digits(integer = 8, fraction = 2, message = "提现利率格式不正确")
    @DecimalMin(value = "0.00", message = "提现利率不能小于0.00%")
    @DecimalMax(value = "100.00", message = "提现利率不能大于100.00%")
    @NotNull(message = "提现利率不能为空")
    @JsonDeserialize(using = JsonDecimalDeserializer.class)
    private BigDecimal rate;
    /**
     * 提现汇率
     */
    @Digits(integer = 8, fraction = 2, message = "提现利率格式不正确")
    @DecimalMin(value = "0.00", message = "提现利率不能小于0.00%")
    @DecimalMax(value = "100.00", message = "提现利率不能大于100.00%")
    @NotNull(message = "提现汇率不能为空")
    @JsonDeserialize(using = JsonDecimalDeserializer.class)
    private BigDecimal rates;
    /**
     * 提现说明
     */
    @NotBlank(message = "提现说明不能为空")
    private String remark;

}
