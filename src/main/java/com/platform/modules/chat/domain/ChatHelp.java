package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>
 * 聊天帮助实体类
 * </p>
 */
@Data
@TableName("chat_help")
@Accessors(chain = true) // 链式调用
public class ChatHelp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long helpId;
    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    @Size(max = 50, message = "标题不能超过50个字符")
    private String title;
    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空")
    @Size(max = 2000, message = "内容不能超过2000个字符")
    private String content;
    /**
     * 排序
     */
    @Max(value = 9999, message = "排序不能超过9999")
    private Integer sort;

}
