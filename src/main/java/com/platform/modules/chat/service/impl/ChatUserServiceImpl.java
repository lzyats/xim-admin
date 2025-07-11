package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.enums.GenderEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.redis.RedisUtils;
import com.platform.common.utils.CodeUtils;
import com.platform.common.web.page.PageDomain;
import com.platform.common.web.page.TableSupport;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatUserDao;
import com.platform.modules.chat.domain.ChatBanned;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.domain.ChatUserAppeal;
import com.platform.modules.chat.domain.ChatUserInfo;
import com.platform.modules.chat.enums.BannedTimeEnum;
import com.platform.modules.chat.enums.BannedTypeEnum;
import com.platform.modules.chat.enums.UserLogEnum;
import com.platform.modules.chat.service.*;
import com.platform.modules.chat.vo.ChatVo01;
import com.platform.modules.chat.vo.ChatVo06;
import com.platform.modules.push.dto.PushBox;
import com.platform.modules.push.dto.PushFrom;
import com.platform.modules.push.dto.PushSetting;
import com.platform.modules.push.enums.PushAuditEnum;
import com.platform.modules.push.enums.PushMsgTypeEnum;
import com.platform.modules.push.enums.PushSettingEnum;
import com.platform.modules.push.service.PushService;
import com.platform.modules.wallet.domain.WalletInfo;
import com.platform.modules.wallet.service.WalletInfoService;
import com.platform.modules.ws.service.HookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 用户表 服务层实现
 * </p>
 */
@Slf4j
@Service("chatUserService")
public class ChatUserServiceImpl extends BaseServiceImpl<ChatUser> implements ChatUserService {

    @Resource
    private ChatUserDao chatUserDao;

    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private WalletInfoService walletInfoService;

    @Resource
    private ChatNumberService chatNumberService;

    @Resource
    private PushService pushService;

    @Resource
    private ChatUserDeletedService chatUserDeletedService;

    @Resource
    private ChatUserInfoService chatUserInfoService;

    @Resource
    private ChatUserTokenService chatUserTokenService;

    @Resource
    private ChatBannedService chatBannedService;

    @Resource
    private ChatPortraitService chatPortraitService;

    @Resource
    private ChatUserLogService chatUserLogService;

    @Resource
    private ChatUserAppealService chatUserAppealService;

    @Resource
    private HookService hookService;

    @Resource
    private ChatRobotService chatRobotService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatUserDao);
    }

    @Override
    public List<ChatUser> queryList(ChatUser t) {
        List<ChatUser> dataList = chatUserDao.queryList(t);
        return dataList;
    }

    @Override
    public PageInfo queryDataList(ChatUser chatUser) {
        // 查询
        List<ChatUser> dataList = chatUserDao.queryDataList(chatUser);
        // 统计
        QueryWrapper<ChatUserInfo> wrapper = new QueryWrapper<>();
        wrapper.select("id_card AS label, COUNT(id_card) as count");
        wrapper.groupBy("id_card");
        wrapper.isNotNull(ChatUserInfo.COLUMN_ID_CARD);
        // 分组
        HashMap<String, Long> dataMap = chatUserInfoService.queryList(wrapper).stream().collect(HashMap::new, (x, y) -> {
            x.put(y.getLabel(), y.getCount());
        }, HashMap::putAll);
        // list转Obj
        List<Dict> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            x.add(format(y, dataMap));
        }, ArrayList::addAll);
        return getPageInfo(dictList, dataList);
    }

    @Override
    public Dict getInfo(Long userId) {
        // 查询
        ChatUser chatUser = findById(userId);
        // 余额
        WalletInfo wallet = walletInfoService.findById(userId);
        // 设置
        chatUser.setBalance(wallet.getBalance());
        Dict dict = format(chatUser, new HashMap<>());
        // 封禁
        ChatBanned chatBanned = chatBannedService.getById(userId);
        if (chatBanned != null) {
            dict.set(ChatBanned.LABEL_REASON, chatBanned.getBannedReason())
                    .set(ChatBanned.LABEL_TIME, chatBanned.getBannedTime());
        }
        return dict;
    }

    @Override
    public ChatUser getByPhone(String phone) {
        return this.queryOne(new ChatUser().setPhone(phone));
    }

    /**
     * 格式化详情
     */
    private Dict format(ChatUser chatUser, HashMap<String, Long> dataMap) {
        YesOrNoEnum status = YesOrNoEnum.transform(chatUser.getDeleted() != null);
        return Dict.create().parseBean(chatUser)
                .filter(ChatUser.LABEL_USER_ID
                        , ChatUser.LABEL_USER_NO
                        , ChatUser.LABEL_PHONE
                        , ChatUser.LABEL_NICKNAME
                        , ChatUser.LABEL_PORTRAIT
                        , ChatUser.LABEL_REMARK
                        , ChatUser.LABEL_AUTH
                        , ChatUser.LABEL_BANNED
                        , ChatUser.LABEL_SPECIAL
                        , ChatUser.LABEL_CREATE_TIME
                        , ChatUser.LABEL_ON_LINE
                        , ChatUser.LABEL_IP
                        , ChatUser.LABEL_IP_ADDR
                        , ChatUser.LABEL_EMAIL
                        , ChatUser.LABEL_BALANCE
                )
                .set(ChatUser.LABEL_AUTH_LABEL, getAuthLabel(chatUser.getAuth()))
                .set(ChatUser.LABEL_BANNED_LABEL, YesOrNoEnum.transform(chatUser.getBanned()) ? "封禁" : "正常")
                .set(ChatUser.LABEL_STATUS, status)
                .set(ChatUser.LABEL_STATUS_LABEL, YesOrNoEnum.transform(status) ? "正常" : "注销")
                .set(ChatUser.LABEL_AUTH_COUNT, dataMap.get(chatUser.getIdCard()));
    }

    /**
     * 认证信息
     */
    @Override
    public String getAuthLabel(ApproveEnum auth) {
        String authLabel;
        switch (auth) {
            case APPLY:
                authLabel = "认证中";
                break;
            case PASS:
                authLabel = "已认证";
                break;
            case REJECT:
                authLabel = "已驳回";
                break;
            default:
                authLabel = "未认证";
        }
        return authLabel;
    }

    @Override
    public PageInfo queryIpList(Long userId) {
        // 查询
        ChatUser chatUser = findById(userId);
        if (StringUtils.isEmpty(chatUser.getIp())) {
            return new PageInfo();
        }
        // 执行分页
        PageDomain pageDomain = TableSupport.getPageDomain();
        PageHelper.startPage(pageDomain.getPageNum(), pageDomain.getPageSize(), StrUtil.toUnderlineCase("userId desc"));
        // 查询
        QueryWrapper<ChatUser> wrapper = new QueryWrapper<>();
        wrapper.eq(ChatUser.COLUMN_IP, chatUser.getIp());
        List<ChatUser> dataList = this.queryList(wrapper);
        // 转换
        List<Dict> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            if (!userId.equals(y.getUserId())) {
                YesOrNoEnum status = YesOrNoEnum.transform(y.getDeleted() != null);
                Dict dict = Dict.create().parseBean(y)
                        .filter(ChatUser.LABEL_USER_ID
                                , ChatUser.LABEL_NICKNAME
                                , ChatUser.LABEL_PORTRAIT
                                , ChatUser.LABEL_USER_NO
                                , ChatUser.LABEL_PHONE
                                , ChatUser.LABEL_CREATE_TIME
                                , ChatUser.LABEL_ON_LINE
                                , ChatUser.LABEL_IP
                                , ChatUser.LABEL_IP_ADDR
                                , ChatUser.LABEL_AUTH
                                , ChatUser.LABEL_BANNED
                        )
                        .set(ChatUser.LABEL_AUTH_LABEL, getAuthLabel(y.getAuth()))
                        .set(ChatUser.LABEL_BANNED_LABEL, YesOrNoEnum.transform(y.getBanned()) ? "封禁" : "正常")
                        .set(ChatUser.LABEL_STATUS, status)
                        .set(ChatUser.LABEL_STATUS_LABEL, YesOrNoEnum.transform(status) ? "正常" : "注销");
                x.add(dict);
            }
        }, ArrayList::addAll);
        // 转换
        return getPageInfo(dictList, dataList);
    }

    @Override
    public PageInfo queryEmailList(Long userId) {
        // 查询
        ChatUser chatUser = findById(userId);
        if (StringUtils.isEmpty(chatUser.getEmail())) {
            return new PageInfo();
        }
        // 执行分页
        PageDomain pageDomain = TableSupport.getPageDomain();
        PageHelper.startPage(pageDomain.getPageNum(), pageDomain.getPageSize(), StrUtil.toUnderlineCase("userId desc"));
        // 查询
        QueryWrapper<ChatUser> wrapper = new QueryWrapper<>();
        wrapper.eq(ChatUser.COLUMN_EMAIL, chatUser.getEmail());
        List<ChatUser> dataList = this.queryList(wrapper);
        // 转换
        List<Dict> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            if (!userId.equals(y.getUserId())) {
                YesOrNoEnum status = YesOrNoEnum.transform(y.getDeleted() != null);
                Dict dict = Dict.create().parseBean(y)
                        .filter(ChatUser.LABEL_USER_ID
                                , ChatUser.LABEL_NICKNAME
                                , ChatUser.LABEL_PORTRAIT
                                , ChatUser.LABEL_USER_NO
                                , ChatUser.LABEL_PHONE
                                , ChatUser.LABEL_EMAIL
                                , ChatUser.LABEL_CREATE_TIME
                                , ChatUser.LABEL_AUTH
                                , ChatUser.LABEL_BANNED
                        )
                        .set(ChatUser.LABEL_AUTH_LABEL, getAuthLabel(y.getAuth()))
                        .set(ChatUser.LABEL_BANNED_LABEL, YesOrNoEnum.transform(y.getBanned()) ? "封禁" : "正常")
                        .set(ChatUser.LABEL_STATUS, status)
                        .set(ChatUser.LABEL_STATUS_LABEL, YesOrNoEnum.transform(status) ? "正常" : "注销");
                x.add(dict);
            }
        }, ArrayList::addAll);
        // 转换
        return getPageInfo(dictList, dataList);
    }

    @Override
    public PageInfo queryAuthList(Long userId) {
        // 查询
        ChatUserInfo userInfo = chatUserInfoService.getById(userId);
        if (StringUtils.isEmpty(userInfo.getIdCard())) {
            return new PageInfo();
        }
        // 执行分页
        PageDomain pageDomain = TableSupport.getPageDomain();
        PageHelper.startPage(pageDomain.getPageNum(), pageDomain.getPageSize(), StrUtil.toUnderlineCase("b.authTime desc"));
        // 查询
        List<ChatUser> dataList = chatUserDao.queryAuthList(new ChatUser().setIdCard(userInfo.getIdCard()));
        // 转换
        List<Dict> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            if (!userId.equals(y.getUserId())) {
                YesOrNoEnum status = YesOrNoEnum.transform(y.getDeleted() != null);
                Dict dict = Dict.create().parseBean(y)
                        .filter(ChatUser.LABEL_USER_ID
                                , ChatUser.LABEL_NICKNAME
                                , ChatUser.LABEL_PORTRAIT
                                , ChatUser.LABEL_USER_NO
                                , ChatUser.LABEL_PHONE
                                , ChatUser.LABEL_AUTH_NAME
                                , ChatUser.LABEL_ID_CARD
                                , ChatUser.LABEL_BANNED
                                , ChatUserInfo.LABEL_AUTH_TIME
                        )
                        .set(ChatUser.LABEL_BANNED_LABEL, YesOrNoEnum.transform(y.getBanned()) ? "封禁" : "正常")
                        .set(ChatUser.LABEL_STATUS, status)
                        .set(ChatUser.LABEL_STATUS_LABEL, YesOrNoEnum.transform(status) ? "正常" : "注销");
                x.add(dict);
            }
        }, ArrayList::addAll);
        // 转换
        return getPageInfo(dictList, dataList);
    }

    @Override
    public Dict getAuth(Long userId) {
        // 查询
        ChatUser chatUser = this.findById(userId);
        // 查询
        ChatUserInfo chatUserInfo = chatUserInfoService.getById(userId);
        ApproveEnum auth = chatUser.getAuth();
        String reason = "-";
        if (ApproveEnum.REJECT.equals(auth)) {
            reason = chatUserInfo.getAuthReason();
        }
        return Dict.create().parseBean(chatUserInfo)
                .set(ChatUserInfo.LABEL_REASON, reason)
                .set(ChatUser.LABEL_USER_NO, chatUser.getUserNo())
                .set(ChatUser.LABEL_AUTH, auth)
                .set(ChatUser.LABEL_AUTH_LABEL, getAuthLabel(auth));
    }

    @Override
    public void cancelAuth(Long userId) {
        ChatUser chatUser = new ChatUser(userId).setAuth(ApproveEnum.NONE);
        // 更新
        this.updateById(chatUser);
        // 缓存
        this.clearCache(userId);
        // 推送
        this.pushSetting(userId, ChatUser.LABEL_AUTH, ApproveEnum.NONE.getCode());
    }

    @Transactional
    @Override
    public void banned(ChatVo01 chatVo) {
        Long userId = chatVo.getUserId();
        String reason = chatVo.getReason();
        BannedTimeEnum bannedTime = chatVo.getBannedTime();
        YesOrNoEnum banned = YesOrNoEnum.YES;
        UserLogEnum logType = UserLogEnum.BANNED;
        // 解封
        if (BannedTimeEnum.DAY_0.equals(bannedTime)) {
            banned = YesOrNoEnum.NO;
            logType = UserLogEnum.BANNED_REMOVE;
        }
        // 更新
        this.updateById(new ChatUser(userId).setBanned(banned));
        // 更新
        chatBannedService.addBanned(userId, bannedTime, reason);
        // 刷新
        chatUserTokenService.refreshBanned(userId, banned);
        // 日志
        chatUserLogService.addLog(userId, logType);
        // 缓存
        this.clearCache(userId);
        // 解除
        String redisKey = StrUtil.format(AppConstants.REDIS_CHAT_SPECIAL, PushAuditEnum.APPLY_SPECIAL.getType());
        redisUtils.sRemove(redisKey, NumberUtil.toStr(userId));
        // 通知
        this.querySpecialCount();
    }

    @Override
    public void setRemark(Long userId, String remark) {
        // 更新
        this.updateById(new ChatUser(userId).setRemark(remark));
    }

    @Override
    public void setEmail(Long userId, String email) {
        // 更新
        this.updateById(new ChatUser(userId).setEmail(email));
        // 缓存
        this.clearCache(userId);
    }

    @Override
    public ChatUser queryCache(Long userId) {
        ChatUser chatUser = this.getById(userId);
        if (chatUser == null) {
            chatUser = new ChatUser()
                    .setUserId(userId)
                    .setPortrait("https://img.alicdn.com/imgextra/i4/87413133/O1CN019OZ3XT1Z0xr2pRYq8_!!87413133.jpg")
                    .setNickname("[用户已注销]")
                    .setUserNo(AppConstants.DELETED_CHAT_NO);
        }
        return chatUser;
    }

    @Override
    public PageInfo queryAuthList(ChatUser chatUser) {
        // 查询
        List<ChatUser> dataList = chatUserDao.queryAuthList(chatUser);
        // 转换
        List<Dict> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            Dict dict = Dict.create().parseBean(y)
                    .filter(ChatUser.LABEL_USER_ID
                            , ChatUser.LABEL_NICKNAME
                            , ChatUser.LABEL_PORTRAIT
                            , ChatUser.LABEL_USER_NO
                            , ChatUser.LABEL_PHONE
                            , ChatUserInfo.LABEL_AUTH_TIME
                    );
            x.add(dict);
        }, ArrayList::addAll);
        // 查询
        Long count = this.queryCount(new ChatUser().setAuth(ApproveEnum.APPLY));
        // 通知
        hookService.handle(PushAuditEnum.APPLY_AUTH, count);
        // 返回
        return getPageInfo(dictList, dataList);
    }

    @Transactional
    @Override
    public void approveAuth(Long userId, String name, String idCard, ApproveEnum auth, String reason) {
        // 查询
        ChatUser chatUser = this.findById(userId);
        // 验证
        if (!ApproveEnum.APPLY.equals(chatUser.getAuth())) {
            return;
        }
        // 更新
        this.updateById(new ChatUser(userId).setAuth(auth));
        // 更新

        ChatUserInfo userInfo = new ChatUserInfo(userId)
                .setName(name)
                .setIdCard(idCard);
        if (ApproveEnum.REJECT.equals(auth)) {
            userInfo.setAuthReason(reason);
        }
        chatUserInfoService.updateById(userInfo);
        // 缓存
        this.clearCache(userId);
        // 推送
        this.pushSetting(userId, ChatUser.LABEL_AUTH, auth.getCode());
        // 通知
        this.pushAuthMsg(userId, auth, reason);
    }

    @Override
    public PageInfo queryBannedList(ChatUser chatUser) {
        // 查询
        List<ChatUser> dataList = chatUserDao.queryBannedList(chatUser);
        // 转换
        List<Dict> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            Dict dict = Dict.create().parseBean(y)
                    .filter(ChatUser.LABEL_USER_ID
                            , ChatUser.LABEL_NICKNAME
                            , ChatUser.LABEL_PORTRAIT
                            , ChatUser.LABEL_USER_NO
                            , ChatUser.LABEL_PHONE
                            , ChatUser.LABEL_BANNED
                            , ChatUser.LABEL_CREATE_TIME
                    )
                    .set(ChatUser.LABEL_BANNED_LABEL, YesOrNoEnum.transform(y.getBanned()) ? "封禁" : "正常");
            x.add(dict);
        }, ArrayList::addAll);
        // 查询
        Long count = chatUserDao.queryBannedCount();
        // 通知
        hookService.handle(PushAuditEnum.APPLY_BANNED, count);
        // 返回
        return getPageInfo(dictList, dataList);
    }

    @Override
    public PageInfo querySpecialList() {
        // 查询
        QueryWrapper<ChatUser> wrapper = new QueryWrapper<>();
        wrapper.eq(ChatUser.COLUMN_BANNED, YesOrNoEnum.NO);
        wrapper.eq(ChatUser.COLUMN_ABNORMAL, YesOrNoEnum.YES);
        List<ChatUser> dataList = this.queryList(wrapper);
        // 转换
        List<Dict> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            Dict dict = Dict.create().parseBean(y)
                    .filter(ChatUser.LABEL_USER_ID
                            , ChatUser.LABEL_NICKNAME
                            , ChatUser.LABEL_PORTRAIT
                            , ChatUser.LABEL_USER_NO
                            , ChatUser.LABEL_PHONE
                            , ChatUser.LABEL_CREATE_TIME
                            , ChatUser.LABEL_ON_LINE
                            , ChatUser.LABEL_IP
                            , ChatUser.LABEL_IP_ADDR
                    );
            x.add(dict);
        }, ArrayList::addAll);
        // 转换
        PageInfo pageInfo = getPageInfo(dictList, dataList);
        // 通知
        hookService.handle(PushAuditEnum.APPLY_SPECIAL, pageInfo.getTotal());
        // 返回
        return pageInfo;
    }

    @Override
    public void querySpecialCount() {
        ThreadUtil.execAsync(() -> {
            // 查询
            Long value = this.queryCount(new ChatUser().setBanned(YesOrNoEnum.NO).setAbnormal(YesOrNoEnum.YES));
            // 通知
            hookService.handle(PushAuditEnum.APPLY_SPECIAL, value);
        });
    }

    @Override
    public void approveAbnormal(Long userId, YesOrNoEnum status) {
        // 更新
        this.updateById(new ChatUser(userId).setAbnormal(status));
        // 缓存
        this.clearCache(userId);
        // 通知
        this.querySpecialCount();
    }

    @Override
    public Dict queryBannedInfo(Long userId) {
        // 查询
        ChatUser chatUser = this.findById(userId);
        // 验证
        if (YesOrNoEnum.NO.equals(chatUser.getBanned())) {
            throw new BaseException("请刷新重试");
        }
        // 查询
        ChatBanned chatBanned = chatBannedService.getById(userId);
        if (chatBanned == null) {
            chatBanned = new ChatBanned()
                    .setBannedReason("其他原因")
                    .setBannedTime(DateUtil.offset(DateUtil.date(), DateField.YEAR, 3));
        }
        // 查询
        ChatUserAppeal chatUserAppeal = chatUserAppealService.queryAppeal(userId);
        // 组装
        return Dict.create().parseBean(chatUser)
                .filter(ChatUser.LABEL_USER_ID
                        , ChatUser.LABEL_NICKNAME
                        , ChatUser.LABEL_PORTRAIT
                        , ChatUser.LABEL_USER_NO
                        , ChatUser.LABEL_PHONE
                        , ChatUser.LABEL_BANNED
                )
                .set(ChatUser.LABEL_CREATE_TIME, chatUserAppeal.getCreateTime())
                .set(ChatUser.LABEL_BANNED_LABEL, YesOrNoEnum.transform(chatUser.getBanned()) ? "封禁" : "正常")
                .set(ChatBanned.LABEL_REASON, chatBanned.getBannedReason())
                .set(ChatBanned.LABEL_TIME, chatBanned.getBannedTime())
                .set(ChatUserAppeal.LABEL_IMAGES, JSONUtil.toList(chatUserAppeal.getImages(), String.class))
                .set(ChatUserAppeal.LABEL_CONTENT, chatUserAppeal.getContent());
    }

    @Transactional
    @Override
    public void approveBanned(Long userId, YesOrNoEnum status) {
        // 查询
        ChatUser chatUser = this.findById(userId);
        // 验证
        if (YesOrNoEnum.NO.equals(chatUser.getBanned())) {
            return;
        }
        // 删除
        chatUserAppealService.deleteById(userId);
        // 处理
        if (YesOrNoEnum.YES.equals(status)) {
            // 更新
            this.updateById(new ChatUser(userId).setBanned(status));
            // 同意
            ChatVo01 chatVo = new ChatVo01()
                    .setUserId(userId)
                    .setBannedType(BannedTypeEnum.OTHER)
                    .setReason("-")
                    .setBannedTime(BannedTimeEnum.DAY_0);
            this.banned(chatVo);
            // 缓存

            this.clearCache(userId);
        }
        // 解除
        String redisKey = StrUtil.format(AppConstants.REDIS_CHAT_SPECIAL, PushAuditEnum.APPLY_BANNED.getType());
        redisUtils.sRemove(redisKey, NumberUtil.toStr(userId));
    }

    @Transactional
    @Override
    public void addUser(ChatVo06 chatVo) {
        // 验证账号
        String phone = chatVo.getPhone();
        // 手机号
        if (!Validator.isMobile(phone)) {
            throw new BaseException("请输入正确的账号");
        }
        String nickname = chatVo.getNickname();
        String email = chatVo.getEmail();
        YesOrNoEnum special = chatVo.getSpecial();
        Date now = DateUtil.date();
        // 微聊号码
        String userNo = chatNumberService.queryNextNo();
        // 头像
        String portrait = chatPortraitService.queryUserPortrait();
        String salt = CodeUtils.salt();
        String password = CodeUtils.password();
        ChatUser chatUser = new ChatUser()
                .setPhone(phone)
                .setUserNo(userNo)
                .setNickname(nickname)
                .setEmail(email)
                .setPortrait(portrait)
                .setGender(GenderEnum.MALE)
                .setBirthday(DateUtil.parseDate("1970-01-01"))
                .setProvince("北京市")
                .setCity("北京城区")
                .setSalt(salt)
                .setPassword(CodeUtils.credentials(CodeUtils.md5(password), salt))
                .setPass(YesOrNoEnum.NO)
                .setAuth(ApproveEnum.NONE)
                .setBanned(YesOrNoEnum.NO)
                .setSpecial(special)
                .setAbnormal(YesOrNoEnum.NO)
                .setPayment(YesOrNoEnum.NO)
                .setPrivacyNo(YesOrNoEnum.YES)
                .setPrivacyPhone(YesOrNoEnum.YES)
                .setPrivacyScan(YesOrNoEnum.YES)
                .setPrivacyCard(YesOrNoEnum.YES)
                .setPrivacyGroup(YesOrNoEnum.YES)
                .setCreateTime(now);
        try {
            // 新增用户
            this.add(chatUser);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            throw new BaseException("当前账号已存在，新增失败");
        }
        // 新增用户
        chatUserInfoService.addInfo(chatUser.getUserId());
        // 新增钱包
        walletInfoService.addWallet(chatUser.getUserId());
    }


    @Transactional
    @Override
    public void deleted(Long userId) {
        ChatUser chatUser = getById(userId);
        String error = "用户不存在";
        if (chatUser == null) {
            throw new BaseException(error);
        }
        if (chatUser.getDeleted() == null) {
            throw new BaseException(error);
        }
        // 注销用户
        chatUserDeletedService.deleted(userId, chatUser.getPhone());
        // 删除用户
        this.update(Wrappers.<ChatUser>lambdaUpdate()
                .set(ChatUser::getDeleted, null)
                .eq(ChatUser::getUserId, userId));
        // 移除token
        chatUserTokenService.deleted(userId);
        // 清空缓存
        this.clearCache(userId);
    }

    @Override
    public Dict resetPwd(Long userId) {
        String salt = RandomUtil.randomString(4);
        String password = CodeUtils.password();
        ChatUser chatUser = new ChatUser()
                .setUserId(userId)
                .setSalt(salt)
                .setPassword(CodeUtils.credentials(CodeUtils.md5(password), salt));
        this.updateById(chatUser);
        return Dict.create().set("data", StrUtil.format("您的新登录密码为：{}，请妥善保管", password));
    }

    @Override
    public Dict resetPay(Long userId) {
        String salt = CodeUtils.salt();
        String password = NumberUtil.toStr(RandomUtil.randomInt(100000, 999999));
        WalletInfo wallet = new WalletInfo()
                .setUserId(userId)
                .setSalt(salt)
                .setPassword(CodeUtils.credentials(password, salt));
        walletInfoService.updateById(wallet);
        // 验证次数
        redisUtils.delete(StrUtil.format(AppConstants.REDIS_CHAT_WALLET, DateUtil.dayOfMonth(DateUtil.date()), userId));
        return Dict.create().set("data", StrUtil.format("您的新支付密码为：{}，请妥善保管", password));
    }

    @Transactional
    @Override
    public void resetSpecial(Long userId, YesOrNoEnum special) {
        // 更新
        this.updateById(new ChatUser().setUserId(userId).setSpecial(special));
    }

    /**
     * 移除缓存
     */
    private void clearCache(Long userId) {
        redisUtils.delete(StrUtil.format(AppConstants.REDIS_CHAT_USER, userId));
    }

    // 通知推送
    private void pushSetting(Long userId, String label, String value) {
        PushSetting setting = new PushSetting(PushSettingEnum.MINE, userId, label, value);
        pushService.pushSetting(setting, Arrays.asList(userId));
    }

    // 通知推送
    private void pushAuthMsg(Long userId, ApproveEnum auth, String reason) {
        // 查询服务
        PushFrom pushFrom = chatRobotService.getPushFrom(AppConstants.ROBOT_PUSH);
        // 组装消息
        String title = "实名认证";
        String content = "认证成功";
        String remark = "实名认证成功";
        String data = "实名认证成功";
        if (ApproveEnum.REJECT.equals(auth)) {
            content = "认证失败";
            remark = "失败原因：" + reason;
            data = "实名认证失败";
        }
        PushBox pushBox = new PushBox(data, title, content, remark);
        // 推送消息
        pushService.pushSingle(pushFrom, userId, JSONUtil.toJsonStr(pushBox), PushMsgTypeEnum.BOX);
    }

}
