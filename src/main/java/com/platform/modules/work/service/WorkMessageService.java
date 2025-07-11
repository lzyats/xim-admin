package com.platform.modules.work.service;

import cn.hutool.json.JSONObject;
import com.platform.modules.work.vo.WorkVo00;
import com.platform.modules.work.vo.WorkVo02;

import java.util.List;

/**
 * <p>
 * 聊天消息 服务层
 * </p>
 */
public interface WorkMessageService {

    /**
     * 发送消息
     */
    WorkVo00 sendMsg(WorkVo02 workVo);

    /**
     * 拉取消息
     */
    List<JSONObject> pullMsg();

    /**
     * 删除消息
     */
    void removeMsg(Long userId, List<String> dataList);

}
