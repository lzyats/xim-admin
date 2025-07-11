package com.platform.modules.sys.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录对象
 */
@Data
@NoArgsConstructor
public class LoginVo02 {
    /**
     * 登录token
     */
    private String token;

    public LoginVo02(String token) {
        this.token = token;
    }
}
