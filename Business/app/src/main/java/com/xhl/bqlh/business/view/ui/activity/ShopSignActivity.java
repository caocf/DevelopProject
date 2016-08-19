package com.xhl.bqlh.business.view.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.xhl.bqlh.business.AppConfig.GlobalParams;
import com.xhl.bqlh.business.Db.DbTaskHelper;
import com.xhl.bqlh.business.Model.App.ShopSignModel;
import com.xhl.bqlh.business.Model.Type.TaskType;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.doman.ShopSignHelper;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.helper.DialogMaker;
import com.xhl.bqlh.business.view.helper.EventHelper;
import com.xhl.bqlh.business.view.helper.PermissionHelper;
import com.xhl.xhl_library.AppFileConfig;
import com.xhl.xhl_library.utils.BitmapUtil;
import com.xhl.xhl_library.utils.TimeUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;

/**
 * Created by Sum on 16/4/20.
 */
@ContentView(R.layout.activity_shop_sign)
public class ShopSignActivity extends BaseAppActivity {

    public static final String INTENT_Sign_LatLng = "sign_LatLng";
    public static final String INTENT_Sign_Distance = "sign_distance";
    public static final String INTENT_Sign_Address = "sign_address";
    public static final String INTENT_Sign_Task_Type = "sign_task_type";
    public static final String INTENT_Sign_Task_Enter = "sign_task_enter";

    private static final int Req_camera_mt = 1;
    private static final int Req_camera_dm = 2;
    private static final int Req_camera_cl = 3;

    @ViewInject(R.id.ed_input_content)
    private EditText ed_input_content;

    @ViewInject(R.id.iv_pic_1)
    private ImageView iv_pic_1;

    @ViewInject(R.id.iv_pic_2)
    private ImageView iv_pic_2;

    @ViewInject(R.id.iv_pic_3)
    private ImageView iv_pic_3;

    @ViewInject(R.id.tv_location)
    private TextView tv_location;

    @Event(R.id.ll_add_ic_1)
    private void onAdd1Click(View view) {
        checkCamera(Req_camera_mt);
    }

    @Event(R.id.ll_add_ic_2)
    private void onAdd2Click(View view) {
        checkCamera(Req_camera_dm);
    }

    @Event(R.id.ll_add_ic_3)
    private void onAdd3Click(View view) {
        checkCamera(Req_camera_cl);
    }

    @Event(R.id.btn_confirm)
    private void onConfirmClick(View view) {
        mHelper.sign();
    }

    private ShopSignHelper mHelper;
    private ShopSignModel mSign;
    private String mPath1, mPath2, mPath3;

    @Override
    public Object getValue(int type) {
        if (type == ShopSignHelper.TYPE_GET_OTHER) {
            return mSign;
        } else if (type == ShopSignHelper.TYPE_GET_REMARKE) {
            return ed_input_content.getText().toString().trim();
        } else if (type == ShopSignHelper.TYPE_GET_IMAG1) {
            return mPath1;
        } else if (type == ShopSignHelper.TYPE_GET_IMAG2) {
            return mPath2;
        } else if (type == ShopSignHelper.TYPE_GET_IMAG3) {
            return mPath3;
        }
        return null;
    }

    @Override
    public void showValue(final int type, Object obj) {
        super.showValue(type, obj);
        if (type == ShopSignHelper.TYPE_RES_FINISH) {
            //保存状态
            if (mSign.checkType == TaskType.TYPE_Normal) {
                DbTaskHelper.getInstance().saveNormalTask(mSign.retailerId);
            } else if (mSign.checkType == TaskType.TYPE_Temporary) {
                DbTaskHelper.getInstance().saveTemporaryTask(mSign.retailerId);
            }

            SnackUtil.longShow(mToolbar, "签到成功");
            mToolbar.postDelayed(new Runnable() {
                @Override
                public void run() {

                    EventHelper.postFinishTask(mSign.retailerId);
                    Intent data = new Intent();
                    data.putExtra("data", true);
                    setResult(RESULT_OK, data);
                    ShopSignActivity.this.finish();
                }
            }, 1000);
        }
    }

    @Override
    protected void initParams() {
        super.initToolbar();
        setTitle("门店签到");
        mHelper = new ShopSignHelper(this);
        mSign = new ShopSignModel();
        Intent intent = getIntent();
        //类型
        mSign.checkType = intent.getIntExtra(INTENT_Sign_Task_Type, 0);
        //经纬度
        LatLng latLng = intent.getParcelableExtra(INTENT_Sign_LatLng);
        if (latLng != null) {
            mSign.coordinateX = latLng.longitude;
            mSign.coordinateY = latLng.latitude;
        }
        mSign.distance = intent.getStringExtra(INTENT_Sign_Distance);
        mSign.chinkInLocation = intent.getStringExtra(INTENT_Sign_Address);
        mSign.retailerId = intent.getStringExtra(GlobalParams.Intent_shop_id);
        mSign.enterPoint = intent.getBooleanExtra(INTENT_Sign_Task_Enter, true);
        tv_location.setText(intent.getStringExtra(INTENT_Sign_Address));
    }

    private void checkCamera(int reqType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionHelper.hasPermissionLocation(this)) {
                PermissionHelper.reqPermissionLocation(this, reqType);
            } else {
                startCamera(reqType);
            }
        } else {
            startCamera(reqType);
        }
    }

    private void startCamera(int reqType) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getShopFile(reqType)));
        startActivityForResult(intent, reqType);
    }

    //文件名:sign+time+type
    private String getFileName(int reqType) {
        String today = TimeUtil.currentTimeDay();
        return (String) TextUtils.concat("sign", "_", today, "_", String.valueOf(reqType), ".jpg");
    }

    private File getShopFile(int reqType) {
        File file = AppFileConfig.getCacheFile();
        return new File(file, getFileName(reqType));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission Granted
            startCamera(requestCode);
        } else {
            // Permission Denied
            DialogMaker.refuseShowSetCamera(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        String path = getShopFile(requestCode).getPath();
//        Logger.v(requestCode + " path:" + path);
        Bitmap bitmap = BitmapUtil.readBitmapForFixMaxSize(getShopFile(requestCode).getPath());
        switch (requestCode) {
            case Req_camera_mt:
                iv_pic_1.setImageBitmap(bitmap);
                mPath1 = path;
                break;
            case Req_camera_dm:
                iv_pic_2.setImageBitmap(bitmap);
                mPath2 = path;
                break;
            case Req_camera_cl:
                iv_pic_3.setImageBitmap(bitmap);
                mPath3 = path;
                break;
        }
    }
}
