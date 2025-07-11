package com.platform.modules.operate.service;

import com.platform.modules.operate.vo.OperateVo03;

/**
 * <p>
 * 充值管理 服务层
 * </p>
 */
public interface OperateRechargeService {

    /**
     * 详情
     */
    OperateVo03 getInfo();

    /**
     * 修改
     */
    void update(OperateVo03 operateVo);

}
