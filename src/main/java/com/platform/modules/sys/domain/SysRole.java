package com.platform.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 角色表 sys_role
 */
@Data
@TableName("sys_role")
@Accessors(chain = true) // 链式调用
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableId
    private Long roleId;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 30, message = "角色名称长度不能超过30个字符")
    private String roleName;

    /**
     * 角色权限
     */
    @NotBlank(message = "权限字符不能为空")
    @Size(max = 100, message = "权限字符长度不能超过100个字符")
    private String roleKey;

    /**
     * 角色排序
     */
    @Max(value = 9999, message = "排序不能超过9999")
    private Integer roleSort;

    /**
     * 角色状态（Y正常 N停用）
     */
    @NotNull(message = "角色状态不能为空")
    private YesOrNoEnum status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 菜单组
     */
    @TableField(exist = false)
    @JsonProperty
    private List<Long> perms;

    public static SysRole initPlatform() {
        String name = "平台管理员";
        return new SysRole()
                .setRoleId(0L)
                .setRoleName(name)
                .setRoleKey("super")
                .setStatus(YesOrNoEnum.YES)
                .setRemark(name);
    }

}
