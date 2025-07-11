package com.platform.common.web.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.common.core.CharsetKit;
import com.platform.common.core.EnumUtils;
import com.platform.common.enums.*;
import com.platform.common.utils.ServletUtils;
import com.platform.common.web.domain.JsonDateDeserializer;
import com.platform.common.web.page.PageDomain;
import com.platform.common.web.page.TableDataInfo;
import com.platform.common.web.page.TableSupport;
import com.platform.modules.chat.enums.BannedTypeEnum;
import com.platform.modules.chat.enums.ChatTalkEnum;
import com.platform.modules.extend.enums.UniTypeEnum;
import com.platform.modules.sys.enums.SysMenuTypeEnum;
import com.platform.modules.wallet.enums.TradePayEnum;
import com.platform.modules.wallet.enums.TradeTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * web层通用数据处理
 */
@Slf4j
@CrossOrigin
public class BaseController {

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(JsonDateDeserializer.parseDate(text));
            }
        });
        // YesOrNoEnum 类型转换
        binder.registerCustomEditor(YesOrNoEnum.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(EnumUtils.toEnum(YesOrNoEnum.class, text));
            }
        });
        // BusinessType 类型转换
        binder.registerCustomEditor(LogTypeEnum.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(EnumUtils.toEnum(LogTypeEnum.class, text));
            }
        });
        // GenderTypeEnum 类型转换
        binder.registerCustomEditor(GenderEnum.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(EnumUtils.toEnum(GenderEnum.class, text));
            }
        });
        // DeviceEnum 类型转换
        binder.registerCustomEditor(DeviceEnum.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(EnumUtils.toEnum(DeviceEnum.class, text));
            }
        });
        // BannedTypeEnum 类型转换
        binder.registerCustomEditor(BannedTypeEnum.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(EnumUtils.toEnum(BannedTypeEnum.class, text));
            }
        });
        // TradeTypeEnum 类型转换
        binder.registerCustomEditor(TradeTypeEnum.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(EnumUtils.toEnum(TradeTypeEnum.class, text));
            }
        });
        // ApproveEnum 类型转换
        binder.registerCustomEditor(ApproveEnum.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(EnumUtils.toEnum(ApproveEnum.class, text));
            }
        });
        // ChatTalkEnum 类型转换
        binder.registerCustomEditor(ChatTalkEnum.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(EnumUtils.toEnum(ChatTalkEnum.class, text));
            }
        });
        // TradePayEnum 类型转换
        binder.registerCustomEditor(TradePayEnum.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(EnumUtils.toEnum(TradePayEnum.class, text));
            }
        });
        // TimeUnitEnum 类型转换
        binder.registerCustomEditor(TimeUnitEnum.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(EnumUtils.toEnum(TimeUnitEnum.class, text));
            }
        });
        // SysMenuTypeEnum 菜单类型
        binder.registerCustomEditor(SysMenuTypeEnum.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(EnumUtils.toEnum(SysMenuTypeEnum.class, text));
            }
        });
        // UniTypeEnum 菜单类型
        binder.registerCustomEditor(UniTypeEnum.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(EnumUtils.toEnum(UniTypeEnum.class, text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageDomain pageDomain = TableSupport.getPageDomain();
        startPage(PageDomain.escapeOrderBySql(pageDomain.getOrderBy()));
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage(String orderBy) {
        PageDomain pageDomain = TableSupport.getPageDomain();
        PageHelper.startPage(pageDomain.getPageNum(), pageDomain.getPageSize(), StrUtil.toUnderlineCase(orderBy));
    }

    /**
     * 设置排序分页数据
     */
    protected void orderBy(String orderBy) {
        PageHelper.orderBy(StrUtil.toUnderlineCase(orderBy));
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(List<?> list) {
        return formatData(list, new PageInfo(list).getTotal());
    }

    protected TableDataInfo getDataTable(List<?> list, PageDomain pageDomain) {
        return getDataTable(CollUtil.sub(list, pageDomain.getPageStart(), pageDomain.getPageEnd()));
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(PageInfo<?> list) {
        return formatData(list.getList(), list.getTotal());
    }

    /**
     * 格式化分页
     */
    private TableDataInfo formatData(List<?> list, Long total) {
        return new TableDataInfo(list, total);
    }

    /**
     * 设置请求excel请求
     */
    protected com.alibaba.excel.ExcelWriter startExcel(String template, String fileName) {
        com.alibaba.excel.ExcelWriter excelWriter = null;
        try {
            HttpServletResponse response = ServletUtils.getResponse();
            Resource resource = ResourceUtil.getResourceObj(template);
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(CharsetKit.UTF_8);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(fileName, CharsetKit.UTF_8) + ".xlsx");
            excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(resource.getStream()).build();
        } catch (Exception e) {
            log.error("导出异常", e);
        }
        return excelWriter;
    }
}
