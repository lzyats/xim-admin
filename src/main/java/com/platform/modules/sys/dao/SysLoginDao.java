package com.platform.modules.sys.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.sys.domain.SysLogin;

import java.util.List;

/**
 * 系统访问日志情况信息 数据层
 */
public interface SysLoginDao extends BaseDao<SysLogin> {

    /**
     * 查询系统登录日志集合
     */
    List<SysLogin> queryList(SysLogin sysLogin);

}
