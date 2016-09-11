package com.moge.gege.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.enums.MyOperateType;
import com.moge.gege.model.enums.PayResultType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.TradingCancelOrderRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TradingPayResultActivity extends BaseActivity implements
        OnClickListener
{
    private Context mContext;
    private ArrayList<String> mOrderlist;
    private int mResult;

    @InjectView(R.id.resultImage) ImageView mResultImage;
    @InjectView(R.id.resultText) TextView mResultText;
    @InjectView(R.id.leftBtn) Button mLeftBtn;
    @InjectView(R.id.rightBtn) Button mRightBtn;

    public static final String TAG = "TradingPayResultActivity";

    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradingpayresult);
        ButterKnife.inject(this);

        mResult = getIntent().getIntExtra("result", PayResultType.PAY_FAILED);
        mOrderlist = getIntent().getStringArrayListExtra("orderlist");

        mContext = TradingPayResultActivity.this;
        initView();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftTitle(R.string.pay_result);
        this.setHeaderLeftImage(R.drawable.icon_back);

        mLeftBtn.setOnClickListener(this);
        mRightBtn.setOnClickListener(this);

        if (mResult != PayResultType.PAY_SUCCESS)
        {
            mResultImage.setImageResource(R.drawable.icon_pay_fail);
            mResultText.setText(getString(R.string.pay_fail));
            mResultText.setTextColor(mContext.getResources().getColor(
                    R.color.pay_fail_text_color));
            mLeftBtn.setText(getString(R.string.cancel_order));
            mRightBtn.setText(getString(R.string.pay_later));
        }
        else
        {
            mResultImage.setImageResource(R.drawable.icon_pay_success);
            mResultText.setText(getString(R.string.pay_success));
            mResultText.setTextColor(mContext.getResources().getColor(
                    R.color.pay_success_text_color));
            mLeftBtn.setText(getString(R.string.query_order));
            mRightBtn.setText(getString(R.string.go_shopping));
        }
    }

    @Override
    protected void onHeaderLeftClick()
    {
        gotoHomePage();
    }

    @Override
    public void onClick(final View v)
    {

        switch (v.getId())
        {
            case R.id.leftBtn:
                if (mResult != PayResultType.PAY_SUCCESS)
                {
                    // 取消订单
                    doTradingCancelOrderRequest(mOrderlist);
                }
                else
                {
                    // 查看订单
                    Intent intent = new Intent(mContext,
                            MyRelatedActivity.class);
                    intent.putExtra("operateType", MyOperateType.MY_PRUCHASE);
                    intent.putExtra("from", TAG);
                    startActivity(intent);
                }
                break;
            case R.id.rightBtn:
                if (mResult != PayResultType.PAY_SUCCESS)
                {
                    // 稍后支付
                }
                else
                {
                    // 继续逛逛
                }
                gotoHomePage();
                break;
            default:
                break;
        }

        // final Intent intent = new Intent();
        // if (v.getId() == R.id.left)
        // {
        // intent.putExtra("payid", mPayId);
        // }
        // else
        // {
        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // intent.setClass(mContext, HomeActivity.class);
        // }
        // startActivity(intent);
        // finish();
    }

    private void gotoHomePage()
    {
        final Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(mContext, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            onHeaderLeftClick();
        }
        return false;
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
                            gotoHomePage();
                            ToastUtil
                                    .showToastShort(R.string.cancel_order_success);
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

}
