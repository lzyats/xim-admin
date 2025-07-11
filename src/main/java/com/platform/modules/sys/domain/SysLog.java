package com.platform.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.common.web.domain.JsonDateDeserializer;
import lombok.Data;

import java.util.Date;

/**
 * 操作日志记录表
 */
@Data
@TableName("sys_log")
public class SysLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 日志主键
     */
    @TableId
    private Long logId;

    /**
     * 操作模块
     */
    private String title;

    /**
     * 业务类型
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LogTypeEnum logType;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 请求url
     */
    private String requestUrl;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 操作人员
     */
    private String username;

    /**
     * 操作地址
     */
    private String ipAddr;

    /**
     * 操作地点
     */
    private String location;

    /**
     * 返回参数
     */
    private String message;

    /**
     * 操作状态（Y正常 N异常）
     */
    private YesOrNoEnum status;

    /**
     * 操作时间
     */
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date createTime;

}
