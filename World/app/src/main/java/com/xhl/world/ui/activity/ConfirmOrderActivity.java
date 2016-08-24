package com.xhl.world.ui.activity;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.AddressModel;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.Coupon;
import com.xhl.world.model.InitOrderModel;
import com.xhl.world.model.SaveOrderModel;
import com.xhl.world.model.serviceOrder.Order;
import com.xhl.world.model.serviceOrder.OrderDetail;
import com.xhl.world.ui.activity.bar.OrderItemBar;
import com.xhl.world.ui.event.AddressEvent;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.fragment.AddressMyFragment;
import com.xhl.world.ui.fragment.OrderInitCouponFragment;
import com.xhl.world.ui.fragment.OrderInitScoreFragment;
import com.xhl.world.ui.utils.DialogMaker;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.world.ui.view.myLoadRelativeLayout;
import com.xhl.world.ui.view.pay.PayHelper;
import com.xhl.xhl_library.ui.view.RippleView;
import com.xhl.xhl_library.utils.NumberUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Sum on 15/12/19.
 */

@ContentView(R.layout.activity_confirm_order)
public class ConfirmOrderActivity extends BaseAppActivity implements OrderInitCouponFragment.selectCouponListener, OrderInitScoreFragment.selectScoreListener {

    @ViewInject(R.id.title_name)
    private TextView title_name;

    @ViewInject(R.id.ll_content)
    private LinearLayout ll_content;//订单根布局

    @ViewInject(R.id.rl_load_layout)
    private myLoadRelativeLayout rl_load_layout;

    @ViewInject(R.id.ll_order_details)
    private LinearLayout ll_order_details;// 动态生成订单

    @ViewInject(R.id.tv_location_people)
    private TextView tv_location_people;//收件人
    @ViewInject(R.id.tv_location_phone)
    private TextView tv_location_phone;//收件人手机号
    @ViewInject(R.id.tv_location_details)
    private TextView tv_location_details;//收件人地址
    @ViewInject(R.id.vs_location_null_show)//空地址显示
    private ViewStub vs_location_null_show;

    @ViewInject(R.id.tv_order_pay_type)
    private TextView tv_order_pay_type;//支付方式提示

    @ViewInject(R.id.tv_order_coupon)
    private TextView tv_order_coupon;//优惠券使用提示

    @ViewInject(R.id.tv_order_jf)
    private TextView tv_order_jf;//积分使用提示

    @ViewInject(R.id.tv_price_order_postal)
    private TextView tv_price_order_postal;//快递方式
    @ViewInject(R.id.tv_price_order)
    private TextView tv_price_order;//商品价格
    @ViewInject(R.id.tv_price_order_revenue)
    private TextView tv_price_order_revenue;//关税价格
    @ViewInject(R.id.tv_price_total)
    private TextView tv_price_total;//应付总额

    @ViewInject(R.id.check_separate_price)
    private CheckBox check_separate_price;//分单选项
    @ViewInject(R.id.tv_separate_price)
    private TextView tv_separate_price;//分单价格

    @ViewInject(R.id.check_un_separate_price)
    private CheckBox check_un_separate_price;//未分单选择
    @ViewInject(R.id.tv_un_separate_price)
    private TextView tv_un_separate_price;//未分单价格
    @ViewInject(R.id.ripple_confirm_orders)
    private View mView;
    private View ll_null_location;//空收货地址显示
    private String mOrderPar;
    private InitOrderModel mInitOrders;
    private boolean mIsSeparate = false;//是否分单
    private boolean mOrderInitFinish = false;
    private Integer mUserScore = 0;//积分个数，需要通过转换比计算
    private Integer mCouponMoney = 0;//优惠券金额
    private Float mTaxMoney = 0f;//关税金额
    private String mAddressId;
    private String mPayMoney;//支付金额
    private String mSplitOrNot;//分单
    private String mAttrJsonStr;//订单数据
    private String mIntegral = "0";//积分
    private String mCouponId;//优惠券id

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        finish();
    }

    @Event(value = R.id.ripple_select_address, type = RippleView.OnRippleCompleteListener.class)
    private void onselectAddressClick(View view) {
        //添加选择地址操作
        pushFragmentToBackStack(AddressMyFragment.class, null);
    }

    @Event(value = R.id.ripple_confirm_orders, type = RippleView.OnRippleCompleteListener.class)
    private void onConfirmClick(View view) {//提交订单
        confirmOrder();
    }

    //支付方式
    @Event(value = R.id.ripple_select_pay_type, type = RippleView.OnRippleCompleteListener.class)
    private void onSelectPayTypeClick(View view) {

       /* AlertDialog.Builder dialog = DialogMaker.getDialog(this);
        dialog.setTitle(R.string.pay_type);
        dialog.setSingleChoiceItems(R.array.pay_array, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        tv_order_pay_type.setText("支付宝支付");
                        break;
                    case 1:
                        tv_order_pay_type.setText("微信支付");
                        break;
                }
            }
        });
        dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();*/
    }

    //选择优惠券
    @Event(value = R.id.ripple_select_coupon, type = RippleView.OnRippleCompleteListener.class)
    private void onSelectCouponClick(View view) {

        pushFragmentToBackStack(OrderInitCouponFragment.class, mInitOrders.getCouponList());

    }

    //选择积分
    @Event(value = R.id.ripple_select_jf, type = RippleView.OnRippleCompleteListener.class)
    private void onSelectJfClick(View view) {

        HashMap<String, String> data = new HashMap<>();
        //积分上限
        data.put("limit", mInitOrders.getUpperLimit().toString());
        //账户积分
        data.put("score", mInitOrders.getIntegral().toString());
        //兑换比例
        data.put("proportion", mInitOrders.getProportion().toString());

        pushFragmentToBackStack(OrderInitScoreFragment.class, data);

    }

    @Override
    protected void onReloadClick() {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //创建订单条件
        mOrderPar = getIntent().getExtras().getString("orderInfo");
        //初始化订单
        initOrder();
    }

    private void initOrder() {

        if (TextUtils.isEmpty(mOrderPar)) {
            SnackMaker.shortShow(mView, "订单初始化异常");
            finish();
            return;
        }

        if (mOrderInitFinish) {
            return;
        }
        mOrderInitFinish = true;
        showProgressLoading("订单初始化中,请稍等...");
        ApiControl.getApi().orderInit(mOrderPar, null, null, new Callback.CommonCallback<ResponseModel<InitOrderModel>>() {
            @Override
            public void onSuccess(ResponseModel<InitOrderModel> result) {
                if (result.isSuccess()) {
                    //获取分单是数据
                    mInitOrders = result.getResultObj();
                    showOrderData();
                } else {
                    SnackMaker.shortShow(mView, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(mView, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (mInitOrders == null) {
                    mOrderInitFinish = false;
                }
                hideLoadingDialog();
            }
        });
    }

    private void showOrderData() {
        if (mInitOrders == null) {
            return;
        }
        //全部布局可见
        ll_content.setVisibility(View.VISIBLE);

        showLocation(mInitOrders.getAddressList());

        showJfSate();//积分控制

        couponShow();//优惠券显示

        //创建订单显示数据
        tv_separate_price.setText(getString(R.string.price, mInitOrders.getAllMoney()));//分单价格
        tv_un_separate_price.setText(getString(R.string.price, mInitOrders.getAllMoney2()));//未分单价格

        chooseType(1);
        //选择分单
        chooseType(0);
    }

    private void changeStyle(int orderType) {
        if (orderType == 0) {
            tv_separate_price.setTypeface(Typeface.DEFAULT_BOLD);
            tv_un_separate_price.setTypeface(Typeface.DEFAULT);
        } else {
            tv_separate_price.setTypeface(Typeface.DEFAULT);
            tv_un_separate_price.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    //显示地址数据
    private void showLocation(AddressModel address) {
        if (address == null) {
            vs_location_null_show.inflate();//初始化空地址界面
            mAddressId = "";
            ll_null_location = findViewById(R.id.ll_null_location);
            ll_null_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //添加选择地址操作
                    pushFragmentToBackStack(AddressMyFragment.class, null);
                }
            });
        } else {
            tv_location_people.setText(getString(R.string.address_user, address.getConsigneeName()));
            tv_location_phone.setText(address.getTelephone());
            tv_location_details.setText(address.getArea() + " " + address.getAddress());
            mAddressId = address.getId();
        }
    }


    //计算全部关税
    private void countRevenue(List<Order> orders) {
        float tax = 0;
        for (Order order : orders) {
            List<OrderDetail> orderDetailList = order.getOrderDetailList();
            for (OrderDetail od : orderDetailList) {
                String taxPrice = od.getTaxprice();//关税
                if (!TextUtils.isEmpty(taxPrice)) {
                    float f = Float.parseFloat(taxPrice);
                    tax += f;
                }
            }
        }
        mTaxMoney = tax;
    }

    //更新当前界面展示费用
    private String updateShowMoney(int orderType) {

        //商品总价格（分单未分单都是同一个商品价格）
        String orderPrice = mInitOrders.getAllMoney();//商品价格
        tv_price_order.setText(getString(R.string.price, orderPrice));

        //关税价格
        tv_price_order_revenue.setText(getString(R.string.price, NumberUtil.getDouble(mTaxMoney)));

        //应付金额(商品价格+关税-积分-优惠券)
        String curPrice;
        if (orderType == 0) {
            curPrice = mInitOrders.getAllMoney();
        } else {
            //包含了税费用
            curPrice = mInitOrders.getAllMoney2();
        }

        Float curAllPrice = Float.valueOf(curPrice);
        //计算积分抵扣金额
        Float proportion = mInitOrders.getProportion();
        Float scoreMoney = mUserScore * proportion;

        //计算当前价格
        Float allPrice = curAllPrice - scoreMoney - mCouponMoney;
        String money = NumberUtil.getDouble(allPrice);

        if (orderType == 0) {
            tv_separate_price.setText(getString(R.string.price, money));
        } else if (orderType == 1) {
            tv_un_separate_price.setText(getString(R.string.price, money));
        }
        tv_price_total.setText(getString(R.string.price, money));

        return money;
    }

    //选择分单类型
    private void chooseType(int orderType) {

        if (orderType == 0) {//分单
            mIsSeparate = true;
            check_separate_price.setChecked(true);
            check_un_separate_price.setChecked(false);
            //计算关税
            countRevenue(mInitOrders.getOrderList());
            separateOrder();
        } else if (orderType == 1) {//未分单
            mIsSeparate = false;
            check_separate_price.setChecked(false);
            check_un_separate_price.setChecked(true);
            //设置关税
            countRevenue(mInitOrders.getOrderList2());
            unSeparateOrder();
        }
        //更新价格显示
        updateShowMoney(orderType);
        //更新字体样式
        changeStyle(orderType);
    }

    //未分单的订单
    private void unSeparateOrder() {
        List<Order> orderList = mInitOrders.getOrderList2();
        addOneOrderChildView(orderList);
    }

    //分单的订单
    private void separateOrder() {
        List<Order> orderList = mInitOrders.getOrderList();
        addOneOrderChildView(orderList);
    }

    private void addOneOrderChildView(List<Order> orders) {

        ll_order_details.removeAllViews();

        for (Order child : orders) {
            OrderItemBar orderItem = new OrderItemBar(this);
            orderItem.setData(child);
            int margin = getResources().getDimensionPixelOffset(R.dimen.px_dimen_20);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
            params.topMargin = margin;

            orderItem.setLayoutParams(params);

            ll_order_details.addView(orderItem);
        }
    }

    @Override
    protected void initParams() {
        title_name.setText(R.string.confirm_order);

        ll_content.setVisibility(View.INVISIBLE);

        check_separate_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseType(0);
            }
        });

        check_un_separate_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseType(1);
            }
        });
    }

    //优惠券使用
    private void couponShow() {
        List<Coupon> coupons = mInitOrders.getCouponList();
        int size = coupons.size();
        if (size == 0) {
            tv_order_coupon.setText("无可用");
        } else {
            tv_order_coupon.setText(getCoupon(size));
        }
    }

    //积分使用
    private void showJfSate() {
        //订单使用积分上限
        Integer upperLimit = mInitOrders.getUpperLimit();
        //当前可用积分
        Integer integral = mInitOrders.getIntegral();

        Integer score = 0;
        if (null == upperLimit || upperLimit == 0) {
            tv_order_jf.setText("不可用");
        } else {
            //账号积分大于上限,最多可用integral积分
            if (integral != null && integral > upperLimit) {
                score = upperLimit;
            } else if (integral != null) {
                score = integral;
            }
            mUserScore = score;
            //设置积分抵扣金额
            tv_order_jf.setText(getScoreMoney(score));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    //选择优惠券之前展示可用数目
    private Spanned getCoupon(int num) {
        StringBuilder sb = new StringBuilder();
        sb.append("<font color=\"#e51c23\">").append(num).append("</font>");
        sb.append("<font color=\"#666666\">").append("张可用").append("</font>");
        return Html.fromHtml(sb.toString());
    }

    //选择优惠券后展示抵扣金额
    private Spanned getUseCoupon(int amount) {
        StringBuilder sb = new StringBuilder();
        sb.append("<font color=\"#666666\">").append("抵扣金额:").append("</font>");
        sb.append("<font color=\"#e51c23\">").append("￥").append(amount).append("</font>");
        return Html.fromHtml(sb.toString());
    }

    //计算积分对应的金额
    private Spanned getScoreMoney(float score) {
        //积分兑换比
        Float proportion = mInitOrders.getProportion();
        //计算抵扣金额
        Float money = score * proportion;
        //拼接字符串
        StringBuilder sb = new StringBuilder();
        sb.append("<font color=\"#666666\">").append("金额:").append("</font>");
        sb.append("<font color=\"#e51c23\">").append("￥").append(NumberUtil.getDouble(money)).append("</font>");

        return Html.fromHtml(sb.toString());
    }

    //地址选择
    public void onEvent(AddressEvent event) {
        if (event.actionType == AddressEvent.Type_select_address) {
            //显示选中地址
            showLocation(event.model);
            if (ll_null_location != null) {
                ll_null_location.setVisibility(View.GONE);
            }
        }
    }

    //创建生成一组订单数据
    private JSONArray saveOrderToJson(List<Order> orders) {
        JSONArray ordersJson = new JSONArray();
        for (Order order : orders) {
            JSONObject oneOrder = createOneOrder(order);
            ordersJson.put(oneOrder);
        }
        return ordersJson;
    }

    private String getAttr(String attr) {
        if (TextUtils.isEmpty(attr)) {
            return "";
        }
        return attr;
    }

    //创建一个订单数据
    private JSONObject createOneOrder(Order order) {
        JSONObject orderJson = new JSONObject();
        try {
            orderJson.put("companyId", getAttr(order.getCompanyId()));
            orderJson.put("couponsMoney", getAttr(order.getCouponsMoney()));//优惠金额
            orderJson.put("payType", getAttr(order.getPageType()));//1 在线支付
            orderJson.put("liableMail", getAttr(order.getLiableMail()));//责任人邮箱
            orderJson.put("orderType", getAttr(order.getOrderType()));//1：普通订单；2：团购订单；3：消费者促销订单；4：渠道促销订单；5订货会订单；6：积分订单）
            orderJson.put("couId", getAttr(order.getCouId()));//优惠券主题id
            orderJson.put("productType", getAttr(order.getProductType() + ""));
            orderJson.put("orderProperty", getAttr(order.getOrderProperty()));

            //创建订单商品数据
            JSONArray jsonArray = new JSONArray();
            List<OrderDetail> details = order.getOrderDetailList();
            for (OrderDetail de : details) {
                JSONObject oneProduct = createOneProduct(de);
                jsonArray.put(oneProduct);
            }
            orderJson.put("attrOrderDetail", jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderJson;
    }

    //创建一个商品数据
    private JSONObject createOneProduct(OrderDetail order) {
        JSONObject itemJson = new JSONObject();
        try {
            String shoppingCartId = order.getShoppingCartId();
            itemJson.put("shoppingCartId", shoppingCartId);
            String goodId = order.getGoodId();
            itemJson.put("goodId", goodId);
            String productName = order.getProductName();
            itemJson.put("productName", productName);
            itemJson.put("num", order.getNum());
            itemJson.put("taxprice", order.getTaxprice());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return itemJson;
    }

    //检测保存订单前的数量
    private void checkNumBeforeSaveOrder(String attrJsonStr) {

        ApiControl.getApi().verifyOrder(attrJsonStr, new Callback.CommonCallback<ResponseModel<HashMap<String, String>>>() {
            @Override
            public void onSuccess(ResponseModel<HashMap<String, String>> result) {
                if (result.isSuccess()) {
                    //1 正常
                    String state = result.getResultObj().get("result");
                    if (state.equals("1")) {
                        confirmCheckOrder();
                    } else {
                        hideLoadingDialog();
                        String msg = result.getResultObj().get("message");
                        SnackMaker.shortShow(mView, msg);
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(mView, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void confirmOrder() {

        if (TextUtils.isEmpty(mAddressId)) {
            SnackMaker.longShow(mView, "地址信息不能为空");
            return;
        }

        showProgressLoading("提交订单中,请稍后...");

        List<Order> separateOrder;

        if (mIsSeparate) {//分单
            separateOrder = mInitOrders.getOrderList();
            mSplitOrNot = "1";
            mPayMoney = mInitOrders.getAllMoney();
        } else {//未分单
            mSplitOrNot = "2";
            separateOrder = mInitOrders.getOrderList2();
            mPayMoney = mInitOrders.getAllMoney2();
        }

        //支付积分
        mIntegral = mUserScore.toString();

        //订单数据
        mAttrJsonStr = saveOrderToJson(separateOrder).toString();

        //检测库存
        checkNumBeforeSaveOrder(mAttrJsonStr);

    }

    //提交创建订单
    private void confirmCheckOrder() {
        ApiControl.getApi().orderSave(mAttrJsonStr, mPayMoney, mIntegral, mSplitOrNot, mAddressId, mCouponId, new Callback.CommonCallback<ResponseModel<SaveOrderModel>>() {
            @Override
            public void onSuccess(ResponseModel<SaveOrderModel> result) {
                if (result.isSuccess()) {
                    SaveOrderModel resultObj = result.getResultObj();
                    if (resultObj.createOrderSuccess()) {
                        createPayOrder(resultObj.getOrderCode(), resultObj.getBlanketOrder().getOrderPayPrice().toString());
                        //通知购物车刷新
                        EventBusHelper.posRefreshShopCarEvent();
                    } else {
                        SnackMaker.shortShow(mView, "保存订单异常");
                    }
                } else {
                    SnackMaker.shortShow(mView, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(mView, R.string.network_error);
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

    //创建支付订单
    private void createPayOrder(String orderCode, String orderPrice) {

        PayHelper payHelper = new PayHelper(this, new PayHelper.PayCallBack() {
            @Override
            public void success() {
                SnackMaker.shortShow(mView, R.string.pay_order_success);
                finish();
            }

            @Override
            public void failed(String msg) {
                SnackMaker.shortShow(mView, msg);
                finish();
            }
        });
        payHelper.payOrder(orderCode, orderPrice);
    }

    @Override
    public void selectCoupon(Coupon coupon) {
        //选取优惠券
        if (coupon != null) {
            mCouponId = coupon.getId();
            tv_order_coupon.setText(getUseCoupon(coupon.getAmount()));
            mCouponMoney = coupon.getAmount();
        } else {
            //取消优惠券
            mCouponId = "";
            int count = mInitOrders.getCouponList().size();
            if (count == 0) {
                tv_order_coupon.setText("无可用");
            } else
                tv_order_coupon.setText(getCoupon(mInitOrders.getCouponList().size()));
            mCouponMoney = 0;
        }
        updateShowMoney(0);
        updateShowMoney(1);
    }

    @Override
    public void selectScore(Integer score) {
        mUserScore = score;
        //设置积分抵扣金额
        tv_order_jf.setText(getScoreMoney(score));
        Float aFloat = Float.valueOf(updateShowMoney(0));
        if (aFloat < 0) {
            errorMoney(aFloat);
        } else {
            aFloat = Float.valueOf(updateShowMoney(1));
            if (aFloat < 0) {
                errorMoney(aFloat);
            }
        }
    }

    private void errorMoney(Float money) {
        AlertDialog.Builder dialog = DialogMaker.getDialog(this);
        dialog.setTitle("支付金额错误");
        dialog.setMessage("应付金额为" + money + ",重置积分数量为0");
        dialog.setPositiveButton("重置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectScore(0);
            }
        });
        dialog.setCancelable(false);
        dialog.create().show();
    }
}
