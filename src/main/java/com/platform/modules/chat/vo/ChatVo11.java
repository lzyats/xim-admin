package com.platform.modules.chat.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Accessors(chain = true) // 链式调用
public class ChatVo11 {

    /**
     * 群组id
     */
    @NotNull(message = "群组id不能为空")
    private Long groupId;
    /**
     * 群组名称
     */
    @NotBlank(message = "群组名称不能为空")
    @Size(max = 15, message = "群组名称不能大于50")
    private String groupName;
    /**
     * 群组头像
     */
    @NotBlank(message = "群组头像不能为空")
    @Size(max = 200, message = "群组头像不能大于200")
    private String portrait;
    /**
     *
     * 公告
     */
    @Size(max = 200, message = "公告不能大于200")
    private String notice;
    /**
     * 公告置顶
     */
    @NotNull(message = "公告置顶不能为空")
    private YesOrNoEnum noticeTop;
    /**
     * 成员保护
     */
    @NotNull(message = "成员保护不能为空")
    private YesOrNoEnum configMember;
    /**
     * 允许邀请
     */
    @NotNull(message = "允许邀请不能为空")
    private YesOrNoEnum configInvite;
    /**
     * 全员禁言Y=禁言N=不禁言
     */
    @NotNull(message = "全员禁言不能为空")
    private YesOrNoEnum configSpeak;
    /**
     * 群组头衔
     */
    @NotNull(message = "群组头衔不能为空")
    private YesOrNoEnum configTitle;
    /**
     * 审核开关
     */
    @NotNull(message = "审核开关不能为空")
    private YesOrNoEnum configAudit;
    /**
     * 发送资源
     */
    @NotNull(message = "发送资源不能为空")
    private YesOrNoEnum configMedia;
    /**
     * 专属可见
     */
    @NotNull(message = "专属可见不能为空")
    private YesOrNoEnum configAssign;
    /**
     * 昵称开关
     */
    @NotNull(message = "昵称开关不能为空")
    private YesOrNoEnum configNickname;
    /**
     * 红包开关
     */
    @NotNull(message = "红包开关不能为空")
    private YesOrNoEnum configPacket;
    /**
     * 显示金额
     */
    @NotNull(message = "显示金额不能为空")
    private YesOrNoEnum configAmount;
    /**
     * 二维码
     */
    @NotNull(message = "二维码不能为空")
    private YesOrNoEnum configScan;
    /**
     * 红包禁抢
     */
    @NotNull(message = "红包禁抢不能为空")
    private YesOrNoEnum configReceive;
    /**
     * 隐私开关
     */
    @NotNull(message = "隐私开关不能为空")
    private YesOrNoEnum privacyNo;
    /**
     * 隐私开关
     */
    @NotNull(message = "隐私开关不能为空")
    private YesOrNoEnum privacyScan;
    /**
     * 隐私开关
     */
    @NotNull(message = "隐私开关不能为空")
    private YesOrNoEnum privacyName;

}
