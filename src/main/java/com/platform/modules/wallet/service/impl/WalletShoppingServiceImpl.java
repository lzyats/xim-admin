package com.platform.modules.wallet.service.impl;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.statistics.vo.StatisticsVo05;
import com.platform.modules.wallet.dao.WalletShoppingDao;
import com.platform.modules.wallet.domain.WalletShopping;
import com.platform.modules.wallet.service.WalletShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 钱包消费 服务层实现
 * </p>
 */
@Service("walletShoppingService")
public class WalletShoppingServiceImpl extends BaseServiceImpl<WalletShopping> implements WalletShoppingService {

    @Resource
    private WalletShoppingDao walletShoppingDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(walletShoppingDao);
    }

    @Override
    public List<WalletShopping> queryList(WalletShopping t) {
        List<WalletShopping> dataList = walletShoppingDao.queryList(t);
        return dataList;
    }

}
