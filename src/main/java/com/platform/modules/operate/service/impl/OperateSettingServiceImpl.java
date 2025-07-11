package com.platform.modules.operate.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.platform.modules.chat.domain.ChatMsg;
import com.platform.modules.chat.domain.ChatResource;
import com.platform.modules.chat.service.ChatMsgService;
import com.platform.modules.chat.service.ChatResourceService;
import com.platform.modules.operate.service.OperateSettingService;
import com.platform.modules.push.utils.RedisOther;
import com.platform.modules.wallet.domain.WalletPacket;
import com.platform.modules.wallet.domain.WalletTrade;
import com.platform.modules.wallet.service.WalletPacketService;
import com.platform.modules.wallet.service.WalletTradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 数据中心 服务层实现
 * </p>
 */
@Slf4j
@Service("operateSettingService")
public class OperateSettingServiceImpl implements OperateSettingService {

    @Resource
    private ChatResourceService chatResourceService;

    @Resource
    private ChatMsgService chatMsgService;

    @Resource
    private WalletTradeService walletTradeService;

    @Resource
    private WalletPacketService walletPacketService;

    @Autowired
    private RedisOther redisOther;

    @Override
    public String cleanPortrait(Date createTime) {
        // 查询列表
        QueryWrapper<ChatResource> wrapper = new QueryWrapper();
        // 设置时间
        wrapper.lt("create_time", createTime);
        // 执行分页
        PageHelper.startPage(1, 10000);
        List<ChatResource> dataList = chatResourceService.queryList(wrapper);
        // 结果集合
        String result = "清理资源：{}条";
        // 集合判空
        if (CollectionUtils.isEmpty(dataList)) {
            return StrUtil.format(result, 0);
        }
        // 删除文件
        chatResourceService.batchDelete(dataList);
        // 返回结果
        return StrUtil.format(result, dataList.size());
    }

    @Override
    public String cleanMessage(Date createTime) {
        // 清理消息
        DateTime now = DateUtil.date();
        for (int i = 1; i < 12; i++) {
            String date = DateUtil.format(DateUtil.offsetMonth(now, -i), DatePattern.SIMPLE_MONTH_PATTERN);
            redisOther.delete(StrUtil.format("push:user:{}{}", date, "*"));
        }
        // 查询列表
        QueryWrapper<ChatMsg> wrapper = new QueryWrapper();
        // 设置时间
        wrapper.lt("create_time", createTime);
        // 结果集合
        int result = chatMsgService.delete(wrapper);
        // 返回结果
        return StrUtil.format("清理消息：{}条", result);
    }

    @Override
    public String cleanTrade(Date createTime) {
        // 制定时间
        createTime = DateUtil.lastMonth();
        // 查询列表
        QueryWrapper<WalletTrade> wrapper1 = new QueryWrapper();
        // 设置时间
        wrapper1.lt("create_time", createTime);
        // 结果集合
        int result = walletTradeService.delete(wrapper1);
        // 查询列表
        QueryWrapper<WalletPacket> wrapper2 = new QueryWrapper();
        // 设置时间
        wrapper2.lt("create_time", createTime);
        // 结果集合
        walletPacketService.delete(wrapper2);
        // 返回结果
        return StrUtil.format("清理交易：{}条", result);
    }

}
