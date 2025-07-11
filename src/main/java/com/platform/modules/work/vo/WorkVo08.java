package com.platform.modules.work.vo;

import com.platform.modules.chat.enums.BannedTimeEnum;
import com.platform.modules.chat.enums.BannedTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true) // 链式调用
public class WorkVo08 {

    @NotNull(message = "用户id不能为空")
    private Long userId;

    @NotNull(message = "封禁类型不能为空")
    private BannedTypeEnum bannedType;

    @NotBlank(message = "封禁原因不能为空")
    @Size(max = 50, message = "封禁原因不能大于50")
    private String bannedReason;

    @NotNull(message = "封禁时长不能为空")
    private BannedTimeEnum bannedTime;

}
