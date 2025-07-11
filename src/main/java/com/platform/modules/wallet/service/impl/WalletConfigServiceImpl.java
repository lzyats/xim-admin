package com.platform.modules.wallet.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.platform.modules.operate.vo.OperateVo07;
import com.platform.modules.sys.domain.SysDict;
import com.platform.modules.sys.service.SysDictService;
import com.platform.modules.wallet.service.WalletConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * 钱包容量 服务层实现
 * </p>
 */
@Service("walletConfigService")
public class WalletConfigServiceImpl implements WalletConfigService {

    @Resource
    private SysDictService sysDictService;

    @Override
    public List<OperateVo07> queryList() {
        // 查询字典
        List<SysDict> dictList = sysDictService.queryDict("recharge_amount");
        // list转Obj
        List<OperateVo07> dataList = dictList.stream().collect(ArrayList::new, (x, y) -> {
            x.add(new OperateVo07(y));
        }, ArrayList::addAll);
        Collections.sort(dataList, Comparator.comparing(OperateVo07::getAmount));
        return dataList;
    }

    @Override
    public void update(OperateVo07 operateVo) {
        SysDict dict = new SysDict()
                .setDictId(operateVo.getPrincipal())
                .setDictCode(NumberUtil.toStr(operateVo.getLevel()))
                .setDictName(NumberUtil.toStr(operateVo.getAmount()))
                .setRemark(operateVo.getStatus().getCode());
        sysDictService.updateById(dict);
    }

}
