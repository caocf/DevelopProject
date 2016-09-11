package com.moge.gege.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.BoardInfoModel;
import com.moge.gege.model.MyBoardModel;
import com.moge.gege.model.RespBoardInfoModel;
import com.moge.gege.model.RespMemberInfoModel;
import com.moge.gege.model.RespTopicListModel;
import com.moge.gege.model.TopicListModel;
import com.moge.gege.model.TopicModel;
import com.moge.gege.model.enums.BoardType;
import com.moge.gege.model.enums.TopicType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.BoardInfoRequest;
import com.moge.gege.network.request.BoardSigninRequest;
import com.moge.gege.network.request.JoinBoardRequest;
import com.moge.gege.network.request.QueryBoardSigninRequest;
import com.moge.gege.network.request.TopicListRequest;
import com.moge.gege.network.request.UserInfoInBoardRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.TopedListAdapter;
import com.moge.gege.ui.adapter.TopedListAdapter.TopedListListener;
import com.moge.gege.ui.adapter.TopicListAdapter;
import com.moge.gege.ui.adapter.TopicListAdapter.TopicListListener;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.ui.widget.PublishCategoryPopupWindow;
import com.moge.gege.ui.widget.PublishCategoryPopupWindow.OnCategoryCallBack;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.widget.MyListView;
import com.moge.gege.util.widget.RoundedImageView;
import com.moge.gege.util.widget.ScrollListView;

public class TopicListActivity extends BaseActivity implements
        TopicListListener, OnCategoryCallBack, TopedListListener
{
    private Context mContext;
    private PullToRefreshScrollView mTopicListScrollView;
    private TextView mBoardNameText;
    private TextView mMembersText;
    private TextView mTopicsText;
    private RoundedImageView mAvatarImage;
    private TextView mSigninBtn;

    private RelativeLayout mTopedLayout;
    private MyListView mTopedListView;
    private TopedListAdapter mTopedListAdapter;

    private ScrollListView mTopicListView;
    private TopicListAdapter mTopicListAdapter;
    private String mNextCursor = "";

    private String mBoardId;
    private boolean mIsBoardMember = false;
    private boolean mHaveSign = false;

    private MyBoardModel model;

    private PublishCategoryPopupWindow mCategoryPopWin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topiclist);

        // receive external params
        mBoardId = getIntent().getStringExtra("board_id");

        mContext = TopicListActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);

        mAvatarImage = (RoundedImageView) this.findViewById(R.id.avatarImage);
        mBoardNameText = (TextView) this.findViewById(R.id.boardNameText);
        mMembersText = (TextView) this.findViewById(R.id.membersText);
        mTopicsText = (TextView) this.findViewById(R.id.topicsText);
        mSigninBtn = (TextView) this.findViewById(R.id.signinBtn);
        mSigninBtn.setOnClickListener(mClickListener);

        mTopedLayout = (RelativeLayout) this.findViewById(R.id.topedLayout);
        mTopedListView = (MyListView) this.findViewById(R.id.topedList);
        mTopedListAdapter = new TopedListAdapter(mContext);
        mTopedListAdapter.setListener(this);
        mTopedListView.setAdapter(mTopedListAdapter);
        mTopedListView.setOnItemClickListener(mTopedListAdapter);

        mTopicListView = (ScrollListView) this.findViewById(R.id.topicList);
        mTopicListAdapter = new TopicListAdapter(mContext);
        mTopicListAdapter.setListView(mTopicListView);
        mTopicListAdapter.setListener(this);
        mTopicListAdapter.setShowTopicTypeInfo(true);
        mTopicListView.setAdapter(mTopicListAdapter);
        mTopicListView.setOnItemClickListener(mTopicListAdapter);

        mTopicListScrollView = (PullToRefreshScrollView) this
                .findViewById(R.id.topicListScrollView);
        mTopicListScrollView.setMode(Mode.BOTH);
        mTopicListScrollView.setRefreshing();
        mTopicListScrollView
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
                        doTopicList(mBoardId, mNextCursor);
                    }
                });

        registerTopicPublishBroadCast();
    }

    @Override
    protected void onHeaderDoubleClick()
    {
        mTopicListScrollView.getRefreshableView().smoothScrollTo(0, 0);
    }

    @Override
    protected void onHeaderRightClick()
    {
        // if (mCategoryPopWin == null)
        // {
        // mCategoryPopWin = new PublishCategoryPopupWindow(this, this);
        // }
        //
        // if (mCategoryPopWin != null)
        // {
        // mCategoryPopWin.showPopupWindow(this
        // .findViewById(R.id.headerRightButton));
        // }

        Intent intent = new Intent(mContext, TopicPublishActivity.class);
        intent.putExtra("board_id", mBoardId);
        this.startActivityForResult(intent, GlobalConfig.INTENT_PUBLISH_TOPIC);
    }

    private void initData()
    {
        showLoadingView();
        doBoardMemberInfoRequest(mBoardId);
        doBoardInfoRequest(mBoardId);

        mNextCursor = "";
        doTopicList(mBoardId, mNextCursor);

    }

    private OnClickListener mClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.signinBtn:
                    onBoardSignBtnClick();
                    break;
                default:
                    break;
            }
        }
    };

    private void onBoardSignBtnClick()
    {
        if (mIsBoardMember)
        {
            doBoardSignin(mBoardId);
        }
        else
        {
            doJoinBoard(true, mBoardId);
        }
    }

    private void doBoardMemberInfoRequest(String boardId)
    {
        UserInfoInBoardRequest request = new UserInfoInBoardRequest(boardId,
                new ResponseEventHandler<RespMemberInfoModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespMemberInfoModel> request,
                            RespMemberInfoModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mIsBoardMember = true;
                            setHeaderRightImage(R.drawable.icon_write);
                            doQueryBoardSigninRequest(mBoardId);
                        }

                        mSigninBtn
                                .setBackgroundResource(mIsBoardMember ? R.drawable.bg_btn_yellow
                                        : R.drawable.bg_btn_green);
                        mSigninBtn
                                .setText(getString(mIsBoardMember ? R.string.registration
                                        : R.string.join_board));
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.e(error.getMessage());
                    }

                });
        executeRequest(request);
    }

    private void doBoardInfoRequest(String boardId)
    {
        BoardInfoRequest request = new BoardInfoRequest(boardId,
                new ResponseEventHandler<RespBoardInfoModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespBoardInfoModel> request,
                            RespBoardInfoModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            BoardInfoModel model = result.getData();
                            if (model.getBoard() != null)
                            {
                                setHeaderLeftTitle(model.getBoard().getName());
                                mBoardNameText.setText(model.getBoard()
                                        .getName());
                                mMembersText.setText(model.getBoard()
                                        .getMember_count() + "");
                                mTopicsText.setText(model.getBoard()
                                        .getTopic_count() + "");
                                if (model.getBoard().getType() == BoardType.OWNER_BOARD)
                                {
                                    setImage(mAvatarImage, getImageUrl(model
                                            .getBoard().getLogo())  + GlobalConfig.IMAGE_STYLE90_90,
                                            R.drawable.icon_default_zone);
                                }
                                else
                                {
                                    setImage(mAvatarImage, getImageUrl(model
                                            .getBoard().getLogo())  + GlobalConfig.IMAGE_STYLE90_90,
                                            R.drawable.icon_default);
                                }
                            }
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.e(error.getMessage());
                    }

                });
        executeRequest(request);
    }

    private void doTopicList(String boardId, String cursor)
    {
        TopicListRequest request = new TopicListRequest(boardId, cursor,
                new ResponseEventHandler<RespTopicListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespTopicListModel> request,
                            RespTopicListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            TopicListModel topicModel = result.getData();

                            // toped list
                            if (mNextCursor.equals(""))
                            {
                                mTopedListAdapter.clear();
                            }

                            if (topicModel.getToped_topics() != null
                                    && topicModel.getToped_topics().size() > 0)
                            {
                                mTopedListAdapter.addAll(result.getData()
                                        .getToped_topics());
                            }
                            mTopedListAdapter.notifyDataSetChanged();

                            // show toped topic list
                            if (mTopedListAdapter.getCount() > 0)
                            {
                                mTopedLayout.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                mTopedLayout.setVisibility(View.GONE);
                            }

                            // topic list
                            if (mNextCursor.equals(""))
                            {
                                mTopicListAdapter.clear();
                            }

                            if (topicModel.getTopics() != null
                                    && topicModel.getTopics().size() > 0)
                            {
                                mTopicListAdapter.addAll(result.getData()
                                        .getTopics());

                                mNextCursor = topicModel.getNext_cursor();
                            }
                            else
                            {
                                ToastUtil.showToastShort(R.string.no_more_data);
                            }

                            hideLoadingView();
                            mTopicListAdapter.notifyDataSetChanged();
                            mTopicListView.notifyDataChange();
                        }
                        else
                        {
                            showLoadFailView(result.getMsg());
                        }

                        mTopicListScrollView.onRefreshComplete();
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                        mTopicListScrollView.onRefreshComplete();
                        showLoadFailView(R.string.load_failed_retry);
                    }

                });
        executeRequest(request);
    }

    private void doQueryBoardSigninRequest(String boardId)
    {
        QueryBoardSigninRequest request = new QueryBoardSigninRequest(boardId,
                new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request, BaseModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mHaveSign = true;
                            mSigninBtn.setEnabled(false);
                        }
                        else
                        {
                            mHaveSign = false;
                        }

                        mSigninBtn
                                .setText(getString(mHaveSign ? R.string.have_registration
                                        : R.string.registration));
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                    }

                });
        executeRequest(request);
    }

    private void doBoardSignin(String boardId)
    {
        BoardSigninRequest request = new BoardSigninRequest(boardId,
                new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request, BaseModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mHaveSign = true;
                            mSigninBtn.setEnabled(false);
                            ToastUtil.showToastShort("签到成功~");
                        }
                        else
                        {
                            mHaveSign = false;
                            ToastUtil.showToastShort("签到失败~");
                        }

                        mSigninBtn
                                .setText(getString(mHaveSign ? R.string.have_registration
                                        : R.string.registration));
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
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
                            mIsBoardMember = true;
                            setHeaderRightImage(R.drawable.icon_write);
                            mSigninBtn
                                    .setBackgroundResource(R.drawable.bg_btn_yellow);
                            mSigninBtn.setText(R.string.registration);
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
            case GlobalConfig.INTENT_PUBLISH_TOPIC:
            case GlobalConfig.INTENT_TOPIC_DETAIL:
                initData();
                break;
            default:
                break;
        }
    }

    private void gotoTopicDetailActivity(TopicModel model)
    {
        UIHelper.showTopicDetailActivity(mContext, mBoardId, model.get_id(),
                model.getTopic_type());
    }

    @Override
    public void onTopicClick(TopicModel model)
    {
        gotoTopicDetailActivity(model);
    }

    @Override
    public void onCategoryItemClick(int index)
    {
        Intent intent = new Intent(mContext, TopicPublishActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("board_id", mBoardId);
        if (index == 0)
        {
            bundle.putInt("topic_type", TopicType.GENERAL_TOPIC);
        }
        else if (index == 1)
        {
            bundle.putInt("topic_type", TopicType.ACTIVITY_TOPIC);
        }
        else
        {
            bundle.putInt("topic_type", TopicType.CATEGORY_TOPIC);
        }
        bundle.putInt("service_type", index);
        intent.putExtras(bundle);
        this.startActivityForResult(intent, GlobalConfig.INTENT_PUBLISH_TOPIC);
    }

    @Override
    public void onAvatarClick(String uid)
    {
        UIHelper.showUserCenterActivity(mContext, uid);
    }

    @Override
    public void onTopedListClick(TopicModel model)
    {
        gotoTopicDetailActivity(model);
    }

    private void registerTopicPublishBroadCast()
    {
        IntentFilter exitFilter = new IntentFilter();
        exitFilter.addAction(GlobalConfig.BROADCAST_ACTION_PUBLISH_TOPIC);
        mContext.registerReceiver(mTopicPublishReceiver, exitFilter);
    }

    private BroadcastReceiver mTopicPublishReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (action.equals(GlobalConfig.BROADCAST_ACTION_PUBLISH_TOPIC))
            {
                doTopicList(mBoardId, mNextCursor = "");
            }
        }
    };

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (mContext != null)
        {
            mContext.unregisterReceiver(mTopicPublishReceiver);
        }
    }
}
