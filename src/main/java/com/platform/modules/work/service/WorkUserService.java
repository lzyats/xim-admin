package com.platform.modules.work.service;

import com.platform.modules.work.vo.WorkVo05;
import com.platform.modules.work.vo.WorkVo06;
import com.platform.modules.work.vo.WorkVo07;
import com.platform.modules.work.vo.WorkVo11;

import java.util.List;

/**
 * <p>
 * 用户 服务层
 * </p>
 */
public interface WorkUserService {

    /**
     * 获取列表
     */
    List<WorkVo05> getDataList(String param);

    /**
     * 获取详情
     */
    WorkVo06 getInfo(Long userId);

    /**
     * 查询封禁
     */
    WorkVo07 getBanned(Long userId);

    /**
     * 认证详情
     */
    WorkVo11 getAuth(Long userId);

}
