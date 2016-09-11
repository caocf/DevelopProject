package com.moge.gege.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.model.DeliveryBoxListModel;
import com.moge.gege.model.DeliveryBoxModel;
import com.moge.gege.model.RespUserDeliveryListModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.GetUserDeliveryRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.CaptureActivity;
import com.moge.gege.ui.CourierListActivity;
import com.moge.gege.ui.HomeActivity;
import com.moge.gege.ui.adapter.UserDeliveryListAdapter;
import com.moge.gege.ui.event.Event;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.ui.widget.DeliveryTopView;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;

import java.util.List;

public class DeliveryFragment extends BaseFragment implements DeliveryTopView.DeliveryTopViewListener
{
    private Context mContext;
    private PullToRefreshListView mDeliveryListView;
    private UserDeliveryListAdapter mAdapter;

    private String mNextCursor = "";
    private boolean mHaveQueryNewTopic = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        final View layout = inflater.inflate(R.layout.fragment_delivery,
                container, false);
        initView(layout);
        initLocalData();
        initData();
        return layout;
    }

    @Override
    protected void initView(View v)
    {
        super.initView(v);

        mContext = getActivity();

        this.setHeaderBackground(mContext.getResources().getColor(R.color.white));
        this.setHeaderLeftImage(R.drawable.icon_new_logo);
        this.setHeaderLeftBackgroundImage(0);

        mDeliveryListView = (PullToRefreshListView) v
                .findViewById(R.id.deliveryListView);
        mDeliveryListView.setMode(Mode.BOTH);
        mDeliveryListView.setRefreshing();
        mDeliveryListView
                .setOnRefreshListener(new OnRefreshListener2<ListView>()
                {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView)
                    {
                        initData();
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView)
                    {
                        doUserDelivery(mNextCursor);
                    }
                });

        DeliveryTopView topView = new DeliveryTopView(mContext);
        topView.setListener(this);
        mDeliveryListView.getRefreshableView().addHeaderView(topView, null, false);

        mAdapter = new UserDeliveryListAdapter(mContext);
        mDeliveryListView.setAdapter(mAdapter);

        mDeliveryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                DeliveryBoxModel model = mAdapter.getItem(i - 2);
                UIHelper.showDeliveryQueryActivity(mContext, model.getNumber(), model.getDelivery_type());
            }
        });
    }

    private void setListViewHeight(int height){
        ViewGroup.LayoutParams params = mDeliveryListView.getLayoutParams();
        params.height = height;
        mDeliveryListView.setLayoutParams(params);
    }

    private void initLocalData()
    {

    }

    private void initData()
    {
        setListViewHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        showLoadingView();
        doUserDelivery(mNextCursor = "");
    }


    private void doUserDelivery(String cursor)
    {
        GetUserDeliveryRequest request = new GetUserDeliveryRequest(cursor,
                new ResponseEventHandler<RespUserDeliveryListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespUserDeliveryListModel> request,
                            RespUserDeliveryListModel result)
                    {
                        setListViewHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                        mDeliveryListView.onRefreshComplete();

                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            if (TextUtils.isEmpty(mNextCursor))
                            {
                                mAdapter.clear();
                            }

                            DeliveryBoxListModel listModel = result.getData();
                            if (listModel != null
                                    && listModel.getDeliverys() != null
                                    && listModel.getDeliverys().size() > 0)
                            {
                                mNextCursor = listModel.getNext_cursor();
                                mAdapter.addAll(listModel.getDeliverys());

                                setListViewHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                                hideLoadingView();
                            }
                            else
                            {
                                ToastUtil.showToastShort(R.string.no_more_data);
                                showLoadEmptyView();
                            }

                            findFirstFinishDeliveryOrder(mAdapter.getAll());
                            mAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                            showLoadFailView(result.getMsg());
                        }

                        mDeliveryListView.onRefreshComplete();
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                        ToastUtil.showToastShort(error.getMessage());
                        mDeliveryListView.onRefreshComplete();

                        setListViewHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                        showLoadFailView(null);
                    }

                });
        executeRequest(request);
    }

    private void findFirstFinishDeliveryOrder(List<DeliveryBoxModel> list)
    {
        for(DeliveryBoxModel model : list)
        {
            if(model.getState() >= 4 && model.getState() <= 7)
            {
                model.setShowHeader(true);
                break;
            }
        }
    }


    @Override
    public void onResume()
    {
        super.onResume();


    }

    @Override
    public void onPause()
    {
        super.onPause();

        mHaveQueryNewTopic = false;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK)
        {
            return;
        }

        switch (requestCode)
        {
            default:
                break;
        }
    }


    @Override
    public void onSendDeliveryClick()
    {
        if (!AppApplication.checkLoginState((Activity)mContext)){
            return;
        }

        startActivity(new Intent(mContext, CourierListActivity.class));
    }

    @Override
    public void onScanOpenBoxClick(){

        //ToastUtil.showToastShort(R.string.please_look_forward);
        if (!AppApplication.checkLoginState((Activity)mContext)){
            return;
        }
        Intent intent = new Intent(mContext, CaptureActivity.class);
        intent.putExtra(CaptureActivity.SCAN_TYPE,CaptureActivity.SCAN_TYPE_OPEN_EBOX);
        startActivity(intent);
    }

    @Override
    public void onSearchDeliveryClick(){

        if (!AppApplication.checkLoginState((Activity)mContext)){
            return;
        }

        UIHelper.showDeliveryQueryActivity(mContext, "", "");
    }

    @Override
    public void onScanDeliveryClick(){

        if (!AppApplication.checkLoginState((Activity)mContext)){
            return;
        }

        Intent intent = new Intent(mContext, CaptureActivity.class);
        intent.putExtra(CaptureActivity.SCAN_TYPE,CaptureActivity.SCAN_TYPE_DETAILS_ORDER);
        startActivity(intent);
    }

    public void onEvent(Event.RefreshEvent event)
    {
        if(event.getRefreshIndex() != HomeActivity.INDEX_DELIVERY)
        {
            return;
        }

        initData();
        onHeaderDoubleClick();
    }

    public void onEvent(Event.LoginEvent event)
    {
        initData();
        onHeaderDoubleClick();
    }

}
