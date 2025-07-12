package com.platform.modules.friend.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true) // 链式调用
public class FriendVo04 {

    /**
     * momentId
     */
    @NotNull(message = "momentId不能为空")
    private Long momentId;

    /**
     * 用户ID
     */
    @NotNull(message = "用户id不能为空")
    private Long userId;

    /**
     * 朋友圈信息
     */
    @NotBlank(message = "评论信息不能为空")
    @Size(max = 300, message = "信息不能超过300个字符")
    private String content;

    /**
     * 用户ID
     */
    //@NotNull(message = "用户id不能为空")
    private Long replyTo;

    private Integer visibility;

}
