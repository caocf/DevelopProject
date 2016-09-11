package com.moge.gege.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.data.PersistentData;
import com.moge.gege.data.dao.ShopcartDAO;
import com.moge.gege.model.AddressListModel;
import com.moge.gege.model.AddressModel;
import com.moge.gege.model.MerchantListModel;
import com.moge.gege.model.PayResultModel;
import com.moge.gege.model.RespAddressListModel;
import com.moge.gege.model.RespDeliveryModel;
import com.moge.gege.model.TradingBaseModel;
import com.moge.gege.model.TradingDetailModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.TradingAddressListRequest;
import com.moge.gege.network.request.TradingDeliveryFeeRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.TradingDetailListAdapter;
import com.moge.gege.ui.adapter.TradingDetailListAdapter.ShoppingCartMode;
import com.moge.gege.ui.adapter.TradingDetailListAdapter.TradingDetailListListener;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.FunctionUtil;
import com.moge.gege.util.FunctionUtils;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.pay.PayHelper;
import com.moge.gege.util.pay.PayHelper.PayHelperResultLinstener;
import com.moge.gege.util.widget.MyListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TradingPayActivity extends BaseActivity implements
        OnClickListener, TradingDetailListListener, PayHelperResultLinstener
{
    private Context mContext;

    @InjectView(R.id.goodsList) MyListView mGoodsListView;
    private TradingDetailListAdapter mAdapter;

    @InjectView(R.id.selectAddressLayout) LinearLayout mSelectAddressLayout;
    @InjectView(R.id.addressLayout) RelativeLayout mAddressLayout;
    @InjectView(R.id.addressText) TextView mAddressText;
    @InjectView(R.id.addressDetailText) TextView mAddressDetailText;
    @InjectView(R.id.nameText) TextView mNameText;
    @InjectView(R.id.mobileText) TextView mMobileText;
    @InjectView(R.id.addNewAddressText) TextView mAddNewAddressText;

    @InjectView(R.id.couponLayout) RelativeLayout mCouponLayout;
    @InjectView(R.id.couponText) TextView mCouponText;
    @InjectView(R.id.shippingFeeTitleText) TextView mShippingFeeTitleText;
    @InjectView(R.id.shippingFeeText) TextView mShippingFeeText;
    @InjectView(R.id.couponFeeText) TextView mCouponFeeText;

    @InjectView(R.id.totalFeeText) TextView mTotalFeeText;
    @InjectView(R.id.payFeeText) TextView mPayFeeText;
    @InjectView(R.id.payBtn) Button mPayBtn;

    @InjectView(R.id.remarkEdit) EditText mRemarkEdit;
    @InjectView(R.id.updownImage) ImageView mUpDownImage;
    @InjectView(R.id.remarkLayout) RelativeLayout mRemarkLayout;

    private ArrayList<TradingBaseModel> mListGoods = new ArrayList<TradingBaseModel>();
    private ArrayList<TradingBaseModel> mListSelectedGoods = new ArrayList<TradingBaseModel>();
    private AddressModel mAddressModel;

    private PayHelper mPayHelper;

    private int mTotalBuyNum;
    private double mTotalMoney;
    private double mPayMoney;
    private int mShippingFee = 0;
    private boolean mFromShoppingCart = false;
    private String mUserCouponId = "";
    private String mCardCouponId = "";
    private String mCouponId = "";
    private String mCouponCode = "";
    private int mCouponFee = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradingpay);
        ButterKnife.inject(this);

        // receive external params
        TradingDetailModel model = (TradingDetailModel) getIntent()
                .getSerializableExtra("goods");
        if (model != null)
        {
            mFromShoppingCart = false;
            mListGoods.add(model);
        }

        ArrayList<TradingBaseModel> goodsList = (ArrayList<TradingBaseModel>) getIntent()
                .getSerializableExtra("goods_list");
        if (goodsList != null)
        {
            mFromShoppingCart = true;
            mListGoods.addAll(goodsList);
        }

        mContext = TradingPayActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(R.string.commit_pay);
//        this.setHeaderRightTitle(R.string.edit);

        mAdapter = new TradingDetailListAdapter(this);
        mAdapter.setListener(this);
        mAdapter.setShowCheckBox(false);
        mAdapter.setUpdateLocalData(mFromShoppingCart);
        mGoodsListView.setAdapter(mAdapter);

        mSelectAddressLayout.setOnClickListener(this);
        mPayBtn.setOnClickListener(this);
        mRemarkLayout.setOnClickListener(this);

        // default shipping fee
        mShippingFeeText.setText(getString(R.string.money,
                FunctionUtils
                        .getDouble(mShippingFee * 1.0 / 100)));

        mCouponLayout.setOnClickListener(this);
        showCouponInfo();
    }

    private void initData()
    {
        mAdapter.addAll(mListGoods);
        mAdapter.notifyDataSetChanged();

        showLoadingView();
        updateTotalInfo(mListGoods);

        mAddressModel = PersistentData.instance().getDeliveryAddress(AppApplication.getLoginId());
        if(mAddressModel == null)
        {
            doTradingAddressRequest();
        }
        else
        {
            showUserAddressInfo(mAddressModel);
            queryDeliveryFee();
        }
    }

    @Override
    protected void onHeaderRightClick()
    {
//        if (mAdapter.getMode() == ShoppingCartMode.Normal)
//        {
//            this.setHeaderRightTitle(R.string.finish);
//            mAdapter.setMode(ShoppingCartMode.Eidt);
//
//            mPayBtn.setEnabled(false);
//        }
//        else
//        {
//            this.setHeaderRightTitle(R.string.edit);
//            mAdapter.setMode(ShoppingCartMode.Normal);
//
//            mPayBtn.setEnabled(mTotalBuyNum > 0 ? true : false);
//        }
//
//        mAdapter.notifyDataSetChanged();
    }

    private void updateTotalInfo(List<TradingBaseModel> listModel)
    {
        mTotalMoney = 0;
        mTotalBuyNum = 0;
        mListSelectedGoods.clear();

        for (TradingBaseModel model : listModel)
        {
            if (model.isSelected())
            {
                mTotalMoney += model.getDiscount_price() * model.getBuyNum();
                mTotalBuyNum += model.getBuyNum();
                mListSelectedGoods.add(model);
            }
        }

        mTotalFeeText.setText(getString(R.string.money,
                FunctionUtils.getDouble(mTotalMoney * 1.0 / 100)));
        if (mAdapter.getMode() == ShoppingCartMode.Normal)
        {
            mPayBtn.setEnabled(mTotalBuyNum > 0 ? true : false);
        }

        updatePayFee();
    }

    private void updatePayFee()
    {
        mPayMoney = mTotalMoney + mShippingFee - mCouponFee;

        if (mPayMoney < 0)
        {
            mPayMoney = 0;
        }

        mPayFeeText.setText(getString(R.string.money,
                FunctionUtils.getDouble(mPayMoney * 1.0 / 100)));
    }

    private void showUserAddressInfo(AddressModel model)
    {
        if (model == null)
        {
            mAddNewAddressText.setVisibility(View.VISIBLE);
            mAddressLayout.setVisibility(View.GONE);
        }
        else
        {
            mAddNewAddressText.setVisibility(View.GONE);
            mAddressLayout.setVisibility(View.VISIBLE);
            if (model.getAddress_type() == 0) // 快递柜
            {
                mAddressText.setText(model.getDelivery_name());

                mAddressDetailText.setVisibility(View.GONE);
                mNameText.setVisibility(View.GONE);
                mMobileText.setVisibility(View.GONE);
            }
            else
            {
                mAddressDetailText.setVisibility(View.GONE);
                mNameText.setVisibility(View.VISIBLE);
                mMobileText.setVisibility(View.VISIBLE);

                mAddressText.setText(model.getDistrict().getProvince() +
                        model.getDistrict().getCity()
                        + model.getDistrict().getName() + model.getAddress());
//                mAddressDetailText.setText(model.getAddress());
                mNameText.setText(getString(R.string.linkman, model.getName()));
                mMobileText.setText(model.getMobile());
            }
        }
    }

    private void showCouponInfo()
    {
        if (!TextUtils.isEmpty(mCouponId))
        {
            mCouponText.setText(getString(R.string.coupon_fee,
                    FunctionUtils.getDouble(mCouponFee * 1.0 / 100)));
            mCouponFeeText.setText(getString(R.string.money,
                    FunctionUtils.getDouble(mCouponFee * 1.0 / 100)));
        }
        else
        {
            mCouponText.setText(R.string.please_select_coupon);
            mCouponFeeText.setText(getString(R.string.money,
                    FunctionUtils.getDouble(0.0)));
        }

        updatePayFee();
    }

    private void doTradingAddressRequest()
    {
        TradingAddressListRequest request = new TradingAddressListRequest(
                new ResponseEventHandler<RespAddressListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespAddressListModel> request,
                            RespAddressListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            AddressListModel listModel = result.getData();
                            if (listModel != null
                                    && listModel.getAddress() != null
                                    && listModel.getAddress().size() > 0)
                            {
                                mAddressModel = listModel.getAddress().get(0);
                                PersistentData.instance().setDeliveryAddress(AppApplication.getLoginId(), mAddressModel);
                                showUserAddressInfo(mAddressModel);

                                // query real fee
                                queryDeliveryFee();
                            }
                            else
                            {
                                hideLoadingView();
                            }
                        }
                        else
                        {
                            showLoadFailView(result.getMsg());
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                        showLoadFailView(R.string.load_failed_retry);
                    }
                });
        executeRequest(request);
    }

    private void doTradingDeliveryFeeRequest(String paramsContent)
    {
        TradingDeliveryFeeRequest request = new TradingDeliveryFeeRequest(
                paramsContent, new ResponseEventHandler<RespDeliveryModel>()
        {
            @Override
            public void onResponseSucess(
                    BaseRequest<RespDeliveryModel> request,
                    RespDeliveryModel result)
            {
                if (result.getStatus() == ErrorCode.SUCCESS)
                {
                    mShippingFee = result.getData().getFee();
                    updatePayFee();

                    hideLoadingView();
                }
                else
                {
//                    showLoadFailView(result.getMsg());
                    hideLoadingView();
                }

                mShippingFeeText.setText(getString(R.string.money,
                        FunctionUtils
                                .getDouble(mShippingFee * 1.0 / 100)));
            }

            @Override
            public void onResponseError(VolleyError error)
            {
//                showLoadFailView(error.getMessage());
                hideLoadingView();
            }

        });
        executeRequest(request);
    }

    private void queryDeliveryFee()
    {
        if (mListSelectedGoods.size() == 0 || mAddressModel == null)
        {
            return;
        }

        String paramsContent = "";
        try
        {
            JSONObject queryFeeObject = new JSONObject();
            queryFeeObject.put("address_id", mAddressModel.get_id());

            JSONArray goodsArray = new JSONArray();
            for (TradingBaseModel model : mListSelectedGoods)
            {
                JSONObject goodItemObject = new JSONObject();
                goodItemObject.put("trading_id", model.get_id());
                goodItemObject.put("num", model.getBuyNum());
                goodsArray.put(goodItemObject);
            }
            queryFeeObject.put("goods", goodsArray);

            paramsContent = queryFeeObject.toString();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return;
        }

        doTradingDeliveryFeeRequest(paramsContent);
    }

    private void onCreateOrder()
    {
        if (mAddressModel == null)
        {
            ToastUtil.showToastShort(R.string.please_complete_address);
            return;
        }

        if (mPayHelper == null)
        {
            mPayHelper = new PayHelper(TradingPayActivity.this,
                    TradingPayActivity.this);
        }

        String remark = mRemarkEdit.getText().toString();
        if (mPayHelper.createNewOrder(mUserCouponId, mCardCouponId, mCouponId,
                mAddressModel, remark, mListSelectedGoods))
        {
            mPayBtn.setEnabled(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK)
        {
            return;
        }

        switch (requestCode)
        {
            case GlobalConfig.INTENT_ADD_NEW_ADDRESS:
            case GlobalConfig.INTENT_SELECT_ADDRESS:
                if (data != null)
                {
                    mAddressModel = (AddressModel) data
                            .getSerializableExtra("address");
                    showUserAddressInfo(mAddressModel);
                    PersistentData.instance().setDeliveryAddress(AppApplication.getLoginId(), mAddressModel);
                    queryDeliveryFee();
                }
                break;
            case GlobalConfig.INTENT_SELECT_COUPON:
                if (data != null)
                {
                    mUserCouponId = data.getStringExtra("user_coupon_id");
                    mCardCouponId = data.getStringExtra("card_coupon_id");
                    mCouponId = data.getStringExtra("coupon_id");
                    mCouponCode = data.getStringExtra("coupon_code");
                    mCouponFee = data.getIntExtra("coupon_fee", 0);
                    showCouponInfo();
                }
                break;
//            case GlobalConfig.INTENT_SELECT_PAYTYPE:
//                if (data != null)
//                {
//                    mPayType = data.getIntExtra("pay_type", PayType.Ali_Pay);
//                    PersistentData.instance().setDefaultType(mPayType);
//                    showPayTypeInfo();
//                }
//                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.payBtn:
                if (FunctionUtil.isFastDoubleClick())
                {
                    return;
                }
                onCreateOrder();
                break;
            case R.id.selectAddressLayout:
                if (mAddNewAddressText.getVisibility() == View.VISIBLE)
                {
                    gotoNewAddressActivity();
                }
                else
                {
                    gotoAddressListActivity();
                }
                break;
            case R.id.addNewAddressText:
                gotoNewAddressActivity();
                break;
            case R.id.addressLayout:
                gotoAddressListActivity();
                break;
            case R.id.couponLayout:
                UIHelper.showCouponListActivity(TradingPayActivity.this,
                        mUserCouponId, mCouponCode, mCouponFee);
                break;
//            case R.id.payTypeLayout:
//                UIHelper.showPayTypeActivity(TradingPayActivity.this, mPayType);
//                break;
            case R.id.remarkLayout:
                if(mRemarkEdit.getVisibility() == View.GONE)
                {
                    mUpDownImage.setImageResource(R.drawable.icon_up);
                    mRemarkEdit.setVisibility(View.VISIBLE);
                }
                else
                {
                    mUpDownImage.setImageResource(R.drawable.icon_down);
                    mRemarkEdit.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    private void gotoNewAddressActivity()
    {
        Intent intent = new Intent(mContext, NewAddressActivity.class);
        this.startActivityForResult(intent,
                GlobalConfig.INTENT_ADD_NEW_ADDRESS);
    }

    private void gotoAddressListActivity()
    {
        Intent intent = new Intent(mContext, AddressListActivity.class);
        intent.putExtra("address_id", mAddressModel.get_id());
        this.startActivityForResult(intent, GlobalConfig.INTENT_SELECT_ADDRESS);
    }

    @Override
    public void onTradingCheckedClick()
    {
        updateTotalInfo(mListGoods);
    }

    @Override
    public void onTradingNumChange()
    {
        updateTotalInfo(mListGoods);
        queryDeliveryFee();
    }

    @Override
    public void onTradingDetailItemClick(TradingBaseModel model)
    {

    }

    @Override
    public void onCreateOrderResult(int result, MerchantListModel orderListModel)
    {
        mPayBtn.setEnabled(true);

        if(result != ErrorCode.SUCCESS)
        {
            return;
        }

        // prepare to remove goods from shoppingcart
        if (mFromShoppingCart)
        {
            ShopcartDAO.instance().clearShopcart(AppApplication.getLoginId());
        }
        UIHelper.showPayTypeActivity(TradingPayActivity.this, orderListModel);
        finish();
    }

    @Override
    public void onPayResult(int result, ArrayList<String> orderList)
    {

    }

    @Override
    public void onCreatePayOrder(PayResultModel payOrderModel)
    {

    }

    @Override
    public void onCancelPayOrder(int result)
    {

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        mPayBtn.setEnabled(true);
    }
}
