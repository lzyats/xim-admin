package com.platform.modules.extend.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.validation.ValidateGroup;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.extend.enums.UniTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>
 * 小程序表实体类
 * </p>
 */
@Data
@TableName("uni_item")
@Accessors(chain = true) // 链式调用
public class UniItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long uniId;
    /**
     * appId
     */
    @NotBlank(message = "appId不能为空", groups = ValidateGroup.ONE.class)
    @Size(max = 20, message = "appId长度不能大于20", groups = ValidateGroup.ONE.class)
    private String appId;
    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    @Size(max = 20, message = "名称长度不能大于20")
    private String name;
    /**
     * 图标
     */
    @NotBlank(message = "图标不能为空")
    @Size(max = 200, message = "图标长度不能大于200")
    private String icon;
    /**
     * 版本
     */
    @NotNull(message = "版本不能为空", groups = ValidateGroup.ONE.class)
    private Long version;
    /**
     * 地址
     */
    @NotBlank(message = "地址不能为空")
    @Size(max = 200, message = "地址长度不能大于200")
    private String path;
    /**
     * 类型
     */
    @NotNull(message = "类型不能为空")
    private UniTypeEnum type;
    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    private YesOrNoEnum status;

}
