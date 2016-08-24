package com.xhl.world.mvp.presenters;

import android.content.Intent;

import com.xhl.world.AppApplication;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.Entities.ShoppingOrderEntities;
import com.xhl.world.model.ShoppingItemDetailsModel;
import com.xhl.world.mvp.domain.ShoppingUserCase;
import com.xhl.world.mvp.domain.back.ActionCallBack;
import com.xhl.world.mvp.domain.back.ErrorEvent;
import com.xhl.world.mvp.views.ShoppingView;
import com.xhl.world.ui.activity.ConfirmOrderActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sum on 15/12/15.
 */
public class ShoppingPresenter extends Presenter implements ActionCallBack<List<ShoppingItemDetailsModel>> {

    private ShoppingView<ShoppingItemDetailsModel> mViews;
    private ShoppingUserCase mUserCase;

    private boolean isNeedRefresh;
    private boolean isBottom = false;

    public ShoppingPresenter(ShoppingUserCase userCase) {
        mUserCase = userCase;
        mUserCase.setPresenter(this);
        isNeedRefresh = true;
    }

    public void attachView(ShoppingView<ShoppingItemDetailsModel> view) {
        mViews = view;
    }

    public void onEditClick() {
        if (mViews.isEditModel()) {
            mViews.hideEditView();
        } else {
            mViews.showEditView();
        }
    }

    public void onMoveCollection() {
        mUserCase.moveToCollection();
    }

    //购买前检测购物车数量
    private void checkCarNum(final ArrayList<ShoppingOrderEntities> orders) {

        JSONArray jsonArray = new JSONArray();

        try {
            for (ShoppingOrderEntities order : orders) {
                JSONObject itemJson = new JSONObject();
                String productId = order.getGoods_id();
                itemJson.put("productId", productId);
                int quantity = order.getCount();
                itemJson.put("num", quantity);
                //添加到数组中
                jsonArray.put(itemJson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mViews.showLoadingView();

        ApiControl.getApi().verifyShopCarProduct(jsonArray.toString(), new Callback.CommonCallback<ResponseModel<HashMap<String, String>>>() {
            @Override
            public void onSuccess(ResponseModel<HashMap<String, String>> result) {
                if (result.isSuccess()) {
                    //1 正常
                    String state = result.getResultObj().get("result");
                    if (state.equals("1")) {
                        startConfirmActivity(orders);
                    } else {
                        String msg = result.getResultObj().get("message");
                        messageHint(msg);
                    }

                } else {
                    messageHint(result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                messageHint(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mViews.hideLoadingView();
            }
        });
    }


    private String initCarOrderData(List<ShoppingOrderEntities> orders) {

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            for (ShoppingOrderEntities order : orders) {
                JSONObject itemJson = new JSONObject();
                String shoppingCartId = order.getCar_id();
                itemJson.put("shoppingCartId", shoppingCartId);
                String sellerId = order.getSeller_id();
                itemJson.put("sellerId", sellerId);
                String productId = order.getGoods_id();
                itemJson.put("productId", productId);
                int quantity = order.getCount();
                itemJson.put("quantity", quantity);
                String productType = order.getGoods_type();
                itemJson.put("productType", productType);
                //添加到数组中
                jsonArray.put(itemJson);
            }
            jsonObject.putOpt("results", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    //结算订单
    public void onAccountOrder() {
        if (!mViews.isEditModel()) {
            ArrayList<ShoppingOrderEntities> orders = mUserCase.getAccountOrders();

            checkCarNum(orders);

        }
    }

    //创建初始化订单数据
    private void startConfirmActivity(ArrayList<ShoppingOrderEntities> orders) {
        //创建订单数据
        String orderInfo = initCarOrderData(orders);

        Intent intent = new Intent(mViews.getViewContext(), ConfirmOrderActivity.class);
        //结算订单信息
        intent.putExtra("orderInfo", orderInfo);

        mViews.getViewContext().startActivity(intent);
//        isNeedRefresh = true;
    }

    //控制在需要的时候，resume的时候会去更新数据，其他情况下resume不去更新，更新成功设置为true
    public void setNeedRefresh() {
        isNeedRefresh = true;
    }

    public void refreshTop() {
        boolean login = AppApplication.appContext.isLogin();
        if (login) {
            mUserCase.refreshTop();
            isBottom = false;
        } else {
            mViews.showGoodsNullView();
        }

    }

    public void refreshBottom() {
        mUserCase.refreshBottom();
        isBottom = true;
    }

    //删除订单
    public void onDeleteOrder() {
        if (mViews.isEditModel()) {
            mUserCase.deleteSelectGoods();
        }
    }

    public void addOneGoods(int position, Object model) {
        if (!mViews.isEditModel()) {
            mViews.showLoadingView();
        }
        mUserCase.addGoods(position, model);
    }

    public void reduceOneGoods(int position, Object model) {
        if (!mViews.isEditModel()) {
            mViews.showLoadingView();
        }
        mUserCase.reduceGoods(position, model);
    }

    public void selectChildGoods(int position, Object model) {
        mViews.showLoadingView();
        mUserCase.selectChildGoods(position, model);
    }

    public void unSelectChildGoods(int position, Object model) {
        mViews.showLoadingView();
        mUserCase.unSelectChildGoods(position, model);
    }

    public void selectAllChildGoods(int position, Object detailsModel) {
        mViews.showLoadingView();
        mUserCase.selectAllChildGoods(position, detailsModel);
    }

    public void unSelectAllChildGoods(int position, Object detailsModel) {
        mViews.showLoadingView();
        mUserCase.unSelectAllChildGoods(position, detailsModel);
    }


    //通知UI更新
    public void updateTotalFree(String free, int count) {
        mViews.hideLoadingView();
        mViews.updateTotalFree(free, String.valueOf(count));
    }

    public void checkItemBoxState(int itemPosition, boolean checkState) {
        mViews.hideLoadingView();
        mViews.checkItemBoxState(itemPosition, checkState);
    }

    public void updateTotalBoxState(boolean checkState) {
        mViews.hideLoadingView();
        mViews.updateTotalBoxState(checkState);
    }

    //更新一项的小计费用
    public void updateItemFree(int position, String free) {
        if (position != -1) {
            mViews.updateItemFree(position, free, null, null);
        }
    }

    @Override
    public void onStart() {
        if (mViews == null) {
            throw new NullPointerException("ShoppingView is null");
        }
        boolean login = AppApplication.appContext.isLogin();
        if (login) {
            mViews.hideGoodsNullView();
            if (mViews.isFirstLoading()) {
                mViews.changeCountPanel(false);
            }
            mViews.showLoadingView();
            //获取订单数据
            mUserCase.execute();
        } else {
            mViews.showGoodsNullView();
        }
    }

    @Override
    public void onResume() {
        if (mViews.isEditModel()) {
            mViews.hideEditView();
        }
        if (isNeedRefresh) {
            onStart();
        } else {
            if (!AppApplication.appContext.isLogin()) {
                mViews.showGoodsNullView();
            }
        }
    }


    @Override
    public void onStop() {

    }

    @Override
    public void onSuccess(List<ShoppingItemDetailsModel> data) {
        mViews.hideLoadingView();

        isNeedRefresh = false;
        if (data == null || data.size() == 0) {
            mViews.showGoodsNullView();
            mViews.changeCountPanel(false);
        } else {
            mViews.hideGoodsNullView();
            mViews.changeCountPanel(true);
            //返回数据
//            if (!isBottom) {
//            } else {
//                mViews.addLoadData(orders);
//            }
            mViews.setLoadData(data);
        }
    }

    @Override
    public void onFailed(String errorType, String errorMsg) {

        mViews.hideLoadingView();
        if (errorType.equals(ErrorEvent.NETWORK_ERROR)) {
            mViews.changeCountPanel(false);
          /*  if (mViews.isFirstLoading()) {
                mViews.showReLoadView();
            } else {
            }*/
            mViews.msgHint(errorMsg);
        } else
            mViews.msgHint(errorType);
    }

    public void messageHint(String msg) {
        mViews.msgHint(msg);
    }

}
