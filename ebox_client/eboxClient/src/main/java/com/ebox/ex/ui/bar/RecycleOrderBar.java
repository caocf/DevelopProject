package com.ebox.ex.ui.bar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.R;
import com.ebox.ex.database.LocalOrder.OrderLocalInfo;
import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.base.type.OrderInfo;
import com.ebox.ex.network.model.enums.PickupType;
import com.ebox.ex.network.request.RequestReSendSms;
import com.ebox.ex.ui.PickupResultActivity;
import com.ebox.ex.ui.adapter.LocalQueryRecycleAdapter;
import com.ebox.ex.ui.fragment.ResultPickupFragment;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.Tip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2015/10/28.
 */
public class RecycleOrderBar extends RelativeLayout implements LocalQueryRecycleAdapter.LocalQueryListener {

    private RecyclerView recyclerView;
    private LocalQueryRecycleAdapter adapter;
    private Context context;
    private TextView tv_hint;
    public boolean mHideHead;
    private DialogUtil dialogUtil;
    private Tip tip;
    private LinearLayout table_head;

    public RecycleOrderBar(Context context) {
        super(context);
        initViews(context);
    }

    public RecycleOrderBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public RecycleOrderBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
    }

    private void initViews(Context context) {
        this.context = context;
    }

    public void initRecycler(boolean isRecycle) {
        this.removeAllViews();
        mHideHead=true;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (!isRecycle) {
            inflater.inflate(R.layout.ex_bar_recycle_order_1, this, true);
        } else {
            inflater.inflate(R.layout.ex_bar_recycle_order, this, true);
        }
        table_head = (LinearLayout) findViewById(R.id.table_head);
        tv_hint = (TextView) findViewById(R.id.tv_hint);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager manager = new LinearLayoutManager(context);

        recyclerView.setLayoutManager(manager);

        recyclerView.setHasFixedSize(true);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new LocalQueryRecycleAdapter(null, this, isRecycle);
        recyclerView.setAdapter(adapter);

        dialogUtil = new DialogUtil();
        dialogUtil.createProgressDialog((Activity) context);
    }

    public void setScrollListener(RecyclerView.OnScrollListener listener){
        recyclerView.addOnScrollListener(listener);
    }

    public void onResume() {
        if (isRecycleFinish())
        {
            setNullShow(true);
        } else {
            setNullShow(false);
        }
    }

    public void clearData(){
        adapter.setData(null);
    }

    public void setDatas(List<OrderLocalInfo> infos) {
        adapter.setData(infos);
    }

    public void addDatas(List<OrderInfo> infos) {

        ArrayList<OrderLocalInfo> datas = new ArrayList<OrderLocalInfo>();

        for (OrderInfo order : infos)
        {
            OrderLocalInfo localInfo=new OrderLocalInfo();
            localInfo.setOrder_id(order.getOrder_id());
            localInfo.setBox_code(order.getBox().getCode());
            localInfo.setCustomer_telephone(order.getMsisdn());
            localInfo.setOperator_telephone(order.getOperator_username());
            localInfo.setDelivery_at(order.getDelivery_at());
            localInfo.setPick_time(order.getFetch_at());
            localInfo.setPick_id(order.getPickup_id());
            localInfo.setItem_id(order.getItem_id());
            localInfo.setPassword(order.getPassword());
            //服务端的订单状态
            localInfo.setOrder_state(order.getState());
            datas.add(localInfo);
        }

        adapter.addData(datas);
    }

    public void setNullShow(boolean show) {
        if (show)
        {
            if (tv_hint.getVisibility() == INVISIBLE)
            {
                tv_hint.setVisibility(VISIBLE);
                if (mHideHead) {
                    table_head.setVisibility(GONE);
                }
            }
        } else {
            if (tv_hint.getVisibility() == VISIBLE)
            {
                tv_hint.setVisibility(INVISIBLE);
                if (mHideHead) {
                    table_head.setVisibility(VISIBLE);
                }
            }
        }
    }

    public boolean isRecycleFinish() {
        return adapter.getItemCount() == 0 ? true : false;
    }

    @Override
    public void sendSms(OrderLocalInfo info) {

        RequestReSendSms requestReSendSms = new RequestReSendSms(info.getOrder_id(), new ResponseEventHandler<BaseRsp>() {
            @Override
            public void onResponseSuccess(BaseRsp result) {
                dialogUtil.closeProgressDialog();
                if (result.isSuccess()) {
                    tip = new Tip(context, getResources().getString(R.string.ex_send_sucess), null);
                    tip.show(0);
                } else {
                    tip = new Tip(context, result.getMsg(), null);
                    tip.show(0);
                }
            }

            @Override
            public void onResponseError(VolleyError error) {
                dialogUtil.closeProgressDialog();
                tip = new Tip(context, getResources().getString(R.string.pub_connect_failed), null);
                tip.show(0);
            }
        });
        RequestManager.addRequest(requestReSendSms, null);
    }

    @Override
    public void sendPhone(OrderLocalInfo info) {
        //语音验证码
    }

    @Override
    public void recycle(final OrderLocalInfo info, int position) {
        adapter.removeItemData(position);
        adapter.notifyItemRemoved(position);

        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, PickupResultActivity.class);

                intent.putExtra("order", info);

                intent.putExtra(ResultPickupFragment.PICK_TYPE, PickupType.operator);

                context.startActivity(intent);
            }
        }, 400);

    }

}
