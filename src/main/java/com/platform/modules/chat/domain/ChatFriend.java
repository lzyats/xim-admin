package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.chat.enums.FriendSourceEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * <p>
 * 好友表实体类
 * </p>
 */
@Data
@TableName("chat_friend")
@NoArgsConstructor
@Accessors(chain = true) // 链式调用
public class ChatFriend extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long friendId;
    /**
     * 当前id
     */
    private Long currentId;
    /**
     * 群组id
     */
    private Long groupId;
    /**
     * 用户id
     */
    private Long userId;
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
     * 聊天号码
     */
    private String userNo;
    /**
     * 申请来源
     */
    private FriendSourceEnum source;
    /**
     * 是否黑名单
     */
    private YesOrNoEnum black;
    /**
     * 是否静默
     */
    private YesOrNoEnum disturb;
    /**
     * 是否置顶
     */
    private YesOrNoEnum top;
    /**
     * 创建时间
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date createTime;

    public String getDefaultRemark() {
        if (StringUtils.isEmpty(remark)) {
            return nickname;
        }
        return remark;
    }

    public ChatFriend(Long currentId) {
        this.currentId = currentId;
    }

    /**
     * 字段
     */
    public static final String LABEL_GROUP_ID = "groupId";
    public static final String LABEL_USER_ID = "userId";
    public static final String LABEL_USER_NO = "userNo";
    public static final String LABEL_NICKNAME = "nickname";
    public static final String LABEL_REMARK = "remark";
    public static final String LABEL_PORTRAIT = "portrait";
    public static final String LABEL_SOURCE = "source";
    public static final String LABEL_CREATE_TIME = "createTime";

}
