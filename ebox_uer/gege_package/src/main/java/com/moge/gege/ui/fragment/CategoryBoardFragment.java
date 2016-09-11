package com.moge.gege.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.moge.gege.R;
import com.moge.gege.model.BaseOptionModel;
import com.moge.gege.model.BoardModel;
import com.moge.gege.model.RespBoardListModel;
import com.moge.gege.model.RespCategoryListModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.BoardCategoryRequest;
import com.moge.gege.network.request.CategoryBoardRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.CategoryBoardListAdapter;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;

public class CategoryBoardFragment extends BaseFragment implements
        OnClickListener, OnGroupClickListener, OnChildClickListener

{
    private Context mContext;
    private PullToRefreshExpandableListView mPullRefreshListView;
    private ExpandableListView mListView;
    private CategoryBoardListAdapter mAdapter;
    private List<BaseOptionModel> mCategoryList = new ArrayList<BaseOptionModel>();
    private HashMap<Integer, List<BoardModel>> mBoardListMap = new HashMap<Integer, List<BoardModel>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        final View layout = inflater.inflate(R.layout.fragment_category_board,
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

        mPullRefreshListView = (PullToRefreshExpandableListView) v.findViewById(
                R.id.categoryListView);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullRefreshListView.setEmptyView(getLoadingView());
        mListView = mPullRefreshListView.getRefreshableView();
        mListView.setOnGroupClickListener(this);
        mListView.setOnChildClickListener(this);
        mAdapter = new CategoryBoardListAdapter(mContext, mCategoryList,
                mBoardListMap);
        mListView.setAdapter(mAdapter);

        mPullRefreshListView.setOnRefreshListener(
                new PullToRefreshBase.OnRefreshListener<ExpandableListView>()
                {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ExpandableListView> refreshView)
                    {
                        initData();
                    }
                });

    }

    private void initData()
    {
        showLoadingView();
        doBoardCategoryRequest();
    }

    private void doBoardCategoryRequest()
    {
        BoardCategoryRequest request = new BoardCategoryRequest(

                new ResponseEventHandler<RespCategoryListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespCategoryListModel> request,
                            RespCategoryListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mCategoryList.clear();

                            List<BaseOptionModel> listModel = result.getData()
                                    .getCategorys();
                            if (listModel != null && listModel.size() > 0)
                            {
                                mCategoryList.addAll(result.getData()
                                        .getCategorys());

                                hideLoadingView();
                            }
                            else
                            {
                                showLoadEmptyView();
                            }

                            mAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            showLoadFailView(result.getMsg());
                        }

                        mPullRefreshListView.onRefreshComplete();
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                        mPullRefreshListView.onRefreshComplete();
                        showLoadFailView(null);
                    }

                });
        executeRequest(request);
    }

    private void doBoardByCategoryRequest(int position, String categoryId)
    {
        CategoryBoardRequest request = new CategoryBoardRequest(position,
                categoryId, "", null);

        request.setResponseListener(
                new ResponseEventHandler<RespBoardListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespBoardListModel> request,
                            RespBoardListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            List<BoardModel> listModel = result.getData()
                                    .getBoards();

                            if (listModel != null && listModel.size() > 0)
                            {
                                mBoardListMap.put(
                                        ((CategoryBoardRequest) request)
                                                .getPosition(),
                                        listModel);
                            }

                            mAdapter.notifyDataSetChanged();
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
            case 0:
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            default:
                break;
        }
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v,
            int groupPosition, long id)
    {
        if (mAdapter.getChildrenCount(groupPosition) == 0)
        {
            BaseOptionModel model = mAdapter.getGroup(groupPosition);
            doBoardByCategoryRequest(groupPosition, model.get_id());
        }

        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
            int groupPosition, int childPosition, long id)
    {
        BoardModel model = mAdapter.getChild(groupPosition, childPosition);
        UIHelper.showTopicListActivity(mContext, model.get_id());
        return false;
    }
}
