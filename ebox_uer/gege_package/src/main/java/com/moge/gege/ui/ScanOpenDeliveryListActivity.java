package com.moge.gege.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.moge.gege.R;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.ScanEboxDataModel;
import com.moge.gege.model.ScanEboxModel;
import com.moge.gege.model.ScanEboxOrderModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.GetScanDeliveryRequest;
import com.moge.gege.network.request.ScanOpenDoorRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.ScanOpenDeliveryAdapter;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.DialogUtil;
import com.moge.gege.util.ToastUtil;

import java.util.List;

import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class ScanOpenDeliveryListActivity extends BaseActivity implements ScanOpenDeliveryAdapter.OnItemDoorClick {
    private Context mContext;

    PullToRefreshListView mSearchView;


    private String mQueryOrder;

    private ScanOpenDeliveryAdapter mAdapter;

    private String mAccess_token;

    private Dialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_open_list);
        mQueryOrder = getIntent().getStringExtra("query_order");

        mContext = ScanOpenDeliveryListActivity.this;

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {

        if (mDialog!=null&&mDialog.isShowing()) {
            mDialog.dismiss();
        }

        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {
        super.initView();
        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(R.string.scan_open_ebox);
        mSearchView= (PullToRefreshListView) findViewById(R.id.pull_to_refresh_order);
        mSearchView.setMode(PullToRefreshBase.Mode.DISABLED);
        mAdapter = new ScanOpenDeliveryAdapter(this);
        mSearchView.setAdapter(mAdapter);
        mAdapter.setListener(this);

    }

    private void setData(List<ScanEboxOrderModel> data) {
        if (data.size()==0) {
            ToastUtil.showToastShort(R.string.get_order_failed);
        }
        mAdapter.setData(data);
    }

    private void initData() {
        mDialog=DialogUtil.createProgressDialog(this,getString(R.string.load_deliver_info));
        if (mDialog!=null)
        {
            mDialog.show();
        }
        GetScanDeliveryRequest request = new GetScanDeliveryRequest(mQueryOrder, new ResponseEventHandler<ScanEboxModel>() {
            @Override
            public void onResponseSucess(BaseRequest<ScanEboxModel> request, ScanEboxModel result) {
                mDialog.dismiss();
                if (result.getStatus() == ErrorCode.SUCCESS) {
                    setData(result.getData().getOrders());
                    mAccess_token=result.getData().getAccess_token();
                }
                else  {
                    ToastUtil.showToastShort(R.string.get_order_failed);
                }
            }

            @Override
            public void onResponseError(VolleyError error) {
                mDialog.dismiss();
                ToastUtil.showToastShort(R.string.get_order_failed);
            }
        });

        executeRequest(request);

    }


    @Override
    public void ItemClick(final int position,final String orderId, final String doorNum) {


        DialogUtil.createConfirmDialog(mContext,
                getString(R.string.general_tips),
                getString(R.string.confirm_open_box_door),
                getString(R.string.general_confirm),
                getString(R.string.general_cancel),
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,
                                        int which)
                    {
                        dialog.dismiss();
                        openDoor(position,orderId, doorNum);
                    }
                }, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,
                                        int which)
                    {
                        dialog.dismiss();
                    }
                }).show();
    }


    private void openDoor(final int pos,String orderId,String doorNum) {
        mDialog=DialogUtil.createProgressDialog(this,"正在打开:"+doorNum);
        if (mDialog!=null)
        {
            mDialog.setCancelable(false);
            mDialog.show();
        }
        ScanOpenDoorRequest openRequest = new ScanOpenDoorRequest(mAccess_token, orderId, new ResponseEventHandler<BaseModel>() {
            @Override
            public void onResponseSucess(BaseRequest<BaseModel> request, BaseModel result) {
                mDialog.dismiss();
                if (result.getStatus() == ErrorCode.SUCCESS)
                {
                    ToastUtil.showToastShort(R.string.open_box_door_success);
                    mAdapter.checkState(pos);
                } else {
                    ToastUtil.showToastShort(result.getMsg());
                }
            }
            @Override
            public void onResponseError(VolleyError error) {
                mDialog.dismiss();
                ToastUtil.showToastShort(R.string.open_box_door_failed);
            }
        });

        executeRequest(openRequest);
    }
}
