package com.platform.modules.operate.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true) // 链式调用
public class OperateVo01 {

    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空")
    @Size(max = 200, message = "内容不能超过200个字符")
    private String content;
    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    private YesOrNoEnum status;

}
