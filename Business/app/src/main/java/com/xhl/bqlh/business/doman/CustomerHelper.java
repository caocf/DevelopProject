package com.xhl.bqlh.business.doman;

import android.text.TextUtils;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.App.RegisterModel;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.doman.callback.ContextValue;
import com.xhl.bqlh.business.view.helper.UploadHelper;
import com.xhl.xhl_library.utils.LogonUtils;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.common.Callback;

import java.io.Serializable;

/**
 * Created by Sum on 16/4/25.
 */
public class CustomerHelper extends BaseValue implements Serializable {

    public static final int TYPE_REGISTER_SUCCESS = 1;
    public static final int TYPE_REGISTER_FAILED = 2;
    //门店信息提交审核
    public static final int TYPE_FIX_INFO_SUCCESS = 3;

    private RegisterModel mRegister;

    public CustomerHelper(ContextValue value) {
        super(value);
    }

    private boolean check() {
        RegisterModel register = (RegisterModel) mValue.getValue(0);
        mRegister = register;
        Logger.v("info:" + register.toString());
        if (TextUtils.isEmpty(register.retailerImgPath) && TextUtils.isEmpty(register.retailerImg)) {
            toastShow("门头照图片为空");
            return false;
        }
        /*if (TextUtils.isEmpty(register.businessLicencePath)) {
            toastShow("营业执照图片为空");
            return false;
        }*/
        if (TextUtils.isEmpty(register.companyName)) {
            toastShow("门店名称为空");
            return false;
        }
        if (TextUtils.isEmpty(register.liableName)) {
            toastShow("负责人为空");
            return false;
        }
        if (TextUtils.isEmpty(register.liablePhone)) {
            toastShow("负责人手机号为空");
            return false;
        }
        if (!LogonUtils.matcherLogonPhone(register.liablePhone)) {
            toastShow("手机号格式有误");
            return false;
        }
       /* if (TextUtils.isEmpty(register.businessLicenceId)) {
            toastShow("营业执照号为空");
            return false;
        }*/
        if (register.coordinateX == 0 && register.coordinateY == 0) {
            toastShow("请标记门店位置");
            return false;
        }
        if (TextUtils.isEmpty(register.location)) {
            toastShow("收货地址为空");
            return false;
        }
        if (TextUtils.isEmpty(register.address)) {
            toastShow("详细地址为空");
            return false;
        }
        if (TextUtils.isEmpty(register.companyTypeId)) {
            toastShow("门店类型为空");
            return false;
        }
        if (TextUtils.isEmpty(register.loginUserName)) {
            toastShow("登陆用户名为空");
            return false;
        }
        return true;
    }

    public void confirmShop() {
        if (check()) {
            //做区域id判断
            if (TextUtils.isEmpty(mRegister.areaId)) {
                reLoadArea();
            } else {
                commit();
            }
        }
    }

    //提交信息
    private void commit() {
        if (TextUtils.isEmpty(mRegister.retailerImg) && !TextUtils.isEmpty(mRegister.retailerImgPath)) {
            uploadRetailerImgPath();
        } else if (TextUtils.isEmpty(mRegister.businessLicence) && !TextUtils.isEmpty(mRegister.businessLicencePath)) {
            uploadBusinessLicenceImgPath();
        } else {
            confirmOther();
        }
    }

    //再次添加区域id
    private void reLoadArea() {
        //注册地址
        ApiControl.getApi().registerShopAreaId(mRegister, new Callback.CommonCallback<ResponseModel<String>>() {
            @Override
            public void onSuccess(ResponseModel<String> result) {
                if (result.isSuccess()) {
                    mRegister.areaId = result.getObj();
                    commit();
                } else {
                    toastShow(result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                toastShow(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    //获取区域
    public void loadArea() {
        RegisterModel register = (RegisterModel) mValue.getValue(0);
        mRegister = register;
        String areaId = register.areaId;
        if (!TextUtils.isEmpty(areaId)) {
            return;
        }
        //注册地址
        ApiControl.getApi().registerShopAreaId(mRegister, new Callback.CommonCallback<ResponseModel<String>>() {
            @Override
            public void onSuccess(ResponseModel<String> result) {
                if (result.isSuccess()) {
                    mRegister.areaId = result.getObj();
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

            }
        });
    }

    private void confirmOther() {

        progressShow("提交信息中...");

        ApiControl.getApi().registerRetailer(mRegister, new Callback.CommonCallback<ResponseModel<Object>>() {
            @Override
            public void onSuccess(ResponseModel<Object> result) {
                if (result.isSuccess()) {
                    if (mRegister.fixInfo) {
                        mValue.showValue(TYPE_FIX_INFO_SUCCESS, null);
                    } else {
                        mValue.showValue(TYPE_REGISTER_SUCCESS, null);
                    }
                } else {
                    mValue.showValue(TYPE_REGISTER_FAILED, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                toastShow(ex.getMessage());
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

    private void uploadRetailerImgPath() {

        progressShow("上传门头照图片中");

        UploadHelper.uploadFile(mRegister.retailerImgPath, new UploadHelper.uploadFileListener() {
            @Override
            public void onSuccess(String ftpFile) {
                mRegister.retailerImg = ftpFile;
                dialogHide();
                uploadBusinessLicenceImgPath();
            }

            @Override
            public void onFailed(String msg) {
                dialogHide();
                toastShow(msg);
            }
        });
    }

    private void uploadBusinessLicenceImgPath() {
        progressShow("上传营业执照图片中");

        UploadHelper.uploadFile(mRegister.businessLicencePath, new UploadHelper.uploadFileListener() {
            @Override
            public void onSuccess(String ftpFile) {
                mRegister.businessLicence = ftpFile;
                dialogHide();
                confirmOther();
            }

            @Override
            public void onFailed(String msg) {
                dialogHide();
                toastShow(msg);
            }
        });
    }

}
