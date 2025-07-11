package com.platform.modules.work.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true) // 链式调用
@NoArgsConstructor
public class WorkVo12 {

    /**
     * 用户ID
     */
    @NotNull(message = "用户id不能为空")
    private Long userId;
    /**
     * 处理状态
     */
    @NotNull(message = "处理状态不能为空")
    private YesOrNoEnum status;

}
