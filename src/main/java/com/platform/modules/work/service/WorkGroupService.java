package com.platform.modules.work.service;

import com.platform.modules.work.vo.WorkVo09;
import com.platform.modules.work.vo.WorkVo10;

import java.util.List;

/**
 * <p>
 * 群组 服务层
 * </p>
 */
public interface WorkGroupService {

    /**
     * 获取列表
     */
    List<WorkVo09> getDataList(String param);

    /**
     * 获取详情
     */
    WorkVo10 getInfo(Long groupId);

}
