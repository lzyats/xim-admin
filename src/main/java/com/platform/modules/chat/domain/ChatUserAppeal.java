package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 用户申请实体类
 * </p>
 */
@Data
@TableName("chat_user_appeal")
@NoArgsConstructor
@Accessors(chain = true) // 链式调用
public class ChatUserAppeal extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long userId;
    /**
     * 图片
     */
    private String images;
    /**
     * 内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Date createTime;

    public ChatUserAppeal(Long userId) {
        this.userId = userId;
    }

    /**
     * 字段
     */
    public static final String LABEL_IMAGES = "images";
    public static final String LABEL_CONTENT = "content";

}
