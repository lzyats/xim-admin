package com.platform.modules.chat.vo;

import cn.hutool.json.JSONUtil;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.modules.chat.domain.ChatFeedback;
import com.platform.modules.chat.domain.ChatUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true) // 链式调用
public class ChatVo07 {

    /**
     * 主键
     */
    private Long id;
    /**
     * 图片
     */
    private List<String> images;
    /**
     * 内容
     */
    private String content;
    /**
     * 提交版本
     */
    private String version;
    /**
     * 处理
     */
    private YesOrNoEnum status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 用户号码
     */
    private String userNo;
    /**
     * 头像
     */
    private String portrait;

    public String getStatusLabel() {
        if (status == null) {
            return null;
        }
        if (YesOrNoEnum.YES.equals(status)) {
            return "已处理";
        }
        return "待处理";
    }

    public ChatVo07(ChatFeedback feedback, ChatUser chatUser) {
        this.id = feedback.getId();
        this.images = JSONUtil.toList(feedback.getImages(), String.class);
        this.content = feedback.getContent();
        this.version = feedback.getVersion();
        this.status = feedback.getStatus();
        this.createTime = feedback.getCreateTime();
        this.userId = chatUser.getUserId();
        this.nickname = chatUser.getNickname();
        this.userNo = chatUser.getUserNo();
        this.portrait = chatUser.getPortrait();
    }
}
