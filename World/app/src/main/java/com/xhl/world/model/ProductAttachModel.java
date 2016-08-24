package com.xhl.world.model;

import com.xhl.world.config.NetWorkConfig;

/**
 * Created by Sum on 16/1/22.
 */
public class ProductAttachModel {

    private String attachmentId;
    private String module;
    private String entity;
    private String fileName;
    private String fileType;
    private String filePath;
    private String smallFilePath;
    private String largeFilePath;
    private String createUser;

    public String getAttachmentId() {
        return attachmentId;
    }

    public String getModule() {
        return module;
    }

    public String getEntity() {
        return entity;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public String getFilePath() {
        return NetWorkConfig.imageHost + filePath;
    }

    public String getSmallFilePath() {
        return NetWorkConfig.imageHost + smallFilePath;
    }

    public String getLargeFilePath() {
        return NetWorkConfig.imageHost + largeFilePath;
    }

    public String getCreateUser() {
        return createUser;
    }
}
