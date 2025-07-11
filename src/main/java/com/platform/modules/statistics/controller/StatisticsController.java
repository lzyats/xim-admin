package com.platform.modules.statistics.controller;

import com.platform.common.enums.TimeUnitEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.domain.BaseEntity;
import com.platform.common.web.vo.LabelVo;
import com.platform.modules.statistics.service.StatisticsService;
import com.platform.modules.statistics.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 统计服务
 */
@RestController
@RequestMapping("/statistics")
@Slf4j
public class StatisticsController extends BaseController {

    @Resource
    private StatisticsService statisticsService;

    /**
     * 用户日活
     */
    @RequiresPermissions("statistics:user:visit")
    @GetMapping("/user/visit")
    public AjaxResult userVisit(BaseEntity dateVo) {
        List<LabelVo> dataList = statisticsService.userVisit(dateVo.getBeginTime());
        return AjaxResult.success(dataList);
    }

    /**
     * 用户走势
     */
    @RequiresPermissions("statistics:user:trend")
    @GetMapping("/user/trend")
    public AjaxResult userTrend(BaseEntity dateVo) {
        Date param = dateVo.getBeginTime();
        TimeUnitEnum timeUnit = dateVo.getTimeUnit();
        List<LabelVo> dataList = statisticsService.userTrend(param, timeUnit);
        return AjaxResult.success(dataList);
    }

    /**
     * 用户充值
     */
    @RequiresPermissions("statistics:user:recharge")
    @GetMapping("/user/recharge")
    public AjaxResult userRecharge(BaseEntity dateVo) {
        List<StatisticsVo00> dataList = statisticsService.userRecharge(dateVo.getBeginTime());
        return AjaxResult.success(dataList);
    }

    /**
     * 用户提现
     */
    @RequiresPermissions("statistics:user:cash")
    @GetMapping("/user/cash")
    public AjaxResult userCash(BaseEntity dateVo) {
        List<StatisticsVo02> dataList = statisticsService.userCash(dateVo.getBeginTime());
        return AjaxResult.success(dataList);
    }

    /**
     * 收支汇总
     */
    @RequiresPermissions("statistics:user:report")
    @GetMapping("/user/report")
    public AjaxResult userReport(BaseEntity dateVo) {
        List<StatisticsVo03> dataList = statisticsService.userReport(dateVo.getBeginTime());
        return AjaxResult.success(dataList);
    }

    /**
     * 统计余额
     */
    @RequiresPermissions("statistics:user:balance")
    @GetMapping("/user/balance")
    public AjaxResult userBalance() {
        List<StatisticsVo04> dataList = statisticsService.userBalance();
        return AjaxResult.success(dataList);
    }

    /**
     * 用户消费
     */
    @RequiresPermissions("statistics:user:shopping")
    @GetMapping("/user/shopping")
    public AjaxResult userShopping(BaseEntity dateVo) {
        List<StatisticsVo05> dataList = statisticsService.userShopping(dateVo.getBeginTime());
        return AjaxResult.success(dataList);
    }

}
