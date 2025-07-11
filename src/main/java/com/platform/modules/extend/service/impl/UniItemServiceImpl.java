package com.platform.modules.extend.service.impl;

import com.platform.common.exception.BaseException;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.extend.dao.UniItemDao;
import com.platform.modules.extend.domain.UniItem;
import com.platform.modules.extend.service.UniItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 小程序表 服务层实现
 * </p>
 */
@Service("uniItemService")
public class UniItemServiceImpl extends BaseServiceImpl<UniItem> implements UniItemService {

    @Resource
    private UniItemDao uniItemDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(uniItemDao);
    }

    @Override
    public List<UniItem> queryList(UniItem t) {
        List<UniItem> dataList = uniItemDao.queryList(t);
        return dataList;
    }

    @Override
    public void addItem(UniItem item) {
        try {
            this.add(item);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            throw new BaseException("appId不能重复");
        }
    }

    @Override
    public void editItem(UniItem item) {
        item.setAppId(null);
        item.setType(null);
        this.updateById(item);
    }
}
