package com.platform.modules.work.controller;

import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.work.service.WorkMineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 我的
 */
@RestController
@Slf4j
@RequestMapping("/work/mine")
public class WorkMineController extends BaseController {

    @Resource
    private WorkMineService workMineService;

    /**
     * 获取基本信息
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        return AjaxResult.success(workMineService.getInfo());
    }

    /**
     * 刷新
     */
    @GetMapping(value = {"/refresh/", "/refresh/{cid}"})
    public AjaxResult refresh(@PathVariable(required = false) String cid) {
        workMineService.refresh(cid);
        return AjaxResult.success();
    }

}
