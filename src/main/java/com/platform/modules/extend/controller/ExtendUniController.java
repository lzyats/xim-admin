package com.platform.modules.extend.controller;

import com.platform.common.aspectj.AppLog;
import com.platform.common.aspectj.DemoRepeat;
import com.platform.common.enums.LogTypeEnum;
import com.platform.common.validation.ValidateGroup;
import com.platform.common.validation.ValidationUtil;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.extend.domain.UniItem;
import com.platform.modules.extend.enums.UniTypeEnum;
import com.platform.modules.extend.service.UniItemService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 小程序表 控制层
 * </p>
 */
@RestController
@RequestMapping("/extend/uni")
public class ExtendUniController extends BaseController {

    private final static String title = "小程序";

    @Resource
    private UniItemService uniItemService;

    /**
     * 列表数据
     */
    @RequiresPermissions(value = {"extend:uni:list"})
    @GetMapping(value = "/list")
    public TableDataInfo list(UniItem uniItem) {
        startPage();
        List<UniItem> list = uniItemService.queryList(uniItem);
        return getDataTable(list);
    }

    /**
     * 新增数据
     */
    @RequiresPermissions(value = {"extend:uni:add"})
    @AppLog(value = title, type = LogTypeEnum.ADD)
    @DemoRepeat
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody UniItem uniItem) {
        if (UniTypeEnum.MINI.equals(uniItem.getType())) {
            ValidationUtil.verify(uniItem, ValidateGroup.ONE.class);
        }
        uniItemService.addItem(uniItem);
        return AjaxResult.successMsg("新增成功");
    }

    /**
     * 修改数据
     */
    @RequiresPermissions(value = {"extend:uni:edit"})
    @AppLog(value = title, type = LogTypeEnum.EDIT)
    @DemoRepeat
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody UniItem uniItem) {
        if (UniTypeEnum.MINI.equals(uniItem.getType())) {
            ValidationUtil.verify(uniItem, ValidateGroup.ONE.class);
        }
        uniItemService.editItem(uniItem);
        return AjaxResult.successMsg("修改成功");
    }

    /**
     * 删除数据
     */
    @RequiresPermissions(value = {"extend:uni:remove"})
    @AppLog(value = title, type = LogTypeEnum.DELETE)
    @DemoRepeat
    @GetMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        uniItemService.deleteById(id);
        return AjaxResult.successMsg("删除成功");
    }

}

