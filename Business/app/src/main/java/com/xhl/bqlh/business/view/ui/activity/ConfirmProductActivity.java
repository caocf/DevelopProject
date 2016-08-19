package com.xhl.bqlh.business.view.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xhl.bqlh.business.AppConfig.GlobalParams;
import com.xhl.bqlh.business.Model.App.ShopCarModel;
import com.xhl.bqlh.business.Model.GiftModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.doman.ConfirmOrderHelper;
import com.xhl.bqlh.business.utils.ToastUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.custom.ComplexText;
import com.xhl.bqlh.business.view.event.OrderStatisticsEvent;
import com.xhl.bqlh.business.view.helper.DialogMaker;
import com.xhl.bqlh.business.view.helper.EventHelper;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.helper.pub.Callback.RecycleViewCallBackWithData;
import com.xhl.bqlh.business.view.ui.recyclerHolder.ProductConfirmNewDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.utils.NumberUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/4/9.
 */
@ContentView(R.layout.activity_product_confirm)
public class ConfirmProductActivity extends BaseAppActivity implements RecycleViewCallBackWithData {

    @ViewInject(R.id.tv_select_type)
    private TextView mTvPayType;

    @ViewInject(R.id.tv_order_price)
    private TextView mTvOrderPrice;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.rl_pay_type)
    private View mPayType;

    @ViewInject(R.id.btn_confirm)
    private Button mBtnConfirm;

    @ViewInject(R.id.tv_order_price_hint)
    private TextView tv_order_price_hint;

    @Event(R.id.rl_pay_type)
    private void onPayTypeClick(View view) {
        AlertDialog.Builder dialog = DialogMaker.getDialog(this);
        dialog.setTitle("付款方式");
        String[] type = new String[]{getString(R.string.pay_type_online), getString(R.string.pay_type_offline)};

        dialog.setItems(type, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        mTvPayType.setText(R.string.pay_type_online);
                        break;
                    case 1:
                        mTvPayType.setText(R.string.pay_type_offline);
                        break;
                }
            }
        });
        dialog.show();
    }

    @Event(R.id.btn_confirm)
    private void onConfirmClick(View view) {
        if (mOrderMoney <= 0) {
            if (mOperatorType == TYPE_CAR_CREATE_ADD || mOperatorType == TYPE_CAR_UPDATE_ADD) {
                ToastUtil.showToastShort("申请数量为0");
            } else {
                ToastUtil.showToastShort("商品总金额为0");
            }
        } else {
            //添加内存中商品数据
            if (mOperatorType == TYPE_CAR_CREATE_ADD || mOperatorType == TYPE_CAR_UPDATE_ADD) {
                mHelper.confirmOrder();
            } else if (mOperatorType == TYPE_ORDER_RETURN || mOperatorType == TYPE_ORDER_RETURN_1) {
                orderReturnConfirm();
            } else {
                orderConfirm();
            }
        }
    }

    //提交订单
    private void orderConfirm() {
        AlertDialog.Builder dialog = DialogMaker.getDialog(this);
        View content = View.inflate(this, R.layout.dialog_confirm_order, null);
        //设置订单总额
        TextView mOrder = (TextView) content.findViewById(R.id.tv_order_price);
        mOrder.setText(getString(R.string.price, NumberUtil.getDouble(mOrderMoney)));
        //已经优惠金额
        TextView discount = (TextView) content.findViewById(R.id.tv_discount_price);
        discount.setText(getString(R.string.price, NumberUtil.getDouble(mDiscountMoney)));

        //实际支付价格
        final EditText ed_price = (EditText) content.findViewById(R.id.ed_price);
        dialog.setView(content);
        dialog.setPositiveButton("提交", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ViewHelper.KeyBoardHide(ed_price);

                //获取支付金额
                String money = ed_price.getEditableText().toString().trim();
                if (TextUtils.isEmpty(money)) {
                    money = "0";
                }

                Float aFloat = Float.valueOf(money);
                if (aFloat > mOrderMoney) {
                    ToastUtil.showToastLong("实际付款金额大于订单总额");
                    return;
                }
                confirmMoneyShow(money, String.valueOf(NumberUtil.getDouble(mOrderMoney - aFloat)));
            }
        });
        dialog.setNegativeButton(R.string.dialog_cancel, null);

        AlertDialog alert = dialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    //实际付款提示
    private void confirmMoneyShow(final String money, String lost) {

        AlertDialog.Builder dialog = DialogMaker.getDialog(this);
        dialog.setTitle("订单付款确认");

        ComplexText.TextBuilder tv_m1 = new ComplexText.TextBuilder();
        tv_m1.setText(money + "元");
        tv_m1.setBold(true);
        tv_m1.setTextColor(ContextCompat.getColor(this, R.color.app_red));
        tv_m1.setTextSizeDp(18);

        ComplexText.TextBuilder tv_m2 = new ComplexText.TextBuilder();
        tv_m2.setText(lost + "元");
        tv_m2.setTextColor(ContextCompat.getColor(this, R.color.app_red));
        tv_m2.setBold(true);
        tv_m2.setTextSizeDp(18);

        SpannableStringBuilder text = ComplexText.getText(new ComplexText.TextBuilder("收款金额:"), tv_m1, new ComplexText.TextBuilder("  赊账金额:"), tv_m2);

        dialog.setMessage(text);
        dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //实际付款提示
                mOrderPayMoney = Float.valueOf(money);
                //提交订单
                mHelper.confirmOrder();
            }
        });
        dialog.setNegativeButton(R.string.dialog_cancel, null);
        dialog.setCancelable(false);
        dialog.show();
    }

    //申请退货
    private void orderReturnConfirm() {

        AlertDialog.Builder dialog = DialogMaker.getDialog(this);
        View content = View.inflate(this, R.layout.dialog_confirm_return_product, null);
        //设置退货总额
        TextView mOrder = (TextView) content.findViewById(R.id.tv_order_price);
        mOrder.setText(getString(R.string.price, NumberUtil.getDouble(mOrderMoney)));
        //备注
        final EditText ed_remark = (EditText) content.findViewById(R.id.ed_remark);
        dialog.setView(content);
        dialog.setPositiveButton("提交", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mHelper.confirmReturn(ed_remark.getText().toString());
            }
        });
        dialog.setNegativeButton(R.string.dialog_cancel, null);
        dialog.setCancelable(false);
        dialog.show();
    }


    /**
     * 操作商品
     */
    public static final String TYPE_PRODUCT_OPERATOR = "product_operator";
    //拜访订单提交
    public static final int TYPE_ORDER_SELF = 11;
    //车销订单提交
    public static final int TYPE_ORDER_STORE = 12;
    //退库申请
    public static final int TYPE_ORDER_RETURN = 5;
    public static final int TYPE_ORDER_RETURN_1 = 6;

    //装车单创建申请车销商品
    public static final int TYPE_CAR_CREATE_ADD = 3;
    //装车单更新添加车销商品
    public static final int TYPE_CAR_UPDATE_ADD = 4;

    private LinearLayoutManager mLayoutManager;
    private RecyclerAdapter mRecyclerAdapter;

    private int mOperatorType;

    private ArrayList<ShopCarModel> mCarData;

    private ArrayList<GiftModel> mGiftData;

    private float mOrderMoney = 0f;//订单总额

    private float mDiscountMoney = 0f;//订单优惠总额

    private float mOrderPayMoney = 0f;//实际支付金额

    private ConfirmOrderHelper mHelper;

    @Override
    public Object getValue(int type) {
        if (type == ConfirmOrderHelper.TYPE_ORDER_TYPE) {
            return mOperatorType;
        }
        if (type == ConfirmOrderHelper.TYPE_ORDER_DATA) {
            return mCarData;
        }
        if (type == ConfirmOrderHelper.TYPE_GIFT_DATA) {
            return mGiftData;
        }
        if (type == ConfirmOrderHelper.TYPE_ORDER_MONEY) {
            return mOrderMoney;
        }
        if (type == ConfirmOrderHelper.TYPE_PAY_TYPE) {
            return 2;
        }
        if (type == ConfirmOrderHelper.TYPE_ORDER_PAY_MONEY) {
            return mOrderPayMoney;
        }
        if (type == ConfirmOrderHelper.TYPE_SHOP_ID) {
            return getIntent().getStringExtra(GlobalParams.Intent_shop_id);
        }
        if (type == ConfirmOrderHelper.TYPE_SHOP_ORDER_ID) {
            return getIntent().getStringExtra(GlobalParams.Intent_shop_order_id);
        }
        return super.getValue(type);
    }

    @Override
    public void showValue(int type, Object obj) {
        super.showValue(type, obj);

        if (type == ConfirmOrderHelper.TYPE_RES_SAVE_ORDER_FINISH) {//订单提交完成
            String orderInfo = new Gson().toJson(obj);
            //统计订单
            OrderStatisticsEvent event = new OrderStatisticsEvent();
            event.orderInfo = orderInfo;
            EventHelper.postDefaultEvent(event);
            Intent intent = new Intent(this, ShopDetailsActivity.class);
            intent.putExtra(TYPE_PRODUCT_OPERATOR, mOperatorType);
            startActivity(intent);

        } else if (type == ConfirmOrderHelper.TYPE_RES_CREATE_CAR_ADD_FINISH) {//新增装车单车销商品提交
            Intent intent = new Intent(this, StoreApplyAddActivity.class);
            intent.putExtra("data", mCarData);
            intent.putExtra("type", 1);
            startActivity(intent);

        } else if (type == ConfirmOrderHelper.TYPE_RES_UPDATE_CAR_ADD_FINISH) {//更新装车单车销商品
            Intent intent = new Intent(this, StoreApplyUpdateActivity.class);
            intent.putExtra("data", mCarData);
            intent.putExtra("type", 1);
            startActivity(intent);
        } else if (type == ConfirmOrderHelper.TYPE_RES_RETURN_FINISH) {//退货申请完成
            if (mOperatorType == TYPE_ORDER_RETURN) {
                Intent intent = new Intent(this, ShopDetailsActivity.class);
                intent.putExtra(TYPE_PRODUCT_OPERATOR, mOperatorType);
                startActivity(intent);
            } else if (mOperatorType == TYPE_ORDER_RETURN_1) {
                finish();
            }
        }
    }

    @Override
    protected void initParams() {
        super.initToolbar(TYPE_child_other_back);

        mHelper = new ConfirmOrderHelper(this);

        mOperatorType = getIntent().getIntExtra(TYPE_PRODUCT_OPERATOR, TYPE_ORDER_SELF);

        if (mOperatorType == TYPE_ORDER_SELF) {
            setTitle(R.string.shop_detail_order_self);
        } else if (mOperatorType == TYPE_ORDER_STORE) {
            setTitle(R.string.shop_detail_order_store);
        } else if (mOperatorType == TYPE_CAR_CREATE_ADD) {
            setTitle(R.string.user_nav_main_store_apply);
            tv_order_price_hint.setText("商品总额:");
            mBtnConfirm.setText("添加");
        } else if (mOperatorType == TYPE_CAR_UPDATE_ADD) {
            setTitle(R.string.user_nav_main_store_update);
            tv_order_price_hint.setText("商品总额:");
            mBtnConfirm.setText("添加");
        } else if (mOperatorType == TYPE_ORDER_RETURN || mOperatorType == TYPE_ORDER_RETURN_1) {
            setTitle(R.string.shop_detail_order_return);
            tv_order_price_hint.setText("退款总额:");
            mBtnConfirm.setText("提交申请");
        }

        mCarData = getIntent().getParcelableArrayListExtra("data");

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerAdapter = new RecyclerAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        showData();
        //计算总额
        onItemClick(0);
    }

    @Override
    public void onItemClick(int position) {
        ArrayList<ShopCarModel> data = this.mCarData;
        float money = 0;
        float discount = 0;
        for (ShopCarModel car : data) {
            money += car.curNum * car.getProductPrice();
            discount += car.curNum * car.getDiscount();
        }
        //订单金额
        mOrderMoney = money;
        //优惠金额
        mDiscountMoney = discount;

        String res = NumberUtil.getDouble(money);

        mTvOrderPrice.setText(getString(R.string.price, res));
    }

    @Override
    public void onClickData(Object o) {
        mCarData.remove(o);
        showData();
    }

    private void showData() {
        //商品数据
        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (ShopCarModel car : mCarData) {
            //拜访下单和车削下单，退货申请显示自定义价格按钮
            if (mOperatorType == TYPE_ORDER_SELF || mOperatorType == TYPE_ORDER_STORE
                    || mOperatorType == TYPE_ORDER_RETURN || mOperatorType == TYPE_ORDER_RETURN_1) {
                car.showFixPrice = true;
            } else {
                car.showFixPrice = false;
            }
            car.operatorType = mOperatorType;
            holders.add(new ProductConfirmNewDataHolder(car, this));
        }
        //赠品数据
      /*  if (mGiftData != null) {
            for (GiftModel gift : mGiftData) {
                holders.add(new GiftDataHolder(gift));
            }
        }*/
        mRecyclerAdapter.setDataHolders(holders);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       /* //车削和拜访的支持添加赠品
        if (mOperatorType == TYPE_ORDER_SELF || mOperatorType == TYPE_ORDER_STORE) {
            getMenuInflater().inflate(R.menu.user_menu_order_g, menu);
        }*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivityForResult(new Intent(this, SelectGiftProductActivity.class), 10);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        //赠品返回
        if (requestCode == 10) {
            mGiftData = (ArrayList<GiftModel>) data.getSerializableExtra("data");
            showData();
        }
    }
}
