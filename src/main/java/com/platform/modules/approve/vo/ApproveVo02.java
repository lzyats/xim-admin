package com.platform.modules.approve.vo;

import cn.hutool.core.util.StrUtil;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.validation.ValidateGroup;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true) // 链式调用
public class ApproveVo02 {

    /**
     * 用户ID
     */
    @NotNull(message = "用户id不能为空")
    private Long userId;
    /**
     * 审批状态
     */
    @NotNull(message = "审批状态不能为空")
    private YesOrNoEnum status;
    /**
     * 用户姓名
     */
    @NotBlank(message = "用户姓名不能为空")
    @Size(max = 20, message = "用户姓名不能大于20")
    private String name;
    /**
     * 身份证号
     */
    @NotBlank(message = "身份证号不能为空")
    @Size(max = 20, message = "身份证号不能大于50")
    private String idCard;
    /**
     * 拒绝原因
     */
    @NotBlank(message = "拒绝原因不能为空", groups = ValidateGroup.ONE.class)
    @Size(max = 50, message = "拒绝原因不能大于50", groups = ValidateGroup.ONE.class)
    private String reason;

    public void setName(String name) {

        this.name = StrUtil.trim(name);
    }

    public void setIdCard(String idCard) {
        this.idCard = StrUtil.trim(idCard);
    }

    public void setReason(String reason) {
        this.reason = StrUtil.trim(reason);
    }

}
