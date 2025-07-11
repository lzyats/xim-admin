package com.platform.modules.sys.service.impl;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.sys.dao.SysLogDao;
import com.platform.modules.sys.domain.SysLog;
import com.platform.modules.sys.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 操作日志 服务层处理
 */
@Service("sysLogService")
public class SysLogServiceImpl extends BaseServiceImpl<SysLog> implements SysLogService {

    @Resource
    private SysLogDao sysLogDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(sysLogDao);
    }

    /**
     * 查询系统操作日志集合
     */
    @Override
    public List<SysLog> queryList(SysLog sysLog) {
        return sysLogDao.queryList(sysLog);
    }

}
