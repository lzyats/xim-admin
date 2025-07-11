package com.platform.modules.operate.service.impl;

import com.platform.modules.chat.domain.ChatConfig;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.service.ChatConfigService;
import com.platform.modules.operate.service.OperateCashService;
import com.platform.modules.operate.vo.OperateVo04;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 提现管理 服务层实现
 * </p>
 */
@Service("operateCashService")
public class OperateCashServiceImpl implements OperateCashService {

    @Resource
    private ChatConfigService chatConfigService;

    @Override
    public OperateVo04 getInfo() {
        // 查询
        Map<ChatConfigEnum, ChatConfig> configMap = chatConfigService.queryConfig();
        // 转换
        return new OperateVo04()
                .setAuth(configMap.get(ChatConfigEnum.WALLET_CASH_AUTH).getYesOrNo())
                .setCount(configMap.get(ChatConfigEnum.WALLET_CASH_COUNT).getInt())
                .setCost(configMap.get(ChatConfigEnum.WALLET_CASH_COST).getBigDecimal())
                .setMax(configMap.get(ChatConfigEnum.WALLET_CASH_MAX).getBigDecimal())
                .setMin(configMap.get(ChatConfigEnum.WALLET_CASH_MIN).getBigDecimal())
                .setRate(configMap.get(ChatConfigEnum.WALLET_CASH_RATE).getBigDecimal())
                .setRemark(configMap.get(ChatConfigEnum.WALLET_CASH_REMARK).getStr());
    }

    @Transactional
    @Override
    public void update(OperateVo04 operateVo) {
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.WALLET_CASH_AUTH).setValue(operateVo.getAuth()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.WALLET_CASH_COUNT).setValue(operateVo.getCount()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.WALLET_CASH_COST).setValue(operateVo.getCost()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.WALLET_CASH_MAX).setValue(operateVo.getMax()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.WALLET_CASH_MIN).setValue(operateVo.getMin()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.WALLET_CASH_RATE).setValue(operateVo.getRate()));
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.WALLET_CASH_REMARK).setValue(operateVo.getRemark()));
    }

}
