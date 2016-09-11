package com.moge.gege.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.data.PersistentData;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.BaseOptionModel;
import com.moge.gege.model.OptionBuilder;
import com.moge.gege.model.RespServiceListModel;
import com.moge.gege.model.ServiceListModel;
import com.moge.gege.model.TopicModel;
import com.moge.gege.model.enums.ServiceType;
import com.moge.gege.model.enums.TopicType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.ServiceListRequest;
import com.moge.gege.network.request.TopicFavoriteRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.ServiceListAdapter;
import com.moge.gege.ui.adapter.ServiceListAdapter.ServiceListListener;
import com.moge.gege.ui.customview.ServiceOptionView;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;

public class ServiceListActivity extends BaseActivity implements
        ServiceListListener
{
    private Context mContext;
    private PullToRefreshListView mRefreshTopicListView;
    private ListView mTopicListView;
    private ServiceListAdapter mServiceListAdapter;
    private String mNextCursor = "";
    private int mServiceType;
    private int mRealServiceType;
    private String[] mServiceNameStrings = new String[8];

    private ServiceOptionView mOptionView;

    private List<List<? extends BaseOptionModel>> mContentList = new ArrayList<List<? extends BaseOptionModel>>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicelist);

        // receive external params
        mServiceType = getIntent().getIntExtra("service_type", 0);
        mRealServiceType = mServiceType - 1;

        mContext = ServiceListActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderRightTitle(R.string.publish);

        mServiceNameStrings = getResources()
                .getStringArray(R.array.service_des);
        if (mServiceType == ServiceType.TOGETHER_SERVICE)
        {
            this.setHeaderLeftTitle(getString(R.string.together));
        }
        else if (mServiceType == ServiceType.MARRIAGE_SERVICE)
        {
            this.setHeaderLeftTitle(getString(R.string.marriage_dating));
        }
        else
        {
            this.setHeaderLeftTitle(mServiceNameStrings[mServiceType]);
        }

        mOptionView = (ServiceOptionView) this.findViewById(R.id.optionView);
        mRefreshTopicListView = (PullToRefreshListView) this
                .findViewById(R.id.serviceTopicList);
        mTopicListView = mRefreshTopicListView.getRefreshableView();
        mServiceListAdapter = new ServiceListAdapter(mContext);
        mServiceListAdapter.setListener(this);
        mServiceListAdapter.setShowTopicTypeInfo(true);
        mTopicListView.setAdapter(mServiceListAdapter);
        mTopicListView.setOnItemClickListener(mServiceListAdapter);
        mRefreshTopicListView.setMode(Mode.BOTH);
        //        mRefreshTopicListView.setRefreshing()
        mRefreshTopicListView.setEmptyView(getLoadingView());
        mRefreshTopicListView
                .setOnRefreshListener(new OnRefreshListener2<ListView>()
                {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView)
                    {
                        mNextCursor = "";
                        doServiceTopicList(mRealServiceType, mNextCursor);
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView)
                    {
                        doServiceTopicList(mRealServiceType, mNextCursor);
                    }
                });

        registerTopicPublishBroadCast();
    }

    @Override
    protected void onHeaderDoubleClick()
    {
        mRefreshTopicListView.getRefreshableView().smoothScrollToPosition(0);
    }

    @Override
    protected void onHeaderRightClick()
    {
        Intent intent = new Intent(mContext, ServicePublishActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("board_id", PersistentData.instance().getUserInfo()
                .getBid());
        bundle.putInt("topic_type", TopicType.CATEGORY_TOPIC);
        bundle.putInt("service_type", mServiceType);
        intent.putExtras(bundle);
        this.startActivityForResult(intent, GlobalConfig.INTENT_PUBLISH_TOPIC);
    }

    private void initData()
    {
        initOptionData();

        showLoadingView();
        doServiceTopicList(mRealServiceType, mNextCursor = "");
    }

    private void getResArray(List<BaseOptionModel> list, int arrayResId)
    {
        String[] items = this.getResources().getStringArray(arrayResId);
        for (String item : items)
        {
            BaseOptionModel model = new BaseOptionModel();
            model.setName(item);
            list.add(model);
        }
    }

    private List<BaseOptionModel> getResArray(int arrayResId)
    {
        List<BaseOptionModel> list = new ArrayList<BaseOptionModel>();
        String[] items = this.getResources().getStringArray(arrayResId);
        for (String item : items)
        {
            BaseOptionModel model = new BaseOptionModel();
            model.setName(item);
            list.add(model);
        }

        return list;
    }

    private List<BaseOptionModel> getResArray(int descArrayResId,
            int iconArrayResId)
    {
        List<BaseOptionModel> list = new ArrayList<BaseOptionModel>();
        String[] items = this.getResources().getStringArray(descArrayResId);
        String[] iconItems = this.getResources().getStringArray(iconArrayResId);

        for (int i = 0; i < items.length; i++)
        {
            BaseOptionModel model = new BaseOptionModel();
            model.setName(items[i]);
            model.setResId(getResources().getIdentifier(iconItems[i],
                    "drawable", getPackageName()));
            list.add(model);
        }

        return list;
    }

    private void initOptionData()
    {
        if (mServiceType == ServiceType.CARPOOL_SERVICE)
        {

        }
        else if (mServiceType == ServiceType.MARRIAGE_SERVICE)
        {
            mContentList.add(getResArray(R.array.marriage_needtype));
            mContentList.add(getResArray(R.array.marriage_age));
            mContentList.add(getResArray(R.array.marriage_gender));
            mOptionView.setDataSource(getResArray(R.array.marriage_menu),
                    mContentList);
        }
        else if (mServiceType == ServiceType.TOGETHER_SERVICE)
        {
            mContentList.add(getResArray(R.array.together_needtype));
            mContentList.add(getResArray(R.array.together_time));
            mContentList.add(getResArray(R.array.together_address));
            mOptionView.setDataSource(getResArray(R.array.together_menu),
                    mContentList);
        }
        else if (mServiceType == ServiceType.PET_SERVICE)
        {
            mContentList.add(getResArray(R.array.pet_needtype));
            mContentList.add(OptionBuilder.instance().getPetBreedList());
            mContentList.add(getResArray(R.array.pet_gender));
            mOptionView.setDataSource(getResArray(R.array.pet_menu),
                    mContentList);
        }
        else if (mServiceType == ServiceType.SECONDHAND_SERVICE)
        {
            mContentList.add(getResArray(R.array.secondhand_needtype));
            mContentList.add(OptionBuilder.instance().getGoodClassifyList());
            mContentList.add(getResArray(R.array.secondhand_condition));
            mOptionView.setDataSource(
                    getResArray(R.array.secondhand_menu,
                            R.array.secondhand_menu_icon), mContentList);
        }
        else if (mServiceType == ServiceType.RENTHOUSE_SERVICE)
        {
            mContentList.add(getResArray(R.array.renthouse_needtype));
            mContentList.add(getResArray(R.array.renthouse_area));
            mContentList.add(OptionBuilder.instance().getHouseTypeList());
            mContentList.add(getResArray(R.array.renthouse_money));
            mOptionView.setDataSource(getResArray(R.array.renthouse_menu),
                    mContentList);

        }
    }

    private void doServiceTopicList(int serviceType, String cursor)
    {
        ServiceListRequest request = new ServiceListRequest(serviceType,
                cursor, new ResponseEventHandler<RespServiceListModel>()
        {
            @Override
            public void onResponseSucess(
                    BaseRequest<RespServiceListModel> request,
                    RespServiceListModel result)
            {
                if (result.getStatus() == ErrorCode.SUCCESS)
                {
                    ServiceListModel serviceModel = result.getData();

                    // toped list
                    if (mNextCursor.equals(""))
                    {
                        mServiceListAdapter.clear();
                    }

                    if (serviceModel.getLivings() != null
                            && serviceModel.getLivings().size() > 0)
                    {
                        mServiceListAdapter.addAll(result.getData()
                                .getLivings());
                        mNextCursor = serviceModel.getNext_cursor();
                    }
                    else
                    {
                        ToastUtil.showToastShort(R.string.no_more_data);
                        showLoadEmptyView();
                    }

                    mServiceListAdapter.notifyDataSetChanged();
                }
                else
                {
                    showLoadFailView(result.getMsg());
                }

                mRefreshTopicListView.onRefreshComplete();
            }

            @Override
            public void onResponseError(VolleyError error)
            {
                LogUtil.i(error.getMessage());
                mRefreshTopicListView.onRefreshComplete();
                showLoadFailView(null);
            }

        });
        executeRequest(request);
    }

    private void doTopicFavorite(int topicType, String topicId, String boardId)
    {
        TopicFavoriteRequest request = new TopicFavoriteRequest(topicType,
                topicId, boardId, new ResponseEventHandler<BaseModel>()
        {
            @Override
            public void onResponseSucess(
                    BaseRequest<BaseModel> request, BaseModel result)
            {
                if (result.getStatus() == ErrorCode.SUCCESS)
                {
                    ToastUtil.showToastShort("收藏成功~");
                }
                else
                {
                    ToastUtil.showToastShort("收藏失败~");
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
                doServiceTopicList(mRealServiceType, mNextCursor = "");
                break;
            default:
                break;
        }
    }

    @Override
    public void onServiceClick(TopicModel model)
    {
        UIHelper.showTopicDetailActivity(mContext, "", model.get_id(),
                model.getTopic_type());
    }

    @Override
    public void onFavoriteClick(TopicModel model)
    {
        doTopicFavorite(model.getTopic_type(), model.get_id(), model.getBid());
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
                doServiceTopicList(mRealServiceType, mNextCursor = "");
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
