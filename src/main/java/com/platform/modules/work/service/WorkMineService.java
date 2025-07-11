package com.platform.modules.work.service;

import com.platform.modules.work.vo.WorkVo04;

/**
 * <p>
 * 我的 服务层
 * </p>
 */
public interface WorkMineService {

    /**
     * 获取基本信息
     */
    WorkVo04 getInfo();

    /**
     * 刷新在线
     */
    void refresh(String cid);

}
