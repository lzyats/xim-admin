package com.platform.modules.chat.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.platform.modules.chat.service.ChatGroupLevelService;
import com.platform.modules.chat.vo.ChatVo09;
import com.platform.modules.sys.domain.SysDict;
import com.platform.modules.sys.service.SysDictService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * 群组级别 服务层实现
 * </p>
 */
@Service("chatGroupLevelService")
public class ChatGroupLevelServiceImpl implements ChatGroupLevelService {

    @Resource
    private SysDictService sysDictService;

    @Override
    public List<ChatVo09> queryList() {
        // 查询字典
        List<SysDict> dictList = sysDictService.queryDict("group_level");
        // list转Obj
        List<ChatVo09> dataList = dictList.stream().collect(ArrayList::new, (x, y) -> {
            x.add(new ChatVo09(y));
        }, ArrayList::addAll);
        Collections.sort(dataList, Comparator.comparing(ChatVo09::getGroupLevel));
        return dataList;
    }

    @Override
    public void update(ChatVo09 chatVo) {
        SysDict dict = new SysDict()
                .setDictId(chatVo.getPrincipal())
                .setDictCode(NumberUtil.toStr(chatVo.getLevelCount()))
                .setDictName(NumberUtil.toStr(chatVo.getLevelPrice()))
                .setRemark(chatVo.getStatus().getCode());
        sysDictService.updateById(dict);
    }

}
