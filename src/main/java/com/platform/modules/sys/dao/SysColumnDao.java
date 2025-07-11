package com.platform.modules.sys.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.sys.domain.SysColumn;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 动态表格 数据库访问层
 * </p>
 */
@Repository
public interface SysColumnDao extends BaseDao<SysColumn> {

    /**
     * 查询列表
     */
    List<SysColumn> queryList(SysColumn sysColumn);

}
