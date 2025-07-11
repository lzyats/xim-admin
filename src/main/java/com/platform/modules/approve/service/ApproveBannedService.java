package com.platform.modules.approve.service;

import cn.hutool.core.lang.Dict;
import com.github.pagehelper.PageInfo;
import com.platform.modules.approve.vo.ApproveVo00;
import com.platform.modules.approve.vo.ApproveVo03;

/**
 * 解封审批 业务层
 */
public interface ApproveBannedService {

    /**
     * 查询审批列表
     */
    PageInfo queryDataList(ApproveVo00 approveVo);

    /**
     * 查询审批详情
     */
    Dict getInfo(Long userId);

    /**
     * 审批
     */
    void auth(ApproveVo03 approveVo);

}
