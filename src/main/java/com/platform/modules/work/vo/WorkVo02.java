package com.platform.modules.work.vo;

import cn.hutool.json.JSONObject;
import com.platform.modules.push.enums.PushMsgTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true) // 链式调用
public class WorkVo02 {

    @NotNull(message = "用户不能为空")
    private Long userId;

    @NotNull(message = "消息类型不能为空")
    private PushMsgTypeEnum msgType;

    @NotNull(message = "消息内容不能为空")
    private JSONObject content;

}
