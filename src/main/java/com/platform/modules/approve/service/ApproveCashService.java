package com.platform.modules.approve.service;

import cn.hutool.core.lang.Dict;
import com.alibaba.excel.ExcelWriter;
import com.github.pagehelper.PageInfo;
import com.platform.modules.approve.vo.ApproveVo00;
import com.platform.modules.approve.vo.ApproveVo01;

/**
 * 提现审批 业务层
 */
public interface ApproveCashService {

    /**
     * 查询审批列表
     */
    PageInfo queryDataList(ApproveVo00 approveVo);

    /**
     * 查询审批详情
     */
    Dict getInfo(Long tradeId);

    /**
     * 审批
     */
    void auth(ApproveVo01 approveVo);

    /**
     * 导出
     */
    void export(ExcelWriter excelWriter);
}
