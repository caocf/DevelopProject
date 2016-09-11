package com.moge.gege.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.data.PersistentData;
import com.moge.gege.model.ActivityListModel;
import com.moge.gege.model.ActivityModel;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.RespActivityListModel;
import com.moge.gege.model.RespApplyResultModel;
import com.moge.gege.model.RespTopicDetailModel;
import com.moge.gege.model.enums.ServiceType;
import com.moge.gege.model.enums.TopicType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.ActivityApplyRequest;
import com.moge.gege.network.request.ActivityDetailRequest;
import com.moge.gege.network.request.ActivityListRequest;
import com.moge.gege.network.request.BoardSigninRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.ActivityListAdapter;
import com.moge.gege.ui.adapter.ActivityListAdapter.ActivityListListener;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.ui.widget.EmptyView;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;

public class ActivityListActivity extends BaseActivity implements
        ActivityListListener
{
    private Context mContext;
    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private ActivityListAdapter mAdapter;
    private String mNextCursor = "";

    private ActivityModel mCurActivityModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitylist);

        mContext = ActivityListActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(R.string.activity);
        this.setHeaderRightImage(R.drawable.icon_write);

        mPullRefreshListView = (PullToRefreshListView) this
                .findViewById(R.id.activityList);
        mPullRefreshListView.setMode(Mode.BOTH);
//        mPullRefreshListView.setRefreshing();
        mPullRefreshListView.setEmptyView(getLoadingView());
        mListView = mPullRefreshListView.getRefreshableView();
        mAdapter = new ActivityListAdapter(mContext);
        mAdapter.setListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mAdapter);
        mListView.setAdapter(mAdapter);
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
                        doActivityList(mNextCursor);
                    }
                });

    }

    @Override
    protected void onHeaderDoubleClick()
    {
        mPullRefreshListView.getRefreshableView().smoothScrollToPosition(0);
    }

    @Override
    protected void onHeaderRightClick()
    {
        Intent intent = new Intent(mContext, ServicePublishActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("board_id", PersistentData.instance().getBid());
        bundle.putInt("topic_type", TopicType.ACTIVITY_TOPIC);
        bundle.putInt("service_type", ServiceType.ACTIVITY_SERVICE);
        intent.putExtras(bundle);
        this.startActivityForResult(intent, GlobalConfig.INTENT_PUBLISH_TOPIC);
    }

    private void initData()
    {
        showLoadingView();
        mNextCursor = "";
        doActivityList(mNextCursor);
    }

    private OnClickListener mClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.signinBtn:
                    break;
                default:
                    break;
            }
        }
    };

    private void doActivityList(String cursor)
    {
        ActivityListRequest request = new ActivityListRequest(cursor,
                new ResponseEventHandler<RespActivityListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespActivityListModel> request,
                            RespActivityListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            ActivityListModel topicModel = result.getData();

                            if (mNextCursor.equals(""))
                            {
                                mAdapter.clear();
                            }

                            if (topicModel.getActivitys() != null
                                    && topicModel.getActivitys().size() > 0)
                            {
                                mAdapter.addAll(result.getData().getActivitys());
                                mAdapter.notifyDataSetChanged();

                                mNextCursor = topicModel.getNext_cursor();
                            }
                            else
                            {
                                ToastUtil.showToastShort(R.string.no_more_data);
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
                        LogUtil.i(error.getMessage());
                        mPullRefreshListView.onRefreshComplete();
                        showLoadFailView(null);
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
                            ToastUtil.showToastShort("签到成功~");
                        }
                        else
                        {
                            ToastUtil.showToastShort("签到失败~");
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

    private void doActivityApply(boolean apply, String activityId)
    {
        ActivityApplyRequest request = new ActivityApplyRequest(apply, activityId,
                new ResponseEventHandler<RespApplyResultModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespApplyResultModel> request,
                            RespApplyResultModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mCurActivityModel.setApply_num(result.getData()
                                    .getCount());
                            mCurActivityModel.setApplyed(!mCurActivityModel.isApplyed());
                            mAdapter.notifyDataSetChanged();
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

    private void doActivityDetail(String activityId)
    {
        ActivityDetailRequest request = new ActivityDetailRequest(activityId,
                new ResponseEventHandler<RespTopicDetailModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespTopicDetailModel> request,
                            RespTopicDetailModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {

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

    @Override
    public void onActivityClick(ActivityModel model)
    {
        UIHelper.showTopicDetailActivity(mContext, "", model.get_id(),
                model.getTopic_type());
    }

    @Override
    public void onActivityApplyClick(ActivityModel model)
    {
        if(!AppApplication.checkLoginState((Activity) mContext))
        {
            return;
        }

        mCurActivityModel = model;
        this.doActivityApply(!model.isApplyed(), model.get_id());
    }
}
