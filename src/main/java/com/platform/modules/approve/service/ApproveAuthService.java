package com.platform.modules.approve.service;

import cn.hutool.core.lang.Dict;
import com.github.pagehelper.PageInfo;
import com.platform.modules.approve.vo.ApproveVo00;
import com.platform.modules.approve.vo.ApproveVo02;

/**
 * 认证审批 业务层
 */
public interface ApproveAuthService {

    /**
     * 查询认证列表
     */
    PageInfo queryDataList(ApproveVo00 approveVo);

    /**
     * 查询认证详情
     */
    Dict getInfo(Long userId);

    /**
     * 审批
     */
    void auth(ApproveVo02 approveVo);

}
