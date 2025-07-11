package com.platform.modules.wallet.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.statistics.vo.StatisticsVo01;
import com.platform.modules.wallet.domain.WalletRecharge;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 钱包充值 数据库访问层
 * </p>
 */
@Repository
public interface WalletRechargeDao extends BaseDao<WalletRecharge> {

    /**
     * 查询列表
     */
    List<WalletRecharge> queryList(WalletRecharge walletRecharge);

    /**
     * 统计
     */
    List<StatisticsVo01> statistics(Date beginTime, Date endTime);

}
