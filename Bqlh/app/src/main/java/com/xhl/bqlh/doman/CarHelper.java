package com.xhl.bqlh.doman;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.AppDelegate;
import com.xhl.bqlh.R;
import com.xhl.bqlh.doman.callback.ContextValue;
import com.xhl.bqlh.model.CarModel;
import com.xhl.bqlh.model.GarbageModel;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.helper.GlobalCarInfo;
import com.xhl.bqlh.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.bqlh.view.ui.activity.OrderConfirmActivity;
import com.xhl.bqlh.view.ui.recyclerHolder.CarInfoDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.utils.log.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sum on 16/7/8.
 */
public class CarHelper extends BaseValue implements RecycleViewCallBack {

    public static int TYPE_RES_CAR_DATA = 1;//购物车数据

    public static int TYPE_RES_CAR_DATA_REFRESH = 2;//购物车数据全选刷新

    public static int TYPE_RES_ORDER_ALL_MONEY = 3;//总金额

    public static int TYPE_RES_ORDER_NUM = 4;//选择数量

    public static int TYPE_RES_SHOW_LOADING = 5;

    public static int TYPE_RES_HIED_LOADING = 6;

    public static int TYPE_RES_NETWORK_ERROR_HIDE = 7;

    public static int TYPE_RES_NETWORK_ERROR_SHOW = 8;

    public static int TYPE_RES_SELECT_STATE = 9;

    public static int TYPE_RES_DATA_NULL = 10;

    private ArrayList<CarModel> mCars;//订货车中全部商品

    private HashMap<Object, ArrayList<CarModel>> mData;//分类商品

    private int mSelectedNum;//选择的商品个数

    private boolean mNeedRefresh = true;// 刷新
    private boolean mIsLoading = false;
    private boolean mIsFirst = true;
    private Context mContext;

    public CarHelper(ContextValue value, Context context) {
        super(value);
        mContext = context;
        mCars = new ArrayList<>();
    }

    //删除
    public void onDelete() {
        if (mSelectedNum == 0) {
            toastShow("您还没有选择商品哦~");
        } else {
            String msg = mContext.getResources().getString(R.string.shop_delete_goods_tips, mSelectedNum);
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            dialog.setTitle(R.string.dialog_title).setMessage(msg).setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    delete();
                }
            }).setNegativeButton(R.string.dialog_cancel, null).show();
        }
    }

    //删除多组
    private void delete() {
        StringBuilder builder = new StringBuilder();
        ArrayList<CarModel> cars = this.mCars;
        for (CarModel car : cars) {
            if (car.isChecked) {
                builder.append(car.getId()).append(",");
            }
        }
        String ids = builder.substring(0, builder.length() - 1);
        ApiControl.getApi().carDelete(ids, new DefaultCallback<ResponseModel<Object>>() {
            @Override
            public void success(ResponseModel<Object> result) {
                onRefreshLoadData();
            }

            @Override
            public void finish() {

            }
        });
    }

    //检测是否满足最小起订金额
    private boolean checkBuyMoney() {
        boolean isOk = false;
        HashMap<Object, ArrayList<CarModel>> data = this.mData;
        Set<Map.Entry<Object, ArrayList<CarModel>>> entries = data.entrySet();

        for (Map.Entry<Object, ArrayList<CarModel>> map : entries) {
            isOk = checkOneShop(map.getValue());
            if (!isOk) {
                break;
            }
        }
        return isOk;
    }

    private boolean checkOneShop(List<CarModel> data) {
        boolean exitBuyProduct = false;
        float buyMoney = 0;
        CarModel firstCar = data.get(0);
        for (CarModel car : data) {
            if (car.isChecked) {
                exitBuyProduct = true;
                buyMoney += car.getProductPrice() * car.mCurNum;
            }
        }
        //存在勾选的商品时判断金额
        if (!exitBuyProduct) {
            return true;
        } else {
            if (buyMoney >= firstCar.getMinOrderPrice()) {
                return true;
            } else {
                ToastUtil.showToastLong(firstCar.getShopName() + "最低起订金额为" + firstCar.getMinOrderPrice() + "元");
                return false;
            }
        }
    }

    //结算
    public void onAccount() {
        if (mSelectedNum == 0) {
            toastShow("您还没有选择商品哦~");
        } else {
            if (!checkBuyMoney()) {
                return;
            }
            JSONArray array = new JSONArray();
            ArrayList<CarModel> cars = this.mCars;
            for (CarModel car : cars) {
                if (car.isChecked) {
                    JSONObject object = itemObj(car);
                    array.put(object);
                }
            }
            //array
            JSONObject object = new JSONObject();
            try {
                object.putOpt("results", array);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String res = object.toString();

            Intent intent = new Intent(mContext, OrderConfirmActivity.class);
            intent.putExtra("data", res);
            mContext.startActivity(intent);
        }
    }


    private JSONObject itemObj(CarModel carModel) {
        JSONObject object = new JSONObject();
        try {
            object.put("shoppingCartId", carModel.getId());
            object.put("productId", carModel.getProductId());
            object.put("sellerId", carModel.getSellerId());
            object.put("quantity", carModel.mCurNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }


    //全选
    public void onSelectAll(boolean select) {
        ArrayList<CarModel> cars = this.mCars;
        for (CarModel car : cars) {
            car.isChecked = select;
        }
        if (select) {
            mSelectedNum = cars.size();
        } else {
            mSelectedNum = 0;
        }
        //更新ui
        showValue(TYPE_RES_CAR_DATA_REFRESH, null);
        //更新显示
        countMoney();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mIsFirst = true;
        HashMap<Object, ArrayList<CarModel>> loadCarInfo = GlobalCarInfo.instance().getLoadCarInfo();
        if (loadCarInfo != null) {
            showData(loadCarInfo);
            mNeedRefresh = false;
        }
    }

    @Override
    public void onResume() {
        if (mNeedRefresh && !mIsLoading) {
            mIsLoading = true;
            onRefreshLoadData();
        }
    }

    //重新加载数据
    public void reLoadCar() {
        mNeedRefresh = true;
    }

    //刷新数据
    public void onRefreshLoadData() {
        if (!AppDelegate.appContext.isLogin()) {
            showValue(TYPE_RES_DATA_NULL, true);
            showValue(TYPE_RES_HIED_LOADING, null);
            return;
        }
        if (mIsFirst) {
            loadingShow();
        } else {
            showValue(TYPE_RES_SHOW_LOADING, null);
        }
        ApiControl.getApi().carInfo(new Callback.CommonCallback<ResponseModel<GarbageModel>>() {
            @Override
            public void onSuccess(ResponseModel<GarbageModel> result) {
                if (result.isSuccess()) {
                    showValue(TYPE_RES_NETWORK_ERROR_HIDE, null);
                    HashMap<Object, ArrayList<CarModel>> resultObj = result.getObj().getResult();
                    showData(resultObj);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showValue(TYPE_RES_NETWORK_ERROR_SHOW, null);
                Logger.e(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                showValue(TYPE_RES_HIED_LOADING, null);
                mNeedRefresh = false;
                mIsLoading = false;
                if (mIsFirst) {
                    mIsFirst = false;
                    dialogHide();
                }
            }
        });
    }

    private void showData(HashMap<Object, ArrayList<CarModel>> data) {
        mData = data;
        Collection<ArrayList<CarModel>> values = data.values();
        mCars.clear();
        List<RecyclerDataHolder> holder = new ArrayList<>();
        //一组商品
        int index = 0;
        for (ArrayList<CarModel> carItem : values) {
            //添加到全部数据中
            mCars.addAll(carItem);
            //显示数据
            index++;
            CarInfoDataHolder carInfoDataHolder = new CarInfoDataHolder(carItem, index);
            carInfoDataHolder.setCallBack(this);
            holder.add(carInfoDataHolder);
        }
        showValue(TYPE_RES_CAR_DATA, holder);

        updateBadge();

        countMoney();
    }

    private void updateBadge() {
        //通知购物车总体数据
        GlobalCarInfo.instance().clear();
        for (CarModel car : mCars) {
            car.mCurNum = car.getPurchaseQuantity();
            GlobalCarInfo.instance().putCarInfo(car);
        }
    }

    //计算金额
    public void countMoney() {

        int inCarNum = 0;
        float inCarMoney = 0;

        ArrayList<CarModel> cars = this.mCars;

        for (CarModel car : cars) {
            if (car.isChecked) {
                inCarNum++;
                inCarMoney += car.getProductPrice() * car.mCurNum;
            }
        }
        mSelectedNum = inCarNum;

        //更新全选ui状态
        if (inCarNum == cars.size()) {
            showValue(TYPE_RES_SELECT_STATE, true);
        } else {
            showValue(TYPE_RES_SELECT_STATE, false);
        }

        //更新数据显示
        showValue(TYPE_RES_ORDER_NUM, String.valueOf(inCarNum));
        showValue(TYPE_RES_ORDER_ALL_MONEY, String.valueOf(inCarMoney));

    }

    @Override
    public void onItemClick(int position, Object o) {
        countMoney();
    }
}
