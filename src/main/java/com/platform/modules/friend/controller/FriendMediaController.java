package com.platform.modules.friend.controller;

import java.util.List;

import javax.annotation.Resource;

import com.platform.common.aspectj.AppLog;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.web.page.TableDataInfo;
import com.platform.common.web.domain.AjaxResult;
import org.springframework.web.bind.annotation.*;
import com.platform.modules.friend.service.FriendMediaService;
import com.platform.modules.friend.domain.FriendMedia;
import com.platform.common.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;

/**
 * <p>
 * 朋友圈媒体资源表 控制层
 * </p>
 */
@RestController
@RequestMapping("/friend/media")
public class FriendMediaController extends BaseController {

    private final static String title = "朋友圈媒体资源表";

    @Resource
    private FriendMediaService friendMediaService;

    /**
     * 列表数据 TODO
     */
    @RequiresPermissions(value = {"friend:media:list"})
    @GetMapping(value = "/list")
    public TableDataInfo list(FriendMedia friendMedia) {
        startPage();
        List<FriendMedia> list = friendMediaService.queryList(friendMedia);
        return getDataTable(list);
    }

    /**
     * 详细信息 TODO
     */
    @RequiresPermissions(value = {"friend:media:query"})
    @GetMapping("/info/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(friendMediaService.getById(id));
    }

    /**
     * 新增数据 TODO
     */
    @RequiresPermissions(value = {"friend:media:add"})
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody FriendMedia friendMedia) {
        friendMediaService.add(friendMedia);
        return AjaxResult.successMsg("新增成功");
    }

    /**
     * 修改数据 TODO
     */
    @RequiresPermissions(value = {"friend:media:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody FriendMedia friendMedia) {
        friendMediaService.updateById(friendMedia);
        return AjaxResult.successMsg("修改成功");
    }

    /**
     * 删除数据 TODO
     */
    @RequiresPermissions(value = {"friend:media:remove"})
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        friendMediaService.deleteById(id);
        return AjaxResult.successMsg("删除成功");
    }

    /**
     * 批量删除数据 TODO
     */
    @RequiresPermissions(value = {"friend:media:remove"})
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/deleteBatch/{ids}")
    public AjaxResult deleteBatch(@PathVariable Long[] ids) {
        friendMediaService.deleteByIds(ids);
        return AjaxResult.successMsg("删除成功");
    }

    /**
     * 状态修改 TODO
     */
    @RequiresPermissions(value = {"friend:media:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @PostMapping("/status")
    public AjaxResult status(@RequestBody FriendMedia friendMedia) {
        friendMediaService.status(friendMedia);
        return AjaxResult.success();
    }

}

