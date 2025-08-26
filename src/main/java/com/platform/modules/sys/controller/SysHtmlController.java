package com.platform.modules.sys.controller;

import java.util.List;

import javax.annotation.Resource;

import com.platform.common.aspectj.AppLog;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.redis.RedisJsonUtil;
import com.platform.common.web.page.TableDataInfo;
import com.platform.common.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.platform.modules.sys.service.SysHtmlService;
import com.platform.modules.sys.domain.SysHtml;
import com.platform.common.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;

/**
 * <p>
 * APP网页定制 控制层
 * </p>
 */
@RestController
@RequestMapping("/sys/html")
public class SysHtmlController extends BaseController {

    private final static String title = "APP网页定制";

    @Resource
    private SysHtmlService sysHtmlService;

    @Autowired
    private RedisJsonUtil redisJsonUtil;

    String redisKey = AppConstants.REDIS_COMMON_SYS + ":html";

    /**
     * 列表数据 TODO
     */
    @RequiresPermissions(value = {"sys:html:list"})
    @GetMapping(value = "/list")
    public TableDataInfo list(SysHtml sysHtml) {
        startPage();
        List<SysHtml> list = sysHtmlService.queryList(sysHtml);
        return getDataTable(list);
    }

    /**
     * 详细信息 TODO
     */
    @RequiresPermissions(value = {"sys:html:query"})
    @GetMapping("/info/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(sysHtmlService.getById(id));
    }

    /**
     * 新增数据 TODO
     */
    @RequiresPermissions(value = {"sys:html:add"})
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysHtml sysHtml) {
        sysHtmlService.add(sysHtml);
        // 清除缓存
        redisJsonUtil.delete(redisKey);
        return AjaxResult.successMsg("新增成功");
    }

    /**
     * 修改数据 TODO
     */
    @RequiresPermissions(value = {"sys:html:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody SysHtml sysHtml) {
        sysHtmlService.updateById(sysHtml);
        // 清除缓存
        redisJsonUtil.delete(redisKey);
        return AjaxResult.successMsg("修改成功");
    }

    /**
     * 删除数据 TODO
     */
    @RequiresPermissions(value = {"sys:html:remove"})
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        sysHtmlService.deleteById(id);
        // 清除缓存
        redisJsonUtil.delete(redisKey);
        return AjaxResult.successMsg("删除成功");
    }

    /**
     * 批量删除数据 TODO
     */
    @RequiresPermissions(value = {"sys:html:remove"})
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/deleteBatch/{ids}")
    public AjaxResult deleteBatch(@PathVariable Long[] ids) {
        sysHtmlService.deleteByIds(ids);
        return AjaxResult.successMsg("删除成功");
    }


}

