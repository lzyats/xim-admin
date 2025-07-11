package com.platform.modules.sys.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录对象
 */
@Data
public class LoginVo01 {
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

}
