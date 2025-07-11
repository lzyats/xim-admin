package com.platform.modules.approve.service.impl;

import cn.hutool.core.lang.Dict;
import com.alibaba.excel.ExcelWriter;
import com.github.pagehelper.PageInfo;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.modules.approve.service.ApproveCashService;
import com.platform.modules.approve.vo.ApproveVo00;
import com.platform.modules.approve.vo.ApproveVo01;
import com.platform.modules.wallet.domain.WalletTrade;
import com.platform.modules.wallet.enums.TradeTypeEnum;
import com.platform.modules.wallet.service.WalletCashService;
import com.platform.modules.wallet.service.WalletTradeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 提现审批 业务层处理
 */
@Service("approveCashService")
public class ApproveCashServiceImpl implements ApproveCashService {

    @Resource
    private WalletTradeService walletTradeService;

    @Resource
    private WalletCashService walletCashService;

    @Override
    public PageInfo queryDataList(ApproveVo00 approveVo) {
        WalletTrade walletTrade = new WalletTrade()
                .setUserNo(approveVo.getUserNo())
                .setNickname(approveVo.getNickname())
                .setPhone(approveVo.getPhone())
                .setTradeType(TradeTypeEnum.CASH)
                .setTradeStatus(ApproveEnum.APPLY);
        return walletTradeService.queryApproveList(walletTrade);
    }

    @Override
    public Dict getInfo(Long tradeId) {
        return walletTradeService.queryApproveInfo(tradeId);
    }

    @Transactional
    @Override
    public void auth(ApproveVo01 approveVo) {
        Long tradeId = approveVo.getTradeId();
        ApproveEnum approveEnum = ApproveEnum.REJECT;
        String reason = approveVo.getReason();
        YesOrNoEnum auto = approveVo.getAuto();
        if (YesOrNoEnum.YES.equals(approveVo.getStatus())) {
            approveEnum = ApproveEnum.PASS;
            reason = null;
        }
        walletTradeService.approve(tradeId, approveEnum, auto, reason);
    }

    @Override
    public void export(ExcelWriter excelWriter) {
        walletCashService.export(excelWriter);
    }

}
