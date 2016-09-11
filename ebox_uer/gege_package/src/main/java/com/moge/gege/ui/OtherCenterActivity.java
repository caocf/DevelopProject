package com.moge.gege.ui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.moge.gege.model.FeedListModel;
import com.moge.gege.model.FeedModel;
import com.moge.gege.model.GiftListModel;
import com.moge.gege.model.GiftModel;
import com.moge.gege.model.MyBoardModel;
import com.moge.gege.model.ProfileModel;
import com.moge.gege.model.RecvGiftModel;
import com.moge.gege.model.RespCommunityInfoModel;
import com.moge.gege.model.RespFeedListModel;
import com.moge.gege.model.RespGiftListModel;
import com.moge.gege.model.RespMyBoardListModel;
import com.moge.gege.model.RespProfileModel;
import com.moge.gege.model.RespUserStatModel;
import com.moge.gege.model.UserStatModel;
import com.moge.gege.model.enums.FriendOperateType;
import com.moge.gege.model.enums.GenderType;
import com.moge.gege.model.enums.GiftOperType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.BoardListRequest;
import com.moge.gege.network.request.CheckFriendRequest;
import com.moge.gege.network.request.CommunityInfoRequest;
import com.moge.gege.network.request.FriendOperateRequest;
import com.moge.gege.network.request.OtherGiftListRequest;
import com.moge.gege.network.request.UserFeedListRequest;
import com.moge.gege.network.request.UserProfileRequest;
import com.moge.gege.network.request.UserStatRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.UserBoardListAdapter;
import com.moge.gege.ui.adapter.UserBoardListAdapter.UserBoardListListener;
import com.moge.gege.ui.adapter.UserFeedListAdapter;
import com.moge.gege.ui.adapter.UserFeedListAdapter.UserFeedListener;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.FunctionUtil;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.widget.MyDrawableCenterTextView;
import com.moge.gege.util.widget.ScrollListView;
import com.moge.gege.util.widget.ScrollListView.OnItemClickListener;
import com.moge.gege.util.widget.horizontalListview.widget.HListView;

public class OtherCenterActivity extends BaseActivity implements
        OnClickListener, UserBoardListListener, UserFeedListener,
        OnItemClickListener
{
    private Context mContext;
    private ImageView mAvatarImage;
    private TextView mNicknameText;
    private TextView mPointText;
    private TextView mIntroductionText;
    private TextView mGiftText;

    private MyDrawableCenterTextView mOperFriendBtn;
    private MyDrawableCenterTextView mSendMsgBtn;
    private MyDrawableCenterTextView mSendGiftBtn;

    private RelativeLayout mGiftLayout;
    private LinearLayout mGiftImageLayout;
    private TextView mGiftCountText;
    private TextView mCommunityText;
    private TextView mJobText;
    private TextView mHhobbyText;

    private HListView mBoardListView;
    private UserBoardListAdapter mBoardAdapter;

    private PullToRefreshScrollView mRefreshScrollView;
    private ScrollListView mTopicListView;
    private UserFeedListAdapter mUserFeedListAdapter;
    private String mNextCursor = "";

    private boolean mIsFriend = false;

    private String mUid;

    /**
     * external flag
     */
    private int from = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_othercenter);

        mUid = getIntent().getStringExtra("uid");

        mContext = OtherCenterActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);

        mAvatarImage = (ImageView) this.findViewById(R.id.avatarImage);
        mNicknameText = (TextView) this.findViewById(R.id.nicknameText);
        mPointText = (TextView) this.findViewById(R.id.pointText);
        mIntroductionText = (TextView) this.findViewById(R.id.introductionText);
        mGiftText = (TextView) this.findViewById(R.id.giftText);
        mGiftText.setOnClickListener(this);

        mOperFriendBtn = (MyDrawableCenterTextView) this
                .findViewById(R.id.operFriendBtn);
        mOperFriendBtn.setOnClickListener(this);
        mSendMsgBtn = (MyDrawableCenterTextView) this
                .findViewById(R.id.sendMsgBtn);
        mSendMsgBtn.setOnClickListener(this);
        mSendGiftBtn = (MyDrawableCenterTextView) this
                .findViewById(R.id.sendGiftBtn);
        mSendGiftBtn.setOnClickListener(this);

        mGiftLayout = (RelativeLayout) this.findViewById(R.id.giftLayout);
        mGiftLayout.setOnClickListener(this);
        mGiftImageLayout = (LinearLayout) this
                .findViewById(R.id.giftImageLayout);
        mGiftCountText = (TextView) this.findViewById(R.id.giftCountText);

        mCommunityText = (TextView) this.findViewById(R.id.communityText);
        mJobText = (TextView) this.findViewById(R.id.jobText);
        mHhobbyText = (TextView) this.findViewById(R.id.hobbyText);

        mBoardListView = (HListView) this.findViewById(R.id.boardListView);
        mBoardAdapter = new UserBoardListAdapter(this);
        mBoardAdapter.setListener(this);
        mBoardListView.setAdapter(mBoardAdapter);
        mBoardListView.setOnItemClickListener(mBoardAdapter);

        mTopicListView = (ScrollListView) this.findViewById(R.id.topicList);
        mUserFeedListAdapter = new UserFeedListAdapter(mContext);
        mTopicListView.setAdapter(mUserFeedListAdapter);
        mTopicListView.setOnItemClickListener(this);

        mRefreshScrollView = (PullToRefreshScrollView) this
                .findViewById(R.id.myCenterScrollView);
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
                        doUserFeedListRequest(mUid, mNextCursor);
                    }
                });
    }

    private void initData()
    {
        showLoadingView();

        doUserProfileRequest(mUid);
        doCheckFriendRequest(mUid);
        doUserStatRequest(mUid);
        doOtherGiftListRequest(mUid, "");
        doBoardListRequest(mUid);
        doUserFeedListRequest(mUid, mNextCursor = "");
    }

    private void doCommunityInfoRequest(String communityId)
    {
        CommunityInfoRequest request = new CommunityInfoRequest(communityId,
                new ResponseEventHandler<RespCommunityInfoModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespCommunityInfoModel> request,
                            RespCommunityInfoModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mCommunityText.setText(result.getData().getName());
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

    private void doCheckFriendRequest(String uid)
    {
        CheckFriendRequest request = new CheckFriendRequest(uid,
                new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request, BaseModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mIsFriend = true;
                        }
                        else if (result.getStatus() == ErrorCode.NOT_FRIEND)
                        {
                            mIsFriend = false;
                        }

                        changeOperateButtonStatus();
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.e(error.getMessage());
                    }

                });
        executeRequest(request);
    }

    private void doFriendOperateRequest(int type, String uid)
    {
        FriendOperateRequest request = new FriendOperateRequest(type, uid, "",
                new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request, BaseModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            if (mIsFriend)
                            {
                                mIsFriend = false;
                                changeOperateButtonStatus();
                            }
                            else
                            {
                                ToastUtil
                                        .showToastShort(R.string.apply_send_success);
                            }
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
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

    private void doUserProfileRequest(String uid)
    {
        UserProfileRequest request = new UserProfileRequest(uid,
                new ResponseEventHandler<RespProfileModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespProfileModel> request,
                            RespProfileModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            ProfileModel model = result.getData();
                            UIHelper.showUserPoints(mPointText, model.getIntegration());

                            // mCommunityText = "";
                            mJobText.setText(model.getProfession());
                            mHhobbyText.setText(model.getInterest());

                            setImage(mAvatarImage,
                                    getImageUrl(model.getAvatar()) + GlobalConfig.IMAGE_STYLE90_90,
                                    R.drawable.icon_default_avatar);
                            mNicknameText.setText(model.getNickname());
                            setHeaderLeftTitle(model.getNickname());
                            mIntroductionText.setText(model.getIntroduction());
                            if (GenderType.MAN == model.getGender())
                            {
                                setDrawableRight(mNicknameText,
                                        R.drawable.icon_man);
                            }
                            else
                            {
                                setDrawableRight(mNicknameText,
                                        R.drawable.icon_woman);
                            }

                            // search community info
                            doCommunityInfoRequest(model.getCommunity_id());

                            hideLoadingView();
                        }
                        else
                        {
                            showLoadFailView(result.getMsg());
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.e(error.getMessage());
                        showLoadFailView(error.getMessage());
                    }

                });
        executeRequest(request);
    }

    private void doUserStatRequest(String uid)
    {
        UserStatRequest request = new UserStatRequest(uid,
                new ResponseEventHandler<RespUserStatModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespUserStatModel> request,
                            RespUserStatModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            UserStatModel model = result.getData();
                            mGiftCountText.setText(model.getGift_count()
                                    + getString(R.string.gift_unit));
                            mGiftText.setText(" x " + model.getGift_count());
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

    private void doOtherGiftListRequest(String uid, String cursor)
    {
        OtherGiftListRequest request = new OtherGiftListRequest(uid, cursor,
                new ResponseEventHandler<RespGiftListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespGiftListModel> request,
                            RespGiftListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            GiftListModel listModel = result.getData();

                            if (listModel.getGifts() != null
                                    && listModel.getGifts().size() > 0)
                            {
                                showGiftImageView(listModel.getGifts());
                            }
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

    private void doBoardListRequest(String uid)
    {
        BoardListRequest request = new BoardListRequest(uid, 2,
                new ResponseEventHandler<RespMyBoardListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespMyBoardListModel> request,
                            RespMyBoardListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mBoardAdapter.clear();

                            if (result.getData() != null)
                            {
                                List<MyBoardModel> listModel = result.getData()
                                        .getMembers();
                                if (listModel != null && listModel.size() > 0)
                                {
                                    mBoardAdapter.addAll(listModel);
                                }
                            }

                            mBoardAdapter.notifyDataSetChanged();
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

    private void doUserFeedListRequest(String uid, String cursor)
    {
        UserFeedListRequest request = new UserFeedListRequest(uid, cursor,
                new ResponseEventHandler<RespFeedListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespFeedListModel> request,
                            RespFeedListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            FeedListModel listModel = result.getData();

                            // topic list
                            if (mNextCursor.equals(""))
                            {
                                mUserFeedListAdapter.clear();
                            }

                            if (listModel.getFeeds() != null
                                    && listModel.getFeeds().size() > 0)
                            {
                                mUserFeedListAdapter.addAll(listModel
                                        .getFeeds());
                                mNextCursor = listModel.getNext_cursor();
                            }
                            else
                            {
                                ToastUtil.showToastShort("没有数据了哦~");
                            }

                            mUserFeedListAdapter.notifyDataSetChanged();
                            mTopicListView.notifyDataChange();
                        }

                        mRefreshScrollView.onRefreshComplete();
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                        mRefreshScrollView.onRefreshComplete();
                    }

                });
        executeRequest(request);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.operFriendBtn:
                onOperateFriendRelation();
                break;
            case R.id.sendMsgBtn:
                gotoChatActivity();
                break;
            case R.id.sendGiftBtn:
                UIHelper.showGiftListActivity(mContext, mUid,
                        GiftOperType.SEND_GIFT);
                break;
            case R.id.giftText:
                UIHelper.showGiftListActivity(mContext, mUid, GiftOperType.QUERY_RECV_LIST);
                break;
            default:
                break;
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
            default:
                break;
        }
    }

    private void setDrawableRight(TextView textView, int resId)
    {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, drawable, null);
    }

    private void changeOperateButtonStatus()
    {
        // mOperFriendBtn
        // .setImageResource(mIsFriend ? R.drawable.icon_delete_friend
        // : R.drawable.icon_add_friend);

        int resId = mIsFriend ? R.drawable.icon_delete_friend
                : R.drawable.icon_add_friend;

        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        mOperFriendBtn.setCompoundDrawables(drawable, null, null, null);

        mOperFriendBtn.setText(mIsFriend ? getString(R.string.delete_friend)
                : getString(R.string.add_friend));
    }

    private void onOperateFriendRelation()
    {
        if (mIsFriend)
        {
            ToastUtil.showToastShort(R.string.not_delete_friend);
//            this.doFriendOperateRequest(FriendOperateType.DELETE_FRIEND, mUid);
        }
        else
        {
            this.doFriendOperateRequest(FriendOperateType.APPLY_FRIEND, mUid);
        }
    }

    private void gotoChatActivity()
    {
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra("uid", mUid);
        startActivity(intent);
    }

    private void showGiftImageView(List<RecvGiftModel> giftList)
    {
        this.mGiftImageLayout.removeAllViews();

        for (int i = 0; i < 2 && i < giftList.size(); i++)
        {
            RecvGiftModel recvmMdel = giftList.get(i);
            GiftModel giftModel = recvmMdel.getGift();
            ImageView image = new ImageView(mContext);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    FunctionUtil.dip2px(mContext, 40), FunctionUtil.dip2px(
                            mContext, 40));
            params.setMargins(FunctionUtil.dip2px(mContext, 10), 0, 0, 0);
            image.setLayoutParams(params);

            setImage(image, getImageUrl(giftModel.getImage()),
                    R.drawable.icon_default);
            mGiftImageLayout.addView(image);
        }
    }

    @Override
    public void onBoardItemClick(MyBoardModel model)
    {
        UIHelper.showTopicListActivity(mContext, model.get_id());
    }

    @Override
    public void onUserFeedItemClick(FeedModel model)
    {

    }

    @Override
    public void onItemClick(ViewGroup parent, View view, int position, Object o)
    {
        FeedModel model = mUserFeedListAdapter.getItem(position);
        if (model != null)
        {
            UIHelper.showTopicDetailActivity(mContext, model.getBid(),
                    model.getTid(), model.getTopic_type());
        }

    }
}
