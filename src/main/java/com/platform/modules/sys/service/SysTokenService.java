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
package com.platform.modules.sys.service;

import com.platform.common.shiro.ShiroUserVo;
import com.platform.modules.sys.vo.LoginVo02;

/**
 * <p>
 * token 服务层
 * </p>
 */
public interface SysTokenService {

    /**
     * 生成token
     */
    LoginVo02 generateToken();

    /**
     * 通过token查询
     */
    ShiroUserVo queryToken(String token);

    /**
     * 刷新
     */
    void refresh(ShiroUserVo userVo);

    /**
     * 删除token
     */
    void deleteToken(String token);

}
