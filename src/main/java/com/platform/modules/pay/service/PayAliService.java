package com.platform.modules.pay.service;

import com.platform.modules.pay.vo.PayVo;

/**
 * <p>
 * 阿里支付 服务层
 * </p>
 */
public interface PayAliService {

    /**
     * 单笔转账
     */
    void transfer(PayVo payVo);

}
