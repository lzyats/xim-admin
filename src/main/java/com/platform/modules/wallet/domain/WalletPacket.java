package com.platform.modules.wallet.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 钱包红包实体类
 * </p>
 */
@Data
@TableName("wallet_packet")
@Accessors(chain = true) // 链式调用
public class WalletPacket extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long packetId;
    /**
     * 交易id
     */
    private Long tradeId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户no
     */
    private String userNo;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String portrait;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 字段
     */
    public static final String LABEL_USER_NO = "userNo";
    public static final String LABEL_NICKNAME = "nickname";
    public static final String LABEL_AMOUNT = "amount";
    public static final String LABEL_CREATE_TIME = "createTime";
    /**
     * 字段
     */
    public static final String COLUMN_TRADE_ID = "trade_id";
}
