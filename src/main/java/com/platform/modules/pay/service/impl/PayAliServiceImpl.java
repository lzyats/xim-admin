package com.platform.modules.pay.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.ijpay.alipay.AliPayApi;
import com.platform.common.exception.BaseException;
import com.platform.modules.pay.service.PayAliService;
import com.platform.modules.pay.vo.PayVo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 阿里支付 服务层实现
 * </p>
 */
@Slf4j
@Service("payAliService")
public class PayAliServiceImpl implements PayAliService {

    @SneakyThrows
    @Override
    public void transfer(PayVo payVo) {
        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();
        model.setOutBizNo(NumberUtil.toStr(payVo.getTradeId()));
        model.setTransAmount(NumberUtil.toStr(payVo.getAmount()));
        model.setBizScene("DIRECT_TRANSFER");
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");
        model.setOrderTitle("批量转账");
        Participant payeeInfo = new Participant();
        payeeInfo.setIdentityType("ALIPAY_LOGON_ID");
        payeeInfo.setName(payVo.getName());
        payeeInfo.setIdentity(StrUtil.removeAll(payVo.getWallet(), " "));
        model.setPayeeInfo(payeeInfo);
        AlipayFundTransUniTransferResponse response = AliPayApi.uniTransferToResponse(model, null);
        if (!"10000".equals(response.getCode())) {
            throw new BaseException(response.getSubMsg());
        }
        if (!"Success".equals(response.getMsg())) {
            throw new BaseException(response.getSubMsg());
        }
    }

}
