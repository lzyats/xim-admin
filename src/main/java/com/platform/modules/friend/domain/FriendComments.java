package com.platform.modules.friend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.platform.common.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 朋友圈评论表实体类
 * </p>
 */
@Data
@TableName("friend_comments")
@Accessors(chain = true) // 链式调用
public class FriendComments extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    @TableId
    private Long commentId;
    /**
     * 关联动态ID
     */
    private Long momentId;
    /**
     * 评论用户ID
     */
    private Long userId;
    /**
     * 回复的评论ID（可为空）
     */
    private Long replyTo;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 逻辑删除标记
     */
    private Integer isDeleted;

}
