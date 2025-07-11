package com.platform.modules.wallet.service;

import cn.hutool.core.lang.Dict;
import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.BaseService;
import com.platform.modules.statistics.vo.StatisticsVo01;
import com.platform.modules.wallet.domain.WalletRecharge;
import com.platform.modules.wallet.enums.TradePayEnum;
import com.platform.modules.wallet.vo.WalletVo01;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 钱包充值 服务层
 * </p>
 */
public interface WalletRechargeService extends BaseService<WalletRecharge> {

    /**
     * 充值查询
     */
    PageInfo queryTradeList(WalletVo01 walletVo);

    /**
     * 充值查询
     */
    Dict queryTradeInfo(Long tradeId);

    /**
     * 统计
     */
    List<StatisticsVo01> statistics(Date beginTime, Date endTime);

    /**
     * 查询充值金额
     */
    BigDecimal queryAmount(TradePayEnum payType);

}
