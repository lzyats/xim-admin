package com.platform.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 动态表格实体类
 * </p>
 */
@Data
@TableName("sys_column")
@Accessors(chain = true) // 链式调用
public class SysColumn extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 表格ID
     */
    @TableId
    private Long columnId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 字段ID
     */
    private Integer tableId;
    /**
     * 表格内容
     */
    private String content;

}
