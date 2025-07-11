package com.platform.modules.work.controller;

import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.chat.enums.BannedTypeEnum;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.chat.vo.ChatVo01;
import com.platform.modules.work.service.WorkUserService;
import com.platform.modules.work.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户
 */
@RestController
@Slf4j
@RequestMapping("/work/user")
public class WorkUserController extends BaseController {

    @Resource
    private WorkUserService workUserService;

    @Resource
    private ChatUserService chatUserService;

    /**
     * 获取列表
     */
    @GetMapping("/getList")
    public TableDataInfo getList(String param) {
        // 分页
        startPage("createTime desc");
        // 查询
        List<WorkVo05> dataList = workUserService.getDataList(param);
        // 返回
        return getDataTable(dataList);
    }

    /**
     * 获取详情
     */
    @GetMapping("/getInfo/{userId}")
    public AjaxResult getInfo(@PathVariable Long userId) {
        // 查询
        WorkVo06 data = workUserService.getInfo(userId);
        // 返回
        return AjaxResult.success(data);
    }

    /**
     * 查询封禁
     */
    @GetMapping("/getBanned/{userId}")
    public AjaxResult getBanned(@PathVariable Long userId) {
        // 查询
        WorkVo07 data = workUserService.getBanned(userId);
        // 返回
        return AjaxResult.success(data);
    }

    /**
     * 查询认证
     */
    @GetMapping("/getAuth/{userId}")
    public AjaxResult getAuth(@PathVariable Long userId) {
        // 查询
        WorkVo11 data = workUserService.getAuth(userId);
        // 返回
        return AjaxResult.success(data);
    }

    /**
     * 封禁账号
     */
    @SubmitRepeat
    @PostMapping("/banned")
    public AjaxResult banned(@Validated @RequestBody WorkVo08 workVo) {
        String reason = workVo.getBannedReason();
        if (!BannedTypeEnum.OTHER.equals(workVo.getBannedType())) {
            reason = workVo.getBannedType().getInfo();
        }
        ChatVo01 chatVo01 = new ChatVo01()
                .setUserId(workVo.getUserId())
                .setBannedType(workVo.getBannedType())
                .setReason(reason)
                .setBannedTime(workVo.getBannedTime());
        chatUserService.banned(chatVo01);
        return AjaxResult.success();
    }

    /**
     * 修改备注
     */
    @SubmitRepeat
    @PostMapping("/remark")
    public AjaxResult remark(@Validated @RequestBody WorkVo13 workVo) {
        chatUserService.setRemark(workVo.getUserId(), workVo.getRemark());
        return AjaxResult.success();
    }

}
