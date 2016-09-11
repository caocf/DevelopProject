package com.moge.gege.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.DeliveryBoxModel;
import com.moge.gege.model.LogisticsModel;
import com.moge.gege.model.RespDeliveryDetailModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.DeliveryQueryRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.LogisticsListAdapter;
import com.moge.gege.ui.event.Event;
import com.moge.gege.util.DeviceInfoUtil;
import com.moge.gege.util.FunctionUtils;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.widget.CustomDialog;
import com.moge.gege.util.widget.MyListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class DeliverySearchDetailActivity extends BaseActivity {
    private Context mContext;

    @InjectView(R.id.serachView)
    LinearLayout mSearchView;

    @InjectView(R.id.deliveryNumberEdit)
    EditText deliveryNumberEdit;

    @InjectView(R.id.selectExpressBtn)
    Button selectExpressBtn;

    @InjectView(R.id.searchDeliveryBtn)
    Button searchDeliveryBtn;

    @InjectView(R.id.detailView)
    LinearLayout mDetailView;

    @InjectView(R.id.deliveryImage)
    ImageView deliveryImage;

    @InjectView(R.id.courierNameText)
    TextView courierNameText;

    @InjectView(R.id.deliveryNameText)
    TextView deliveryNameText;

    @InjectView(R.id.phoneText)
    TextView phoneText;

    @InjectView(R.id.deliveryNumberText)
    TextView deliveryNumberText;

    @InjectView(R.id.boxNumberText)
    TextView boxNumberText;

    @InjectView(R.id.deliveryAddressText)
    TextView deliveryAddressText;

    @InjectView(R.id.deliveryTimeText)
    TextView deliveryTimeText;

    @InjectView(R.id.fetchTimeText)
    TextView fetchTimeText;

    @InjectView(R.id.passwordText)
    TextView passwordText;

    @InjectView(R.id.openBoxBtn)
    Button openBoxBtn;

    @InjectView(R.id.statusBtn)
    Button statusBtn;

    @InjectView(R.id.logisticsView)
    LinearLayout logisticsView;

    @InjectView(R.id.logisticsTitleText)
    TextView logisticsTitleText;

    @InjectView(R.id.logisticsLisView)
    MyListView logisticsListView;

    private LogisticsListAdapter mLogisticsAdapter;

    // 外部参数
    private String mDeliveryNumber;
    private String mDeliveryCompany;
    private boolean mDetailMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_search_detail);

        ButterKnife.inject(this);
        EventBus.getDefault().register(this);

        mDeliveryNumber = getIntent().getStringExtra("delivery_number");
        mDeliveryCompany = getIntent().getStringExtra("delivery_company");
        mDetailMode = !TextUtils.isEmpty(mDeliveryCompany);

        mContext = DeliverySearchDetailActivity.this;
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {
        super.initView();
        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(R.string.logistics_detail);

        // 查询模式
        if (!mDetailMode) {
            mSearchView.setVisibility(View.VISIBLE);
            deliveryNumberEdit.setText(mDeliveryNumber);
            this.setHeaderLeftTitle(R.string.delivery_search);
        }

        phoneText.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.icon_phone_blue, 0, 0, 0);

        mLogisticsAdapter = new LogisticsListAdapter(mContext);
        logisticsListView.setAdapter(mLogisticsAdapter);
    }

    private void initData() {
        if (mDetailMode) {
            doQueryDelivery(mDeliveryNumber, mDeliveryCompany);
        }
    }

    @OnClick(R.id.selectExpressBtn)
    void onSelectExpressCompanyBtnClick() {
        startActivity(new Intent(mContext, DeliveryCompanyActivity.class));
    }

    @OnClick(R.id.searchDeliveryBtn)
    void onSearchDeliveryBtnClick() {

        FunctionUtils.hideInputMethod(mContext);

        mDeliveryNumber = deliveryNumberEdit.getText().toString().trim();

        if (TextUtils.isEmpty(mDeliveryNumber) || mDeliveryNumber.length() < 4) {
            ToastUtil.showToastShort(R.string.please_input_number);
            return;
        }

        if (TextUtils.isEmpty(mDeliveryCompany)) {
            ToastUtil.showToastShort(R.string.please_select_company);
            return;
        }

        doQueryDelivery(mDeliveryNumber, mDeliveryCompany);
    }

    @OnClick(R.id.phoneText)
    void onPhoneTextClick()
    {
        new CustomDialog.Builder(mContext)
                .setTitle(phoneText.getText().toString())
                .setMessage(null)
                .setPositiveButton(R.string.call,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                if (!DeviceInfoUtil.isSimExist(mContext))
                                {
                                    ToastUtil.showToastShort(R.string.please_check_your_sim_card);
                                    return;
                                }

                                DeviceInfoUtil.startCall(mContext, phoneText.getText().toString());
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

    @OnClick(R.id.openBoxBtn)
    void onOpenBoxClick()
    {

        if (!AppApplication.checkLoginState((Activity) mContext)){
            return;
        }
        Intent intent = new Intent(mContext, CaptureActivity.class);
        intent.putExtra(CaptureActivity.SCAN_TYPE,CaptureActivity.SCAN_TYPE_OPEN_EBOX);
        startActivity(intent);
    }

    private void updateDeliveryInfo(DeliveryBoxModel model) {

        mDetailView.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(model.getDelivery_logo()))
        {
            this.setImage(deliveryImage, getImageUrl(model.getDelivery_logo()) + GlobalConfig.IMAGE_STYLE90_90,
                    R.drawable.icon_default_avatar);
        }
        else
        {
            deliveryImage
                    .setImageResource(R.drawable.icon_default_avatar);
        }

        courierNameText.setText(model.getOperator_name());
        deliveryNameText.setText(model.getDelivery_name());
        phoneText.setText(model.getOperator_mobile());
        deliveryNumberText.setText(getString(R.string.delivery_number, model.getNumber()));
        boxNumberText.setText(getString(R.string.box_name, model.getRack_name() + model.getBox_name()));
        deliveryAddressText.setText(getString(R.string.delivery_address2, model.getTerminal_name() + model.getTerminal_village_address()));
        deliveryTimeText.setText(getString(R.string.delivery_time, model.getDeliver_time()));

        if (model.getState() >= 4 && model.getState() <= 7) {
            passwordText.setVisibility(View.GONE);
            fetchTimeText.setText(getString(R.string.fetch_time, model.getFetch_time()));
            statusBtn.setVisibility(View.VISIBLE);
            statusBtn.setText(model.getVerbose_state());
        } else {
            fetchTimeText.setVisibility(View.GONE);
            passwordText.setText(getString(R.string.box_password, model.getPassword()));
            openBoxBtn.setVisibility(View.VISIBLE);
        }
    }

    private void updateLogistics(List<LogisticsModel> modelList) {

        logisticsView.setVisibility(View.VISIBLE);
        if(mDetailMode)
        {
            logisticsTitleText.setVisibility(View.VISIBLE);
        }
        else
        {
            logisticsTitleText.setVisibility(View.GONE);
        }
        mLogisticsAdapter.clear();
        mLogisticsAdapter.addAll(modelList);
        mLogisticsAdapter.notifyDataSetChanged();
    }


    private void doQueryDelivery(String number, String company) {
        DeliveryQueryRequest request = new DeliveryQueryRequest(number, company,
                new ResponseEventHandler<RespDeliveryDetailModel>() {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespDeliveryDetailModel> request,
                            RespDeliveryDetailModel result) {
                        if (result.getStatus() == ErrorCode.SUCCESS) {

                            DeliveryBoxModel boxModel = result.getData().getOrder();
                            if(mDetailMode && boxModel != null)
                            {
                                updateDeliveryInfo(boxModel);
                            }

                            updateLogistics(result.getData().getLogistics());

                        } else {
                            ToastUtil.showToastShort(result.getMsg());
                            showLoadFailView(result.getMsg());
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error) {
                        ToastUtil.showToastShort(error.getMessage());
                        showLoadFailView(error.getMessage());
                    }
                });
        executeRequest(request);
    }

    public void onEvent(Event.DeliverySelectEvent event) {
        selectExpressBtn.setText(event.getName());
        mDeliveryCompany = event.getType();
    }
}
