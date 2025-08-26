package com.platform.modules.operate.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.redis.RedisJsonUtil;
import com.platform.common.redis.RedisUtils;
import com.platform.modules.chat.domain.ChatConfig;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.service.ChatConfigService;
import com.platform.modules.operate.service.OperateNotifyService;
import com.platform.modules.operate.vo.OperateVo01;
import com.platform.modules.push.dto.PushSetting;
import com.platform.modules.push.enums.PushSettingEnum;
import com.platform.modules.push.service.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

// 导入 FastJSON 核心类
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

/**
 * <p>
 * 首页公告 服务层实现
 * </p>
 */
@Slf4j
@Service("operateNotifyService")
public class OperateNotifyServiceImpl implements OperateNotifyService {

    @Resource
    private ChatConfigService chatConfigService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private RedisJsonUtil redisJsonUtil;

    @Resource
    private PushService pushService;

    @Override
    public OperateVo01 getInfo() {
        // 查询
        Map<ChatConfigEnum, ChatConfig> configMap = chatConfigService.queryConfig();
        // 转换
        return new OperateVo01()
                .setContent(configMap.get(ChatConfigEnum.NOTICE_CONTENT).getStr())
                .setNotype(configMap.get(ChatConfigEnum.NOTICE_NOTYPE).getInt())
                .setStatus(configMap.get(ChatConfigEnum.NOTICE_STATUS).getYesOrNo());
    }

    @Transactional
    @Override
    public void updateNotify(OperateVo01 operateVo) {
        YesOrNoEnum status = operateVo.getStatus();
        String content = operateVo.getContent();
        int notype=operateVo.getNotype();
        // 更新
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.NOTICE_STATUS).setValue(status));
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.NOTICE_CONTENT).setValue(content));
        chatConfigService.updateById(new ChatConfig().setConfigKey(ChatConfigEnum.NOTICE_NOTYPE).setValue(notype));
        // 开启
        if (YesOrNoEnum.YES.equals(status)) {
            redisUtils.set(AppConstants.REDIS_CHAT_NOTICE, content, 30, TimeUnit.DAYS);
        }
        // 关闭
        else {
            redisUtils.delete(AppConstants.REDIS_CHAT_NOTICE);
        }
        //清除前端缓存
        String key=AppConstants.REDIS_COMMON_CONFIG+"one";
        String REDIS_KEY = AppConstants.REDIS_COMMON_CONFIG+"all";
        log.info("key: {}",key);
        redisJsonUtil.delete(key);
        redisJsonUtil.delete(REDIS_KEY);
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
        int notype=operateVo.getNotype();
        // 定义Map，键为字符串，值为Object（兼容数字1和字符串content）
        Map<String, Object> pushdata = new HashMap<>();
        // 添加键值对：notype对应1，content对应content变量
        pushdata.put("notype", notype);
        pushdata.put("content", content);
        // 2. FastJSON 转为 JSON 字符串（核心步骤）
        String jsonData;
        try {
            // 直接调用 JSON.toJSONString() 完成序列化
            jsonData = JSON.toJSONString(pushdata);
        } catch (JSONException e) {
            // 处理 JSON 序列化异常（如 Map 中存在无法序列化的特殊对象）
            e.printStackTrace(); // 实际项目建议用日志框架记录（如 log.info/log.error）
            return; // 序列化失败时，终止推送逻辑（按需调整业务逻辑）
        }

        // 3. 将 JSON 字符串传入推送服务
        pushService.pushSetting(new PushSetting(PushSettingEnum.SYS, "notice", jsonData));
    }

}
