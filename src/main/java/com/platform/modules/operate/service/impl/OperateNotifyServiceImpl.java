package com.platform.modules.operate.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.redis.RedisUtils;
import com.platform.modules.chat.domain.ChatConfig;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.service.ChatConfigService;
import com.platform.modules.operate.service.OperateNotifyService;
import com.platform.modules.operate.vo.OperateVo01;
import com.platform.modules.push.dto.PushSetting;
import com.platform.modules.push.enums.PushSettingEnum;
import com.platform.modules.push.service.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 首页公告 服务层实现
 * </p>
 */
@Service("operateNotifyService")
public class OperateNotifyServiceImpl implements OperateNotifyService {

    @Resource
    private ChatConfigService chatConfigService;

    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private PushService pushService;

    @Override
    public OperateVo01 getInfo() {
        // 查询
        Map<ChatConfigEnum, ChatConfig> configMap = chatConfigService.queryConfig();
        // 转换
        return new OperateVo01()
                .setContent(configMap.get(ChatConfigEnum.NOTICE_CONTENT).getStr())
                .setStatus(configMap.get(ChatConfigEnum.NOTICE_STATUS).getYesOrNo());
    }

    @Transactional
    @Override
    public void updateNotify(OperateVo01 operateVo) {
        YesOrNoEnum status = operateVo.getStatus();
        String content = operateVo.getContent();
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.NOTICE_STATUS).setValue(status));
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.NOTICE_CONTENT).setValue(content));
        // 开启
        if (YesOrNoEnum.YES.equals(status)) {
            redisUtils.set(AppConstants.REDIS_CHAT_NOTICE, content, 30, TimeUnit.DAYS);
        }
        // 关闭
        else {
            redisUtils.delete(AppConstants.REDIS_CHAT_NOTICE);
        }
    }

    @Override
    public void pushNotify(OperateVo01 operateVo) {
        // 修改
        this.updateNotify(operateVo);
        // 推送
        this.pushSetting(operateVo);
    }

    @Override
    public List<String> getDemo() {
        String now = DateUtil.format(DateUtil.date(), DatePattern.CHINESE_DATE_PATTERN);
        List<String> dataList = new ArrayList<>();
        dataList.add(StrUtil.format("停机更新：系统将于{} 09:00~12:00进行服务升级", now));
        dataList.add(StrUtil.format("不停机更新：系统将于{} 09:00~12:00进行服务升级", now));
        dataList.add("重要通知：近期诈骗犯罪案件时有发生，为防止您在经济上蒙受损失，请您接到陌生人或以熟人名义要求转账、汇款时，务必提高警惕，以防受骗");
        dataList.add("新版功能更新上线，欢迎升级体验");
        return dataList;
    }

    /**
     * 推送
     */
    private void pushSetting(OperateVo01 operateVo) {
        YesOrNoEnum status = operateVo.getStatus();
        if (YesOrNoEnum.NO.equals(status)) {
            return;
        }
        String content = operateVo.getContent();
        pushService.pushSetting(new PushSetting(PushSettingEnum.SYS, "notice", content));
    }

}
