package com.platform.modules.chat.vo;

import com.platform.common.validation.ValidateGroup;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Accessors(chain = true) // 链式调用
public class ChatVo03 {

    /**
     * 名称
     */
    @NotBlank(message = "主菜单名称不能为空")
    @NotBlank(message = "主菜单名称不能为空", groups = ValidateGroup.ONE.class)
    @NotBlank(message = "子菜单名称不能为空", groups = ValidateGroup.TWO.class)
    private String name;
    /**
     * 类型
     */
    @NotBlank(message = "主菜单类型不能为空")
    @NotBlank(message = "主菜单类型不能为空", groups = ValidateGroup.ONE.class)
    @NotBlank(message = "子菜单类型不能为空", groups = ValidateGroup.TWO.class)
    private String type;
    /**
     * 数据
     */
    @NotBlank(message = "主菜单数据不能为空")
    @NotBlank(message = "子菜单数据不能为空", groups = ValidateGroup.TWO.class)
    private String value;
    /**
     * 子菜单
     */
    private List<ChatVo03> children;

}
