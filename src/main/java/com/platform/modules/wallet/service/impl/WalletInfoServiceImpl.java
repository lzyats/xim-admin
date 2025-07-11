package com.platform.modules.wallet.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.utils.CodeUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.wallet.dao.WalletInfoDao;
import com.platform.modules.wallet.domain.WalletInfo;
import com.platform.modules.wallet.domain.WalletRecharge;
import com.platform.modules.wallet.domain.WalletTrade;
import com.platform.modules.wallet.enums.TradePayEnum;
import com.platform.modules.wallet.enums.TradeTypeEnum;
import com.platform.modules.wallet.service.WalletInfoService;
import com.platform.modules.wallet.service.WalletRechargeService;
import com.platform.modules.wallet.service.WalletTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户钱包 服务层实现
 * </p>
 */
@Service("walletInfoService")
public class WalletInfoServiceImpl extends BaseServiceImpl<WalletInfo> implements WalletInfoService {

    @Resource
    private WalletInfoDao walletInfoDao;

    @Resource
    private WalletTradeService walletTradeService;

    @Resource
    private WalletRechargeService walletRechargeService;

    @Resource
    private ChatUserService chatUserService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(walletInfoDao);
    }

    @Override
    public List<WalletInfo> queryList(WalletInfo t) {
        List<WalletInfo> dataList = walletInfoDao.queryList(t);
        return dataList;
    }

    @Override
    public void addWallet(Long userId) {
        String salt = CodeUtils.salt();
        String pass = RandomUtil.randomNumbers(6);
        String password = CodeUtils.md5(pass);
        WalletInfo wallet = new WalletInfo()
                .setUserId(userId)
                .setBalance(BigDecimal.ZERO)
                .setSalt(salt)
                .setPassword(CodeUtils.credentials(password, salt));
        this.add(wallet);
    }

    @Transactional
    @Override
    public void recharge(Long userId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) == 0) {
            return;
        }
        // 查询用户
        ChatUser chatUser = chatUserService.findById(userId, "用户不存在");
        // 1、变更余额
        BigDecimal balance = this.addBalance(userId, amount);
        // 2、写入账单
        TradeTypeEnum tradeType = TradeTypeEnum.RECHARGE;
        Date now = DateUtil.date();
        WalletTrade trade = new WalletTrade()
                .setTradeType(tradeType)
                .setTradeAmount(amount)
                .setTradeBalance(balance)
                .setTradeStatus(ApproveEnum.PASS)
                .setTradeRemark(tradeType.getInfo())
                .setUserId(chatUser.getUserId())
                .setUserNo(chatUser.getUserNo())
                .setNickname(chatUser.getNickname())
                .setPhone(chatUser.getPhone())
                .setPortrait(chatUser.getPortrait())
                .setCreateTime(now)
                .setUpdateTime(now);
        walletTradeService.add(trade);
        // 3、删除账单
        if (BigDecimal.ZERO.compareTo(amount) == 1) {
            walletTradeService.update(Wrappers.<WalletTrade>lambdaUpdate()
                    .set(WalletTrade::getDeleted, null)
                    .eq(WalletTrade::getTradeId, trade.getTradeId()));
        }
        // 4、写入充值
        WalletRecharge recharge = new WalletRecharge()
                .setTradeId(trade.getTradeId())
                .setUserId(userId)
                .setUserNo(chatUser.getUserNo())
                .setPhone(chatUser.getPhone())
                .setNickname(chatUser.getNickname())
                .setPayType(TradePayEnum.SYS_PAY)
                .setAmount(amount)
                .setTradeNo(RandomUtil.randomString(32))
                .setOrderNo(RandomUtil.randomNumbers(32))
                .setCreateTime(now)
                .setUpdateTime(now);
        walletRechargeService.add(recharge);
    }

    @Override
    public BigDecimal addBalance(Long userId, BigDecimal amount) {
        // 查询数据库
        WalletInfo entity = this.getById(userId);
        BigDecimal balance = NumberUtil.add(entity.getBalance(), amount);
        // 更新余额
        WalletInfo wallet = new WalletInfo()
                .setUserId(userId)
                .setBalance(balance)
                .setVersion(entity.getVersion());
        if (this.updateById(wallet) == 0) {
            throw new BaseException("余额增加失败，请稍后再试");
        }
        return balance;
    }

    @Override
    public BigDecimal getBalance(Long userId) {
        // 查询数据库
        WalletInfo walletInfo = this.getById(userId);
        if (walletInfo == null) {
            return BigDecimal.ZERO;

        }
        return walletInfo.getBalance();
    }

}
