package com.platform.modules.chat.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true) // 链式调用
public class ChatVo02 {

    @NotNull(message = "服务不能为空")
    private Long robotId;

    @NotBlank(message = "菜单不能为空")
    private String menu;

}
