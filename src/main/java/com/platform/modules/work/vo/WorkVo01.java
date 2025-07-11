package com.platform.modules.work.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true) // 链式调用
public class WorkVo01 {

    @NotNull(message = "群聊id不能为空")
    private Long groupId;

    @NotNull(message = "封禁类型不能为空")
    private YesOrNoEnum banned;

}
