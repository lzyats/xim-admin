package com.platform.modules.monitor.service;

import com.github.pagehelper.PageInfo;

/**
 * 在线用户 服务层
 */
public interface MonitorOnlineService {

    /**
     * 查询在线用户
     */
    PageInfo queryDataList(boolean isFilter);

    /**
     * 强退
     */
    void logout(String tokenId);

}
