package com.platform.modules.common.controller;

import com.platform.common.constant.HeadConstant;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.sys.service.SysDictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 通用请求处理
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController extends BaseController {

    @Resource
    private SysDictService sysDictService;

    /**
     * 查询字典
     */
    @RequiresPermissions(HeadConstant.PERM_ADMIN)
    @GetMapping("/queryDict/{dictType}")
    public AjaxResult queryDict(@PathVariable String dictType) {
        orderBy("dictSort");
        return AjaxResult.success(sysDictService.queryLabel(dictType));
    }

}
