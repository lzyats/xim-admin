package com.platform.modules.work.controller;

import cn.hutool.json.JSONObject;
import com.platform.common.constant.AppConstants;
import com.platform.common.exception.BaseException;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.push.service.PushService;
import com.platform.modules.work.service.WorkMessageService;
import com.platform.modules.work.vo.WorkVo00;
import com.platform.modules.work.vo.WorkVo02;
import com.platform.modules.work.vo.WorkVo03;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 聊天
 */
@RestController
@Slf4j
@RequestMapping("/work/msg")
public class WorkMessageController extends BaseController {

    @Resource
    private WorkMessageService workMessageService;

    @Resource
    private PushService pushService;

    /**
     * 发送信息
     */
    @PostMapping("/sendMsg")
    public AjaxResult sendMsg(@Validated @RequestBody WorkVo02 workVo) {
        switch (workVo.getMsgType()) {
            case TEXT:
            case IMAGE:
            case VIDEO:
            case VOICE:
            case FILE:
            case LOCATION:
            case CARD:
            case RECALL:
            case REPLY:
            case FORWARD:
                break;
            default:
                throw new BaseException("不支持的消息类型");
        }
        WorkVo00 data = workMessageService.sendMsg(workVo);
        return AjaxResult.success(data);
    }

    /**
     * 拉取消息
     */
    @GetMapping("/pullMsg")
    public AjaxResult pullMsg() {
        List<JSONObject> dataList = workMessageService.pullMsg();
        return AjaxResult.success(dataList);
    }

    /**
     * 清空消息
     */
    @GetMapping("/clearMsg/{groupId}")
    public AjaxResult clearMsg(@PathVariable Long groupId) {
        Long current = AppConstants.ROBOT_ID;
        pushService.clearMsg(current, groupId);
        return AjaxResult.success();
    }

    /**
     * 删除消息
     */
    @PostMapping("/removeMsg")
    public AjaxResult removeMsg(@RequestBody WorkVo03 workVo) {
        Long current = AppConstants.ROBOT_ID;
        workMessageService.removeMsg(current, workVo.getDataList());
        return AjaxResult.success();
    }

}
