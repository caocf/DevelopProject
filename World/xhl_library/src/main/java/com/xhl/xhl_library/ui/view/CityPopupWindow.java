package com.xhl.xhl_library.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.xhl.xhl_library.R;
import com.xhl.xhl_library.ui.view.widget.wheel.OnWheelChangedListener;
import com.xhl.xhl_library.ui.view.widget.wheel.WheelView;
import com.xhl.xhl_library.ui.view.widget.wheel.adapters.ArrayWheelAdapter;
import com.xhl.xhl_library.ui.view.widget.wheel.city.CityModel;
import com.xhl.xhl_library.ui.view.widget.wheel.city.DistrictModel;
import com.xhl.xhl_library.ui.view.widget.wheel.city.ProvinceModel;
import com.xhl.xhl_library.ui.view.widget.wheel.city.XmlParserHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class CityPopupWindow implements OnWheelChangedListener {
    private Context mContext;
    private Activity mActivity;

    protected String[] mProvinceDatas;
    protected Map<String, String[]> mCitisDatasMap = new HashMap<>();
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<>();
    protected Map<String, String> mZipcodeDatasMap = new HashMap<>();

    protected String mCurrentProviceName;
    protected String mCurrentCityName;
    protected String mCurrentDistrictName = "";
    protected String mCurrentZipCode = "";

    private WheelView mViewProvince;//城市
    private WheelView mViewCity;//省份
    private WheelView mViewDistrict;//邮编

    AlertDialog.Builder mDialog;
    private OnWheelItemSelected mCallback;
    private HashMap<String, String> selectData = new HashMap<>();

    public CityPopupWindow(Context context, OnWheelItemSelected callback) {
        mContext = context;
        mActivity = (Activity) context;
        mCallback = callback;
        initPopupWindow();
    }

    private void initPopupWindow() {

        mDialog = new AlertDialog.Builder(mContext);
        mDialog.setTitle("区域选择");

        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_city_select, null);

        mViewProvince = (WheelView) view.findViewById(R.id.wheel_left);
        mViewCity = (WheelView) view.findViewById(R.id.wheel_center);
        mViewDistrict = (WheelView) view.findViewById(R.id.wheel_right);

        mViewProvince.addChangingListener(this);
        mViewCity.addChangingListener(this);
        mViewDistrict.addChangingListener(this);

        mDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selected();
            }
        });

        mDialog.setNegativeButton("取消", null);

        mDialog.setCancelable(false);

        setUpData();

        mDialog.setView(view);
    }

    private void selected() {
        selectData.put("city", mCurrentCityName);
        selectData.put("province", mCurrentProviceName);
        selectData.put("area", mCurrentDistrictName);
        selectData.put("code", mCurrentZipCode);
        if (mCallback != null) {
            mCallback.onWheelSelect(selectData);
        }
    }

    private void setUpData() {


        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                initProvinceDatas();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (mProvinceDatas != null) {

                    mViewProvince.setViewAdapter(createAdapter(mProvinceDatas));

                    mViewProvince.setVisibleItems(7);
                    mViewCity.setVisibleItems(7);
                    mViewDistrict.setVisibleItems(7);

                    updateCities();

                    updateAreas();
                }
            }
        }.execute();

    }

    private ArrayWheelAdapter<String> createAdapter(String[] data) {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<>(mContext, data);
        adapter.setTextSize(16);
        adapter.setTextColor(0x8a000000);
        return adapter;
    }

    //更新城市数据
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(createAdapter(cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    //更新区域数据
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }

        mViewDistrict.setViewAdapter(createAdapter(areas));
        mViewDistrict.setCurrentItem(0);
    }


    private void initProvinceDatas() {
        List<ProvinceModel> provinceList;
        AssetManager asset = mContext.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            provinceList = handler.getDataList();
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }


    public void show() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    /**
     * Callback method to be invoked when current item changed
     *
     * @param wheel    the wheel view whose state has changed
     * @param oldValue the old value of current item
     * @param newValue the new value of current item
     */
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    public interface OnWheelItemSelected {
        void onWheelSelect(HashMap<String, String> data);
    }

}
