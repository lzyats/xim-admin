package com.platform.modules.home.service.impl;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.common.web.vo.LabelVo;
import com.platform.modules.chat.service.ChatUserLogService;
import com.platform.modules.home.service.HomeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 首页统计 服务层实现
 * </p>
 */
@Service("homeService")
public class HomeServiceImpl extends BaseServiceImpl implements HomeService {

    @Resource
    private ChatUserLogService chatUserLogService;

    @Override
    public List<LabelVo> userVersion() {
        return chatUserLogService.version();
    }

    @Override
    public List<LabelVo> userDevice() {
        return chatUserLogService.device();
    }

}
