package com.platform.modules.sys.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色对象
 */
@Data
public class SysRoleVo {
    /**
     * 角色ID
     */
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    /**
     * 权限
     */
    private List<Long> prems;

}
