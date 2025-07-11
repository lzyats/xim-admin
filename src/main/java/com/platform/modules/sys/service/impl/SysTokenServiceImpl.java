/**
 * Licensed to the Apache Software Foundation （ASF） under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * （the "License"）； you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.platform.modules.sys.service.impl;

import cn.hutool.json.JSONUtil;
import com.platform.common.config.PlatformConfig;
import com.platform.common.constant.HeadConstant;
import com.platform.common.enums.ResultEnum;
import com.platform.common.exception.LoginException;
import com.platform.common.redis.RedisUtils;
import com.platform.common.shiro.ShiroUserVo;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.utils.CodeUtils;
import com.platform.modules.sys.service.SysTokenService;
import com.platform.modules.sys.vo.LoginVo02;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * token 服务层
 */
@Service("sysTokenService")
public class SysTokenServiceImpl implements SysTokenService {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public LoginVo02 generateToken() {
        ShiroUserVo shiroUserVo = ShiroUtils.getLoginUser();
        redisUtils.set(makeToken(shiroUserVo.getToken()), JSONUtil.toJsonStr(shiroUserVo), PlatformConfig.TIMEOUT, TimeUnit.HOURS);
        return new LoginVo02(shiroUserVo.getToken());
    }

    @Override
    public ShiroUserVo queryToken(String token) {
        String redisKey = this.makeToken(token);
        boolean result;
        try {
            result = redisUtils.hasKey(redisKey);
        } catch (Exception e) {
            throw new LoginException(ResultEnum.FAIL);
        }
        if (!result) {
            return null;
        }
        // 加密盐
        String salt = CodeUtils.salt();
        // 转换
        return JSONUtil.toBean(redisUtils.get(redisKey), ShiroUserVo.class)
                .setSalt(salt)
                .setCredentials(CodeUtils.credentials(token, salt));
    }

    @Override
    public void refresh(ShiroUserVo userVo) {
        // 条件
        String lastId = userVo.getLastId();
        Set<String> perms = userVo.getPermissions();
        // 更新
        ShiroUserVo shiroUserVo = ShiroUtils.getLoginUser();
        if (!StringUtils.isEmpty(lastId)) {
            shiroUserVo.setLastId(lastId);
        }
        if (perms != null) {
            shiroUserVo.setPermissions(perms);
        }
        redisUtils.set(makeToken(shiroUserVo.getToken()), JSONUtil.toJsonStr(shiroUserVo), PlatformConfig.TIMEOUT, TimeUnit.HOURS);
    }

    @Override
    public void deleteToken(String token) {
        redisUtils.delete(makeToken(token));
    }

    /**
     * 组装token
     */
    private String makeToken(String token) {
        return HeadConstant.TOKEN_REDIS_ADMIN + token;
    }

}
