package com.platform.modules.friend.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Accessors(chain = true) // 链式调用
public class FriendVo02 {

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
    @NotBlank(message = "朋友圈信息不能为空")
    @Size(max = 300, message = "朋友圈信息不能超过300个字符")
    private String content;

    /**
     * 用户ID
     */
    //@NotNull(message = "用户id不能为空")
    private String location;

    private Integer visibility;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
