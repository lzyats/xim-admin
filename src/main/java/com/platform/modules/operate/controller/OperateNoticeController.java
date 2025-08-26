package com.platform.modules.operate.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.redis.RedisJsonUtil;
import com.platform.common.redis.RedisUtils;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.chat.domain.ChatNotice;
import com.platform.modules.chat.service.ChatNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 通知公告 控制层
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/operate/notice")
public class OperateNoticeController extends BaseController {

    private final static String title = "通知公告";

    @Resource
    private ChatNoticeService chatNoticeService;

    @Autowired
    private RedisJsonUtil redisJsonUtil;

    String redisKey = AppConstants.REDIS_COMMON_NOTIC + "list:*";

    /**
     * 列表数据
     */
    @RequiresPermissions(value = {"operate:notice:list"})
    @GetMapping(value = "/list")
    public TableDataInfo list(ChatNotice chatNotice) {
        startPage("createTime desc");
        return getDataTable(chatNoticeService.queryList(chatNotice));
    }

    /**
     * 新增数据
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:notice:add"})
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @DemoRepeat
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody ChatNotice chatNotice) {
        chatNoticeService.add(chatNotice);
        // 清除缓存
        redisJsonUtil.delete(redisKey);
        return AjaxResult.success();
    }

    /**
     * 修改数据
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:notice:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody ChatNotice chatNotice) {
        chatNoticeService.updateById(chatNotice);
        // 清除缓存
        boolean deletedCount = redisJsonUtil.delete(redisKey);
        log.info("批量删除完成，成功删除key数量：{}", deletedCount);
        return AjaxResult.success();
    }

    /**
     * 删除数据
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:notice:remove"})
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/delete/{id}")
    @DemoRepeat
    public AjaxResult delete(@PathVariable Long id) {
        chatNoticeService.deleteById(id);
        // 清除缓存
        redisJsonUtil.delete(redisKey);
        return AjaxResult.success();
    }

    /**
     * 拷贝
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:notice:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @GetMapping("/copy/{noticeId}")
    public AjaxResult copy(@PathVariable Long noticeId) {
        chatNoticeService.copy(noticeId);
        // 清除缓存
        redisJsonUtil.delete(redisKey);
        return AjaxResult.success();
    }

}

