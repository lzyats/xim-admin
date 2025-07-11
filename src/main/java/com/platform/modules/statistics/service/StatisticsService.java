package com.platform.modules.statistics.service;

import com.platform.common.enums.TimeUnitEnum;
import com.platform.common.web.vo.LabelVo;
import com.platform.modules.statistics.vo.*;

import java.util.Date;
import java.util.List;

/**
 * 统计服务
 */
public interface StatisticsService {

    /**
     * 用户日活-首页
     */
    List<LabelVo> userVisit();

    /**
     * 用户日活
     */
    List<LabelVo> userVisit(Date param);

    /**
     * 用户走势-首页
     */
    List<LabelVo> userTrend();

    /**
     * 用户走势
     */
    List<LabelVo> userTrend(Date param, TimeUnitEnum timeUnit);

    /**
     * 用户充值
     */
    List<StatisticsVo00> userRecharge(Date param);

    /**
     * 用户提现
     */
    List<StatisticsVo02> userCash(Date param);

    /**
     * 收支统计
     */
    List<StatisticsVo03> userReport(Date param);

    /**
     * 统计余额
     */
    List<StatisticsVo04> userBalance();

    /**
     * 用户消费
     */
    List<StatisticsVo05> userShopping(Date param);

}
