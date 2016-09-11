package com.moge.gege.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.model.*;
import com.moge.gege.model.enums.EventType;
import com.moge.gege.model.enums.OrderStatusType;
import com.moge.gege.model.enums.PayResultType;
import com.moge.gege.model.enums.PayType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.TradingAddressRequest;
import com.moge.gege.network.request.TradingCancelOrderRequest;
import com.moge.gege.network.request.TradingOrderDetailRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.TradingDetailListAdapter;
import com.moge.gege.ui.event.Event;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.FunctionUtils;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.pay.PayHelper;
import com.moge.gege.util.pay.PayHelper.PayHelperResultLinstener;
import com.moge.gege.util.widget.CustomDialog;
import com.moge.gege.util.widget.ScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class TradingOrderDetailActivity extends BaseActivity
{
    private Activity mContext;
    @InjectView(R.id.orderIdText) TextView mOrderIdText;
    @InjectView(R.id.orderStatusText) TextView mOrderStatusText;
    @InjectView(R.id.shippingFeeText) TextView mShippingFeeText;
    @InjectView(R.id.totalFeeText) TextView mTotalFeeText;
    @InjectView(R.id.couponFeeText) TextView mCouponFeeText;
    @InjectView(R.id.payFeeText) TextView mPayFeeText;
    @InjectView(R.id.payTypeText) TextView mPayTypeText;
    @InjectView(R.id.orderTimeText) TextView mOrderTimeText;
    @InjectView(R.id.userNameText) TextView mUserNameText;
    @InjectView(R.id.mobileText) TextView mMobileText;
    @InjectView(R.id.addressText) TextView mAddressText;
    @InjectView(R.id.totalGoodsText) TextView mTotalGoodsText;

    @InjectView(R.id.bottomLayout) RelativeLayout mBottomLayout;
    @InjectView(R.id.cancelOrderText) TextView mCancelOrderText;
    @InjectView(R.id.payOrderText) TextView mPayOrderText;

    @InjectView(R.id.goodsListView) ScrollListView mGoodsListView;
    private TradingDetailListAdapter mAdapter;
    private List<TradingBaseModel> mTradingListData;

    private OrderModel mOrderDetailModel;
    private String mOrderId;
    private ArrayList<String> mOrderIdList = new ArrayList<String>();

    private int mPayType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradingorderdetail);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);

        // receive external params
        mOrderId = getIntent().getStringExtra("order_id");

        mContext = TradingOrderDetailActivity.this;
        initView();
        initData();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(R.string.order_detail);

        mAdapter = new TradingDetailListAdapter(this);
        mAdapter.setShowCheckBox(false);
        mGoodsListView.setAdapter(mAdapter);
    }

    private void initData()
    {
        showLoadingView();
        doTradingOrderDetailRequest(mOrderId);
    }

    private void updateView(OrderModel orderModel)
    {
        if (orderModel == null)
        {
            return;
        }

        mOrderIdText.setText(getString(R.string.order_id,
                orderModel.getOrder_id()));
        mOrderStatusText.setText(getString(R.string.order_status,
                orderModel.getStatus_verbose_name()));
        mShippingFeeText
                .setText(getString(R.string.order_shipping_fee, FunctionUtils
                        .getDouble(orderModel.getDelivery_fee() * 1.0 / 100)));
        mTotalFeeText
                .setText(getString(R.string.order_total_fee, FunctionUtils
                        .getDouble(orderModel.getTotal_fee() * 1.0 / 100)));
        if (orderModel.getCoupon() != null)
        {
            mCouponFeeText
                    .setText(getString(R.string.order_coupon_fee,
                            FunctionUtils.getDouble(orderModel.getCoupon()
                                    .getFee() * 1.0 / 100)));
        }
        else
        {
            mCouponFeeText.setText(getString(R.string.order_coupon_fee,
                    FunctionUtils.getDouble(0)));
        }
        mPayFeeText.setText(getString(R.string.order_pay_fee,
                FunctionUtils.getDouble(orderModel.getPay_fee() * 1.0 / 100)));
        showPayTypeInfo(orderModel.getPay_type());
        mOrderTimeText.setText(getString(R.string.order_time,
                orderModel.getOrder_at()));
        mTotalGoodsText.setText(getString(R.string.order_total_goods,
                orderModel.getTotal_num()));

        if (orderModel.getStatus() == OrderStatusType.NOT_PAYED
                || orderModel.getStatus() == OrderStatusType.PAY_CANCEL
                || orderModel.getStatus() == OrderStatusType.PAY_FAILED
                || orderModel.getStatus() == OrderStatusType.PAY_ING)
        {
            mBottomLayout.setVisibility(View.VISIBLE);
        }
        else if(orderModel.getPay_type() == PayType.Cod_Pay && (orderModel.getStatus() == OrderStatusType.WAIT_DELIVERY || orderModel.getStatus() == OrderStatusType.SORTING))
        {
            mBottomLayout.setVisibility(View.VISIBLE);
            mPayOrderText.setVisibility(View.GONE);

            RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) mCancelOrderText
                    .getLayoutParams();
            p.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            mCancelOrderText.setLayoutParams(p);
        }
        else
        {
            mBottomLayout.setVisibility(View.GONE);
        }

        mTradingListData = transferDataModel(orderModel.getGoods());
        mAdapter.clear();
        mAdapter.addAll(mTradingListData);
        mAdapter.notifyDataSetChanged();
        mGoodsListView.notifyDataChange();

        // query address
        if(orderModel.getAddress() != null)
        {
            showAddressInfo(orderModel.getAddress());
        }
        else
        {
            doTradingAddressRequest(orderModel.getAddress_id());
        }

        // for cancel order
        mOrderIdList.clear();
        mOrderIdList.add(orderModel.getOrder_id());
    }

    private void showPayTypeInfo(int payType)
    {
        mPayTypeText.setVisibility(View.VISIBLE);
        switch (payType)
        {
            case PayType.Ali_Pay:
                mPayTypeText.setText(getString(R.string.order_pay_type,
                        getString(R.string.ali_pay)));
                break;
            case PayType.Weixin_Pay:
                mPayTypeText.setText(getString(R.string.order_pay_type,
                        getString(R.string.weixin_pay)));
                break;
            case PayType.Cod_Pay:
                mPayTypeText.setText(getString(R.string.order_pay_type,
                        getString(R.string.cod_pay)));
                break;
            case PayType.Balance_Pay:
                mPayTypeText.setText(getString(R.string.order_pay_type,
                        getString(R.string.balance_pay)));
                break;
            case PayType.Bank_Pay:
                mPayTypeText.setText(getString(R.string.order_pay_type,
                        getString(R.string.bank_pay)));
                break;
            case PayType.Ali_Wap_Pay:
                mPayTypeText.setText(getString(R.string.order_pay_type,
                        getString(R.string.ali_wap_pay)));
                break;
            case PayType.Weixin_Wap_Pay:
                mPayTypeText.setText(getString(R.string.order_pay_type,
                        getString(R.string.weixin_wap_pay)));
                break;
            case PayType.Balance_Wap_Pay:
                mPayTypeText.setText(getString(R.string.order_pay_type,
                        getString(R.string.balance_wap_pay)));
                break;
            default:
                mPayTypeText.setVisibility(View.GONE);
                break;
        }
    }

    private void showAddressInfo(AddressModel model)
    {
        if (model != null)
        {
            DistrictModel distModel = model.getDistrict();
            if (distModel != null)
            {
                mUserNameText.setText(getString(
                        R.string.linkman, model.getName()));
                mMobileText.setText(model.getMobile());
                mAddressText.setText(distModel
                        .getProvince()
                        + distModel.getCity()
                        + distModel.getName()
                        + model.getAddress());
            }

            CommunityModel communityModel = model
                    .getCommunity();
            if (communityModel != null)
            {
                mUserNameText.setText(getString(
                        R.string.linkman,
                        model.getDelivery_name()));
                mMobileText.setText(model.getMobile());
                mAddressText.setText(communityModel
                        .getProvince()
                        + communityModel.getCity()
                        + communityModel.getDistrict()
                        + communityModel.getAddress());
            }
        }
    }

    @OnClick(R.id.cancelOrderText)
    public void onCancelOrderClick()
    {
        tryToCancelPayIngOrder();

        new CustomDialog.Builder(mContext)
                .setTitle(R.string.general_tips)
                .setMessage(R.string.cancel_order_confirm)
                .setPositiveButton(R.string.general_confirm,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                doTradingCancelOrderRequest(mOrderIdList);
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(R.string.general_cancel,
                        new DialogInterface.OnClickListener()
                        {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                dialog.dismiss();
                            }

                        }).create().show();
    }

    @OnClick(R.id.payOrderText)
    public void onPayOrderClick()
    {
        tryToCancelPayIngOrder();
        UIHelper.showPayTypeActivity(TradingOrderDetailActivity.this,
                mOrderDetailModel);
    }

    private void tryToCancelPayIngOrder()
    {
        if(mOrderDetailModel != null && mOrderDetailModel.getStatus() == OrderStatusType.PAY_ING)
        {
            new PayHelper(TradingOrderDetailActivity.this, null).cancelPayOrder(mOrderDetailModel.getPay_type(), mOrderDetailModel.getPay_id());
        }
    }

    private void doTradingOrderDetailRequest(String orderId)
    {
        TradingOrderDetailRequest request = new TradingOrderDetailRequest(
                orderId, new ResponseEventHandler<RespOrderModel>()
        {
            @Override
            public void onResponseSucess(
                    BaseRequest<RespOrderModel> request,
                    RespOrderModel result)
            {
                if (result.getStatus() == ErrorCode.SUCCESS)
                {
                    mOrderDetailModel = result.getData();
                    updateView(mOrderDetailModel);
                    hideLoadingView();
                }
                else
                {
                    showLoadFailView(result.getMsg());
                }
            }

            @Override
            public void onResponseError(VolleyError error)
            {
                ToastUtil.showToastShort(error.getMessage());
                showLoadFailView(error.getMessage());
            }

        });
        executeRequest(request);
    }

    private void doTradingAddressRequest(String addressId)
    {
        TradingAddressRequest request = new TradingAddressRequest(addressId,
                new ResponseEventHandler<RespAddressModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespAddressModel> request,
                            RespAddressModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            showAddressInfo(result.getData());
                        }
                        else
                        {

                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                    }

                });
        executeRequest(request);
    }

    private void doTradingCancelOrderRequest(ArrayList<String> orderList)
    {
        TradingCancelOrderRequest request = new TradingCancelOrderRequest(
                orderList, new ResponseEventHandler<BaseModel>()
        {
            @Override
            public void onResponseSucess(
                    BaseRequest<BaseModel> request, BaseModel result)
            {
                if (result.getStatus() == ErrorCode.SUCCESS)
                {
                    EventBus.getDefault().post(new Event.PayEvent(EventType.CANCEL_ORDER_EVENT, ErrorCode.SUCCESS, null));

                    ToastUtil
                            .showToastShort(R.string.cancel_order_success);
                    setResult(RESULT_OK);
                    finish();
                }
                else
                {
                    ToastUtil.showToastShort(result.getMsg());
                }
            }

            @Override
            public void onResponseError(VolleyError error)
            {
                ToastUtil.showToastShort(error.getMessage());
            }
        });
        executeRequest(request);
    }

    private List<TradingBaseModel> transferDataModel(List<GoodItemModel> goods)
    {
        List<TradingBaseModel> tradingsModel = new ArrayList<TradingBaseModel>();

        for (GoodItemModel goodModel : goods)
        {
            TradingBaseModel tradingModel = new TradingBaseModel();

            tradingModel
                    .setAttachments(goodModel.getTrading().getAttachments());
            tradingModel.setDiscount_price(goodModel.getPrice()); // display buy
            // price
            tradingModel.setNum(goodModel.getTrading().getNum());
            tradingModel.setTitle(goodModel.getTrading().getTitle());
            tradingModel.setPrice(goodModel.getTrading().getPrice());
            tradingModel.set_id(goodModel.getTrading().get_id());
            tradingModel.setAuthor_uid(goodModel.getTrading().getAuthor_uid());
            tradingModel.setBuyNum(goodModel.getNum());
            tradingsModel.add(tradingModel);
        }

        return tradingsModel;
    }

    public void onEventMainThread(Event.PayEvent event)
    {
        this.initData();
    }
}
