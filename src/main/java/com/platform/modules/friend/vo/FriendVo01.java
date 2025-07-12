package com.platform.modules.friend.vo;

import com.platform.common.validation.ValidateGroup;
import com.platform.modules.chat.enums.BannedTimeEnum;
import com.platform.modules.chat.enums.BannedTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true) // 链式调用
public class FriendVo01 {

    /**
     * 用户ID
     */
    @NotNull(message = "用户id不能为空")
    private Long userId;

    /**
     * 朋友圈信息
     */
    @NotBlank(message = "朋友圈信息不能为空")
    @Size(max = 300, message = "朋友圈信息不能超过300个字符")
    private String content;

    /**
     * 用户ID
     */
    //@NotNull(message = "用户id不能为空")
    private String location;

    private Integer visibility;
}
