package com.xhl.bqlh.business.doman;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.App.OrderQueryCondition;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.OrderModel;
import com.xhl.bqlh.business.doman.callback.ContextValue;
import com.xhl.bqlh.business.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.bqlh.business.view.ui.recyclerHolder.OrderDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/5/9.
 */
public class OrderQueryHelper extends BaseValue implements Callback.CommonCallback<ResponseModel<OrderModel>>, RecycleViewCallBack {

    public static final int TYPE_SHOW_RES_ADD = 1;
    public static final int TYPE_SHOW_RES_SET = 2;

    public static final int TYPE_SHOW_REFRESH_TRUE = 11;
    public static final int TYPE_SHOW_REFRESH_FALSE = 12;
    public static final int TYPE_SHOW_CANCEL = 13;//取消订单


    private boolean mIsQuerying = false;
    private boolean mHasLoadSuccess = false;

    private OrderQueryCondition mCondition;

    Callback.Cancelable mCancel;

    public OrderQueryHelper(ContextValue value) {
        super(value);
    }

    public void onVisibleLoad() {
        if (mHasLoadSuccess) {
            return;
        }
        onRefreshTop();
    }

    public void onRefreshTop() {
        if (mValue == null) {
            return;
        }
        mCondition = (OrderQueryCondition) mValue.getValue(0);
        mPageIndex = 1;
        showValue(TYPE_SHOW_REFRESH_TRUE, null);
        loadData();
    }

    public void onRefreshMore() {
        if (mValue == null) {
            return;
        }
        mCondition = (OrderQueryCondition) mValue.getValue(0);
        if (mPageIndex * mPageNum > mTotalSize) {
            toastShow("没有更多数据了");
            showValue(TYPE_SHOW_REFRESH_FALSE, null);
        } else {
            mPageIndex++;
            loadData();
        }
    }

    private void loadData() {
        if (mIsQuerying) {
            return;
        }
        mIsQuerying = true;
        mCancel = ApiControl.getApi().orderQuery(mPageNum, mPageIndex, mCondition, this);
    }

    private void createData(List<OrderModel> orders) {
        if (orders.size() == 0 && mPageIndex > 1) {
            toastShow("没有更多数据了");
            return;
        }

        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (OrderModel order : orders) {
            order.isNeedShowType = mCondition.isNeedOrderTag;
            OrderDataHolder holder = new OrderDataHolder(order);
            holder.setCallBack(this);
            holders.add(holder);
        }
        if (mPageIndex > 1) {
            showValue(TYPE_SHOW_RES_ADD, holders);
        } else {
            showValue(TYPE_SHOW_RES_SET, holders);
        }
    }

    @Override
    public void onDestroy() {
        if (mCancel != null) {
            mCancel.cancel();
        }
    }

    @Override
    public void onSuccess(ResponseModel<OrderModel> result) {
        if (result.isSuccess()) {
            mHasLoadSuccess = true;
            mTotalSize = result.getPageSize();
            createData(result.getObjList());
        } else {
            toastShow(result.getMessage());
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        toastShow(ex.getMessage());
    }

    @Override
    public void onCancelled(CancelledException cex) {
    }

    @Override
    public void onFinished() {
        mIsQuerying = false;
        showValue(TYPE_SHOW_REFRESH_FALSE, null);
    }

    @Override
    public void onItemClick(int position) {
        showValue(TYPE_SHOW_CANCEL, position);
    }
}
