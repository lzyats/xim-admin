package com.platform.modules.operate.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.JsonDecimalDeserializer;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@Accessors(chain = true) // 链式调用
public class OperateVo06 {

    /**
     * 申请好友次数
     */
    @NotNull(message = "申请好友次数不能为空")
    @Min(value = 1, message = "申请好友次数不能小于1")
    @Max(value = 9999, message = "申请好友次数不能超过9999")
    private Integer applyFriend;
    /**
     * 申请群组次数
     */
    @NotNull(message = "申请群组次数不能为空")
    @Min(value = 1, message = "申请群组次数不能小于1")
    @Max(value = 9999, message = "申请群组次数不能超过9999")
    private Integer applyGroup;
    /**
     * 用户注销间隔
     */
    @NotNull(message = "用户注销间隔不能为空")
    @Min(value = 1, message = "用户注销间隔不能小于1")
    @Max(value = 9999, message = "用户注销间隔不能超过9999")
    private Integer deleted;
    /**
     * 用户注册开关
     */
    @NotNull(message = "用户注册开关不能为空")
    private YesOrNoEnum register;
    /**
     * 用户手持
     */
    @NotNull(message = "用户手持开关不能为空")
    private YesOrNoEnum holdCard;
    /**
     * 用户短信开关
     */
    @NotNull(message = "用户短信开关不能为空")
    private YesOrNoEnum sms;
    /**
     * 系统名称
     */
    @NotBlank(message = "系统名称不能为空")
    @Size(max = 20, message = "系统名称不能超过20个字符")
    private String project;
    /**
     * 备案信息
     */
    @Size(max = 40, message = "备案信息不能超过40个字符")
    private String beian;
    /**
     * 注册昵称
     */
    @Size(max = 40, message = "注册昵称不能超过40个字符")
    private String nickname;
    /**
     * 系统截屏
     */
    @NotNull(message = "系统截屏开关不能为空")
    private YesOrNoEnum screenshot;
    /**
     * 系统水印
     */
    private String watermark;
    /**
     * 登录验证码
     */
    @NotBlank(message = "登录验证码不能为空")
    @Pattern(regexp = "[0-9]{4}", message = "登录验证码格式不正确")
    private String captcha;
    /**
     * 红包金额
     */
    @Digits(integer = 8, fraction = 2, message = "红包金额格式不正确")
    @DecimalMin(value = "0.01", message = "红包金额不能小于0.01")
    @DecimalMax(value = "999999.00", message = "红包金额不能大于999999.00")
    @NotNull(message = "红包金额不能为空")
    @JsonDeserialize(using = JsonDecimalDeserializer.class)
    private BigDecimal packet;
    /**
     * 分享页面
     */
    @NotBlank(message = "分享页面下载地址不能为空")
    private String share;
    /**
     * webHook地址
     */
    private String hook;
    /**
     * 消息撤回时间
     */
    @NotNull(message = "撤回时间不能为空")
    @Min(value = 1, message = "撤回时间不能小于1")
    @Max(value = 9999, message = "撤回时间不能超过999999")
    private Integer recall;
    /**
     * 推荐奖励
     */
    @Digits(integer = 8, fraction = 2, message = "推荐奖励格式不正确")
    @DecimalMin(value = "0.01", message = "推荐奖励不能小于0.01")
    @DecimalMax(value = "999999.00", message = "推荐奖励不能大于999999.00")
    @NotNull(message = "推荐奖励不能为空")
    private BigDecimal invo;

    /**
     * 推荐加好友开关
     */
    @NotNull(message = " 推荐加好友开关")
    private YesOrNoEnum invoadus;
    /**
     * 签到奖励
     */
    @Digits(integer = 8, fraction = 2, message = "签到奖励格式不正确")
    @DecimalMin(value = "0.01", message = "签到奖励不能小于0.01")
    @DecimalMax(value = "999999.00", message = "签到奖励不能大于999999.00")
    @NotNull(message = "签到奖励不能为空")
    private BigDecimal sign;

    /**
     * 用户签到开关
     */
    @NotNull(message = " 签到奖励开关不能为空")
    private YesOrNoEnum signtoal;

    /**
     * 补发朋友圈开关
     */
    @NotNull(message = " 补发朋友圈开关不能为空")
    private YesOrNoEnum sendmoment;

    /**
     * 补发朋友圈开关
     */
    private String friends;

}
