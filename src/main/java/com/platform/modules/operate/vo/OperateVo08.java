package com.platform.modules.operate.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.platform.common.web.domain.JsonDateDeserializer;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Accessors(chain = true) // 链式调用
public class OperateVo08 {

    /**
     * 执行时间
     */
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @NotNull(message = "执行时间不能为空")
    private Date createTime;

}
