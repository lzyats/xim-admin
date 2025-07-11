package com.platform.modules.work.service.impl;

import com.platform.common.constant.AppConstants;
import com.platform.common.shiro.ShiroUtils;
import com.platform.modules.chat.domain.ChatRobot;
import com.platform.modules.chat.service.ChatRobotService;
import com.platform.modules.work.service.WorkMineService;
import com.platform.modules.work.vo.WorkVo04;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 我的 服务层实现
 * </p>
 */
@Slf4j
@Service("workMineService")
public class WorkMineServiceImpl implements WorkMineService {

    @Resource
    private ChatRobotService chatRobotService;

    @Override
    public WorkVo04 getInfo() {
        ChatRobot robot = chatRobotService.getById(AppConstants.ROBOT_ID);
        return new WorkVo04()
                .setUserId(robot.getRobotId())
                .setNickname(robot.getNickname())
                .setPortrait(robot.getPortrait())
                .setSign(ShiroUtils.getSign());
    }

    @Transactional
    @Override
    public void refresh(String cid) {

    }

}
