package com.moge.ebox.phone.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.moge.ebox.phone.R;
import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.bettle.network.http.ApiClient;
import com.moge.ebox.phone.bettle.tools.Logger;
import com.moge.ebox.phone.ui.adapter.BaseListAdapter;
import com.moge.ebox.phone.ui.customview.Head;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectCompanyActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context mContext;

    private ListView mContentListView;
    private List<String> mDataList = new ArrayList<String>();
    private ArrayAdapter<String> mAdpater;
    private Head mHead;
    private companyAdapter adapterCompany;


    private ArrayList<HashMap<String, String>> mCompanyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_company);

        mContext = SelectCompanyActivity.this;
        initView();
    }

    protected void initView() {

        mContentListView = (ListView) this.findViewById(R.id.commpany_lv_as_content);

        getCompany();

        initHead();
    }

    private void initHead() {
        mHead = findviewById_(R.id.commpany_title);
        Head.HeadData data = mHead.new HeadData();
        data.backVisibility = 1;
        data.tvVisibility = true;
        data.tvContent = getResources().getString(R.string.choose_kd_company);
        mHead.setData(data, this);
    }


    /**
     * 获取快递公司
     */
    private void getCompany() {
        Map params = new HashMap();
        ApiClient.getOrgnization(EboxApplication.getInstance(), params,
                new ApiClient.ClientCallback() {
                    @Override
                    public void onSuccess(JSONArray data, int code) {
                        factoryCompany(data);
                    }

                    @Override
                    public void onFailed(byte[] data, int code) {
                    }
                });
    }

    /**
     * 获得快递公司数据源
     *
     * @param
     */
    protected void factoryCompany(JSONArray dataPre) {
        mCompanyData = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> company;
        try {
            JSONArray data = dataPre.getJSONObject(0).getJSONArray("organizations");
            for (int i = 0; i < data.length(); i++) {
                JSONObject object = data.getJSONObject(i);
                String orgnization_id = object.getString("orgnization_id");
                String orgnization_name = object.getString("orgnization_name");
                String telephone = object.getString("telephone");
                String username = object.getString("username");
                if (orgnization_name.contains("魔格")
                        || orgnization_name.contains("格格快递")) {
                    break;
                }
                company = new HashMap<String, String>();
                company.put("orgnization_id", orgnization_id);
                company.put("name", orgnization_name);
                company.put("telephone", telephone);
                company.put("username", username);
                mCompanyData.add(company);
                mDataList.add(orgnization_name);
                initListView();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void initListView() {
        mAdpater = new ArrayAdapter<String>(this, R.layout.item_district,
                R.id.addressText, mDataList);
        mContentListView.setAdapter(mAdpater);
        mContentListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        HashMap<String, String> item = mCompanyData.get(position);
        String c_id = item.get("orgnization_id");
        String c_name = item.get("name");
        Logger.i("SelectCompanyActivity:" + position + "--" + c_id + "----" + c_name);
        Bundle result = new Bundle();
        result.putString("orgnization_id", c_id);
        result.putString("name", c_name);
        Intent resultIntent = new Intent(SelectCompanyActivity.this, RegisterPreActivity.class);
        resultIntent.putExtras(result);
        setResult(RESULT_OK, resultIntent);
        finish();
    }


    private class companyAdapter extends
            BaseListAdapter<HashMap<String, String>> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_company,
                        null);
            }
            TextView tv = ViewHolder.get(convertView, R.id.tv_company);
            HashMap<String, String> item = getItem(position);

            tv.setText(item.get("name"));
            return convertView;
        }

    }
}