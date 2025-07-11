package com.platform.modules.approve.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class ApproveVo00 {

    /**
     * 用户ID
     */
    private String userNo;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户手机
     */
    private String phone;

}
