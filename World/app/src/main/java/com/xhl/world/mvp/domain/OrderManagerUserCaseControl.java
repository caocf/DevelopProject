package com.xhl.world.mvp.domain;

import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.Response;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.serviceOrder.Order;
import com.xhl.world.model.serviceOrder.OrderDetail;
import com.xhl.world.mvp.domain.back.ErrorEvent;
import com.xhl.world.mvp.presenters.OrderManagerPresenter;
import com.xhl.world.mvp.presenters.Presenter;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sum on 16/1/5.
 */
public class OrderManagerUserCaseControl implements OrderManagerUserCase, Callback.CommonCallback<ResponseModel<Response<Order>>> {

    /**
     * 1    查询全部
     * 2    查询未付款
     * 3    查询待发货
     * 4    查询待收货
     * 5    查询未评价的订单
     * 6    查询退货订单
     */
    private int mViewTag;
    private OrderManagerPresenter mPresenter;

    private List<Order> mResOrders;

    OrderDetail mCloneOrderDetails = new OrderDetail();


    private int mCurPage = 0;
    private int mCurPageSize = 20;

    private boolean mLoading = false;

    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = (OrderManagerPresenter) presenter;
        mResOrders = new ArrayList<>();
    }

    @Override
    public void setViewTag(int tag) {
        mViewTag = tag;
    }

    @Override
    public void orderCancel(Order order) {
        ApiControl.getApi().orderUpdateState("0", order, new CommonCallback<ResponseModel<Integer>>() {
            @Override
            public void onSuccess(ResponseModel<Integer> result) {
                if (result.isSuccess()) {
                    if (result.getResultObj() == 1) {
                        mPresenter.onMessageTint("取消成功");
                        mPresenter.popTopFragment();
                        execute();//刷新数据
                    } else {
                        mPresenter.onMessageTint("取消订单异常,请稍后再试~");
                    }
                } else {
                    mPresenter.onFailed(ErrorEvent.OTHER_ERROR, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mPresenter.onFailed(ErrorEvent.NETWORK_ERROR, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void orderConfirm(Order order) {
        ApiControl.getApi().orderUpdateState("1", order, new CommonCallback<ResponseModel<Integer>>() {
            @Override
            public void onSuccess(ResponseModel<Integer> result) {
                if (result.isSuccess()) {
                    if (result.getResultObj() == 1) {
                        mPresenter.popTopFragment();
                        execute();
                    } else {
                        mPresenter.onMessageTint("确认收货异常,请稍后再试~");
                    }
                } else {
                    mPresenter.onFailed(ErrorEvent.OTHER_ERROR, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mPresenter.onFailed(ErrorEvent.NETWORK_ERROR, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void orderNotify(Order order) {

    }


    @Override
    public void execute() {
        if (!mLoading) {
            mLoading = true;
            //根据订单查询状态
            ApiControl.getApi().orderQueryByState(mCurPage, mCurPageSize, String.valueOf(mViewTag), this);
        }
    }

    //返回排序后的数据
    private void lastDo() {
        //sort
        Collections.sort(mResOrders, new Comparator<Order>() {
            @Override
            public int compare(Order lhs, Order rhs) {
                int res;
                if (lhs.getOrderState() == rhs.getOrderState()) {
                    res = 0;
                } else if (lhs.getOrderState() > rhs.getOrderState()) {
                    res = 1;
                } else {
                    res = -1;
                }
                return res;
            }
        });
        mPresenter.onSuccess(mResOrders);
    }

    //重置排序订单
    private void resetOrders(List<Order> orders) {
        if (orders == null || orders.size() == 0) {
            mPresenter.onSuccess(orders);
            return;
        }
        //按照store_order_code分类
        HashMap<String, List<Order>> serviceData = new HashMap();
        for (Order order : orders) {
            //唯一区分id
            String orderCodeKey = order.getStoreOrderCode();
            boolean containsKey = serviceData.containsKey(orderCodeKey);
            if (containsKey) {
                List<Order> models = serviceData.get(orderCodeKey);
                models.add(order);
            } else {
                //不存在创建新的集合存放
                List<Order> list = new ArrayList<>();
                list.add(order);
                serviceData.put(orderCodeKey, list);
            }
        }

        //重组订单集合
        Set<Map.Entry<String, List<Order>>> entries = serviceData.entrySet();

        mResOrders.clear();

        for (Map.Entry<String, List<Order>> entry : entries) {
            //至少包含了一个商品
            List<Order> childOrder = entry.getValue();

            List<OrderDetail> orderDetails = new ArrayList<>();
            Order resOrder = childOrder.get(0);

            for (Order order : childOrder) {
                orderDetails.add(createOrderDetails(order));
            }
            //设置订单详情数据
            resOrder.setOrderDetailList(orderDetails);
            //设置当前订单所在数据
            resOrder.setViewTag(mViewTag);

            mResOrders.add(resOrder);
        }

        lastDo();
    }

    //获取当前订单中的商品信息
    private OrderDetail createOrderDetails(Order order) {
        OrderDetail detail = null;
        try {
            detail = mCloneOrderDetails.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (detail == null) {
            detail = new OrderDetail();
        }
        detail.setReturnStatus(order.getReturnsStatus());//退货状态
        detail.setProductName(order.getProductName());//商品名称
        detail.setProductPic(order.getProductPic());//商品图片
        detail.setGoodId(order.getProductId());//商品id
        detail.setNum(order.getNum());//商品数量
        detail.setUnitPrice(order.getProductPrice());//商品单价

        detail.setEvaluateStatus(order.getEvaluateStatus());//订单评价状态

        return detail;
    }


    @Override
    public void onSuccess(ResponseModel<Response<Order>> result) {
        if (result.isSuccess()) {
            List<Order> rows = result.getResultObj().getRows();
            resetOrders(rows);
        } else {
            mPresenter.onFailed(ErrorEvent.OTHER_ERROR, result.getMessage());
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        mPresenter.onFailed(ErrorEvent.NETWORK_ERROR, ex.getMessage());
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {
        mLoading = false;
    }
}
