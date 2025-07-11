package com.platform.modules.common.controller;

import com.platform.common.constant.HeadConstant;
import com.platform.common.exception.BaseException;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.common.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 通用请求处理
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Resource
    private FileService fileService;

    /**
     * 通用上传请求
     */
    @RequiresPermissions(HeadConstant.PERM_ADMIN)
    @PostMapping("/upload")
    public AjaxResult upload(MultipartFile file) {
        if (file == null) {
            throw new BaseException("上传文件不能为空");
        }
        return AjaxResult.success(fileService.uploadFile(file));
    }

    /**
     * 获取上传凭证
     */
    @GetMapping(value = "/getUploadToken")
    public AjaxResult getUploadToken() {
        return AjaxResult.success(fileService.getUploadToken());
    }

    /**
     * 音频转换
     */
    @GetMapping("/audio2Text/{msgId}")
    public AjaxResult getVoice(@PathVariable Long msgId) {
        return AjaxResult.success(fileService.audio2Text(msgId));
    }

}
