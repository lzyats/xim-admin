package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 群组实体类
 * </p>
 */
@Data
@TableName("chat_group")
@NoArgsConstructor
@Accessors(chain = true) // 链式调用
public class ChatGroup extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long groupId;
    /**
     * 群组名称
     */
    private String groupName;
    /**
     * 群组编号
     */
    private String groupNo;
    /**
     * 群组封禁
     */
    private YesOrNoEnum banned;
    /**
     * 群组头像
     */
    private String portrait;
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
     * 群组等级
     */
    private Integer groupLevel;
    /**
     * 群组容量
     */
    private Integer groupLevelCount;
    /**
     * 群组价格
     */
    private BigDecimal groupLevelPrice;
    /**
     * 群组容量时间
     */
    private Date groupLevelTime;
    /**
     * 隐私开关
     */
    private YesOrNoEnum privacyNo;
    /**
     * 隐私开关
     */
    private YesOrNoEnum privacyScan;
    /**
     * 隐私开关
     */
    private YesOrNoEnum privacyName;
    /**
     * 状态
     */
    @TableField(exist = false)
    private YesOrNoEnum status;
    /**
     * 成员数
     */
    @TableField(exist = false)
    private Long memberCount;
    /**
     * 创建时间
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date createTime;
    /**
     * 注销0正常null注销
     */
    private Integer deleted;

    public ChatGroup(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * 字段
     */
    public static final String LABEL_GROUP_ID = "groupId";
    public static final String LABEL_GROUP_NAME = "groupName";
    public static final String LABEL_GROUP_NO = "groupNo";
    public static final String LABEL_PORTRAIT = "portrait";
    public static final String LABEL_MEMBER_COUNT = "memberCount";
    public static final String LABEL_LEVEL_COUNT = "levelCount";
    public static final String LABEL_BANNED = "banned";
    public static final String LABEL_BANNED_LABEL = "bannedLabel";
    public static final String LABEL_CREATE_TIME = "createTime";
    public static final String LABEL_STATUS = "status";
    public static final String LABEL_STATUS_LABEL = "statusLabel";
    public static final String LABEL_NOTICE = "notice";
    public static final String LABEL_NOTICE_TOP = "noticeTop";
    public static final String LABEL_CONFIG_MEMBER = "configMember";
    public static final String LABEL_CONFIG_INVITE = "configInvite";
    public static final String LABEL_CONFIG_SPEAK = "configSpeak";
    public static final String LABEL_CONFIG_TITLE = "configTitle";
    public static final String LABEL_CONFIG_AUDIT = "configAudit";
    public static final String LABEL_CONFIG_MEDIA = "configMedia";
    public static final String LABEL_CONFIG_ASSIGN = "configAssign";
    public static final String LABEL_CONFIG_NICKNAME = "configNickname";
    public static final String LABEL_CONFIG_PACKET = "configPacket";
    public static final String LABEL_CONFIG_AMOUNT = "configAmount";
    public static final String LABEL_CONFIG_SCAN = "configScan";
    public static final String LABEL_CONFIG_RECEIVE = "configReceive";
    public static final String LABEL_PRIVACY_NO = "privacyNo";
    public static final String LABEL_PRIVACY_SCAN = "privacyScan";
    public static final String LABEL_PRIVACY_NAME = "privacyName";
    /**
     * 字段
     */
    public static final String COLUMN_DELETED = "deleted";

}
