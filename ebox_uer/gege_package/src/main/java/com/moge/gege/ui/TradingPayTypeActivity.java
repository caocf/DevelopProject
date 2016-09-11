package com.moge.gege.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.data.PersistentData;
import com.moge.gege.data.dao.ShopcartDAO;
import com.moge.gege.model.*;
import com.moge.gege.model.enums.EventType;
import com.moge.gege.model.enums.PayResultType;
import com.moge.gege.model.enums.PayType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.TradingAddressListRequest;
import com.moge.gege.network.request.TradingCancelOrderRequest;
import com.moge.gege.network.request.TradingDeliveryFeeRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.PayTypeListAdapter;
import com.moge.gege.ui.adapter.TradingDetailListAdapter;
import com.moge.gege.ui.adapter.TradingDetailListAdapter.ShoppingCartMode;
import com.moge.gege.ui.adapter.TradingDetailListAdapter.TradingDetailListListener;
import com.moge.gege.ui.event.Event;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.ui.widget.BalancePayDialog;
import com.moge.gege.util.FunctionUtil;
import com.moge.gege.util.FunctionUtils;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.pay.BalancePayController;
import com.moge.gege.util.pay.PayHelper;
import com.moge.gege.util.pay.PayHelper.PayHelperResultLinstener;
import com.moge.gege.util.pay.WXPayHelper;
import com.moge.gege.util.widget.CustomDialog;
import com.moge.gege.util.widget.MyListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;

public class TradingPayTypeActivity extends BaseActivity implements PayHelperResultLinstener,BalancePayController.BalancePayListener
{
    private Context mContext;

    @InjectView(R.id.payTypeList) ListView mPayTypeList;
    @InjectView(R.id.cancelOrderText) TextView mCancelOrderText;
    @InjectView(R.id.payOrderText) TextView mPayOrderText;

    private int mPayType = PayType.Ali_Pay;
    private PayTypeListAdapter mAdapter;

    private PayHelper mPayHelper;
    private OrderModel mOneOrderModel;
    private MerchantListModel mMoreOrderModel;
    private ArrayList<String> mOrderIdList = new ArrayList<String>();
    private PayResultModel mPayOrderModel;
    private boolean mNeedCancelOrderBeforyPay = false;
    private BalancePayController mBalancePayController;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradingpaytype);
        ButterKnife.inject(this);

        mOneOrderModel = (OrderModel)getIntent().getSerializableExtra("one_order");
        mMoreOrderModel = (MerchantListModel)getIntent().getSerializableExtra("more_order");

        mContext = TradingPayTypeActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(R.string.pay_type);

        mAdapter = new PayTypeListAdapter(mContext);
        mPayTypeList.setAdapter(mAdapter);
    }

    private void initData()
    {
        // init listview datasource
        List<BaseOptionModel> payList = new ArrayList<BaseOptionModel>();

        BaseOptionModel aliPayModel = new BaseOptionModel();
        aliPayModel.setName(getString(R.string.ali_pay));
        aliPayModel.setResId(R.drawable.icon_ali_pay);
        aliPayModel.set_id(String.valueOf(PayType.Ali_Pay));
        payList.add(aliPayModel);

        BaseOptionModel wxPayModel = new BaseOptionModel();
        wxPayModel.setName(getString(R.string.weixin_pay));
        wxPayModel.setResId(R.drawable.icon_weixin_pay);
        wxPayModel.set_id(String.valueOf(PayType.Weixin_Pay));
        payList.add(wxPayModel);

        BaseOptionModel codPayModel = new BaseOptionModel();
        codPayModel.setName(getString(R.string.cod_pay));
        codPayModel.setResId(R.drawable.icon_cod_pay);
        codPayModel.set_id(String.valueOf(PayType.Cod_Pay));
        payList.add(codPayModel);

        BaseOptionModel balancePayModel = new BaseOptionModel();
        balancePayModel.setName(getString(R.string.balance_pay));
        balancePayModel.setResId(R.drawable.icon_balance_pay);
        balancePayModel.set_id(String.valueOf(PayType.Balance_Pay));
        payList.add(balancePayModel);

        mAdapter.addAll(payList);
        mAdapter.notifyDataSetChanged();

        //  get order id list
        if(mOneOrderModel != null)
        {
            mOrderIdList.add(mOneOrderModel.getOrder_id());
        }

        if(mMoreOrderModel != null)
        {
            for(MerchantModel merchant : mMoreOrderModel.getMerchants())
            {
                mOrderIdList.add(merchant.getOrder_id());
            }
        }
    }

    @Override
    protected void onHeaderLeftClick()
    {
        if(mOneOrderModel != null) {
            finish();
            return;
        }

        new CustomDialog.Builder(mContext)
                .setTitle(R.string.general_tips)
                .setMessage(R.string.quit_pay_confirm)
                .setPositiveButton(R.string.general_confirm,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                finish();
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(R.string.general_cancel,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }

                        }).create().show();
    }

    @OnItemClick(R.id.payTypeList)
    public void onItemClick(int position)
    {
        mAdapter.setSelectedIndex(position);

        BaseOptionModel model = mAdapter.getItem(position);
        mPayType = Integer.parseInt(model.get_id());
    }

    @OnClick(R.id.cancelOrderText)
    public void onCancelOrderClick()
    {
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
        if(FunctionUtil.isFastDoubleClick())
        {
            return;
        }

        if(mPayOrderModel != null)
        {
            mNeedCancelOrderBeforyPay = true;
            mPayHelper.cancelPayOrder(mPayOrderModel.getPay_type(), mPayOrderModel.getPay_id());
            return;
        }

        payOrder();
    }

    private void payOrder()
    {
        if(mPayType == PayType.Weixin_Pay && !WXPayHelper.checkValid(mContext))
        {
            return;
        }

        if (mPayHelper == null)
        {
            mPayHelper = new PayHelper(TradingPayTypeActivity.this, this);
        }

        if(mOneOrderModel != null)
        {
            mPayHelper.payForOneOrder(mPayType, mOneOrderModel);
        }

        if(mMoreOrderModel != null)
        {
            mPayHelper.payForMoreOrder(mPayType, mMoreOrderModel);
        }

        mPayOrderText.setEnabled(false);
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
                    // notify listener
                    if(mMoreOrderModel != null)
                    {
                        UIHelper.showHomePageActivity(mContext);
                    }
                    else
                    {
                        EventBus.getDefault().post(new Event.PayEvent(EventType.CANCEL_ORDER_EVENT, ErrorCode.SUCCESS, null));
                    }

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

    @Override
    public void onCreateOrderResult(int result, MerchantListModel orderListModel)
    {

    }

    @Override
    public void onCreatePayOrder(PayResultModel payOrderModel)
    {
        mPayOrderModel = payOrderModel;

        if(payOrderModel.getPay_type() == PayType.Balance_Pay)
        {
            mBalancePayController = new BalancePayController(mContext, payOrderModel.getPay_id(), payOrderModel.getOrder_info().getCode_mobile(), this).startWork();
        }
    }

    @Override
    public void onCancelPayOrder(int result)
    {
        mPayOrderModel = null;
        mPayOrderText.setEnabled(true);

        if(mNeedCancelOrderBeforyPay)
        {
            mNeedCancelOrderBeforyPay = !mNeedCancelOrderBeforyPay;
            payOrder();
        }
    }

    @Override
    public void onPayResult(int result, ArrayList<String> orderList)
    {

        if (result == PayResultType.PAY_SUCCESS)
        {
            if(mMoreOrderModel != null)
            {
                UIHelper.showPayResultActivity(mContext, mOrderIdList, PayResultType.PAY_SUCCESS);
            }
            else
            {
                EventBus.getDefault().post(new Event.PayEvent(EventType.PAY_ORDER_EVENT, ErrorCode.SUCCESS, null));
            }

            if(mPayOrderModel.getPay_type() == PayType.Balance_Pay)
            {
                EventBus.getDefault().post(new Event.BalanceEvent());
            }

            ToastUtil.showToastShort(R.string.pay_success);
            setResult(RESULT_OK);
            finish();
        }

        mPayOrderModel = null;
        mPayOrderText.setEnabled(true);
    }

    @Override
    public void onToPayWithBalance(String payId, String code)
    {
        mPayHelper.payForBalanceOrder(payId, code);
    }

    @Override
    public void onToCancelBalancePay(String payId)
    {
        mPayHelper.cancelPayOrder(PayType.Balance_Pay, payId);
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            this.onHeaderLeftClick();
        }

        return false;
    }

    @Override
    protected  void onDestroy()
    {
        super.onDestroy();

        if(mBalancePayController != null)
        {
            mBalancePayController.finishWork();
        }
    }

    @Override
    protected  void onResume()
    {
        super.onResume();

        mPayOrderText.setEnabled(true);
    }
}
