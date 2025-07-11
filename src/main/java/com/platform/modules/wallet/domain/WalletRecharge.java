package com.platform.modules.wallet.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.wallet.enums.TradePayEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 钱包充值实体类
 * </p>
 */
@Data
@TableName("wallet_recharge")
@Accessors(chain = true) // 链式调用
public class WalletRecharge extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 交易id
     */
    @TableId
    private Long tradeId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户编号
     */
    private String userNo;
    /**
     * 用户手机
     */
    private String phone;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 支付类型
     */
    private TradePayEnum payType;
    /**
     * 支付金额
     */
    private BigDecimal amount;
    /**
     * 交易号码
     */
    private String tradeNo;
    /**
     * 交易号码
     */
    private String orderNo;
    /**
     * 交易时间
     */
    private Date createTime;
    /**
     * 处理时间
     */
    private Date updateTime;

    /**
     * 字段
     */
    public static final String LABEL_TRADE_ID = "tradeId";
    public static final String LABEL_USER_ID = "userId";
    public static final String LABEL_USER_NO = "userNo";
    public static final String LABEL_PHONE = "phone";
    public static final String LABEL_NICKNAME = "nickname";
    public static final String LABEL_PAY_TYPE = "payType";
    public static final String LABEL_PAY_TYPE_LABEL = "payTypeLabel";
    public static final String LABEL_AMOUNT = "amount";
    public static final String LABEL_TRADE_NO = "tradeNo";
    public static final String LABEL_ORDER_NO = "orderNo";
    public static final String LABEL_CREATE_TIME = "createTime";
    public static final String LABEL_UPDATE_TIME = "updateTime";
    /**
     * 字段
     */
    public static final String COLUMN_TRADE_NO = "trade_no";
    public static final String COLUMN_ORDER_NO = "order_no";
    public static final String COLUMN_PAY_TYPE = "pay_type";
    public static final String COLUMN_USER_NO = "user_no";
    public static final String COLUMN_PHONE = "phone";

}
