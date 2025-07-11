package com.platform.modules.work.controller;

import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.statistics.service.StatisticsService;
import com.platform.modules.statistics.vo.StatisticsVo03;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 统计服务
 */
@RestController
@RequestMapping("/work/income")
@Slf4j
public class WorkIncomeController extends BaseController {

    @Resource
    private StatisticsService statisticsService;

    /**
     * 收支统计
     */
    @GetMapping("/list")
    public AjaxResult list(BaseEntity dateVo) {
        List<StatisticsVo03> dataList = statisticsService.userReport(dateVo.getBeginTime());
        return AjaxResult.success(dataList);
    }

}
