package com.moge.gege.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.model.DistrictModel;
import com.moge.gege.model.RespDistrictListModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.CityListRequest;
import com.moge.gege.network.request.DeliveryDistrictListRequest;
import com.moge.gege.network.request.DistrictListRequest;
import com.moge.gege.network.request.ProvinceListRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.util.ToastUtil;

public class SelectDistrictActivity extends BaseActivity implements
        OnTouchListener, OnItemClickListener
{
    private Context mContext;

    private LinearLayout mSelectAddressLayout;
    private TextView mSelectAddressText;
    private ListView mContentListView;
    private List<DistrictModel> mProvinceList;
    private List<DistrictModel> mCityList;
    private List<DistrictModel> mDistrictList;
    private List<String> mDataList = new ArrayList<String>();
    private ArrayAdapter<String> mAdpater;

    private int mStep = 0;
    private int mPid;
    private int mCid;
    private int mDid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectdistrict);

        mContext = SelectDistrictActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(R.string.select_district);

        mSelectAddressLayout = (LinearLayout) this
                .findViewById(R.id.selectAddressLayout);
        mSelectAddressText = (TextView) this
                .findViewById(R.id.selectAddressText);

        mContentListView = (ListView) this.findViewById(R.id.contentList);
        mAdpater = new ArrayAdapter<String>(this, R.layout.item_district,
                R.id.addressText, mDataList);
        mContentListView.setAdapter(mAdpater);
        mContentListView.setOnItemClickListener(this);
    }

    @Override
    protected void onHeaderRightClick()
    {

    }

    private void initData()
    {
        // to do list!!!(now only support nj)
        // doProvinceListRequest();

        doDeliveryDistrictListRequest();
    }

    private void setSelectAddressContent(String content)
    {
        mSelectAddressLayout.setVisibility(View.GONE);
        mSelectAddressText.setText(mSelectAddressText.getText() + content);
    }

    private void doProvinceListRequest()
    {
        ProvinceListRequest request = new ProvinceListRequest(
                new ResponseEventHandler<RespDistrictListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespDistrictListModel> request,
                            RespDistrictListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mProvinceList = result.getData().getProvinces();
                            updateList(mProvinceList);
                        }
                        else
                        {
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

    private void doCityListRequest(int pid)
    {
        CityListRequest request = new CityListRequest(pid,
                new ResponseEventHandler<RespDistrictListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespDistrictListModel> request,
                            RespDistrictListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mCityList = result.getData().getCitys();
                            updateList(mCityList);
                            mStep = 1;
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

    private void doDistrictListRequest(int cid)
    {
        DistrictListRequest request = new DistrictListRequest(cid,
                new ResponseEventHandler<RespDistrictListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespDistrictListModel> request,
                            RespDistrictListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mDistrictList = result.getData().getDistricts();
                            updateList(mDistrictList);
                            mStep = 2;
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

    private void doDeliveryDistrictListRequest()
    {
        DeliveryDistrictListRequest request = new DeliveryDistrictListRequest(
                new ResponseEventHandler<RespDistrictListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespDistrictListModel> request,
                            RespDistrictListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mDistrictList = result.getData().getDistricts();
                            updateList(mDistrictList);
                            mStep = 2;
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

    private void updateList(List<DistrictModel> listModel)
    {
        convertDataToStringList(listModel);
        mAdpater.notifyDataSetChanged();
    }

    private void convertDataToStringList(List<DistrictModel> listModel)
    {
        if (listModel == null)
        {
            return;
        }

        mDataList.clear();
        for (DistrictModel model : listModel)
        {
            mDataList.add(model.getName());
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mStep == 0)
        {
            mPid = mProvinceList.get(position).get_id();
            setSelectAddressContent(mProvinceList.get(position).getName());
            doCityListRequest(mPid);
        }
        else if (mStep == 1)
        {
            mCid = mCityList.get(position).get_id();
            setSelectAddressContent(mCityList.get(position).getName());
            doDistrictListRequest(mCid);
        }
        else
        {
            mDid = mDistrictList.get(position).get_id();
            setSelectAddressContent(mDistrictList.get(position).getName());

            Intent intent = new Intent();
            intent.putExtra("district", mDistrictList.get(position));
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
