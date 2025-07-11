package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.chat.enums.GroupMemberEnum;
import com.platform.modules.chat.enums.GroupSourceEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * <p>
 * 实体类
 * </p>
 */
@Data
@TableName("chat_group_member")
@Accessors(chain = true) // 链式调用
@NoArgsConstructor
public class ChatGroupMember extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long memberId;
    /**
     * 群组id
     */
    private Long groupId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 聊天号码
     */
    private String userNo;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String portrait;
    /**
     * 用户备注
     */
    private String remark;
    /**
     * 成员类型
     */
    private GroupMemberEnum memberType;
    /**
     * 是否置顶
     */
    private YesOrNoEnum top;
    /**
     * 是否免打扰
     */
    private YesOrNoEnum disturb;
    /**
     * 来源
     */
    private GroupSourceEnum memberSource;
    /**
     * 接收白名单，Y=白名单
     */
    private YesOrNoEnum packetWhite;
    /**
     * 禁言开关
     */
    private YesOrNoEnum speak;
    /**
     * 禁言时间
     */
    private Date speakTime;
    /**
     * 加入时间
     */
    private Date createTime;
    /**
     * 注销0正常null注销
     */
    private Integer deleted;

    public String getDefaultRemark() {
        if (StringUtils.isEmpty(remark)) {
            return nickname;
        }
        return remark;
    }

    public ChatGroupMember(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * 字段
     */
    public static final String LABEL_USER_ID = "userId";
    public static final String LABEL_USER_NO = "userNo";
    public static final String LABEL_NICKNAME = "nickname";
    public static final String LABEL_PORTRAIT = "portrait";
    public static final String LABEL_MEMBER_TYPE = "memberType";
    public static final String LABEL_MEMBER_TYPE_LABEL = "memberTypeLabel";
    public static final String LABEL_TOP = "top";
    public static final String LABEL_TOP_LABEL = "topLabel";
    public static final String LABEL_DISTURB = "disturb";
    public static final String LABEL_DISTURB_LABEL = "disturbLabel";
    public static final String LABEL_SOURCE = "source";
    public static final String LABEL_CREATE_TIME = "createTime";
    /**
     * 字段
     */
    public static final String COLUMN_GROUP_ID = "group_id";

}
