package com.xhl.bqlh.view.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.AppDelegate;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.GarbageModel;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.model.event.CommonEvent;
import com.xhl.bqlh.utils.SnackUtil;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.helper.FragmentContainerHelper;
import com.xhl.bqlh.view.ui.activity.CollectionActivity;
import com.xhl.bqlh.view.ui.activity.OrderQueryActivity;
import com.xhl.bqlh.view.ui.bar.MyInfoBar;
import com.xhl.bqlh.view.ui.recyclerHolder.MyFunItem;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.line.GirdViewItemDecoration;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/7/1.
 */
@ContentView(R.layout.fragment_home_my)
public class HomeMeFragment extends BaseAppFragment {

    @ViewInject(R.id.my_info_bar)
    private MyInfoBar my_info_bar;

    @ViewInject(R.id.recycler_view)
    private RecyclerView recycler_view;

    @ViewInject(R.id.tv_num_order_pay)
    private TextView tv_num_order_pay;//代付款

    @ViewInject(R.id.tv_num_order_take)
    private TextView tv_num_order_take;//代收货

    @ViewInject(R.id.tv_num_order_judge)
    private TextView tv_num_order_judge;//待评价

    @Event(R.id.order_all)
    private void orderAllClick(View view) {
        orderOpen(OrderQueryActivity.ORDER_ALL);
    }

    @Event(R.id.order_wait_for_pay)
    private void orderPayClick(View view) {
        orderOpen(OrderQueryActivity.ORDER_PAY);
    }

    @Event(R.id.order_wait_for_take)
    private void orderTakeClick(View view) {
        orderOpen(OrderQueryActivity.ORDER_TAKE);
    }

    @Event(R.id.order_wait_for_judge)
    private void orderJudgeClick(View view) {
        orderOpen(OrderQueryActivity.ORDER_JUDGE);
    }

    private void orderOpen(int type) {
        if (AppDelegate.appContext.isLogin(getContext())) {
            Intent intent = new Intent(getContext(), OrderQueryActivity.class);
            intent.putExtra(OrderQueryActivity.TAG_ORDER_TYPE, type);
            getContext().startActivity(intent);
        }
    }

    @Override
    protected boolean isNeedRegisterEventBus() {
        return true;
    }

    private RecyclerAdapter mFuncAdapter;

    @Override
    protected void initParams() {


        //功能数据
        GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
        recycler_view.setLayoutManager(manager);
        recycler_view.setHasFixedSize(true);
        List<RecyclerDataHolder> data = getFuncData();
        mFuncAdapter = new RecyclerAdapter(getContext(), data);
        recycler_view.addItemDecoration(new GirdViewItemDecoration(getContext()));
        recycler_view.setAdapter(mFuncAdapter);

        loadOrderNum();
    }

    private void loadOrderNum() {

        if (!AppDelegate.appContext.isLogin()) {
            return;
        }

        ApiControl.getApi().orderNum(new DefaultCallback<ResponseModel<GarbageModel>>() {
            @Override
            public void success(ResponseModel<GarbageModel> result) {
                GarbageModel resultObj = result.getObj();
                if (resultObj.getPayNum().equals("0")) {
                    tv_num_order_pay.setVisibility(View.GONE);
                } else {
                    tv_num_order_pay.setVisibility(View.VISIBLE);
                    tv_num_order_pay.setText(resultObj.getPayNum());
                }
                if (resultObj.getReceiveNum().equals("0")) {
                    tv_num_order_take.setVisibility(View.GONE);
                } else {
                    tv_num_order_take.setVisibility(View.VISIBLE);
                    tv_num_order_take.setText(resultObj.getReceiveNum());
                }
                if (resultObj.getEvaluateNum().equals("0")) {
                    tv_num_order_judge.setVisibility(View.GONE);
                } else {
                    tv_num_order_judge.setVisibility(View.VISIBLE);
                    tv_num_order_judge.setText(resultObj.getEvaluateNum());
                }
            }

            @Override
            public void finish() {
                mNeedRefreshOrderNum = false;
            }
        });
    }

    //function
    private List<RecyclerDataHolder> getFuncData() {
        List<RecyclerDataHolder> holders = new ArrayList<>();

        MyFunItem.SheetItem item = new MyFunItem.SheetItem();
        item.id = R.drawable.icon_my_fun_1;
        item.name = getString(R.string.my_fun_1);
        item.callback = buildFunc;
        holders.add(new MyFunItem(item));

        item = new MyFunItem.SheetItem();
        item.id = R.drawable.icon_my_fun_2;
        item.name = getString(R.string.my_fun_2);
        item.callback = func;
        item.tag = 3;
        holders.add(new MyFunItem(item));

        item = new MyFunItem.SheetItem();
        item.id = R.drawable.icon_my_fun_3;
        item.name = getString(R.string.my_fun_3);
        item.callback = buildFunc;
        holders.add(new MyFunItem(item));

        item = new MyFunItem.SheetItem();
        item.id = R.drawable.icon_my_fun_4;
        item.name = getString(R.string.my_fun_4);
        item.callback = buildFunc;
        holders.add(new MyFunItem(item));

        item = new MyFunItem.SheetItem();
        item.id = R.drawable.icon_my_fun_5;
        item.name = getString(R.string.my_fun_5);
        item.callback = buildFunc;
        holders.add(new MyFunItem(item));

        item = new MyFunItem.SheetItem();
        item.id = R.drawable.icon_my_fun_6;
        item.name = getString(R.string.my_fun_6);
        item.callback = buildFunc;
        holders.add(new MyFunItem(item));

        item = new MyFunItem.SheetItem();
        item.id = R.drawable.icon_my_fun_7;
        item.name = getString(R.string.my_fun_7);
        item.callback = buildFunc;
        holders.add(new MyFunItem(item));

        item = new MyFunItem.SheetItem();
        item.id = R.drawable.icon_my_fun_8;
        item.name = getString(R.string.my_fun_8);
        item.callback = func;
        item.tag = 2;
        holders.add(new MyFunItem(item));

        return holders;
    }

    private MyFunItem.SheetItemClickCallback func = new MyFunItem.SheetItemClickCallback() {
        @Override
        public void onClick(int tag) {
            if (tag == 2) {//设置
                FragmentContainerHelper.startFragment(getContext(), FragmentContainerHelper.fragment_setting);
            } else if (tag == 3) {//收藏夹
                if (AppDelegate.appContext.isLogin(getContext())) {
                    Intent intent = new Intent(getContext(), CollectionActivity.class);
                    intent.putExtra("data", 0);
                    startActivity(intent);
                }
            }
        }
    };

    private MyFunItem.SheetItemClickCallback buildFunc = new MyFunItem.SheetItemClickCallback() {
        @Override
        public void onClick(int tag) {
            SnackUtil.shortShow(mView, R.string.building);
        }
    };

    private boolean mNeedRefreshOrderNum = false;

    @Override
    public void onResume() {
        super.onResume();
        if (mNeedRefreshOrderNum) {
            loadOrderNum();
        }
        my_info_bar.refreshUserInfo();
    }


    @Override
    public void onEvent(CommonEvent event) {
        //刷新用户信息
        if (event.getEventTag() == CommonEvent.ET_RELOAD_USER_INFO) {
            my_info_bar.refreshUserInfo();
            my_info_bar.refreshUserCollection();
            loadOrderNum();
        } else if (event.getEventTag() == CommonEvent.ET_RELOAD_ORDER_NUM) {
            mNeedRefreshOrderNum = true;
        } else if (event.getEventTag() == CommonEvent.ET_RELOAD_COLLECTION_NUM) {//刷新收藏数量
            my_info_bar.refreshUserCollection();
        } else if (event.getEventTag() == CommonEvent.ET_RELOGIN) {//重新登录
            tv_num_order_pay.setVisibility(View.GONE);
            tv_num_order_take.setVisibility(View.GONE);
            tv_num_order_judge.setVisibility(View.GONE);
            my_info_bar.refreshUserCollection();
        }
    }
}
