package com.xhl.bqlh.business.view.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.AppConfig.GlobalParams;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.ShopDisplayModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.utils.ToastUtil;
import com.xhl.bqlh.business.view.base.BaseAppFragment;
import com.xhl.bqlh.business.view.event.AutoLocationEvent;
import com.xhl.bqlh.business.view.helper.PhotoHelper;
import com.xhl.bqlh.business.view.helper.UploadHelper;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.xhl_library.utils.BitmapUtil;
import com.xhl.xhl_library.utils.TimeUtil;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/6/4.
 * 拍照
 */
@ContentView(R.layout.fragment_shop_display_add)
public class ShopDisplayAddFragment extends BaseAppFragment {

    @ViewInject(R.id.ll_image_content)
    private LinearLayout mLImageContent;

    @ViewInject(R.id.ed_input_content)
    private EditText ed_input_content;

    @ViewInject(R.id.iv_add)
    private View iv_add;

    @ViewInject(R.id.tv_location)
    private TextView tv_location;

    @Event(R.id.iv_add)
    private void onAddClick(View view) {
        mPhotoHelper.capture();
    }

    @Override
    protected boolean isNeedRegisterEventBus() {
        return true;
    }

    @Event(R.id.btn_confirm)
    private void onConfirmClick(View view) {

        if (mFiles.size() > 0) {
            showProgressLoading("上传图片中...");
            UploadHelper.uploadFile(mFiles, new UploadHelper.uploadFileListener() {
                @Override
                public void onSuccess(final String ftpFile) {
                    hideLoadingDialog();
                    mToolbar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            uploadInfo(ftpFile);
                        }
                    }, 200);
                }

                @Override
                public void onFailed(String msg) {
                    hideLoadingDialog();
                    ToastUtil.showToastShort(msg);
                }
            });
        } else {
            SnackUtil.shortShow(view, "未添加陈列照");
        }
    }

    private void uploadInfo(String netImage) {
        displayModel.setImg(netImage);
        String mark = ed_input_content.getEditableText().toString().trim();
        displayModel.setRemark(mark);
        displayModel.setTime(TimeUtil.currentTime());
        displayModel.shopId = mShopId;

        showProgressLoading("提交信息中...");
        ApiControl.getApi().shopDisplayAdd(displayModel, new Callback.CommonCallback<ResponseModel<String>>() {
            @Override
            public void onSuccess(ResponseModel<String> result) {
                if (result.isSuccess()) {
                    SnackUtil.shortShow(mToolbar, "提交成功");
                    mToolbar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLImageContent.removeAllViews();
                            getSumContext().popTopFragment(displayModel);
                        }
                    }, 500);
                } else {
                    SnackUtil.shortShow(mToolbar, result.getMessage());
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
                hideLoadingDialog();
            }
        });
    }


    private List<String> mFiles;

    private ShopDisplayModel displayModel;

    private String mShopId;

    private LatLng mCurLocation;

    @Override
    public void onEnter(Object data) {
        if (data != null) {
            mShopId = (String) data;
        }
    }

    @Override
    protected void initParams() {
        super.initToolbar();
        mToolbar.setTitle(R.string.menu_shop_camera);
        mFiles = new ArrayList<>();
        displayModel = new ShopDisplayModel();
        mCurLocation = GlobalParams.mSelfLatLng;
        //定位当前位置
        getAppApplication().mLocation.resolveLocation();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        //拍照请求返回
        if (requestCode == PhotoHelper.CAPTURE_PHOTO_DEFAULT_CODE) {
            String path = mPhotoHelper.getPhoto().getPath();

            Bitmap bitmap = BitmapUtil.readBitmapForFixMaxSize(path);

            final ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageBitmap(bitmap);
            int width = getResources().getDimensionPixelSize(R.dimen.pub_dimen_56dp);
            int margin = getResources().getDimensionPixelSize(R.dimen.pub_dimen_8dp);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
            if (mFiles.size() != 0) {
                params.leftMargin = margin;
            }
            imageView.setTag(path);
            imageView.setLayoutParams(params);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click(v);
                }
            });
            //添加显示
            mLImageContent.addView(imageView);
            //添加到集合中
            mFiles.add(path);
            if (mFiles.size() == 5) {
                ViewHelper.setViewGone(iv_add, true);
            } else {
                ViewHelper.setViewGone(iv_add, false);
            }
        }
    }

    private void click(View view) {
        String file = (String) view.getTag();
        getSumContext().pushFragmentToBackStack(ImageDetailsFragment.class, file);
    }

    public void onEvent(AutoLocationEvent event) {
        tv_location.setText(event.nearBy);
        //精度
        displayModel.setCoordinateX(event.latLng.longitude);
        //维度
        displayModel.setCoordinateY(event.latLng.latitude);

        displayModel.setAddress(event.nearBy);
    }
}
