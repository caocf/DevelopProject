package com.xhl.bqlh.view.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.webkit.URLUtil;

import com.xhl.bqlh.R;
import com.xhl.bqlh.doman.CheckServiceHelper;
import com.xhl.bqlh.model.event.AdEvent;
import com.xhl.bqlh.model.event.CommonEvent;
import com.xhl.bqlh.model.event.DetailsEvent;
import com.xhl.bqlh.view.base.BaseAppActivity;
import com.xhl.bqlh.view.custom.MultiCheckBox;
import com.xhl.bqlh.view.ui.activity.web.WebPageActivity;
import com.xhl.bqlh.view.ui.fragment.HomeAFragment;
import com.xhl.bqlh.view.ui.fragment.HomeCarFragment;
import com.xhl.bqlh.view.ui.fragment.HomeClassifyFragment;
import com.xhl.bqlh.view.ui.fragment.HomeMeFragment;
import com.xhl.xhl_library.ui.FragmentCacheManager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/6/30.
 */
@ContentView(R.layout.activity_home)
public class HomeActivity extends BaseAppActivity {

    @ViewInject(R.id.main_rb_home)
    private MultiCheckBox mCbHome;

    @ViewInject(R.id.main_rb_classify)
    private MultiCheckBox mCbClassify;

    @ViewInject(R.id.main_rb_shopping)
    private MultiCheckBox mCbShopping;

    @ViewInject(R.id.main_rb_my)
    private MultiCheckBox mCbMy;

    @Event(value = {R.id.main_rb_home, R.id.main_rb_classify, R.id.main_rb_shopping, R.id.main_rb_my})
    private void onCheckClick(View view) {
        switch (view.getId()) {
            case R.id.main_rb_home:
                checkItem(1);
                break;
            case R.id.main_rb_classify:
                checkItem(2);
                break;
            case R.id.main_rb_shopping:
                checkItem(3);
                break;
            case R.id.main_rb_my:
                checkItem(4);
                break;
        }
    }

    private FragmentCacheManager fragmentCacheManager;

    @Override
    public void onBackPressed() {
        fragmentCacheManager.onBackPress();
    }

    @Override
    protected void initParams() {
        super.initBadgeView(mCbShopping);

        fragmentCacheManager = new FragmentCacheManager();
        fragmentCacheManager.setUp(this, R.id.fl_content);
        fragmentCacheManager.addFragmentToCache(1, HomeAFragment.class);
        fragmentCacheManager.addFragmentToCache(2, HomeClassifyFragment.class);
        fragmentCacheManager.addFragmentToCache(3, HomeCarFragment.class);
        fragmentCacheManager.addFragmentToCache(4, HomeMeFragment.class);
        fragmentCacheManager.setListener(new FragmentCacheManager.onBootCallBackListener() {
            @Override
            public void onBootCallBack() {
                checkItem(1);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkItem(1);
            }
        }, 10);

        new CheckServiceHelper(this).start();
    }

    public void checkItem(int item) {

        checkState(item);

        fragmentCacheManager.setCurrentFragment(item);
    }

    private void checkState(int item) {

        if (item == 1) {
            mCbHome.setChecked(true);
            mCbClassify.setChecked(false);
            mCbShopping.setChecked(false);
            mCbMy.setChecked(false);
        } else if (item == 2) {
            mCbHome.setChecked(false);
            mCbClassify.setChecked(true);
            mCbShopping.setChecked(false);
            mCbMy.setChecked(false);
        } else if (item == 3) {
            mCbHome.setChecked(false);
            mCbClassify.setChecked(false);
            mCbShopping.setChecked(true);
            mCbMy.setChecked(false);
        } else if (item == 4) {
            mCbHome.setChecked(false);
            mCbClassify.setChecked(false);
            mCbShopping.setChecked(false);
            mCbMy.setChecked(true);
        }
    }

    public void onEvent(CommonEvent event) {
        super.onEvent(event);
        //切换到首页
        if (event.getEventTag() == CommonEvent.ET_MAIN) {
            checkItem(1);
        }
    }

    public void onEvent(DetailsEvent event) {
        int tag = event.getTag();
        Intent intent = null;
        if (tag == DetailsEvent.TAG_SHOP) {
            intent = new Intent(HomeActivity.this, ShopDetailsActivity.class);
        } else if (tag == DetailsEvent.TAG_PRODUCT) {
            intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
        }
        if (intent != null) {
            intent.putExtra("id", event.getInfoId());
            startActivity(intent);
        }
    }

    //广告处理
    public void onEventBackgroundThread(AdEvent event) {
        Intent intent;
        switch (event.adType) {
            case AdEvent.type_web:
                if (URLUtil.isNetworkUrl(event.data)) {
                    intent = new Intent(this, WebPageActivity.class);
                    intent.putExtra(WebPageActivity.TAG_URL, event.data);
                    intent.putExtra(WebPageActivity.TAG_TITLE, event.title);
                    startActivity(intent);
                }
                break;
            case AdEvent.type_scan:
                intent = new Intent(HomeActivity.this, SearchProductResActivity.class);
                intent.putExtra(SearchProductResActivity.SEARCH_PARAMS, event.data);
                intent.putExtra(SearchProductResActivity.SEARCH_TYPE, SearchProductResActivity.SEARCH_TYPE_SCAN);
                startActivity(intent);
                break;
            case AdEvent.type_product:
                intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                intent.putExtra("id", event.data);
                startActivity(intent);
                break;
            case AdEvent.type_shop:
                intent = new Intent(HomeActivity.this, ShopDetailsActivity.class);
                intent.putExtra("id", event.data);
                startActivity(intent);
                break;
        }
    }
}
