package com.platform.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.platform.common.exception.BaseException;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.common.web.vo.LabelVo;
import com.platform.modules.sys.dao.SysDictDao;
import com.platform.modules.sys.domain.SysDict;
import com.platform.modules.sys.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 字典数据 服务层实现
 * </p>
 */
@Service("sysDictService")
public class SysDictServiceImpl extends BaseServiceImpl<SysDict> implements SysDictService {

    @Resource
    private SysDictDao sysDictDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(sysDictDao);
    }

    @Override
    public List<SysDict> queryList(SysDict t) {
        List<SysDict> dataList = sysDictDao.queryList(t);
        return dataList;
    }

    @Override
    public void addDict(SysDict sysDict) {
        try {
            this.add(sysDict);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            throw new BaseException("编码不能重复");
        }
    }

    @Transactional
    @Override
    public void editDict(SysDict sysDict) {
        // 查询
        SysDict oldDict = this.getById(sysDict.getDictId());
        if (oldDict == null) {
            return;
        }
        // 主字典
        if (StringUtils.isEmpty(oldDict.getDictType())) {
            this.update(Wrappers.<SysDict>lambdaUpdate()
                    .set(SysDict::getDictType, sysDict.getDictCode())
                    .eq(SysDict::getDictType, oldDict.getDictCode()));
        }
        try {
            this.updateById(sysDict);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            throw new BaseException("编码不能重复");
        }
    }

    @Override
    public void deleteDict(Long dictId) {
        // 查询
        SysDict sysDict = this.getById(dictId);
        if (sysDict == null) {
            return;
        }
        // 校验
        if (StringUtils.isEmpty(sysDict.getDictType())) {
            if (this.queryCount(new SysDict().setDictType(sysDict.getDictCode())) > 0) {
                throw new BaseException("存在子字典,不允许删除");
            }
        }
        // 删除
        this.deleteById(dictId);
    }

    @Override
    public List<LabelVo> queryLabel(String dictType) {
        List<SysDict> dataList = this.queryList(new SysDict().setDictType(dictType));
        // list转Obj
        return dataList.stream().collect(ArrayList::new, (x, y) -> {
            x.add(new LabelVo(y.getDictName(), y.getDictCode()));
        }, ArrayList::addAll);
    }

    @Override
    public List<SysDict> queryDict(String dictType) {
        return this.queryList(new SysDict().setDictType(dictType));
    }

}
