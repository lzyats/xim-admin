package com.platform.modules.wallet.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.wallet.domain.WalletInfo;

import java.math.BigDecimal;

/**
 * <p>
 * 用户钱包 服务层
 * </p>
 */
public interface WalletInfoService extends BaseService<WalletInfo> {

    /**
     * 新增钱包
     */
    void addWallet(Long userId);

    /**
     * 充值
     */
    void recharge(Long userId, BigDecimal amount);

    /**
     * 增加余额
     */
    BigDecimal addBalance(Long userId, BigDecimal amount);

    /**
     * 查询余额
     */
    BigDecimal getBalance(Long userId);

}
