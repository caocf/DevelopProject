package com.moge.gege.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.model.BaseOptionModel;
import com.moge.gege.model.MerchantListModel;
import com.moge.gege.model.OrderModel;
import com.moge.gege.model.PayResultModel;
import com.moge.gege.model.RechargeResultModel;
import com.moge.gege.model.RespRechargeResultModel;
import com.moge.gege.model.enums.PayResultType;
import com.moge.gege.model.enums.PayType;
import com.moge.gege.model.enums.RechargeType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.RechargeRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.PayTypeListAdapter;
import com.moge.gege.ui.event.Event;
import com.moge.gege.util.FunctionUtil;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.UmengUtil;
import com.moge.gege.util.pay.PayHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;

public class RechargeActivity extends BaseActivity implements PayHelper.PayHelperResultLinstener {

    @InjectView(R.id.payTypeList)
    ListView mPayTypeList;
    @InjectView(R.id.moneyEdit)
    EditText mMoneyEdit;
    @InjectView(R.id.cardCodeEdit)
    EditText mCardCodeEdit;
    @InjectView(R.id.cardPwdEdit)
    EditText mCardPwdEdit;
    @InjectView(R.id.cardLayout)
    LinearLayout mCardLayout;
    @InjectView(R.id.rechargeBtn)
    Button mRechargeBtn;

    private Context mContext;
    private PayTypeListAdapter mAdapter;
    private boolean mOnlineCharge;
    private int mPayType = PayType.Ali_Pay;
    private int mRechargeFee = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.inject(this);

        mContext = RechargeActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView() {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(R.string.balance_recharge);

        mAdapter = new PayTypeListAdapter(mContext);
        mPayTypeList.setAdapter(mAdapter);
    }

    private void initData()
    {
        List<BaseOptionModel> payList = new ArrayList<BaseOptionModel>();

        BaseOptionModel aliPayModel = new BaseOptionModel();
        aliPayModel.setName(getString(R.string.ali_recharge));
        aliPayModel.setResId(R.drawable.icon_ali_pay);
        aliPayModel.set_id(String.valueOf(PayType.Ali_Pay));
        payList.add(aliPayModel);

        BaseOptionModel wxPayModel = new BaseOptionModel();
        wxPayModel.setName(getString(R.string.wx_recharge));
        wxPayModel.setResId(R.drawable.icon_weixin_pay);
        wxPayModel.set_id(String.valueOf(PayType.Weixin_Pay));
        payList.add(wxPayModel);

        BaseOptionModel cardPayModel = new BaseOptionModel();
        cardPayModel.setName(getString(R.string.card_recharge));
        cardPayModel.setResId(R.drawable.icon_balance_pay);
        cardPayModel.set_id(String.valueOf(PayType.Card_Pay));
        payList.add(cardPayModel);

        mAdapter.addAll(payList);
        mAdapter.notifyDataSetChanged();
        mOnlineCharge = true;
    }

    @OnItemClick(R.id.payTypeList)
    public void onItemClick(int position)
    {
        mAdapter.setSelectedIndex(position);

        BaseOptionModel model = mAdapter.getItem(position);
        if(model.get_id() == String.valueOf(PayType.Card_Pay))
        {
            mMoneyEdit.setVisibility(View.GONE);
            mCardLayout.setVisibility(View.VISIBLE);
            mMoneyEdit.setText("");
            mOnlineCharge = false;
        }
        else
        {
            mMoneyEdit.setVisibility(View.VISIBLE);
            mCardLayout.setVisibility(View.GONE);
            mCardCodeEdit.setText("");
            mCardPwdEdit.setText("");
            mOnlineCharge = true;
        }

        mPayType = Integer.parseInt(model.get_id());
    }


    @OnClick(R.id.rechargeBtn)
    public void onRechargeBtnClick()
    {
        if(FunctionUtil.isFastDoubleClick())
        {
            return;
        }

        double fee = FunctionUtil.parseDoubleByString(mMoneyEdit.getText().toString());
        String cardCode = mCardCodeEdit.getText().toString().trim();
        String cardPwd = mCardPwdEdit.getText().toString().trim();

        if(mOnlineCharge && fee < 0.01)
        {
            ToastUtil.showToastShort(R.string.invalid_money);
            return;
        }

        if(!mOnlineCharge)
        {
            if(TextUtils.isEmpty(cardCode))
            {
                ToastUtil.showToastShort(R.string.invalid_cardno);
                return;
            }
            else if(TextUtils.isEmpty(cardPwd))
            {
                ToastUtil.showToastShort(R.string.invalid_cardpwd);
                return;
            }
        }

        mRechargeBtn.setEnabled(false);
        doRecharge((int) (fee * 100), cardCode, cardPwd);
    }


    private void doRecharge(int fee, String cardCode, String cardPwd)
    {
        RechargeRequest request = new RechargeRequest(fee, cardCode, cardPwd, new ResponseEventHandler<RespRechargeResultModel>()
        {
            @Override
            public void onResponseSucess(
                    BaseRequest<RespRechargeResultModel> request,
                    RespRechargeResultModel result)
            {
                if (result.getStatus() == ErrorCode.SUCCESS)
                {
                    RechargeResultModel model = result.getData();

                    mRechargeFee = model.getFee();

                    if(!TextUtils.isEmpty(model.getOrder_id()))
                    {
                        OrderModel orderModel = new OrderModel();
                        orderModel.setOrder_id(model.getOrder_id());
                        PayHelper payHelper = new PayHelper(RechargeActivity.this, RechargeActivity.this);
                        payHelper.payForRechargeOrder(mPayType, model.getOrder_id(), model.getFee());
                    }
                    else
                    {
                        onPayResult(PayResultType.PAY_SUCCESS, null);
                    }
                }
                else
                {
                    ToastUtil.showToastShort(result.getMsg());
                    mRechargeBtn.setEnabled(true);
                }
            }

            @Override
            public void onResponseError(VolleyError error)
            {
                ToastUtil.showToastShort(error.getMessage());
                mRechargeBtn.setEnabled(true);
            }
        });

        executeRequest(request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            default:
                break;
        }
    }

    @Override
    public void onCreateOrderResult(int result, MerchantListModel orderListModel)
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
    public void onPayResult(int result, ArrayList<String> orderList)
    {
        mRechargeBtn.setEnabled(true);

        if (result == PayResultType.PAY_SUCCESS)
        {
            ToastUtil.showToastShort(R.string.recharge_success);
            EventBus.getDefault().post(new Event.BalanceEvent());
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        mRechargeBtn.setEnabled(true);
    }

}
