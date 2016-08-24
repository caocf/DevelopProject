package com.xhl.world.ui.activity.bar;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.serviceOrder.Order;
import com.xhl.world.model.serviceOrder.OrderDetail;
import com.xhl.world.ui.event.OrderManagerEvent;
import com.xhl.world.ui.view.LifeCycleImageView;
import com.xhl.world.ui.view.pub.BaseBar;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 16/1/5.
 */
public class OrderManagerItemChildBar extends BaseBar {

    @ViewInject(R.id.iv_item_order_icon)
    private LifeCycleImageView iv_item_order_icon;

    @ViewInject(R.id.tv_item_order_name)
    private TextView tv_item_order_name;

    @ViewInject(R.id.tv_item_order_price)
    private TextView tv_item_order_price;

    @ViewInject(R.id.tv_item_order_num)
    private TextView tv_item_order_num;

    @ViewInject(R.id.iv_right)
    private ImageView iv_right;

    @ViewInject(R.id.view_line)
    private View view_line;

    @ViewInject(R.id.rl_details)//点击商品详情
    private View rl_details;

    @ViewInject(R.id.vs_product_action)
    private ViewStub vs_product_action;
    public boolean isNeedHideLine = true;
    private LinearLayout ll_item_action;//单个商品操作
    private Button button_1;//申请退货
    private Button button_2;//去评价

    private Order mOrder;//商品关联的订单
    private OrderDetail mProduct;//单个商品信息
    private int mViewTag;

    public OrderManagerItemChildBar(Context context) {
        super(context);
    }

    public OrderManagerItemChildBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initParams() {
    }

    public void setChildOrderInfo(OrderDetail childModel) {
        mProduct = childModel;
        iv_item_order_icon.bindImageUrl(childModel.getProductPic());
        //单一价格
        String price = childModel.getUnitPrice();
        tv_item_order_price.setText(getResources().getString(R.string.price, price));
        tv_item_order_name.setText(childModel.getProductName());
        String num = childModel.getNum();
        if (TextUtils.isEmpty(num)) {
            tv_item_order_num.setVisibility(INVISIBLE);
        } else {
            tv_item_order_num.setText(getResources().getString(R.string.num, childModel.getNum()));
        }
    }

    /**
     * 订单在确认收货后显示单个商品的操作按钮
     */
    public void completeActionShow() {
        vs_product_action.inflate();
        button_1 = (Button) findViewById(R.id.button_1);
        button_1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionApply();
            }
        });
        button_2 = (Button) findViewById(R.id.button_2);
        button_2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionGoJudge();
            }
        });
      /*  rl_details.setClickable(true);
        rl_details.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionGoSingleDetails();
            }
        });*/

        //检测状态
        checkReturnStatus(mProduct);

        AutoUtils.autoSize(this);
    }

    private void actionGoSingleDetails() {
        int type = OrderManagerEvent.ORDER_ACTION_SINGLE_DETAILS;
        postEvent(type);
    }

    private void actionGoJudge() {
        int type = OrderManagerEvent.ORDER_ACTION_pj;
        postEvent(type);
    }

    private void actionApply() {
        int type = OrderManagerEvent.ORDER_ACTION_th;
        postEvent(type);
    }

    private void postEvent(int type) {

        OrderManagerEvent event = new OrderManagerEvent();
        event.setOrder_action(type);
        event.setOrderDetail(mProduct);
        event.setOrder(mOrder);
        event.setView_tag(mViewTag);

        EventBus.getDefault().post(event);
    }

    //检测订单状态，已经申请退货的不可在申请,已经评价的不用在评价
    private void checkReturnStatus(OrderDetail child) {
        String status = child.getReturnStatus();//订单退货状态
        if (!TextUtils.isEmpty(status)) {
            if (!status.equals("0")) {//非申请退货状态
                button_1.setEnabled(false);
                button_1.setBackgroundResource(R.color.app_orange);
                button_1.setText(getReturnDes(child.getReturnStatus()));
                button_1.setTextColor(ContextCompat.getColor(getContext(), R.color.app_while));
            }
        }

        String evaluateStatus = child.getEvaluateStatus();
        //已经评价
        if (!TextUtils.isEmpty(evaluateStatus) && evaluateStatus.equals("1")) {
            button_2.setEnabled(false);
            button_2.setText("已评价");
            button_2.setBackgroundResource(R.color.app_orange);
            button_2.setTextColor(ContextCompat.getColor(getContext(), R.color.app_while));
        }
    }

    //生成对应的退货状态
    private String getReturnDes(String status) {
        String stDes = "";
        //（0：未申请  1：退货/等待卖家确认; 2:卖家不同意退货；3：货已收到等待退款； 5:同意退货等待退货 ；6：完成退货）
        if (status.equals("1")) {
            stDes = "退货申请中";
        } else if (stDes.equals("2")) {
            stDes = "卖家已拒绝";
        } else if (stDes.equals("6")) {
            stDes = "退货完成";
        } else {
            stDes = "退货处理中";
        }
        return stDes;
    }

    public void setArrowShow(boolean show) {
        if (!show) {
            iv_right.setVisibility(INVISIBLE);
        } else {
            iv_right.setVisibility(VISIBLE);
        }
    }

    public void hideLine() {
        if (isNeedHideLine) {
            view_line.setVisibility(INVISIBLE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_order_manager_child;
    }

    public void setViewTag(int mViewTag) {
        this.mViewTag = mViewTag;
    }

    public void setOrder(Order mOrder) {
        this.mOrder = mOrder;
    }
}
