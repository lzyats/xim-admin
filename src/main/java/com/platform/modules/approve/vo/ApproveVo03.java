package com.platform.modules.approve.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true) // 链式调用
public class ApproveVo03 {

    /**
     * 用户ID
     */
    @NotNull(message = "用户id不能为空")
    private Long userId;
    /**
     * 审批状态
     */
    @NotNull(message = "审批状态不能为空")
    private YesOrNoEnum status;

}
