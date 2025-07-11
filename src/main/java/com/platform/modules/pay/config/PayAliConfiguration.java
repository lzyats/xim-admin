package com.platform.modules.pay.config;

import cn.hutool.core.io.resource.ResourceUtil;
import com.alipay.api.AlipayApiException;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayAliConfiguration {

    @Autowired
    private PayAliConfig payAliConfig;

    @Bean
    public AliPayApiConfig aliPayApiConfig() throws AlipayApiException {
        AliPayApiConfig aliPayApiConfig = AliPayApiConfig.builder()
                // appId
                .setAppId(payAliConfig.getAppId())
                // 应用私钥
                .setPrivateKey(payAliConfig.getAppPrivateKey())
                // 应用公钥
                .setAppCertContent(ResourceUtil.readUtf8Str(payAliConfig.getAppPublicPath()))
                // 支付宝公钥
                .setAliPayCertContent(ResourceUtil.readUtf8Str(payAliConfig.getAliPayPublicPath()))
                // 支付宝根证书
                .setAliPayRootCertContent(ResourceUtil.readUtf8Str(payAliConfig.getAliPayRootPath()))
                // 支付网关
                .setServiceUrl(payAliConfig.getServiceUrl())
                .buildByCertContent();
        return AliPayApiConfigKit.putApiConfig(aliPayApiConfig);
    }

}
