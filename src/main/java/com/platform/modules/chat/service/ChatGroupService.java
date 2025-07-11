package com.platform.modules.chat.service;

import cn.hutool.core.lang.Dict;
import com.github.pagehelper.PageInfo;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatGroup;
import com.platform.modules.chat.vo.ChatVo11;

/**
 * <p>
 * 群组 服务层
 * </p>
 */
public interface ChatGroupService extends BaseService<ChatGroup> {

    /**
     * 列表
     */
    PageInfo groupList(Long userId);

    /**
     * 列表
     */
    PageInfo queryDataList(ChatGroup chatGroup);

    /**
     * 详情
     */
    Dict getInfo(Long groupId);

    /**
     * 群组成员
     */
    PageInfo memberList(Long groupId);

    /**
     * 修改封禁
     */
    void banned(Long groupId, YesOrNoEnum banned);

    /**
     * 群员数量
     */
    void levelCount(Long groupId, Integer levelCount);

    /**
     * 修改群组
     */
    void editGroup(ChatVo11 chatVo);

}
