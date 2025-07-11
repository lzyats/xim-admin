package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.common.web.domain.JsonDateDeserializer;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * <p>
 * 通知公告实体类
 * </p>
 */
@Data
@TableName("chat_notice")
@Accessors(chain = true) // 链式调用
public class ChatNotice extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long noticeId;
    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    @Size(max = 20, message = "标题不能超过20个字符")
    private String title;
    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空")
    @Size(max = 2000, message = "内容不能超过2000个字符")
    private String content;
    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    private YesOrNoEnum status;
    /**
     * 发布时间
     */
    @NotNull(message = "发布时间不能为空")
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date createTime;

}
