package com.moge.gege.ui.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.data.PersistentData;
import com.moge.gege.model.*;
import com.moge.gege.model.enums.ServiceType;
import com.moge.gege.model.enums.TopicType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.FeedListRequest;
import com.moge.gege.network.request.RecommendPlaceRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.*;
import com.moge.gege.ui.adapter.LocalServiceAdapter;
import com.moge.gege.ui.adapter.LocalServiceAdapter.LocalServiceListener;
import com.moge.gege.ui.adapter.TopicListAdapter;
import com.moge.gege.ui.adapter.TopicListAdapter.TopicListListener;
import com.moge.gege.ui.event.Event;
import com.moge.gege.ui.helper.DataCacheHelper;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.ui.widget.PublishCategoryPopupWindow;
import com.moge.gege.ui.widget.PublishCategoryPopupWindow.OnCategoryCallBack;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.widget.MyGridView;
import com.moge.gege.util.widget.ScrollListView;

import java.util.ArrayList;
import java.util.List;

public class LocalServiceFragment extends BaseFragment implements
        LocalServiceListener, TopicListListener, OnCategoryCallBack
{
    private Activity mContext;
    private MyGridView mLocalServiceGridView;
    private LocalServiceAdapter mLocalServiceAdapter;

    private ImageView mPlace1Image;
    private ImageView mPlace2Image;
    private ImageView mPlace3Image;
    private ImageView mPlace4Image;

    private PullToRefreshScrollView mRefreshScrollView;
    private ScrollListView mFeedsListView;
    private TopicListAdapter mFeedsAdapter;
    private String mNextCursor = "";

    private PublishCategoryPopupWindow mCategoryPopWin;

    // cache data
    private TopicListModel mFeedsData;
    private IndexRecommendPlaceModel mRecommendPlaceData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        final View layout = inflater.inflate(R.layout.fragment_localservice,
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

        this.setHeaderLeft(R.string.app_name, R.drawable.icon_logo);
        this.setHeaderRightImage(R.drawable.icon_write);
        this.setHeaderRightOptionImage(R.drawable.icon_scan);

        mContext = this.getActivity();

        mLocalServiceGridView = (MyGridView) v
                .findViewById(R.id.localServiceGridView);
        mLocalServiceAdapter = new LocalServiceAdapter(mContext);
        mLocalServiceAdapter.setListener(this);
        mLocalServiceGridView.setAdapter(mLocalServiceAdapter);
        mLocalServiceGridView.setOnItemClickListener(mLocalServiceAdapter);

        mPlace1Image = (ImageView) v.findViewById(R.id.place1Image);
        mPlace2Image = (ImageView) v.findViewById(R.id.place2Image);
        mPlace3Image = (ImageView) v.findViewById(R.id.place3Image);
        mPlace4Image = (ImageView) v.findViewById(R.id.place4Image);

        mFeedsListView = (ScrollListView) v.findViewById(R.id.feedsListView);
        mFeedsAdapter = new TopicListAdapter(mContext);
        mFeedsAdapter.setListView(mFeedsListView);
        mFeedsAdapter.setListener(this);
        mFeedsListView.setAdapter(mFeedsAdapter);
        mFeedsListView.setOnItemClickListener(mFeedsAdapter);

        mRefreshScrollView = (PullToRefreshScrollView) v
                .findViewById(R.id.refreshScrollView);
        mRefreshScrollView.setMode(Mode.BOTH);
        mRefreshScrollView.setRefreshing();
        mRefreshScrollView
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
                        doFeedsListRequest(mNextCursor);
                    }
                });

        registerTopicPublishBroadCast();
    }

    private void initLocalData()
    {
        mFeedsData = DataCacheHelper.loadFeedsData(mContext);
        if (mFeedsData != null)
        {
            updateFeedsUI(mFeedsData);
        }

        mRecommendPlaceData = DataCacheHelper
                .loadIndexRecommendPlaceData(mContext);
        if (mRecommendPlaceData != null)
        {
            updateRecommendPlace(mRecommendPlaceData);
        }
    }

    private void initData()
    {
        initServiceData();

        doRecommendPlaceRequest();
        doFeedsListRequest(mNextCursor = "");
    }

    private void initServiceData()
    {
        final String nameList[] = this.getResources().getStringArray(
                R.array.service_des);
        final String resNameList[] = this.getResources().getStringArray(
                R.array.service_des_resname);
        final String valueList[] = this.getResources().getStringArray(
                R.array.service_des_value);

        List<LocalServiceModel> serviceList = new ArrayList<LocalServiceModel>();

        for (int i = 2; i < nameList.length; i++)
        {
            LocalServiceModel model = new LocalServiceModel();
            model.setName(nameList[i]);
            model.setResId(getResources().getIdentifier(resNameList[i],
                    "drawable", mContext.getPackageName()));
            model.setValue(Integer.parseInt(valueList[i]));

            serviceList.add(model);
        }

        mLocalServiceAdapter.clear();
        mLocalServiceAdapter.addAll(serviceList);
        mLocalServiceAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onHeaderDoubleClick()
    {
        mRefreshScrollView.getRefreshableView().smoothScrollTo(0, 0);
    }

    @Override
    protected void onHeaderRightClick(View v)
    {
        // if (mCategoryPopWin == null)
        // {
        // mCategoryPopWin = new PublishCategoryPopupWindow(mContext, this);
        // }
        //
        // if (mCategoryPopWin != null)
        // {
        // mCategoryPopWin.showPopupWindow(v
        // .findViewById(R.id.headerRightButton));
        // }

        Intent intent = new Intent(mContext, TopicPublishActivity.class);
        intent.putExtra("board_id", PersistentData.instance().getBid());
        this.startActivityForResult(intent, GlobalConfig.INTENT_PUBLISH_TOPIC);
    }

    @Override
    protected void onHeaderRightOptionClick()
    {
        startActivity(new Intent(mContext, CaptureActivity.class));
    }

    @Override
    public void onServiceClick(int position, LocalServiceModel model)
    {
        switch (model.getValue())
        {
            case ServiceType.ACTIVITY_SERVICE:
                UIHelper.showActivityList(mContext);
                return;
            case ServiceType.MORE_SERVICE:
                startActivity(new Intent(mContext,
                        ConvenienceSecondaryActivity.class));
                return;
            default:
                gotoServiceTopicListActivity(model.getValue());
                break;
        }
    }

    private void gotoServiceTopicListActivity(int type)
    {
        Intent intent = new Intent(mContext, ServiceListActivity.class);
        intent.putExtra("service_type", type);
        startActivity(intent);
    }

    private void gotoConvenienceActivity(int type)
    {
        Intent intent = new Intent(mContext, ConvenienceListActivity.class);
        intent.putExtra("type", type);
        this.startActivity(intent);
    }

    private void doRecommendPlaceRequest()
    {
        RecommendPlaceRequest request = new RecommendPlaceRequest(
                new ResponseEventHandler<RespIndexRecommendPlaceModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespIndexRecommendPlaceModel> request,
                            RespIndexRecommendPlaceModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mRecommendPlaceData = result.getData();
                            updateRecommendPlace(mRecommendPlaceData);
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

    private void doFeedsListRequest(String cursor)
    {
        FeedListRequest request = new FeedListRequest(cursor,
                new ResponseEventHandler<RespTopicListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespTopicListModel> request,
                            RespTopicListModel result)
                    {
                        mRefreshScrollView.onRefreshComplete();

                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mFeedsData = result.getData();
                            updateFeedsUI(mFeedsData);
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
                        mRefreshScrollView.onRefreshComplete();
                    }

                });
        executeRequest(request);
    }

    private void updateFeedsUI(TopicListModel feedsData)
    {
        List<TopicModel> listModel = feedsData.getFeeds();

        // topic list
        if (mNextCursor.equals(""))
        {
            mFeedsAdapter.clear();
        }

        if (listModel != null && listModel.size() > 0)
        {
            mFeedsAdapter.addAll(listModel);
            mNextCursor = feedsData.getNext_cursor();
        }
        else
        {
            ToastUtil.showToastShort(R.string.no_more_data);
        }

        mFeedsAdapter.notifyDataSetChanged();
        mFeedsListView.notifyDataChange();
    }

    private void updateRecommendPlace(IndexRecommendPlaceModel placeModel)
    {
        if (placeModel == null)
        {
            return;
        }

        RecommendPlaceModel place1Model = placeModel.getRecommend1();
        if (place1Model != null)
        {
            setImage(mPlace1Image, getImageUrl(place1Model.getImage()),
                    R.drawable.icon_default);
            mPlace1Image.setOnClickListener(new MyClickListener(place1Model));
        }

        RecommendPlaceModel place1Mode2 = placeModel.getRecommend2();
        if (place1Mode2 != null)
        {
            setImage(mPlace2Image, getImageUrl(place1Mode2.getImage()),
                    R.drawable.icon_default);
            mPlace2Image.setOnClickListener(new MyClickListener(place1Mode2));
        }

        RecommendPlaceModel place1Mode3 = placeModel.getRecommend3();
        if (place1Mode3 != null)
        {
            setImage(mPlace3Image, getImageUrl(place1Mode3.getImage()),
                    R.drawable.icon_default);
            mPlace3Image.setOnClickListener(new MyClickListener(place1Mode3));
        }

        RecommendPlaceModel place1Mode4 = placeModel.getRecommend4();
        if (place1Mode4 != null)
        {
            setImage(mPlace4Image, getImageUrl(place1Mode4.getImage()),
                    R.drawable.icon_default);
            mPlace4Image.setOnClickListener(new MyClickListener(place1Mode4));
        }
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
                doFeedsListRequest(mNextCursor = "");
                break;
            default:
                break;
        }
    }

    public class MyClickListener implements OnClickListener
    {
        RecommendPlaceModel model;

        MyClickListener(RecommendPlaceModel model)
        {
            this.model = model;
        }

        @Override
        public void onClick(View v)
        {
            UIHelper.showUrlRedirect(mContext, model.getUrl());
        }
    }

    @Override
    public void onAvatarClick(String uid)
    {
        UIHelper.showUserCenterActivity(mContext, uid);
    }

    @Override
    public void onTopicClick(TopicModel model)
    {
        if (model.getTopic_type() != TopicType.BUSINESS_TOPIC)
        {
            Intent intent = new Intent(mContext, TopicDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("board_id", model.getBid());
            bundle.putString("topic_id", model.get_id());
            bundle.putInt("topic_type", model.getTopic_type());
            intent.putExtras(bundle);
            startActivityForResult(intent, GlobalConfig.INTENT_TOPIC_DETAIL);
        }
        else
        {
            UIHelper.showTradingDetailActivity(mContext, model.get_id());
        }
    }

    @Override
    public void onCategoryItemClick(int index)
    {
        Intent intent = new Intent(mContext, TopicPublishActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("board_id", PersistentData.instance().getBid());
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
                doFeedsListRequest(mNextCursor = "");
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

        DataCacheHelper.saveFeedsData(mContext,
                mFeedsData);
        DataCacheHelper.saveIndexRecommendPlaceData(mContext,
                mRecommendPlaceData);
    }

    public void onEvent(Event.RefreshEvent event)
    {
        if(event.getRefreshIndex() != HomeActivity.INDEX_FEED)
        {
            return;
        }

        initData();
        onHeaderDoubleClick();
    }

}
