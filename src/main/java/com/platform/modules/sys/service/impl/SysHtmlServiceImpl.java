package com.platform.modules.sys.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.sys.service.SysHtmlService;
import com.platform.modules.sys.dao.SysHtmlDao;
import com.platform.modules.sys.domain.SysHtml;

/**
 * <p>
 * APP网页定制 服务层实现
 * </p>
 */
@Service("sysHtmlService")
public class SysHtmlServiceImpl extends BaseServiceImpl<SysHtml> implements SysHtmlService {

    @Resource
    private SysHtmlDao sysHtmlDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(sysHtmlDao);
    }

    @Override
    public List<SysHtml> queryList(SysHtml t) {
        List<SysHtml> dataList = sysHtmlDao.queryList(t);
        return dataList;
    }

}
