package com.xhl.bqlh.model.base;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by Sum on 16/4/25.
 */
@HttpResponse(parser = JsonResParser.class)
public class UploadResponse {
    private String filePath;
    private int retCode;

    public int getRetCode() {
        return retCode;
    }

    public String getFilePath() {
        return filePath;
    }
}
