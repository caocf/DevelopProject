package com.ebox.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.chinaums.mis.sst.transshell.CommonDataDef;
import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.mall.warehouse.model.AddressListModel;
import com.ebox.mall.warehouse.model.AddressModel;
import com.ebox.mall.warehouse.model.AttachmentModel;
import com.ebox.mall.warehouse.model.MerchantListModel;
import com.ebox.mall.warehouse.model.MerchantModel;
import com.ebox.mall.warehouse.model.PayResultModel;
import com.ebox.mall.warehouse.model.RespAddressListModel;
import com.ebox.mall.warehouse.model.RespMerchantListModel;
import com.ebox.mall.warehouse.model.RespPayOrderModel;
import com.ebox.mall.warehouse.model.TradingBaseModel;
import com.ebox.mall.warehouse.model.TradingDetailModel;
import com.ebox.mall.warehouse.model.enums.PayType;
import com.ebox.mall.warehouse.network.ErrorCode;
import com.ebox.mall.warehouse.network.request.TradingAddressListRequest;
import com.ebox.mall.warehouse.network.request.TradingCreateOrderRequest;
import com.ebox.mall.warehouse.network.request.TradingPayOrderRequest;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.customview.RoundedImageView;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.ui.keyboard.KeyboardUtil;
import com.ebox.pub.utils.DeviceInfoUtil;
import com.ebox.pub.utils.Dialog;
import com.ebox.pub.utils.Dialog.onClickListener;
import com.ebox.pub.utils.EditTextUtil;
import com.ebox.pub.utils.FunctionUtil;
import com.ebox.pub.utils.FunctionUtils;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.PackageUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.ums.model.ums.TransData;
import com.ebox.ums.ui.PayCenterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TradingPayActivity extends CommonActivity implements
        OnClickListener, onClickListener {
    private Context mContext;


    private RoundedImageView imageView;
    private TextView titleText;
    private TextView buyNumText;
    private TextView singal_price;
    private TextView ShippingFeeText;
    private TextView total_fee;
    private Button mPayBtn;
    private EditText et_telephone;
    private TextView mAddressText;
    private KeyboardUtil keyBoard;
    private Tip tip;

    private ArrayList<TradingBaseModel> mListGoods = new ArrayList<TradingBaseModel>();
    private ArrayList<TradingBaseModel> mListSelectedGoods = new ArrayList<TradingBaseModel>();
    private MerchantListModel mMerchantListModel;
    private PayResultModel mPayResultModel;
    private AddressModel mAddressModel;
    private String mobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_activity_tradingpay);
        MGViewUtil.scaleContentView(this, R.id.rootView);

        // receive external params
        TradingDetailModel model = (TradingDetailModel) getIntent()
                .getSerializableExtra("goods");
        if (model != null) {
            mListGoods.add(model);
        }


        mContext = TradingPayActivity.this;
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tip != null) {
            tip.closeTip();
        }
    }

    protected void initView() {

        imageView = (RoundedImageView) this.findViewById(R.id.tradingImage);
        titleText = (TextView) this.findViewById(R.id.titleText);

        buyNumText = (TextView) this.findViewById(R.id.buyNumText);
        singal_price = (TextView) this.findViewById(R.id.singal_price);
        total_fee = (TextView) this.findViewById(R.id.total_fee);
        ShippingFeeText = (TextView) this.findViewById(R.id.ShippingFeeText);
        mAddressText = (TextView) this.findViewById(R.id.addr);

        et_telephone = (EditText) this.findViewById(R.id.et_telephone);
        EditTextUtil.limitCount(et_telephone, 11, null);
        et_telephone.requestFocus();
        KeyboardUtil.hideInput(this, et_telephone);
        keyBoard = new KeyboardUtil(TradingPayActivity.this, TradingPayActivity.this,
                et_telephone);
        keyBoard.showKeyboard();
        mPayBtn = (Button) this.findViewById(R.id.btn_ok);
        mPayBtn.setOnClickListener(this);
        initTitle();
    }

    private Title title;
    private TitleData data;

    private void initTitle() {
        title = (Title) findViewById(R.id.title);
        data = title.new TitleData();
        data.backVisibility = 1;
        data.tvContent = getResources().getString(R.string.pub_huozhan);
        data.tvVisibility = true;
        title.setData(data, this);
    }

    private void initData() {

        updateTotalInfo(mListGoods);

        doTradingAddressRequest();

    }


    private void updateTotalInfo(List<TradingBaseModel> listModel) {
        double totalmall_money = 0;

        for (TradingBaseModel model : listModel) {
            totalmall_money += model.getDiscount_price() * model.getBuyNum();

            titleText.setText(model.getTitle());
            singal_price
                    .setText(getString(R.string.mall_money, FunctionUtils
                            .getDouble(model.getDiscount_price() * 1.0 / 100)));

            AttachmentModel attachModel = model.getAttachments();
            if (attachModel != null && attachModel.getImages().size() > 0) {
                RequestManager.loadImage(imageView,
                        RequestManager.getImageUrl(attachModel
                                .getImages().get(0) + GlobalField.IMAGE_STYLE150_150), 0);
            }

            buyNumText.setText(model.getBuyNum() + "");

        }

        if (totalmall_money >= GlobalField.mLowestFee) {
            ShippingFeeText.setText(getString(R.string.mall_money, 0));
        } else {
            ShippingFeeText.setText(getString(R.string.mall_money, FunctionUtils
                    .getDouble(GlobalField.mShippingFee * 1.0 / 100)));
            totalmall_money += GlobalField.mShippingFee;
        }

        total_fee.setText(getString(R.string.mall_money,
                FunctionUtils.getDouble(totalmall_money * 1.0 / 100)));
    }

    private void doTradingAddressRequest() {
        String terminal_code = AppApplication.getInstance().getTerminal_code();
        if (terminal_code.equals("")) {
            Toast.makeText(AppApplication.globalContext, getResources().getString(R.string.pub_check_config), Toast.LENGTH_LONG).show();
            return;
        }
        TradingAddressListRequest request = new TradingAddressListRequest(terminal_code,
                new ResponseEventHandler<RespAddressListModel>() {
                    @Override
                    public void onResponseSuccess(RespAddressListModel result) {
                        if (result.getStatus() == ErrorCode.SUCCESS) {
                            AddressListModel listModel = result.getData();
                            if (listModel != null
                                    && listModel.getAddress() != null
                                    && listModel.getAddress().size() > 0) {
                                mAddressModel = listModel.getAddress().get(0);
                                showUserAddressInfo(mAddressModel);
                            }
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error) {
                        LogUtil.i(error.getMessage());
                    }

                });
        RequestManager.addRequest(request, this);
    }

    private void showUserAddressInfo(AddressModel model) {
        if (model != null && model.getAddress_type() == 0) {
            mAddressText.setText(model.getDelivery_name());
        }

    }


    private void doTradingCreateOrderRequest(String content) {
        TradingCreateOrderRequest request = new TradingCreateOrderRequest(
                content, new ResponseEventHandler<RespMerchantListModel>() {
            @Override
            public void onResponseSuccess(RespMerchantListModel result) {
                if (result.getStatus() == ErrorCode.SUCCESS) {
                    mMerchantListModel = result.getData();
                    //payOrder();
                    singal_price
                            .setText(getString(R.string.mall_money, FunctionUtils
                                    .getDouble(mMerchantListModel.getMerchants().get(0)
                                            .getGoods().get(0).getPrice() * 1.0 / 100)));

                    total_fee.setText(getString(R.string.mall_money,
                            FunctionUtils.getDouble(mMerchantListModel.getTotal_fee() * 1.0 / 100)));
                    new Dialog(mContext,
                            getString(R.string.mall_your_need_pay,
                                    FunctionUtils.getDouble(mMerchantListModel.getTotal_fee() * 1.0 / 100)),
                            getString(R.string.ums_payConfirm),
                            getString(R.string.pub_cancel),
                            TradingPayActivity.this,
                            null);


                } else {
                    tip = new Tip(mContext,
                            result.getMsg(),
                            null);
                    tip.show(0);
                }
            }

            @Override
            public void onResponseError(VolleyError error) {
                tip = new Tip(mContext,
                        error.getMessage(),
                        null);
                tip.show(0);
            }

        });
        RequestManager.addRequest(request, this);
    }


    private void doTradingPayOrderRequest(String content) {
        TradingPayOrderRequest request = new TradingPayOrderRequest(content,
                new ResponseEventHandler<RespPayOrderModel>() {
                    @Override
                    public void onResponseSuccess(RespPayOrderModel result) {
                        if (result.getStatus() == ErrorCode.SUCCESS) {
                            mPayResultModel = result.getData();

                            //TODO 跳转到支付页面

                            TransData transReq = new TransData();
                            transReq.setPhone(mobile);
                            transReq.setAppType(CommonDataDef.cupBasicApp);
                            transReq.setTranType(CommonDataDef.cupCustTranstype);
                            transReq.setApendCode("0");
                            transReq.setcontent("消费");
                            transReq.setTradeType(7);
                            transReq.setPay_type(mPayResultModel.getPay_type());
                            transReq.setPayId(mPayResultModel.getPay_id());
                            transReq.setNoncestr(mPayResultModel.getOrder_info().getNoncestr());
                            transReq.setTimestamp(mPayResultModel.getOrder_info().getTimestamp());
                            String price = mPayResultModel.getTotal_fee() + "";

                            transReq.setAmount(("000000000000" + price).substring(price.length(), 12 + price.length()));
                            LogUtil.d("cash--", price);
                            Intent intent = new Intent(TradingPayActivity.this, PayCenterActivity.class);
                            Bundle mBundle = new Bundle();
                            mBundle.putSerializable("trans", transReq);
                            intent.putExtras(mBundle);
                            startActivity(intent);
                            TradingPayActivity.this.finish();


                        } else {
                            tip = new Tip(mContext,
                                    result.getMsg(),
                                    null);
                            tip.show(0);
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error) {
                        tip = new Tip(mContext,
                                error.getMessage(),
                                null);
                        tip.show(0);
                    }

                });
        RequestManager.addRequest(request, this);
    }

    private JSONObject getDeviceInfoObject() {
        JSONObject deviceObject = new JSONObject();
        try {
            deviceObject.put("udid", AppApplication.getInstance().getTerminal_code());
            deviceObject.put("appver", PackageUtil.getVersionName(mContext));
            deviceObject.put("os", "android");
            deviceObject.put("phonemodel",
                    DeviceInfoUtil.getDeviceName(mContext));
            deviceObject.put("network", DeviceInfoUtil.getNetType(mContext));
            deviceObject.put("app_name", "ebox");
            deviceObject.put("osver", DeviceInfoUtil.getSysVersion(mContext));
            return deviceObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return deviceObject;
    }


    private void onCreateOrder() {
        // {"merchants":[{"merchant_id":"53bf946ebea2ff642842c0d3","goods":[{"trading_id":"54084bf2b8a9bd17058b4569","num":1,"price":100,"total_fee":100}],"total_fee":100,"total_num":1}],"total_fee":100,"total_num":1,"address_info":""}
        String orderContent = "";


        try {
            JSONObject merchantsObject = new JSONObject();

            JSONObject addressObject = new JSONObject();
            addressObject.put("_id", mAddressModel.get_id());
            addressObject.put("address_type", mAddressModel.getAddress_type());
            merchantsObject.put("address_info", addressObject);
            merchantsObject.put("device_info", getDeviceInfoObject());


            JSONArray merchantArray = new JSONArray();
            JSONObject merchantObject = new JSONObject();
            JSONArray goodsArray = new JSONArray();

            int total_num = 0;
            int total_fee = 0;
            mListSelectedGoods.clear();

            for (int i = 0; i < mListGoods.size(); i++) {
                TradingBaseModel model = (TradingBaseModel) mListGoods.get(i);

                if (i == 0) {
                    merchantObject.put("merchant_id", model.getAuthor_uid());
                }

                JSONObject goodItemObject = new JSONObject();

                goodItemObject.put("trading_id", model.get_id());

                goodItemObject.put("num", model.getBuyNum());
                goodItemObject
                        .put("price", model.getDiscount_price() * 100);
                goodItemObject
                        .put("total_fee",
                                model.getBuyNum()
                                        * model.getDiscount_price() * 100);
                goodsArray.put(goodItemObject);

                total_num += model.getBuyNum();
                total_fee += model.getBuyNum() * model.getDiscount_price()
                        * 100;

                mListSelectedGoods.add(model);
            }

            merchantObject.put("goods", goodsArray);
            merchantObject.put("total_fee", total_fee);
            merchantObject.put("total_num", total_num);
            merchantArray.put(merchantObject);

            merchantsObject.put("merchants", merchantArray);
            merchantsObject.put("total_fee", total_fee);
            merchantsObject.put("total_num", total_num);
            merchantsObject.put("mobile", mobile);

            orderContent = merchantsObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(orderContent)) {
            tip = new Tip(mContext, getResources().getString(R.string.mall_generate_order_failed),
                    null);
            tip.show(0);
            return;
        }

        doTradingCreateOrderRequest(orderContent);
    }

    private void payOrder() {
        // "pay_type":1,
        // "total_fee":10000,
        // "total_num":1,
        // "order_ids":["201409131744318249297081"]

        String orderContent = "";

        try {

            JSONObject payObject = new JSONObject();
            payObject.put("device_info", getDeviceInfoObject());
            payObject.put("pay_type", PayType.Ums_Pay);
            payObject.put("total_fee", mMerchantListModel.getTotal_fee());
            payObject.put("total_num", mMerchantListModel.getTotal_num());
            payObject.put("mobile", mobile);

            JSONArray orderIdArray = new JSONArray();

            for (int i = 0; i < mMerchantListModel.getMerchants().size(); i++) {
                MerchantModel merchantModel = mMerchantListModel.getMerchants()
                        .get(i);
                orderIdArray.put(merchantModel.getOrder_id());
            }

            payObject.put("order_ids", orderIdArray);

            orderContent = payObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(orderContent)) {

            tip = new Tip(mContext,
                    AppApplication.globalContext.getResources().getString(R.string.ums_pay_order_failed),
                    null);
            tip.show(0);
            return;
        }

        doTradingPayOrderRequest(orderContent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                if (et_telephone.getText() == null
                        || et_telephone.getText().toString().length() == 0
                        ) {
                    tip = new Tip(mContext,
                            getResources().getString(R.string.ums_phoneNoLengthError),
                            null);
                    tip.show(0);
                    return;
                }

                if (!FunctionUtil.isMobile(et_telephone.getText().toString())) {
                    tip = new Tip(mContext,
                            getResources().getString(R.string.ums_phoneNoLengthError),
                            null);
                    tip.show(0);
                    return;
                }
                if (mAddressText.getText() == null
                        || mAddressText.getText().toString().length() == 0
                        ) {
                    tip = new Tip(mContext,
                            getResources().getString(R.string.mall_addr_error),
                            null);
                    tip.show(0);
                    return;
                }

                mobile = et_telephone.getText().toString();
                onCreateOrder();

                //payOrder();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        title.showTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        title.stopTimer();
    }


    // to do test
//    private void test()
//    {
//        String str = "resultStatus={9000};memo={};result={_input_charset=\"utf-8\"&app_id=\"e35c8680d1984397\"&appenv=\"system=android^version=1.0.1\"&body=\"201409191049309918200498\"&it_b_pay=\"30m\"&notify_url=\"http%3A%2F%2Fdev.api.aimoge.com%2Fv1%2Ftrading%2Fpay%2Falipay%2Fnotify\"&out_trade_no=\"201409191049309918200498\"&partner=\"2088511706777100\"&payment_type=\"1\"&seller_id=\"service@mgooo.cn\"&service=\"mobile.securitypay.pay\"&subject=\"201409191049309918200498\"&total_fee=\"0.01\"&success=\"true\"&sign_type=\"RSA\"&sign=\"T6X95arY/Aa6Gh0NfIGQwEBRWO8IX9mbVU82hQ9fcnbe+hvNU5UQfKD5/RIZImcV9sVPB55KCUcy+YBG392KQrFxmtJUdeVACcOafpY7QNCr5HRs4hoaP8vy6Tnh1fjEm1oGg5DUW2HbpmecI1JSzx9D7BUVgOprPKzzasqoCmc=\"}";
//        Result result = new Result(str);
//        finishOrderPay("201409191049309918200498", result);
//    }


    @Override
    public void onOk(Object value) {
        payOrder();
    }

    @Override
    public void onCancel(Object value) {
    }

}
