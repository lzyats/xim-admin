package com.platform.modules.monitor.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.platform.common.constant.AppConstants;
import com.platform.common.constant.HeadConstant;
import com.platform.common.redis.RedisUtils;
import com.platform.common.shiro.ShiroUserVo;
import com.platform.common.web.page.PageDomain;
import com.platform.common.web.page.TableSupport;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.monitor.domain.MonitorOnline;
import com.platform.modules.monitor.service.MonitorOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * 在线用户 服务层处理
 */
@Service("monitorOnlineService")
public class MonitorOnlineServiceImpl extends BaseServiceImpl<String> implements MonitorOnlineService {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public PageInfo queryDataList(boolean isFilter) {
        // 分页对象
        PageDomain pageDomain = TableSupport.getPageDomain();
        Integer pageStart = pageDomain.getPageStart();
        Integer pageEnd = pageDomain.getPageEnd();
        Collection<String> keys = redisUtils.keys(HeadConstant.TOKEN_REDIS_ADMIN + "*");
        List<MonitorOnline> dataList = new ArrayList<>();
        // 制作分页
        List<String> redisKeys = CollUtil.sub(CollUtil.sort(keys, Comparator.naturalOrder()), pageStart, pageEnd);
        if (!CollectionUtils.isEmpty(redisKeys)) {
            List<String> redisValues = redisUtils.multiGet(redisKeys);
            for (String redisValue : redisValues) {
                MonitorOnline online = format(redisValue, isFilter);
                if (online != null) {
                    dataList.add(online);
                }
            }
        }
        return getPageInfo(dataList, keys.size());
    }

    /**
     * 格式化用户信息
     */
    private MonitorOnline format(String redisValue, boolean isFilter) {
        if (StringUtils.isEmpty(redisValue)) {
            return null;
        }
        ShiroUserVo shiroUserVo = JSONUtil.toBean(redisValue, ShiroUserVo.class);
        if (shiroUserVo == null) {
            return null;
        }
        if (isFilter && AppConstants.USERNAME.equals(shiroUserVo.getUsername())) {
            return null;
        }
        MonitorOnline online = new MonitorOnline();
        online.setToken(shiroUserVo.getToken());
        online.setUsername(shiroUserVo.getUsername());
        online.setIpAddr(shiroUserVo.getIpAddr());
        online.setLocation(shiroUserVo.getLocation());
        online.setBrowser(shiroUserVo.getBrowser());
        online.setOs(shiroUserVo.getOs());
        online.setLoginTime(DateUtil.date(shiroUserVo.getLoginTime() * 1000));
        return online;
    }

    @Override
    public void logout(String tokenId) {
        redisUtils.delete(HeadConstant.TOKEN_REDIS_ADMIN + tokenId);
    }
}
