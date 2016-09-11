package com.moge.gege.ui;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.CommunityModel;
import com.moge.gege.model.RespCommunityListModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.LocateCommunityRequest;
import com.moge.gege.network.request.SearchCommunityRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.CommunityListAdapter;
import com.moge.gege.ui.adapter.CommunityListAdapter.OnCommunityListener;
import com.moge.gege.util.FunctionUtils;
import com.moge.gege.util.ToastUtil;

public class CommunityActivity extends BaseActivity implements
        OnCommunityListener, TextWatcher, OnClickListener
{
    private Context mContext;
    private PullToRefreshListView mRefreshListView;
    private ListView mListView;
    private EditText mKeywordsEdit;
    private ImageView mDeleteKeywordImage;
    private CommunityListAdapter mAdapter;
    private String mNextCursor = "";
    private String mKeyWord = "";

    private boolean mSearchFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        mContext = CommunityActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(R.string.select_community);
        this.setHeaderRightTitle(R.string.nanjing);

        mRefreshListView = (PullToRefreshListView) this
                .findViewById(R.id.communityList);
        mListView = mRefreshListView.getRefreshableView();
        mAdapter = new CommunityListAdapter(mContext);
        mAdapter.setListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mAdapter);
        mRefreshListView.setMode(Mode.BOTH);
        // mRefreshListView.setRefreshing();
        mRefreshListView.setEmptyView(getLoadingView());
        mRefreshListView
                .setOnRefreshListener(new OnRefreshListener2<ListView>()
                {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView)
                    {
                        if (!mSearchFlag)
                        {
                            mNextCursor = "";
                            doLocateCommunity(mNextCursor);
                        }
                        else
                        {
                            mNextCursor = "";
                            doSearchCommunity(mKeyWord, mNextCursor);
                        }
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView)
                    {
                        if (!mSearchFlag)
                        {
                            doLocateCommunity(mNextCursor);
                        }
                        else
                        {
                            doSearchCommunity(mKeyWord, mNextCursor);
                        }
                    }
                });

        mDeleteKeywordImage = (ImageView) this
                .findViewById(R.id.deleteKeywordImage);
        mDeleteKeywordImage.setOnClickListener(this);
        mKeywordsEdit = (EditText) this.findViewById(R.id.keywordsEdit);
        mKeywordsEdit.addTextChangedListener(this);
        mKeywordsEdit.setOnEditorActionListener(new OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                    KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    // 先隐藏键盘
                    ((InputMethodManager) mKeywordsEdit.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus()
                                    .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                    mKeyWord = mKeywordsEdit.getText().toString().trim();
                    if (TextUtils.isEmpty(mKeyWord))
                    {
                        ToastUtil.showToastShort(R.string.keyword_not_empty);
                        return true;
                    }

                    // set flag
                    mSearchFlag = true;

                    mNextCursor = "";
                    doSearchCommunity(mKeyWord, mNextCursor);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onHeaderRightClick()
    {

    }

    private void initData()
    {
        showLoadingView();
        mNextCursor = "";
        doLocateCommunity(mNextCursor);
    }

    private void doLocateCommunity(String cursor)
    {
        LocateCommunityRequest request = new LocateCommunityRequest(
                GlobalConfig.getLongitude(), GlobalConfig.getLatitude(),
                GlobalConfig.getmCityId(), cursor,
                new ResponseEventHandler<RespCommunityListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespCommunityListModel> request,
                            RespCommunityListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            if (mNextCursor.equals(""))
                            {
                                mAdapter.clear();
                            }

                            if (result.getData() != null)
                            {
                                List<CommunityModel> communitys = result
                                        .getData().getCommunitys();
                                if (communitys != null && communitys.size() > 0)
                                {
                                    mNextCursor = result.getData()
                                            .getNext_cursor();
                                    mAdapter.addAll(result.getData()
                                            .getCommunitys());
                                }
                                else
                                {
                                    ToastUtil
                                            .showToastShort(R.string.no_more_data);
                                    showLoadEmptyView();
                                }

                                mAdapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                            showLoadFailView(result.getMsg());
                        }

                        mRefreshListView.onRefreshComplete();
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                        mRefreshListView.onRefreshComplete();
                        showLoadFailView(null);
                    }

                });
        executeRequest(request);
    }

    private void doSearchCommunity(String keyword, String cursor)
    {
        SearchCommunityRequest request = new SearchCommunityRequest(keyword,
                GlobalConfig.getmCityId(), cursor,
                new ResponseEventHandler<RespCommunityListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespCommunityListModel> request,
                            RespCommunityListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            if (mNextCursor.equals(""))
                            {
                                mAdapter.clear();
                            }

                            if (result.getData() != null)
                            {
                                List<CommunityModel> communitys = result
                                        .getData().getCommunitys();
                                if (communitys != null && communitys.size() > 0)
                                {
                                    mNextCursor = result.getData()
                                            .getNext_cursor();
                                    mAdapter.addAll(result.getData()
                                            .getCommunitys());
                                }
                                else
                                {
                                    ToastUtil
                                            .showToastShort(R.string.no_more_data);
                                    showLoadEmptyView();
                                }

                                mAdapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                            showLoadFailView(result.getMsg());
                        }

                        mRefreshListView.onRefreshComplete();
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                        mRefreshListView.onRefreshComplete();
                        showLoadFailView(null);
                    }

                });
        executeRequest(request);
    }

    @Override
    public void onCommunityItemClick(CommunityModel model)
    {
        FunctionUtils.hideInputMethod(mContext);

        Intent intent = new Intent();
        intent.putExtra("community_id", model.get_id());
        intent.putExtra("community_name", model.getName());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        // set flag
        mSearchFlag = true;

        mKeyWord = mKeywordsEdit.getText().toString().trim();

        if (mKeyWord.length() == 0)
        {
            mDeleteKeywordImage.setVisibility(View.GONE);
        }
        else
        {
            mDeleteKeywordImage.setVisibility(View.VISIBLE);
        }

        mNextCursor = "";
        doSearchCommunity(mKeyWord, mNextCursor);
    }

    @Override
    public void afterTextChanged(Editable s)
    {
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.deleteKeywordImage:
                mKeywordsEdit.setText("");
                break;
            default:
                break;
        }
    }
}
