package com.platform.modules.home.service;

import com.platform.common.web.service.BaseService;
import com.platform.common.web.vo.LabelVo;

import java.util.List;

public interface HomeService extends BaseService {

    /**
     * 用户版本
     */
    List<LabelVo> userVersion();

    /**
     * 用户设备
     */
    List<LabelVo> userDevice();

}
