package com.platform.modules.wallet.vo;

import com.platform.modules.wallet.enums.TradePayEnum;
import com.platform.modules.wallet.enums.TradeTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true) // 链式调用
public class WalletVo01 {

    /**
     * 交易类型
     */
    @NotNull(message = "交易类型不能为空")
    private TradeTypeEnum tradeType;
    /**
     * 用户号码
     */
    private String userNo;
    /**
     * 用户手机
     */
    private String phone;
    /**
     * 接收号码
     */
    private String receiveNo;
    /**
     * 接收手机
     */
    private String receivePhone;
    /**
     * 群号
     */
    private String groupNo;
    /**
     * 支付类型
     */
    private TradePayEnum payType;
    /**
     * 交易号码
     */
    private String tradeNo;
    /**
     * 交易号码
     */
    private String orderNo;
    /**
     * 交易备注
     */
    private String remark;

}
