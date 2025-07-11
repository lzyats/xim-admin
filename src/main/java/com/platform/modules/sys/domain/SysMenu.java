package com.platform.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.sys.enums.SysMenuTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 菜单权限表 sys_menu
 */
@Data
@TableName("sys_menu")
@Accessors(chain = true) // 链式调用
public class SysMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long menuId;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 50, message = "菜单名称长度不能超过50个字符")
    private String menuName;

    /**
     * 类型（M目录 C菜单 F按钮）
     */
    @NotNull(message = "菜单类型不能为空")
    private SysMenuTypeEnum menuType;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 路由地址
     */
    @Size(max = 200, message = "路由地址不能超过200个字符")
    private String path;

    /**
     * 组件路径
     */
    @Size(max = 200, message = "组件路径不能超过255个字符")
    private String component;

    /**
     * 权限字符串
     */
    @Size(max = 200, message = "权限标识长度不能超过200个字符")
    private String perms;

    /**
     * 显示顺序
     */
    @Max(value = 9999, message = "排序不能超过9999")
    private Integer sort;

    /**
     * 外链
     */
    private YesOrNoEnum frameFlag;

    /**
     * 外链地址
     */
    @Size(max = 200, message = "权限标识长度不能超过100个字符")
    private String frameUrl;

    /**
     * 菜单状态
     */
    private YesOrNoEnum status;

    /**
     * 显示状态
     */
    private YesOrNoEnum visible;

    /**
     * 角色id
     */
    @TableField(exist = false)
    private Long roleId;

}
