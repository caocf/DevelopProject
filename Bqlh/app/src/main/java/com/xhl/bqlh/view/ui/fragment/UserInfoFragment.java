package com.xhl.bqlh.view.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.AppDelegate;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.UserInfo;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.model.event.CommonEvent;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.custom.RoundedImageView;
import com.xhl.bqlh.view.helper.DialogMaker;
import com.xhl.bqlh.view.helper.EventHelper;
import com.xhl.bqlh.view.helper.PhotoHelper;
import com.xhl.bqlh.view.helper.UploadHelper;
import com.xhl.xhl_library.Base.Anim.AnimType;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Summer on 2016/7/20.
 */
@ContentView(R.layout.fragment_user_info)
public class UserInfoFragment extends BaseAppFragment {

    @ViewInject(R.id.iv_my_pic)
    private RoundedImageView iv_my_pic;

    @ViewInject(R.id.tv_user_login_name)
    private TextView tv_user_login_name;

    @ViewInject(R.id.tv_user_name)
    private TextView tv_user_name;

    @ViewInject(R.id.tv_user_phone)
    private TextView tv_user_phone;

    @ViewInject(R.id.tv_user_mail)
    private TextView tv_user_mail;

    @ViewInject(R.id.tv_user_address)
    private TextView tv_user_address;

    @Event(R.id.iv_my_pic)
    private void onIvDetailsCkicl(View view) {
        if (TextUtils.isEmpty(AppDelegate.appContext.getUserFaceImage())) {
            takeCamera();
        } else {
            getSumContext().pushFragmentToBackStack(ImageDetailsFragment.class, AppDelegate.appContext.getUserFaceImage());
        }
    }

    @Event(R.id.ll_user_face)
    private void onFaceClick(View view) {
        takeCamera();
    }

    private void takeCamera() {
        AlertDialog.Builder builder = DialogMaker.getDialog(getContext());
        builder.setTitle("头像上传");
        CharSequence[] item = {"相册", "拍照"};
        builder.setItems(item, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        showDICM();
                        break;
                    case 1:
                        startCamera();
                        break;
                }
            }
        });
        builder.create().show();
    }

    //相册
    private void showDICM() {
        mPhotoHelper.showDICM();
    }

    //相机
    private void startCamera() {
        mPhotoHelper.capture();
    }


    @Event(R.id.ll_account)
    private void onAccountClick(View view) {
        getSumContext().pushFragmentToBackStack(UserAccountFragment.class, null, AnimType.ANIM_RIGHT);
    }

    @Event(R.id.ll_fix_pwd)
    private void onFixPwdClick(View view) {
        getSumContext().pushFragmentToBackStack(UserFixPwdFragment.class, null, AnimType.ANIM_RIGHT);
    }

    @Event(R.id.ll_user_phone)
    private void onPhoneClick(View view) {
        String title = "修改手机号";
        String hint = "请输入新手机号";
        UserInfo oldText = AppDelegate.appContext.getUserInfo();
        String tag = "1";
        getSumContext().pushFragmentToBackStack(UserInfoUpdateFragment.class, new Object[]{title, hint, oldText, tag});
    }

    @Event(R.id.ll_user_email)
    private void onEmailClick(View view) {
        String title = "修改邮箱";
        String hint = "请输入新邮箱";
        UserInfo oldText = AppDelegate.appContext.getUserInfo();
        String tag = "2";
        getSumContext().pushFragmentToBackStack(UserInfoUpdateFragment.class, new Object[]{title, hint, oldText, tag});
    }

    @Event(R.id.ll_user_address)
    private void onAddressClick(View view) {
        String title = "修改地址";
        String hint = "请输入新地址";
        UserInfo oldText = AppDelegate.appContext.getUserInfo();
        String tag = "3";
        getSumContext().pushFragmentToBackStack(UserInfoUpdateFragment.class, new Object[]{title, hint, oldText, tag});
    }

    @Override
    public void onBackWithData(Object data) {
        super.onBackWithData(data);
        if (data != null) {
            loadInfo();
        }
    }

    @Override
    protected void initParams() {

        super.initBackBar("用户信息", true, false);

        loadInfo();
    }

    private void loadInfo() {
        UserInfo userInfo = AppDelegate.appContext.getUserInfo();
        setInfo(tv_user_login_name, userInfo.userId);
        setInfo(tv_user_name, userInfo.liableName);
        setInfo(tv_user_phone, userInfo.liablePhone);
        setInfo(tv_user_mail, userInfo.liableMail);
        setInfo(tv_user_address, userInfo.address);

        String faceImage = AppDelegate.appContext.getUserFaceImage();
        if (!TextUtils.isEmpty(faceImage)) {
            iv_my_pic.LoadDrawable(faceImage);
        }
    }

    private void setInfo(TextView textView, String info) {
        if (TextUtils.isEmpty(info)) {
            info = "";
        }
        textView.setText(info);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PhotoHelper.CAPTURE_PHOTO_DEFAULT_CODE:
                mPhotoHelper.startPhotoZoom(Uri.fromFile(mPhotoHelper.getPhoto()));
                break;

            case PhotoHelper.DICM_PHOTO_DEFAULT_CODE:
                if (data != null) {
                    mPhotoHelper.startPhotoZoom(data.getData());
                }
                break;
            case PhotoHelper.CROP_PHOTO_DEFAULT_CODE:
                if (data != null) {
                    Bitmap bitmap = mPhotoHelper.getCropBitmap();
                    uploadBitmap(bitmap);
                }
                break;
        }
    }

    //直接上传bitmap字节到服务器
    private void uploadBitmap(Bitmap f) {
        UploadHelper.uploadBitmap(f, new UploadHelper.uploadFileListener() {
            @Override
            public void onSuccess(String ftpFile) {
                saveFace(ftpFile);
            }

            @Override
            public void onFailed(String msg) {
            }
        });
    }

    private void saveFace(final String file) {
        ApiControl.getApi().userUpdateImage(file, new DefaultCallback<ResponseModel<Object>>() {
            @Override
            public void success(ResponseModel<Object> result) {
                UserInfo userInfo = getAppApplication().getUserInfo();
                userInfo.headImage = file;
                getAppApplication().saveLoginInfo(userInfo);

                iv_my_pic.LoadDrawable(AppDelegate.appContext.getUserFaceImage());

                EventHelper.postCommonEvent(CommonEvent.ET_RELOAD_USER_IMAGE_INFO);
            }

            @Override
            public void finish() {

            }
        });
    }

}
