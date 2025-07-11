package com.platform.modules.approve.vo;

import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.validation.ValidateGroup;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true) // 链式调用
public class ApproveVo01 {

    /**
     * 交易id
     */
    @NotNull(message = "交易id不能为空")
    private Long tradeId;
    /**
     * 审批状态
     */
    @NotNull(message = "审批状态不能为空")
    private YesOrNoEnum status;
    /**
     * 审批状态
     */
    @NotNull(message = "审批状态不能为空", groups = ValidateGroup.ONE.class)
    private YesOrNoEnum auto;
    /**
     * 拒绝原因
     */
    @NotBlank(message = "拒绝原因不能为空", groups = ValidateGroup.TWO.class)
    @Size(max = 50, message = "拒绝原因不能大于50", groups = ValidateGroup.TWO.class)
    private String reason;

}
