package com.platform.modules.work.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true) // 链式调用
public class WorkVo09 {

    /**
     * 主键
     */
    private Long groupId;
    /**
     * 群ID
     */
    private String groupNo;
    /**
     * 群名
     */
    private String groupName;
    /**
     * 头像
     */
    private String portrait;
    /**
     * 注册时间
     */
    private Date createTime;

}
