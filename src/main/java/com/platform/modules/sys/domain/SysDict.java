package com.platform.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * <p>
 * 字典数据实体类
 * </p>
 */
@Data
@TableName("sys_dict")
@Accessors(chain = true) // 链式调用
public class SysDict extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long dictId;
    /**
     * 字典名称
     */
    @NotBlank(message = "字典名称不能为空")
    @Size(max = 100, message = "字典名称长度不能超过100个字符")
    private String dictName;
    /**
     * 字典类型
     */
    private String dictType;
    /**
     * 字典编码
     */
    @NotBlank(message = "字典编码不能为空")
    @Size(max = 100, message = "字典编码长度不能超过100个字符")
    private String dictCode;
    /**
     * 字典排序
     */
    @Max(value = 9999, message = "排序不能超过9999")
    private Integer dictSort;
    /**
     * 备注
     */
    @Size(max = 100, message = "备注长度不能超过100个字符")
    private String remark;

}
