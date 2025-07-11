package com.platform.modules.chat.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;

@Data
@Accessors(chain = true) // 链式调用
public class ChatVo06 {

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空")
    @Size(max = 20, message = "账号长度不能超过20个字符")
    private String phone;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    @Size(max = 15, message = "昵称长度不能大于15")
    private String nickname;

    /**
     * 特殊标识
     */
    @NotNull(message = "特殊标识不能为空")
    private YesOrNoEnum special;

    /**
     * 邮箱
     */
    @Size(max = 50, message = "邮箱长度不能大于50")
    private String email;

}
