package com.ebox.ex.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import com.android.volley.VolleyError;
import com.ebox.R;
import com.ebox.mall.ui.TradingDetailActivity;
import com.ebox.mall.warehouse.model.RespTradingListModel;
import com.ebox.mall.warehouse.model.TradingListModel;
import com.ebox.mall.warehouse.model.TradingModel;
import com.ebox.mall.warehouse.network.ErrorCode;
import com.ebox.mall.warehouse.network.request.TradingListRequest;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.ums.ui.adapter.TradingListAdapter;
import com.ebox.ums.ui.adapter.TradingListAdapter.TradingListListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

public class GegeHuozhanActivity extends CommonActivity implements
TradingListListener{
	
	private PullToRefreshGridView mRefreshScrollView;
	private TradingListAdapter mAdapter;
    private String mNextCursor = "";
    private DialogUtil dialogUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mall_activity_huozhan);
		MGViewUtil.scaleContentView(this,R.id.rootView);
		initView();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dialogUtil.closeProgressDialog();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		title.showTimer();
		initData();
	}
	@Override
	protected void onPause() {
		super.onPause();
		title.stopTimer();
	}
	private Title title;
	private TitleData data;
	private void initTitle() {
		title=(Title) findViewById(R.id.title);
		data=title.new TitleData();
		data.backVisibility=1;
		data.tvContent=getResources().getString(R.string.pub_huozhan);
		data.tvVisibility=true;
		title.setData(data, this);
	}
	private void initView(){
		mRefreshScrollView = (PullToRefreshGridView) findViewById(R.id.tradingScrollView);
		mAdapter = new TradingListAdapter(this);
        mAdapter.setListener(this);
        mRefreshScrollView.setAdapter(mAdapter);
        mRefreshScrollView.setOnItemClickListener(mAdapter);
		
		
        mRefreshScrollView.setMode(Mode.BOTH);
        mRefreshScrollView
                .setOnRefreshListener(new OnRefreshListener2<GridView>()
                {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						initData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						doTradingListRequest(mNextCursor);
					}
                });
        dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this);
		initTitle();
	}
	
//	public void resetMyself()
//	{
//		initData();
//	}
	
	private void initData()
    {
        doTradingListRequest(mNextCursor = "");
    }

    private void doTradingListRequest(String cursor)
    {
    	dialogUtil.showProgressDialog();
        TradingListRequest request = new TradingListRequest(mNextCursor,
                new ResponseEventHandler<RespTradingListModel>()
                {
                    @Override
                    public void onResponseSuccess(RespTradingListModel result)
                    {
                    	dialogUtil.closeProgressDialog();
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            TradingListModel listModel = result.getData();

                            if (mNextCursor.equals(""))
                            {
                                mAdapter.clear();
                            }

                            if (listModel.getTradings() != null
                                    && listModel.getTradings().size() > 0)
                            {
                                mAdapter.addAll(listModel.getTradings());
                                mNextCursor = listModel.getNext_cursor();
                            }

                            mAdapter.notifyDataSetChanged();
                        }

                        mRefreshScrollView.onRefreshComplete();
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                    	dialogUtil.closeProgressDialog();
                        LogUtil.i(error.getMessage());
                        mRefreshScrollView.onRefreshComplete();
                    }

                });
        //executeRequest(request);
        RequestManager.addRequest(request, this);
    }

	@Override
	public void onTradingItemClick(TradingModel model) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, TradingDetailActivity.class);
        intent.putExtra("trading_id", model.get_id());
        this.startActivity(intent);
	}
	
	@Override
    public void onStop()
    {
        super.onStop();
        dialogUtil.closeProgressDialog();

        RequestManager.cancelAll(this);
    }
}
