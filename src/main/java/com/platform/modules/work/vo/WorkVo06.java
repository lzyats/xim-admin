package com.platform.modules.work.vo;

import com.platform.common.enums.ApproveEnum;
import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true) // 链式调用
public class WorkVo06 {

    /**
     * 主键
     */
    private Long userId;
    /**
     * 账号
     */
    private String phone;
    /**
     * 微聊号
     */
    private String userNo;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String portrait;
    /**
     * 备注
     */
    private String remark;
    /**
     * 禁用标志
     */
    private YesOrNoEnum banned;
    /**
     * 认证状态
     */
    private ApproveEnum auth;
    /**
     * 认证状态
     */
    private String authLabel;
    /**
     * 在线时间
     */
    private Date onLine;
    /**
     * ip
     */
    private String ip;
    /**
     * ip地址
     */
    private String ipAddr;
    /**
     * 注册时间
     */
    private Date createTime;
    /**
     * 账户余额
     */
    private BigDecimal balance;

}
