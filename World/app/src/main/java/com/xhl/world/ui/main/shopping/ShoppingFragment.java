package com.xhl.world.ui.main.shopping;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.ShoppingItemDetailsModel;
import com.xhl.world.mvp.domain.ShoppingUserCaseController;
import com.xhl.world.mvp.domain.back.ErrorEvent;
import com.xhl.world.mvp.presenters.ShoppingPresenter;
import com.xhl.world.mvp.views.ShoppingView;
import com.xhl.world.ui.adapter.SelectStateRecyclerAdapter;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.event.EventType;
import com.xhl.world.ui.event.ReLoadEvent;
import com.xhl.world.ui.event.ShoppingEvent;
import com.xhl.world.ui.fragment.BaseAppFragment;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.world.ui.utils.ViewUtils;
import com.xhl.world.ui.view.myLoadRelativeLayout;
import com.xhl.world.ui.view.pub.PageScrollListener;
import com.xhl.world.ui.view.pub.callback.RecyclerViewScrollBottomListener;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayout;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayoutDirection;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 15/11/25.
 */
@ContentView(R.layout.fragment_shopping)
public class ShoppingFragment extends BaseAppFragment implements ShoppingView<ShoppingItemDetailsModel>, RecyclerViewScrollBottomListener {

    public static final int Type_Other = 1;

    @ViewInject(R.id.title_name)
    private TextView title_name;

    @ViewInject(R.id.title_other)
    private Button title_other;

    @ViewInject(R.id.title_back)
    private RippleView title_back;

    @Event(R.id.title_other)
    private void onEditClick(View view) {
        mPresenter.onEditClick();
    }

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        if (mEnterType == Type_Other) {
            getSumContext().popTopFragment(null);
        }
    }


    @ViewInject(R.id.goods_refresh_layout)
    private SwipyRefreshLayout mSwipRefresh;

    @ViewInject(R.id.fl_operator)
    private View fl_operator;//操作的最外层容器

    @ViewInject(R.id.rl_null_show)
    private View mNullLayout;

    @ViewInject(R.id.btn_go)//随便逛逛按钮
    private View btn_go;

    @ViewInject(R.id.rl_goods_content)
    private myLoadRelativeLayout rl_goods_content;


    @ViewInject(R.id.shop_car_recycler_view)
    private RecyclerView mShopRecyclerView;

    @ViewInject(R.id.rl_style_count)
    private RelativeLayout mRlCountLayout;//计算面板

    @ViewInject(R.id.check_all_orders)
    private CheckBox check_all_orders;
    @ViewInject(R.id.tv_free_all_orders)
    private TextView tv_free_all_orders;
    @ViewInject(R.id.btn_count_orders)
    private Button btn_count_orders;

    @Event(R.id.btn_count_orders)
    private void onAccountClick(View view) {
        if (mGoodsCount.equals("0")) {
            SnackMaker.shortShow(title_name, R.string.my_shopping_count_null_hint);
        } else {
            mPresenter.onAccountOrder();
        }
    }

    @ViewInject(R.id.rl_style_edit)
    private RelativeLayout mRlEditLayout;//编辑面板

    @ViewInject(R.id.edit_check_all_orders)
    private CheckBox edit_check_all_orders;
    @ViewInject(R.id.btn_edit_delete)
    private Button btn_edit_delete;

    @Event(R.id.btn_edit_delete)
    private void onDeleteClick(View view) {
        if (!mGoodsCount.equals("0")) {
            String msg = getResources().getString(R.string.shop_delete_goods_tips, mGoodsCount);
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle(R.string.dialog_title).setMessage(msg).setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPresenter.onDeleteOrder();
                }
            }).setNegativeButton(R.string.dialog_cancel, null).show();
        } else {
            SnackMaker.shortShow(title_name, "请选择您要删除的商品");
        }
    }

    @Event(R.id.btn_move_collection)
    private void onMoveCollectionClick(View view) {
        if (!mGoodsCount.equals("0")) {
            String msg = getResources().getString(R.string.shop_collection_goods_tips);
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle(R.string.dialog_title).setMessage(msg).setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPresenter.onMoveCollection();
                }
            }).setNegativeButton(R.string.dialog_cancel, null).show();
        }
    }

    //使用点击事件代替切换监听，避免非主动触发事件导致业务逻辑错误
    //适配器OnBindView需要在View可见的时候调用，业务逻辑需要调用对应View中数据直接计算
    @Event(R.id.check_all_orders)
    private void onCheckOrderClick(View view) {
        if (check_all_orders.isChecked()) {
            mAdapter.selectAll();
            selectAll(true);
        } else if (!check_all_orders.isChecked()) {
            mAdapter.clearSelectState();
            selectAll(false);
        }
    }

    private void selectAll(boolean select) {
        for (ShoppingDataHolder holder : mShoppingViews) {
            ShopItemBar bar = holder.getBar(getContext());
            if (select) {
                bar.countSelectAllChild();
            } else {
                bar.countUnSelectAllChild();
            }
        }
    }

    @Event(R.id.edit_check_all_orders)
    private void onEditCheckOrderClick(View view) {
        if (edit_check_all_orders.isChecked()) {
            mAdapter.selectAll();
            selectAll(true);
        } else if (!edit_check_all_orders.isChecked()) {
            mAdapter.clearSelectState();
            selectAll(false);
        }
    }

    private boolean isEditStyle = false;
    private boolean isFirstLoad = true;
    private boolean isExistOrder = false;
    private LinearLayoutManager mLayoutManager;
    private SelectStateRecyclerAdapter mAdapter;
    private ShoppingPresenter mPresenter;
    private List<ShoppingDataHolder> mShoppingViews;
    private String mGoodsCount = "0";
    private int mEnterType = -1;

    @Override
    protected boolean needEventBusRegister() {
        return false;
    }


    @Event(R.id.btn_go)
    private void onGoClick(View view) {
        EventBusHelper.postGoHomeEvent();
    }

    @Override
    public void onEnter(Object data) {
        if (data != null) {
            mEnterType = (int) data;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new ShoppingPresenter(new ShoppingUserCaseController());
        mPresenter.attachView(this);

        //购物车需要在创建后一直监听，在需要更新的时候通知更新购物车数据
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initParams() {
        if (mEnterType == Type_Other) {
            title_back.setVisibility(View.VISIBLE);
            btn_go.setVisibility(View.GONE);
        } else {
            title_back.setVisibility(View.INVISIBLE);
            title_back.setEnabled(false);
        }
        title_name.setText(R.string.my_shopping_car);
        title_other.setText(R.string.my_shopping_edit);

        mShoppingViews = new ArrayList<>();
        mAdapter = new SelectStateRecyclerAdapter(getContext());
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mShopRecyclerView.setLayoutManager(mLayoutManager);
        mShopRecyclerView.setAdapter(mAdapter);
        mShopRecyclerView.setHasFixedSize(true);
        PageScrollListener listener = new PageScrollListener(mLayoutManager);
        listener.addBottomListener(this);
        mShopRecyclerView.addOnScrollListener(listener);

        initPullRefresh();
    }

    private void initPullRefresh() {
        mSwipRefresh.setColorSchemeResources(R.color.app_green, R.color.app_blue);
        mSwipRefresh.setDirection(SwipyRefreshLayoutDirection.TOP);
        mSwipRefresh.setDistanceToTriggerSync(130);
        mSwipRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    mPresenter.refreshTop();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        hideLoadingView();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void setLoadData(List<ShoppingItemDetailsModel> data) {
        mSwipRefresh.setRefreshing(false);
        isFirstLoad = false;
        List<RecyclerDataHolder> holders = new ArrayList<>();
        mShoppingViews.clear();
        for (int i = 0; i < data.size(); i++) {
            ShoppingDataHolder holder = new ShoppingDataHolder(data.get(i));
            holder.setViewType(i);
            mShoppingViews.add(holder);
            mShopRecyclerView.getRecycledViewPool().setMaxRecycledViews(i, 20);
            holders.add(holder);
        }
        mAdapter.setDataHolders(holders);
    }

    @Override
    public void addLoadData(List<ShoppingItemDetailsModel> data) {
    }

    @Override
    public void showGoodsNullView() {
        ViewUtils.changeViewVisible(title_other, false);
        //显示空订单UI
        ViewUtils.changeViewVisible(mNullLayout, true);
        //控制面板隐藏
        ViewUtils.changeViewVisible(fl_operator, false);
        //隐藏刷新按钮
        mSwipRefresh.setRefreshing(false);
        //清理适配器数据
        mAdapter.setDataHolders(null);
    }

    @Override
    public void hideGoodsNullView() {
        //控制面板显示
        ViewUtils.changeViewVisible(fl_operator, true);
        ViewUtils.changeViewVisible(mNullLayout, false);
        ViewUtils.changeVisible(title_other, true);
    }

    @Override
    public void changeCountPanel(boolean isShow) {
        ViewUtils.changeViewVisible(mRlCountLayout, isShow);
        ViewUtils.changeVisible(title_other, isShow);
    }

    @Override
    public void showEditView() {
        isEditStyle = true;
        ViewUtils.changeViewVisible(mRlEditLayout, true);
        ViewUtils.changeViewVisible(mRlCountLayout, false);
        title_other.setText("完成");
    }

    @Override
    public void hideEditView() {
        isEditStyle = false;
        ViewUtils.changeViewVisible(mRlEditLayout, false);
        ViewUtils.changeViewVisible(mRlCountLayout, true);
        title_other.setText(R.string.my_shopping_edit);
    }


    @Override
    public boolean isEditModel() {
        return isEditStyle;
    }

    @Override
    public void checkItemBoxState(int itemPosition, boolean checkState) {
        try {
            // ShopItemBar itemBar = mShoppingViews.get(itemPosition).getBar(getContext());
            ShopItemBar viewByPosition = (ShopItemBar) mLayoutManager.findViewByPosition(itemPosition);

            viewByPosition.updateCheckState(checkState);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void msgHint(String error) {
        if (!TextUtils.isEmpty(error)) {
            if (error.equals(ErrorEvent.NETWORK_ERROR)) {
                SnackMaker.shortShow(title_name, R.string.network_error);
            } else if (error.equals(ErrorEvent.NO_MORE_ERROR)) {
                SnackMaker.shortShow(title_name, R.string.bottom_tip);
            } else {
                SnackMaker.shortShow(title_name, error);
            }
        }
        mSwipRefresh.setRefreshing(false);
    }

    @Override
    public void updateTotalFree(String free, String goodsCount) {
        if (TextUtils.isEmpty(free)) {
            tv_free_all_orders.setText(getString(R.string.price, "0"));
        } else {
            tv_free_all_orders.setText(getString(R.string.price, free));
        }
        //商品数目
        if (TextUtils.isEmpty(goodsCount)) {
            btn_count_orders.setText(getString(R.string.my_shopping_count, "0"));
        } else {
            btn_count_orders.setText(getString(R.string.my_shopping_count, goodsCount));
        }
        mGoodsCount = goodsCount;
    }

    @Override
    public void updateItemFree(int itemPosition, String free, String postal, String revenue) {

        ShopItemBar viewByPosition = (ShopItemBar) mLayoutManager.findViewByPosition(itemPosition);
        //更新选中的费用
        viewByPosition.updateFree(free, postal, revenue);
    }

    @Override
    public void updateTotalBoxState(boolean checkState) {
        check_all_orders.setChecked(checkState);
        edit_check_all_orders.setChecked(checkState);
    }

    @Override
    public void showLoadingView() {
        rl_goods_content.showLoadingView();
//        showLoadingDialog();
    }

    @Override
    public void hideLoadingView() {
        rl_goods_content.closeLoadingView();
//        hideLoadingDialog();
    }

    @Override
    public void showReLoadView() {
        rl_goods_content.showNetWorkErrorView();
    }

    @Override
    public boolean isFirstLoading() {
        return isFirstLoad;
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    public void onEvent(ReLoadEvent event) {
        mPresenter.onStart();
    }

    public void onEvent(ShoppingEvent event) {
        int position = event.adapter_postion;
        switch (event.action) {
            case EventType.Shopping_Event_item_child_add_goods:
                mPresenter.addOneGoods(position, event.obj);
                break;

            case EventType.Shopping_Event_item_child_reduce_goods:
                mPresenter.reduceOneGoods(position, event.obj);
                break;

            case EventType.Shopping_Event_item_child_select_all:
                mPresenter.selectChildGoods(position, event.obj);
                break;

            case EventType.Shopping_Event_item_child_un_select_all:
                mPresenter.unSelectChildGoods(position, event.obj);
                break;

            case EventType.Shopping_Event_item_select_all:
                mPresenter.selectAllChildGoods(position, event.obj);
                break;

            case EventType.Shopping_Event_item_un_select_all:
                mPresenter.unSelectAllChildGoods(position, event.obj);
                break;
            case EventType.Shopping_Event_Refresh:
                mPresenter.setNeedRefresh();
                break;
        }
    }

    @Override
    public void onScrollBottom() {
        // mPresenter.refreshBottom();
    }
}
