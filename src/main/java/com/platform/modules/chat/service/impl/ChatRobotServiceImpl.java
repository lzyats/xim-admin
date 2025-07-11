package com.platform.modules.chat.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.platform.common.constant.AppConstants;
import com.platform.common.core.EnumUtils;
import com.platform.common.exception.BaseException;
import com.platform.common.redis.RedisUtils;
import com.platform.common.validation.ValidateGroup;
import com.platform.common.validation.ValidationUtil;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.common.web.vo.LabelVo;
import com.platform.modules.chat.dao.ChatRobotDao;
import com.platform.modules.chat.domain.ChatRobot;
import com.platform.modules.chat.enums.RobotMenuEnum;
import com.platform.modules.chat.service.ChatRobotReplyService;
import com.platform.modules.chat.service.ChatRobotService;
import com.platform.modules.chat.vo.ChatVo02;
import com.platform.modules.chat.vo.ChatVo03;
import com.platform.modules.push.dto.PushFrom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务号 服务层实现
 * </p>
 */
@Service("chatRobotService")
public class ChatRobotServiceImpl extends BaseServiceImpl<ChatRobot> implements ChatRobotService {

    @Resource
    private ChatRobotDao chatRobotDao;

    @Resource
    private ChatRobotReplyService chatRobotReplyService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatRobotDao);
    }

    @Override
    public List<ChatRobot> queryList(ChatRobot t) {
        return chatRobotDao.queryList(t);
    }

    @Override
    public PushFrom getPushFrom(Long robotId) {
        ChatRobot chatRobot = this.findById(robotId, "服务不存在");
        return chatRobot.getPushFrom();
    }

    @Override
    public void editRobot(ChatRobot robot) {
        // 更新数据
        ChatRobot chatRobot = new ChatRobot(robot.getRobotId())
                .setNickname(robot.getNickname())
                .setPortrait(robot.getPortrait());
        this.updateById(chatRobot);
        // 删除缓存
        this.clearCache(robot.getRobotId());
    }

    @Override
    public void editMenu(ChatVo02 chatVo) {
        Long robotId = chatVo.getRobotId();
        // 验证
        if (!JSONUtil.isTypeJSON(chatVo.getMenu())) {
            throw new BaseException("菜单格式不正确");
        }
        // 转换验证
        List<ChatVo03> dataList = JSONUtil.toList(chatVo.getMenu(), ChatVo03.class);
        // 校验
        for (ChatVo03 data : dataList) {
            // 子菜单
            List<ChatVo03> children = data.getChildren();
            // 主菜单
            if (CollectionUtils.isEmpty(children)) {
                ValidationUtil.verify(data);
            }
            // 子菜单
            else {
                ValidationUtil.verify(data, ValidateGroup.ONE.class);
                for (ChatVo03 child : children) {
                    ValidationUtil.verify(child, ValidateGroup.TWO.class);
                }
            }
        }
        // 转换验证
        String menu = JSONUtil.toJsonStr(dataList);
        this.updateById(new ChatRobot(robotId).setMenu(menu));
        // 删除缓存
        this.clearCache(robotId);
    }

    @Override
    public void resetRobot(Long robotId) {
        ChatRobot robot = new ChatRobot()
                .setRobotId(robotId)
                .setSecret(RandomUtil.randomString(32));
        // 更新数据
        this.updateById(robot);
        // 删除缓存
        this.clearCache(robotId);
    }

    @Override
    public List<LabelVo> extend(String extendType, Long robotId) {
        List<LabelVo> dataList = new ArrayList<>();
        switch (EnumUtils.toEnum(RobotMenuEnum.class, extendType)) {
            // 小程序
            case MINI:
                dataList.add(new LabelVo("小程序1", "1"));
                dataList.add(new LabelVo("小程序2", "2"));
                break;
            // 内页跳转
            case PAGE:
                dataList.add(new LabelVo("好友通知", "/friend_approve"));
                dataList.add(new LabelVo("群聊通知", "/group_approve"));
                dataList.add(new LabelVo("我的群聊", "/group_index"));
                dataList.add(new LabelVo("官方服务", "/robot_index"));
                dataList.add(new LabelVo("账单明细", "/wallet_trade?1000"));
                dataList.add(new LabelVo("充值记录", "/wallet_trade?1001"));
                dataList.add(new LabelVo("红包记录", "/wallet_trade?1004"));
                dataList.add(new LabelVo("提现记录", "/wallet_trade?1002"));
                dataList.add(new LabelVo("支付密码", "/wallet_payment"));
                dataList.add(new LabelVo("关于我们", "/common_about"));
                dataList.add(new LabelVo("帮助中心", "/common_help"));
                dataList.add(new LabelVo("建议反馈", "/common_feedback"));
                break;
            // 事件消息
            case EVEN:
                dataList = chatRobotReplyService.queryEvenList(robotId);
                break;
        }
        return dataList;
    }

    /**
     * 移除缓存
     */
    private void clearCache(Long robotId) {
        redisUtils.delete(AppConstants.REDIS_CHAT_ROBOT + robotId);
    }

}
