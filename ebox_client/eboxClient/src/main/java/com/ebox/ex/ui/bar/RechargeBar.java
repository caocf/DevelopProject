package com.ebox.ex.ui.bar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.R;
import com.ebox.ex.network.model.RspGetActive;
import com.ebox.ex.network.model.RspRechargeOrderId;
import com.ebox.ex.network.model.RspRechargePlayOrder;
import com.ebox.ex.network.model.base.type.AcitveType;
import com.ebox.ex.network.model.base.type.DeviceType;
import com.ebox.ex.network.model.req.ReqGetRechargePayOrder;
import com.ebox.ex.network.request.RechargeOrderIdRequest;
import com.ebox.ex.network.request.RechargePayOrderRequest;
import com.ebox.ex.network.request.RequestGetActive;
import com.ebox.ex.ui.RechargeActivity;
import com.ebox.ex.ui.RechargeWeiActivity;
import com.ebox.ex.ui.adapter.ActiveAdapter;
import com.ebox.ex.utils.OperatorHelper;
import com.ebox.pub.utils.DeviceInfoUtil;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.PackageUtil;
import com.ebox.pub.utils.Tip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prin on 2015/10/29.
 * 充值界面
 */
public class RechargeBar extends OperMainLayout implements View.OnClickListener, ActiveAdapter.activeOnItemClickListener, CompoundButton.OnCheckedChangeListener {

    private Button bt_recharge;
    private TextView tv_account;
    private GridView mGridView;
    private LinearLayout ll_active;
    private RadioButton rb_alipay, rb_weixin;

    private ActiveAdapter mAdapter;
    private String operatorId;
    private DialogUtil dialogUtil;
    private Tip tip;
    private Activity context;
    private int money;
    private final static String TAG = "RechargeBar    ";
    private final static int ALI_PAY = 1;
    private final static int WEIXIN_PAY = 2;

    public RechargeBar(Context context, String operatorId) {
        super(context);

        this.context = (Activity) context;
        this.operatorId = operatorId;
        initViews(context);
    }


    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.ex_bar_recharge, this, true);

        bt_recharge = (Button) findViewById(R.id.bt_recharge);
        tv_account = (TextView) findViewById(R.id.tv_pay_account);
        mGridView = (GridView) findViewById(R.id.GridView);
        ll_active = (LinearLayout) findViewById(R.id.ll_active);
        rb_alipay = (RadioButton) findViewById(R.id.rb_alipay);
        rb_weixin = (RadioButton) findViewById(R.id.rb_weixin);

        rb_alipay.setOnCheckedChangeListener(this);
        rb_weixin.setOnCheckedChangeListener(this);

        
//        rb_alipay.setChecked(true);

        if (operatorId != null) {
            tv_account.setText(operatorId);
        }

        mAdapter = new ActiveAdapter(context);
        mAdapter.setListener(this);
        mGridView.setAdapter(mAdapter);

        bt_recharge.setOnClickListener(this);

        dialogUtil = new DialogUtil();
        dialogUtil.createProgressDialog((Activity) context);

        initData();

    }

    private void initData() {
        ll_active.setVisibility(GONE);
        dialogUtil.showProgressDialog();
        if (OperatorHelper.cacheCookie != null) {
            getActive();
        } else {
            //静默登录
            OperatorHelper.silenceLogin(new OperatorHelper.LoginResponseListener() {
                @Override
                public void success() {
                    getActive();
                }

                @Override
                public void failed() {
                    dialogUtil.closeProgressDialog();
                    new Tip(context, getResources().getString(R.string.pub_connect_failed), null).show(0);
                    ll_active.setVisibility(GONE);
                }
            });
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_recharge:
                Recharge();

                break;
        }
    }

    private void resetMySelf() {
        rb_alipay.setChecked(false);
        rb_weixin.setChecked(false);
    }

    private void Recharge() {
        if (checkPara()) {
            if (OperatorHelper.cacheCookie != null) {
                doPay();

            } else {
                OperatorHelper.silenceLogin(new OperatorHelper.LoginResponseListener() {
                    @Override
                    public void success() {
                        doPay();
                    }

                    @Override
                    public void failed() {
                        dialogUtil.closeProgressDialog();
                        new Tip(context, getResources().getString(R.string.pub_connect_failed), null).show(0);
                    }
                });
            }
        }
    }

    private void doPay() {
        if (rb_alipay.isChecked()) {
            reqQrCode(ALI_PAY);
        } else if (rb_weixin.isChecked()) {
            reqQrCode(WEIXIN_PAY);
        } else {
            reqQrCode(ALI_PAY);
        }
    }


    /**
     * 获取订单
     */
    private void reqQrCode(final int pay_type) {
        resetMySelf();
        dialogUtil.showProgressDialog();
        RechargeOrderIdRequest orderIdRequest = new RechargeOrderIdRequest(money, new ResponseEventHandler<RspRechargeOrderId>() {
            @Override
            public void onResponseSuccess(RspRechargeOrderId result) {
                if (result.isSuccess()) {
                    if (pay_type == ALI_PAY) {
                        createAliPayOrder(result.getData().order.order_id, result.getData().order.fee);
                    } else if (pay_type == WEIXIN_PAY) {
                        createWeixinPayOrder(result.getData().order.order_id, result.getData().order.fee);
                    }
                } else {

                    new Tip(context, result.getMsg(), null).show(0);
                    dialogUtil.closeProgressDialog();
                }

            }

            @Override
            public void onResponseError(VolleyError error) {
                dialogUtil.closeProgressDialog();
                LogUtil.e(error.getMessage());
                new Tip(context, getResources().getString(R.string.pub_connect_failed), null).show(0);
            }
        });
        RequestManager.addRequest(orderIdRequest, null);
    }


    /**
     * 根据订单ID，订单fee获得支付单
     *
     * @param orderid
     * @param fee
     */
    private void createAliPayOrder(String orderid, int fee) {
        ReqGetRechargePayOrder params = new ReqGetRechargePayOrder();
        List<String> orders = new ArrayList<String>();
        orders.add(orderid);
        params.order_ids = orders;
        params.pay_type=9;
        params.return_url = NetworkConfig.notify_address + "/kuaidi/pay/alipay/{pay_id}/redirect";

        params.total_fee = money;
        params.total_num = 1;
        params.device_info = getDeviceInfoObject(context);
        params.service = "delivery_courier_recharge_order_service";


        RechargePayOrderRequest payOrderRequest = new RechargePayOrderRequest(params, new ResponseEventHandler<RspRechargePlayOrder>() {
            @Override
            public void onResponseSuccess(RspRechargePlayOrder result) {

                dialogUtil.closeProgressDialog();
                if (result.isSuccess()) {
                    String order = result.getData().order_info.params;
                    Intent intent = new Intent(context, RechargeActivity.class);
                    intent.putExtra("order_id", order);
                    context.startActivity(intent);
                } else {
                    new Tip(context, result.getMsg(), null).show(0);
                }
            }

            @Override
            public void onResponseError(VolleyError error) {
                dialogUtil.closeProgressDialog();
                LogUtil.e(error.getMessage());
                new Tip(context, getResources().getString(R.string.pub_connect_failed), null).show(0);
            }
        });
        RequestManager.addRequest(payOrderRequest, null);
    }

    /**
     * 根据订单ID，订单fee获得微信支付单
     *
     * @param orderid
     * @param fee
     */
    private void createWeixinPayOrder(final String orderid, final int fee) {
        ReqGetRechargePayOrder params = new ReqGetRechargePayOrder();
        List<String> orders = new ArrayList<String>();
        orders.add(orderid);
        params.order_ids = orders;
        params.pay_type=10;
        params.total_fee = money;
        params.total_num = 1;
        params.device_info = getDeviceInfoObject(context);
        params.service = "delivery_courier_recharge_order_service";


        RechargePayOrderRequest payOrderRequest = new RechargePayOrderRequest(params, new ResponseEventHandler<RspRechargePlayOrder>() {
            @Override
            public void onResponseSuccess(RspRechargePlayOrder result) {

                dialogUtil.closeProgressDialog();
                if (result.isSuccess()) {
                    String order = result.getData().order_info.code_url;
                    Intent intent = new Intent(context, RechargeWeiActivity.class);
                    intent.putExtra("order_url", order);
                    intent.putExtra("order_money",String.valueOf(fee/100));
                    intent.putExtra("order_id",orderid);
                    context.startActivity(intent);
                } else {
                    new Tip(context, result.getMsg(), null).show(0);
                }
            }

            @Override
            public void onResponseError(VolleyError error) {
                dialogUtil.closeProgressDialog();
                LogUtil.e(error.getMessage());
                new Tip(context, getResources().getString(R.string.pub_connect_failed), null).show(0);
            }
        });
        RequestManager.addRequest(payOrderRequest, null);
    }


    private boolean checkPara() {
        if (operatorId == null) {
            tip = new Tip(context, "账号异常", null);
            tip.show(0);
            return false;
        }

        if (money == 0) {
            tip = new Tip(context, "请选择充值金额", null);
            tip.show(0);
            return false;
        }

        return true;
    }

    @Override
    public void OnItemClickListener(int positon) {
        AcitveType item = mAdapter.getItem(positon);
        if (item != null) {
            money = item.getAct_value();
        }
    }

    /**
     * 获得可充值的活动
     */
    public void getActive() {
        RequestGetActive req = new RequestGetActive(new ResponseEventHandler<RspGetActive>() {
            @Override
            public void onResponseSuccess(RspGetActive result) {
                dialogUtil.closeProgressDialog();
                if (result.isSuccess()) {
                    showActive(result.getData().getActivities());
                } else {
                    ll_active.setVisibility(GONE);
                    new Tip(context, result.getMsg(), null).show(0);
                }
            }

            @Override
            public void onResponseError(VolleyError error) {
                dialogUtil.closeProgressDialog();
                ll_active.setVisibility(GONE);
                new Tip(context, getResources().getString(R.string.pub_connect_failed), null).show(0);
            }
        });
        RequestManager.addRequest(req, null);
    }

    /**
     * 显示充值活动
     *
     * @param result
     */
    private void showActive(List<AcitveType> result) {
        mAdapter.addAll(result);
        //默认选择第一个
        if (result.size() > 0) {
            money = result.get(0).getAct_value();
            ll_active.setVisibility(VISIBLE);
        } else {
            ll_active.setVisibility(GONE);
            new Tip(context, "网点没有充值活动", null).show(0);
        }

    }


    public static DeviceType getDeviceInfoObject(Context context) {
        DeviceType type = new DeviceType();
        type.udid = DeviceInfoUtil.getDeviceId(context);
        type.app_name = "com.ebox";
        type.appver = PackageUtil.getVersionName(context);
        type.os = "Android";
        type.phonemodel = DeviceInfoUtil.getDeviceName(context);
        type.network = DeviceInfoUtil.getNetType(context);
        type.osver = DeviceInfoUtil.getSysVersion(context);

        return type;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.rb_weixin:
                if (isChecked) {
                    rb_alipay.setChecked(false);
                }
                break;
            case R.id.rb_alipay:
                if (isChecked) {
                    rb_weixin.setChecked(false);
                }
                break;

        }
    }


}
