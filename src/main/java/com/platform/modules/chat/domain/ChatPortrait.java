package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.chat.enums.ChatTalkEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>
 * 聊天头像实体类
 * </p>
 */
@Data
@TableName("chat_portrait")
@NoArgsConstructor
@Accessors(chain = true) // 链式调用
public class ChatPortrait extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 头像
     */
    @NotBlank(message = "头像不能为空")
    @Size(max = 200, message = "头像不能超过200个字符")
    private String portrait;
    /**
     * 类型
     */
    @NotNull(message = "类型不能为空")
    private ChatTalkEnum chatType;
    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    private YesOrNoEnum status;

    public ChatPortrait(ChatTalkEnum chatType) {
        this.chatType = chatType;
    }

    public String getChatTypeLabel() {
        if (chatType == null) {
            return null;
        }
        String label;
        switch (chatType) {
            case GROUP:
                label = "群组";
                break;
            case FRIEND:
                label = "个人";
                break;
            default:
                label = chatType.getInfo();
                break;
        }
        return label;
    }
}
