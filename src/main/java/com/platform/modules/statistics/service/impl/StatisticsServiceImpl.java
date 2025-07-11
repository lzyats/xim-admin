package com.platform.modules.statistics.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.common.enums.TimeUnitEnum;
import com.platform.common.web.vo.LabelVo;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.domain.ChatVisit;
import com.platform.modules.chat.service.ChatUserLogService;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.chat.service.ChatVisitService;
import com.platform.modules.statistics.service.StatisticsService;
import com.platform.modules.statistics.vo.*;
import com.platform.modules.wallet.domain.WalletCash;
import com.platform.modules.wallet.domain.WalletRecharge;
import com.platform.modules.wallet.domain.WalletShopping;
import com.platform.modules.wallet.enums.TradePayEnum;
import com.platform.modules.wallet.service.WalletCashService;
import com.platform.modules.wallet.service.WalletRechargeService;
import com.platform.modules.wallet.service.WalletShoppingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service("statisticsService")
public class StatisticsServiceImpl implements StatisticsService {

    @Resource
    private ChatUserService chatUserService;

    @Resource
    private ChatUserLogService chatUserLogService;

    @Resource
    private ChatVisitService chatVisitService;

    @Resource
    private WalletRechargeService walletRechargeService;

    @Resource
    private WalletCashService walletCashService;

    @Resource
    private WalletShoppingService walletShoppingService;

    @Override
    public List<LabelVo> userVisit() {
        Date now = DateUtil.date();
        Date beginTime = DateUtil.beginOfDay(DateUtil.offsetDay(now, -6));
        Date endTime = DateUtil.beginOfDay(now);
        return userVisit(beginTime, endTime);
    }

    @Override
    public List<LabelVo> userVisit(Date param) {
        if (param == null) {
            param = DateUtil.date();
        }
        Date beginTime = DateUtil.beginOfMonth(param);
        Date endTime = DateUtil.endOfMonth(param);
        return userVisit(beginTime, endTime);
    }

    /**
     * 日活计算
     */
    private List<LabelVo> userVisit(Date beginTime, Date endTime) {
        // 当前时间
        Integer now = DateUtil.dayOfMonth(DateUtil.date());
        // 查询
        QueryWrapper<ChatVisit> wrapper = new QueryWrapper<>();
        wrapper.between(ChatVisit.COLUMN_VISIT_DATE, beginTime, endTime);
        List<ChatVisit> dataList = chatVisitService.queryList(wrapper);
        HashMap<Integer, Long> dataMap = dataList.stream().collect(HashMap::new, (x, y) -> {
            x.put(DateUtil.dayOfMonth(y.getVisitDate()), y.getVisitCount());
        }, HashMap::putAll);
        List<LabelVo> dictList = new ArrayList<>();
        // 间隔
        long between = DateUtil.between(beginTime, endTime, DateUnit.DAY) + 1;
        for (int i = 0; i < between; i++) {
            Date dateTime = DateUtil.offset(beginTime, DateField.DAY_OF_MONTH, i);
            Integer day = DateUtil.dayOfMonth(dateTime);
            Long count = dataMap.get(day);
            if (count == null) {
                count = 0L;
            }
            if (day.equals(now)) {
                count = chatUserLogService.visit();
            }
            dictList.add(new LabelVo(DateUtil.format(dateTime, "MM-dd"), count));
        }
        return dictList;
    }

    @Override
    public List<LabelVo> userTrend() {
        // 当前时间
        Date param = DateUtil.date();
        // 开始时间
        Date beginTime = DateUtil.beginOfDay(DateUtil.offsetDay(param, -6));
        // 结束时间
        Date endTime = DateUtil.beginOfDay(param);
        // 查询
        return userTrendMonth(beginTime, endTime);
    }

    @Override
    public List<LabelVo> userTrend(Date param, TimeUnitEnum timeUnit) {
        if (param == null) {
            param = DateUtil.date();
        }
        // 按年
        if (TimeUnitEnum.YEAR.equals(timeUnit)) {
            Date beginTime = DateUtil.beginOfYear(param);
            Date endTime = DateUtil.endOfYear(param);
            return userTrendYear(beginTime, endTime);
        }
        // 按月
        Date beginTime = DateUtil.beginOfMonth(param);
        Date endTime = DateUtil.endOfMonth(param);
        return userTrendMonth(beginTime, endTime);
    }

    @Override
    public List<StatisticsVo00> userRecharge(Date param) {
        if (param == null) {
            param = DateUtil.date();
        }
        Date beginTime = DateUtil.beginOfMonth(param);
        Date endTime = DateUtil.endOfMonth(param);
        int now = DateUtil.thisDayOfMonth() - 1;
        // 查询
        List<StatisticsVo01> resultList = walletRechargeService.statistics(beginTime, endTime);
        // 转换
        HashMap<Date, StatisticsVo01> resultMap = resultList.stream().collect(HashMap::new, (x, y) -> {
            x.put(y.getCreateTime(), y);
        }, HashMap::putAll);
        // 处理
        List<StatisticsVo00> dataList = new ArrayList<>();
        // 间隔
        long between = DateUtil.between(beginTime, endTime, DateUnit.DAY) + 1;
        // 汇总
        StatisticsVo01 total = new StatisticsVo01();
        // 今日
        StatisticsVo01 today = new StatisticsVo01();
        for (int i = 0; i < between; i++) {
            Date dateTime = DateUtil.offset(beginTime, DateField.DAY_OF_MONTH, i);
            StatisticsVo01 dataVo = resultMap.get(dateTime);
            if (dataVo == null) {
                dataVo = new StatisticsVo01(dateTime);
            }
            dataList.add(new StatisticsVo00(dataVo, TradePayEnum.SYS_PAY));
            dataList.add(new StatisticsVo00(dataVo, TradePayEnum.ALI_PAY));
            dataList.add(new StatisticsVo00(dataVo, TradePayEnum.WX_PAY));
            // 当日
            if (now == i) {
                today = dataVo;
            }
            // 汇总
            total.setPlatformCount(total.getPlatformCount() + dataVo.getPlatformCount());
            total.setPlatformAmount(total.getPlatformAmount().add(dataVo.getPlatformAmount()));
            total.setAliCount(total.getAliCount() + dataVo.getAliCount());
            total.setAliAmount(total.getAliAmount().add(dataVo.getAliAmount()));
            total.setWxCount(total.getWxCount() + dataVo.getWxCount());
            total.setWxAmount(total.getWxAmount().add(dataVo.getWxAmount()));
            total.setTotalCount(total.getTotalCount() + dataVo.getTotalCount());
            total.setTotalAmount(total.getTotalAmount().add(dataVo.getTotalAmount()));
        }
        // 当月汇总
        dataList.add(new StatisticsVo00(total, TradePayEnum.SYS_PAY));
        dataList.add(new StatisticsVo00(total, TradePayEnum.ALI_PAY));
        dataList.add(new StatisticsVo00(total, TradePayEnum.WX_PAY));
        dataList.add(0, new StatisticsVo00(total, TradePayEnum.SYS_PAY));
        dataList.add(1, new StatisticsVo00(total, TradePayEnum.ALI_PAY));
        dataList.add(2, new StatisticsVo00(total, TradePayEnum.WX_PAY));
        dataList.add(0, new StatisticsVo00(today, TradePayEnum.SYS_PAY).setTotalLabel("今日汇总"));
        dataList.add(1, new StatisticsVo00(today, TradePayEnum.ALI_PAY).setTotalLabel("今日汇总"));
        dataList.add(2, new StatisticsVo00(today, TradePayEnum.WX_PAY).setTotalLabel("今日汇总"));
        return dataList;
    }

    @Override
    public List<StatisticsVo02> userCash(Date param) {
        if (param == null) {
            param = DateUtil.date();
        }
        Date beginTime = DateUtil.beginOfMonth(param);
        Date endTime = DateUtil.endOfMonth(param);
        int now = DateUtil.thisDayOfMonth() - 1;
        QueryWrapper<WalletCash> wrapper1 = new QueryWrapper<>();
        wrapper1.select("DATE(update_time) AS create_time, count(1) AS 'count', SUM(amount) AS 'amount'");
        wrapper1.isNull(WalletCash.COLUMN_REASON);
        wrapper1.between("update_time", beginTime, endTime);
        wrapper1.groupBy("DATE(update_time)");
        // 通过
        HashMap<Date, WalletCash> dataMap1 = walletCashService.queryList(wrapper1).stream().collect(HashMap::new, (x, y) -> {
            x.put(y.getCreateTime(), y);
        }, HashMap::putAll);
        // 拒绝
        QueryWrapper<WalletCash> wrapper2 = new QueryWrapper<>();
        wrapper2.select("DATE(update_time) AS create_time, count(1) AS 'count', SUM(amount) AS 'amount'");
        wrapper2.between("update_time", beginTime, endTime);
        wrapper2.isNotNull(WalletCash.COLUMN_REASON);
        wrapper2.groupBy("DATE(update_time)");
        HashMap<Date, WalletCash> dataMap2 = walletCashService.queryList(wrapper2).stream().collect(HashMap::new, (x, y) -> {
            x.put(y.getCreateTime(), y);
        }, HashMap::putAll);
        // 间隔
        long between = DateUtil.between(beginTime, endTime, DateUnit.DAY) + 1;
        // 汇总
        List<StatisticsVo02> dataList = new ArrayList<>();
        StatisticsVo02 total = new StatisticsVo02();
        // 今日
        StatisticsVo02 today = new StatisticsVo02();
        for (int i = 0; i < between; i++) {
            Date dateTime = DateUtil.offset(beginTime, DateField.DAY_OF_MONTH, i);
            // 通过
            WalletCash pass = dataMap1.get(dateTime);
            Long passCount = 0L;
            BigDecimal passAmount = BigDecimal.ZERO;
            if (pass != null) {
                passCount = pass.getCount();
                passAmount = pass.getAmount();
            }
            // 拒绝
            WalletCash reject = dataMap2.get(dateTime);
            Long rejectCount = 0L;
            BigDecimal rejectAmount = BigDecimal.ZERO;
            if (reject != null) {
                rejectCount = reject.getCount();
                rejectAmount = reject.getAmount();
            }
            dataList.add(new StatisticsVo02(dateTime, passCount, passAmount, rejectCount, rejectAmount));
            // 当日
            if (now == i) {
                today = new StatisticsVo02(dateTime, passCount, passAmount, rejectCount, rejectAmount).setLabel("今日汇总");
            }
            // 汇总
            total.setPassCount(total.getPassCount() + passCount);
            total.setPassAmount(total.getPassAmount().add(passAmount));
            total.setRejectCount(total.getRejectCount() + rejectCount);
            total.setRejectAmount(total.getRejectAmount().add(rejectAmount));
        }
        dataList.add(total);
        dataList.add(0, total);
        dataList.add(0, today);
        return dataList;
    }

    @Override
    public List<StatisticsVo05> userShopping(Date param) {
        if (param == null) {
            param = DateUtil.date();
        }
        Date beginTime = DateUtil.beginOfMonth(param);
        Date endTime = DateUtil.endOfMonth(param);
        int now = DateUtil.thisDayOfMonth() - 1;
        // 查询
        QueryWrapper<WalletShopping> wrapper = new QueryWrapper<>();
        wrapper.select("DATE(create_time) AS create_time, SUM(abs(amount)) AS 'amount', COUNT(DISTINCT trade_id) AS 'count'");
        wrapper.groupBy("DATE(create_time)");
        wrapper.between("create_time", beginTime, endTime);
        // 分组
        HashMap<Date, WalletShopping> dataMap = walletShoppingService.queryList(wrapper).stream().collect(HashMap::new, (x, y) -> {
            x.put(y.getCreateTime(), y);
        }, HashMap::putAll);
        // 间隔
        long between = DateUtil.between(beginTime, endTime, DateUnit.DAY) + 1;
        // 汇总
        List<StatisticsVo05> dataList = new ArrayList<>();
        StatisticsVo05 total = new StatisticsVo05();
        // 今日
        StatisticsVo05 today = new StatisticsVo05();
        for (int i = 0; i < between; i++) {
            Date dateTime = DateUtil.offset(beginTime, DateField.DAY_OF_MONTH, i);
            WalletShopping walletShopping = dataMap.get(dateTime);
            Long count = 0L;
            BigDecimal amount = BigDecimal.ZERO;
            if (walletShopping != null) {
                count = walletShopping.getCount();
                amount = walletShopping.getAmount();
            }
            dataList.add(new StatisticsVo05(dateTime, count, amount));
            // 当日
            if (now == i) {
                today = new StatisticsVo05(dateTime, count, amount).setLabel("今日汇总");
            }
            // 汇总
            total.setCount(total.getCount() + count);
            total.setAmount(total.getAmount().add(amount));
        }
        dataList.add(total);
        dataList.add(0, total);
        dataList.add(0, today);
        return dataList;
    }

    @Override
    public List<StatisticsVo03> userReport(Date param) {
        if (param == null) {
            param = DateUtil.date();
        }
        Date beginTime = DateUtil.beginOfMonth(param);
        Date endTime = DateUtil.endOfMonth(param);
        int now = DateUtil.thisDayOfMonth() - 1;
        // 充值
        QueryWrapper<WalletRecharge> wrapper1 = new QueryWrapper<>();
        wrapper1.select("DATE(create_time) AS create_time, SUM(amount) AS 'amount'");
        wrapper1.between("create_time", beginTime, endTime);
        wrapper1.groupBy("DATE(create_time)");
        HashMap<Date, BigDecimal> dataMap1 = walletRechargeService.queryList(wrapper1).stream().collect(HashMap::new, (x, y) -> {
            x.put(y.getCreateTime(), y.getAmount());
        }, HashMap::putAll);
        // 提现
        QueryWrapper<WalletCash> wrapper2 = new QueryWrapper<>();
        wrapper2.select("DATE(update_time) AS create_time, SUM(amount) AS 'amount'");
        wrapper2.isNull(WalletCash.COLUMN_REASON);
        wrapper2.groupBy("DATE(update_time)");
        wrapper2.between("update_time", beginTime, endTime);
        HashMap<Date, BigDecimal> dataMap2 = walletCashService.queryList(wrapper2).stream().collect(HashMap::new, (x, y) -> {
            x.put(y.getCreateTime(), NumberUtil.sub(BigDecimal.ZERO, y.getAmount()));
        }, HashMap::putAll);
        // 消费
        QueryWrapper<WalletShopping> wrapper3 = new QueryWrapper<>();
        wrapper3.select("DATE(create_time) AS create_time, SUM(abs(amount)) AS 'amount'");
        wrapper3.groupBy("DATE(create_time)");
        wrapper3.between("create_time", beginTime, endTime);
        HashMap<Date, BigDecimal> dataMap3 = walletShoppingService.queryList(wrapper3).stream().collect(HashMap::new, (x, y) -> {
            x.put(y.getCreateTime(), y.getAmount());
        }, HashMap::putAll);
        // 服务费
        QueryWrapper<WalletCash> wrapper4 = new QueryWrapper<>();
        wrapper4.select("DATE(update_time) AS create_time, SUM(charge) AS 'charge'");
        wrapper4.groupBy("DATE(update_time)");
        wrapper4.between("update_time", beginTime, endTime);
        HashMap<Date, BigDecimal> dataMap4 = walletCashService.queryList(wrapper4).stream().collect(HashMap::new, (x, y) -> {
            x.put(y.getCreateTime(), y.getCharge());
        }, HashMap::putAll);
        // 间隔
        long between = DateUtil.between(beginTime, endTime, DateUnit.DAY) + 1;
        // 汇总
        List<StatisticsVo03> dataList = new ArrayList<>();
        StatisticsVo03 total = new StatisticsVo03();
        // 今日
        StatisticsVo03 today = new StatisticsVo03();
        for (int i = 0; i < between; i++) {
            Date dateTime = DateUtil.offset(beginTime, DateField.DAY_OF_MONTH, i);
            BigDecimal income = dataMap1.get(dateTime);
            if (income == null) {
                income = BigDecimal.ZERO;
            }
            BigDecimal disburse = dataMap2.get(dateTime);
            if (disburse == null) {
                disburse = BigDecimal.ZERO;
            }
            BigDecimal consume = dataMap3.get(dateTime);
            if (consume == null) {
                consume = BigDecimal.ZERO;
            }
            BigDecimal charge = dataMap4.get(dateTime);
            if (charge == null) {
                charge = BigDecimal.ZERO;
            }
            dataList.add(new StatisticsVo03(dateTime, income, disburse, consume, charge));
            // 当日
            if (now == i) {
                today = new StatisticsVo03(dateTime, income, disburse, consume, charge).setLabel("今日汇总");
            }
            // 汇总
            total.setIncome(total.getIncome().add(income));
            total.setDisburse(total.getDisburse().add(disburse));
            total.setConsume(total.getConsume().add(consume));
            total.setCharge(total.getCharge().add(charge));
            total.setTotal(total.getTotal().add(consume).add(charge));
        }
        dataList.add(total);
        dataList.add(0, total);
        dataList.add(0, today);
        return dataList;
    }

    @Override
    public List<StatisticsVo04> userBalance() {
        // 充值
        QueryWrapper<WalletRecharge> wrapper1 = new QueryWrapper<>();
        wrapper1.select("SUM(amount) AS amount");
        WalletRecharge walletRecharge = walletRechargeService.queryOne(wrapper1);
        // 提现
        QueryWrapper<WalletCash> wrapper2 = new QueryWrapper<>();
        wrapper2.select("SUM(-amount) AS amount");
        wrapper2.isNull(WalletCash.COLUMN_REASON);
        wrapper2.isNotNull(WalletCash.COLUMN_UPDATE_TIME);
        WalletCash walletCash = walletCashService.queryOne(wrapper2);
        // 消费
        QueryWrapper<WalletShopping> wrapper3 = new QueryWrapper<>();
        wrapper3.select("SUM(-amount) AS amount");
        WalletShopping walletShopping = walletShoppingService.queryOne(wrapper3);
        // 充值
        BigDecimal recharge = BigDecimal.ZERO;
        if (walletRecharge != null) {
            recharge = walletRecharge.getAmount();
        }
        // 提现
        BigDecimal cash = BigDecimal.ZERO;
        if (walletCash != null) {
            cash = walletCash.getAmount();
        }
        // 消费
        BigDecimal consume = BigDecimal.ZERO;
        if (walletShopping != null) {
            consume = walletShopping.getAmount();
        }
        // 汇总
        BigDecimal balance = NumberUtil.add(recharge, cash, consume);
        List<StatisticsVo04> dataList = new ArrayList<>();
        dataList.add(new StatisticsVo04(TradePayEnum.SYS_PAY, walletRechargeService.queryAmount(TradePayEnum.SYS_PAY), recharge));
        dataList.add(new StatisticsVo04(TradePayEnum.ALI_PAY, walletRechargeService.queryAmount(TradePayEnum.ALI_PAY), recharge));
        dataList.add(new StatisticsVo04(TradePayEnum.WX_PAY, walletRechargeService.queryAmount(TradePayEnum.WX_PAY), recharge));
        dataList.add(new StatisticsVo04("提现", cash));
        dataList.add(new StatisticsVo04("消费", consume));
        dataList.add(new StatisticsVo04("汇总", balance));
        return dataList;
    }

    /**
     * 按年查询
     */
    private List<LabelVo> userTrendYear(Date beginTime, Date endTime) {
        // 查询
        QueryWrapper<ChatUser> wrapper = new QueryWrapper<>();
        wrapper.select("DATE(CONCAT(YEAR(create_time), '-', MONTH(create_time), '-01')) AS create_time, COUNT(DISTINCT user_id) AS 'count'");
        wrapper.between("create_time", beginTime, endTime);
        wrapper.groupBy("MONTH(create_time)");
        HashMap<Date, Long> dataMap = chatUserService.queryList(wrapper).stream().collect(HashMap::new, (x, y) -> {
            x.put(y.getCreateTime(), y.getCount());
        }, HashMap::putAll);
        List<LabelVo> labelList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Date dateTime = DateUtil.offset(beginTime, DateField.MONTH, i);
            Long count = dataMap.get(dateTime);
            if (count == null) {
                count = 0L;
            }
            labelList.add(new LabelVo(DateUtil.format(dateTime, DatePattern.NORM_MONTH_PATTERN), count));
        }
        return labelList;
    }

    /**
     * 按月查询
     */
    private List<LabelVo> userTrendMonth(Date beginTime, Date endTime) {
        // 查询
        QueryWrapper<ChatUser> wrapper = new QueryWrapper<>();
        wrapper.select("DATE(create_time) AS create_time, COUNT(DISTINCT user_id) AS 'count'");
        wrapper.between("create_time", beginTime, endTime);
        wrapper.groupBy("DATE(create_time)");
        List<ChatUser> dataList = chatUserService.queryList(wrapper);
        // 转换
        HashMap<Date, Long> dataMap = dataList.stream().collect(HashMap::new, (x, y) -> {
            x.put(y.getCreateTime(), y.getCount());
        }, HashMap::putAll);
        List<LabelVo> dictList = new ArrayList<>();
        // 查询当月有几天
        long between = DateUtil.between(beginTime, endTime, DateUnit.DAY) + 1;
        for (int i = 0; i < between; i++) {
            Date dateTime = DateUtil.offset(beginTime, DateField.DAY_OF_MONTH, i);
            Long count = dataMap.get(dateTime);
            if (count == null) {
                count = 0L;
            }
            dictList.add(new LabelVo(DateUtil.format(dateTime, "MM-dd"), count));
        }
        return dictList;
    }

}
