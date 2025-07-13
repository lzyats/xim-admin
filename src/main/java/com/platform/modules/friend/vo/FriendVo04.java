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
     * 朋友圈信息
     */
    @NotBlank(message = "图片地址不能为空")
    @Size(max = 500, message = "图片地址超过300个字符")
    private String url;

    private Integer type;

}
