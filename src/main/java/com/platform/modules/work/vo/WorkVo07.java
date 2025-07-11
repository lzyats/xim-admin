package com.platform.modules.work.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true) // 链式调用
@NoArgsConstructor
public class WorkVo07 {

    /**
     * 主键
     */
    private Long userId;
    /**
     * 禁用标志
     */
    private YesOrNoEnum banned;
    /**
     * 封禁原因
     */
    private String reason;
    /**
     * 封禁时间
     */
    private Date bannedTime;

    public WorkVo07(Long userId) {
        this.userId = userId;
        this.banned = YesOrNoEnum.NO;
    }

    public WorkVo07(Long userId, String reason, Date bannedTime) {
        this.userId = userId;
        this.banned = YesOrNoEnum.YES;
        this.reason = reason;
        this.bannedTime = bannedTime;
    }
}
