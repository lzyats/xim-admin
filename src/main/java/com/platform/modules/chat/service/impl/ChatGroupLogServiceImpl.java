package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatGroupLogDao;
import com.platform.modules.chat.domain.ChatGroupLog;
import com.platform.modules.chat.enums.GroupLogEnum;
import com.platform.modules.chat.service.ChatGroupLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 群组日志 服务层实现
 * </p>
 */
@Service("chatGroupLogService")
public class ChatGroupLogServiceImpl extends BaseServiceImpl<ChatGroupLog> implements ChatGroupLogService {

    @Resource
    private ChatGroupLogDao chatGroupLogDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatGroupLogDao);
    }

    @Override
    public List<ChatGroupLog> queryList(ChatGroupLog t) {
        List<ChatGroupLog> dataList = chatGroupLogDao.queryList(t);
        return dataList;
    }

    @Override
    public void addLog(Long groupId, GroupLogEnum logType) {
        ChatGroupLog log = new ChatGroupLog(groupId)
                .setLogType(logType)
                .setContent(logType.getInfo())
                .setCreateTime(DateUtil.date());
        this.add(log);
    }

    @Override
    public PageInfo queryDataList(Long groupId) {
        // 查询
        List<ChatGroupLog> dataList = this.queryList(new ChatGroupLog(groupId));
        // 转换
        List<Dict> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            Dict dict = Dict.create().parseBean(y)
                    .filter(ChatGroupLog.LABEL_LOG_TYPE
                            ,ChatGroupLog.LABEL_CONTENT
                            ,ChatGroupLog.LABEL_CREATE_TIME
                    )
                    .set(ChatGroupLog.LABEL_LOG_TYPE_LABEL, y.getLogType().getInfo());
            x.add(dict);
        }, ArrayList::addAll);
        return getPageInfo(dictList, dataList);
    }

}
