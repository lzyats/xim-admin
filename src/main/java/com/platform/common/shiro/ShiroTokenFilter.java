package com.platform.common.shiro;

import cn.hutool.json.JSONUtil;
import com.platform.common.constant.HeadConstant;
import com.platform.common.enums.ResultEnum;
import com.platform.common.exception.LoginException;
import com.platform.common.web.domain.AjaxResult;
import lombok.SneakyThrows;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * auth2过滤器
 */
public class ShiroTokenFilter extends AuthenticatingFilter {

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse servletResponse) {
        // 请求token
        ShiroLoginToken token = getToken(request);
        if (token == null) {
            return null;
        }
        return token;
    }

    @SneakyThrows
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (RequestMethod.OPTIONS.name().equalsIgnoreCase(httpServletRequest.getMethod())) {
            return true;
        }
        if ("/favicon.ico".equals(httpServletRequest.getRequestURI())) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 请求token
        ShiroLoginToken token = getToken(request);
        if (token == null) {
            return error(response, ResultEnum.UNAUTHORIZED, null);
        }
        try {
            getSubject(request, response).login(token);
            return true;
        } catch (LoginException e) {
            return error(response, ResultEnum.UNAUTHORIZED, e.getMessage());
        } catch (AuthenticationException e) {
            return error(response, ResultEnum.UNAUTHORIZED, null);
        }
    }

    private boolean error(ServletResponse response, ResultEnum resultCode, String msg) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.getWriter().print(JSONUtil.toJsonStr(AjaxResult.result(resultCode, msg)));
        return false;
    }

    /**
     * 请求的token
     */
    private ShiroLoginToken getToken(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = queryToken(httpRequest, HeadConstant.TOKEN_HEADER_ADMIN);
        if (StringUtils.isEmpty(token)) {
            token = httpRequest.getParameter(HeadConstant.TOKEN_HEADER_ADMIN);
        }
        if (!StringUtils.isEmpty(token)) {
            return new ShiroLoginToken(token);
        }
        return null;
    }

    /**
     * 查询token
     */
    private String queryToken(HttpServletRequest httpRequest, String headerKey) {
        // 查询头部
        String token = httpRequest.getHeader(headerKey);
        if (!StringUtils.isEmpty(token)) {
            return token;
        }
        // 查询参数
        return httpRequest.getParameter(HeadConstant.TOKEN_HEADER_ADMIN);
    }

}
