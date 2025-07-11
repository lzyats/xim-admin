package com.platform.modules.extend.service;

import com.platform.modules.extend.domain.UniItem;
import com.platform.common.web.service.BaseService;

/**
 * <p>
 * 小程序表 服务层
 * </p>
 */
public interface UniItemService extends BaseService<UniItem> {

    /**
     * 新增
     */
    void addItem(UniItem item);

    /**
     * 修改
     */
    void editItem(UniItem item);
}
