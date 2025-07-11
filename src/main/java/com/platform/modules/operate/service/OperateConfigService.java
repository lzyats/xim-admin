package com.platform.modules.operate.service;

import com.platform.modules.operate.vo.OperateVo06;

/**
 * <p>
 * 配置中心 服务层
 * </p>
 */
public interface OperateConfigService {

    /**
     * 详情
     */
    OperateVo06 getInfo();

    /**
     * 修改
     */
    void update(OperateVo06 operateVo);

}
