package com.platform.modules.chat.service;

import cn.hutool.core.lang.Dict;
import com.github.pagehelper.PageInfo;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.vo.ChatVo01;
import com.platform.modules.chat.vo.ChatVo06;

/**
 * <p>
 * 用户表 服务层
 * </p>
 */
public interface ChatUserService extends BaseService<ChatUser> {

    /**
     * 列表
     */
    PageInfo queryDataList(ChatUser chatUser);

    /**
     * 详情
     */
    Dict getInfo(Long userId);

    /**
     * 详情
     */
    ChatUser getByPhone(String phone);

    /**
     * 认证详情
     */
    Dict getAuth(Long userId);

    /**
     * 取消认证
     */
    void cancelAuth(Long userId);

    /**
     * 封禁用户
     */
    void banned(ChatVo01 chatVo);

    /**
     * 备注
     */
    void setRemark(Long userId, String remark);

    /**
     * 邮箱
     */
    void setEmail(Long userId, String email);

    /**
     * 查询用户
     */
    ChatUser queryCache(Long userId);

    /**
     * 查询认证列表
     */
    PageInfo queryAuthList(ChatUser chatUser);

    /**
     * 审批
     */
    void approveAuth(Long userId, String name, String idCard, ApproveEnum auth, String reason);

    /**
     * 查询封禁列表
     */
    PageInfo queryBannedList(ChatUser chatUser);

    /**
     * 查询特殊列表
     */
    PageInfo querySpecialList();

    /**
     * 查询特殊数量
     */
    void querySpecialCount();

    /**
     * 审批
     */
    void approveAbnormal(Long userId, YesOrNoEnum status);

    /**
     * 查询封禁详情
     */
    Dict queryBannedInfo(Long userId);

    /**
     * 审批
     */
    void approveBanned(Long userId, YesOrNoEnum status);

    /**
     * 新增
     */
    void addUser(ChatVo06 chatVo);

    /**
     * 用户注销
     */
    void deleted(Long userId);

    /**
     * 重置密码
     */
    Dict resetPwd(Long userId);

    /**
     * 重置支付密码
     */
    Dict resetPay(Long userId);

    /**
     * 重置特殊
     */
    void resetSpecial(Long userId, YesOrNoEnum special);

    /**
     * 认证状态
     */
    String getAuthLabel(ApproveEnum auth);

    /**
     * 关联地址
     */
    PageInfo queryIpList(Long userId);

    /**
     * 关联邮箱
     */
    PageInfo queryEmailList(Long userId);

    /**
     * 关联实名
     */
    PageInfo queryAuthList(Long userId);

}
