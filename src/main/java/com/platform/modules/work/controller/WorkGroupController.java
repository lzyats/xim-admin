package com.platform.modules.work.controller;

import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.chat.service.ChatGroupService;
import com.platform.modules.work.service.WorkGroupService;
import com.platform.modules.work.vo.WorkVo01;
import com.platform.modules.work.vo.WorkVo09;
import com.platform.modules.work.vo.WorkVo10;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 群组
 */
@RestController
@Slf4j
@RequestMapping("/work/group")
public class WorkGroupController extends BaseController {

    @Resource
    private WorkGroupService workGroupService;

    @Resource
    private ChatGroupService chatGroupService;

    /**
     * 获取列表
     */
    @GetMapping("/getList")
    public TableDataInfo getList(String param) {
        // 分页
        startPage("createTime desc");
        // 查询
        List<WorkVo09> dataList = workGroupService.getDataList(param);
        // 返回
        return getDataTable(dataList);
    }

    /**
     * 获取详情
     */
    @GetMapping("/getInfo/{groupId}")
    public AjaxResult getInfo(@PathVariable Long groupId) {
        // 查询
        WorkVo10 data = workGroupService.getInfo(groupId);
        // 返回
        return AjaxResult.success(data);
    }

    /**
     * 封禁群组
     */
    @SubmitRepeat
    @PostMapping("/banned")
    public AjaxResult banned(@Validated @RequestBody WorkVo01 workVo) {
        chatGroupService.banned(workVo.getGroupId(), workVo.getBanned());
        return AjaxResult.success();
    }

}
