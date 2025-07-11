package com.platform.modules.chat.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Accessors(chain = true) // 链式调用
public class ChatVo05 {

    /**
     * 群组id
     */
    @NotNull(message = "群组id不能为空")
    private Long groupId;

    /**
     * 群组人数
     */
    @NotNull(message = "群组人数不能为空")
    private Integer levelCount;

}
