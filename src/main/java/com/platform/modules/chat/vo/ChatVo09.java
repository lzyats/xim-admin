package com.platform.modules.chat.vo;

import cn.hutool.core.util.NumberUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.platform.common.core.EnumUtils;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.common.web.domain.JsonDecimalDeserializer;
import com.platform.modules.sys.domain.SysDict;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 群组容量
 * </p>
 */
@Data
@NoArgsConstructor
@Accessors(chain = true) // 链式调用
public class ChatVo09 extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long principal;
    /**
     * 等级
     */
    private Integer groupLevel;
    /**
     * 容量
     */
    private Integer levelCount;
    /**
     * 价格
     */
    @Digits(integer = 8, fraction = 2, message = "充值金额格式不正确")
    @DecimalMin(value = "0.00", message = "充值金额不能小于0.01")
    @DecimalMax(value = "999999.00", message = "充值金额不能大于999999.00")
    @NotNull(message = "充值金额不能为空")
    @JsonDeserialize(using = JsonDecimalDeserializer.class)
    private BigDecimal levelPrice;
    /**
     * 状态
     */
    private YesOrNoEnum status;

    public ChatVo09(SysDict sysDict) {
        this.principal = sysDict.getDictId();
        this.groupLevel = sysDict.getDictSort();
        this.levelCount = NumberUtil.parseInt(sysDict.getDictCode());
        this.levelPrice = NumberUtil.toBigDecimal(sysDict.getDictName()).setScale(2);
        this.status = EnumUtils.toEnum(YesOrNoEnum.class, sysDict.getRemark(), YesOrNoEnum.NO);
    }

    public String getStatusLabel() {
        if (status == null) {
            return null;
        }
        switch (status) {
            case NO:
                return "停用";
            case YES:
                return "正常";
            default:
                return null;
        }
    }

}
