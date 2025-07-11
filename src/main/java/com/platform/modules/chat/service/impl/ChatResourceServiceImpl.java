package com.platform.modules.chat.service.impl;

import com.platform.common.upload.service.UploadService;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatResourceDao;
import com.platform.modules.chat.domain.ChatResource;
import com.platform.modules.chat.service.ChatResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 聊天资源 服务层实现
 * </p>
 */
@Service("chatResourceService")
public class ChatResourceServiceImpl extends BaseServiceImpl<ChatResource> implements ChatResourceService {

    @Resource
    private ChatResourceDao chatResourceDao;

    @Resource
    private UploadService uploadService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatResourceDao);
    }

    @Override
    public List<ChatResource> queryList(ChatResource t) {
        List<ChatResource> dataList = chatResourceDao.queryList(t);
        return dataList;
    }

    @Override
    public void batchDelete(List<ChatResource> dataList) {
        // 转换
        List<List<ChatResource>> batchList = this.batch(dataList, 1000);
        // 删除远端文件
        batchList.forEach(batch -> {
            uploadService.delFile(batch.stream().map(ChatResource::getPath).collect(Collectors.toList()));
        });
        // 删除本地文件
        this.deleteByIds(dataList.stream().map(ChatResource::getResourceId).collect(Collectors.toList()));
    }

}
