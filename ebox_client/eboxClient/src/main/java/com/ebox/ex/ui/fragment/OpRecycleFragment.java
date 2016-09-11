package com.ebox.ex.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.database.LocalOrder.OrderLocalInfo;
import com.ebox.ex.database.LocalOrder.OrderLocalInfoOp;
import com.ebox.ex.ui.bar.RecycleOrderBar;
import com.ebox.ex.ui.base.BaseOpFragment;
import com.ebox.ex.utils.OperatorHelper;
import com.ebox.pub.utils.Tip;

import java.util.List;

/**
 * Created by Android on 2015/10/22.
 */
public class OpRecycleFragment extends BaseOpFragment implements View.OnClickListener {

    private RecycleOrderBar recycle_order;

    private List<OrderLocalInfo> mCacheOrders;

    private RelativeLayout rl_header;

    private Button rb_no_time_out;

    private TextView tv_hint;

    private String phone;
    private int curPage;
    private Tip tip;

    private boolean isExistTimeoutOrder;

    public static BaseOpFragment newInstance() {
        BaseOpFragment fragment = new OpRecycleFragment();
        Log.i(TAG, String.format("newInstance %s", "OpRecycleFragment"));
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phone = operatorPhone();
        isExistTimeoutOrder = OperatorHelper.checkTimeOut(phone);
    }

    @Override
    protected int getViewId() {
        return R.layout.ex_fragment_op_timeout;
    }

    @Override
    protected void initView(View view) {
        rb_no_time_out = (Button) view.findViewById(R.id.rb_no_time_out);
        rb_no_time_out.setOnClickListener(this);
        rl_header = (RelativeLayout) view.findViewById(R.id.rl_header);
        tv_hint = (TextView) view.findViewById(R.id.tv_hint);

        recycle_order = (RecycleOrderBar) view.findViewById(R.id.recycle_order);
        recycle_order.initRecycler(true);
        recycle_order.mHideHead=false;
        if (!isExistTimeoutOrder) {
            changePage(0);
        } else {
            changePage(1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        recycle_order.onResume();
        if (isExistTimeoutOrder) {
            if (tv_hint.getVisibility() != View.VISIBLE) {
                tv_hint.setVisibility(View.VISIBLE);
            }
        } else {
            if (tv_hint.getVisibility() == View.VISIBLE) {
                tv_hint.setVisibility(View.GONE);
            }
        }
    }

    private void changePage(int page) {
        curPage=page;
        if (mCacheOrders != null) {
            mCacheOrders.clear();
        }
        if (page == 0)
        {
            //所有未超期件
            mCacheOrders = OrderLocalInfoOp.getAllUnTimeOutOrderByOperator(operatorPhone());
            tv_hint.setVisibility(View.INVISIBLE);
            rb_no_time_out.setText("查看已超期");
        } else
        {
            //查询所有超期件
            mCacheOrders = OrderLocalInfoOp.getAllTimeOutOrderByOperator(operatorPhone());
            rb_no_time_out.setText("查看未超期");
            if (mCacheOrders.size() > 0)
            {
                tv_hint.setVisibility(View.VISIBLE);
                isExistTimeoutOrder=true;
            }
        }
        recycle_order.setDatas(mCacheOrders);
        recycle_order.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_no_time_out:

                if (isExistTimeoutOrder && !recycle_order.isRecycleFinish()) {
                    tip = new Tip(context, "请先处理超时件", null);
                    tip.show(0);
                    return;
                }
                if (curPage==0) {
                    changePage(1);
                }else {
                    changePage(0);
                }
                isExistTimeoutOrder = false;
                //清理
                OperatorHelper.mHasTimeoutOrder = false;
                break;
        }
    }
}
