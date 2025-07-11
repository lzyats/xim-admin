package com.platform.modules.home.controller;

import com.platform.common.constant.HeadConstant;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.home.service.HomeService;
import com.platform.modules.statistics.service.StatisticsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 首页 控制层
 * </p>
 */
@RestController
@RequestMapping("/home")
public class HomeController extends BaseController {

    @Resource
    private HomeService homeService;

    @Resource
    private StatisticsService statisticsService;

    /**
     * 用户版本
     */
    @RequiresPermissions(HeadConstant.PERM_ADMIN)
    @GetMapping("/user/version")
    public AjaxResult userVersion() {
        return AjaxResult.success(homeService.userVersion());
    }

    /**
     * 用户设备
     */
    @RequiresPermissions(HeadConstant.PERM_ADMIN)
    @GetMapping("/user/device")
    public AjaxResult userDevice() {
        return AjaxResult.success(homeService.userDevice());
    }

    /**
     * 用户走势
     */
    @RequiresPermissions(HeadConstant.PERM_ADMIN)
    @GetMapping("/user/trend")
    public AjaxResult userTrend() {
        return AjaxResult.success(statisticsService.userTrend());
    }

    /**
     * 用户日活
     */
    @RequiresPermissions(HeadConstant.PERM_ADMIN)
    @GetMapping("/user/visit")
    public AjaxResult userVisit() {
        return AjaxResult.success(statisticsService.userVisit());
    }

}

