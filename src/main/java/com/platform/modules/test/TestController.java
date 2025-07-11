package com.platform.modules.test;

import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试服务
 */
@RestController
@Slf4j
@RequestMapping("/test")
public class TestController extends BaseController {

    @GetMapping("/test1")
    public AjaxResult test1() {
        return AjaxResult.success();
    }

    @GetMapping("/test2")
    public AjaxResult test2() {
        return AjaxResult.success();
    }

}
