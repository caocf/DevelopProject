package com.moge.gege.ui.fragment;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.BaseBoardModel;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.BoardListModel;
import com.moge.gege.model.BoardModel;
import com.moge.gege.model.BoardNewTopicModel;
import com.moge.gege.model.MyBoardListModel;
import com.moge.gege.model.MyBoardModel;
import com.moge.gege.model.RespBoardListModel;
import com.moge.gege.model.RespBoardNewTopicListModel;
import com.moge.gege.model.RespMyBoardListModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.BoardListRequest;
import com.moge.gege.network.request.BoardNewTopicRequest;
import com.moge.gege.network.request.JoinBoardRequest;
import com.moge.gege.network.request.NearBoardRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.HomeActivity;
import com.moge.gege.ui.MoreBoardActivity;
import com.moge.gege.ui.adapter.BoardListAdapter;
import com.moge.gege.ui.adapter.BoardListAdapter.BoardListListener;
import com.moge.gege.ui.adapter.MyBoardListAdapter;
import com.moge.gege.ui.adapter.MyBoardListAdapter.MyBoardListListener;
import com.moge.gege.ui.event.Event;
import com.moge.gege.ui.helper.DataCacheHelper;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.widget.CustomDialog;
import com.moge.gege.util.widget.MyListView;

public class LifeZoneFragment extends BaseFragment implements
        MyBoardListListener, BoardListListener
{
    private Context mContext;
    private PullToRefreshScrollView mLifezoneScrollView;

    private MyListView mMyBoardListView;
    private MyListView mNearBoardListView;
    private MyBoardListAdapter mMyBoardAdapter;
    private BoardListAdapter mNearBoardAdapter;
    private View mMyBoardHeaderView;
    private View mNearBoardHeaderView;
    private LinearLayout mRefreshNearBoardLayout;
    private transient Animation mRefreshAnimation;
    private ImageView mRefreshImage;
    private TextView mMyboardsText;
    private LinearLayout mMoreLayout;

    private String mNextCursor = "";
    private boolean mHaveQueryNewTopic = false;

    // cache data
    private MyBoardListModel mMyBoardListData;
    private BoardListModel mNearListData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        final View layout = inflater.inflate(R.layout.fragment_lifezone,
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

        this.setHeaderRightImage(R.drawable.icon_search);
        this.setHeaderTitle(R.string.lifezone_title);

        mMyBoardListView = (MyListView) v.findViewById(R.id.myBoardList);
        mMyBoardHeaderView = LayoutInflater.from(mContext).inflate(
                R.layout.listview_myboard_header, null);
        mMyboardsText = (TextView) mMyBoardHeaderView
                .findViewById(R.id.myboardsText);
        mMyBoardListView.addHeaderView(mMyBoardHeaderView, null, false);
        mMyBoardAdapter = new MyBoardListAdapter(mContext);
        mMyBoardAdapter.setListener(this);
        mMyBoardListView.setAdapter(mMyBoardAdapter);
        mMyBoardListView.setOnItemClickListener(mMyBoardAdapter);
        mMyBoardListView.setOnItemLongClickListener(mMyBoardAdapter);

        mNearBoardListView = (MyListView) v.findViewById(R.id.nearBoardList);
        mNearBoardHeaderView = LayoutInflater.from(mContext).inflate(
                R.layout.listview_nearboard_header, null);
        mRefreshNearBoardLayout = (LinearLayout) mNearBoardHeaderView
                .findViewById(R.id.refreshNearBoardLayout);
        mRefreshImage = (ImageView) mNearBoardHeaderView
                .findViewById(R.id.refreshImage);
        mRefreshAnimation = AnimationUtils.loadAnimation(mContext,
                R.anim.loading_rotate);
        mRefreshNearBoardLayout.setOnClickListener(mClickListener);
        mNearBoardListView.addHeaderView(mNearBoardHeaderView, null, false);

        View nearBoardFootView = LayoutInflater.from(mContext).inflate(
                R.layout.listview_nearboard_footer, null);
        mMoreLayout = (LinearLayout) nearBoardFootView
                .findViewById(R.id.moreLayout);
        mMoreLayout.setOnClickListener(mClickListener);
        mNearBoardListView.addFooterView(nearBoardFootView, null, false);

        mNearBoardAdapter = new BoardListAdapter(mContext);
        mNearBoardAdapter.setListener(this);
        mNearBoardListView.setAdapter(mNearBoardAdapter);
        mNearBoardListView.setOnItemClickListener(mNearBoardAdapter);

        mLifezoneScrollView = (PullToRefreshScrollView) v
                .findViewById(R.id.lifezoneScrollView);
        mLifezoneScrollView.setMode(Mode.PULL_FROM_START);
        mLifezoneScrollView.setRefreshing();
        mLifezoneScrollView
                .setOnRefreshListener(new OnRefreshListener2<ScrollView>()
                {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ScrollView> refreshView)
                    {
                        initData();
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ScrollView> refreshView)
                    {

                    }
                });
    }

    private void initLocalData()
    {
        mMyBoardListData = DataCacheHelper.loadMyBoardListData(mContext,
                AppApplication.getLoginId());
        if (mMyBoardListData != null)
        {
            updateMyBoardUI(mMyBoardListData);
        }

        mNearListData = DataCacheHelper.loadNearBoardListData(mContext,
                AppApplication.getLoginId());
        if (mNearListData != null)
        {
            updateNearBoardUI(mNearListData);
        }
    }

    private void initData()
    {
        doMyBoards();
        doNearBoards(mNextCursor = "");
    }

    @Override
    protected void onHeaderDoubleClick()
    {
        mLifezoneScrollView.getRefreshableView().smoothScrollTo(0, 0);
    }

    @Override
    protected void onHeaderRightClick(View v)
    {
        startActivity(new Intent(mContext, MoreBoardActivity.class));
    }

    private OnClickListener mClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.refreshNearBoardLayout:
                    doNearBoards(mNextCursor);
                    break;
                case R.id.moreLayout:
                    startActivity(new Intent(mContext, MoreBoardActivity.class));
                    break;
                default:
                    break;
            }
        }
    };

    private void doNearBoards(String cursor)
    {
        mRefreshImage.startAnimation(mRefreshAnimation);

        NearBoardRequest request = new NearBoardRequest(cursor,
                GlobalConfig.getLongitude(), GlobalConfig.getLatitude(),
                new ResponseEventHandler<RespBoardListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespBoardListModel> request,
                            RespBoardListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mNearListData = result.getData();
                            updateNearBoardUI(mNearListData);
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                        }

                        mRefreshImage.clearAnimation();
                        mLifezoneScrollView.onRefreshComplete();
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                        ToastUtil.showToastShort(error.getMessage());
                        mRefreshImage.clearAnimation();
                        mLifezoneScrollView.onRefreshComplete();
                    }

                });
        executeRequest(request);
    }

    private void doMyBoards()
    {
        BoardListRequest request = new BoardListRequest("", 0,
                new ResponseEventHandler<RespMyBoardListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespMyBoardListModel> request,
                            RespMyBoardListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mMyBoardListData = result.getData();
                            updateMyBoardUI(mMyBoardListData);
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                    }

                });
        executeRequest(request);
    }

    private void doJoinBoard(boolean join, String boardId)
    {
        JoinBoardRequest request = new JoinBoardRequest(join, boardId,
                new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request, BaseModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            // requery data to refresh ui
                            doNearBoards(mNextCursor = "");
                            doMyBoards();
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

    private void doMyBoardNewTopicRequest(List<MyBoardModel> boardList)
    {
        mHaveQueryNewTopic = true;

        BoardNewTopicRequest request = new BoardNewTopicRequest(boardList,
                new ResponseEventHandler<RespBoardNewTopicListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespBoardNewTopicListModel> request,
                            RespBoardNewTopicListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            List<BoardNewTopicModel> newTopicList = result
                                    .getData().getBoards();
                            for (int i = 0; newTopicList != null
                                    && i < newTopicList.size(); i++)
                            {
                                BoardNewTopicModel countModel = newTopicList
                                        .get(i);
                                for (int j = 0; j < mMyBoardAdapter.getCount(); j++)
                                {
                                    MyBoardModel myModel = mMyBoardAdapter
                                            .getItem(j);
                                    if (countModel.getBid().equals(
                                            myModel.get_id()))
                                    {
                                        myModel.setNewTopicCount(countModel
                                                .getUpdate_count());
                                        break;
                                    }
                                }
                            }

                            mMyBoardAdapter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                    }

                });
        executeRequest(request);
    }

    private void updateMyBoardUI(MyBoardListModel myBoardListData)
    {
        // first to clear old data
        mMyBoardAdapter.clear();

        mMyBoardAdapter.addAll(myBoardListData.getMembers());
        mMyBoardAdapter.notifyDataSetChanged();

        mMyboardsText.setText(mContext.getResources().getString(
                R.string.my_lifezones)
                + "(" + mMyBoardAdapter.getCount() + ")");

        // query new topic count
        doMyBoardNewTopicRequest(myBoardListData.getMembers());
    }

    private void updateNearBoardUI(BoardListModel nearBoardListData)
    {
        // first to clear old data
        mNearBoardAdapter.clear();

        mNextCursor = nearBoardListData.getNext_cursor();
        mNearBoardAdapter.addAll(nearBoardListData.getBoards());
        mNearBoardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (!mHaveQueryNewTopic && mMyBoardAdapter != null
                && mMyBoardAdapter.getCount() > 0)
        {
            doMyBoardNewTopicRequest(mMyBoardAdapter.getAll());
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();

        mHaveQueryNewTopic = false;
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
    public void onMyBoardClick(MyBoardModel model)
    {
        gotoTopicListAcitvity(model, true);
    }

    @Override
    public void onMyBoardLongClick(final MyBoardModel model)
    {
        if (model.getIs_default() == 1)
        {
            return;
        }

        new CustomDialog.Builder(mContext)
                .setTitle(R.string.general_tips)
                .setMessage(R.string.quit_board_confirm)
                .setPositiveButton(R.string.general_confirm,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which)
                            {
                                doJoinBoard(false, model.get_id());
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
    public void onJoinBoardClick(BoardModel model)
    {
        doJoinBoard(true, model.get_id());
    }

    @Override
    public void onBoardItemClick(BoardModel model)
    {
        gotoTopicListAcitvity(model, false);
    }

    private void gotoTopicListAcitvity(BaseBoardModel model, boolean isMember)
    {
        UIHelper.showTopicListActivity(mContext, model.get_id());
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        DataCacheHelper.saveMyBoardListData(mContext,
                AppApplication.getLoginId(), mMyBoardListData);
        DataCacheHelper.saveNearBoardListData(mContext,
                AppApplication.getLoginId(), mNearListData);
    }

    public void onEvent(Event.RefreshEvent event)
    {
        if(event.getRefreshIndex() != HomeActivity.INDEX_LIFEZONE)
        {
            return;
        }

        initData();
        onHeaderDoubleClick();
    }

}
