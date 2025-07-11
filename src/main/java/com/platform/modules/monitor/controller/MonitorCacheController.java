package com.platform.modules.monitor.controller;

import com.platform.common.web.domain.AjaxResult;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

/**
 * 缓存监控
 */
@RestController
@RequestMapping("/monitor/cache")
@CrossOrigin
public class MonitorCacheController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 列表
     */
    @RequiresPermissions("monitor:cache:list")
    @GetMapping()
    public AjaxResult getInfo() {
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info());
        return AjaxResult.success(info);
    }
}