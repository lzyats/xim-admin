package com.platform.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.common.web.domain.JsonDateDeserializer;
import lombok.Data;

import java.util.Date;

/**
 * 系统访问记录表
 */
@Data
@TableName("sys_login")
public class SysLogin extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId
    private Long logId;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 登录状态
     */
    private YesOrNoEnum status;

    /**
     * 登录IP地址
     */
    private String ipAddr;

    /**
     * 登录地点
     */
    private String location;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 提示消息
     */
    private String message;

    /**
     * 操作时间
     */
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date createTime;

}