package com.platform.modules.work.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true) // 链式调用
public class WorkVo10 {

    /**
     * 主键
     */
    private Long groupId;
    /**
     * 群ID
     */
    private String groupNo;
    /**
     * 群名
     */
    private String groupName;
    /**
     * 头像
     */
    private String portrait;
    /**
     * 禁用标志
     */
    private YesOrNoEnum banned;
    /**
     * 公告
     */
    private String notice;
    /**
     * 公告置顶
     */
    private YesOrNoEnum noticeTop;
    /**
     * 成员保护
     */
    private YesOrNoEnum configMember;
    /**
     * 允许邀请
     */
    private YesOrNoEnum configInvite;
    /**
     * 全员禁言Y=禁言N=不禁言
     */
    private YesOrNoEnum configSpeak;
    /**
     * 群组头衔
     */
    private YesOrNoEnum configTitle;
    /**
     * 审核开关
     */
    private YesOrNoEnum configAudit;
    /**
     * 发送资源
     */
    private YesOrNoEnum configMedia;
    /**
     * 专属可见
     */
    private YesOrNoEnum configAssign;
    /**
     * 昵称开关
     */
    private YesOrNoEnum configNickname;
    /**
     * 红包开关
     */
    private YesOrNoEnum configPacket;
    /**
     * 显示金额
     */
    private YesOrNoEnum configAmount;
    /**
     * 二维码
     */
    private YesOrNoEnum configScan;
    /**
     * 红包禁抢
     */
    private YesOrNoEnum configReceive;
    /**
     * 隐私开关
     */
    private YesOrNoEnum privacyNo;
    /**
     * 隐私开关
     */
    private YesOrNoEnum privacyScan;
    /**
     * 群组容量
     */
    private Long levelCount;
    /**
     * 成员数
     */
    private Long memberCount;
    /**
     * 创建时间
     */
    private Date createTime;

}
