package com.platform.modules.sys.controller;

import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.config.CaptchaConfig;
import com.platform.common.constant.HeadConstant;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.LoginException;
import com.platform.common.shiro.ShiroLoginAuth;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.utils.LogUtils;
import com.platform.common.validation.ValidateGroup;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.sys.service.SysTokenService;
import com.platform.modules.sys.vo.LoginVo01;
import com.platform.modules.sys.vo.LoginVo02;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 登录验证
 */
@RestController
@Slf4j
@RequestMapping("/auth")
public class SysAuthController extends BaseController {

    @Autowired
    private CaptchaConfig captchaConfig;

    @Resource
    private SysTokenService sysTokenService;

    /**
     * 登录方法
     */
    @PostMapping("/login")
    public AjaxResult login(@Validated(ValidateGroup.LOGIN.class) @RequestBody LoginVo01 loginVo) {
        String username = loginVo.getUsername();
        String password = loginVo.getPassword();
        String msg = null;
        try {
            ShiroLoginAuth auth = new ShiroLoginAuth(username, password);
            ShiroUtils.getSubject().login(auth);
        } catch (LoginException e) {
            msg = e.getMessage();
        } catch (AuthenticationException e) {
            msg = "账号或密码不正确";
        } catch (Exception e) {
            msg = "未知异常";
            log.error(msg, e);
        }
        if (!StringUtils.isEmpty(msg)) {
            LogUtils.recordLogin(username, YesOrNoEnum.NO, msg);
            return AjaxResult.fail(msg);
        }
        LogUtils.recordLogin(username, YesOrNoEnum.YES, "登录成功");
        // 生成TOKEN
        LoginVo02 data = sysTokenService.generateToken();
        return AjaxResult.success(data);
    }

    /**
     * 生成验证码
     */
    @GetMapping("/getCaptcha")
    public AjaxResult getCaptcha() {
        return AjaxResult.success(captchaConfig.easyCaptcha());
    }

    /**
     * 退出系统
     */
    @SubmitRepeat
    @RequiresPermissions(HeadConstant.PERM_ADMIN)
    @GetMapping("/logout")
    public AjaxResult logout() {
        String username = ShiroUtils.getUsername();
        try {
            sysTokenService.deleteToken(ShiroUtils.getToken());
            ShiroUtils.getSubject().logout();
            log.info("退出成功。。。。");
        } catch (Exception ex) {
            log.error("退出异常", ex);
        } finally {
            LogUtils.recordLogin(username, YesOrNoEnum.YES, "退出成功");
        }
        return AjaxResult.successMsg("退出成功");
    }
}
