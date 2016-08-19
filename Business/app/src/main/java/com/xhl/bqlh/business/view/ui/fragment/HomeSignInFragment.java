package com.xhl.bqlh.business.view.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.AppConfig.GlobalParams;
import com.xhl.bqlh.business.Db.PreferenceData;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.SignConfigModel;
import com.xhl.bqlh.business.Model.SignRecordModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppFragment;
import com.xhl.bqlh.business.view.event.AutoLocationEvent;
import com.xhl.bqlh.business.view.helper.DialogMaker;
import com.xhl.bqlh.business.view.ui.activity.ShopDetailsActivity;
import com.xhl.bqlh.business.view.ui.activity.SignInRecordActivity;
import com.xhl.xhl_library.utils.TimeUtil;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Date;

/**
 * Created by Sum on 16/5/15.
 */
@ContentView(R.layout.fragment_home_sign_in)
public class HomeSignInFragment extends BaseAppFragment {

    private static final int TYPE_START = 1;
    private static final int TYPE_END = 2;

    @ViewInject(R.id.tv_text_null)
    private View view;

    @Event(R.id.btn_qd_start)
    private void onSignStartClick(View view) {
        sign(TYPE_START);
    }

    @Event(R.id.btn_qd_end)
    private void onSignEndClick(View view) {
        sign(TYPE_END);
    }

    @Event(R.id.btn_record)
    private void onRecordClick(View view) {
        Intent intent = new Intent(getContext(), SignInRecordActivity.class);
        startActivity(intent);
    }

    @Event(R.id.tv_location)
    private void onLocationClick(View view) {

        Intent intent = new Intent(getContext(), ShopDetailsActivity.class);
        intent.putExtra(GlobalParams.INTENT_VISIT_SHOP_TYPE, GlobalParams.VISIT_SHOP_SIGN);
        if (mSignConfig != null) {
            intent.putExtra("distance", mSignConfig.getSignDistance());
            intent.putExtra(GlobalParams.Intent_shop_latitude, mSignConfig.coordinateY);
            intent.putExtra(GlobalParams.Intent_shop_longitude, mSignConfig.coordinateX);
            intent.putExtra(ShopDetailsActivity.Location_type, ShopDetailsActivity.Location_type_1);
        }
        startActivity(intent);
    }

    @ViewInject(R.id.tv_location)
    private TextView tv_location;

    @ViewInject(R.id.tv_sign_start_time)
    private TextView tv_sign_start_time;

    @ViewInject(R.id.tv_sign_end_time)
    private TextView tv_sign_end_time;

    @ViewInject(R.id.tv_sign_location)
    private TextView tv_sign_location;

    @ViewInject(R.id.tv_sign_distance)
    private TextView tv_sign_distance;

    @ViewInject(R.id.btn_qd_start)
    private Button btn_qd_start;

    @ViewInject(R.id.btn_qd_end)
    private Button btn_qd_end;

    private SignConfigModel mSignConfig;

    private boolean isLocating = true;

    private boolean isSigning = false;

    //是否在指定范围签到
    private boolean mIsSignInDistance = false;

    private int mSignType = 0;

    @Override
    protected boolean isNeedRegisterEventBus() {
        return true;
    }

    @Override
    protected void initParams() {
        super.initHomeToolbar();
        mToolbar.setTitle(R.string.user_nav_main_attendance);
        //定位当前位置
        getAppApplication().mLocation.resolveLocation();

        SignConfigModel signConfigModel = PreferenceData.getInstance().signConfig();
        mSignConfig = signConfigModel;
        if (signConfigModel != null) {
            tv_sign_start_time.setText(getString(R.string.attendance_start_time, signConfigModel.workTime));
            tv_sign_end_time.setText(getString(R.string.attendance_end_time, signConfigModel.dutyTime));
            tv_sign_location.setText(getString(R.string.attendance_location, signConfigModel.signPlace));
            tv_sign_distance.setText(getString(R.string.attendance_distance, signConfigModel.getSignDistance()));
        } else {
            view.setVisibility(View.VISIBLE);
        }
        updateShow();
    }

    private void updateShow() {
        if (PreferenceData.getInstance().todaySignStart()) {
            btn_qd_start.setEnabled(false);
            btn_qd_start.setText("已签到");
        }

        if (PreferenceData.getInstance().todaySignEnd()) {
            btn_qd_end.setEnabled(false);
            btn_qd_end.setText("已签退");
        }
    }

    private void sign(int type) {
        if (isLocating) {
            SnackUtil.shortShow(mToolbar, R.string.locating);
            return;
        }
        mSignType = type;
        if (mSignConfig != null) {
            boolean containsPoint = SpatialRelationUtil.isCircleContainsPoint(mSignConfig.getSignLocation(), mSignConfig.getSignDistance(), GlobalParams.mSelfLatLng);
            mIsSignInDistance = containsPoint;
            if (mSignConfig.isForceSign()) {
                if (containsPoint) {
                    checkTime();
                } else {
                    SnackUtil.shortShow(mToolbar, getString(R.string.attendance_sign_limit, mSignConfig.getSignDistance()));
                }
            } else {
                checkTime();
            }
        } else {
            SnackUtil.longShow(mToolbar, "数据异常,请重新登陆");
        }
    }

    private void checkTime() {

        if (mSignType == TYPE_END) {
            String time = TimeUtil.currentTime("HH:mm:ss");
            String dutyTime = mSignConfig.dutyTime;
            if (time.compareTo(dutyTime) < 0) {
                AlertDialog.Builder dialog = DialogMaker.getDialog(getContext());
                dialog.setTitle(R.string.attendance_end);
                dialog.setMessage("未到下班时间,您确定现在打卡吗?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("打卡", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmSign();
                    }
                });
                dialog.setNegativeButton(R.string.dialog_cancel, null);
                dialog.show();
            } else {
                confirmSign();
            }
        } else {
            confirmSign();
        }

    }

    private void confirmSign() {
        if (isSigning) {
            return;
        }
        isSigning = true;

        final SignRecordModel record = new SignRecordModel();
        record.setType(mSignType);
        record.setWorkPlace(GlobalParams.mNearBy);
        record.setDutyPlace(GlobalParams.mNearBy);
        record.setWorkOnTime(mSignConfig.workTime);
        record.setDutyUpTime(mSignConfig.dutyTime);

        double latitude = GlobalParams.mSelfLatLng.latitude;
        double longitude = GlobalParams.mSelfLatLng.longitude;
        //经度，维度
        record.setDutyCoordinateX(String.valueOf(longitude));
        record.setDutyCoordinateY(String.valueOf(latitude));

        record.setWorkCoordinateX(String.valueOf(longitude));
        record.setWorkCoordinateY(String.valueOf(latitude));
        record.enterPoint = mIsSignInDistance;

        ApiControl.getApi().sign(record, new Callback.CommonCallback<ResponseModel<String>>() {
            @Override
            public void onSuccess(ResponseModel<String> result) {
                if (result.isSuccess()) {
                    if (mSignType == 1) {
                        PreferenceData.getInstance().todaySignStart(true);
                    } else {
                        PreferenceData.getInstance().todaySignEnd(true);
                    }
                    PreferenceData.getInstance().signLastTime(new Date());
                    SnackUtil.shortShow(mToolbar, result.getObj());
                } else {
                    String message = result.getMessage();
                    SnackUtil.shortShow(mToolbar, message);
                    if (message.equals("今日已签到")) {
                        PreferenceData.getInstance().todaySignStart(true);
                    } else if (message.equals("今日已签退")) {
                        PreferenceData.getInstance().todaySignEnd(true);
                    }
                }
                updateShow();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackUtil.shortShow(mToolbar, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                isSigning = false;
            }
        });
    }

    public void onEvent(AutoLocationEvent event) {
        isLocating = false;
        //当前位置在什么附近
        tv_location.setText(event.nearBy);
    }
}
