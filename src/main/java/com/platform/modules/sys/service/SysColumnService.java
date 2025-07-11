package com.platform.modules.sys.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.sys.domain.SysColumn;

/**
 * <p>
 * 动态表格 服务层
 * </p>
 */
public interface SysColumnService extends BaseService<SysColumn> {

    /**
     * 查询详情
     */
    String getTable(Integer tableId);

    /**
     * 修改详情
     */
    void editTable(SysColumn sysColumn);

    /**
     * 删除详情
     */
    void deleteTable(Integer tableId);
}
