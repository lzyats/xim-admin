package com.platform.modules.chat.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatFriendInformDao;
import com.platform.modules.chat.domain.ChatFriendInform;
import com.platform.modules.chat.service.ChatFriendInformService;
import com.platform.modules.push.enums.PushAuditEnum;
import com.platform.modules.ws.service.HookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 骚扰举报 服务层实现
 * </p>
 */
@Service("chatFriendInformService")
public class ChatFriendInformServiceImpl extends BaseServiceImpl<ChatFriendInform> implements ChatFriendInformService {

    @Resource
    private ChatFriendInformDao chatFriendInformDao;

    @Resource
    private HookService hookService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatFriendInformDao);
    }

    @Override
    public List<ChatFriendInform> queryList(ChatFriendInform t) {
        List<ChatFriendInform> dataList = chatFriendInformDao.queryList(t);
        return dataList;
    }

    @Override
    public PageInfo queryDataList(ChatFriendInform chatFriendInform) {
        // 查询
        List<ChatFriendInform> dataList = this.queryList(chatFriendInform);
        // 转换
        List<Dict> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            Dict dict = Dict.create().parseBean(y)
                    .filter(ChatFriendInform.LABEL_INFORM_ID
                            , ChatFriendInform.LABEL_INFORM_TYPE
                            , ChatFriendInform.LABEL_USER_ID
                            , ChatFriendInform.LABEL_USER_NO
                            , ChatFriendInform.LABEL_NICKNAME
                            , ChatFriendInform.LABEL_TARGET_NO
                            , ChatFriendInform.LABEL_TARGET_NAME
                            , ChatFriendInform.LABEL_CONTENT
                            , ChatFriendInform.LABEL_STATUS
                            , ChatFriendInform.LABEL_CREATE_TIME)
                    .set(ChatFriendInform.LABEL_TARGET_ID, y.getGroupId())
                    .set(ChatFriendInform.LABEL_IMAGES, JSONUtil.toList(y.getImages(), String.class))
                    .set(ChatFriendInform.LABEL_INFORM_TYPE_LABEL, y.getInformType().getInfo())
                    .set(ChatFriendInform.LABEL_STATUS_LABEL, YesOrNoEnum.YES.equals(y.getStatus()) ? "已处理" : "未处理");
            x.add(dict);
        }, ArrayList::addAll);
        // 转换
        Long count = this.queryCount(new ChatFriendInform().setStatus(YesOrNoEnum.NO));
        // 通知
        hookService.handle(PushAuditEnum.INFORM_USER, count);
        // 返回
        return getPageInfo(dictList, dataList);
    }

    @Override
    public void ignore(Long informId) {
        // 更新
        ChatFriendInform inform = new ChatFriendInform(informId)
                .setStatus(YesOrNoEnum.YES);
        this.updateById(inform);
    }

}
