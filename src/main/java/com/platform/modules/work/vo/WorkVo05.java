package com.platform.modules.work.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true) // 链式调用
public class WorkVo05 {

    /**
     * 主键
     */
    private Long userId;
    /**
     * 微聊号
     */
    private String userNo;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String portrait;
    /**
     * ip
     */
    private String ipAddr;
    /**
     * 注册时间
     */
    private Date createTime;

}
