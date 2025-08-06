package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.enums.GenderEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.chat.enums.ChatTalkEnum;
import com.platform.modules.push.dto.PushSync;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 用户表实体类
 * </p>
 */
@Data
@TableName("chat_user")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class ChatUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
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
     * 昵称
     */
    private String nickname;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 头像
     */
    private String portrait;
    /**
     * 备注
     */
    private String remark;
    /**
     * 性别1男2女
     */
    private GenderEnum gender;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 盐
     */
    private String salt;
    /**
     * 密码
     */
    private String password;
    /**
     * 密码标志
     */
    private YesOrNoEnum pass;
    /**
     * 禁用标志
     */
    private YesOrNoEnum banned;
    /**
     * 特殊
     */
    private YesOrNoEnum special;
    /**
     * 异常(Y异常N正常R忽略)
     */
    private YesOrNoEnum abnormal;
    /**
     * 支付密码
     */
    private YesOrNoEnum payment;
    /**
     * 认证状态
     */
    private ApproveEnum auth;
    /**
     * 隐私no
     */
    private YesOrNoEnum privacyNo;
    /**
     * 隐私账号
     */
    private YesOrNoEnum privacyPhone;
    /**
     * 隐私扫码
     */
    private YesOrNoEnum privacyScan;
    /**
     * 隐私名片
     */
    private YesOrNoEnum privacyCard;
    /**
     * 隐私群组
     */
    private YesOrNoEnum privacyGroup;
    /**
     * 微信id
     */
    private String loginWx;
    /**
     * 苹果id
     */
    private String loginIos;
    /**
     * qq
     */
    private String loginQq;
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
     * 余额
     */
    @TableField(exist = false)
    private BigDecimal balance;
    /**
     * 状态
     */
    @TableField(exist = false)
    private YesOrNoEnum status;
    /**
     * 注册时间
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date createTime;
    /**
     * 审核时间
     */
    @TableField(exist = false)
    private Date authTime;
    /**
     * 身份姓名
     */
    @TableField(exist = false)
    private String name;
    /**
     * 身份号码
     */
    @TableField(exist = false)
    private String idCard;
    /**
     * 注销0正常null注销
     */
    private Integer deleted;

    /**
     * 用户层级
     */
    private Integer userDep;

    /**
     * 层级关系表
     */
    private String userLevel;

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 邀请码
     */
    private String incode;

    /**
     * 是否VIP，0普通1VIP
     */
    private Integer isvip;

    public ChatUser(Long userId) {
        this.userId = userId;
    }

    /**
     * 字段
     */
    public static final String LABEL_USER_ID = "userId";
    public static final String LABEL_USER_NO = "userNo";
    public static final String LABEL_PHONE = "phone";
    public static final String LABEL_NICKNAME = "nickname";
    public static final String LABEL_PORTRAIT = "portrait";
    public static final String LABEL_REMARK = "remark";
    public static final String LABEL_AUTH = "auth";
    public static final String LABEL_AUTH_LABEL = "authLabel";
    public static final String LABEL_BANNED = "banned";
    public static final String LABEL_BANNED_LABEL = "bannedLabel";
    public static final String LABEL_SPECIAL = "special";
    public static final String LABEL_CREATE_TIME = "createTime";
    public static final String LABEL_ON_LINE = "onLine";
    public static final String LABEL_IP = "ip";
    public static final String LABEL_IP_ADDR = "ipAddr";
    public static final String LABEL_EMAIL = "email";
    public static final String LABEL_STATUS = "status";
    public static final String LABEL_STATUS_LABEL = "statusLabel";
    public static final String LABEL_BALANCE = "balance";
    public static final String LABEL_AUTH_NAME = "name";
    public static final String LABEL_ID_CARD = "idCard";
    public static final String LABEL_AUTH_COUNT = "authCount";
    /**
     * 字段
     */
    public static final String COLUMN_DELETED = "deleted";
    public static final String COLUMN_BANNED = "banned";
    public static final String COLUMN_ABNORMAL = "abnormal";
    public static final String COLUMN_IP = "ip";
    public static final String COLUMN_EMAIL = "email";
    public static final String LABEL_ISVIP= "isvip";
    public static final String LABEL_USER_DEP= "userDep";
    public static final String LABEL_USER_LEVEL = "userLevel";
    public static final String LABEL_PARENT_ID= "parentId";

    public PushSync getPushSync() {
        return new PushSync()
                .setUserId(userId)
                .setNickname(nickname)
                .setPortrait(portrait)
                .setChatTalk(ChatTalkEnum.ROBOT.getType());
    }

}
