package com.moge.gege.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.data.PersistentData;
import com.moge.gege.model.ConvenienceListModel;
import com.moge.gege.model.ConvenienceModel;
import com.moge.gege.model.RespConvenienceListModel;
import com.moge.gege.model.RespDialResultModel;
import com.moge.gege.model.enums.ConvenienceType;
import com.moge.gege.model.enums.ServiceType;
import com.moge.gege.model.enums.TopicType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.ConvenienceDialRequest;
import com.moge.gege.network.request.ConvenienceListRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.ConvenienceListAdapter;
import com.moge.gege.ui.adapter.ConvenienceListAdapter.ConvenienceListListener;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.ui.widget.EmptyView;
import com.moge.gege.util.DeviceInfoUtil;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;

public class ConvenienceListActivity extends BaseActivity implements
        ConvenienceListListener
{
    private Context mContext;
    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private ConvenienceListAdapter mAdapter;
    private String mNextCursor = "";

    private int mConvenienceType;
    private String mTitle;

    private ConvenienceModel mCurModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conveniencelist);

        mConvenienceType = getIntent().getIntExtra("type",
                ConvenienceType.KUAI_DI);

        mContext = ConvenienceListActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);

        final String[] titleList = this.getResources().getStringArray(
                R.array.convenience_list);
        mTitle = titleList[mConvenienceType - 1];
        this.setHeaderLeftTitle(mTitle);

        mPullRefreshListView = (PullToRefreshListView) this
                .findViewById(R.id.convenienceList);
        mPullRefreshListView.setMode(Mode.BOTH);
        //        mPullRefreshListView.setRefreshing();
        mPullRefreshListView.setEmptyView(getLoadingView());
        mListView = mPullRefreshListView.getRefreshableView();
        mAdapter = new ConvenienceListAdapter(mContext);
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
                        doConvenienceList(mConvenienceType, mNextCursor);
                    }
                });

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
        super.showLoadingView();
        mNextCursor = "";
        doConvenienceList(mConvenienceType, mNextCursor);
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

    private void doConvenienceList(int type, String cursor)
    {
        ConvenienceListRequest request = new ConvenienceListRequest(type,
                cursor, new ResponseEventHandler<RespConvenienceListModel>()
        {
            @Override
            public void onResponseSucess(
                    BaseRequest<RespConvenienceListModel> request,
                    RespConvenienceListModel result)
            {
                if (result.getStatus() == ErrorCode.SUCCESS)
                {
                    ConvenienceListModel listModel = result.getData();

                    if (mNextCursor.equals(""))
                    {
                        mAdapter.clear();
                    }

                    if (listModel.getServices() != null
                            && listModel.getServices().size() > 0)
                    {
                        mAdapter.addAll(listModel.getServices());
                        mAdapter.notifyDataSetChanged();

                        mNextCursor = listModel.getNext_cursor();
                    }
                    else
                    {
                        ToastUtil.showToastShort(R.string.no_more_data);
                        showLoadEmptyView();
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

    private void doDialRequest(String id)
    {
        ConvenienceDialRequest request = new ConvenienceDialRequest(id,
                new ResponseEventHandler<RespDialResultModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespDialResultModel> request,
                            RespDialResultModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            // refresh ui
                            mCurModel.setDial_count(result.getData()
                                    .getDial_count());
                            mAdapter.notifyDataSetChanged();
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
                initData();
                break;
            default:
                break;
        }
    }

    @Override
    public void onConvenienceItemClick(ConvenienceModel model)
    {
        UIHelper.showConvenienceDetail(mContext, model.get_id(), mTitle);
    }

    @Override
    public void onConveniencePhoneClick(ConvenienceModel model)
    {
        if (!DeviceInfoUtil.isSimExist(this))
        {
            ToastUtil.showToastShort(R.string.please_check_your_sim_card);
            return;
        }

        String phonenumber = "";

        if (model.getContacts() != null && model.getContacts().size() > 0)
        {
            do
            {
                String mobile = model.getContacts().get(0).getMobile();
                if (!TextUtils.isEmpty(mobile))
                {
                    phonenumber = mobile;
                    break;
                }

                phonenumber = model.getContacts().get(0).getPhone();
                break;
            }
            while (true);
        }

        if (TextUtils.isEmpty(phonenumber))
        {
            ToastUtil.showToastShort(R.string.no_contact_phone);
            return;
        }

        mCurModel = model;
        doDialRequest(model.get_id());
        DeviceInfoUtil.startCall(mContext, phonenumber);
    }
}
