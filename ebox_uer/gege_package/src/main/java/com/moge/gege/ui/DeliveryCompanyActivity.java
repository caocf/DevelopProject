package com.moge.gege.ui;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.model.DeliveryCompanyModel;
import com.moge.gege.model.RespDeliveryCompanyModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.DeliveryCompanyQueryRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.DeliveryCompanyListAdapter;
import com.moge.gege.ui.event.Event;
import com.moge.gege.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;

public class DeliveryCompanyActivity extends BaseActivity {
    private Context mContext;

    @InjectView(R.id.keywordEdit)
    EditText keywordEdit;

    @InjectView(R.id.deleteInputImage)
    ImageView deleteInputImage;

    @InjectView(R.id.companyList)
    ListView companyList;




    private DeliveryCompanyListAdapter mAdapter;
    private List<DeliveryCompanyModel> mDeliveryCompanyList;
    private ArrayList<DeliveryCompanyModel> mFilterDataList = new ArrayList<DeliveryCompanyModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_company);

        ButterKnife.inject(this);

        mContext = DeliveryCompanyActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView() {
        super.initView();
        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(R.string.select_delivery_company);

        mAdapter = new DeliveryCompanyListAdapter(mContext);
        companyList.setAdapter(mAdapter);

        keywordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                String keywords = charSequence.toString();

                if (TextUtils.isEmpty(keywords)) {
                    deleteInputImage.setVisibility(View.GONE);
                } else {
                    deleteInputImage.setVisibility(View.VISIBLE);
                }

                mAdapter.clear();
                mAdapter.addAll(filterDeliveryCompany(mDeliveryCompanyList, keywords));
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initData() {
        doQueryDeliveryCompany();
    }

    private List<DeliveryCompanyModel> filterDeliveryCompany(List<DeliveryCompanyModel> deliveryList, String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            return deliveryList;
        }

        mFilterDataList.clear();

        Pattern pattern = Pattern.compile(keyword);

        for (DeliveryCompanyModel model : deliveryList) {
            Matcher matcher = pattern.matcher(model.getName());
            if (matcher.find()) {
                mFilterDataList.add(model);
            }
        }

        return mFilterDataList;
    }

    @OnClick(R.id.deleteInputImage)
    void onDeleteInputImageClick() {
        keywordEdit.setText("");
    }

    @OnItemClick(R.id.companyList)
    void onCompanyListItemClick(int position) {
        DeliveryCompanyModel model = mAdapter.getItem(position);
        EventBus.getDefault().post(new Event.DeliverySelectEvent(model.getName(), model.getDelivery_type()));
        finish();
    }

    private void doQueryDeliveryCompany() {
        DeliveryCompanyQueryRequest request = new DeliveryCompanyQueryRequest(
                new ResponseEventHandler<RespDeliveryCompanyModel>() {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespDeliveryCompanyModel> request,
                            RespDeliveryCompanyModel result) {
                        if (result.getStatus() == ErrorCode.SUCCESS) {
                            mDeliveryCompanyList = result.getData().getCompanys();
                            mAdapter.addAll(mDeliveryCompanyList);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToastShort(result.getMsg());
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error) {
                        ToastUtil.showToastShort(error.getMessage());
                    }
                });
        executeRequest(request);
    }
}
