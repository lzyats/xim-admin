package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.common.web.vo.LabelVo;
import com.platform.modules.chat.domain.ChatRobot;
import com.platform.modules.chat.vo.ChatVo02;
import com.platform.modules.push.dto.PushFrom;

import java.util.List;

/**
 * <p>
 * 服务号 服务层
 * </p>
 */
public interface ChatRobotService extends BaseService<ChatRobot> {

    /**
     * 发送人
     */
    PushFrom getPushFrom(Long robotId);

    /**
     * 修改
     */
    void editRobot(ChatRobot robot);

    /**
     * 菜单
     */
    void editMenu(ChatVo02 chatVo);

    /**
     * 重置
     */
    void resetRobot(Long robotId);

    /**
     * 菜单类型
     */
    List<LabelVo> extend(String extendType, Long robotId);

}
