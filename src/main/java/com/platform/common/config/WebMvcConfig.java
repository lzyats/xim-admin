package com.platform.common.config;

import cn.hutool.core.io.file.FileNameUtil;
import com.platform.common.web.interceptor.CaptchaInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * 通用配置
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    private CaptchaInterceptor captchaInterceptor;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(ApplicationConfig.objectMapper());
    }

    @Value("${platform.rootPath}")
    private String rootPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // favicon.ico
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/static/");
        // file
        registry.addResourceHandler("/file/**").addResourceLocations("file:" + rootPath + FileNameUtil.UNIX_SEPARATOR);
    }

    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(captchaInterceptor).addPathPatterns("/auth/login");
    }

}