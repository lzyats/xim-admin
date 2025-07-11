package com.platform.modules.wallet.service;

import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.BaseService;
import com.platform.modules.wallet.domain.WalletPacket;

/**
 * <p>
 * 钱包红包 服务层
 * </p>
 */
public interface WalletPacketService extends BaseService<WalletPacket> {

    /**
     * 查询领取列表
     */
    PageInfo queryDataList(Long tradeId);

}
