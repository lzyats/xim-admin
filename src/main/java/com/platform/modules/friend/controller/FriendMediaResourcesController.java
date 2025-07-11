package com.platform.modules.friend.controller;

import java.util.List;

import javax.annotation.Resource;

import com.platform.common.aspectj.AppLog;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.web.page.TableDataInfo;
import com.platform.common.web.domain.AjaxResult;
import org.springframework.web.bind.annotation.*;
import com.platform.modules.friend.service.FriendMediaResourcesService;
import com.platform.modules.friend.domain.FriendMediaResources;
import com.platform.common.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;

/**
 * <p>
 * 朋友圈媒体资源表 控制层
 * </p>
 */
@RestController
@RequestMapping("/friend/media/resources")
public class FriendMediaResourcesController extends BaseController {

    private final static String title = "朋友圈媒体资源表";

    @Resource
    private FriendMediaResourcesService friendMediaResourcesService;

    /**
     * 列表数据 TODO
     */
    @RequiresPermissions(value = {"friend:media:resources:list"})
    @GetMapping(value = "/list")
    public TableDataInfo list(FriendMediaResources friendMediaResources) {
        startPage();
        List<FriendMediaResources> list = friendMediaResourcesService.queryList(friendMediaResources);
        return getDataTable(list);
    }

    /**
     * 详细信息 TODO
     */
    @RequiresPermissions(value = {"friend:media:resources:query"})
    @GetMapping("/info/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(friendMediaResourcesService.getById(id));
    }

    /**
     * 新增数据 TODO
     */
    @RequiresPermissions(value = {"friend:media:resources:add"})
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody FriendMediaResources friendMediaResources) {
        friendMediaResourcesService.add(friendMediaResources);
        return AjaxResult.successMsg("新增成功");
    }

    /**
     * 修改数据 TODO
     */
    @RequiresPermissions(value = {"friend:media:resources:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody FriendMediaResources friendMediaResources) {
        friendMediaResourcesService.updateById(friendMediaResources);
        return AjaxResult.successMsg("修改成功");
    }

    /**
     * 删除数据 TODO
     */
    @RequiresPermissions(value = {"friend:media:resources:remove"})
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        friendMediaResourcesService.deleteById(id);
        return AjaxResult.successMsg("删除成功");
    }

    /**
     * 批量删除数据 TODO
     */
    @RequiresPermissions(value = {"friend:media:resources:remove"})
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/deleteBatch/{ids}")
    public AjaxResult deleteBatch(@PathVariable Long[] ids) {
        friendMediaResourcesService.deleteByIds(ids);
        return AjaxResult.successMsg("删除成功");
    }

    /**
     * 状态修改 TODO
     */
    @RequiresPermissions(value = {"friend:media:resources:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @PostMapping("/status")
    public AjaxResult status(@RequestBody FriendMediaResources friendMediaResources) {
        friendMediaResourcesService.status(friendMediaResources);
        return AjaxResult.success();
    }

}

