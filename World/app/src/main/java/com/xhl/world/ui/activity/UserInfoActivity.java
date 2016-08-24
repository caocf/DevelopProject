package com.xhl.world.ui.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.xhl.world.AppApplication;
import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.user.UserInfo;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.fragment.AccountSafeFragment;
import com.xhl.world.ui.fragment.AddressMyFragment;
import com.xhl.world.ui.fragment.RegisterFragment;
import com.xhl.world.ui.utils.DialogMaker;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.world.ui.utils.ToastUtil;
import com.xhl.world.ui.utils.UploadHelper;
import com.xhl.world.ui.view.RoundedImageView;
import com.xhl.xhl_library.AppFileConfig;
import com.xhl.xhl_library.ui.view.CityPopupWindow;
import com.xhl.xhl_library.ui.view.RippleView;
import com.xhl.xhl_library.utils.TimeUtil;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Sum on 15/12/17.
 */
@ContentView(R.layout.activity_user_info)
public class UserInfoActivity extends BaseAppActivity implements CityPopupWindow.OnWheelItemSelected {

    public static final int INTENT_TAKE_PIC_CAMEAR = 0;
    public static final int INTENT_TAKE_PIC_DICM = 1;
    public static final int INTENT_TAKE_PIC_SCALE = 2;

    private static final int permission_camera = 10;

    @ViewInject(R.id.title_name)
    private TextView mTitleName;

    @ViewInject(R.id.iv_my_pic)
    private RoundedImageView mImage;

    @ViewInject(R.id.tv_user_sex)
    private TextView tv_user_sex;

    @ViewInject(R.id.tv_user_names)
    private TextView tv_user_names;

    @ViewInject(R.id.tv_user_area)
    private TextView tv_user_area;

    @ViewInject(R.id.tv_user_birthday)
    private TextView tv_user_birthday;

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        backDo();
    }

    @Event(value = R.id.ripple_user_head, type = RippleView.OnRippleCompleteListener.class)
    private void onSetUserFaceClick(View view) {
        ShowTakePicDialog();
    }

    @Event(value = R.id.ripple_user_address, type = RippleView.OnRippleCompleteListener.class)
    private void onUserAddressClick(View view) {
        pushFragmentToBackStack(AddressMyFragment.class, 10);
    }

    @Event(value = R.id.ripple_user_pwd, type = RippleView.OnRippleCompleteListener.class)
    private void onUserPwdClick(View view) {
        pushFragmentToBackStack(AccountSafeFragment.class, RegisterFragment.Type_forget_by_login);
    }

    @Event(R.id.ripple_user_name)
    private void onChangeNameClick(View view) {
       /* AlertDialog.Builder dialog = DialogMaker.getDialog(this);

        View rootView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_name, null);

        final TextInputLayout edNameLayout = (TextInputLayout) rootView.findViewById(R.id.text_input_name);

//        final TextInputLayout edQQLayout = (TextInputLayout) rootView.findViewById(R.id.text_input_qq);

        dialog.setView(rootView);

        dialog.setNegativeButton("取消", null);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = edNameLayout.getEditText().getText().toString();
                if (!TextUtils.isEmpty(name) && name.length() > 2) {
                    tv_user_names.setText(name);
                    mUserInfo.userName = name;
                }
            }
        });

        dialog.create().show();*/

    }

    @Event(R.id.ripple_user_sex)
    private void onChangeSexClick(View view) {

        AlertDialog.Builder dialog = DialogMaker.getDialog(this);
        dialog.setTitle("性别");
        String sex = mUserInfo.sex;
        int index = 0;
        if (!TextUtils.isEmpty(sex)) {
            index = Integer.valueOf(sex) - 1;
        }

        dialog.setSingleChoiceItems(R.array.sex, index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sex = "";
                switch (which) {
                    case 0:
                        sex = "男";
                        mUserInfo.sex = "1";
                        break;
                    case 1:
                        sex = "女";
                        mUserInfo.sex = "2";
                        break;
                }
                tv_user_sex.setText(sex);
                mHasChange = true;
            }
        });
        dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    @Event(R.id.ripple_user_birthday)
    private void onChangeBirthdayClick(View view) {

        Calendar instance = Calendar.getInstance();

        final DatePickerDialog mDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                StringBuilder builder = new StringBuilder();
                builder.append(year).append("-");
                builder.append(monthOfYear + 1).append("-");
                builder.append(dayOfMonth);

                tv_user_birthday.setText(builder.toString());
                mHasChange = true;
                //用户出生日期
                mUserInfo.birthday = builder.toString();

            }
        }, instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH));

        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);

        mDialog.show();
    }

    @Event(R.id.ripple_user_area)
    private void onAreaClick(View view) {
        CityPopupWindow mPopup = new CityPopupWindow(this, this);
        mPopup.show();
    }

    private UserInfo mUserInfo;
    private boolean mHasChange = false;

    @Override
    protected boolean needRoot() {
        return false;
    }

    @Override
    protected void initParams() {

        mUserInfo = AppApplication.appContext.getLoginUserInfo();

        mTitleName.setText(R.string.my_account);
        showUserInfo();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fl_content;
    }

    public void showUserInfo() {
        String image = AppApplication.appContext.getUserFaceImage();
        if (!TextUtils.isEmpty(image)) {
            mImage.LoadDrawable(image);
        }
        String sex = mUserInfo.sex;
        if (TextUtils.isEmpty(sex)) {
            mUserInfo.sex = "1";
        }
        tv_user_sex.setText(mUserInfo.sex.equals("1") ? "男" : "女");
        if (!TextUtils.isEmpty(mUserInfo.birthday)) {
            //坑爹的服务的数据
            try {
                String dataStr = TimeUtil.getDataStr(Long.valueOf(mUserInfo.birthday));
                tv_user_birthday.setText(dataStr);
                mUserInfo.birthday = dataStr;
            } catch (NumberFormatException e) {
                tv_user_birthday.setText(mUserInfo.birthday);
            }
        }

        if (!TextUtils.isEmpty(mUserInfo.area))
            tv_user_area.setText(mUserInfo.areaAddress);
        if (!TextUtils.isEmpty(mUserInfo.userName))
            tv_user_names.setText(mUserInfo.userName);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == permission_camera) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                startCamera();
            } else {
                // Permission Denied
                ToastUtil.showToastLong("请检测您是否已经允许打开相机了哦~");
            }
        }
    }

    //动态申请照相机权限
    private boolean Target23ReqCamera() {
        boolean hasPermission = true;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //申请照相机权限
            hasPermission = false;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, permission_camera);
        }
        return hasPermission;
    }

    private void ShowTakePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        CharSequence[] item = {"相册", "拍照"};
        builder.setItems(item, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        showDICM(dialog);
                        break;
                    case 1:
                        showCamera(dialog);
                        break;
                }
            }
        });
        builder.create().show();
    }

    private Uri mUri;

    private void showCamera(DialogInterface dialog) {
        dialog.dismiss();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Target23ReqCamera()) {
                startCamera();
            }
        } else {
            startCamera();
        }
    }

    private void startCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getUserHeadFile()));
        startActivityForResult(intent, INTENT_TAKE_PIC_CAMEAR);
    }

    private void showDICM(DialogInterface dialog) {
        dialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, INTENT_TAKE_PIC_DICM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case INTENT_TAKE_PIC_CAMEAR:
                mUri = Uri.fromFile(getUserHeadFile());
                startPhotoZoom(mUri);
                //setPicToView(mUri);
                break;

            case INTENT_TAKE_PIC_DICM:
                if (data != null) {
                    mUri = data.getData();
                    startPhotoZoom(mUri);
                    //setPicToView(mUri);
                }
                break;
            case INTENT_TAKE_PIC_SCALE:
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, INTENT_TAKE_PIC_SCALE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            mImage.setImageBitmap(photo);
            savePic(photo);
        } else {
            mImage.setImageResource(R.drawable.icon_user_no_head);
        }
    }

    private File getUserHeadFile() {
        File file = AppFileConfig.getCacheFile();
        return new File(file, "user.jpg");
    }


    private void savePic(Bitmap face) {
        File file = getUserHeadFile();
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            OutputStream outStream = new FileOutputStream(file);
            face.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
            //上传头像
            updateFace();

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateFace() {

        UploadHelper.uploadFile(getUserHeadFile().getPath(), new UploadHelper.uploadFileListener() {
            @Override
            public void onSuccess(String ftpFile) {

                mUserInfo.vipPic = ftpFile;
                mHasChange = true;
            }

            @Override
            public void onFailed(String msg) {
                SnackMaker.shortShow(mTitleName, msg);
            }
        });

    }

    private void backDo() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (mHasChange && count == 0) {
            String title = getString(R.string.dialog_title);

            DialogMaker.showAlterDialog(this, title, "您的个人信息有变动,是否保存?", "放弃", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }, "保存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    updateUserInfo();
                }
            });
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        backDo();
    }

    private void updateUserInfo() {

        showLoadingDialog();

        ApiControl.getApi().updateUserInfo(mUserInfo, new Callback.CommonCallback<ResponseModel>() {
            @Override
            public void onSuccess(ResponseModel result) {
                if (result.isSuccess()) {
                    AppApplication.appContext.saveLoginInfo(mUserInfo);

                    EventBusHelper.postReloadUserInfo();

                    finish();
                } else {
                    SnackMaker.shortShow(mTitleName, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(mTitleName, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideLoadingDialog();
            }
        });

    }


    @Override
    public void onWheelSelect(HashMap<String, String> data) {
        StringBuilder builder = new StringBuilder();
        builder.append(data.get("province")).append(",").append(data.get("city")).append(",").append(data.get("area"));
        String location = builder.toString();
        String code = data.get("code");
        mUserInfo.area = location;
        Logger.e("area:" + location + " code:" + code);
        //区域数据
        tv_user_area.setText(location);
        mUserInfo.areaAddress = location;
        mHasChange = true;
    }

    //切换动画


    @Override
    protected int fragmentOpenEnterAnim() {
        return R.anim.right_in_enter;
    }

    @Override
    protected int fragmentOpenExitAnim() {
        return R.anim.right_in_exist;
    }

    @Override
    protected int fragmentCloseExitAnim() {
        return R.anim.right_out_exist;
    }

    @Override
    protected int fragmentCloseEnterAnim() {
        return R.anim.right_out_enter;
    }
}
