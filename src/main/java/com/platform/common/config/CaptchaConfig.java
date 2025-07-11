package com.platform.common.config;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.platform.common.constant.AppConstants;
import com.platform.common.redis.RedisUtils;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.service.ChatConfigService;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 验证码配置类
 */
@Component
@Slf4j
public class CaptchaConfig {

    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private ChatConfigService chatConfigService;

    /**
     * 生成验证码
     */
    public Dict easyCaptcha() {
        // 算术类型 https://gitee.com/whvse/EasyCaptcha
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36);
        // 运算的结果
        String code = captcha.text();
        if (StrUtil.startWith(code, "-")) {
            return easyCaptcha();
        }
        // 唯一标识
        String uuid = IdUtil.simpleUUID();
        String key = AppConstants.CAPTCHA_KEY + uuid;
        String value = code + chatConfigService.queryConfig(ChatConfigEnum.SYS_CAPTCHA).getStr();
        Console.log(StrUtil.format("生成uuid:[{}]，验证码：[{}]", uuid, value));
        redisUtils.set(key, value, AppConstants.CAPTCHA_TIMEOUT, TimeUnit.MINUTES);
        return Dict.create()
                .set("uuid", uuid)
                .set("img", captcha.toBase64());
    }

}
