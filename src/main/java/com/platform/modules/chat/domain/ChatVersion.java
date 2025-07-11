package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.enums.DeviceEnum;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * <p>
 * 版本实体类
 * </p>
 */
@Data
@TableName("chat_version")
@Accessors(chain = true) // 链式调用
public class ChatVersion extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 版本
     */
    @NotBlank(message = "版本不能为空")
    @Size(max = 20, message = "版本不能超过20个字符")
    private String version;
    /**
     * 设备
     */
    private DeviceEnum device;
    /**
     * 设备
     */
    @TableField(exist = false)
    private String deviceLabel;
    /**
     * 地址
     */
    @NotBlank(message = "地址不能为空")
    @Size(max = 2000, message = "地址不能超过2000个字符")
    private String url;
    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空")
    @Size(max = 2000, message = "内容不能超过2000个字符")
    private String content;

    public String getDeviceLabel() {
        if (device != null) {
            switch (device) {
                case ANDROID:
                    deviceLabel = "安卓";
                    break;
                case IOS:
                    deviceLabel = "苹果";
                    break;
                case WINDOWS:
                    deviceLabel = "windows";
                    break;
                case MAC:
                    deviceLabel = "mac";
                    break;
                default:
                    deviceLabel = "未知";
                    break;
            }
        }
        return deviceLabel;
    }
}
