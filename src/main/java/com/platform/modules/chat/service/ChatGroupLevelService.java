package com.platform.modules.chat.service;

import com.platform.modules.chat.vo.ChatVo09;

import java.util.List;

/**
 * <p>
 * 群组级别 服务层
 * </p>
 */
public interface ChatGroupLevelService {

    /**
     * 查询列表
     */
    List<ChatVo09> queryList();

    /**
     * 修改数据
     */
    void update(ChatVo09 chatVo);

}
