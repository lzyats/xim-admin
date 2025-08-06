package com.platform.modules.wallet.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.wallet.enums.TradeTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 钱包交易实体类
 * </p>
 */
@Data
@TableName("wallet_trade")
@NoArgsConstructor
@Accessors(chain = true) // 链式调用
public class WalletTrade extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long tradeId;
    /**
     * 交易类型
     */
    private TradeTypeEnum tradeType;
    /**
     * 交易金额
     */
    private BigDecimal tradeAmount;
    /**
     * 钱包余额
     */
    private BigDecimal tradeBalance;
    /**
     * 交易数量
     */
    private Integer tradeCount;
    /**
     * 交易备注
     */
    private String tradeRemark;
    /**
     * 来源id
     */
    private Long sourceId;
    /**
     * 来源类型
     */
    private TradeTypeEnum sourceType;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户号码
     */
    private String userNo;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户账号
     */
    private String phone;
    /**
     * 用户头像
     */
    private String portrait;
    /**
     * 群组
     */
    private Long groupId;
    /**
     * 群号
     */
    private String groupNo;
    /**
     * 群名
     */
    private String groupName;
    /**
     * 接收人
     */
    private Long receiveId;
    /**
     * 接收号码
     */
    private String receiveNo;
    /**
     * 接收昵称
     */
    private String receiveName;
    /**
     * 接收账号
     */
    private String receivePhone;
    /**
     * 接收头像
     */
    private String receivePortrait;
    /**
     * 交易状态
     */
    private ApproveEnum tradeStatus;
    /**
     * 交易时间
     */
    private Date createTime;
    /**
     * 交易时间
     */
    private Date updateTime;
    /**
     * 注销0正常null注销
     */
    private Integer deleted;

    public BigDecimal getAbsolute() {
        if (tradeAmount == null) {
            tradeAmount = BigDecimal.ZERO;
        }
        return tradeAmount.abs();
    }

    public WalletTrade(Long tradeId) {
        this.tradeId = tradeId;
    }

    /**
     * 字段
     */
    public static final String LABEL_TRADE_ID = "tradeId";
    public static final String LABEL_TRADE_TYPE = "tradeType";
    public static final String LABEL_TRADE_TYPE_LABEL = "tradeTypeLabel";
    public static final String LABEL_TRADE_AMOUNT = "tradeAmount";
    public static final String LABEL_TRADE_BALANCE = "tradeBalance";
    public static final String LABEL_PORTRAIT = "portrait";
    public static final String LABEL_NICKNAME = "nickname";
    public static final String LABEL_USER_ID = "userId";
    public static final String LABEL_USER_NO = "userNo";
    public static final String LABEL_SOURCE_ID= "sourceId";
    public static final String LABEL_PHONE = "phone";
    public static final String LABEL_RECEIVE_NAME = "receiveName";
    public static final String LABEL_RECEIVE_NO = "receiveNo";
    public static final String LABEL_RECEIVE_ID = "receiveId";
    public static final String LABEL_RECEIVE_PHONE = "receivePhone";
    public static final String LABEL_GROUP_ID = "groupId";
    public static final String LABEL_GROUP_NO = "groupNo";
    public static final String LABEL_GROUP_NAME = "groupName";
    public static final String LABEL_AMOUNT = "amount";
    public static final String LABEL_CREATE_TIME = "createTime";
    public static final String LABEL_UPDATE_TIME = "updateTime";
    public static final String LABEL_TRADE_STATUS = "tradeStatus";
    public static final String LABEL_TRADE_STATUS_LABEL = "tradeStatusLabel";
    public static final String LABEL_TRADE_REMARK = "remark";
    public static final String LABEL_TRADE_COUNT = "tradeCount";
    /**
     * 字段
     */
    public static final String COLUMN_TRADE_TYPE = "trade_type";
    public static final String COLUMN_TRADE_AMOUNT = "trade_amount";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_USER_NO = "user_no";
    public static final String COLUMN_RECEIVE_NO = "receive_no";
    public static final String COLUMN_RECEIVE_PHONE = "receive_phone";
    public static final String COLUMN_GROUP_NO = "group_no";
    public static final String COLUMN_TRADE_REMARK = "trade_remark";
    public static final String COLUMN_TRADE_STATUS = "trade_status";

}
