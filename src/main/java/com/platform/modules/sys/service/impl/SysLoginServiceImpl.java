package com.platform.modules.sys.service.impl;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.sys.dao.SysLoginDao;
import com.platform.modules.sys.domain.SysLogin;
import com.platform.modules.sys.service.SysLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统访问日志情况信息 服务层处理
 */
@Service("sysLoginService")
public class SysLoginServiceImpl extends BaseServiceImpl<SysLogin> implements SysLoginService {

    @Resource
    private SysLoginDao sysLoginDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(sysLoginDao);
    }

    /**
     * 查询系统登录日志集合
     */
    @Override
    public List<SysLogin> queryList(SysLogin sysLogin) {
        return sysLoginDao.queryList(sysLogin);
    }

}
