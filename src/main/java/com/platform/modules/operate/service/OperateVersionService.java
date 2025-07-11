package com.platform.modules.operate.service;

import com.platform.modules.operate.vo.OperateVo02;

/**
 * <p>
 * 升级版本 服务层
 * </p>
 */
public interface OperateVersionService {

    /**
     * 详情
     */
    OperateVo02 getInfo();

    /**
     * 修改
     */
    void update(OperateVo02 operateVo);

}
