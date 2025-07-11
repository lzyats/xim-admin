package com.platform.common.shiro;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * token
 */
@Data
public class ShiroLoginAuth implements AuthenticationToken {

    private String username;
    private char[] password;

    public ShiroLoginAuth(String username, String password) {
        this.username = username;
        this.password = password.toCharArray();
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

}
