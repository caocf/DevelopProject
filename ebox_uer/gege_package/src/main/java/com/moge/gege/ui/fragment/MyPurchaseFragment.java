package com.moge.gege.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.BaseAdapter;

import android.widget.Toast;
import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.data.PersistentData;
import com.moge.gege.data.dao.ShopcartDAO;
import com.moge.gege.model.GoodItemModel;
import com.moge.gege.model.MerchantListModel;
import com.moge.gege.model.OrderModel;
import com.moge.gege.model.RespOrderListModel;
import com.moge.gege.model.TradingBaseModel;
import com.moge.gege.model.TradingModel;
import com.moge.gege.model.enums.OrderStatusType;
import com.moge.gege.model.enums.PayResultType;
import com.moge.gege.model.enums.PayType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.MyTradingOrderRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.TradingOrderDetailActivity;
import com.moge.gege.ui.TradingPayTypeActivity;
import com.moge.gege.ui.adapter.OrderListAdapter;
import com.moge.gege.ui.adapter.OrderListAdapter.OrderListListener;
import com.moge.gege.ui.event.Event;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.DialogUtil;
import com.moge.gege.util.FunctionUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.pay.PayHelper;
import com.moge.gege.util.pay.PayHelper.PayHelperResultLinstener;

public class MyPurchaseFragment extends MyBaseFragment implements
        OrderListListener
{
    private OrderListAdapter mOrderListAdapter;
    private String mNextCursor = "";

    protected BaseAdapter getAdatapter()
    {
        if (mOrderListAdapter == null)
        {
            mOrderListAdapter = new OrderListAdapter(mContext);
            mOrderListAdapter.setListener(this);
        }
        return mOrderListAdapter;
    }

    protected void initData()
    {
        showLoadingView();
        doMyPurchaseList(mNextCursor = "");
    }

    protected void loadMoreData()
    {
        doMyPurchaseList(mNextCursor);
    }

    protected int getDividerHeight()
    {
        return FunctionUtil.dip2px(getActivity(), 5);
    }

    protected void onListItemClick(int position)
    {
        OrderModel model = mOrderListAdapter.getItem(position - 1);
        if (model != null)
        {
            Intent intent = new Intent(mContext,
                    TradingOrderDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("order_id", model.getOrder_id());
            intent.putExtras(bundle);
            startActivityForResult(intent, GlobalConfig.INTENT_ORDER_DETAIL);
        }
    }

    private void doMyPurchaseList(String cursor)
    {
        MyTradingOrderRequest request = new MyTradingOrderRequest(cursor,
                new ResponseEventHandler<RespOrderListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespOrderListModel> request,
                            RespOrderListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            List<OrderModel> listModel = result.getData()
                                    .getOrders();

                            if (mNextCursor.equals(""))
                            {
                                mOrderListAdapter.clear();
                            }

                            if (listModel != null && listModel.size() > 0)
                            {
                                mOrderListAdapter.addAll(listModel);
                                mOrderListAdapter.notifyDataSetChanged();

                                mNextCursor = result.getData().getNext_cursor();
                            }
                            else
                            {
                                ToastUtil.showToastShort(R.string.no_more_data);
                                showLoadEmptyView();
                            }
                        }
                        else
                        {
                            showLoadFailView(result.getMsg());
                        }

                        stopRefreshUI();
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                        stopRefreshUI();
                        showLoadFailView(null);
                    }

                });
        executeRequest(request);
    }

    private List<TradingBaseModel> transferDataModel(List<OrderModel> orders)
    {
        List<TradingBaseModel> tradingsModel = new ArrayList<TradingBaseModel>();

        for (OrderModel orderModel : orders)
        {
            TradingBaseModel tradingBaseModel = new TradingBaseModel();

            GoodItemModel goodModel = orderModel.getGoods().get(0);
            TradingModel tradingModel = goodModel.getTrading();

            tradingBaseModel.setAttachments(tradingModel.getAttachments());
            tradingBaseModel
                    .setDiscount_price(tradingModel.getDiscount_price());
            tradingBaseModel.setNum(tradingModel.getNum());
            tradingBaseModel.setTitle(tradingModel.getTitle());
            tradingBaseModel.setPrice(tradingModel.getPrice());
            tradingBaseModel.set_id(tradingModel.get_id());
            tradingBaseModel.setAuthor_uid(tradingModel.getAuthor_uid());
            tradingBaseModel.setBuyNum(goodModel.getNum());
            tradingsModel.add(tradingBaseModel);

            tradingBaseModel.setStatus(orderModel.getStatus());
            tradingBaseModel.setStatusDesc(orderModel.getStatus_verbose_name());
            tradingBaseModel.setOrderTime(orderModel.getOrder_at());
            if (orderModel.getGoods().size() > 1)
            {
                tradingBaseModel.setMultiGoods(true);
            }
            else
            {
                tradingBaseModel.setMultiGoods(false);
            }
        }

        return tradingsModel;
    }

    @Override
    public void onOrderPayBtnClick(OrderModel model)
    {
        if(model != null && model.getStatus() == OrderStatusType.PAY_ING)
        {
            new PayHelper(getActivity(), null).cancelPayOrder(model.getPay_type(), model.getPay_id());
        }

        UIHelper.showPayTypeActivity(getActivity(), model);
    }

    public void onEventMainThread(Event.PayEvent event)
    {
        this.initData();
    }
}
