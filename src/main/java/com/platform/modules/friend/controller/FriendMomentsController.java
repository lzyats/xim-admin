package com.platform.modules.friend.controller;

import java.util.List;

import javax.annotation.Resource;

import com.platform.common.aspectj.AppLog;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.web.page.TableDataInfo;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.friend.vo.FriendVo02;
import org.springframework.web.bind.annotation.*;
import com.platform.modules.friend.service.FriendMomentsService;
import com.platform.modules.friend.domain.FriendMoments;
import com.platform.common.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import com.platform.modules.friend.vo.FriendVo01;

/**
 * <p>
 * 朋友圈动态表 控制层
 * </p>
 */
@RestController
@RequestMapping("/friend/moments")
public class FriendMomentsController extends BaseController {

    private final static String title = "朋友圈动态表";

    @Resource
    private FriendMomentsService friendMomentsService;

    /**
     * 列表数据 TODO
     */
    @RequiresPermissions(value = {"friend:moments:list"})
    @GetMapping(value = "/list")
    public TableDataInfo list(FriendMoments friendMoments) {
        startPage("createTime desc");

        List<FriendMoments> list = friendMomentsService.queryList(friendMoments);
        return getDataTable(list);
    }

    /**
     * 详细信息 TODO
     */
    @RequiresPermissions(value = {"friend:moments:query"})
    @GetMapping("/info/{momentId}")
    public AjaxResult getInfo(@PathVariable Long momentId) {
        return AjaxResult.success(friendMomentsService.getById(momentId));
    }

    /**
     * 新增数据 TODO
     */
    @RequiresPermissions(value = {"friend:moments:add"})
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody FriendVo01 friendVo) {
        //friendMomentsService.add(friendMoments);
        friendMomentsService.addMoments(friendVo);
        return AjaxResult.successMsg("新增成功");
    }

    /**
     * 修改数据 TODO
     */
    @RequiresPermissions(value = {"friend:moments:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody FriendVo02 friendVo2) {

        friendMomentsService.editMoments(friendVo2);
        return AjaxResult.successMsg("修改成功");
    }

    /**
     * 删除数据 TODO
     */
    @RequiresPermissions(value = {"friend:moments:remove"})
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        friendMomentsService.deleteById(id);
        return AjaxResult.successMsg("删除成功");
    }

    /**
     * 批量删除数据 TODO
     */
    @RequiresPermissions(value = {"friend:moments:remove"})
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/deleteBatch/{ids}")
    public AjaxResult deleteBatch(@PathVariable Long[] ids) {
        friendMomentsService.deleteByIds(ids);
        return AjaxResult.successMsg("删除成功");
    }



}

