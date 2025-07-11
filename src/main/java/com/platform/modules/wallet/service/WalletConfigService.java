package com.platform.modules.wallet.service;

import com.platform.modules.operate.vo.OperateVo07;

import java.util.List;

/**
 * <p>
 * 钱包容量 服务层
 * </p>
 */
public interface WalletConfigService {

    /**
     * 查询列表
     */
    List<OperateVo07> queryList();

    /**
     * 修改数据
     */
    void update(OperateVo07 chatVo);

}
