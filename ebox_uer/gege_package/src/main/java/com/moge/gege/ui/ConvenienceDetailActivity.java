package com.moge.gege.ui;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.model.ContactModel;
import com.moge.gege.model.ConvenienceModel;
import com.moge.gege.model.RespConvenienceModel;
import com.moge.gege.model.RespDialResultModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.ConvenienceDetailRequest;
import com.moge.gege.network.request.ConvenienceDialRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.util.DeviceInfoUtil;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;

public class ConvenienceDetailActivity extends BaseActivity implements
        OnClickListener
{
    private Context mContext;
    private TextView mTitleText;
    private TextView mDialCountText;
    private TextView mTimeText;
    private TextView mRangeText;
    private TextView mAddressText;
    private TextView mContentText;

    private LinearLayout mContactLayout;
    private TextView mPhoneText;
    private TextView mNameText;

    private String mConvenienceId;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conveniencedetail);

        mConvenienceId = getIntent().getStringExtra("cid");
        mName = getIntent().getStringExtra("name");
        if(TextUtils.isEmpty(mName))
        {
            mName = "";
        }

        mContext = ConvenienceDetailActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(mName);

        mTitleText = (TextView) this.findViewById(R.id.titleText);
        mDialCountText = (TextView) this.findViewById(R.id.dialCountText);
        mTimeText = (TextView) this.findViewById(R.id.timeText);
        mRangeText = (TextView) this.findViewById(R.id.rangeText);
        mAddressText = (TextView) this.findViewById(R.id.addressText);
        mContentText = (TextView) this.findViewById(R.id.contentText);
        mContactLayout = (LinearLayout) this.findViewById(R.id.contactLayout);
        mContactLayout.setOnClickListener(this);
        mPhoneText = (TextView) this.findViewById(R.id.phoneText);
        mNameText = (TextView) this.findViewById(R.id.nameText);

    }

    private void initData()
    {
        showLoadingView();
        doConvenienceDetailRequest(mConvenienceId);
    }

    private void doConvenienceDetailRequest(String id)
    {
        ConvenienceDetailRequest request = new ConvenienceDetailRequest(id,
                new ResponseEventHandler<RespConvenienceModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespConvenienceModel> request,
                            RespConvenienceModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            ConvenienceModel model = result.getData();
                            if (model != null)
                            {
                                mTitleText.setText(model.getTitle());
                                mDialCountText.setText(model.getDial_count()
                                        + getString(R.string.dialcount_unit));
                                mTimeText.setText(" "
                                        + model.getBusiness_time());
                                mRangeText.setText(" "
                                        + model.getBusiness_district());
                                mAddressText.setText(" " + model.getAddress());
                                mContentText.setText(" " + model.getContent());

                                List<ContactModel> listContact = model
                                        .getContacts();
                                if (listContact != null
                                        && listContact.size() > 0)
                                {
                                    ContactModel contactModel = listContact
                                            .get(0);

                                    String phoneNumber;
                                    if (!TextUtils.isEmpty(contactModel
                                            .getMobile()))
                                    {
                                        phoneNumber = contactModel
                                                .getMobile();

                                    }
                                    else
                                    {
                                        phoneNumber = contactModel
                                                .getPhone();
                                    }

                                    if(TextUtils.isEmpty(phoneNumber))
                                    {
                                        mContactLayout.setEnabled(false);
                                        mPhoneText.setText(R.string.no_contact_info);
                                        mNameText.setText("");
                                    }
                                    else
                                    {
                                        mPhoneText.setText(getString(R.string.dial, phoneNumber));
                                        mNameText.setText(contactModel.getContact());
                                    }
                                }
                            }

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
                        LogUtil.i(error.getMessage());
                        showLoadFailView(R.string.load_failed_retry);
                    }

                });
        executeRequest(request);
    }

    private void doDialRequest(String id)
    {
        ConvenienceDialRequest request = new ConvenienceDialRequest(id,
                new ResponseEventHandler<RespDialResultModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespDialResultModel> request,
                            RespDialResultModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mDialCountText.setText(result.getData()
                                    .getDial_count()
                                    + getString(R.string.dialcount_unit));
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

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.contactLayout:
                callContact();
                break;
        }
    }

    private void callContact()
    {

        if (!DeviceInfoUtil.isSimExist(this))
        {
            ToastUtil.showToastShort(R.string.please_check_your_sim_card);
            return;
        }

        String phonenumber = mPhoneText.getText().toString().trim();

        if (TextUtils.isEmpty(phonenumber))
        {
            ToastUtil.showToastShort(R.string.no_contact_phone);
            return;
        }

        doDialRequest(mConvenienceId);
        DeviceInfoUtil.startCall(mContext, phonenumber);
    }

}
