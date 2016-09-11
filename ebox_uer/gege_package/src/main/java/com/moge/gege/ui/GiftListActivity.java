package com.moge.gege.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.moge.gege.R;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.GiftListModel;
import com.moge.gege.model.GiftModel;
import com.moge.gege.model.RespGiftListModel;
import com.moge.gege.model.enums.GiftOperType;
import com.moge.gege.model.enums.GiftType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.GiftListRequest;
import com.moge.gege.network.request.OtherGiftListRequest;
import com.moge.gege.network.request.SendGiftRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.GiftListAdapter;
import com.moge.gege.ui.adapter.GiftListAdapter.GiftListListener;
import com.moge.gege.ui.adapter.RecvGiftListAdapter;
import com.moge.gege.ui.event.Event;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.widget.CustomDialog;

import de.greenrobot.event.EventBus;

public class GiftListActivity extends BaseActivity implements GiftListListener
{
    private Context mContext;
    private PullToRefreshGridView mRefreshGridView;
    private GridView mGridView;
    private GiftListAdapter mAdapter;
    private RecvGiftListAdapter mRecvAdapter;
    private String mNextCursor = "";

    private String mUid;
    private GiftModel mSelectGiftModel;
    private boolean mRefreshGiftList = false;
    private int mOpertype = GiftOperType.SEND_GIFT;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giftlist);

        mUid = getIntent().getStringExtra("uid");
        mOpertype = getIntent().getIntExtra("opertype", GiftOperType.SEND_GIFT);

        mContext = GiftListActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        if (mOpertype == GiftOperType.SEND_GIFT)
        {
            this.setHeaderLeftTitle(R.string.gift_window);
        }
        else
        {
            this.setHeaderLeftTitle(R.string.my_gift);
        }

        mRefreshGridView = (PullToRefreshGridView) this
                .findViewById(R.id.giftGridView);
        mGridView = mRefreshGridView.getRefreshableView();

        if (mOpertype == GiftOperType.SEND_GIFT)
        {
            mAdapter = new GiftListAdapter(mContext);
            mAdapter.setListener(this);
            mGridView.setAdapter(mAdapter);
            mGridView.setOnItemClickListener(mAdapter);
        }
        else
        {
            mRecvAdapter = new RecvGiftListAdapter(mContext);
            mGridView.setAdapter(mRecvAdapter);
        }

        mRefreshGridView.setMode(Mode.BOTH);
        //        mRefreshGridView.setRefreshing();
        mRefreshGridView.setEmptyView(getLoadingView());
        mRefreshGridView
                .setOnRefreshListener(new OnRefreshListener2<GridView>()
                {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<GridView> refreshView)
                    {
                        initData();
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<GridView> refreshView)
                    {
                        if (mOpertype == GiftOperType.SEND_GIFT)
                        {
                            doGiftListRequest(mNextCursor);
                        }
                        else
                        {
                            doOtherGiftListRequest(mUid, mNextCursor);
                        }
                    }
                });

    }

    @Override
    protected void onHeaderLeftClick()
    {
        Intent intent = new Intent();
        intent.putExtra("refresh_gift", mRefreshGiftList);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void initData()
    {
        showLoadingView();

        if (mOpertype == GiftOperType.SEND_GIFT)
        {
            doGiftListRequest(mNextCursor = "");
        }
        else
        {
            doOtherGiftListRequest(mUid, mNextCursor = "");
        }
    }

    private void doGiftListRequest(String cursor)
    {
        GiftListRequest request = new GiftListRequest(cursor,
                new ResponseEventHandler<RespGiftListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespGiftListModel> request,
                            RespGiftListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            GiftListModel listModel = result.getData();

                            // toped list
                            if (mNextCursor.equals(""))
                            {
                                mAdapter.clear();
                            }

                            if (listModel.getGift() != null
                                    && listModel.getGift().size() > 0)
                            {
                                mAdapter.addAll(listModel.getGift());
                                mAdapter.notifyDataSetChanged();

                                mNextCursor = listModel.getNext_cursor();
                            }
                            else
                            {
                                ToastUtil.showToastShort(R.string.no_more_data);
                                hideLoadingView();
                            }
                        }
                        else
                        {
                            showLoadFailView(result.getMsg());
                        }

                        mRefreshGridView.onRefreshComplete();
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                        mRefreshGridView.onRefreshComplete();
                        showLoadFailView(null);
                    }

                });
        executeRequest(request);
    }

    private void doOtherGiftListRequest(String uid, String cursor)
    {
        OtherGiftListRequest request = new OtherGiftListRequest(uid, cursor,
                new ResponseEventHandler<RespGiftListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespGiftListModel> request,
                            RespGiftListModel result)
                    {

                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            GiftListModel listModel = result.getData();

                            // toped list
                            if (mNextCursor.equals(""))
                            {
                                mRecvAdapter.clear();
                            }

                            if (listModel.getGifts() != null
                                    && listModel.getGifts().size() > 0)
                            {

                                mRecvAdapter.addAll(listModel.getGifts());
                                mRecvAdapter.notifyDataSetChanged();

                                mNextCursor = listModel.getNext_cursor();
                            }
                            else
                            {
                                ToastUtil.showToastShort(R.string.no_more_data);
                                hideLoadingView();
                            }
                        }
                        else
                        {
                            showLoadFailView(result.getMsg());
                        }

                        mRefreshGridView.onRefreshComplete();
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                        mRefreshGridView.onRefreshComplete();
                        showLoadFailView(null);
                    }

                });
        executeRequest(request);
    }

    private void doSendGiftRequest(String uid, String gid, int gtype)
    {
        SendGiftRequest request = new SendGiftRequest(uid, gid, gtype,
                new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request, BaseModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mRefreshGiftList = true;
                            EventBus.getDefault().post(new Event.PointEvent());
                            ToastUtil.showToastShort("您的礼物已送达~");
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                        }

                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                    }

                });
        executeRequest(request);
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
    public void onGiftItemClick(GiftModel model)
    {
        if (mOpertype != GiftOperType.SEND_GIFT)
        {
            return;
        }

        mSelectGiftModel = model;

        new CustomDialog.Builder(mContext)
                .setTitle(R.string.general_tips)
                .setMessage(R.string.send_gift_tips)
                .setPositiveButton(R.string.general_confirm,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which)
                            {
                                doSendGiftRequest(mUid,
                                        mSelectGiftModel.get_id(),
                                        GiftType.VIRTUAL_GIFT);
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

}
