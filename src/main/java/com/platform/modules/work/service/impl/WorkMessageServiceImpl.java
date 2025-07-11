package com.platform.modules.work.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.platform.common.constant.AppConstants;
import com.platform.common.shiro.ShiroUserVo;
import com.platform.common.shiro.ShiroUtils;
import com.platform.modules.chat.domain.ChatMsg;
import com.platform.modules.chat.domain.ChatRobot;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.enums.ChatTalkEnum;
import com.platform.modules.chat.service.ChatMsgService;
import com.platform.modules.chat.service.ChatRobotService;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.common.service.FileService;
import com.platform.modules.push.dto.PushFrom;
import com.platform.modules.push.dto.PushSync;
import com.platform.modules.push.enums.PushMsgTypeEnum;
import com.platform.modules.push.service.PushService;
import com.platform.modules.sys.service.SysTokenService;
import com.platform.modules.work.service.WorkMessageService;
import com.platform.modules.work.vo.WorkVo00;
import com.platform.modules.work.vo.WorkVo02;
import com.platform.modules.ws.service.HookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 聊天消息 服务层实现
 * </p>
 */
@Slf4j
@Service("workMessageService")
public class WorkMessageServiceImpl implements WorkMessageService {

    @Resource
    private ChatMsgService chatMsgService;

    @Resource
    private ChatRobotService chatRobotService;

    @Resource
    private PushService pushService;

    @Resource
    private ChatUserService chatUserService;

    @Resource
    private FileService fileService;

    @Resource
    private HookService hookService;

    @Resource
    private SysTokenService tokenService;

    @Transactional
    @Override
    public WorkVo00 sendMsg(WorkVo02 workVo) {
        Long msgId = IdWorker.getId();
        Long syncId = IdWorker.getId();
        Long userId = workVo.getUserId();
        Long robotId = AppConstants.ROBOT_ID;
        PushMsgTypeEnum msgType = workVo.getMsgType();
        JSONObject jsonObject = workVo.getContent();
        // 检查服务号
        ChatRobot robot = chatRobotService.getById(robotId);
        // 查询用户
        ChatUser chatUser = chatUserService.queryCache(userId);
        // 撤回消息
        if (PushMsgTypeEnum.RECALL.equals(msgType)) {
            this.recallMsg(jsonObject, robot, chatUser);
        }
        // 声音消息
        if (PushMsgTypeEnum.VOICE.equals(msgType)) {
            fileService.uploadVoice(msgId, jsonObject.getStr("data"));
        }
        // 消息内容
        String content = JSONUtil.toJsonStr(jsonObject);
        // 保存数据
        ChatMsg chatMsg = new ChatMsg()
                .setMsgId(msgId)
                .setSyncId(syncId)
                .setUserId(robotId)
                .setReceiveId(robotId)
                .setGroupId(robotId)
                .setMsgType(msgType)
                .setTalkType(ChatTalkEnum.ROBOT)
                .setContent(content)
                .setCreateTime(DateUtil.date());
        chatMsgService.add(chatMsg);
        // 在线客服
        PushFrom pushFrom = robot.getPushFrom()
                .setGroupId(robotId, userId);
        PushSync pushSync = chatUser.getPushSync();
        pushService.pushSingle(pushFrom, userId, content, msgType);
        pushService.pushSync(pushFrom, pushSync, content, msgType);
        // 返回结果
        return new WorkVo00(msgId, syncId);
    }

    /**
     * 消息回撤
     */
    private void recallMsg(JSONObject jsonObject, ChatRobot robot, ChatUser chatUser) {
        // 查询撤回消息
        Long msgId = jsonObject.getLong("data");
        ChatMsg chatMsg = chatMsgService.getById(msgId);
        if (chatMsg == null) {
            return;
        }
        String tips = StrUtil.format("[{}]撤回了一条消息", robot.getNickname());
        // 执行撤回
        pushService.recallMsg(Arrays.asList(NumberUtil.toStr(chatMsg.getMsgId()), NumberUtil.toStr(chatMsg.getSyncId())));
        // 推送
        PushFrom pushFrom = robot.getPushFrom()
                .setGroupId(robot.getRobotId(), chatUser.getUserId())
                .setSign("");
        PushSync pushSync = chatUser.getPushSync();
        // 正常消息
        pushService.pushSingle(pushFrom, chatUser.getUserId(), tips, PushMsgTypeEnum.TIPS);
        // 同步消息
        pushService.pushSync(pushFrom, pushSync, "[你]撤回了一条消息", PushMsgTypeEnum.TIPS);
    }

    @Override
    public List<JSONObject> pullMsg() {
        // 下发计数
        ThreadUtil.execAsync(() -> {
            hookService.connection();
        });
        Long current = AppConstants.ROBOT_ID;
        String lastId = ShiroUtils.getLastId();
        // 拉取消息
        List<JSONObject> dataList = pushService.pullMsg(current, lastId, 200);
        // 集合判空
        if (CollectionUtils.isEmpty(dataList)) {
            return dataList;
        }
        // 刷新msgId
        JSONObject data = dataList.get(dataList.size() - 1);
        String msgId = data.getJSONObject("pushData").getStr("msgId");
        ShiroUserVo userVo = new ShiroUserVo().setLastId(msgId);
        tokenService.refresh(userVo);
        return dataList;
    }

    @Override
    public void removeMsg(Long userId, List<String> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        pushService.removeMsg(userId, dataList);
    }

}
