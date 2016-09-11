package com.moge.gege.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.data.dao.ShopcartDAO;
import com.moge.gege.model.*;
import com.moge.gege.model.enums.TradingSortType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.TradingCategoryRequest;
import com.moge.gege.network.request.TradingListRequest;
import com.moge.gege.network.request.TradingPromotionRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.HomeActivity;
import com.moge.gege.ui.ShoppingCartActivity;
import com.moge.gege.ui.adapter.GoodCategoryListAdapter;
import com.moge.gege.ui.adapter.TradingListAdapter;
import com.moge.gege.ui.adapter.TradingListAdapter.TradingListListener;
import com.moge.gege.ui.event.Event;
import com.moge.gege.ui.helper.DataCacheHelper;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.ui.widget.ImagePageView;
import com.moge.gege.ui.widget.ImagePageView.ImagePageViewListener;
import com.moge.gege.ui.widget.TradingCategoryPopupWindow;
import com.moge.gege.ui.widget.TradingCategoryPopupWindow.TradingCategoryPopWinListener;
import com.moge.gege.ui.widget.TradingPromotionView;
import com.moge.gege.util.FunctionUtil;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.ViewUtil;
import com.moge.gege.util.widget.MyGridView;
import com.moge.gege.util.widget.horizontalListview.widget.AdapterView;
import com.moge.gege.util.widget.horizontalListview.widget.HListView;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

public class WareHouseFragment extends BaseFragment implements
        TradingListListener, ImagePageViewListener, OnClickListener,
        TradingCategoryPopWinListener
{
    private Context mContext;

    @InjectView(R.id.promotionView) ImagePageView mPromotionView;
//    @InjectView(R.id.promotion2View) TradingPromotionView mPromotion2View;
//    @InjectView(R.id.promotion3View) TradingPromotionView mPromotion3View;
    @InjectView(R.id.promotion4View) TradingPromotionView mPromotion4View;
    @InjectView(R.id.tradingScrollView) PullToRefreshScrollView mRefreshScrollView;
    @InjectView(R.id.tradingGridView) MyGridView mGridView;

    private HListView mCategoryListView;
    private GoodCategoryListAdapter mGoodCategoryAdapter;
    private TradingListAdapter mAdapter;
    private String mNextCursor = "";

    private String mSortType = "";
    private String mCategoryId = "";

    private TradingCategoryPopupWindow mCategoryPopWin;

    // cache data
    private TradingListModel mTradingListData;
    private TradingPromotionListModel mPromotionListData;
    private CategoryListModel mTradingCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        final View layout = inflater.inflate(R.layout.fragment_warehouse,
                container, false);
        ButterKnife.inject(this, layout);
        initView(layout);
        initLocalData();
        initData();
        return layout;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        checkShoppingCart();
    }

    @Override
    protected void initView(View v)
    {
        super.initView(v);

        this.setHeaderTitle(R.string.gege_warehouse);
        this.setHeaderRightImage(R.drawable.icon_trading_filter);
        this.setHeaderLeftImage(R.drawable.icon_shoppingcart);

        mContext = this.getActivity();

        mCategoryListView = (HListView)v.findViewById(R.id.categoryListView);
        mGoodCategoryAdapter = new GoodCategoryListAdapter(mContext);
        mCategoryListView.setAdapter(mGoodCategoryAdapter);
        mCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BaseOptionModel model = mGoodCategoryAdapter.getItem(position);
                onTradingCategoryItemClick(model);
            }
        });

        mPromotionView.setListener(this);
        mAdapter = new TradingListAdapter(mContext);
        mAdapter.setListener(this);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(mAdapter);


        mRefreshScrollView.setMode(Mode.BOTH);
        mRefreshScrollView.setRefreshing();
        mRefreshScrollView
                .setOnRefreshListener(new OnRefreshListener2<ScrollView>()
                {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ScrollView> refreshView)
                    {
                        initData();
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ScrollView> refreshView)
                    {
                        doTradingListRequest(mNextCursor, mSortType,
                                mCategoryId);
                    }
                });
    }

    @Override
    protected void onHeaderDoubleClick()
    {
        mRefreshScrollView.getRefreshableView().smoothScrollTo(0, 0);
    }

    @Override
    protected void onHeaderLeftClick()
    {
        this.startActivity(new Intent(mContext, ShoppingCartActivity.class));
    }

    @Override
    protected void onHeaderRightClick(View v)
    {
        if (mCategoryPopWin == null)
        {
            mCategoryPopWin = new TradingCategoryPopupWindow(mContext, this);
        }

        mCategoryPopWin.showPopupWindow(v, 0, 0);
    }

    private void initLocalData()
    {
        mPromotionListData = DataCacheHelper
                .loadTradingPromotionListData(mContext);
        if (mPromotionListData != null)
        {
            updateTradingPromotionUI(mPromotionListData);
        }

        mTradingListData = DataCacheHelper.loadTradingListData(mContext);
        if (mTradingListData != null)
        {
            updateTradingListUI(mTradingListData, true);
        }

        mTradingCategory = DataCacheHelper.loadTradingCategoryListData(mContext);
        if (mTradingCategory != null)
        {
            updateTradingCategorys(mTradingCategory);
        }
    }

    private void initData()
    {
        doTradingPromotionRequest();
        doTradingCategoryRequest();
        doTradingListRequest(mNextCursor = "", mSortType = "",
                mCategoryId = "");
    }

    private void checkShoppingCart()
    {
        if (ShopcartDAO.instance().getShopcartSize(AppApplication.getLoginId())
                > 0)
        {
            this.setHeaderLeftImage(R.drawable.icon_shoppingcart_new);
        }
        else
        {
            this.setHeaderLeftImage(R.drawable.icon_shoppingcart);
        }
    }

    private void doTradingCategoryRequest()
    {
        TradingCategoryRequest request = new TradingCategoryRequest(

                new ResponseEventHandler<RespCategoryListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespCategoryListModel> request,
                            RespCategoryListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mTradingCategory = result.getData();
                            updateTradingCategorys(mTradingCategory);
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                    }
                });

        RequestManager.addRequest(request, this);
    }


    private void doTradingListRequest(String cursor, String sortType,
            String categoryId)
    {
        TradingListRequest request = new TradingListRequest(mNextCursor,
                GlobalConfig.getLongitude(), GlobalConfig.getLatitude(),
                sortType, categoryId,
                new ResponseEventHandler<RespTradingListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespTradingListModel> request,
                            RespTradingListModel result)
                    {
                        mRefreshScrollView.onRefreshComplete();

                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mTradingListData = result.getData();
                            updateTradingListUI(mTradingListData, false);
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                        }

                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                        mRefreshScrollView.onRefreshComplete();
                    }

                });
        executeRequest(request);
    }

    private void doTradingPromotionRequest()
    {
        TradingPromotionRequest request = new TradingPromotionRequest(
                GlobalConfig.getLongitude(), GlobalConfig.getLatitude(),
                new ResponseEventHandler<RespTradingPromotionListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespTradingPromotionListModel> request,
                            RespTradingPromotionListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mPromotionListData = result.getData()
                                    .getPromotions();
                            updateTradingPromotionUI(mPromotionListData);
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                    }

                });
        executeRequest(request);
    }

    private void updateTradingListUI(TradingListModel tradingListData, boolean localData)
    {
        if (mNextCursor.equals("") && mAdapter.getCount() > 0)
        {
            mAdapter.clear();
        }

        if (tradingListData.getTradings() != null
                && tradingListData.getTradings().size() > 0)
        {
            // 上拉显示新数据
            if (!TextUtils.isEmpty(mNextCursor))
            {
                new Handler().postDelayed(new Runnable()
                {
                    public void run()
                    {
                        mRefreshScrollView.getRefreshableView()
                                .smoothScrollBy(0,
                                        FunctionUtil.dip2px(mContext, 60));
                    }
                }, 300);
            }

            mAdapter.addAll(tradingListData.getTradings());
            mNextCursor = tradingListData.getNext_cursor();
        }
        else
        {
            ToastUtil.showToastShort(R.string.no_more_data);
        }

        if(!localData)
        {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void updateTradingPromotionUI(
            TradingPromotionListModel promotionListData)
    {
        int targetWidth = 0;
        int targetHeight = 0;

        mPromotionView.setDataSource(promotionListData.getPlace_1());

        // place2
//        mPromotion2View.setPromotionInfo(promotionListData.getPlace_2());
//        mPromotion2View.setOnClickListener(new MyClickListener(
//                promotionListData.getPlace_2()));
//
//        targetWidth = (int) (ViewUtil.getWidth() * 0.41);
//        targetHeight = (int) getImageTargetHeight(
//                promotionListData.getPlace_2().getImage(), targetWidth);
//        setViewSize(mPromotion2View, targetWidth, targetHeight);
//
//        // place3
//        mPromotion3View.setPromotionInfo(promotionListData.getPlace_3());
//        mPromotion3View.setOnClickListener(new MyClickListener(
//                promotionListData.getPlace_3()));
//
//        targetWidth = (int) (ViewUtil.getWidth() * 0.57);
//        targetHeight = (int) getImageTargetHeight(
//                promotionListData.getPlace_3().getImage(), targetWidth);
//        setViewSize(mPromotion3View, targetWidth, targetHeight);

        // place4
        targetWidth = (int) (ViewUtil.getWidth() * 1.0);
        targetHeight = (int) getImageTargetHeight(
                promotionListData.getPlace_4().getImage(), targetWidth);
        setViewSize(mPromotion4View, targetWidth, targetHeight);

        mPromotion4View.setPromotionInfo(promotionListData.getPlace_4());
        mPromotion4View.setOnClickListener(new MyClickListener(
                promotionListData.getPlace_4()));
    }

    private void updateTradingCategorys(CategoryListModel tradingCategory)
    {
        mGoodCategoryAdapter.clear();
        mGoodCategoryAdapter.addAll(tradingCategory.getCategorys());
        mGoodCategoryAdapter.notifyDataSetChanged();
    }

    public static void setViewSize(View v, int width, int height)
    {
        ViewGroup.LayoutParams params = v.getLayoutParams();
        if (params == null)
        {
            return;
        }

        params.width = width;
        params.height = height;
        v.setLayoutParams(params);
    }

    private int getImageTargetHeight(String imagename, int targetWidth)
    {
        int defaultHeight = 140;

        if (TextUtils.isEmpty(imagename) || imagename.length() <= 29)
        {
            return defaultHeight;
        }

        String leftStr = imagename.substring(29, imagename.indexOf("_", 29));

        String[] pxArray = leftStr.split("x");
        if (pxArray != null && pxArray.length >= 2)
        {
            int imageWidth;
            int imageHeight;

            imageWidth = Integer.parseInt(pxArray[0]);
            imageHeight = Integer.parseInt(pxArray[1]);
            defaultHeight = (int) (targetWidth * 1.0 / imageWidth
                    * imageHeight);
        }

        return defaultHeight;
    }

    private SimpleImageLoadingListener mImageLoadListener = new SimpleImageLoadingListener()
    {
        public void onLoadingComplete(String imageUri, View view,
                Bitmap loadedImage)
        {

            int targetWidth = 0;
            int targetHeight = 0;

            // if (view == mPlace2Image)
            // {
            // targetWidth = (int) (ViewUtil.getWidth() * 0.41);
            // targetHeight = (int) (targetWidth * 1.0
            // / loadedImage.getWidth() * loadedImage.getHeight());
            //
            // mPlace2Image.setLayoutParams(new RelativeLayout.LayoutParams(
            // targetWidth, targetHeight));
            // }
            // else if (view == mPlace3Image)
            // {
            // targetWidth = (int) (ViewUtil.getWidth() * 0.57);
            // targetHeight = (int) (targetWidth * 1.0
            // / loadedImage.getWidth() * loadedImage.getHeight());
            //
            // mPlace3Image.setLayoutParams(new RelativeLayout.LayoutParams(
            // targetWidth, targetHeight));
            // }
            // else if (view == mPlace4Image)
            // {
            // targetWidth = ViewUtil.getWidth();
            // targetHeight = (int) (targetWidth * 1.0
            // / loadedImage.getWidth() * loadedImage.getHeight());
            //
            // mPlace4Image.setLayoutParams(new RelativeLayout.LayoutParams(
            // targetWidth, targetHeight));
            // }

        }
    };

    @Override
    public void onShoppingCartClick(TradingModel model)
    {
        int leftNum = model.getNum() - model.getSale_num();
        if(leftNum < 1)
        {
            ToastUtil.showToastShort(R.string.no_enough_good);
            return;
        }

        leftNum -= ShopcartDAO.instance().getGoodBuyNum(AppApplication.getLoginId(), model.get_id());
        if(leftNum < 1)
        {
            ToastUtil.showToastShort(R.string.no_left_good);
            return;
        }

        model.setSelected(true);
        model.setBuyNum(1);
        if (ShopcartDAO.instance().insertShopcart(model,
                AppApplication.getLoginId()))
        {
            checkShoppingCart();
            ToastUtil.showToastShort(R.string.add_to_shoppingcard_success);
        }
        else
        {
            ToastUtil.showToastShort(R.string.add_to_shoppingcard_failed);
        }
    }

    @Override
    public void onTradingItemClick(TradingModel model)
    {
        UIHelper.showTradingDetailActivity(mContext, model.get_id());
    }

    @Override
    public void onImagePageClick(ImageModel model)
    {
        UIHelper.showUrlRedirect(mContext, model.getUrl());
    }

    @Override
    public void onImagePromotionFinish()
    {

    }

    @Override
    public void onImagePromotionUnStart(int hour, int minute, int second)
    {
    }

    @Override
    public void onImagePromotionIng(int hour, int minute, int second)
    {
    }

    public class MyClickListener implements OnClickListener
    {
        TradingPromotionModel model;

        MyClickListener(TradingPromotionModel model)
        {
            this.model = model;
        }

        @Override
        public void onClick(View v)
        {
            if (model != null)
            {
                UIHelper.showUrlRedirect(mContext, model.getUrl());
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            default:
                break;
        }
    }

    @Override
    public void onTradingCategoryItemClick(BaseOptionModel model)
    {
        if (TextUtils.isEmpty(model.get_id()))
        {
            return;
        }

        if (model.get_id().equals(TradingSortType.HOT)
                || model.get_id().equals(TradingSortType.LATEST)
                || model.get_id().equals(TradingSortType.PRAISE))
        {
            mSortType = model.get_id();
            mCategoryId = "";
            mNextCursor = "";
            doTradingListRequest(mNextCursor, mSortType, mCategoryId);
        }
        else if (model.get_id().equals(TradingSortType.MORE))
        {
            UIHelper.showTradingListActivity(mContext, false, "",
                    model.getName());
        }
        else
        {
            // to do list!!!
            // mSortType = "";
            // mCategoryId = model.get_id();
            // mNextCursor = "";
            // doTradingListRequest(mNextCursor, mSortType, mCategoryId);

            UIHelper.showTradingListActivity(mContext, false, model.get_id(),
                    model.getName());
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        DataCacheHelper.saveTradingListData(mContext, mTradingListData);
        DataCacheHelper.saveTradingPromotionListData(mContext,
                mPromotionListData);
        DataCacheHelper.saveTradingCategoryListData(mContext, mTradingCategory);
    }


    public void onEvent(Event.RefreshEvent event)
    {
        if(event.getRefreshIndex() != HomeActivity.INDEX_WAREHOUSE)
        {
            return;
        }

        initData();
        onHeaderDoubleClick();
    }
}
