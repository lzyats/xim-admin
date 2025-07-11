package com.platform.modules.operate.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true) // 链式调用
public class OperateVo05 {

    /**
     * 默认人数
     */
    @NotNull(message = "默认人数不能为空")
    @Min(value = 1, message = "默认人数不能小于1")
    @Max(value = 9999, message = "默认人数不能超过9999")
    private Integer count;

    /**
     * 搜索开关
     */
    @NotNull(message = "搜索开关不能为空")
    private YesOrNoEnum search;

}
