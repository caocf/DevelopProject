package com.xhl.world.ui.utils;

import android.text.TextUtils;

import com.xhl.world.config.NetWorkConfig;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.xhl_library.utils.BitmapUtil;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/1/8.
 */
public class UploadHelper {

    //上传图片地址
    public static final String uploadFileUrl = NetWorkConfig.uploadFilePath;

    public static void uploadFile(String file, uploadFileListener listener) {

        List<String> files = new ArrayList<>();
        files.add(file);
        uploadFile(files, listener);
    }


    public static void uploadFile(List<String> files, final uploadFileListener listener) {

        if (TextUtils.isEmpty(uploadFileUrl)) {
            Logger.e("upload file url is null");
            return;
        }
        if (files.size() == 0) {
            return;
        }
        // /storage/emulated/0/Xhl/pic/1452750773487.jpg
        // /storage/emulated/0/Xhl/pic/1452750780465.jpg
        // /storage/emulated/0/Xhl/pic/1452750790321.jpg

        RequestParams params = new RequestParams(uploadFileUrl);
        // 使用multipart表单上传文件
        params.setMultipart(true);
        //循环添加所以文件
        for (String file : files) {
            String fileName = file.substring(file.lastIndexOf("/"), file.length());
            byte[] bytes = BitmapUtil.compressImage(file);
            //添加字节数组
            params.addBodyParameter("imgFile", bytes, "image/jpeg", fileName);

            Logger.v("upload file:" + file+" fileName:"+fileName);
        }
        x.http().post(params, new Callback.CommonCallback<ResponseModel<String>>() {

            @Override
            public void onSuccess(ResponseModel<String> result) {
                if (result.isSuccess()) {
                    Logger.v("image url:" + result.getResultObj());
                    if (listener != null) {
                        listener.onSuccess(result.getResultObj());
                    }
                } else {
                    if (listener != null) {
                        listener.onFailed(result.getMessage());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (listener != null) {
                    listener.onFailed(ex.getMessage());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Logger.e("upload finish");
            }
        });
    }

    public interface uploadFileListener {
        //多文件以逗号隔开
        void onSuccess(String ftpFile);

        void onFailed(String msg);
    }

}
