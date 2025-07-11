package com.platform.modules.operate.service.impl;

import cn.hutool.core.util.StrUtil;
import com.platform.common.exception.BaseException;
import com.platform.modules.chat.domain.ChatConfig;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.service.ChatConfigService;
import com.platform.modules.operate.service.OperateRechargeService;
import com.platform.modules.operate.vo.OperateVo03;
import com.platform.modules.wallet.enums.TradePayEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 充值管理 服务层实现
 * </p>
 */
@Service("operateRechargeService")
public class OperateRechargeServiceImpl implements OperateRechargeService {

    @Resource
    private ChatConfigService chatConfigService;

    @Override
    public OperateVo03 getInfo() {
        // 查询
        Map<ChatConfigEnum, ChatConfig> configMap = chatConfigService.queryConfig();
        // 转换
        String startTime = configMap.get(ChatConfigEnum.WALLET_RECHARGE_START).getStr();
        String endTime = configMap.get(ChatConfigEnum.WALLET_RECHARGE_END).getStr();
        return new OperateVo03()
                .setAmount(configMap.get(ChatConfigEnum.WALLET_RECHARGE_AMOUNT).getBigDecimal())
                .setTimeRange(Arrays.asList(startTime, endTime))
                .setAndroid(configMap.get(ChatConfigEnum.WALLET_RECHARGE_ANDROID).getStr())
                .setIos(configMap.get(ChatConfigEnum.WALLET_RECHARGE_IOS).getStr())
                .setCount(configMap.get(ChatConfigEnum.WALLET_RECHARGE_COUNT).getInt());
    }

    @Transactional
    @Override
    public void update(OperateVo03 operateVo) {
        // 参数
        List<String> timeRange = operateVo.getTimeRange();
        if (timeRange.size() != 2) {
            throw new BaseException("充值时间不能为空");
        }
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.WALLET_RECHARGE_AMOUNT).setValue(operateVo.getAmount()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.WALLET_RECHARGE_START).setValue(timeRange.get(0)));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.WALLET_RECHARGE_END).setValue(timeRange.get(1)));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.WALLET_RECHARGE_ANDROID).setValue(formatType(operateVo.getAndroid())));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.WALLET_RECHARGE_IOS).setValue(formatType(operateVo.getIos())));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.WALLET_RECHARGE_COUNT).setValue(operateVo.getCount()));
    }

    /**
     * 格式化支付方式
     */
    private String formatType(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        List<String> dataList = StrUtil.split(str, ',');
        for (TradePayEnum tradePayEnum : TradePayEnum.values()) {
            String code = tradePayEnum.getCode();
            if (dataList.contains(code)) {
                builder.append(code).append(",");
            }
        }
        return StrUtil.removeSuffix(builder.toString(), ",");
    }

}
