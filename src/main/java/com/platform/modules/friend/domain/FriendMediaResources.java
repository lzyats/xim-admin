package com.platform.modules.friend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.platform.common.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 朋友圈媒体资源表实体类
 * </p>
 */
@Data
@TableName("friend_media_resources")
@Accessors(chain = true) // 链式调用
public class FriendMediaResources extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 媒体资源ID
     */
    @TableId
    private Long mediaId;
    /**
     * 关联动态ID
     */
    private Long momentId;
    /**
     * 事件ID
     */
    private Long momid;
    /**
     * 资源URL
     */
    private String url;
    /**
     * 缩略图
     */
    private String thumbnail;
    /**
     * 类型：0-图片，1-视频，2-音频
     */
    private Integer type;
    /**
     * 排序顺序
     */
    private Integer sortOrder;
    /**
     * 宽度（图片/视频）
     */
    private Integer width;
    /**
     * 高度（图片/视频）
     */
    private Integer height;
    /**
     * 时长（视频/音频，单位：秒）
     */
    private Integer duration;
    /**
     * 创建时间
     */
    private Date createTime;

}
