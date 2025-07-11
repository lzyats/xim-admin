package com.platform.modules.wallet.service;

import cn.hutool.core.lang.Dict;
import com.github.pagehelper.PageInfo;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.service.BaseService;
import com.platform.modules.wallet.domain.WalletTrade;
import com.platform.modules.wallet.vo.WalletVo01;

/**
 * <p>
 * 钱包交易 服务层
 * </p>
 */
public interface WalletTradeService extends BaseService<WalletTrade> {

    /**
     * 收支明细
     */
    PageInfo income(Long userId);

    /**
     * 查询审批列表
     */
    PageInfo queryApproveList(WalletTrade walletTrade);

    /**
     * 查询审批详情
     */
    Dict queryApproveInfo(Long tradeId);

    /**
     * 审批
     */
    void approve(Long tradeId, ApproveEnum approveEnum, YesOrNoEnum auto, String reason);

    /**
     * 交易-列表
     */
    PageInfo queryTradeList(WalletVo01 walletVo);

    /**
     * 交易-详情
     */
    Object queryTradeInfo(Long tradeId);

}
