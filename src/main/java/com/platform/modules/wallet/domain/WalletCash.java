package com.platform.modules.wallet.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 钱包提现实体类
 * </p>
 */
@Data
@TableName("wallet_cash")
@NoArgsConstructor
@Accessors(chain = true) // 链式调用
public class WalletCash extends BaseEntity {

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
     * 姓名
     */
    private String name;
    /**
     * 钱包账户
     */
    private String wallet;
    /**
     * 申请金额
     */
    private BigDecimal amount;
    /**
     * 服务费
     */
    private BigDecimal charge;
    /**
     * 申请利率
     */
    private BigDecimal rate;
    /**
     * 服务费用
     */
    private BigDecimal cost;
    /**
     * 拒绝原因
     */
    private String reason;
    /**
     * 交易时间
     */
    private Date createTime;
    /**
     * 交易时间
     */
    private Date updateTime;

    public WalletCash(Long tradeId) {
        this.tradeId = tradeId;
    }

    /**
     * 字段
     */
    public static final String LABEL_NAME = "name";
    public static final String LABEL_WALLET = "wallet";
    public static final String LABEL_CHARGE = "charge";
    public static final String LABEL_BALANCE = "balance";
    public static final String LABEL_COST = "cost";
    public static final String LABEL_REASON = "reason";
    public static final String LABEL_CREATE_TIME = "createTime";

    /**
     * 字段
     */
    public static final String COLUMN_REASON = "reason";
    public static final String COLUMN_UPDATE_TIME = "update_time";

}
