package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.platform.common.validation.ValidateGroup;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.chat.enums.RobotReplyEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>
 * 服务号实体类
 * </p>
 */
@Data
@TableName("chat_robot_reply")
@Accessors(chain = true) // 链式调用
@NoArgsConstructor
public class ChatRobotReply extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long replyId;
    /**
     * 机器人
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @NotNull(message = "服务号不能为空")
    private Long robotId;
    /**
     * 类型
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private RobotReplyEnum replyType;
    /**
     * 关键字
     */
    @NotBlank(message = "关键字不能为空", groups = ValidateGroup.ONE.class)
    @Size(max = 200, message = "关键字不能超过200个字符", groups = ValidateGroup.ONE.class)
    private String replyKey;
    /**
     * 内容
     */
    @NotBlank(message = "回复内容不能为空")
    @Size(max = 200, message = "回复内容不能超过200个字符")
    private String content;

    public ChatRobotReply(Long robotId, RobotReplyEnum replyType) {
        this.robotId = robotId;
        this.replyType = replyType;
    }

}
