package com.platform.modules.wallet.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.service.ChatRobotService;
import com.platform.modules.pay.service.PayAliService;
import com.platform.modules.pay.vo.PayVo;
import com.platform.modules.push.dto.PushBox;
import com.platform.modules.push.dto.PushFrom;
import com.platform.modules.push.enums.PushAuditEnum;
import com.platform.modules.push.enums.PushBoxEnum;
import com.platform.modules.push.enums.PushMsgTypeEnum;
import com.platform.modules.push.service.PushService;
import com.platform.modules.wallet.dao.WalletTradeDao;
import com.platform.modules.wallet.domain.WalletCash;
import com.platform.modules.wallet.domain.WalletTrade;
import com.platform.modules.wallet.enums.TradeTypeEnum;
import com.platform.modules.wallet.service.*;
import com.platform.modules.wallet.vo.WalletVo01;
import com.platform.modules.ws.service.HookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 钱包交易 服务层实现
 * </p>
 */
@Service("walletTradeService")
public class WalletTradeServiceImpl extends BaseServiceImpl<WalletTrade> implements WalletTradeService {

    @Resource
    private WalletTradeDao walletTradeDao;

    @Resource
    private WalletRechargeService walletRechargeService;

    @Resource
    private WalletCashService walletCashService;

    @Resource
    private WalletInfoService walletInfoService;

    @Resource
    private WalletPacketService walletPacketService;

    @Resource
    private ChatRobotService chatRobotService;

    @Resource
    private PushService pushService;

    @Resource
    private PayAliService payAliService;

    @Resource
    private HookService hookService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(walletTradeDao);
    }

    @Override
    public List<WalletTrade> queryList(WalletTrade t) {
        List<WalletTrade> dataList = walletTradeDao.queryList(t);
        return dataList;
    }

    @Override
    public PageInfo income(Long userId) {
        // 查询
        List<WalletTrade> dataList = this.queryList(new WalletTrade().setUserId(userId));
        // 转换
        List<Dict> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            Dict dict = Dict.create().parseBean(y)
                    .filter(WalletTrade.LABEL_TRADE_ID
                            , WalletTrade.LABEL_TRADE_TYPE
                            , WalletTrade.LABEL_TRADE_TYPE_LABEL
                            , WalletTrade.LABEL_TRADE_AMOUNT
                            , WalletTrade.LABEL_TRADE_BALANCE
                            , WalletTrade.LABEL_CREATE_TIME
                    )
                    .set(WalletTrade.LABEL_TRADE_TYPE_LABEL, y.getTradeType().getInfo());
            if (BigDecimal.ZERO.compareTo(y.getTradeAmount()) == -1) {
                dict.set(WalletTrade.LABEL_TRADE_AMOUNT, "+" + y.getTradeAmount());
            }
            x.add(dict);
        }, ArrayList::addAll);
        return getPageInfo(dictList, dataList);
    }

    @Override
    public PageInfo queryApproveList(WalletTrade walletTrade) {
        // 查询
        List<WalletTrade> dataList = this.queryList(walletTrade);
        // 转换
        List<Dict> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            Dict dict = Dict.create().parseBean(y)
                    .filter(WalletTrade.LABEL_TRADE_ID
                            , WalletTrade.LABEL_CREATE_TIME
                    )
                    .set(WalletTrade.LABEL_PORTRAIT, y.getPortrait())
                    .set(WalletTrade.LABEL_NICKNAME, y.getNickname())
                    .set(WalletTrade.LABEL_USER_ID, y.getUserId())
                    .set(WalletTrade.LABEL_USER_NO, y.getUserNo())
                    .set(WalletTrade.LABEL_PHONE, y.getPhone())
                    .set(WalletTrade.LABEL_AMOUNT, y.getAbsolute());
            x.add(dict);
        }, ArrayList::addAll);
        // 查询
        WalletTrade query = new WalletTrade()
                .setTradeType(TradeTypeEnum.CASH)
                .setTradeStatus(ApproveEnum.APPLY);
        Long count = this.queryCount(query);
        // 通知
        hookService.handle(PushAuditEnum.APPLY_CASH, count);
        // 返回
        return getPageInfo(dictList, dataList);
    }

    @Override
    public Dict queryApproveInfo(Long tradeId) {
        // 查询
        WalletCash walletCash = walletCashService.findById(tradeId);
        // 手续费用
        BigDecimal charge = NumberUtil.sub(walletCash.getCharge(), walletCash.getCost());
        // 服务费用
        BigDecimal balance = NumberUtil.sub(walletCash.getAmount(), walletCash.getCharge());
        // 查询用户
        WalletTrade trade = getById(tradeId);
        return Dict.create()
                .set(WalletTrade.LABEL_TRADE_ID, tradeId)
                .set(WalletTrade.LABEL_USER_ID, trade.getUserId())
                .set(WalletTrade.LABEL_USER_NO, trade.getUserNo())
                .set(WalletTrade.LABEL_AMOUNT, walletCash.getAmount())
                .set(WalletCash.LABEL_NAME, walletCash.getName())
                .set(WalletCash.LABEL_WALLET, StrUtil.removeAll(walletCash.getWallet(), " "))
                .set(WalletCash.LABEL_BALANCE, balance)
                .set(WalletCash.LABEL_CHARGE, charge)
                .set(WalletCash.LABEL_COST, walletCash.getCost())
                .set(WalletCash.LABEL_CREATE_TIME, walletCash.getCreateTime());
    }

    @Override
    public void approve(Long tradeId, ApproveEnum approveEnum, YesOrNoEnum auto, String reason) {
        // 查询
        WalletTrade walletTrade = this.findById(tradeId);
        // 验证
        if (!ApproveEnum.APPLY.equals(walletTrade.getTradeStatus())) {
            return;
        }
        Date now = DateUtil.date();
        Long userId = walletTrade.getUserId();
        // 更新
        this.updateById(new WalletTrade(tradeId).setTradeStatus(approveEnum).setUpdateTime(now));
        // 更新
        walletCashService.updateById(new WalletCash(tradeId).setUpdateTime(now).setReason(reason));
        // 同意
        if (ApproveEnum.PASS.equals(approveEnum)) {
            // 一键打款
            if (YesOrNoEnum.YES.equals(auto)) {
                PayVo payVo = walletCashService.transfer(tradeId);
                payAliService.transfer(payVo);
            }
        }
        // 拒绝
        else if (ApproveEnum.REJECT.equals(approveEnum)) {
            // 更新钱包
            BigDecimal balance = walletInfoService.addBalance(userId, walletTrade.getAbsolute());
            // 写入记录
            WalletTrade newTrade = BeanUtil.toBean(walletTrade, WalletTrade.class)
                    .setTradeId(null)
                    .setTradeAmount(walletTrade.getAbsolute())
                    .setTradeType(TradeTypeEnum.REFUND)
                    .setTradeStatus(ApproveEnum.PASS)
                    .setTradeRemark(TradeTypeEnum.REFUND.getInfo())
                    .setTradeBalance(balance)
                    .setSourceId(tradeId)
                    .setSourceType(walletTrade.getTradeType())
                    .setCreateTime(now)
                    .setUpdateTime(now);
            this.add(newTrade);
            // 通知
            this.pushTradeMsg(userId, newTrade.getTradeId(), walletTrade.getAbsolute());
        }
        // 通知
        this.pushApproveMsg(userId, tradeId, approveEnum, reason);
    }

    @Override
    public PageInfo queryTradeList(WalletVo01 walletVo) {
        // 交易类型
        TradeTypeEnum tradeType = walletVo.getTradeType();
        // 充值
        if (TradeTypeEnum.RECHARGE.equals(tradeType)) {
            return walletRechargeService.queryTradeList(walletVo);
        }
        String userNo = walletVo.getUserNo();
        String phone = walletVo.getPhone();
        String receiveNo = walletVo.getReceiveNo();
        String receivePhone = walletVo.getReceivePhone();
        String groupNo = walletVo.getGroupNo();
        String remark = walletVo.getRemark();
        // 查询
        QueryWrapper<WalletTrade> wrapper = new QueryWrapper<>();
        wrapper.eq(WalletTrade.COLUMN_TRADE_TYPE, tradeType);
        if (TradeTypeEnum.SIGN.equals(tradeType)) {
            wrapper.gt(WalletTrade.COLUMN_TRADE_AMOUNT, BigDecimal.ZERO);
        }else {
            wrapper.lt(WalletTrade.COLUMN_TRADE_AMOUNT, BigDecimal.ZERO);
        }

        if (!StringUtils.isEmpty(phone)) {
            wrapper.eq(WalletTrade.COLUMN_PHONE, phone);
        }
        if (!StringUtils.isEmpty(userNo)) {
            wrapper.eq(WalletTrade.COLUMN_USER_NO, userNo);
        }
        if (!StringUtils.isEmpty(receiveNo)) {
            wrapper.eq(WalletTrade.COLUMN_RECEIVE_NO, receiveNo);
        }
        if (!StringUtils.isEmpty(receivePhone)) {
            wrapper.eq(WalletTrade.COLUMN_RECEIVE_PHONE, receivePhone);
        }
        if (!StringUtils.isEmpty(groupNo)) {
            wrapper.eq(WalletTrade.COLUMN_GROUP_NO, groupNo);
        }
        if (!StringUtils.isEmpty(remark)) {
            wrapper.like(WalletTrade.COLUMN_TRADE_REMARK, remark);
        }
        List<WalletTrade> dataList = this.queryList(wrapper);
        List<Dict> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            x.add(format(y));
        }, ArrayList::addAll);
        return getPageInfo(dictList, dataList);
    }

    @Override
    public Object queryTradeInfo(Long tradeId) {
        // 查询
        WalletTrade walletTrade = findById(tradeId);
        // 交易类型
        TradeTypeEnum tradeType = walletTrade.getTradeType();
        // 充值
        if (TradeTypeEnum.RECHARGE.equals(tradeType)) {
            return walletRechargeService.queryTradeInfo(tradeId);
        }
        // 手气红包/普通红包
        if (TradeTypeEnum.PACKET_LUCK.equals(tradeType) || TradeTypeEnum.PACKET_NORMAL.equals(tradeType)) {
            return walletPacketService.queryDataList(tradeId);
        }
        Dict dict = format(walletTrade);
        // 提现
        if (TradeTypeEnum.CASH.equals(tradeType)) {
            WalletCash walletCash = walletCashService.getById(tradeId);
            dict.set(WalletCash.LABEL_NAME, walletCash.getName())
                    .set(WalletCash.LABEL_WALLET, walletCash.getWallet())
                    .set(WalletCash.LABEL_CHARGE, NumberUtil.sub(walletCash.getCharge(), walletCash.getCost()))
                    .set(WalletCash.LABEL_COST, walletCash.getCost())
                    .set(WalletCash.LABEL_BALANCE, NumberUtil.sub(walletCash.getAmount(), walletCash.getCharge()))
                    .set(WalletTrade.LABEL_AMOUNT, walletCash.getAmount())
                    .set(WalletCash.LABEL_REASON, walletCash.getReason());
        }
        return dict;
    }

    /**
     * 格式化
     */
    private Dict format(WalletTrade walletTrade) {
        Dict dict = Dict.create().parseBean(walletTrade)
                .filter(WalletTrade.LABEL_TRADE_ID
                        , WalletTrade.LABEL_USER_ID
                        , WalletTrade.LABEL_USER_NO
                        , WalletTrade.LABEL_SOURCE_ID
                        , WalletTrade.LABEL_PHONE
                        , WalletTrade.LABEL_NICKNAME
                        , WalletTrade.LABEL_CREATE_TIME
                        , WalletTrade.LABEL_UPDATE_TIME
                )
                .set(WalletTrade.LABEL_AMOUNT, walletTrade.getAbsolute());
        TradeTypeEnum tradeType = walletTrade.getTradeType();
        ApproveEnum tradeStatus = walletTrade.getTradeStatus();
        // 提现
        if (TradeTypeEnum.CASH.equals(tradeType)) {
            // 设置状态
            this.setStatus(dict, tradeStatus, "待处理", "已处理", "已驳回");
        }
        // 个人转账
        else if (TradeTypeEnum.TRANSFER.equals(tradeType) || TradeTypeEnum.SCAN.equals(tradeType)) {
            // 设置接收人
            this.setReceive(dict, walletTrade);
        }
        // 个人红包
        else if (TradeTypeEnum.PACKET.equals(tradeType)) {
            // 设置接收人
            this.setReceive(dict, walletTrade);
            // 设置状态
            this.setStatus(dict, tradeStatus, "未领取", "已领取", "已退款");
        }
        // 专属红包
        else if (TradeTypeEnum.PACKET_ASSIGN.equals(tradeType)) {
            // 设置接收人
            this.setReceive(dict, walletTrade);
            // 设置群组
            this.setGroup(dict, walletTrade);
            // 设置状态
            this.setStatus(dict, tradeStatus, "未领取", "已领取", "已退款");
        }
        // 群内转账
        else if (TradeTypeEnum.GROUP_TRANSFER.equals(tradeType)) {
            // 设置接收人
            this.setReceive(dict, walletTrade);
            // 设置群组
            this.setGroup(dict, walletTrade);
        }
        // 手气红包/普通红包
        else if (TradeTypeEnum.PACKET_LUCK.equals(tradeType) || TradeTypeEnum.PACKET_NORMAL.equals(tradeType)) {
            // 设置群组
            this.setGroup(dict, walletTrade);
            // 设置状态
            this.setStatus(dict, tradeStatus, "未领取", "已领完", "有退款");
            // 红包数量
            dict.set(WalletTrade.LABEL_TRADE_COUNT, walletTrade.getTradeCount());
        }
        // 消费
        else if (TradeTypeEnum.SHOPPING.equals(tradeType)) {
            dict.set(WalletTrade.LABEL_TRADE_REMARK, walletTrade.getTradeRemark());
        }
        return dict;
    }

    /**
     * 设置接收人
     */
    private void setReceive(Dict dict, WalletTrade walletTrade) {
        dict.set(WalletTrade.LABEL_RECEIVE_NAME, walletTrade.getReceiveName());
        dict.set(WalletTrade.LABEL_RECEIVE_ID, walletTrade.getReceiveId());
        dict.set(WalletTrade.LABEL_RECEIVE_NO, walletTrade.getReceiveNo());
        dict.set(WalletTrade.LABEL_RECEIVE_PHONE, walletTrade.getReceivePhone());
        dict.set(WalletTrade.LABEL_TRADE_REMARK, walletTrade.getTradeRemark());
    }

    /**
     * 设置群组
     */
    private void setGroup(Dict dict, WalletTrade walletTrade) {
        dict.set(WalletTrade.LABEL_GROUP_NAME, walletTrade.getGroupName());
        dict.set(WalletTrade.LABEL_GROUP_NO, walletTrade.getGroupNo());
        dict.set(WalletTrade.LABEL_GROUP_ID, walletTrade.getGroupId());
        dict.set(WalletTrade.LABEL_TRADE_REMARK, walletTrade.getTradeRemark());
    }

    /**
     * 设置状态
     */
    private void setStatus(Dict dict, ApproveEnum tradeStatus, String label1, String label2, String label3) {
        dict.set(WalletTrade.LABEL_TRADE_STATUS, tradeStatus);
        String label = null;
        switch (tradeStatus) {
            case APPLY:
                label = label1;
                break;
            case PASS:
                label = label2;
                break;
            case REJECT:
                label = label3;
                break;
        }
        dict.set(WalletTrade.LABEL_TRADE_STATUS_LABEL, label);
    }

    // 通知推送
    private void pushApproveMsg(Long userId, Long tradeId, ApproveEnum approveEnum, String reason) {
        // 查询服务
        PushFrom pushFrom = chatRobotService.getPushFrom(AppConstants.ROBOT_PUSH);
        // 组装消息
        String title = "提现审核";
        String content = "审核成功";
        String remark = "提现审核成功";
        String data = "提现审核成功";
        if (ApproveEnum.REJECT.equals(approveEnum)) {
            content = "审核失败";
            remark = "失败原因：" + reason;
            data = "提现审核失败";
        }
        PushBox pushBox = new PushBox(data, title, content, remark, PushBoxEnum.TRADE, tradeId);
        // 推送消息
        pushService.pushSingle(pushFrom, userId, JSONUtil.toJsonStr(pushBox), PushMsgTypeEnum.BOX);
    }

    // 通知推送
    private void pushTradeMsg(Long userId, Long tradeId, BigDecimal amount) {
        // 查询服务
        PushFrom pushFrom = chatRobotService.getPushFrom(AppConstants.ROBOT_PAY);
        // 组装消息
        String title = "提现退款";
        String content = StrUtil.format("￥ +{} 元", amount.abs().setScale(2));
        String remark = StrUtil.format("退款到账：￥ {} 元", amount.abs().setScale(2));
        PushBox pushBox = new PushBox(remark, title, content, remark, PushBoxEnum.TRADE, tradeId);
        // 推送消息
        pushService.pushSingle(pushFrom, userId, JSONUtil.toJsonStr(pushBox), PushMsgTypeEnum.BOX);
    }

}
