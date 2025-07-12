package com.platform.modules.friend.controller;

import java.util.List;

import javax.annotation.Resource;

import com.platform.common.aspectj.AppLog;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.web.page.TableDataInfo;
import com.platform.common.web.domain.AjaxResult;
import org.springframework.web.bind.annotation.*;
import com.platform.modules.friend.service.FriendMediasService;
import com.platform.modules.friend.domain.FriendMedias;
import com.platform.common.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;

/**
 * <p>
 * 朋友圈媒体资源表 控制层
 * </p>
 */
@RestController
@RequestMapping("/friend/medias")
public class FriendMediasController extends BaseController {

    private final static String title = "朋友圈媒体资源表";

    @Resource
    private FriendMediasService friendMediasService;

    /**
     * 列表数据 TODO
     */
    @RequiresPermissions(value = {"friend:medias:list"})
    @GetMapping(value = "/list")
    public TableDataInfo list(FriendMedias friendMedias) {
        startPage();
        List<FriendMedias> list = friendMediasService.queryList(friendMedias);
        return getDataTable(list);
    }

    /**
     * 列表数据 TODO
     */
    @RequiresPermissions(value = {"friend:comments:list"})
    @GetMapping(value = "/listall/{momentId}")
    public TableDataInfo listall(@PathVariable Long momentId) {
        startPage("createTime");
        return getDataTable(friendMediasService.queryListall(momentId));
    }

    /**
     * 详细信息 TODO
     */
    @RequiresPermissions(value = {"friend:medias:query"})
    @GetMapping("/info/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(friendMediasService.getById(id));
    }

    /**
     * 新增数据 TODO
     */
    @RequiresPermissions(value = {"friend:medias:add"})
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody FriendMedias friendMedias) {
        friendMediasService.add(friendMedias);
        return AjaxResult.successMsg("新增成功");
    }

    /**
     * 修改数据 TODO
     */
    @RequiresPermissions(value = {"friend:medias:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody FriendMedias friendMedias) {
        friendMediasService.updateById(friendMedias);
        return AjaxResult.successMsg("修改成功");
    }

    /**
     * 删除数据 TODO
     */
    @RequiresPermissions(value = {"friend:medias:remove"})
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        friendMediasService.deleteById(id);
        return AjaxResult.successMsg("删除成功");
    }

    /**
     * 批量删除数据 TODO
     */
    @RequiresPermissions(value = {"friend:medias:remove"})
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/deleteBatch/{ids}")
    public AjaxResult deleteBatch(@PathVariable Long[] ids) {
        friendMediasService.deleteByIds(ids);
        return AjaxResult.successMsg("删除成功");
    }


}

