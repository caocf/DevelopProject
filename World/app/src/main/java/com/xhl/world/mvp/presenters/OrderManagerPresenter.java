package com.xhl.world.mvp.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.LogisticsModel;
import com.xhl.world.model.serviceOrder.Order;
import com.xhl.world.model.serviceOrder.OrderDetail;
import com.xhl.world.mvp.domain.OrderManagerUserCase;
import com.xhl.world.mvp.domain.back.ErrorEvent;
import com.xhl.world.mvp.views.OrderManagerView;
import com.xhl.world.ui.fragment.JudgeFragment;
import com.xhl.world.ui.fragment.LogisticsFragment;
import com.xhl.world.ui.fragment.OrderDetailsFragment;
import com.xhl.world.ui.fragment.ReturnGoodsFragment;
import com.xhl.world.ui.utils.DialogMaker;
import com.xhl.world.ui.view.pay.PayHelper;
import com.xhl.xhl_library.Base.Sum.SumFragmentActivity;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.common.Callback;

import java.util.List;

/**
 * Created by Sum on 16/1/5.
 */
public class OrderManagerPresenter extends Presenter {

    private OrderManagerView mViews;

    private OrderManagerUserCase mUserCase;

    private boolean isExecuting = false;
    private boolean isResume = false;

    public OrderManagerPresenter(OrderManagerUserCase userCase) {
        mUserCase = userCase;
        mUserCase.setPresenter(this);
    }

    public void attachView(OrderManagerView view) {
        mViews = view;
    }

    @Override
    public void onStart() {
        if (mViews == null) {
            throw new NullPointerException("order manager view is null");
        }

        if (mViews.isFirstLoading()) {
            mViews.showLoadingView();
        }
        mUserCase.setViewTag(mViews.viewTag());
        mUserCase.execute();
        isExecuting = true;
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onResume() {
        if (!isExecuting && mViews.needLoadData()) {
            onStart();
        }
    }

    /**
     * 支付订单
     */
    public void actionPayOrder(Order order) {

        PayHelper pay = new PayHelper((Activity) mViews.getViewContext(), new PayHelper.PayCallBack() {
            @Override
            public void success() {
                onMessageTint("付款成功");
                onStart();
            }

            @Override
            public void failed(String msg) {
                onMessageTint(msg);
            }
        });

        String price = order.getOnlinePayMoney();
        String orderCode = order.getStoreOrderCode();
        pay.payOrder(orderCode, price);

    }

    /**
     * 取消订单
     */

    public void actionCancelOrder(final Order order) {

        DialogMaker.showAlterDialog(mViews.getViewContext(), "取消订单", "您确定要取消订单吗?", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mUserCase.orderCancel(order);
            }
        });

    }

    /**
     * 确认收货
     */
    public void actionConfirmOrder(final Order order) {
        DialogMaker.showAlterDialog(mViews.getViewContext(), "确认收货", "亲，请确保您已经收到货了哦!", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mUserCase.orderConfirm(order);
            }
        });
    }

    /**
     * 查看物流
     */
    public void actionWatchCommerce(final Order order) {
        mViews.showLoadingView();
        String orderCode = order.getStoreOrderCode();
        ApiControl.getApi().orderLogistics(orderCode, new Callback.CommonCallback<ResponseModel<LogisticsModel>>() {
            @Override
            public void onSuccess(ResponseModel<LogisticsModel> result) {
                if (result.isSuccess()) {
                    Context viewContext = mViews.getViewContext();
                    if (viewContext instanceof SumFragmentActivity) {
                        LogisticsModel resultObj = result.getResultObj();
                        //添加商品图片
//                        resultObj.setProductPic(order.getProductPic());

                        ((SumFragmentActivity) viewContext).pushFragmentToBackStack(LogisticsFragment.class, resultObj);
                    }
                } else {
                    onMessageTint(result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                onMessageTint(ex.getMessage());
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

    /**
     * 提醒发货
     */
    public void actionHintSend(Order order) {
        mUserCase.orderNotify(order);
    }

    /**
     * 去评价
     */
    public void actionGoJudge(Order order, OrderDetail orderDetail) {

        order.getOrderDetailList().set(0, orderDetail);
        Context viewContext = mViews.getViewContext();
        if (viewContext instanceof SumFragmentActivity) {
            ((SumFragmentActivity) viewContext).pushFragmentToBackStack(JudgeFragment.class, order);
        }
    }

    /**
     * 申请退货
     */
    public void actionApply(Order order, OrderDetail orderDetail) {

        order.getOrderDetailList().set(0, orderDetail);

        Context viewContext = mViews.getViewContext();

        if (viewContext instanceof SumFragmentActivity) {
            ((SumFragmentActivity) viewContext).pushFragmentToBackStack(ReturnGoodsFragment.class, order);
        }
    }

    /**
     * 订单详情
     */
    public void actionOrderDetails(Order order) {
        mViews.showLoadingView();
        ApiControl.getApi().orderDetails(order.getStoreOrderCode(), new Callback.CommonCallback<ResponseModel<Order>>() {
            @Override
            public void onSuccess(ResponseModel<Order> result) {
                if (result.isSuccess()) {
                    Order order1 = result.getResultObj();
                    if (order1 == null) {
                        Logger.e("order details is null");
                        onMessageTint("订单数据异常");
                        return;
                    }
                    order1.setViewTag(mViews.viewTag());//View表示
                    Context viewContext = mViews.getViewContext();
                    if (viewContext instanceof SumFragmentActivity) {
                        ((SumFragmentActivity) viewContext).pushFragmentToBackStack(OrderDetailsFragment.class, order1);
                    }
                } else {
                    onMessageTint(result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                onMessageTint(ex.getMessage());
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

    //查询订单成功
    public void onSuccess(List<Order> data) {
        isExecuting = false;
        mViews.hideLoadingView();
        if (data == null || data.size() <= 0) {
            mViews.showNullView();
        } else {
            mViews.hideNullView();
            mViews.setLoadData(data);
        }
    }

    //查询订单失败
    public void onFailed(String errorType, String errorMsg) {
        mViews.hideLoadingView();
        if (errorType.equals(ErrorEvent.NETWORK_ERROR)) {
            if (mViews.isFirstLoading()) {
                mViews.showReLoadView();
            } else {
                mViews.showTint(errorType);
            }
        } else
            mViews.showTint(errorMsg);
    }

    public void onMessageTint(String msg) {
        mViews.showTint(msg);
    }

    //退出当前的Fragment
    public void popTopFragment() {
        Context viewContext = mViews.getViewContext();
        if (viewContext instanceof SumFragmentActivity) {
            ((SumFragmentActivity) viewContext).popTopFragment(null);
        }
    }
}
