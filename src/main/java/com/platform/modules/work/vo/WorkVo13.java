package com.platform.modules.work.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true) // 链式调用
public class WorkVo13 {

    @NotNull(message = "用户id不能为空")
    private Long userId;

    @Size(max = 200, message = "备注不能大于200")
    private String remark;

}
