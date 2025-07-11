package com.platform.modules.extend.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.common.web.vo.LabelVo;
import com.platform.modules.chat.domain.ChatRobot;
import com.platform.modules.chat.domain.ChatRobotReply;
import com.platform.modules.chat.enums.RobotReplyEnum;
import com.platform.modules.chat.service.ChatRobotReplyService;
import com.platform.modules.chat.service.ChatRobotService;
import com.platform.modules.chat.vo.ChatVo02;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务号 控制层
 * </p>
 */
@RestController
@RequestMapping("/extend/robot")
public class ExtendRobotController extends BaseController {

    private final static String title = "服务管理";

    @Resource
    private ChatRobotService chatRobotService;

    @Resource
    private ChatRobotReplyService chatRobotReplyService;

    /**
     * 列表
     */
    @RequiresPermissions(value = {"extend:robot:list"})
    @GetMapping(value = "/robot/list")
    public TableDataInfo queryRobotList(ChatRobot chatRobot) {
        startPage();
        return getDataTable(chatRobotService.queryList(chatRobot));
    }

    /**
     * 修改
     */
    @SubmitRepeat
    @RequiresPermissions("extend:robot:edit")
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @PostMapping("/robot/edit")
    public AjaxResult editRobot(@Validated @RequestBody ChatRobot robot) {
        chatRobotService.editRobot(robot);
        return AjaxResult.success();
    }

    /**
     * 菜单
     */
    @SubmitRepeat
    @RequiresPermissions("extend:robot:edit")
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @PostMapping("/robot/menu")
    public AjaxResult editMenu(@Validated @RequestBody ChatVo02 chatVo) {
        chatRobotService.editMenu(chatVo);
        return AjaxResult.success();
    }

    /**
     * 菜单类型
     */
    @RequiresPermissions(value = {"extend:robot:list"})
    @GetMapping("/robot/extend/{extendType}/{robotId}")
    public AjaxResult extend(@PathVariable String extendType, @PathVariable Long robotId) {
        List<LabelVo> dataList = chatRobotService.extend(extendType, robotId);
        return AjaxResult.success(dataList);
    }

    /**
     * 重置
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"extend:robot:edit"})
    @AppLog(value = title, type = LogTypeEnum.RESET)
    @GetMapping("/robot/reset/{robotId}")
    public AjaxResult resetRobot(@PathVariable Long robotId) {
        chatRobotService.resetRobot(robotId);
        return AjaxResult.success();
    }

    /**
     * 列表
     */
    @RequiresPermissions("extend:robot:reply")
    @GetMapping(value = "/subscribe/list")
    public TableDataInfo querySubscribeList(ChatRobot chatRobot) {
        startPage();
        List<ChatRobotReply> dataList = chatRobotReplyService.queryDataList(chatRobot.getRobotId(), RobotReplyEnum.SUBSCRIBE);
        return getDataTable(dataList);
    }

    /**
     * 新增
     */
    @SubmitRepeat
    @RequiresPermissions("extend:robot:reply")
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @DemoRepeat
    @PostMapping("/subscribe/add")
    public AjaxResult addSubscribe(@Validated @RequestBody ChatRobotReply robotReply) {
        robotReply.setReplyType(RobotReplyEnum.SUBSCRIBE);
        chatRobotReplyService.addReply(robotReply);
        return AjaxResult.success();
    }

    /**
     * 修改
     */
    @SubmitRepeat
    @RequiresPermissions("extend:robot:reply")
    @DemoRepeat
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @PostMapping("/subscribe/edit")
    public AjaxResult editSubscribe(@Validated @RequestBody ChatRobotReply robotReply) {
        robotReply.setReplyType(RobotReplyEnum.SUBSCRIBE);
        chatRobotReplyService.editReply(robotReply);
        return AjaxResult.success();
    }

    /**
     * 拷贝
     */
    @SubmitRepeat
    @RequiresPermissions("extend:robot:reply")
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @DemoRepeat
    @GetMapping("/subscribe/copy/{replyId}")
    public AjaxResult copySubscribe(@PathVariable Long replyId) {
        chatRobotReplyService.copyReply(replyId);
        return AjaxResult.success();
    }

    /**
     * 删除
     */
    @SubmitRepeat
    @RequiresPermissions("extend:robot:reply")
    @DemoRepeat
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/subscribe/delete/{replyId}")
    public AjaxResult deleteSubscribe(@PathVariable Long replyId) {
        chatRobotReplyService.deleteById(replyId);
        return AjaxResult.success();
    }

    /**
     * 列表
     */
    @RequiresPermissions("extend:robot:reply")
    @GetMapping(value = "/even/list")
    public TableDataInfo queryEvenList(ChatRobot chatRobot) {
        startPage();
        List<ChatRobotReply> dataList = chatRobotReplyService.queryDataList(chatRobot.getRobotId(), RobotReplyEnum.EVEN);
        return getDataTable(dataList);
    }

    /**
     * 新增
     */
    @SubmitRepeat
    @RequiresPermissions("extend:robot:reply")
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @DemoRepeat
    @PostMapping("/even/add")
    public AjaxResult addEven(@Validated @RequestBody ChatRobotReply robotReply) {
        robotReply.setReplyType(RobotReplyEnum.EVEN);
        chatRobotReplyService.addReply(robotReply);
        return AjaxResult.success();
    }

    /**
     * 修改
     */
    @SubmitRepeat
    @RequiresPermissions("extend:robot:reply")
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @PostMapping("/even/edit")
    public AjaxResult editEven(@Validated @RequestBody ChatRobotReply robotReply) {
        robotReply.setReplyType(RobotReplyEnum.EVEN);
        chatRobotReplyService.editReply(robotReply);
        return AjaxResult.success();
    }

    /**
     * 拷贝
     */
    @SubmitRepeat
    @RequiresPermissions("extend:robot:reply")
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @DemoRepeat
    @GetMapping("/even/copy/{replyId}")
    public AjaxResult copyEven(@PathVariable Long replyId) {
        chatRobotReplyService.copyReply(replyId);
        return AjaxResult.success();
    }

    /**
     * 删除
     */
    @SubmitRepeat
    @RequiresPermissions("extend:robot:reply")
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @DemoRepeat
    @GetMapping("/even/delete/{replyId}")
    public AjaxResult deleteEven(@PathVariable Long replyId) {
        chatRobotReplyService.deleteById(replyId);
        return AjaxResult.success();
    }

}

