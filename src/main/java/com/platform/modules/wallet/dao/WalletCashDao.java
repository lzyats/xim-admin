package com.platform.modules.wallet.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.statistics.vo.StatisticsVo02;
import com.platform.modules.wallet.domain.WalletCash;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 钱包提现 数据库访问层
 * </p>
 */
@Repository
public interface WalletCashDao extends BaseDao<WalletCash> {

    /**
     * 查询列表
     */
    List<WalletCash> queryList(WalletCash walletCash);

}
