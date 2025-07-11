package com.platform.modules.operate.service.impl;

import com.platform.common.enums.YesOrNoEnum;
import com.platform.modules.chat.domain.ChatConfig;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.service.ChatConfigService;
import com.platform.modules.operate.service.OperateGroupService;
import com.platform.modules.operate.vo.OperateVo05;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 群组扩容 服务层实现
 * </p>
 */
@Service("operateGroupService")
public class OperateGroupServiceImpl implements OperateGroupService {

    @Resource
    private ChatConfigService chatConfigService;

    @Override
    public OperateVo05 getInfo() {
        // 查询
        Map<ChatConfigEnum, ChatConfig> configMap = chatConfigService.queryConfig();
        // 转换
        return new OperateVo05()
                .setCount(configMap.get(ChatConfigEnum.GROUP_LEVEL_COUNT).getInt())
                .setSearch(configMap.get(ChatConfigEnum.GROUP_NAME_SEARCH).getYesOrNo());
    }

    @Transactional
    @Override
    public void update(OperateVo05 operateVo) {
        Integer count = operateVo.getCount();
        YesOrNoEnum search = operateVo.getSearch();
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.GROUP_LEVEL_COUNT).setValue(count));
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.GROUP_NAME_SEARCH).setValue(search));
    }

}
