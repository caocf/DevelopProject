package com.moge.gege.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.GoodsClassifyListRequest;
import com.moge.gege.network.request.PetBreedListRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.util.LogUtil;

public class OptionBuilder
{
    private Context mContext;
    private static OptionBuilder instance;

    private CharSequence[] mHouseTypeArray;
    private List<HouseTypeOptionModel> mHouseTypeList = new ArrayList<HouseTypeOptionModel>();

    private CharSequence[] mGoodClassifyArray;
    private List<BaseOptionModel> mGoodClassifyList = new ArrayList<BaseOptionModel>();

    private CharSequence[] mPetBreedArray;
    private List<BaseOptionModel> mPetBreedList = new ArrayList<BaseOptionModel>();
    private HashMap<String, String> mPetBreedMap = new HashMap<String, String>();

    private CharSequence[] mGoodConditionArray;

    public static OptionBuilder instance()
    {
        if (instance == null)
        {
            instance = new OptionBuilder();

        }
        return instance;
    }

    public void init(Context context)
    {
        mContext = context;
        mGoodConditionArray = mContext.getResources().getStringArray(
                R.array.secondhand_publish_condition);
        doGoodClassifyRequest();
        doPetBreedRequest();
    }

    public CharSequence[] getHouseTypeArray()
    {
        if (mHouseTypeArray == null)
        {
            getHouseTypeList();

            mHouseTypeArray = new String[mHouseTypeList.size()];

            for (int i = 0; i < mHouseTypeList.size(); i++)
            {
                mHouseTypeArray[i] = mHouseTypeList.get(i).getName();
            }
        }
        return mHouseTypeArray;
    }

    public List<HouseTypeOptionModel> getHouseTypeList()
    {
        if (mHouseTypeList.size() > 0)
        {
            return mHouseTypeList;
        }

        for (int room = 1; room <= 4; room++)
        {
            for (int hall = 0; hall <= room && hall < 3; hall++)
            {
                if (hall == 0 && room > 1)
                {
                    continue;
                }

                HouseTypeOptionModel model = new HouseTypeOptionModel();
                model.setResId(-1);
                model.setName(room + "室" + hall + "厅");
                model.setRoom(room);
                model.setHall(hall);
                model.setWashroom(0);
                mHouseTypeList.add(model);
            }
        }

        HouseTypeOptionModel model = new HouseTypeOptionModel();
        model.setResId(-1);
        model.setName("其他");
        model.setRoom(0);
        model.setHall(0);
        model.setWashroom(0);
        mHouseTypeList.add(model);

        return mHouseTypeList;
    }

    public CharSequence[] getGoodClassifyArray()
    {
        if (mGoodClassifyArray == null)
        {
            mGoodClassifyArray = new String[mGoodClassifyList.size()];

            for (int i = 0; i < mGoodClassifyList.size(); i++)
            {
                mGoodClassifyArray[i] = mGoodClassifyList.get(i).getName();
            }
        }
        return mGoodClassifyArray;
    }

    public List<BaseOptionModel> getGoodClassifyList()
    {
        return mGoodClassifyList;
    }

    public String getPetBreedNameById(String id)
    {
        return mPetBreedMap.get(id);
    }

    private void changeBreedListToMap(List<BaseOptionModel> list,
            Map<String, String> map)
    {
        if (list == null || map == null)
        {
            return;
        }

        for (BaseOptionModel model : list)
        {
            map.put(model.get_id(), model.getName());
        }
    }

    public CharSequence[] getPetBreedArray()
    {
        if (mPetBreedArray == null)
        {
            mPetBreedArray = new String[mPetBreedList.size()];

            for (int i = 0; i < mPetBreedList.size(); i++)
            {
                mPetBreedArray[i] = mPetBreedList.get(i).getName();
            }
        }
        return mPetBreedArray;
    }

    public List<BaseOptionModel> getPetBreedList()
    {
        return mPetBreedList;
    }

    public CharSequence[] getGoodConditionArray()
    {
        if (mGoodConditionArray == null)
        {
            mGoodConditionArray = mContext.getResources().getStringArray(
                    R.array.secondhand_publish_condition);
        }

        return mGoodConditionArray;
    }

    public CharSequence getGoodConditionByValue(int value)
    {
        if (value >= 7 && value <= 10)
        {
            return getGoodConditionArray()[10 - value];
        }

        return "其他";
    }

    private void doGoodClassifyRequest()
    {
        GoodsClassifyListRequest request = new GoodsClassifyListRequest(
                new ResponseEventHandler<RespCategoryListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespCategoryListModel> request,
                            RespCategoryListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            if (result.getData() != null)
                            {
                                mGoodClassifyList = result.getData()
                                        .getClassifys();
                            }
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                    }

                });
        RequestManager.addRequest(request, mContext);
    }

    private void doPetBreedRequest()
    {
        PetBreedListRequest request = new PetBreedListRequest(
                new ResponseEventHandler<RespCategoryListModel>()
                {

                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespCategoryListModel> request,
                            RespCategoryListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            if (result.getData() != null)
                            {
                                mPetBreedList = result.getData().getBreeds();
                                changeBreedListToMap(mPetBreedList,
                                        mPetBreedMap);
                            }
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                    }

                });
        RequestManager.addRequest(request, mContext);
    }

}
