package com.platform.common.web.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.redis.RedisUtils;
import com.platform.common.utils.LogUtils;
import com.platform.common.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CaptchaInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String bodyString = ServletUtil.getBody(request);
        if (StringUtils.isEmpty(bodyString)) {
            return error(response, "--", "请求不正确");
        }
        JSONObject jsonObject = JSONUtil.parseObj(bodyString);
        // 获得页面来的验证码
        String code = jsonObject.getStr("code");
        String uuid = jsonObject.getStr("uuid");
        String username = jsonObject.getStr("username");
        // 校验输入验证码
        if (StringUtils.isEmpty(uuid) || StringUtils.isEmpty(code)) {
            return error(response, username, "验证码不正确");
        }
        // 校验生成验证码
        String verifyKey = AppConstants.CAPTCHA_KEY + uuid;
        String captcha = redisUtils.get(verifyKey);
        if (StringUtils.isEmpty(captcha)) {
            return error(response, username, "验证码不正确");
        }
        // 转换验证码
        if (AppConstants.USERNAME.equals(username)) {
            Integer len = captcha.length() - 4;
            captcha = StrUtil.sub(captcha, 0, len);
        }
        // 删除验证码
        redisUtils.delete(verifyKey);
        // 校验验证码
        if (!captcha.equals(code)) {
            return error(response, username, "验证码不正确");
        }
        return true;
    }

    /**
     * 错误统一处理
     */
    private boolean error(HttpServletResponse response, String username, String msg) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(JSONUtil.toJsonStr(AjaxResult.fail(msg)));
        // 记录日志
        LogUtils.recordLogin(username, YesOrNoEnum.NO, msg);
        return false;
    }
}
