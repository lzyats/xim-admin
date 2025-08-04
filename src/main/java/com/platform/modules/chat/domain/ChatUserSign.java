package com.platform.modules.chat.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.platform.common.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户按天签到记录实体类
 * </p>
 */
@Data
@TableName("chat_user_sign")
@Accessors(chain = true) // 链式调用
public class ChatUserSign extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
    private Long signid;
    /**
     * 用户ID（关联用户表）
     */
    private Long userId;
    /**
     * 交易id
     */
    private Long tradeId;
    /**
     * 签到日期（仅记录年月日，精确到天）
     */
    private Date signDate;
    /**
     * 签到奖励（如USDT数量）
     */
    private BigDecimal rewardAmount;
    /**
     * 签到类型：1-正常签到，2-补签
     */
    private Boolean signType;
    /**
     * 是否有效：1-有效，0-无效（如取消签到）
     */
    private Boolean isValid;
    /**
     * 创建时间（精确到秒）
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

}
