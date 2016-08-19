package com.xhl.bqlh.business.view.helper;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.xhl.bqlh.business.AppConfig.NetWorkConfig;
import com.xhl.bqlh.business.AppDelegate;
import com.xhl.bqlh.business.Model.Base.UploadResponse;
import com.xhl.xhl_library.utils.BitmapUtil;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/1/8.
 */
public class UploadHelper {

    //上传图片地址
    public static final String uploadFileUrl = NetWorkConfig.uploadFilePath;

    public static void uploadFile(String file, uploadFileListener listener) {
        if (TextUtils.isEmpty(file)) {
            if (listener != null) {
                listener.onFailed("upload files is null");
            }
            return;
        }
        File file1 = new File(file);
        if (!file1.exists()) {
            if (listener != null) {
                listener.onFailed("upload files is not exit");
            }
            return;
        }

        List<String> files = new ArrayList<>();
        files.add(file);
        uploadFile(files, listener);
    }

    public static void uploadFile(final List<String> files, final uploadFileListener listener) {

        if (TextUtils.isEmpty(uploadFileUrl)) {
            Logger.v("upload file url is null");
            if (listener != null) {
                listener.onFailed("upload file url is null");
            }
            return;
        }
        if (files.size() == 0) {
            Logger.v("upload files is null");
            if (listener != null) {
                listener.onFailed("upload files is null");
            }
            return;
        }
        x.task().run(new Runnable() {
            @Override
            public void run() {
                uploadAllFile(files, listener);
            }
        });
    }

    private static void uploadAllFile(List<String> files, final uploadFileListener listener) {
        RequestParams params = new RequestParams(uploadFileUrl);
        // 使用multipart表单上传文件
        params.setMultipart(true);
        //循环添加所以文件
        for (String file : files) {
            String fileName = file.substring(file.lastIndexOf("/") + 1, file.length());
            byte[] bytes = BitmapUtil.compressImage(file);
            //添加字节数组
            params.addBodyParameter("imgFile", bytes, "image/jpeg", fileName);

            Logger.v("upload file:" + file);
        }

        upload(listener, params);
    }

    public static void uploadBitmap(Bitmap bitmap, uploadFileListener listener) {
        if (bitmap == null) {
            return;
        }
        RequestParams params = new RequestParams(uploadFileUrl);
        // 使用multipart表单上传文件
        params.setMultipart(true);
        //循环添加所以文件
        byte[] bytes = BitmapUtil.bitmap2Bytes(bitmap);
        //添加字节数组
        params.addBodyParameter("imgFile", bytes, "image/jpeg", "upload_file");

        upload(listener, params);
    }

    private static void upload(final uploadFileListener listener, RequestParams params) {
        params.addBodyParameter("saveType", "0");

        String cookie = AppDelegate.appContext.mCookie;
        if (!TextUtils.isEmpty(cookie)) {
            params.setHeader("Cookie", cookie);
        }

        x.http().post(params, new Callback.CommonCallback<UploadResponse>() {

            @Override
            public void onSuccess(UploadResponse result) {
                if (listener != null) {
                    listener.onSuccess(result.getFilePath());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (listener != null) {
                    listener.onFailed(ex.getMessage());
                }
                Logger.v(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Logger.v("upload finish");
            }
        });
    }

    public interface uploadFileListener {
        //多文件以逗号隔开
        void onSuccess(String ftpFile);

        void onFailed(String msg);
    }

}
