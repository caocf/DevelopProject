package com.xhl.world.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;

import com.avos.avoscloud.AVAnalytics;
import com.google.gson.Gson;
import com.xhl.world.AppApplication;
import com.xhl.world.R;
import com.xhl.world.chat.PushMessage;
import com.xhl.world.chat.event.MessageEvent;
import com.xhl.world.config.Constant;
import com.xhl.world.config.NetWorkConfig;
import com.xhl.world.ui.activity.BaseAppActivity;
import com.xhl.world.ui.activity.ProductDetailsActivity;
import com.xhl.world.ui.activity.SearchDetailsActivity;
import com.xhl.world.ui.event.EventType;
import com.xhl.world.ui.event.GlobalEvent;
import com.xhl.world.ui.event.ProductDetailsEvent;
import com.xhl.world.ui.event.ShopEvent;
import com.xhl.world.ui.main.classify.ClassifyFragment;
import com.xhl.world.ui.main.home.HomeFragment;
import com.xhl.world.ui.main.my.MyFragment;
import com.xhl.world.ui.main.shopping.ShoppingFragment;
import com.xhl.world.ui.view.BadgeView;
import com.xhl.world.ui.view.MultiCheckBox;
import com.xhl.world.ui.webUi.WebPageActivity;
import com.xhl.xhl_library.ui.FragmentCacheManager;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_home)
public class MainActivity extends BaseAppActivity {

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

    private String indexAdvUrl = "";

    private BadgeView mShopNum;

    @Override
    protected void onStart() {
        super.onStart();
        //统计通过推送打开界面的
        Intent intent = getIntent();
        AVAnalytics.trackAppOpened(intent);
    }

    @Override
    protected void initParams() {
       /* mShopNum = new BadgeView(this);
        mShopNum.setTargetView(mCbShopping);
        mShopNum.setBadgeCount(40);*/

        fragmentCacheManager = new FragmentCacheManager();
        fragmentCacheManager.setUp(this, R.id.fl_content);
        fragmentCacheManager.addFragmentToCache(1, HomeFragment.class);
        fragmentCacheManager.addFragmentToCache(2, ClassifyFragment.class);
        fragmentCacheManager.addFragmentToCache(3, ShoppingFragment.class);
        fragmentCacheManager.addFragmentToCache(4, MyFragment.class);
        fragmentCacheManager.setListener(new FragmentCacheManager.onBootCallBackListener() {
            @Override
            public void onBootCallBack() {
                checkItem(1);
            }
        });

        mCbHome.post(new Runnable() {
            @Override
            public void run() {
                checkItem(1);
            }
        });

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

    @Override
    public void onBackPressed() {
        fragmentCacheManager.onBackPress();
    }

    @Override
    protected void onReloadClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AppApplication.appContext.chatDefaultLogin();
        handIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.e("main onNewIntent");
        handIntent(intent);
    }

    //处理启动的Intent
    private void handIntent(Intent intent) {
        AppApplication.appContext.mAppExit = false;

        if (intent == null) {
            Logger.e("handIntent intent is null");
            return;
        }

        String msgType = intent.getStringExtra(Constant.intent_message_type);
        if (TextUtils.isEmpty(msgType)) {
            return;
        }
        //推送的系统消息处理
        if (msgType.equals(Constant.intent_system_message)) {
            String stringExtra = intent.getStringExtra(Constant.intent_message_data);
            if (TextUtils.isEmpty(stringExtra)) {
                return;
            }
            PushMessage message = new Gson().fromJson(stringExtra, PushMessage.class);
            handlePushMessage(message);
        } else if (msgType.equals(Constant.intent_chat_message)) {

        } else if (msgType.equals(Constant.intent_change_to_main)) {//转到首页
            checkItem(1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void handlePushMessage(PushMessage message) {
        // 1：基本消息，2：商品详情，3：店铺详情，4：广告Url
        switch (message.getPushType()) {
            case "1":
                break;
            case "2":
                ProductDetailsEvent event = new ProductDetailsEvent();
                event.productId = message.getPushAttr();
                onEvent(event);
                break;
            case "3":
                ShopEvent shopEvent = new ShopEvent();
                shopEvent.shopId = message.getPushAttr();
                onEvent(shopEvent);
                break;
            case "4":
                Intent url = new Intent(MainActivity.this, WebPageActivity.class);
                url.putExtra(WebPageActivity.TAG_URL, message.getPushAttr());
                startActivity(url);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        AppApplication.appContext.mAppExit = true;
        super.onDestroy();
    }

    public void onEvent(MessageEvent event) {
        Logger.e("MessageEvent onEvent:" + event.message);
        //在消息界面点击直接跳转
        if (event.pushMessage != null) {
            handlePushMessage(event.pushMessage);
        } else {
            //广播里发送的消息处理
            //在没有退出应用时候，显示首页
            Intent startMain = new Intent(MainActivity.this, MainActivity.class);
            startMain.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            //消息类型
            startMain.putExtra(Constant.intent_message_type, event.type);
            //消息数据
            startMain.putExtra(Constant.intent_message_data, event.message);

            startActivity(startMain);
        }
    }

    //商品详情统一跳转
    public void onEvent(ProductDetailsEvent event) {
        Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
        intent.putExtra("productId", event.productId);
        startActivity(intent);
    }

    //店铺详情统一跳转
    public void onEvent(ShopEvent event) {
        //本地的店铺url
        String url = Constant.URL_shop + event.shopId;
        //webApp分享的链接
        String shareUrl = NetWorkConfig.web_app_index + "?shopId=" + event.shopId;
        //网页参数
        Intent intent = new Intent(this, WebPageActivity.class);
        //店铺网页
        intent.putExtra(WebPageActivity.TAG_URL, url);
        //分享的链接
        intent.putExtra(WebPageActivity.TAG_SHARE_URL, shareUrl);
        //店铺标题
        intent.putExtra(WebPageActivity.TAG_TITLE, event.shopTitle);
        //分享的图标
        intent.putExtra(WebPageActivity.TAG_SHARE_ICON, event.shopIcon);
        //按钮样式
        intent.putExtra(WebPageActivity.right_button_style, WebPageActivity.rightButtonStyleShare);
        startActivity(intent);
    }

    public void onEventBackgroundThread(GlobalEvent<String> event) {
        //处理扫码结果
        if (event.getEventType() == EventType.Event_Scan) {
            String text = event.getObject();

            if (URLUtil.isNetworkUrl(text)) {//默认店铺
                Intent intent = new Intent(this, WebPageActivity.class);
                intent.putExtra(WebPageActivity.TAG_URL, text);
                intent.putExtra(WebPageActivity.TAG_QUIT, false);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, SearchDetailsActivity.class);
                intent.putExtra(SearchDetailsActivity.SEARCH_TYPE, SearchDetailsActivity.SEARCH_TYPE_SCAN);
                intent.putExtra(SearchDetailsActivity.SEARCH_PARAMS, text);
                startActivity(intent);
            }
        }
        //重新登陆操作
        else if (event.getEventType() == EventType.Event_ReLogin) {
            AppApplication.appContext.setUserLogout();
            AppApplication.appContext.isLogin(this);
        }
    }

   /* //聊天登陆
    public void onEventBackgroundThread(ChatLoginFinish login) {
        if (TextUtils.isEmpty(login.chatUserId)) {
            return;
        }
        ChatManager chatManager = ChatManager.getInstance();
        if (chatManager.isConnect()) {
            return;
        }
        //根据试航的用户唯一ID，用作第三方聊天的唯一ID
        chatManager.setupManagerWithUserId(login.chatUserId);
        chatManager.openClient(null);
    }*/

    public void onEvent(GlobalEvent event) {
        if (event.getEventType() == EventType.Event_Go_HOME) {
            checkItem(1);
        }
    }
}
