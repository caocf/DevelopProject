package com.moge.ebox.phone.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moge.ebox.phone.R;
import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.bettle.network.http.ApiClient;
import com.moge.ebox.phone.bettle.tools.Logger;
import com.moge.ebox.phone.model.AreaModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectAreaActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private Context mContext;
    private ListView mContentListView;
    private List<AreaModel> mProvinceList;
    private List<AreaModel> mCityList;
    private List<AreaModel> mAreaList;
    private List<String> mDataList = new ArrayList<String>();
    private ArrayAdapter<String> mAdpater;

    private int mStep = 0;
    private int mPid;
    private int mCid;
    private int mAid;

    private String p_name;
    private String c_name;
    private String a_name;

    private RelativeLayout backRL;
    private TextView headerTV;

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_area);

        mContext = SelectAreaActivity.this;
        initView();
    }

    protected void initView() {
        mContentListView = (ListView) this.findViewById(R.id.area_lv_as_content);
        mAdpater = new ArrayAdapter<String>(this, R.layout.item_district,
                R.id.addressText, mDataList);
        mContentListView.setAdapter(mAdpater);
        mContentListView.setOnItemClickListener(this);

        intent = new Intent(SelectAreaActivity.this, RegisterPreActivity.class);
        getArea();
        initAreaHead();
    }

    private void getArea() {
        doProvinceListRequest();
    }

    private void initAreaHead() {
        backRL = (RelativeLayout) this.findViewById(R.id.rl_back);
        headerTV = (TextView) this.findViewById(R.id.header_title);
        backRL.setOnClickListener(this);

    }


    /**
     * 进行省份数据请求
     */
    private void doProvinceListRequest() {
        HashMap params = new HashMap();
        Logger.i("SelectActivity:省份请求");
        ApiClient.getProvince(EboxApplication.getInstance(), params, new ApiClient.ClientCallback() {
            @Override
            public void onSuccess(JSONArray data, int code) {
                //省份获得成功
                Logger.i("SelectAreaActivity" + data.toString());
                headerTV.setText("省份");
                try {
                    JSONArray provinces = data.getJSONObject(0).getJSONArray("provinces");
                    mProvinceList = new ArrayList<AreaModel>();
                    for (int i = 0; i < provinces.length(); i++) {
                        AreaModel provinceModel = new AreaModel();
                        provinceModel.setPid(provinces.getJSONObject(i).getInt("id"));
                        provinceModel.setName(provinces.getJSONObject(i).getString("name"));
                        mProvinceList.add(provinceModel);
                    }
                    updateList(mProvinceList);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(byte[] data, int code) {
                Logger.i("SelectAreaActivity   " + code);

            }
        });
    }

    /**
     * 进行城市数据请求
     */

    private void doCityListRequest(int pid) {
        Logger.i("SelectAreaActivity:获得城市" + pid);
        ApiClient.getCity(EboxApplication.getInstance(), pid, new ApiClient.ClientCallback() {
            @Override
            public void onSuccess(JSONArray data, int code) {
                Logger.i("SelectAreaActivity:城市获得成功" + data.toString());
                headerTV.setText("城市");
                try {
                    JSONArray provinces = data.getJSONObject(0).getJSONArray("cities");
                    mCityList = new ArrayList<AreaModel>();
                    for (int i = 0; i < provinces.length(); i++) {
                        AreaModel cityModel = new AreaModel();
                        cityModel.setCid(provinces.getJSONObject(i).getInt("id"));
                        cityModel.setName(provinces.getJSONObject(i).getString("name"));
                        mCityList.add(cityModel);
                    }
                    updateList(mCityList);
                    mStep = 1;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(byte[] data, int code) {
                Logger.i("SelectAreaActivity:城市获得失败" + code);

            }
        });
    }

    /**
     * 进行区域城市选择
     *
     * @param cid
     */
    private void doDistrictListRequest(int cid) {
        ApiClient.getRegion(EboxApplication.getInstance(), cid, new ApiClient.ClientCallback() {
            @Override
            public void onSuccess(JSONArray data, int code) {
                Logger.i("SelectAreaActivity  区域城市选择" + data.toString());
                //获得成功

                try {
                    JSONArray provinces = data.getJSONObject(0).getJSONArray("regions");
                    if (provinces.length() == 0) {
                        Bundle result = new Bundle();
                        result.putInt("id", mCid);
                        result.putString("name", c_name);
                        backRegiste(result);
                    } else {
                        headerTV.setText("区域");
                    }
                    mAreaList = new ArrayList<AreaModel>();
                    for (int i = 0; i < provinces.length(); i++) {
                        AreaModel areaModel = new AreaModel();
                        areaModel.setAid(provinces.getJSONObject(i).getInt("id"));
                        areaModel.setName(provinces.getJSONObject(i).getString("name"));
                        mAreaList.add(areaModel);
                    }
                    updateList(mAreaList);
                    mStep = 2;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(byte[] data, int code) {
                Logger.i("SelectAreaActivity  区域城市选择   error" + code);
            }
        });
    }

    /**
     * 更新数据
     *
     * @param listModel
     */
    private void updateList(List<AreaModel> listModel) {
        if (listModel == null) {
            return;
        }

        mDataList.clear();
        for (AreaModel model : listModel) {
            mDataList.add(model.getName());
        }

        mAdpater.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Logger.i("SelectAreaActivity：" + mStep);
        if (mStep == 0) {
            mPid = mProvinceList.get(position).getPid();
            p_name = mProvinceList.get(position).getName();
            doCityListRequest(mPid);
        } else if (mStep == 1) {
            mCid = mCityList.get(position).getCid();
            c_name = mCityList.get(position).getName();
            doDistrictListRequest(mCid);

        } else {
            mAid = mAreaList.get(position).getAid();
            a_name = mAreaList.get(position).getName();

            Bundle result = new Bundle();
            result.putInt("id", mAreaList.get(position).getAid());
            result.putString("name", a_name);
            backRegiste(result);
        }
    }


    private void backRegiste(Bundle result) {

        intent.putExtras(result);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rl_back) {
            Logger.i("SelectAreaActivity:" + mStep);
            switch (mStep) {
                case 0:
                    finish();
                    break;
                case 1:
                    doProvinceListRequest();
                    mStep = 0;
                    break;
                case 2:
                    doCityListRequest(mPid);
                    mStep = 1;
                    break;
            }
        }
    }
}
