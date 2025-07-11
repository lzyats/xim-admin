package com.platform.modules.operate.service;

import com.platform.modules.operate.vo.OperateVo04;

/**
 * <p>
 * 提现管理 服务层
 * </p>
 */
public interface OperateCashService {

    /**
     * 详情
     */
    OperateVo04 getInfo();

    /**
     * 修改
     */
    void update(OperateVo04 operateVo);

}
