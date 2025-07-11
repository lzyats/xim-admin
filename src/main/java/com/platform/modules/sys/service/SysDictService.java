package com.platform.modules.sys.service;

import com.platform.common.web.service.BaseService;
import com.platform.common.web.vo.LabelVo;
import com.platform.modules.sys.domain.SysDict;

import java.util.List;

/**
 * <p>
 * 字典数据 服务层
 * </p>
 */
public interface SysDictService extends BaseService<SysDict> {

    /**
     * 新增
     */
    void addDict(SysDict sysDict);

    /**
     * 修改
     */
    void editDict(SysDict sysDict);

    /**
     * 删除
     */
    void deleteDict(Long dictId);

    /**
     * 查询字典
     */
    List<LabelVo> queryLabel(String dictType);

    /**
     * 查询字典
     */
    List<SysDict> queryDict(String dictType);
}
