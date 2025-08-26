package com.platform.modules.common.service;

import cn.hutool.core.lang.Dict;
import com.platform.common.upload.vo.UploadFileVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务
 */
public interface FileService {

    /**
     * 获取上传凭证
     */
    Dict getUploadToken(String fileExt);

    /**
     * 文件上传
     */
    UploadFileVo uploadFile(MultipartFile file);

    /**
     * 文件音频
     */
    void uploadVoice(Long msgId, String voicePath);

    /**
     * 文件音频
     */
    String audio2Text(Long msgId);

}
