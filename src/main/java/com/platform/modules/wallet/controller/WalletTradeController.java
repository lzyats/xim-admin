package com.platform.modules.wallet.controller;

import com.github.pagehelper.PageInfo;
import com.platform.common.validation.ValidationUtil;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.wallet.service.WalletTradeService;
import com.platform.modules.wallet.vo.WalletVo01;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 钱包交易 控制层
 * </p>
 */
@RestController
@RequestMapping("/wallet/trade")
public class WalletTradeController extends BaseController {

    @Resource
    private WalletTradeService walletTradeService;

    /**
     * 交易-列表
     */
    @RequiresPermissions("wallet:trade:list")
    @GetMapping("/list")
    public TableDataInfo list(WalletVo01 walletVo) {
        // 校验
        ValidationUtil.verify(walletVo);
        // 分页
        startPage("createTime desc");
        // 查询
        PageInfo list = walletTradeService.queryTradeList(walletVo);
        return getDataTable(list);
    }

    /**
     * 交易-详情
     */
    @RequiresPermissions("wallet:trade:list")
    @GetMapping("/info/{tradeId}")
    public Object info(@PathVariable Long tradeId) {
        // 查询
        Object data = walletTradeService.queryTradeInfo(tradeId);
        if (data instanceof PageInfo) {
            return getDataTable((PageInfo) data);
        }
        return AjaxResult.success(data);
    }

}

