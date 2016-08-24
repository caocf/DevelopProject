package com.xhl.world.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.config.Constant;
import com.xhl.world.model.Base.Response;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.CollectionModel;
import com.xhl.world.ui.event.CollectionOpEvent;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.recyclerHolder.ProductItemDataHolder;
import com.xhl.world.ui.recyclerHolder.ShopItemDataHolder;
import com.xhl.world.ui.utils.DialogMaker;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.world.ui.webUi.WebPageActivity;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayout;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayoutDirection;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 15/12/31.
 */
@ContentView(R.layout.fragment_my_care_detail)
public class MyCareDetailFragment extends BaseAppFragment implements Callback.CommonCallback<ResponseModel<Response<CollectionModel>>> {

    @ViewInject(R.id.order_recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.swipy_refresh_layout)
    private SwipyRefreshLayout mSwipRefresh;

    @ViewInject(R.id.rl_null_show)
    private View mNullView;

    @ViewInject(R.id.iv_null_ic)
    private ImageView iv_null_ic;

    @ViewInject(R.id.tv_null_txt)
    private TextView tv_null_txt;

    private LinearLayoutManager mLayoutManager;
    private RecyclerAdapter mRecyclerAdapter;
    private int mTag;
    private List<RecyclerDataHolder> mDatas;
    private boolean isFirstLoad = true;

    public static MyCareDetailFragment instance(int tag) {
        MyCareDetailFragment fragment = new MyCareDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tag", tag);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTag = getArguments().getInt("tag");
        query();
    }

    @Override
    protected void initParams() {

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDatas = new ArrayList<>();
        mRecyclerAdapter = new RecyclerAdapter(getContext());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        initPullRefresh();
    }


    private void initPullRefresh() {
        mSwipRefresh.setColorSchemeResources(R.color.app_green, R.color.app_blue);
        mSwipRefresh.setDirection(SwipyRefreshLayoutDirection.TOP);
        mSwipRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    query();
                }
            }
        });
    }

    private void query() {
        if (isFirstLoad) {
            showLoadingDialog();
        }
        if (mTag == 0) {
            queryProductData();
        } else {
            queryShopData();
        }
    }

    private void queryShopData() {
        ApiControl.getApi().collectionQueryShop(this);

    }

    private void queryProductData() {

        ApiControl.getApi().collectionQueryProduct(this);

    }

    private void setShowData(List<CollectionModel> products) {
        if (products == null || products.size() <= 0) {
            if (mTag == 0) {
                iv_null_ic.setImageResource(R.drawable.icon_null_product);
                tv_null_txt.setText("亲，没有看到您收藏的商品");
            } else {
                iv_null_ic.setImageResource(R.drawable.icon_null_shop);
                tv_null_txt.setText("亲，没有看到您收藏的店铺");
            }
            mRecyclerAdapter.setDataHolders(null);
            mNullView.setVisibility(View.VISIBLE);
            return;
        }
        mDatas.clear();

        for (CollectionModel collection : products) {
            RecyclerDataHolder holder = null;
            collection.mFragmentTag = mTag;
            if (mTag == 0) {
                holder = new ProductItemDataHolder(collection);
            } else if (mTag == 1) {
                holder = new ShopItemDataHolder(collection);
            }
            mDatas.add(holder);
        }
        mRecyclerAdapter.setDataHolders(mDatas);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDatas != null && mDatas.size() > 0) {
            mRecyclerAdapter.setDataHolders(mDatas);
        }
    }

    @Override
    public void onSuccess(ResponseModel<Response<CollectionModel>> result) {
        if (result.isSuccess()) {
            List<CollectionModel> list = result.getResultObj().getRows();
            setShowData(list);
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        SnackMaker.shortShow(mSwipRefresh, R.string.network_error);
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {
        mSwipRefresh.setRefreshing(false);
        hideLoadingDialog();
        isFirstLoad = false;
    }

    //删除事件
    public void onEvent(CollectionOpEvent event) {

        if (event.data.mFragmentTag != mTag) {
            return;
        }

        if (event.actionType == 0) {//点击

            if (event.collectionType == 0) {//商品点击
                EventBusHelper.postProductDetails(event.productId);
            } else if (event.collectionType == 1) {//店铺点击
                //发送店铺详情事件
//                ShopEvent shopEvent = new ShopEvent();
//                shopEvent.shopId = event.data.getShopId();
//                shopEvent.shopUrl = event.data.getShopUrl();
//                shopEvent.shopIcon = event.data.getShopLogo();
//                shopEvent.shopTitle = event.data.getShopName();
//                EventBusHelper.post(event);
                //店铺url
                String url = Constant.URL_shop + event.data.getShopId();
                Intent intent = new Intent(getActivity(), WebPageActivity.class);
                //店铺网页
                intent.putExtra(WebPageActivity.TAG_URL, url);
                //店铺标题
                intent.putExtra(WebPageActivity.TAG_TITLE, event.data.getShopName());
                //按钮样式
                intent.putExtra(WebPageActivity.right_button_style, WebPageActivity.rightButtonStyleShare);
                startActivity(intent);
            }
        } else if (event.actionType == 1) {//长按
            onDeleteCollection(event.collectionType, event.data.getId());
        }
    }


    public void onDeleteCollection(int type, final String collectionId) {
        String title = "";
        String msg = "";
        if (type == 0) {
            title = "删除商品";
            msg = "您确定要删除收藏的商品吗?";
        } else {
            title = "删除店铺";
            msg = "您确定要删除收藏的店铺吗?";
        }

        DialogMaker.showAlterDialog(getContext(), title, msg, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete(collectionId);
            }
        });
    }

    private void delete(String id) {
        showLoadingDialog();

        ApiControl.getApi().collectionRemove(id, new CommonCallback<ResponseModel>() {
            @Override
            public void onSuccess(ResponseModel result) {
                if (result.isSuccess()) {
                    query();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(mSwipRefresh, ex.getMessage());
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
}
