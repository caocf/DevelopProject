package com.xhl.world.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.umeng.socialize.UMShareAPI;
import com.xhl.sum.chatlibrary.Constants;
import com.xhl.sum.chatlibrary.controller.ChatManager;
import com.xhl.world.AppApplication;
import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.chat.view.ChatRoomActivity;
import com.xhl.world.config.Constant;
import com.xhl.world.config.NetWorkConfig;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.ProductAttachModel;
import com.xhl.world.model.ProductDetailsModel;
import com.xhl.world.ui.adapter.FragmentViewPagerAdapter;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.event.EventType;
import com.xhl.world.ui.event.GlobalEvent;
import com.xhl.world.ui.event.ShopEvent;
import com.xhl.world.ui.fragment.ProductImageFragment;
import com.xhl.world.ui.fragment.ProductJudgeFragment;
import com.xhl.world.ui.main.MainActivity;
import com.xhl.world.ui.main.shopping.ShoppingFragment;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.world.ui.utils.ToastUtil;
import com.xhl.world.ui.utils.umeng.share.CustomShareBoard;
import com.xhl.world.ui.utils.umeng.share.ShareParam;
import com.xhl.world.ui.view.LazyScrollView;
import com.xhl.world.ui.view.MultiCheckBox;
import com.xhl.world.ui.view.ProductOperatorBroad;
import com.xhl.world.ui.view.RoundedImageView;
import com.xhl.world.ui.webUi.WebPageActivity;
import com.xhl.xhl_library.ui.view.RippleView;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/1/20.
 */
@ContentView(R.layout.activity_product_details)
public class ProductDetailsActivity extends BaseAppActivity implements ProductOperatorBroad.OperatorListener {

    @Event(value = R.id.title_back)
    private void onBackClick(View view) {
        finish();
    }

    @ViewInject(R.id.lazy_scrollview)
    private LazyScrollView mLazyScrollview;

    @ViewInject(R.id.ll_product)//商品服务
    private LinearLayout mProductsContainer;

    @ViewInject(R.id.goods_image)//商品描述图片
    private ViewPager goods_image;

    @ViewInject(R.id.fl_goods_image_num)//商品个数显示
    private FrameLayout fl_goods_image_num;

    @ViewInject(R.id.tv_num_cur)//商品当前位置
    private TextView tv_num_cur;

    @ViewInject(R.id.tv_num_max)//商品最多
    private TextView tv_num_max;

    @ViewInject(R.id.tv_product_type)//商品类型
    private TextView tv_product_type;

    @ViewInject(R.id.tv_product_name)//商品名称
    private TextView tv_product_name;

    @ViewInject(R.id.tv_product_price)//商品价格
    private TextView tv_product_price;

    @ViewInject(R.id.tv_product_origin_price)//商品海外原价
    private TextView tv_product_origin_price;

    @ViewInject(R.id.iv_shop_logo)//店铺logo
    private RoundedImageView iv_shop_logo;

    @ViewInject(R.id.tv_shop_name)//店铺名称
    private TextView tv_shop_name;

    @ViewInject(R.id.tv_product_tip)//购物提示
    private TextView tv_product_tip;

    @ViewInject(R.id.tv_judge_num)//评价人数
    private TextView tv_judge_num;

    @ViewInject(R.id.tv_scroll_hint)
    private TextView tv_scroll_hint;

    @Event(R.id.iv_share)
    private void onShareClick(View view) {
        if (mProduct == null) {
            return;
        }
        ShareParam param = new ShareParam();
        param.setTitle(mProduct.getProductName());
        param.setContent(mProduct.getProductProperty() + "\t" + mProduct.getProductName());
        param.setImageUrl(mProduct.getProductPic());

        String target = NetWorkConfig.web_app_index + "?productId=" + mProductId;

        param.setTargetUrl(target);

        CustomShareBoard shareBoard = new CustomShareBoard(this, param);
        shareBoard.show();

    }

    @ViewInject(R.id.btn_op_2)//收藏状态
    private MultiCheckBox btn_op_2;

    @ViewInject(R.id.btn_op_4)
    private Button btn_op_4;

    @Event(R.id.btn_op_4) // 添加到购物车
    private void onAddShoppingCarClick(View view) {
        if (!AppApplication.appContext.isLogin(this)) {
            return;
        }
        if (!isLoading) {
            ProductOperatorBroad broad = new ProductOperatorBroad(this, mProduct);
            broad.setListener(this);
            broad.show();
        }
    }

    @Event(R.id.btn_op_3) // 购物车
    private void onShoppingCarClick(View view) {
        if (!AppApplication.appContext.isLogin(this)) {
            return;
        }
        //带有返回按钮的界面
        pushFragmentToBackStack(ShoppingFragment.class, ShoppingFragment.Type_Other);
    }

    @Event(R.id.btn_op_2) // 添加收藏
    private void onAddCollectionClick(View view) {
        if (!AppApplication.appContext.isLogin(this)) {
            return;
        }
        if (!isCollectionProduct) {
            collectionAdd();
        } else {
            SnackMaker.shortShow(mProductsContainer, R.string.collection_finish);
        }
    }

    private void collectionAdd() {
        ApiControl.getApi().collectionAdd(mProductId, "1", new Callback.CommonCallback<ResponseModel>() {
            @Override
            public void onSuccess(ResponseModel result) {
                if (result.isSuccess()) {
                    SnackMaker.shortShow(mProductsContainer, R.string.collection_success);
                    isCollectionProduct = true;//收藏的商品
                    btn_op_2.setImageIcon(R.drawable.icon_product_collectioned);
                    btn_op_2.setText(getString(R.string.product_op_add_collectioned));
                } else {
                    String message = result.getMessage();
                    if (message.startsWith("已经收藏")) {
                        btn_op_2.setImageIcon(R.drawable.icon_product_collectioned);
                        btn_op_2.setText(getString(R.string.product_op_add_collectioned));
                        SnackMaker.shortShow(mProductsContainer, R.string.collection_finish);
                    }
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

    @Event(R.id.btn_op_1) // 客服
    private void onServiceClick(View view) {
     /*   if (!AppApplication.appContext.isLogin(this)) {
            return;
        }
        String shopId = mProduct.getShop().getShopId();
        if (TextUtils.isEmpty(shopId)) {
            return;
        }
        chatConnect();*/
        //启动首页
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constant.intent_message_type, Constant.intent_change_to_main);
        startActivity(intent);

    }

    private void connectDo() {
        //启动聊天界面
        Intent intent = new Intent(this, ChatRoomActivity.class);
        intent.putExtra(Constants.MEMBER_ID, mProduct.getShop().getShopId());//成员id
//        intent.putExtra(Constants.MEMBER_ID, "469aa3d4c9c94638abec6aa97e5791c9");//成员id
        //会话名称，商家名
        intent.putExtra(Constants.CONVERSATION__NAME, mProduct.getShop().getShopName());
        //会话扩展参数，商家Logo
        intent.putExtra(Constants.shop_attr_conversation_logo, mProduct.getShop().getShopLogo());
        //会话扩展参数，商家地址
        intent.putExtra(Constants.shop_attr_conversation_url, mProduct.getShop().getShopUrl());
        startActivity(intent);
    }

    private void chatConnect() {

        boolean isConnect = ChatManager.getInstance().isConnect();
        if (isConnect) {
            connectDo();
        } else {
            showProgressLoading("正在登陆聊天服务器,请稍后...");

            String userId = AppApplication.appContext.getLoginUserInfo().userId;

            ChatManager chatManager = ChatManager.getInstance();
            //根据试航的用户唯一ID，用作第三方聊天的唯一ID
            chatManager.setupManagerWithUserId(userId);
            chatManager.openClient(new AVIMClientCallback() {
                @Override
                public void done(AVIMClient avimClient, AVIMException e) {
                    hideLoadingDialog();
                    if (e == null) {
                        connectDo();
                    } else {
                        ToastUtil.showToastShort(e.getMessage());
                    }
                }
            });
        }

    }

    @Event(R.id.tv_product_tip)
    private void onTipClick(View view) {
       /* AlertDialog.Builder dialog = DialogMaker.getDialog(this);
        dialog.setTitle(R.string.dialog_title);
        dialog.setMessage(R.string.product_tip_msg);
        dialog.setNegativeButton(R.string.dialog_ok, null);
        dialog.create().show();*/
        Intent intent = new Intent(this, WebPageActivity.class);
        intent.putExtra(WebPageActivity.TAG_URL, Constant.URL_tip);
        intent.putExtra(WebPageActivity.TAG_TITLE, getString(R.string.product_tip));
        intent.putExtra(WebPageActivity.TAG_QUIT, false);
        startActivity(intent);
    }

    @Event(value = R.id.rl_judge, type = RippleView.OnRippleCompleteListener.class)
    private void onJudgeClick(View view) {
        pushFragmentToBackStack(ProductJudgeFragment.class, mProductId);
    }

    @Event(value = R.id.rl_shop, type = RippleView.OnRippleCompleteListener.class)
    private void onShopClick(View view) {
        //店铺id
        String id = mProduct.getShop().getShopId();
        if (TextUtils.isEmpty(id)) {
            return;
        }
        ShopEvent event = new ShopEvent();
        event.shopId = id;
        event.shopUrl = mProduct.getShop().getShopUrl();
        event.shopIcon = mProduct.getShop().getShopLogo();
        event.shopTitle = mProduct.getShop().getShopName();
        EventBusHelper.post(event);
    }

    @Override
    protected boolean needRoot() {
        return false;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fl_content;
    }

    @Override
    protected void initParams() {
        btn_op_4.setEnabled(false);
        //设置中线
//        tv_product_origin_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        mLazyScrollview.setmListener(new LazyScrollView.ScrollListener() {
            @Override
            public void onScrollBottom() {
                onScrollBottomDo();
            }

            @Override
            public void onScrollY(int nowY, int oldY) {

            }
        });
    }

    private ArrayList<String> mImageUrls;
    private String mProductId;

    private ProductDetailsModel mProduct;
    private boolean isLoading = false;
    private boolean isCollectionProduct = false;
    private boolean mHasAddWebView = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProductId = getIntent().getStringExtra("productId");
        loadProductDetails();
    }

    private void loadProductDetails() {

        if (TextUtils.isEmpty(mProductId)) {
            return;
        }

        if (isLoading) {
            return;
        }
        isLoading = true;
        showLoadingDialog();

        ApiControl.getApi().productDetails(mProductId, new Callback.CommonCallback<ResponseModel<ProductDetailsModel>>() {
            @Override
            public void onSuccess(ResponseModel<ProductDetailsModel> result) {
                if (result.isSuccess()) {
                    mProductsContainer.setVisibility(View.VISIBLE);
                    btn_op_4.setEnabled(true);
                    mProduct = result.getResultObj();
                    showData();
                } else {
                    SnackMaker.shortShow(mProductsContainer, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(mProductsContainer, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                isLoading = false;
                hideLoadingDialog();
            }
        });
    }

    //基本数据显示
    private void showData() {
        if (mProduct == null) {
            return;
        }
//        StringBuilder product_name = new StringBuilder();
//        product_name.append(mProduct.getProductProperty()).append("\t").append(" ").append(mProduct.getProductName());

        String name = (String) TextUtils.concat(mProduct.getProductProperty(), " ", " ", " ", mProduct.getProductName());

        tv_product_type.setText(mProduct.getProductProperty());//商品类型
        tv_product_name.setText(name);//商品名称
        tv_product_price.setText(getString(R.string.price, mProduct.getRetailPrice()));//当前价格
        tv_product_origin_price.setText(getString(R.string.price_origin, mProduct.getOriginalPrice()));//海外原价
        tv_judge_num.setText(getString(R.string.product_judge_num, mProduct.getEvaluateTotalNum()));//评价数

        showProductImage();
        //店铺数据
        iv_shop_logo.LoadDrawable(mProduct.getShop().getShopLogo());
        tv_shop_name.setText(mProduct.getShop().getShopName());

        //收藏状态
        if (mProduct.isCollection()) {
            isCollectionProduct = true;//收藏的商品
            btn_op_2.setImageIcon(R.drawable.icon_product_collectioned);
            btn_op_2.setText(getString(R.string.product_op_add_collectioned));
        }
    }

    private void showProductImage() {
        List<ProductAttachModel> attachment = mProduct.getAttachment();
        if (attachment.size() <= 0) {
            return;
        }
        fl_goods_image_num.setVisibility(View.VISIBLE);
        tv_num_cur.setText("1");
        tv_num_max.setText(String.valueOf(attachment.size()));

        List<Fragment> images = new ArrayList<>();

        mImageUrls = new ArrayList<>();

        for (ProductAttachModel product : attachment) {
            //大图
            mImageUrls.add(product.getLargeFilePath());

            images.add(ProductImageFragment.instance(product.getFilePath()));
        }

        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getSupportFragmentManager());
        adapter.setFragments(images);
        goods_image.setAdapter(adapter);
        //切换动画
//        goods_image.setPageTransformer(true, new ZoomOutPageTransformer());
        //商品位置
        goods_image.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_num_cur.setText(String.valueOf(position + 1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //添加到购物车
    private void addToShopCar(String num) {

        showLoadingDialog();

        ApiControl.getApi().addToShoppingCar(mProductId, mProduct.getProductType(), num, new Callback.CommonCallback<ResponseModel<Integer>>() {
            @Override
            public void onSuccess(ResponseModel<Integer> result) {
                if (result.isSuccess()) {
                    if (result.getResultObj() == 1) {
                        ToastUtil.showToastShort("加入成功");
                    }
                } else {
                    SnackMaker.shortShow(mProductsContainer, result.getResultObj());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(mProductsContainer, ex.getMessage());
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

    private void onScrollBottomDo() {
        if (mHasAddWebView) {
            return;
        }
        webView();
    }

    private void webView() {
        //富媒体链接
        String dataUrl = mProduct.getProductDesc();
        if (TextUtils.isEmpty(dataUrl)) {
            return;
        }
        final WebView view = new WebView(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.topMargin = getResources().getDimensionPixelOffset(R.dimen.px_dimen_20);
        view.setLayoutParams(params);

        AutoUtils.auto(view);

        //参数设置
        WebSettings settings = view.getSettings();
        settings.setSupportZoom(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }

        view.setWebViewClient(new WebViewClient());

//        view.loadUrl(Constant.URL_product_desc);
        view.loadData(getProductHtmlData(dataUrl), "text/html; charset=utf-8", "utf-8");
        mProductsContainer.addView(view);
        mHasAddWebView = true;

        tv_scroll_hint.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_scroll_hint.setVisibility(View.GONE);
            }
        }, 500);
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    private String getProductHtmlData(String product) {

        String body = product.replaceAll("src=\"", "src=\"" + NetWorkConfig.imageHost);

        return getHtmlData(body);
    }


    @Override
    public void onAddClick(int num) {
        //添加到购物车
        addToShopCar(String.valueOf(num));
        //通知购物车刷新
        EventBusHelper.posRefreshShopCarEvent();
    }

    public void onEvent(GlobalEvent event) {
        //单个图片
        if (event.getEventType() == EventType.Event_SingleImageDetails) {
            String url = (String) event.getObject();
            if (URLUtil.isValidUrl(url)) {
                Intent intent = new Intent(this, ImageDetailsActivity.class);

                intent.putExtra(ImageDetailsActivity.Image_source, url);

                intent.putExtra(ImageDetailsActivity.Image_type, ImageDetailsActivity.Image_url);

                startActivity(intent);
            } else {
                SnackMaker.shortShow(mProductsContainer, "图片错误 =.=!!");
            }

        }
        //多张图片
        else if (event.getEventType() == EventType.Event_MulitImageDetails) {
            ArrayList<String> urls = mImageUrls;

            Intent intent = new Intent(this, ImageDetailsActivity.class);

            intent.putStringArrayListExtra(ImageDetailsActivity.Image_source, urls);

            intent.putExtra(ImageDetailsActivity.Image_type, ImageDetailsActivity.Image_urls);

            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
