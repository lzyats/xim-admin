
package com.platform.modules.operate.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.chat.domain.ChatPortrait;
import com.platform.modules.chat.enums.ChatTalkEnum;
import com.platform.modules.chat.service.ChatPortraitService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 头像管理 控制层
 * </p>
 */
@RestController
@RequestMapping("/operate/portrait")
public class OperatePortraitController extends BaseController {

    private final static String title = "头像管理";

    @Resource
    private ChatPortraitService chatPortraitService;

    /**
     * 列表数据
     */
    @RequiresPermissions(value = {"operate:portrait:user"})
    @GetMapping(value = "/user")
    public TableDataInfo user(ChatPortrait chatPortrait) {
        startPage();
        List<ChatPortrait> list = chatPortraitService.queryList(chatPortrait.setChatType(ChatTalkEnum.FRIEND));
        return getDataTable(list);
    }

    /**
     * 列表数据
     */
    @RequiresPermissions(value = {"operate:portrait:group"})
    @GetMapping(value = "/group")
    public TableDataInfo group(ChatPortrait chatPortrait) {
        startPage();
        List<ChatPortrait> list = chatPortraitService.queryList(chatPortrait.setChatType(ChatTalkEnum.GROUP));
        return getDataTable(list);
    }

    /**
     * 详细信息
     */
    @RequiresPermissions(value = {"operate:portrait:query"})
    @GetMapping("/info/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(chatPortraitService.getById(id));
    }

    /**
     * 新增数据
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:portrait:add"})
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @DemoRepeat
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody ChatPortrait chatPortrait) {
        chatPortraitService.add(chatPortrait);
        return AjaxResult.success();
    }

    /**
     * 修改数据
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:portrait:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody ChatPortrait chatPortrait) {
        chatPortraitService.updateById(chatPortrait.setChatType(null));
        return AjaxResult.success();
    }

    /**
     * 修改数据
     */
    @SubmitRepeat
    @RequiresPermissions(value = {"operate:portrait:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @PostMapping("/status")
    public AjaxResult status(@RequestBody ChatPortrait chatPortrait) {
        chatPortraitService.update(chatPortrait);
        return AjaxResult.success();
    }

    /**
     * 删除数据
     */
    @SubmitRepeat
    @DemoRepeat
    @RequiresPermissions(value = {"operate:portrait:remove"})
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @GetMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        chatPortraitService.deleteById(id);
        return AjaxResult.success();
    }

}

