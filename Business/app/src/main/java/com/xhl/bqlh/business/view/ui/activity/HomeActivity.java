package com.xhl.bqlh.business.view.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.AppConfig.GlobalParams;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.UserInfo;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.doman.CheckServiceHelper;
import com.xhl.bqlh.business.utils.ToastUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.custom.LifeCycleImageView;
import com.xhl.bqlh.business.view.event.ClickShopEvent;
import com.xhl.bqlh.business.view.event.CommonEvent;
import com.xhl.bqlh.business.view.event.OrderDetailsEvent;
import com.xhl.bqlh.business.view.event.OrderStatisticsEvent;
import com.xhl.bqlh.business.view.helper.DialogMaker;
import com.xhl.bqlh.business.view.helper.EventHelper;
import com.xhl.bqlh.business.view.helper.PhotoHelper;
import com.xhl.bqlh.business.view.helper.UploadHelper;
import com.xhl.bqlh.business.view.ui.fragment.HomeCustomerFragment;
import com.xhl.bqlh.business.view.ui.fragment.HomeMainFragment;
import com.xhl.bqlh.business.view.ui.fragment.HomeOderQueryFragment;
import com.xhl.bqlh.business.view.ui.fragment.HomeOrderReturnFragment;
import com.xhl.bqlh.business.view.ui.fragment.HomeSignInFragment;
import com.xhl.bqlh.business.view.ui.fragment.HomeStatisticsOrderFragment;
import com.xhl.bqlh.business.view.ui.fragment.HomeStatisticsShopFragment;
import com.xhl.bqlh.business.view.ui.fragment.HomeStoreApplyFragment;
import com.xhl.xhl_library.ui.FragmentCacheManager;
import com.xhl.xhl_library.ui.view.RoundedImageView;
import com.xhl.xhl_library.utils.NumberUtil;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.common.Callback;
import org.xutils.x;

/**
 * Created by Sum on 16/5/15.
 */
public class HomeActivity extends BaseAppActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int NAVDRAWER_LAUNCH_DELAY = 220;

    @Override
    public boolean isNeedInject() {
        return false;
    }

    @Override
    protected boolean isNeedCompatStatusBar() {
        return false;
    }

    @Override
    protected boolean needRoot() {
        return false;
    }

    private Handler mHandler;

    private DrawerLayout mDrawer;//侧滑容器
    private NavigationView mNavigation;//菜单内容
    //user info
    protected RoundedImageView mUserAvatar;
    protected View mUserExit_t;
    protected TextView mUserName;

    //首页Fragment缓存
    private FragmentCacheManager mFragmentCacheManager;

    private WebView mWebVew;


    @Override
    protected void initParams() {
        mHandler = new Handler();

        //统计订单
        mWebVew = new WebView(this);
        mWebVew.getSettings().setJavaScriptEnabled(true);
//        mWebVew.loadUrl("file:///android_asset/count.html");
        mWebVew.loadUrl("http://www.xhlbqlh.com/resources/appCount.html");


        mDrawer = _findViewById(R.id.main_drawer_layout);
        mNavigation = _findViewById(R.id.main_nav_view);
        mNavigation.setNavigationItemSelectedListener(this);
        mNavigation.setItemBackgroundResource(R.drawable.side_nav_item_bg);
        mNavigation.setItemIconTintList(getIconList());
        mUserExit_t = mNavigation.findViewById(R.id.user_exit_t);
        mUserExit_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userQuitDialog();
            }
        });

        //user info
        View headerView = mNavigation.getHeaderView(0);
        mUserAvatar = (RoundedImageView) headerView.findViewById(R.id.user_imageView);
        mUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeCamera();
            }
        });
        mUserName = (TextView) headerView.findViewById(R.id.user_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initParams();
        initFragment();
        checkVersion();
        refreshUserInfo();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    //首页使用Fragment管理
    private void initFragment() {
        mFragmentCacheManager = new FragmentCacheManager();
        mFragmentCacheManager.setUp(this, R.id.fl_content);

        mFragmentCacheManager.addFragmentToCache(0, HomeMainFragment.class);
        mFragmentCacheManager.addFragmentToCache(1, HomeSignInFragment.class);
        mFragmentCacheManager.addFragmentToCache(2, HomeCustomerFragment.class);
        mFragmentCacheManager.addFragmentToCache(3, HomeStoreApplyFragment.class);
        mFragmentCacheManager.addFragmentToCache(4, HomeOderQueryFragment.class);
        mFragmentCacheManager.addFragmentToCache(5, HomeStatisticsOrderFragment.class);
        mFragmentCacheManager.addFragmentToCache(6, HomeStatisticsShopFragment.class);
        mFragmentCacheManager.addFragmentToCache(7, HomeOrderReturnFragment.class);

        mFragmentCacheManager.setCurrentFragment(0);

        mNavigation.setCheckedItem(R.id.user_menu_home);
    }

    private void checkVersion() {
        new CheckServiceHelper(this).start();

       /* List<TaskRecord> allRecordTask = DbTaskHelper.getInstance().getAllRecordTask();
        if (allRecordTask == null) {
            Logger.v("任务记录为空");
        } else {
            StringBuffer record = new StringBuffer();
            for (TaskRecord taskRecord : allRecordTask) {
                record.append("\n").append(taskRecord.toString());
            }
            Logger.v("******任务记录******");
            Logger.v(record.toString());
            Logger.v("*******************");
        }

        Logger.v("******任务店铺******");
        List<TaskShop> allTaskShop = DbTaskHelper.getInstance().getAllTaskShop();
        for (TaskShop task : allTaskShop) {
            Logger.v(task.toString());
        }*/
    }


    private void takeCamera() {
        AlertDialog.Builder builder = DialogMaker.getDialog(this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
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
        ApiControl.getApi().userUpdateImage(file, new Callback.CommonCallback<ResponseModel<Object>>() {
            @Override
            public void onSuccess(ResponseModel<Object> result) {
                if (result.isSuccess()) {
                    UserInfo userInfo = getAppApplication().getUserInfo();
                    userInfo.headImage = file;
                    getAppApplication().saveLoginInfo(userInfo);
                    EventHelper.postCommonEvent(CommonEvent.EVENT_REFRESH_INFO);
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

    //相册
    private void showDICM() {
        mPhotoHelper.showDICM();
    }

    //相机
    private void startCamera() {
        mPhotoHelper.capture();
    }


    @Override
    public void onBackPressed() {
        if (mDrawer != null && mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            int currentIndex = mFragmentCacheManager.getCurrentIndex();
            if (currentIndex != 0) {
                mNavigation.setCheckedItem(R.id.user_menu_home);
                mFragmentCacheManager.setCurrentFragment(0);
            } else {
                boolean fastDoubleClick = NumberUtil.isFastDoubleClick();
                if (fastDoubleClick) {
                    finish();
                } else {
                    ToastUtil.showToastShort(R.string.exit_app);
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.user_menu_home:
                onNavItemSelected(0);
                break;
            case R.id.user_menu_attendance:
                onNavItemSelected(1);
                break;
            case R.id.user_menu_new_customer:
                onNavItemSelected(2);
                break;
            case R.id.user_menu_store_manager:
                onNavItemSelected(3);
                break;
            case R.id.user_menu_order_manager:
                onNavItemSelected(4);
                break;
            case R.id.user_menu_order_statistics:
                onNavItemSelected(5);
                break;
            case R.id.user_menu_order_account:
                onNavItemSelected(6);
                break;
            case R.id.user_menu_order_return:
                onNavItemSelected(7);
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //选择菜单Item
    private void onNavItemSelected(final int index) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentCacheManager.setCurrentFragment(index);
            }
        }, NAVDRAWER_LAUNCH_DELAY);
    }

    //刷新用户数据信息
    private void refreshUserInfo() {
        if (mUserName == null)
            return;
        mUserName.setText(getAppApplication().getUserName());
        String userFaceImage = getAppApplication().getUserFaceImage();
        x.image().loadDrawable(userFaceImage, LifeCycleImageView.imageOptions, new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable result) {
                mUserAvatar.setImageDrawable(result);
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

    //店铺位置详情
    public void onEventBackgroundThread(ClickShopEvent shopEvent) {
        Intent intent = new Intent(this, ShopDetailsActivity.class);
        //店铺类型
        intent.putExtra(GlobalParams.INTENT_VISIT_SHOP_TYPE, shopEvent.shopType);
        //拜访状态
        intent.putExtra(GlobalParams.Intent_visit_client_state, shopEvent.shopTaskState);
        //拜访类型
        intent.putExtra(GlobalParams.Intent_visit_client_type, shopEvent.shopTaskType);
        //拜访时间差
        intent.putExtra(GlobalParams.Intent_visit_client_time, shopEvent.dayInterval);
        //店铺数据
        intent.putExtra(GlobalParams.Intent_shop_id, shopEvent.shopId);
        intent.putExtra(GlobalParams.Intent_shop_name, shopEvent.shopName);
        intent.putExtra(GlobalParams.Intent_shop_latitude, shopEvent.latitude);
        intent.putExtra(GlobalParams.Intent_shop_longitude, shopEvent.longitude);
        intent.putExtra(GlobalParams.Intent_shop_city, shopEvent.city);
        intent.putExtra(GlobalParams.Intent_shop_street, shopEvent.address);

        //经纬度不为空精确搜索
        if (shopEvent.latitude != 0 && shopEvent.longitude != 0) {
            intent.putExtra(ShopDetailsActivity.Location_type, ShopDetailsActivity.Location_type_1);
        }
        //城市不为空按钮城市搜索
        else if (!TextUtils.isEmpty(shopEvent.city)) {
            intent.putExtra(ShopDetailsActivity.Location_type, ShopDetailsActivity.Location_type_2);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.anim_right_open_enter, R.anim.anim_right_open_exit);
    }

    //订单详情
    public void onEvent(OrderDetailsEvent orderEvent) {
        if (TextUtils.isEmpty(orderEvent.storeOrderCode)) {
            Logger.e("订单号为空");
            return;
        }
        Intent intent = new Intent(this, OrderDetailsActivity.class);
        intent.putExtra("storeOrderCode", orderEvent.storeOrderCode);
        intent.putExtra("name", orderEvent.name);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_right_open_enter, R.anim.anim_right_open_exit);
    }

    public void onEvent(CommonEvent event) {
        if (event.eventType == CommonEvent.EVENT_OPEN_DRAWER) {
            if (mDrawer != null && !mDrawer.isDrawerOpen(GravityCompat.START)) {
                mDrawer.openDrawer(GravityCompat.START);
            }
        } else if (event.eventType == CommonEvent.EVENT_REFRESH_INFO) {
            refreshUserInfo();
        }
    }

    //百度订单统计
    public void onEvent(OrderStatisticsEvent event) {
        if (!TextUtils.isEmpty(event.orderInfo)) {
            mWebVew.loadUrl("javascript:pushToBaidu('" + event.orderInfo + "')");
        }
    }

}
