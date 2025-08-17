package com.platform.modules.friend.controller;

import java.util.List;

import javax.annotation.Resource;

import com.platform.common.aspectj.AppLog;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.web.page.TableDataInfo;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.friend.dao.FriendMomentsDao;
import com.platform.modules.friend.service.impl.FriendMomentsServiceImpl;
import com.platform.modules.friend.vo.FriendVo02;
import com.platform.modules.friend.vo.MomentVo03;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequestMapping("/friend/moments")
public class FriendMomentsController extends BaseController {

    private final static String title = "朋友圈动态表";

    @Resource
    private FriendMomentsService friendMomentsService;

    @Resource
    private FriendMomentsDao friendMomentsDao;

    @Resource
    private FriendMomentsServiceImpl friendMomentsServiceimpl;

    /**
     * 列表数据 TODO
     */
    @RequiresPermissions(value = {"friend:moments:list"})
    @GetMapping(value = "/list")
    public TableDataInfo list(FriendMoments friendMoments) {
        startPage("createTime desc");

        return getDataTable(friendMomentsService.queryLists(friendMoments));
    }

    /**
     * 详细信息 TODO
     */
    @RequiresPermissions(value = {"friend:moments:query"})
    @GetMapping("/info/{momentId}")
    public AjaxResult getInfo(@PathVariable Long momentId) {
        MomentVo03 info= friendMomentsDao.getMomentsByMomentId(momentId);
        log.info("查询值：{}",info);
        return AjaxResult.success(info);
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
    @GetMapping("/deleted/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        // 发送广播 先广播，不然删除了没有任何记录了
        friendMomentsServiceimpl.getmoments(id,1);
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

