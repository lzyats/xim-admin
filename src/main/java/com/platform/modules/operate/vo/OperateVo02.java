package com.platform.modules.operate.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true) // 链式调用
public class OperateVo02 {

    /**
     * 审核手机
     */
    @NotBlank(message = "审核手机不能为空")
    private String phone;
    /**
     * 审核开关
     */
    @NotNull(message = "审核开关不能为空")
    private YesOrNoEnum audit;


}
