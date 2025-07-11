package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.platform.common.utils.VersionUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.common.web.vo.LabelVo;
import com.platform.modules.chat.dao.ChatUserLogDao;
import com.platform.modules.chat.domain.ChatUserLog;
import com.platform.modules.chat.enums.UserLogEnum;
import com.platform.modules.chat.service.ChatUserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户日志 服务层实现
 * </p>
 */
@Service("chatUserLogService")
public class ChatUserLogServiceImpl extends BaseServiceImpl<ChatUserLog> implements ChatUserLogService {

    @Resource
    private ChatUserLogDao chatUserLogDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatUserLogDao);
    }

    @Override
    public List<ChatUserLog> queryList(ChatUserLog t) {
        List<ChatUserLog> dataList = chatUserLogDao.queryList(t);
        return dataList;
    }

    @Override
    public void addLog(Long userId, UserLogEnum logType) {
        ChatUserLog log = new ChatUserLog(userId)
                .setLogType(logType)
                .setContent(logType.getInfo())
                .setIp("-")
                .setIpAddr("-")
                .setDeviceType("-")
                .setDeviceVersion("-")
                .setCreateTime(DateUtil.date());
        this.add(log);
    }

    @Override
    public Long visit() {
        Date beginTime = DateUtil.beginOfDay(DateUtil.date());
        QueryWrapper<ChatUserLog> wrapper = new QueryWrapper<>();
        wrapper.select("COUNT(DISTINCT user_id) AS 'count'");
        wrapper.gt("create_time", beginTime);
        List<ChatUserLog> dataList = this.queryList(wrapper);
        // 集合判空
        if (CollectionUtils.isEmpty(dataList)) {
            return 0L;
        }
        return dataList.get(0).getCount();
    }

    @Override
    public List<LabelVo> version() {
        QueryWrapper<ChatUserLog> wrapper = new QueryWrapper<>();
        wrapper.select("device_version, COUNT(DISTINCT user_id) AS 'count'");
        wrapper.groupBy("device_version");
        List<ChatUserLog> logsList = this.queryList(wrapper);
        // 集合判空
        if (CollectionUtils.isEmpty(logsList)) {
            return new ArrayList<>();
        }
        List<LabelVo> dataList = logsList.stream().collect(ArrayList::new, (x, y) -> {
            x.add(new LabelVo("-".equals(y.getDeviceVersion()) ? "1.0.0" : y.getDeviceVersion(), y.getCount()));
        }, ArrayList::addAll);
        // 逆序
        Collections.sort(dataList, (a, b) -> VersionUtils.versionStrToNum(b.getLabel()) - VersionUtils.versionStrToNum(a.getLabel()));
        LabelVo labelVo = new LabelVo("其他", 0L);
        dataList = dataList.stream().collect(ArrayList::new, (list, data) -> {
            if (list.size() < 3) {
                list.add(new LabelVo(data.getLabel(), data.getValue()));
            } else {
                labelVo.setValue(NumberUtil.toStr(NumberUtil.parseLong(labelVo.getValue()) + NumberUtil.parseLong(data.getValue())));
            }
        }, ArrayList::addAll);
        dataList.add(labelVo);
        return dataList;
    }

    @Override
    public List<LabelVo> device() {
        QueryWrapper<ChatUserLog> wrapper = new QueryWrapper<>();
        wrapper.select("device_type, COUNT(DISTINCT user_id) AS 'count'");
        wrapper.groupBy("device_type");
        List<ChatUserLog> dataList = this.queryList(wrapper);
        // 集合判空
        if (CollectionUtils.isEmpty(dataList)) {
            return new ArrayList<>();
        }
        return dataList.stream().collect(ArrayList::new, (x, y) -> {
            x.add(new LabelVo(y.getDeviceType(), y.getCount()));
        }, ArrayList::addAll);
    }

    @Override
    public PageInfo queryDataList(Long userId) {
        // 查询
        List<ChatUserLog> dataList = this.queryList(new ChatUserLog(userId));
        // 转换
        List<Dict> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            Dict dict = Dict.create().parseBean(y)
                    .filter(ChatUserLog.LABEL_LOG_TYPE
                            , ChatUserLog.LABEL_CONTENT
                            , ChatUserLog.LABEL_CREATE_TIME
                    )
                    .set(ChatUserLog.LABEL_LOG_ID, y.getId())
                    .set(ChatUserLog.LABEL_LOG_TYPE_LABEL, y.getLogType().getInfo());
            x.add(dict);
        }, ArrayList::addAll);
        return getPageInfo(dictList, dataList);
    }

}
