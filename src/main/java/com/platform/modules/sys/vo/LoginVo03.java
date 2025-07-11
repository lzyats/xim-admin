package com.platform.modules.sys.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录对象
 */
@Data
public class LoginVo03 {

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String oldPwd;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    private String newPwd;

}
