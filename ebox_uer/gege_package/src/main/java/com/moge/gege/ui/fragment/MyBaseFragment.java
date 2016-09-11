package com.moge.gege.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;

public abstract class MyBaseFragment extends BaseFragment
{
    public Context mContext;
    private PullToRefreshListView mPullRefreshListView;
    protected ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        final View layout = inflater.inflate(R.layout.fragment_mybaselist,
                container, false);
        initView(layout);
        initData();
        return layout;
    }

    @Override
    protected void initView(View v)
    {
        super.initView(v);

        mContext = getActivity();

        mPullRefreshListView = (PullToRefreshListView) v
                .findViewById(R.id.contentList);
        mPullRefreshListView.setMode(Mode.BOTH);
        mPullRefreshListView.setEmptyView(getLoadingView());
        mListView = mPullRefreshListView.getRefreshableView();
        mListView.setDividerHeight(getDividerHeight());
        BaseAdapter adapter = getAdatapter();
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id)
            {
                onListItemClick(position);
            }
        });

        mPullRefreshListView
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
                        loadMoreData();
                    }

                });
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
            case GlobalConfig.INTENT_TOPIC_DETAIL:
            case GlobalConfig.INTENT_ORDER_DETAIL:
                initData();
                break;
            case GlobalConfig.INTENT_SELECT_PAYTYPE:

                break;
            default:
                break;
        }
    }

    protected void stopRefreshUI()
    {
        mPullRefreshListView.onRefreshComplete();
    }

    protected abstract BaseAdapter getAdatapter();

    protected abstract void initData();

    protected abstract void loadMoreData();

    protected abstract void onListItemClick(int position);

    protected abstract int getDividerHeight();

}
