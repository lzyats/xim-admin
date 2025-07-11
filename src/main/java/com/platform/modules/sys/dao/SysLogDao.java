package com.platform.modules.sys.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.sys.domain.SysLog;

import java.util.List;

/**
 * 操作日志 数据层
 */
public interface SysLogDao extends BaseDao<SysLog> {

    /**
     * 查询系统操作日志集合
     */
    List<SysLog> queryList(SysLog sysLog);

}
