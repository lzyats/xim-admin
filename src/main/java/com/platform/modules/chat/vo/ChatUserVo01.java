package com.platform.modules.chat.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true) // 链式调用
public class ChatUserVo01 {

    /**
     * 主键
     */
    @TableId
    private Long userId;

    /**
     * 昵称
     */
    private String nickname;


}
