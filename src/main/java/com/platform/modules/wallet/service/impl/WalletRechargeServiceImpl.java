package com.platform.modules.wallet.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.statistics.vo.StatisticsVo01;
import com.platform.modules.wallet.dao.WalletRechargeDao;
import com.platform.modules.wallet.domain.WalletRecharge;
import com.platform.modules.wallet.enums.TradePayEnum;
import com.platform.modules.wallet.service.WalletRechargeService;
import com.platform.modules.wallet.vo.WalletVo01;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 钱包充值 服务层实现
 * </p>
 */
@Service("walletRechargeService")
public class WalletRechargeServiceImpl extends BaseServiceImpl<WalletRecharge> implements WalletRechargeService {

    @Resource
    private WalletRechargeDao walletRechargeDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(walletRechargeDao);
    }

    @Override
    public List<WalletRecharge> queryList(WalletRecharge t) {
        List<WalletRecharge> dataList = walletRechargeDao.queryList(t);
        return dataList;
    }

    @Override
    public PageInfo queryTradeList(WalletVo01 walletVo) {
        String tradeNo = walletVo.getTradeNo();
        String orderNo = walletVo.getOrderNo();
        TradePayEnum payType = walletVo.getPayType();
        String userNo = walletVo.getUserNo();
        String phone = walletVo.getPhone();
        QueryWrapper<WalletRecharge> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(tradeNo)) {
            wrapper.eq(WalletRecharge.COLUMN_TRADE_NO, tradeNo);
        }
        if (!StringUtils.isEmpty(orderNo)) {
            wrapper.eq(WalletRecharge.COLUMN_ORDER_NO, orderNo);
        }
        if (payType != null) {
            wrapper.eq(WalletRecharge.COLUMN_PAY_TYPE, payType);
        }
        if (!StringUtils.isEmpty(userNo)) {
            wrapper.eq(WalletRecharge.COLUMN_USER_NO, userNo);
        }
        if (!StringUtils.isEmpty(phone)) {
            wrapper.eq(WalletRecharge.COLUMN_PHONE, phone);
        }
        List<WalletRecharge> dataList = this.queryList(wrapper);
        List<Dict> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            x.add(format(y));
        }, ArrayList::addAll);
        return getPageInfo(dictList, dataList);
    }

    @Override
    public Dict queryTradeInfo(Long tradeId) {
        // 查询
        WalletRecharge recharge = this.findById(tradeId);
        // 格式化
        return format(recharge);
    }

    @Override
    public List<StatisticsVo01> statistics(Date beginTime, Date endTime) {
        return walletRechargeDao.statistics(beginTime, endTime);
    }

    @Override
    public BigDecimal queryAmount(TradePayEnum payType) {
        QueryWrapper<WalletRecharge> wrapper = new QueryWrapper<>();
        wrapper.select("SUM(amount) AS amount");
        wrapper.eq(WalletRecharge.COLUMN_PAY_TYPE, payType);
        WalletRecharge walletRecharge = this.queryOne(wrapper);
        BigDecimal amount = BigDecimal.ZERO;
        if (walletRecharge != null) {
            amount = walletRecharge.getAmount();
        }
        return amount;
    }

    /**
     * 格式化
     */
    private Dict format(WalletRecharge recharge) {
        return Dict.create().parseBean(recharge)
                .filter(WalletRecharge.LABEL_TRADE_ID
                        , WalletRecharge.LABEL_USER_ID
                        , WalletRecharge.LABEL_USER_NO
                        , WalletRecharge.LABEL_PHONE
                        , WalletRecharge.LABEL_NICKNAME
                        , WalletRecharge.LABEL_PAY_TYPE
                        , WalletRecharge.LABEL_AMOUNT
                        , WalletRecharge.LABEL_TRADE_NO
                        , WalletRecharge.LABEL_ORDER_NO
                        , WalletRecharge.LABEL_CREATE_TIME
                        , WalletRecharge.LABEL_UPDATE_TIME
                )
                .set(WalletRecharge.LABEL_PAY_TYPE_LABEL, recharge.getPayType().getInfo());
    }

}
