package com.moge.gege.ui.fragment;

/**
 * Created by sam on 2014/12/2.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.data.PersistentData;
import com.moge.gege.model.BalanceModel;
import com.moge.gege.model.BoardInfoModel;
import com.moge.gege.model.CouponModel;
import com.moge.gege.model.ProfileModel;
import com.moge.gege.model.RespBalanceModel;
import com.moge.gege.model.RespBoardInfoModel;
import com.moge.gege.model.RespCommunityInfoModel;
import com.moge.gege.model.RespCommunityUserModel;
import com.moge.gege.model.RespCouponCountModel;
import com.moge.gege.model.RespProfileModel;
import com.moge.gege.model.RespUserStatModel;
import com.moge.gege.model.UserModel;
import com.moge.gege.model.UserStatModel;
import com.moge.gege.model.enums.MyOperateType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.BoardInfoRequest;
import com.moge.gege.network.request.CommunityInfoRequest;
import com.moge.gege.network.request.CommunityUserRequest;
import com.moge.gege.network.request.CouponCountRequest;
import com.moge.gege.network.request.GetBalanceRequest;
import com.moge.gege.network.request.UserProfileRequest;
import com.moge.gege.network.request.UserStatRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.CouponListActivity;
import com.moge.gege.ui.HomeActivity;
import com.moge.gege.ui.OtherActivity;
import com.moge.gege.ui.RechargeActivity;
import com.moge.gege.ui.RegisterActivity;
import com.moge.gege.ui.event.Event;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.LogUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;


public class MeFragment extends BaseFragment
{
    private Context mContext;

    @Optional @InjectView(R.id.nameText) TextView mUsernameText;
    @InjectView(R.id.giftTipsImage) ImageView mGiftTipsImage;
    @InjectView(R.id.pointsText) TextView mPointsText;
    @InjectView(R.id.communityText) TextView mCommunityText;
    @InjectView(R.id.friendsText) TextView mFriendNeighboursText;
    @InjectView(R.id.avatarImage) ImageView mAvatarImage;

    @Optional @InjectView(R.id.myCouponText) TextView mCouponText;
    @Optional @InjectView(R.id.myPurchaseText) TextView mPurchaseText;
    @InjectView(R.id.balanceText) TextView mBalanceText;

    @Optional @InjectView(R.id.rechargeLayout) RelativeLayout mRechargeLayout;
    @InjectView(R.id.publishLayout) RelativeLayout mPublishLayout;
    @InjectView(R.id.activityLayout) RelativeLayout mActivityLayout;
    @InjectView(R.id.favoriteLayout) RelativeLayout mFavoriteLayout;
    @InjectView(R.id.addressLayout) RelativeLayout mAddressLayout;
    @InjectView(R.id.otherLayout) RelativeLayout mOtherLayout;

    @InjectView(R.id.myCenterScrollView)
    PullToRefreshScrollView mRefreshScrollView;


    private boolean mFromActivity = false;
    private int mBalanceFee = 0;
    private String mCommunityId;

    public void setFromActivity(boolean fromActivity)
    {
        mFromActivity = fromActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        final View layout = inflater.inflate(R.layout.fragment_me, container,
                false);
        ButterKnife.inject(this, layout);
        initView(layout);
        initLocalData();
        initData();
        return layout;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // for update userinfo after register activity
        mUsernameText.setText(PersistentData.instance().getUserInfo()
                .getNickname());

        // update coupon numbers
        doValidCouponCount();
    }

    @Override
    protected void initView(View v)
    {
        super.initView(v);

        mContext = getActivity();

        if (mFromActivity)
        {
            this.setHeaderLeft(R.string.persional_center, R.drawable.icon_back);
        }
        else
        {
            this.setHeaderTitle(R.string.persional_center);
        }

        mRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mRefreshScrollView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ScrollView> refreshView) {
                        initData();
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ScrollView> refreshView) {

                    }
                });
    }

    private void initLocalData()
    {
        UserModel userInfo = PersistentData.instance().getUserInfo();
        if (userInfo != null)
        {
            mUsernameText.setText(userInfo.getNickname());
            this.setImage(mAvatarImage, getImageUrl(userInfo.getAvatar()) + GlobalConfig.IMAGE_STYLE90_90,
                    R.drawable.icon_default_avatar);
        }

        mCommunityId = PersistentData.instance().getCommunityId();
        mCommunityText.setText(PersistentData.instance().getCommunityName());
        mFriendNeighboursText.setText(getString(
                    R.string.member_count, PersistentData.instance().getCommunityUser()));

        UIHelper.showUserPoints(mPointsText, PersistentData.instance().getUserPoints());

        mBalanceText.setText("￥" + PersistentData.instance().getUserBalance(AppApplication.getLoginId()) * 1.0 / 100);
    }

    private void initData()
    {
        doUserProfileRequest();
        doUserStat();
        doBalanceQuery();
    }

    @Override
    protected void onHeaderLeftClick()
    {
        getActivity().finish();
    }


    @Optional @OnClick(R.id.nameText)
    public void onGiftClick(View view)
    {
//        UIHelper.showGiftListActivity(mContext,
//                AppApplication.getLoginId(),
//                GiftOperType.QUERY_RECV_LIST);
    }

    @OnClick({R.id.avatarImage, R.id.editUserInfoLayout, R.id.editUserInfoLayout2})
    public void onAvatarClick()
    {
        startActivityForResult(new Intent(mContext,
                        RegisterActivity.class),
                GlobalConfig.INTENT_EDIT_USERINFO);
    }

    @Optional @OnClick(R.id.myCouponText)
    public void onCouponClick()
    {
        UIHelper.showCouponListActivity(getActivity(),
                CouponListActivity.TagFromCenter, "", 0);
    }

    @OnClick(R.id.publishLayout)
    public void onPublishClick()
    {
        UIHelper.showMyRelatedActivity(mContext,
                MyOperateType.MY_PUBLISH);
    }

    @OnClick(R.id.rechargeLayout)
    public void onRechargeClick()
    {
        startActivity(new Intent(mContext, RechargeActivity.class));
    }

    @Optional  @OnClick(R.id.myPurchaseText)
    public void onPurchaseClick()
    {
        UIHelper.showMyRelatedActivity(mContext,
                MyOperateType.MY_PRUCHASE);
    }

    @OnClick(R.id.activityLayout)
    public void onActivityClick()
    {
        UIHelper.showMyRelatedActivity(mContext,
                MyOperateType.MY_ACTIVITY);
    }

    @OnClick(R.id.favoriteLayout)
    public void onFavoriteClick()
    {
        UIHelper.showMyRelatedActivity(mContext,
                MyOperateType.MY_FAVORITE);
    }

    @OnClick(R.id.addressLayout)
    public void onAddressClick()
    {
        UIHelper.showAddressListActivity(getActivity(), "");
    }

    @OnClick(R.id.otherLayout)
    public void onOtherClick()
    {
        startActivity(new Intent(mContext, OtherActivity.class));
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
                                mCommunityText.setText(model.getBoard()
                                        .getName());
                                mFriendNeighboursText.setText(getString(
                                        R.string.member_count, model.getBoard()
                                                .getMember_count()));

                                // save community info
                                PersistentData.instance()
                                        .setCommunityInfo(model.getBoard());
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
                            PersistentData.instance().setCommunityName(result.getData().getName());
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

    private void doCommunityUserRequest(String communityId)
    {
        CommunityUserRequest request = new CommunityUserRequest(communityId,
                new ResponseEventHandler<RespCommunityUserModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespCommunityUserModel> request,
                            RespCommunityUserModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mFriendNeighboursText.setText(getString(
                                    R.string.member_count, result.getData().getUser_count()));
                            PersistentData.instance().setCommunityUser(result.getData().getUser_count());
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

    private void doUserProfileRequest()
    {
        UserProfileRequest request = new UserProfileRequest("",
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
                            mUsernameText.setText(model.getNickname());
                            setImage(mAvatarImage,
                                    getImageUrl(model.getAvatar()) + GlobalConfig.IMAGE_STYLE90_90,
                                    R.drawable.icon_default_avatar);

                            mCommunityId = model.getCommunity_id();
                            doCommunityInfoRequest(mCommunityId);
                            doCommunityUserRequest(mCommunityId);
                        }
                        else
                        {
//                            ToastUtil.showToastShort(result.getMsg());
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
//                        ToastUtil.showToastShort(error.getMessage());
                    }

                });
        executeRequest(request);
    }

    private void doUserStat()
    {
        UserStatRequest request = new UserStatRequest("",
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
                            UIHelper.showUserPoints(mPointsText, model.getIntegration());
                            PersistentData.instance().setUserPoints(model.getIntegration());

//                            if (model.getGift_count() > 0)
//                            {
//                                mGiftTipsImage.setVisibility(View.VISIBLE);
//                            }
//                            else
//                            {
//                                mGiftTipsImage.setVisibility(View.GONE);
//                            }
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

    private void doValidCouponCount()
{
    CouponCountRequest request = new CouponCountRequest(
            new ResponseEventHandler<RespCouponCountModel>()
            {
                @Override
                public void onResponseSucess(
                        BaseRequest<RespCouponCountModel> request,
                        RespCouponCountModel result)
                {
                    if (result.getStatus() == ErrorCode.SUCCESS)
                    {
                        CouponModel model = result.getData();
                        if (model.getCount() > 0)
                        {
                            mCouponText.setText(getString(R.string.coupon) + " " + model.getCount());
                        }
                        else
                        {
                            mCouponText.setText(R.string.coupon);
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

    private void doBalanceQuery()
    {
        GetBalanceRequest request = new GetBalanceRequest(
                new ResponseEventHandler<RespBalanceModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespBalanceModel> request,
                            RespBalanceModel result)
                    {
                        mRefreshScrollView.onRefreshComplete();

                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            BalanceModel model = result.getData();
                            if (model.getBalance() > 0)
                            {
                                mBalanceFee = model.getBalance();
                                PersistentData.instance().setUserBalance(AppApplication.getLoginId(), mBalanceFee);
                                mBalanceText.setText("￥" + mBalanceFee*1.0/100);
                            }
                            else
                            {
                                mBalanceText.setText("￥" + 0*1.0/100);
                            }
                        }
                        else if(result.getStatus() == ErrorCode.NOT_RECHARGE)
                        {
                            mBalanceText.setText("￥" + 0*1.0/100);
                        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK)
        {
            return;
        }

        switch (requestCode)
        {
            case GlobalConfig.INTENT_EDIT_USERINFO:
                if (data != null)
                {
                    UserModel model = (UserModel) data
                            .getSerializableExtra("userinfo");
                    if (model != null)
                    {
                        mUsernameText.setText(model.getNickname());
                        this.setImage(mAvatarImage,
                                getImageUrl(model.getAvatar()) + GlobalConfig.IMAGE_STYLE90_90,
                                R.drawable.icon_default);
                        mCommunityId = PersistentData.instance().getCommunityId();
                        doCommunityInfoRequest(mCommunityId);
                        doCommunityUserRequest(mCommunityId);
                    }
                }
                break;
            case GlobalConfig.INTENT_LOGIN:
                break;
            default:
                break;
        }
    }

    public void onEvent(Event.RefreshEvent event)
    {
        if(event.getRefreshIndex() != HomeActivity.INDEX_ME)
        {
            return;
        }

        initData();
    }

    // refresh points
    public void onEvent(Event.PointEvent event)
    {
        doUserStat();
    }

    public void onEvent(Event.BalanceEvent event)
    {
        doBalanceQuery();
    }
}
