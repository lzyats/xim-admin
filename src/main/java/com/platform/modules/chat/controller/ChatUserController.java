package com.platform.modules.chat.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.Validator;
import com.github.pagehelper.PageInfo;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.core.EnumUtils;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.utils.ServletUtils;
import com.platform.common.validation.ValidateGroup;
import com.platform.common.validation.ValidationUtil;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.enums.BannedTimeEnum;
import com.platform.modules.chat.enums.BannedTypeEnum;
import com.platform.modules.chat.service.*;
import com.platform.modules.chat.vo.*;
import com.platform.modules.wallet.service.WalletInfoService;
import com.platform.modules.wallet.service.WalletTradeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 用户 控制层
 * </p>
 */
@RestController
@RequestMapping("/chat/user")
public class ChatUserController extends BaseController {

    @Resource
    private ChatUserService chatUserService;

    @Resource
    private ChatFriendService chatFriendService;

    @Resource
    private ChatGroupService chatGroupService;

    @Resource
    private ChatMsgService chatMsgService;

    @Resource
    private WalletTradeService walletTradeService;

    @Resource
    private WalletInfoService walletInfoService;

    @Resource
    private ChatUserLogService chatUserLogService;

    /**
     * 列表数据
     */
    @RequiresPermissions(value = {"chat:user:list"})
    @GetMapping(value = "/list")
    public TableDataInfo list(ChatUser chatUser) {
        String param = ServletUtils.getParameter("sorting");
        YesOrNoEnum sorting = EnumUtils.toEnum(YesOrNoEnum.class, param, YesOrNoEnum.NO);
        if (YesOrNoEnum.YES.equals(sorting)) {
            startPage("b.balance desc");
        } else {
            startPage("a.createTime desc");
        }
        return getDataTable(chatUserService.queryDataList(chatUser));
    }

    /**
     * 列表数据
     */
    @RequiresPermissions(value = {"chat:user:list"})
    @GetMapping(value = "/listall")
    public TableDataInfo listall() {
        return getDataTable(chatUserService.queryDataListall());
    }

    /**
     * 详细信息
     */
    @RequiresPermissions(value = {"chat:user:query"})
    @GetMapping("/info/{userId}")
    public AjaxResult getInfo(@PathVariable Long userId) {
        return AjaxResult.success(chatUserService.getInfo(userId));
    }

    /**
     * 查询认证
     */
    @RequiresPermissions(value = {"chat:user:auth"})
    @GetMapping(value = "/getAuth/{userId}")
    public AjaxResult getAuth(@PathVariable Long userId) {
        Dict data = chatUserService.getAuth(userId);
        return AjaxResult.success(data);
    }

    /**
     * 取消认证
     */
    @RequiresPermissions(value = {"chat:user:auth"})
    @GetMapping(value = "/cancelAuth/{userId}")
    @SubmitRepeat
    public AjaxResult cancelAuth(@PathVariable Long userId) {
        chatUserService.cancelAuth(userId);
        return AjaxResult.success();
    }

    /**
     * 封禁账号
     */
    @RequiresPermissions(value = {"chat:user:banned"})
    @PostMapping("/banned")
    @DemoRepeat
    @SubmitRepeat
    public AjaxResult banned(@Validated @RequestBody ChatVo01 chatVo) {
        if (!BannedTimeEnum.DAY_0.equals(chatVo.getBannedTime())) {
            ValidationUtil.verify(chatVo, ValidateGroup.TWO.class);
            if (BannedTypeEnum.OTHER.equals(chatVo.getBannedType())) {
                ValidationUtil.verify(chatVo, ValidateGroup.ONE.class);
            } else {
                chatVo.setReason(chatVo.getBannedType().getInfo());
            }
        }
        chatUserService.banned(chatVo);
        return AjaxResult.success();
    }

    /**
     * 充值
     */
    @RequiresPermissions(value = {"chat:user:wallet"})
    @PostMapping(value = "/recharge")
    @SubmitRepeat
    public AjaxResult recharge(@Validated @RequestBody ChatVo08 chatVo) {
        walletInfoService.recharge(chatVo.getUserId(), chatVo.getAmount());
        return AjaxResult.success();
    }

    /**
     * 备注
     */
    @RequiresPermissions(value = {"chat:user:edit"})
    @PostMapping(value = "/remark")
    @SubmitRepeat
    public AjaxResult setRemark(@Validated @RequestBody ChatVo10 chatVo) {
        chatUserService.setRemark(chatVo.getUserId(), chatVo.getRemark());
        return AjaxResult.success();
    }

    /**
     * 邮箱
     */
    @RequiresPermissions(value = {"chat:user:edit"})
    @PostMapping(value = "/email")
    @SubmitRepeat
    public AjaxResult setEmail(@Validated @RequestBody ChatVo04 chatVo) {
        chatUserService.setEmail(chatVo.getUserId(), chatVo.getEmail());
        return AjaxResult.success();
    }

    /**
     * 收支明细
     */
    @RequiresPermissions(value = {"chat:user】" +
            ":wallet"})
    @GetMapping(value = "/income/{userId}")
    public TableDataInfo income(@PathVariable Long userId) {
        startPage("createTime desc");
        PageInfo pageInfo = walletTradeService.income(userId);
        return getDataTable(pageInfo);
    }

    /**
     * 重置密码
     */
    @RequiresPermissions(value = {"chat:user:edit"})
    @GetMapping(value = "/resetPwd/{userId}")
    @SubmitRepeat
    public AjaxResult resetPwd(@PathVariable Long userId) {
        Dict data = chatUserService.resetPwd(userId);
        return AjaxResult.success(data);
    }

    /**
     * 重置支付
     */
    @RequiresPermissions(value = {"chat:user:edit"})
    @GetMapping(value = "/resetPay/{userId}")
    @SubmitRepeat
    public AjaxResult resetPay(@PathVariable Long userId) {
        Dict data = chatUserService.resetPay(userId);
        return AjaxResult.success(data);
    }

    /**
     * 好友列表
     */
    @RequiresPermissions(value = {"chat:user:friend"})
    @GetMapping("/friendList/{userId}")
    public TableDataInfo friendList(@PathVariable Long userId) {
        startPage("createTime");
        return getDataTable(chatFriendService.queryDataList(userId));
    }

    /**
     * 群组列表
     */
    @RequiresPermissions(value = {"chat:user:group"})
    @GetMapping("/groupList/{userId}")
    public TableDataInfo groupList(@PathVariable Long userId) {
        startPage("createTime");
        return getDataTable(chatGroupService.groupList(userId));
    }

    /**
     * 消息
     */
    @RequiresPermissions(value = {"chat:user:msg"})
    @GetMapping(value = "/message/{groupId}")
    public TableDataInfo getMessage(@PathVariable Long groupId) {
        startPage("msgId desc");
        return getDataTable(chatMsgService.getMessage(groupId));
    }

    /**
     * 新增用户
     */
    @RequiresPermissions(value = {"chat:user:add"})
    @PostMapping(value = "/add")
    @SubmitRepeat
    public AjaxResult add(@Validated @RequestBody ChatVo06 chatVo) {
        // 邮箱
        String email = chatVo.getEmail();
        if (StringUtils.isEmpty(email)) {
            chatVo.setEmail(chatVo.getPhone() + "@qq.com");
        } else if (!Validator.isEmail(email)) {
            throw new BaseException("请输入正确的邮箱");
        }
        chatUserService.addUser(chatVo);
        return AjaxResult.success();
    }

    /**
     * 测试用户
     */
    @RequiresPermissions(value = {"chat:user:edit"})
    @PostMapping(value = "/special")
    @SubmitRepeat
    public AjaxResult special(@RequestBody ChatUser chatUser) {
        chatUserService.resetSpecial(chatUser.getUserId(), chatUser.getSpecial());
        return AjaxResult.success();
    }

    /**
     * 内部账号用户
     */
    @RequiresPermissions(value = {"chat:user:edit"})
    @PostMapping(value = "/isvip")
    @SubmitRepeat
    public AjaxResult isvip(@RequestBody ChatUser chatUser) {
        chatUserService.resetIsvip(chatUser.getUserId(), chatUser.getIsvip());
        return AjaxResult.success();
    }

    /**
     * 注销
     */
    @RequiresPermissions(value = {"chat:user:deleted"})
    @GetMapping(value = "/deleted/{userId}")
    @SubmitRepeat
    @DemoRepeat
    public AjaxResult deleted(@PathVariable Long userId) {
        chatUserService.deleted(userId);
        return AjaxResult.success();
    }

    /**
     * 查询日志
     */
    @RequiresPermissions(value = {"chat:user:log"})
    @GetMapping("/log/{userId}")
    public TableDataInfo getLog(@PathVariable Long userId) {
        startPage("id desc");
        return getDataTable(chatUserLogService.queryDataList(userId));
    }

    /**
     * 关联地址
     */
    @RequiresPermissions(value = {"chat:user:ip"})
    @GetMapping("/ipList/{userId}")
    public TableDataInfo ipList(@PathVariable Long userId) {
        return getDataTable(chatUserService.queryIpList(userId));
    }

    /**
     * 关联实名
     */
    @RequiresPermissions(value = {"chat:user:auth"})
    @GetMapping("/authList/{userId}")
    public TableDataInfo authList(@PathVariable Long userId) {
        return getDataTable(chatUserService.queryAuthList(userId));
    }

    /**
     * 关联邮箱
     */
    @RequiresPermissions(value = {"chat:user:email"})
    @GetMapping("/emailList/{userId}")
    public TableDataInfo emailList(@PathVariable Long userId) {
        return getDataTable(chatUserService.queryEmailList(userId));
    }

}

