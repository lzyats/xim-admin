package com.platform.modules.operate.service;

import com.platform.modules.operate.vo.OperateVo05;

/**
 * <p>
 * 群组扩容 服务层
 * </p>
 */
public interface OperateGroupService {

    /**
     * 详情
     */
    OperateVo05 getInfo();

    /**
     * 修改
     */
    void update(OperateVo05 operateVo);

}
