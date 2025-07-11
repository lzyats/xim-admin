package com.platform.modules.wallet.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.common.web.page.PageDomain;
import com.platform.common.web.page.TableSupport;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.wallet.dao.WalletPacketDao;
import com.platform.modules.wallet.domain.WalletPacket;
import com.platform.modules.wallet.service.WalletPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 钱包红包 服务层实现
 * </p>
 */
@Service("walletPacketService")
public class WalletPacketServiceImpl extends BaseServiceImpl<WalletPacket> implements WalletPacketService {

    @Resource
    private WalletPacketDao walletPacketDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(walletPacketDao);
    }

    @Override
    public List<WalletPacket> queryList(WalletPacket t) {
        List<WalletPacket> dataList = walletPacketDao.queryList(t);
        return dataList;
    }

    @Override
    public PageInfo queryDataList(Long tradeId) {
        // 执行分页
        PageDomain pageDomain = TableSupport.getPageDomain();
        PageHelper.startPage(pageDomain.getPageNum(), pageDomain.getPageSize(), StrUtil.toUnderlineCase("packetId asc"));
        // 查询
        QueryWrapper<WalletPacket> wrapper = new QueryWrapper<>();
        wrapper.eq(WalletPacket.COLUMN_TRADE_ID, tradeId);
        List<WalletPacket> dataList = this.queryList(wrapper);
        List<Dict> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            Dict dict = Dict.create().parseBean(y)
                    .filter(WalletPacket.LABEL_USER_NO
                            , WalletPacket.LABEL_NICKNAME
                            , WalletPacket.LABEL_AMOUNT
                            , WalletPacket.LABEL_CREATE_TIME);
            x.add(dict);
        }, ArrayList::addAll);
        return getPageInfo(dictList, dataList);
    }
}
