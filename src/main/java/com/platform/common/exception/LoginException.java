package com.platform.common.exception;

import com.platform.common.enums.ResultEnum;
import lombok.Getter;
import org.apache.shiro.authc.AuthenticationException;

/**
 * 登录异常
 */
public class LoginException extends AuthenticationException {

    @Getter
    private ResultEnum code;

    public LoginException(String message) {
        super(message);
        this.code = ResultEnum.FAIL;
    }

    public LoginException(ResultEnum resultCode) {
        super(resultCode.getInfo());
        this.code = resultCode;
    }

}
