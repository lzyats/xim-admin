package com.platform.modules.work.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class WorkVo04 {

    /**
     * 主键
     */
    private Long userId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 签名
     */
    private String sign;
    /**
     * 头像
     */
    private String portrait;

}
