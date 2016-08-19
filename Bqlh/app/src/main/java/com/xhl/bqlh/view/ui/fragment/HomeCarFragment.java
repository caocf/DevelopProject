package com.xhl.bqlh.view.ui.fragment;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xhl.bqlh.R;
import com.xhl.bqlh.doman.CarHelper;
import com.xhl.bqlh.model.event.CommonEvent;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.bqlh.view.helper.EventHelper;
import com.xhl.bqlh.view.helper.ViewHelper;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by Sum on 16/7/1.
 */
@ContentView(R.layout.fragment_home_car)
public class HomeCarFragment extends BaseAppFragment {

    private boolean mHasBack = false;

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout refreshLayout;

    @ViewInject(R.id.recycler_view)
    private RecyclerView recyclerView;

    @ViewInject(R.id.tv_right)
    private TextView tv_right;

    @ViewInject(R.id.rl_null_show)
    private View rl_null_show;//空购物车

    @ViewInject(R.id.ll_operator)
    private View ll_operator;//总面板

    @ViewInject(R.id.rl_op_edit)
    private View rl_op_edit;//编辑面板

    @ViewInject(R.id.rl_op_count)
    private View rl_op_count;//结算面板

    @ViewInject(R.id.check_all_orders)//全选操作
    private CheckBox check_all_orders;

    @ViewInject(R.id.tv_free_all_orders)
    private TextView tv_free_all_orders;//选择的订单金额

    @ViewInject(R.id.btn_count_orders)
    private Button btn_count_orders;

    @Event(R.id.check_all_orders)//全选
    private void onCheckOrderClick(View view) {
        mHelper.onSelectAll(check_all_orders.isChecked());
    }

    @Event(R.id.btn_count_orders)//结算
    private void onAccountClick(View view) {
        mHelper.onAccount();
    }

    @Event(R.id.btn_edit_delete)//删除
    private void onDeleteClick(View view) {
        mHelper.onDelete();
    }

    @Event(R.id.btn_go)//go
    private void onGoClick(View view) {
        if (!mHasBack) {
            EventHelper.postCommonEvent(CommonEvent.ET_MAIN);
        } else {
            getSumContext().popTopFragment(null);
        }
    }

    @Event(R.id.fl_btn_right)//编辑
    private void onEditClick(View view) {
        mIsEditStyle = !mIsEditStyle;
        updateOperator(mIsEditStyle);
    }

    private void updateOperator(boolean edit) {
        if (edit) {
            ViewHelper.setViewGone(rl_op_edit, false);
            ViewHelper.setViewGone(rl_op_count, true);
            tv_right.setText("完成");
        } else {
            ViewHelper.setViewGone(rl_op_edit, true);
            ViewHelper.setViewGone(rl_op_count, false);
            tv_right.setText("编辑");
        }
    }

    private boolean mIsEditStyle = false;

    private RecyclerAdapter mAdapter;//购物车数据

    private CarHelper mHelper;

    @Override
    protected boolean isNeedRegisterEventBus() {
        return true;
    }

    @Override
    protected void initParams() {
        super.initBackBar(getString(R.string.main_shopping), mHasBack);
        tv_right.setText("编辑");

        super.initRefreshStyle(refreshLayout, SwipeRefreshLayoutDirection.TOP);

        mHelper = new CarHelper(this, getContext());
        mHelper.onCreate();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 0;
                outRect.left = 0;
                outRect.right = 0;
                outRect.bottom = 20;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateOperator(false);
        mHelper.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHelper.onDestroy();
    }

    @Override
    public Object getValue(int type) {
        return super.getValue(type);
    }

    @Override
    public void showValue(int type, Object obj) {
        super.showValue(type, obj);
        //商品数据
        if (type == CarHelper.TYPE_RES_CAR_DATA) {
            if (obj == null) {
                ViewHelper.setViewGone(rl_null_show, false);
                ViewHelper.setViewGone(ll_operator, true);
            } else {
                List<RecyclerDataHolder> holders = (List<RecyclerDataHolder>) obj;
                if (holders.size() == 0) {
                    ViewHelper.setViewGone(rl_null_show, false);
                    ViewHelper.setViewGone(ll_operator, true);
                } else {
                    ViewHelper.setViewGone(ll_operator, false);
                    ViewHelper.setViewGone(rl_null_show, true);
                }
                mAdapter = new RecyclerAdapter(getContext(), holders);
                recyclerView.setAdapter(mAdapter);
            }
        }
        //刷新数据
        else if (type == CarHelper.TYPE_RES_CAR_DATA_REFRESH) {
            mAdapter.notifyDataSetChanged();
        }
        //总额
        else if (type == CarHelper.TYPE_RES_ORDER_ALL_MONEY) {
            String free = (String) obj;
            if (TextUtils.isEmpty(free)) {
                tv_free_all_orders.setText(getString(R.string.price, "0"));
            } else {
                tv_free_all_orders.setText(getString(R.string.price, free));
            }
        }
        //数量
        else if (type == CarHelper.TYPE_RES_ORDER_NUM) {
            String goodsCount = (String) obj;
            //商品数目
            if (TextUtils.isEmpty(goodsCount)) {
                btn_count_orders.setText(getString(R.string.my_shopping_count, "0"));
            } else {
                btn_count_orders.setText(getString(R.string.my_shopping_count, goodsCount));
            }
        } else if (type == CarHelper.TYPE_RES_NETWORK_ERROR_HIDE) {
            networkErrorHide();
        } else if (type == CarHelper.TYPE_RES_NETWORK_ERROR_SHOW) {
            networkErrorShow();
        } else if (type == CarHelper.TYPE_RES_SHOW_LOADING) {
            if (!refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(true);
            }
        } else if (type == CarHelper.TYPE_RES_HIED_LOADING) {
            if (refreshLayout.isRefreshing())
                refreshLayout.setRefreshing(false);
        } else if (type == CarHelper.TYPE_RES_SELECT_STATE) {
            boolean check = (boolean) obj;
            check_all_orders.setChecked(check);
        } else if (type == CarHelper.TYPE_RES_DATA_NULL) {
            boolean check = (boolean) obj;
            ViewHelper.setViewGone(rl_null_show, !check);
            ViewHelper.setViewGone(ll_operator, check);
        }
    }

    @Override
    public void onRefreshLoadData() {
        super.onRefreshLoadData();
        mHelper.onRefreshLoadData();
    }

    @Override
    public void onEvent(CommonEvent event) {
        super.onEvent(event);
        if (event.getEventTag() == CommonEvent.ET_RELOAD_CAR) {
            mHelper.reLoadCar();
        } else if (event.getEventTag() == CommonEvent.ET_RELOGIN) {
            mAdapter.setDataHolders(null);
            ViewHelper.setViewGone(rl_null_show, false);
        }else if (event.getEventTag() == CommonEvent.ET_RELOAD_CAR_MONEY) {//重新计算总额
            mHelper.countMoney();
        } else if (event.getEventTag() == CommonEvent.ET_RELOAD_USER_INFO) {
            mHelper.onRefreshLoadData();
        }
    }

    @Override
    public void onEnter(Object data) {
        if (data != null) {
            mHasBack = true;
        }
    }
}
