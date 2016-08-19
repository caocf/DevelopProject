package com.xhl.bqlh.business.doman;

import android.text.TextUtils;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.App.ShopSignModel;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.doman.callback.ContextValue;
import com.xhl.bqlh.business.view.helper.UploadHelper;

import org.xutils.common.Callback;

/**
 * Created by Sum on 16/4/26.
 */
public class ShopSignHelper extends BaseValue {

    public static final int TYPE_GET_REMARKE = 1;
    public static final int TYPE_GET_IMAG1 = 3;
    public static final int TYPE_GET_IMAG2 = 4;
    public static final int TYPE_GET_IMAG3 = 5;
    //签到的其他数据
    public static final int TYPE_GET_OTHER = 6;

    //签到完成
    public static final int TYPE_RES_FINISH = 7;

    public ShopSignHelper(ContextValue value) {
        super(value);
    }

    private ShopSignModel mSign;

    public void sign() {
        mSign = (ShopSignModel) mValue.getValue(TYPE_GET_OTHER);

        String marker = (String) mValue.getValue(TYPE_GET_REMARKE);
        mSign.remark = marker;

        String imag1 = (String) mValue.getValue(TYPE_GET_IMAG1);

        String imag2 = (String) mValue.getValue(TYPE_GET_IMAG2);

        String imag3 = (String) mValue.getValue(TYPE_GET_IMAG3);

        progressShow("提交签到数据中...");

        if (!TextUtils.isEmpty(imag1)) {
            uploadImg1();
        } else if (!TextUtils.isEmpty(imag2)) {
            uploadImg2();
        } else if (!TextUtils.isEmpty(imag3)) {
            uploadImg3();
        }else {
            report();
        }
    }

    private void uploadImg1() {
        String path = (String) mValue.getValue(TYPE_GET_IMAG1);
        UploadHelper.uploadFile(path, new UploadHelper.uploadFileListener() {
            @Override
            public void onSuccess(String ftpFile) {
                mSign.img1 = ftpFile;
                uploadImg2();
            }

            @Override
            public void onFailed(String msg) {
                uploadImg2();
            }
        });
    }

    private void uploadImg2() {
        String path = (String) mValue.getValue(TYPE_GET_IMAG2);
        UploadHelper.uploadFile(path, new UploadHelper.uploadFileListener() {
            @Override
            public void onSuccess(String ftpFile) {
                mSign.img2 = ftpFile;
                uploadImg3();
            }

            @Override
            public void onFailed(String msg) {
                uploadImg3();
            }
        });
    }

    private void uploadImg3() {
        String path = (String) mValue.getValue(TYPE_GET_IMAG3);
        UploadHelper.uploadFile(path, new UploadHelper.uploadFileListener() {
            @Override
            public void onSuccess(String ftpFile) {
                mSign.img3 = ftpFile;
                report();
            }

            @Override
            public void onFailed(String msg) {
                report();
            }
        });
    }


    private void report() {

        ApiControl.getApi().shopCheckIn(mSign, new Callback.CommonCallback<ResponseModel<Object>>() {
            @Override
            public void onSuccess(ResponseModel<Object> result) {
                if (result.isSuccess()) {
                    showValue(TYPE_RES_FINISH, result.getObj());
                } else {
                    toastShow(result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dialogHide();
            }
        });

    }
}
