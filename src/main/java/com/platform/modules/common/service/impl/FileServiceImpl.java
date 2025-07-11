package com.platform.modules.common.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.thread.ThreadUtil;
import com.platform.common.upload.service.UploadService;
import com.platform.common.upload.vo.UploadFileVo;
import com.platform.modules.chat.domain.ChatVoice;
import com.platform.modules.chat.service.ChatVoiceService;
import com.platform.modules.common.service.FileService;
import com.platform.modules.work.tencent.TencentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Service("fileService")
public class FileServiceImpl implements FileService {

    @Resource
    private UploadService uploadService;

    @Resource
    private ChatVoiceService chatVoiceService;

    @Autowired
    private TencentBuilder tencentBuilder;

    @Override
    public Dict getUploadToken() {
        return uploadService.getFileToken();
    }

    @Override
    public UploadFileVo uploadFile(MultipartFile file) {
        return uploadService.uploadFile(file);
    }

    @Override
    public void uploadVoice(Long msgId, String voicePath) {
        // 新增
        chatVoiceService.addVoice(msgId, voicePath);
        // 解析
        this.parsingVoice(msgId, voicePath);
    }

    @Override
    public String audio2Text(Long msgId) {
        ChatVoice chatVoice = chatVoiceService.getById(msgId);
        if (chatVoice == null) {
            return "";
        }
        String voiceText = chatVoice.getVoiceText();
        if (!StringUtils.isEmpty(voiceText)) {
            return voiceText;
        }
        // 解析
        this.parsingVoice(msgId, chatVoice.getVoiceUrl());
        return "正在识别，请稍后再试";
    }

    /**
     * 解析声音
     */
    private void parsingVoice(Long msgId, String voicePath) {
        ThreadUtil.execAsync(() -> {
            String voiceText = tencentBuilder.audio2Text(voicePath);
            ChatVoice chatVoice = new ChatVoice()
                    .setMsgId(msgId)
                    .setVoiceText(voiceText);
            chatVoiceService.updateById(chatVoice);
        });
    }

}
