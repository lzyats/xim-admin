package com.platform.modules.chat.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatGroupInformDao;
import com.platform.modules.chat.domain.ChatGroupInform;
import com.platform.modules.chat.service.ChatGroupInformService;
import com.platform.modules.chat.service.ChatGroupService;
import com.platform.modules.push.enums.PushAuditEnum;
import com.platform.modules.ws.service.HookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 骚扰举报 服务层实现
 * </p>
 */
@Service("chatGroupInformService")
public class ChatGroupInformServiceImpl extends BaseServiceImpl<ChatGroupInform> implements ChatGroupInformService {

    @Resource
    private ChatGroupInformDao chatGroupInformDao;

    @Resource
    private HookService hookService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatGroupInformDao);
    }

    @Override
    public List<ChatGroupInform> queryList(ChatGroupInform t) {
        List<ChatGroupInform> dataList = chatGroupInformDao.queryList(t);
        return dataList;
    }

    @Override
    public PageInfo queryDataList(ChatGroupInform chatGroupInform) {
        // 查询
        List<ChatGroupInform> dataList = this.queryList(chatGroupInform);
        // 转换
        List<Dict> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            Dict dict = Dict.create().parseBean(y)
                    .filter(ChatGroupInform.LABEL_INFORM_ID
                            , ChatGroupInform.LABEL_INFORM_TYPE
                            , ChatGroupInform.LABEL_USER_ID
                            , ChatGroupInform.LABEL_USER_NO
                            , ChatGroupInform.LABEL_NICKNAME
                            , ChatGroupInform.LABEL_CONTENT
                            , ChatGroupInform.LABEL_STATUS
                            , ChatGroupInform.LABEL_CREATE_TIME)
                    .set(ChatGroupInform.LABEL_TARGET_ID, y.getGroupId())
                    .set(ChatGroupInform.LABEL_TARGET_NO, y.getGroupNo())
                    .set(ChatGroupInform.LABEL_TARGET_NAME, y.getGroupName())
                    .set(ChatGroupInform.LABEL_INFORM_TYPE_LABEL, y.getInformType().getInfo())
                    .set(ChatGroupInform.LABEL_IMAGES, JSONUtil.toList(y.getImages(), String.class))
                    .set(ChatGroupInform.LABEL_STATUS_LABEL, YesOrNoEnum.YES.equals(y.getStatus()) ? "已处理" : "未处理");
            x.add(dict);
        }, ArrayList::addAll);
        // 查询
        Long count = this.queryCount(new ChatGroupInform().setStatus(YesOrNoEnum.NO));
        // 通知
        hookService.handle(PushAuditEnum.INFORM_GROUP, count);
        // 返回
        return getPageInfo(dictList, dataList);
    }

    @Override
    public void ignore(Long informId) {
        // 更新
        ChatGroupInform inform = new ChatGroupInform(informId)
                .setStatus(YesOrNoEnum.YES);
        this.updateById(inform);
    }

}
