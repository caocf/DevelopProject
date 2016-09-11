package com.moge.gege.ui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.data.PersistentData;
import com.moge.gege.model.AddressListModel;
import com.moge.gege.model.AddressModel;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.RespAddressListModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.TradingAddressDeleteRequest;
import com.moge.gege.network.request.TradingAddressListRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.AddressListAdapter;
import com.moge.gege.ui.adapter.AddressListAdapter.AddressListListener;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.widget.CustomDialog;
import com.moge.gege.util.widget.horizontalListview.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddressListActivity extends BaseActivity implements
        AddressListListener
{
    private Context mContext;
    @InjectView(R.id.addressList) ListView mListView;
    private AddressListAdapter mAdapter;

    private String mAddressId;
    private boolean mManageMode = false;
    private List<AddressModel> mAddressList = new ArrayList<AddressModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addresslist);
        ButterKnife.inject(this);

        mAddressId = getIntent().getStringExtra("address_id");
        mManageMode = TextUtils.isEmpty(mAddressId);
        mContext = AddressListActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderRightImage(R.drawable.icon_add_address);

        mAdapter = new AddressListAdapter(mContext);
        mAdapter.setListener(this);
        mAdapter.setSelectAddress(mAddressId);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mAdapter);
        if(!mManageMode) {
            this.setHeaderLeftTitle(R.string.address_list);
        }
        else
        {
            this.setHeaderLeftTitle(R.string.address_manage);
            mListView.setOnItemLongClickListener(mAdapter);
        }
    }

    @Override
    protected void onHeaderRightClick()
    {
        Intent intent = new Intent(mContext, NewAddressActivity.class);
        this.startActivityForResult(intent, GlobalConfig.INTENT_ADD_NEW_ADDRESS);
    }

    private void initData()
    {
        showLoadingView();
        doAddressListRequest();
    }

    // 快递柜的地址过滤掉
    private List<AddressModel> filterAddressData(List<AddressModel> allList)
    {
        mAddressList.clear();
        for (AddressModel model : allList)
        {
            if(model.getAddress_type() == 1)
            {
                mAddressList.add(model);
            }
        }

        return  mAddressList;
    }

    private void doAddressListRequest()
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
                            mAdapter.clear();

                            AddressListModel listModel = result.getData();
                            if (listModel != null
                                    && listModel.getAddress() != null
                                    && listModel.getAddress().size() > 0)
                            {
                                mAdapter.addAll(filterAddressData(listModel.getAddress()));
                            }

                            if(mAdapter.getCount() == 0){
                                showLoadEmptyView(R.string.address_empty_tips);
                            } else {
                                hideLoadingView();
                            }

                            mAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
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

    private void doAddressDeleteRequest(String addressId)
    {
        TradingAddressDeleteRequest request = new TradingAddressDeleteRequest(addressId,
                new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request,
                            BaseModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            ToastUtil.showToastShort(R.string.delete_success);
                            initData();

                            // delete local save delivery address
                            TradingAddressDeleteRequest deleteRequest = (TradingAddressDeleteRequest)request;
                            AddressModel curDeliveryAddress = PersistentData.instance().getDeliveryAddress(AppApplication.getLoginId());
                            if(curDeliveryAddress != null && deleteRequest.getAddresId().equals(curDeliveryAddress.get_id()))
                            {
                                PersistentData.instance().setDeliveryAddress(AppApplication.getLoginId(), null);
                            }
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
            case GlobalConfig.INTENT_ADD_NEW_ADDRESS:
            case GlobalConfig.INTENT_EDIT_ADDRESS:
                initData();
                break;
            default:
                break;
        }
    }

    @Override
    public void onAddressItemClick(AddressModel model)
    {
        Intent intent = new Intent();
        intent.putExtra("address", model);
        if(mManageMode){
            intent.setClass(mContext, NewAddressActivity.class);
            startActivityForResult(intent, GlobalConfig.INTENT_EDIT_ADDRESS);
        } else {
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    @Override
    public void onAddressItemLongClick(final AddressModel model)
    {
        new CustomDialog.Builder(mContext)
                .setTitle(R.string.general_tips)
                .setMessage(R.string.delete_address_confirm)
                .setPositiveButton(R.string.general_confirm,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which)
                            {
                                doAddressDeleteRequest(model.get_id());
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

}
