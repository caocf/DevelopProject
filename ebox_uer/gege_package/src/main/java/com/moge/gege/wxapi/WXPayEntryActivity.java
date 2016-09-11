package com.moge.gege.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.ui.BaseActivity;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.pay.WXPayHelper;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends BaseActivity implements
        IWXAPIEventHandler
{
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, GlobalConfig.WXAppID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req)
    {
    }

    @Override
    public void onResp(BaseResp resp)
    {
        LogUtil.d("onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX)
        {
            if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL)
            {
                ToastUtil.showToastShort(R.string.pay_cancel);
            }
            else if (resp.errCode != BaseResp.ErrCode.ERR_OK)
            {
                ToastUtil.showToastShort(getString(R.string.pay_failed) + "["
                        + resp.errCode + "]" + resp.errStr);
            }

            PayResp payResp = (PayResp) resp;
            WXPayHelper.finishPayOrder(this, payResp.extData, payResp);

            finish();
        }
    }
}