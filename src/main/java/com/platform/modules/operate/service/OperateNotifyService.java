package com.platform.modules.operate.service;

import com.platform.modules.operate.vo.OperateVo01;

import java.util.List;

/**
 * <p>
 * 首页公告 服务层
 * </p>
 */
public interface OperateNotifyService {

    /**
     * 详情
     */
    OperateVo01 getInfo();

    /**
     * 修改
     */
    void updateNotify(OperateVo01 operateVo);

    /**
     * 推送
     */
    void pushNotify(OperateVo01 operateVo);

    /**
     * Demo
     */
    List<String> getDemo();

}
