package com.platform.modules.wallet.service;

import com.alibaba.excel.ExcelWriter;
import com.platform.common.web.service.BaseService;
import com.platform.modules.pay.vo.PayVo;
import com.platform.modules.wallet.domain.WalletCash;

/**
 * <p>
 * 钱包提现 服务层
 * </p>
 */
public interface WalletCashService extends BaseService<WalletCash> {

    /**
     * 导出
     */
    void export(ExcelWriter excelWriter);

    /**
     * 查询
     */
    PayVo transfer(Long tradeId);

}
