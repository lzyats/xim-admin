package com.platform.modules.chat.service.impl;

import cn.hutool.core.util.StrUtil;
import com.platform.common.exception.BaseException;
import com.platform.common.validation.ValidateGroup;
import com.platform.common.validation.ValidationUtil;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.common.web.vo.LabelVo;
import com.platform.modules.chat.dao.ChatRobotReplyDao;
import com.platform.modules.chat.domain.ChatRobotReply;
import com.platform.modules.chat.enums.RobotReplyEnum;
import com.platform.modules.chat.service.ChatRobotReplyService;
import com.platform.modules.chat.service.ChatRobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 服务号 服务层实现
 * </p>
 */
@Service("chatRobotReplyService")
public class ChatRobotReplyServiceImpl extends BaseServiceImpl<ChatRobotReply> implements ChatRobotReplyService {

    @Resource
    private ChatRobotReplyDao chatRobotReplyDao;

    @Resource
    private ChatRobotService chatRobotService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatRobotReplyDao);
    }

    @Override
    public List<ChatRobotReply> queryList(ChatRobotReply t) {
        List<ChatRobotReply> dataList = chatRobotReplyDao.queryList(t);
        return dataList;
    }

    @Override
    public List<ChatRobotReply> queryDataList(Long robotId, RobotReplyEnum replyType) {
        if (robotId == null) {
            throw new BaseException("服务号不能为空");
        }
        // 查询
        List<ChatRobotReply> dataList = this.queryList(new ChatRobotReply(robotId, replyType));
        dataList.forEach(data -> {
            formatReply(data);
        });
        return dataList;
    }

    @Override
    public List<LabelVo> queryEvenList(Long robotId) {
        // 查询
        List<ChatRobotReply> dataList = this.queryList(new ChatRobotReply(robotId, RobotReplyEnum.EVEN));
        // list转Obj
        List<String> labelList = new ArrayList<>();
        return dataList.stream().collect(ArrayList::new, (x, y) -> {
            String replyKey = y.getReplyKey();
            // 拼接前缀
            replyKey = StrUtil.removePrefix(replyKey, "|");
            // 拼接后缀
            replyKey = StrUtil.removeSuffix(replyKey, "|");
            // 分割
            for (String label : StrUtil.split(replyKey, "|")) {
                if (!labelList.contains(label)) {
                    labelList.add(label);
                    x.add(new LabelVo(label, y.getContent()));
                }
            }
        }, ArrayList::addAll);
    }

    /**
     * 格式化
     */
    private ChatRobotReply formatReply(ChatRobotReply robotReply) {
        String replyKey = null;
        if (!RobotReplyEnum.SUBSCRIBE.equals(robotReply.getReplyType())) {
            replyKey = robotReply.getReplyKey();
            // 拼接前缀
            replyKey = StrUtil.removePrefix(replyKey, "|");
            // 拼接后缀
            replyKey = StrUtil.removeSuffix(replyKey, "|");
        }
        return robotReply.setReplyKey(replyKey);
    }

    @Override
    public void addReply(ChatRobotReply chatRobotReply) {
        // 校验
        if (chatRobotService.getById(chatRobotReply.getRobotId()) == null) {
            throw new BaseException("服务不存在");
        }
        // 校验
        this.verifyReplyKey(chatRobotReply);
        // 新增
        this.add(chatRobotReply);
    }

    @Override
    public void editReply(ChatRobotReply chatRobotReply) {
        // 校验
        this.verifyReplyKey(chatRobotReply);
        // 修改
        this.updateById(chatRobotReply);
    }

    @Override
    public void copyReply(Long replyId) {
        ChatRobotReply robotReply = this.getById(replyId);
        if (robotReply == null) {
            throw new BaseException("数据不存在");
        }
        this.add(robotReply.setReplyId(null));
    }

    /**
     * 校验
     */
    private void verifyReplyKey(ChatRobotReply robotReply) {
        switch (robotReply.getReplyType()) {
            case REPLY:
            case EVEN:
                ValidationUtil.verify(robotReply, ValidateGroup.ONE.class);
                break;
        }
        String replyKey = robotReply.getReplyKey();
        if (StringUtils.isEmpty(replyKey)) {
            replyKey = robotReply.getReplyType().getCode();
        }
        // trim
        replyKey = StrUtil.trim(replyKey);
        // 拼接前缀
        replyKey = StrUtil.addPrefixIfNot(replyKey, "|");
        // 拼接后缀
        replyKey = StrUtil.addSuffixIfNot(replyKey, "|");
        robotReply.setReplyKey(StrUtil.trim(replyKey));
    }


}
