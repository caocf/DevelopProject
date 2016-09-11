package com.moge.gege.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.moge.gege.R;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.CourierListModel;
import com.moge.gege.model.CourierModel;
import com.moge.gege.model.GiftListModel;
import com.moge.gege.model.GiftModel;
import com.moge.gege.model.RespCourierListModel;
import com.moge.gege.model.RespGiftListModel;
import com.moge.gege.model.enums.GiftOperType;
import com.moge.gege.model.enums.GiftType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.CourierListRequest;
import com.moge.gege.network.request.GiftListRequest;
import com.moge.gege.network.request.OtherGiftListRequest;
import com.moge.gege.network.request.SendGiftRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.CourierListAdapter;
import com.moge.gege.ui.adapter.GiftListAdapter;
import com.moge.gege.ui.adapter.GiftListAdapter.GiftListListener;
import com.moge.gege.ui.adapter.RecvGiftListAdapter;
import com.moge.gege.ui.event.Event;
import com.moge.gege.util.DeviceInfoUtil;
import com.moge.gege.util.FunctionUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.widget.CustomDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;

public class CourierListActivity extends BaseActivity implements CourierListAdapter.CourierListListener {
    private Context mContext;

    private PullToRefreshListView mRefreshListView;
    private CourierListAdapter mAdapter;
    private String mNextCursor = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courierlist);

        mContext = CourierListActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView() {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(R.string.send_delivery);

        mRefreshListView = (PullToRefreshListView)findViewById(R.id.courierList);
        mRefreshListView.setMode(Mode.BOTH);
        mRefreshListView.setEmptyView(getLoadingView());
        mRefreshListView
                .setOnRefreshListener(new OnRefreshListener2<ListView>() {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        initData();
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        doCourierListRequest(mNextCursor);
                    }
                });

        mAdapter = new CourierListAdapter(mContext);
        mAdapter.setListener(this);
        mRefreshListView.setOnItemClickListener(mAdapter);
        mRefreshListView.setAdapter(mAdapter);
    }

    private void initData() {
        showLoadingView();
        doCourierListRequest(mNextCursor = "");
    }

    private void doCourierListRequest(String cursor) {
        CourierListRequest request = new CourierListRequest(cursor,
                new ResponseEventHandler<RespCourierListModel>() {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespCourierListModel> request,
                            RespCourierListModel result) {
                        if (result.getStatus() == ErrorCode.SUCCESS) {
                            CourierListModel listModel = result.getData();

                            // toped list
                            if (mNextCursor.equals("")) {
                                mAdapter.clear();
                            }

                            if (listModel.getDeliverys() != null
                                    && listModel.getDeliverys().size() > 0) {
                                mAdapter.addAll(listModel.getDeliverys());
                                mAdapter.notifyDataSetChanged();

                                mNextCursor = listModel.getNext_cursor();
                            } else {
                                ToastUtil.showToastShort(R.string.no_more_data);
                                if (mAdapter.getCount() == 0) {
                                    showLoadEmptyView();
                                } else {
                                    hideLoadingView();
                                }

                            }
                        } else {
                            showLoadFailView(result.getMsg());
                        }

                        mRefreshListView.onRefreshComplete();
                    }

                    @Override
                    public void onResponseError(VolleyError error) {
                        ToastUtil.showToastShort(error.getMessage());
                        mRefreshListView.onRefreshComplete();
                        showLoadFailView(null);
                    }

                });
        executeRequest(request);
    }

    public void onCourierItemClick(final CourierModel model)
    {
        new CustomDialog.Builder(mContext)
                .setTitle(model.getMobile())
                .setMessage(null)
                .setPositiveButton(R.string.call,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                DeviceInfoUtil.startCall(mContext, model.getMobile());
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(R.string.general_cancel,
                        new DialogInterface.OnClickListener()
                        {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                dialog.dismiss();
                            }

                        }).create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            default:
                break;
        }
    }


}
