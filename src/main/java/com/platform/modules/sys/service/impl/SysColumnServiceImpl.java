package com.platform.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.sys.dao.SysColumnDao;
import com.platform.modules.sys.domain.SysColumn;
import com.platform.modules.sys.service.SysColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 动态表格 服务层实现
 * </p>
 */
@Service("sysColumnService")
public class SysColumnServiceImpl extends BaseServiceImpl<SysColumn> implements SysColumnService {

    @Resource
    private SysColumnDao sysColumnDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(sysColumnDao);
    }

    @Override
    public List<SysColumn> queryList(SysColumn t) {
        List<SysColumn> dataList = sysColumnDao.queryList(t);
        return dataList;
    }

    @Override
    public String getTable(Integer tableId) {
        Long userId = ShiroUtils.getUserId();
        SysColumn sysColumn = this.queryOne(new SysColumn().setUserId(userId).setTableId(tableId));
        if (sysColumn == null) {
            return null;
        }
        return sysColumn.getContent();
    }

    @Transactional
    @Override
    public void editTable(SysColumn sysColumn) {
        // 删除
        this.deleteTable(sysColumn.getTableId());
        // 新增
        if (!StringUtils.isEmpty(sysColumn.getContent())) {
            sysColumn.setUserId(ShiroUtils.getUserId());
            this.add(sysColumn);
        }
    }

    @Override
    public void deleteTable(Integer tableId) {
        Long userId = ShiroUtils.getUserId();
        this.delete(Wrappers.<SysColumn>lambdaUpdate()
                .eq(SysColumn::getUserId, userId)
                .eq(SysColumn::getTableId, tableId));
    }

}
