package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.chat.enums.ChatTalkEnum;
import com.platform.modules.push.dto.PushFrom;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * <p>
 * 服务号实体类
 * </p>
 */
@Data
@TableName("chat_robot")
@Accessors(chain = true) // 链式调用
@NoArgsConstructor
public class ChatRobot extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long robotId;
    /**
     * 秘钥
     */
    private String secret;
    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    @Size(max = 20, message = "昵称不能超过20个字符")
    private String nickname;
    /**
     * 头像
     */
    @NotBlank(message = "头像不能为空")
    @Size(max = 200, message = "头像不能超过200个字符")
    private String portrait;
    /**
     * 菜单
     */
    private String menu;

    public ChatRobot(Long robotId) {
        this.robotId = robotId;
    }

    public PushFrom getPushFrom() {
        return new PushFrom()
                .setUserId(robotId)
                .setNickname(nickname)
                .setPortrait(portrait)
                .setMsgId(IdWorker.getId())
                .setSyncId(IdWorker.getId())
                .setSign(ShiroUtils.getSign())
                .setChatTalk(ChatTalkEnum.ROBOT.getType());
    }

}
