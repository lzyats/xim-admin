package com.platform.modules.wallet.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.pay.vo.PayVo;
import com.platform.modules.wallet.dao.WalletCashDao;
import com.platform.modules.wallet.domain.WalletCash;
import com.platform.modules.wallet.service.WalletCashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 钱包提现 服务层实现
 * </p>
 */
@Service("walletCashService")
public class WalletCashServiceImpl extends BaseServiceImpl<WalletCash> implements WalletCashService {

    @Resource
    private WalletCashDao walletCashDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(walletCashDao);
    }

    @Override
    public List<WalletCash> queryList(WalletCash t) {
        List<WalletCash> dataList = walletCashDao.queryList(t);
        return dataList;
    }

    @Override
    public void export(ExcelWriter excelWriter) {
        // 查询
        QueryWrapper<WalletCash> wrapper = new QueryWrapper<>();
        wrapper.isNull("update_time");
        List<WalletCash> cashList = this.queryList(wrapper);
        // 结果
        List<Map<String, Object>> dataList = new ArrayList<>();
        // 集合判空
        if (!CollectionUtils.isEmpty(cashList)) {
            for (int i = 0; i < cashList.size(); i++) {
                WalletCash walletCash = cashList.get(i);
                Map<String, Object> map = new HashMap<>();
                map.put("index", i + 1);
                map.put("wallet", StrUtil.removeAll(walletCash.getWallet(), " "));
                map.put("name", walletCash.getName());
                map.put("balance", calculation(walletCash));
                dataList.add(map);
            }
        }
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        excelWriter.fill(dataList, writeSheet);
    }

    @Override
    public PayVo transfer(Long tradeId) {
        // 查询
        WalletCash walletCash = this.findById(tradeId);
        return new PayVo()
                .setTradeId(tradeId)
                .setName(walletCash.getName())
                .setWallet(walletCash.getWallet())
                .setAmount(calculation(walletCash));
    }

    /**
     * 计算剩余
     */
    private BigDecimal calculation(WalletCash walletCash) {
        BigDecimal balance = NumberUtil.sub(walletCash.getAmount(), walletCash.getCharge());
        return balance.setScale(2, BigDecimal.ROUND_DOWN).abs();
    }

}
