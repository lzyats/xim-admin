package com.platform.modules.wallet.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 用户钱包实体类
 * </p>
 */
@Data
@TableName("wallet_info")
@NoArgsConstructor
@Accessors(chain = true) // 链式调用
public class WalletInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户
     */
    @TableId
    private Long userId;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 版本
     */
    @Version
    private Integer version;
    /**
     * 盐巴
     */
    private String salt;
    /**
     * 密码
     */
    private String password;

    public WalletInfo(Long userId) {
        this.userId = userId;
        this.balance = BigDecimal.ZERO;
        this.version = 0;
    }

}
