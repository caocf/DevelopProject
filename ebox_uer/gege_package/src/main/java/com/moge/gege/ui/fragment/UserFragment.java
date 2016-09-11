package com.moge.gege.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.moge.gege.R;
import com.moge.gege.model.RespUserListModel;
import com.moge.gege.model.UserModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.FriendListRequest;
import com.moge.gege.network.request.NeighborListRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.ChatActivity;
import com.moge.gege.ui.adapter.UserListAdapter;
import com.moge.gege.ui.adapter.UserListAdapter.UserListListener;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.ToastUtil;

public class UserFragment extends BaseFragment implements UserListListener
{
    private Context mContext;
    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private UserListAdapter mAdapter;
    private String mNextCursor = "";
    UserType mType = UserType.FRIEND;

    public enum UserType
    {
        FRIEND, NEIGHBOR;
    };

    public void setFragmentType(UserType type)
    {
        mType = type;
    }

    public void reloadData()
    {
        this.initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        final View layout = inflater.inflate(R.layout.fragment_userlist,
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
        mAdapter = new UserListAdapter(mContext);
        mAdapter.setListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mAdapter);

        mPullRefreshListView
                .setOnRefreshListener(new OnRefreshListener2<ListView>()
                {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView)
                    {
                        mNextCursor = "";
                        doUserListRequest(mNextCursor);
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView)
                    {
                        doUserListRequest(mNextCursor);
                    }

                });
    }

    private void initData()
    {
        showLoadingView();
        mNextCursor = "";
        doUserListRequest(mNextCursor);
    }

    private void doUserListRequest(String cursor)
    {
        BaseRequest request = null;

        switch (mType)
        {
            case FRIEND:
                request = new FriendListRequest(cursor, null);
                break;
            case NEIGHBOR:
                request = new NeighborListRequest(cursor, null);
                break;
            default:
                break;

        }

        if (request == null)
        {
            return;
        }

        request.setResponseListener(new ResponseEventHandler<RespUserListModel>()
        {
            @Override
            public void onResponseSucess(
                    BaseRequest<RespUserListModel> request,
                    RespUserListModel result)
            {
                if (result.getStatus() == ErrorCode.SUCCESS)
                {
                    // first to clear old data
                    if (mNextCursor.equals(""))
                    {
                        mAdapter.clear();
                    }

                    if (mType == UserType.FRIEND)
                    {
                        if (result.getData().getFriends() != null
                                && result.getData().getFriends().size() > 0)
                        {
                            mNextCursor = result.getData().getNext_cursor();
                            mAdapter.addAll(result.getData().getFriends());
                            mAdapter.notifyDataSetChanged();
                        }
                        else
                        {
//                            ToastUtil.showToastShort(R.string.no_more_data);
                            showLoadEmptyView();
                        }
                    }
                    else
                    {
                        if (result.getData().getNeighbors() != null
                                && result.getData().getNeighbors().size() > 0)
                        {
                            mNextCursor = result.getData().getNext_cursor();
                            mAdapter.addAll(result.getData().getNeighbors());
                            mAdapter.notifyDataSetChanged();
                        }
                        else
                        {
//                            ToastUtil.showToastShort(R.string.no_more_data);
                            showLoadEmptyView();
                        }
                    }
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
    public void onUserAvatarClick(String uid)
    {
        // 跳转个人中心
        UIHelper.showUserCenterActivity(mContext, uid);
    }

    @Override
    public void onUserItemClick(UserModel model)
    {
        // 私聊
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra("uid", model.get_id());
        startActivity(intent);
    }

}
