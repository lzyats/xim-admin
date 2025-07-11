package com.platform.modules.chat.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true) // 链式调用
public class ChatVo04 {

    @NotNull(message = "用户id不能为空")
    private Long userId;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Size(max = 50, message = "邮箱长度不能大于50")
    private String email;

}
