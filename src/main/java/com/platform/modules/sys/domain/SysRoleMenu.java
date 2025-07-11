package com.platform.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 角色和菜单关联 sys_role_menu
 */
@Data
@TableName("sys_role_menu")
@Accessors(chain = true) // 链式调用
public class SysRoleMenu extends BaseEntity {
    /**
     * 角色ID
     */
    @TableId
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;

}
