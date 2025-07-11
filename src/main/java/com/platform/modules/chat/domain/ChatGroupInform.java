package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.chat.enums.BannedTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 骚扰举报实体类
 * </p>
 */
@Data
@TableName("chat_group_inform")
@NoArgsConstructor
@Accessors(chain = true) // 链式调用
public class ChatGroupInform extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long informId;
    /**
     * 类型
     */
    private BannedTypeEnum informType;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户no
     */
    @TableField(exist = false)
    private String userNo;
    /**
     * 用户no
     */
    @TableField(exist = false)
    private String nickname;
    /**
     * 群组id
     */
    private Long groupId;
    /**
     * 群组no
     */
    @TableField(exist = false)
    private String groupNo;
    /**
     * 群组名称
     */
    @TableField(exist = false)
    private String groupName;
    /**
     * 图片
     */
    private String images;
    /**
     * 内容
     */
    private String content;
    /**
     * 处理状态
     */
    private YesOrNoEnum status;
    /**
     * 创建时间
     */
    private Date createTime;

    public ChatGroupInform(Long informId) {
        this.informId = informId;
    }

    /**
     * 字段
     */
    public static final String LABEL_INFORM_ID = "informId";
    public static final String LABEL_INFORM_TYPE = "informType";
    public static final String LABEL_INFORM_TYPE_LABEL = "informTypeLabel";
    public static final String LABEL_USER_ID = "userId";
    public static final String LABEL_USER_NO = "userNo";
    public static final String LABEL_NICKNAME = "nickname";
    public static final String LABEL_TARGET_ID = "targetId";
    public static final String LABEL_TARGET_NO = "targetNo";
    public static final String LABEL_TARGET_NAME = "targetName";
    public static final String LABEL_IMAGES = "images";
    public static final String LABEL_CONTENT = "content";
    public static final String LABEL_STATUS = "status";
    public static final String LABEL_STATUS_LABEL = "statusLabel";
    public static final String LABEL_CREATE_TIME = "createTime";
}
