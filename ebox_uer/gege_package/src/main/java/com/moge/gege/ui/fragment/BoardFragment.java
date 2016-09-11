package com.moge.gege.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.BoardModel;
import com.moge.gege.model.NewBoardModel;
import com.moge.gege.model.RespBoardListModel;
import com.moge.gege.model.RespNewBoardListModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.BoardUpRequest;
import com.moge.gege.network.request.CategoryBoardRequest;
import com.moge.gege.network.request.HotBoardRequest;
import com.moge.gege.network.request.JoinBoardRequest;
import com.moge.gege.network.request.LatestBoardRequest;
import com.moge.gege.network.request.RecommendBoardRequest;
import com.moge.gege.network.request.SearchBoardRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.BaseActivity;
import com.moge.gege.ui.adapter.BoardListAdapter;
import com.moge.gege.ui.adapter.BoardListAdapter.BoardListListener;
import com.moge.gege.ui.adapter.LatestBoardListAdapter;
import com.moge.gege.ui.adapter.LatestBoardListAdapter.LatestBoardListListener;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.ToastUtil;

public class BoardFragment extends BaseFragment implements BoardListListener,
        OnClickListener, TextWatcher, LatestBoardListListener
{
    private Context mContext;
    private BaseActivity mActivity;
    private RelativeLayout mSearchLayout;
    private ImageView mFindImage;
    private EditText mKeywordEdit;
    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private BoardListAdapter mAdapter;
    private LatestBoardListAdapter mLatestBoardAdapter;
    private String mNextCursor = "";

    BoardType mType = BoardType.CATEGORY;
    private String mCategoryId;
    private String mKeyword = "";
    private ModeType mModeType = ModeType.NormalType;
    private boolean mEnableSearch = false;
    private BoardModel mCurBoardModel;

    enum ModeType
    {
        NormalType, SearchType
    }

    ;

    public enum BoardType
    {
        CATEGORY, HOT, LATEST, SEARCH, RECOMMEND;
    }

    ;

    public void setFragmentType(BoardType type)
    {
        mType = type;
    }

    public void setCategoryId(String category)
    {
        mCategoryId = category;
    }

    public void setSearchKeyword(String keyword)
    {
        mKeyword = keyword;
    }

    public void setSearchEnable(boolean enable)
    {
        mEnableSearch = enable;
    }

    public void reloadData()
    {
        this.initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        final View layout = inflater.inflate(R.layout.fragment_list, container,
                false);
        initView(layout);
        initData();
        return layout;
    }

    @Override
    protected void initView(View v)
    {
        super.initView(v);

        mContext = getActivity();
        mActivity = (BaseActivity) getActivity();

        mSearchLayout = (RelativeLayout) v.findViewById(R.id.searchLayout);
        mFindImage = (ImageView) v.findViewById(R.id.findImage);
        mKeywordEdit = (EditText) v.findViewById(R.id.keywordEdit);
        mKeywordEdit.setOnEditorActionListener(mEditorActionListener);
        mKeywordEdit.addTextChangedListener(this);
        mFindImage.setOnClickListener(this);
        if (mEnableSearch)
        {
            mSearchLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            mSearchLayout.setVisibility(View.GONE);
        }

        mPullRefreshListView = (PullToRefreshListView) v
                .findViewById(R.id.contentList);
        mPullRefreshListView.setMode(Mode.BOTH);
        mPullRefreshListView.setEmptyView(getLoadingView());
        mListView = mPullRefreshListView.getRefreshableView();
        if (mType != BoardType.LATEST)
        {
            mAdapter = new BoardListAdapter(mContext);
            mAdapter.setListener(this);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(mAdapter);
        }
        else
        {
            mLatestBoardAdapter = new LatestBoardListAdapter(mContext);
            mLatestBoardAdapter.setListener(this);
            mListView.setAdapter(mLatestBoardAdapter);
            mListView.setOnItemClickListener(mLatestBoardAdapter);
        }

        mPullRefreshListView
                .setOnRefreshListener(new OnRefreshListener2<ListView>()
                {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView)
                    {
                        showLoadingView();

                        if (mModeType == ModeType.NormalType)
                        {
                            doBoardRequest(mNextCursor = "");
                        }
                        else
                        {
                            doBoardSearchRequest(mKeyword, mNextCursor = "");
                        }
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView)
                    {
                        if (mModeType == ModeType.NormalType)
                        {
                            doBoardRequest(mNextCursor);
                        }
                        else
                        {
                            doBoardSearchRequest(mKeyword, mNextCursor);
                        }
                    }

                });
    }

    private void initData()
    {
        showLoadingView();
        mNextCursor = "";
        doBoardRequest(mNextCursor);
    }

    private void doBoardSearchRequest(String keyword, String cursor)
    {
        SearchBoardRequest request = new SearchBoardRequest(mKeyword, cursor,
                null);
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
                            // first to clear old data
                            if (mNextCursor.equals(""))
                            {
                                mAdapter.clear();
                            }

                            if (result.getData().getBoards() != null
                                    && result.getData().getBoards().size() > 0)
                            {
                                mNextCursor = result.getData().getNext_cursor();
                                mAdapter.addAll(result.getData().getBoards());
                            }
                            else
                            {
                                ToastUtil.showToastShort(R.string.no_more_data);
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
                        showLoadFailView(error.getMessage());
                    }

                });
        executeRequest(request);
    }

    private void doLatestBoardRequest(String cursor)
    {
        LatestBoardRequest request = new LatestBoardRequest(cursor,
                new ResponseEventHandler<RespNewBoardListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespNewBoardListModel> request,
                            RespNewBoardListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            // first to clear old data
                            if (mNextCursor.equals(""))
                            {
                                mLatestBoardAdapter.clear();
                            }

                            if (result.getData().getBoard() != null
                                    && result.getData().getBoard().size() > 0)
                            {
                                mNextCursor = result.getData().getNext_cursor();
                                mLatestBoardAdapter.addAll(result.getData()
                                        .getBoard());
                            }
                            else
                            {
                                ToastUtil.showToastShort(R.string.no_more_data);
                            }

                            mLatestBoardAdapter.notifyDataSetChanged();
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

    private void doBoardUpRequest(String boardId)
    {
        BoardUpRequest request = new BoardUpRequest(boardId, null);
        request.setResponseListener(new ResponseEventHandler<BaseModel>()
        {
            @Override
            public void onResponseSucess(BaseRequest<BaseModel> request,
                    BaseModel result)
            {
                if (result.getStatus() == ErrorCode.SUCCESS)
                {
                    ToastUtil.showToastShort(R.string.support_success);
                }
                else
                {
                    ToastUtil.showToastLong(result.getMsg());
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

    private void doBoardRequest(String cursor)
    {
        BaseRequest request = null;

        switch (mType)
        {
            case CATEGORY:
                mAdapter.setShowJoinBtn(false);
                if (TextUtils.isEmpty(mCategoryId))
                {
                    return;
                }
                request = new CategoryBoardRequest(0, mCategoryId, cursor,
                        null);
                break;
            case HOT:
                mAdapter.setShowJoinBtn(false);
                request = new HotBoardRequest(cursor, null);
                break;
            case LATEST:
                doLatestBoardRequest(cursor);
                return;
            case SEARCH:
                if (TextUtils.isEmpty(mKeyword))
                {
                    return;
                }
                mAdapter.setShowJoinBtn(false);
                request = new SearchBoardRequest(mKeyword, cursor, null);
                break;
            case RECOMMEND:
                mAdapter.setShowJoinBtn(true);
                request = new RecommendBoardRequest(cursor,
                        GlobalConfig.getLongitude(),
                        GlobalConfig.getLatitude(), null);
                break;
            default:
                break;

        }

        if (request == null)
        {
            return;
        }

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
                            // first to clear old data
                            if (mNextCursor.equals(""))
                            {
                                mAdapter.clear();
                            }

                            if (result.getData().getBoards() != null
                                    && result.getData().getBoards().size() > 0)
                            {
                                mNextCursor = result.getData().getNext_cursor();
                                mAdapter.addAll(result.getData().getBoards());
                            }
                            else
                            {
                                ToastUtil.showToastShort(R.string.no_more_data);
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
                            mCurBoardModel.setBoardMember(true);
                            mAdapter.notifyDataSetChanged();

                            ToastUtil.showToastShort(R.string.join_board_success);
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
            case 0:
                break;
            default:
                break;
        }
    }

    @Override
    public void onJoinBoardClick(BoardModel model)
    {
        mCurBoardModel = model;
        doJoinBoard(true, model.get_id());
    }

    @Override
    public void onBoardItemClick(BoardModel model)
    {
        // recommend board when register success
        if(mType == BoardType.RECOMMEND)
        {
            return;
        }

        UIHelper.showTopicListActivity(mContext, model.get_id());
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.findImage:
                onSearchBoard();
                break;
            default:
                break;
        }
    }

    OnEditorActionListener mEditorActionListener = new OnEditorActionListener()
    {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            if (actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                onSearchBoard();

                InputMethodManager imm = (InputMethodManager) v.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive())
                {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),
                            0);
                }

                return true;
            }
            return false;
        }
    };

    private void onSearchBoard()
    {
        String keyword = mKeywordEdit.getText().toString().trim();
        if (TextUtils.isEmpty(keyword))
        {
            ToastUtil.showToastShort(R.string.keyword_not_empty);
            return;
        }

        mKeyword = keyword;

        mModeType = ModeType.SearchType;
        doBoardSearchRequest(keyword, mNextCursor = "");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        // more once query bug
//        if (TextUtils.isEmpty(s))
//        {
//            mModeType = ModeType.NormalType;
//            doBoardRequest(mNextCursor = "");
//        }
    }

    @Override
    public void afterTextChanged(Editable s)
    {

    }

    @Override
    public void onLatestBoardItemClick(NewBoardModel model)
    {
    }

    @Override
    public void onSupportBoardClick(String boardId)
    {
        doBoardUpRequest(boardId);
    }

}
