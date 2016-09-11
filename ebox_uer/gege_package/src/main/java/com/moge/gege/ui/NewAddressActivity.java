package com.moge.gege.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.AddressModel;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.DistrictModel;
import com.moge.gege.model.RespAddressItemModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.TradingAddressAddRequest;
import com.moge.gege.network.request.TradingAddressEditRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.util.LogonUtils;
import com.moge.gege.util.ToastUtil;

public class NewAddressActivity extends BaseActivity implements OnTouchListener
{
    private Context mContext;
    private EditText mNameEdit;
    private EditText mPhoneEdit;
    private TextView mDistrictText;
    private EditText mDetailAddressEdit;

    private DistrictModel mDistrictModel;

    // external params
    private AddressModel mEditAddressModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newaddress);

        mEditAddressModel = (AddressModel)getIntent().getSerializableExtra("address");

        mContext = NewAddressActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(R.string.add_new_address);
        this.setHeaderRightTitle(R.string.finish);

        mNameEdit = (EditText) this.findViewById(R.id.nameEdit);
        mPhoneEdit = (EditText) this.findViewById(R.id.phoneEdit);
        mDetailAddressEdit = (EditText) this
                .findViewById(R.id.detailAddressEdit);
        mDistrictText = (TextView) this.findViewById(R.id.districtText);
        mDistrictText.setOnTouchListener(this);

        if(mEditAddressModel != null){

            mDistrictModel = mEditAddressModel.getDistrict();
            mNameEdit.setText(mEditAddressModel.getName());
            mPhoneEdit.setText(mEditAddressModel.getMobile());
            mDistrictText.setText(mDistrictModel.getProvince()
                    + mDistrictModel.getCity()
                    + mDistrictModel.getName());
            mDetailAddressEdit.setText(mEditAddressModel.getAddress());
        }
    }

    @Override
    protected void onHeaderRightClick()
    {
        onAddNewAddressAction();
    }

    private void initData()
    {

    }

    private void onAddNewAddressAction()
    {
        String name = mNameEdit.getText().toString().trim();
        if (TextUtils.isEmpty(name))
        {
            ToastUtil.showToastShort(R.string.please_input_consignee);
            return;
        }

        String phone = mPhoneEdit.getText().toString().trim();
        if (TextUtils.isEmpty(phone))
        {
            ToastUtil.showToastShort(R.string.mobile_empty);
            return;
        }

        if (!LogonUtils.matcherLogonId(phone))
        {
            ToastUtil.showToastShort(R.string.mobile_format_error);
            return;
        }

        if (mDistrictModel == null)
        {
            ToastUtil.showToastShort(R.string.please_select_district);
            return;
        }

        String detailAddress = mDetailAddressEdit.getText().toString().trim();
        if (TextUtils.isEmpty(detailAddress))
        {
            ToastUtil.showToastShort(R.string.please_input_detail_address);
            return;
        }

        if(mEditAddressModel != null){
            doTradingAddressEditRequest(mEditAddressModel.get_id(), mDistrictModel.getPid(),
                    mDistrictModel.getCid(), mDistrictModel.get_id(), name, phone,
                    detailAddress);
        } else {
            doTradingAddressRequest(mDistrictModel.getPid(),
                    mDistrictModel.getCid(), mDistrictModel.get_id(), name, phone,
                    detailAddress);
        }

    }

    private void doTradingAddressRequest(int pid, int cid, int did,
            String name, String mobile, String address)
    {
        TradingAddressAddRequest request = new TradingAddressAddRequest(pid,
                cid, did, name, mobile, address,
                new ResponseEventHandler<RespAddressItemModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespAddressItemModel> request,
                            RespAddressItemModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            Intent intent = new Intent();
                            intent.putExtra("address", result.getData()
                                    .getAddress());
                            setResult(RESULT_OK, intent);
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

    private void doTradingAddressEditRequest(String addressId, int pid, int cid, int did,
                                         String name, String mobile, String address)
    {
        TradingAddressEditRequest request = new TradingAddressEditRequest(addressId, pid,
                cid, did, name, mobile, address,
                new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request,
                            BaseModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK)
        {
            return;
        }

        switch (requestCode)
        {
            case GlobalConfig.INTENT_SELECT_DISTRICT:
                if (data != null)
                {
                    mDistrictModel = (DistrictModel) data
                            .getSerializableExtra("district");
                    mDistrictText.setText(mDistrictModel.getProvince()
                            + mDistrictModel.getCity()
                            + mDistrictModel.getName());
                }

                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        Intent intent = new Intent(mContext, SelectDistrictActivity.class);
        startActivityForResult(intent, GlobalConfig.INTENT_SELECT_DISTRICT);
        return false;
    }
}
