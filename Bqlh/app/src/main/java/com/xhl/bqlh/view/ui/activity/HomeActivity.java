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
import com.xhl.bqlh.view.custom.MultiRadioGroup;
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

    @ViewInject(R.id.main_radio_group)
    private MultiRadioGroup mGroup;

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
        mGroup.check(view.getId());
        fragmentCacheManager.setCurrentFragment(view.getId());
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
        fragmentCacheManager.addFragmentToCache(mCbHome.getId(), HomeAFragment.class);
        fragmentCacheManager.addFragmentToCache(mCbClassify.getId(), HomeClassifyFragment.class);
        fragmentCacheManager.addFragmentToCache(mCbShopping.getId(), HomeCarFragment.class);
        fragmentCacheManager.addFragmentToCache(mCbMy.getId(), HomeMeFragment.class);
        fragmentCacheManager.setListener(new FragmentCacheManager.onBootCallBackListener() {
            @Override
            public void onBootCallBack() {
                checkFirst();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkFirst();
            }
        }, 10);

        new CheckServiceHelper(this).start();
    }

    public void checkFirst() {
        mGroup.check(mCbHome.getId());
        fragmentCacheManager.setCurrentFragment(mCbHome.getId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

    public void onEvent(CommonEvent event) {
        super.onEvent(event);
        //切换到首页
        if (event.getEventTag() == CommonEvent.ET_MAIN) {
            checkFirst();
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
                    intent.putExtra(WebPageActivity.TAG_COOKIE, getAppApplication().mCookie);
                    intent.putExtra(WebPageActivity.TAG_AREA, getAppApplication().mArea);
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
