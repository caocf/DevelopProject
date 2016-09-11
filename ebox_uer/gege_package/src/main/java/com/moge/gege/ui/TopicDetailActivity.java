package com.moge.gege.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.widget.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.ApplyListModel;
import com.moge.gege.model.ApplyModel;
import com.moge.gege.model.AttachmentModel;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.LikeListModel;
import com.moge.gege.model.LikeModel;
import com.moge.gege.model.RespApplyListModel;
import com.moge.gege.model.RespApplyResultModel;
import com.moge.gege.model.RespLikeListModel;
import com.moge.gege.model.RespLikeResultModel;
import com.moge.gege.model.RespTopicDetailModel;
import com.moge.gege.model.RespTopicPostListModel;
import com.moge.gege.model.ShareParam;
import com.moge.gege.model.TopicModel;
import com.moge.gege.model.TopicPostModel;
import com.moge.gege.model.enums.GenderType;
import com.moge.gege.model.enums.TopicType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.ActivityApplyListRequest;
import com.moge.gege.network.request.ActivityApplyRequest;
import com.moge.gege.network.request.TopicDeleteReplyRequest;
import com.moge.gege.network.request.TopicDeleteRequest;
import com.moge.gege.network.request.TopicDetailRequest;
import com.moge.gege.network.request.TopicDislikeRequest;
import com.moge.gege.network.request.TopicFavoriteRequest;
import com.moge.gege.network.request.TopicLikeListRequest;
import com.moge.gege.network.request.TopicLikeRequest;
import com.moge.gege.network.request.TopicPostListRequest;
import com.moge.gege.network.request.TopicReplyRequest;
import com.moge.gege.network.request.TopicReportRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.TopicImageAdapter;
import com.moge.gege.ui.adapter.TopicImageAdapter.TopicImageListener;
import com.moge.gege.ui.adapter.TopicPostListAdapter;
import com.moge.gege.ui.adapter.TopicPostListAdapter.TopicPostListener;
import com.moge.gege.ui.customview.CarPoolListItem;
import com.moge.gege.ui.customview.MarriageDatingListItem;
import com.moge.gege.ui.customview.PetListItem;
import com.moge.gege.ui.customview.RentHouseListItem;
import com.moge.gege.ui.customview.SecondHandListItem;
import com.moge.gege.ui.customview.TogetherListItem;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.ui.widget.ShareDialog;
import com.moge.gege.ui.widget.ShareDialog.ShareDialogListener;
import com.moge.gege.util.DeviceInfoUtil;
import com.moge.gege.util.DialogUtil;
import com.moge.gege.util.FunctionUtils;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.TimeUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.widget.CustomDialog;
import com.moge.gege.util.widget.MyGridView;
import com.moge.gege.util.widget.MyListView;
import com.moge.gege.util.widget.chat.IMFooterView;
import com.moge.gege.util.widget.chat.IMFooterView.IMFooterViewListener;

public class TopicDetailActivity extends BaseActivity implements
        TopicPostListener, IMFooterViewListener, TopicImageListener,
        OnLongClickListener, ShareDialogListener
{
    private Context mContext;
    private RelativeLayout mTopicOperateLayout;
    private TextView mSortText;
    private TextView mFavoriteText;
    private TextView mReportText;
    private PullToRefreshScrollView mTopicDetailScrollView;
    private LinearLayout mTopContainerLayout;
    private ImageView mAvatarImage;
    private ImageView mGenderImage;
    private TextView mLevelText;
    private TextView mNickNameText;
    private TextView mPostTimeText;
    private TextView mTitleText;
    private TextView mContentText;

    private ImageView mLikeImage;
    private ImageView mLikeAuthor1Image;
    private ImageView mLikeAuthor2Image;
    private ImageView mLikeAuthor3Image;
    private ImageView mLikeAuthor4Image;
    private ImageView mLikeAuthor5Image;
    private TextView mLikeCountText;

    /**
     * activity
     */
    private LinearLayout mApplyLayout;
    private TextView mApplyText;
    private TextView mActivityTimeText;
    private TextView mActivityLocationText;
    private TextView mApplyCountText;

    /*
     * second hand
     */
    private TextView conditionText;
    private TextView oldPriceText;
    private TextView newPriceText;

    /**
     * rent house
     */
    private TextView communityText;
    private TextView houseTypeText;
    private TextView rentMoneyText;

    /**
     * marriage dating
     */

    private TextView genderText;
    private TextView jobText;
    private TextView ageText;

    /**
     * pet
     */
    private TextView varietyText;
    private TextView petAgeText;
    private TextView petGenderText;

    /**
     * carpool
     */
    private TextView startAddressText;
    private TextView endAddressText;
    private TextView startTimeText;

    /**
     * together
     */
    private TextView togetherTimeText;
    private TextView togetherAddressText;

    private MyGridView mTopicImageGridView;
    private TopicImageAdapter mTopicImageAdapter;

    private MyListView mTopicPostListView;
    private TopicPostListAdapter mTopicPostListAdapter;
    private String mNextCursor = "";

    private String mBoardId;
    private String mTopicId;
    private int mTopicType;
    private TopicModel mTopicDetailModel;
    private boolean mLikeTopic = false;

    private IMFooterView mFooterView;
    private TopicPostModel mCurPostModel;

    private boolean mIsReverse = false;

    private ShareDialog mShareDialog;

    // 是否报名参加活动
    private boolean mApplyActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topicdetail);

        // receive external params
        mBoardId = getIntent().getStringExtra("board_id");
        mTopicId = getIntent().getStringExtra("topic_id");
        mTopicType = getIntent().getIntExtra("topic_type",
                TopicType.GENERAL_TOPIC);

        mContext = TopicDetailActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderRightImage(R.drawable.icon_option);

        mTopicOperateLayout = (RelativeLayout) this
                .findViewById(R.id.topicOperateLayout);
        mSortText = (TextView) this.findViewById(R.id.sortText);
        mSortText.setOnClickListener(mClickListener);
        mFavoriteText = (TextView) this.findViewById(R.id.favoriteText);
        mFavoriteText.setOnClickListener(mClickListener);
        mReportText = (TextView) this.findViewById(R.id.reportText);
        mReportText.setOnClickListener(mClickListener);
        mTopContainerLayout = (LinearLayout) this
                .findViewById(R.id.topContainerLayout);
        mTopContainerLayout.setOnLongClickListener(this);
        mFooterView = (IMFooterView) this.findViewById(R.id.footerView);
        mFooterView.setListner(this);
        mFooterView.setHintText(getString(R.string.reply_topic));

        if (mTopicType == TopicType.GENERAL_TOPIC)
        {
            this.setHeaderLeftTitle(R.string.topic);
            mTopContainerLayout.addView(
                    LayoutInflater.from(mContext).inflate(
                            R.layout.topicdetail_top_general, null), 0);

            mLikeCountText = (TextView) this.findViewById(R.id.likeCountText);
            mLikeImage = (ImageView) this.findViewById(R.id.likeImage);
            mLikeImage.setOnClickListener(mClickListener);
        }
        else if (mTopicType == TopicType.ACTIVITY_TOPIC)
        {
            this.setHeaderLeftTitle(R.string.activity);
            mTopContainerLayout.addView(
                    LayoutInflater.from(mContext).inflate(
                            R.layout.topicdetail_top_activity, null), 0);
            mActivityLocationText = (TextView) this
                    .findViewById(R.id.activityLocationText);
            mActivityTimeText = (TextView) this
                    .findViewById(R.id.activityTimeText);
            mApplyCountText = (TextView) this.findViewById(R.id.applyCountText);
            mApplyLayout = (LinearLayout) this.findViewById(R.id.applyLayout);
            mApplyText = (TextView) this.findViewById(R.id.applyText);
            mApplyLayout.setOnClickListener(mClickListener);
        }
        else if (mTopicType > TopicType.CATEGORY_TOPIC)
        {
            View categoryLayout = LayoutInflater.from(mContext).inflate(
                    R.layout.topicdetail_top_category, null);
            mTopContainerLayout.addView(categoryLayout, 0);

            LinearLayout serviceLayout = (LinearLayout) categoryLayout
                    .findViewById(R.id.serviceLayout);

            if (mTopicType == TopicType.CATEGORY_CARPOOL_TOPIC)
            {
                this.setHeaderLeftTitle(R.string.carpool);
                serviceLayout.addView(LayoutInflater.from(mContext).inflate(
                        R.layout.item_carpool, null));

                startAddressText = (TextView) serviceLayout
                        .findViewById(R.id.startAddressText);
                endAddressText = (TextView) serviceLayout
                        .findViewById(R.id.endAddressText);
                startTimeText = (TextView) serviceLayout
                        .findViewById(R.id.startTimeText);
            }
            else if (mTopicType == TopicType.CATEGORY_MARRIAGE_TOPIC)
            {
                this.setHeaderLeftTitle(R.string.marriage_dating);
                serviceLayout.addView(LayoutInflater.from(mContext).inflate(
                        R.layout.item_marriage_dating, null));

                genderText = (TextView) serviceLayout
                        .findViewById(R.id.genderText);
                jobText = (TextView) serviceLayout.findViewById(R.id.jobText);
                ageText = (TextView) serviceLayout.findViewById(R.id.ageText);
            }
            else if (mTopicType == TopicType.CATEGORY_TOGETHER_TOPIC)
            {
                this.setHeaderLeftTitle(R.string.together);
                serviceLayout.addView(LayoutInflater.from(mContext).inflate(
                        R.layout.item_together, null));

                togetherTimeText = (TextView) serviceLayout
                        .findViewById(R.id.togetherTimeText);
                togetherAddressText = (TextView) serviceLayout.findViewById(R.id.togetherAddressText);
            }
            else if (mTopicType == TopicType.CATEGORY_PET_TOPIC)
            {
                this.setHeaderLeftTitle(R.string.pet);
                serviceLayout.addView(LayoutInflater.from(mContext).inflate(
                        R.layout.item_pet, null));

                varietyText = (TextView) serviceLayout
                        .findViewById(R.id.varietyText);
                petAgeText = (TextView) serviceLayout
                        .findViewById(R.id.petAgeText);
                petGenderText = (TextView) serviceLayout
                        .findViewById(R.id.petGenderText);
            }
            else if (mTopicType == TopicType.CATEGORY_SECONDHAND_TOPIC)
            {
                this.setHeaderLeftTitle(R.string.second_hand);
                serviceLayout.addView(LayoutInflater.from(mContext).inflate(
                        R.layout.item_secondhand, null));
                conditionText = (TextView) serviceLayout
                        .findViewById(R.id.conditionText);
                oldPriceText = (TextView) serviceLayout
                        .findViewById(R.id.oldPriceText);
                newPriceText = (TextView) serviceLayout
                        .findViewById(R.id.newPriceText);
            }
            else if (mTopicType == TopicType.CATEGORY_RENTHOUSE_TOPIC)
            {
                this.setHeaderLeftTitle(R.string.rent_house);
                serviceLayout.addView(LayoutInflater.from(mContext).inflate(
                        R.layout.item_renthouse, null));

                communityText = (TextView) serviceLayout
                        .findViewById(R.id.communityText);
                houseTypeText = (TextView) serviceLayout
                        .findViewById(R.id.houseTypeText);
                rentMoneyText = (TextView) serviceLayout
                        .findViewById(R.id.rentMoneyText);
            }
        }
        else
        {
            mTopContainerLayout.addView(
                    LayoutInflater.from(mContext).inflate(
                            R.layout.topicdetail_top_general, null), 0);

            mLikeImage = (ImageView) this.findViewById(R.id.likeImage);
            mLikeImage.setOnClickListener(mClickListener);
        }

        mAvatarImage = (ImageView) this.findViewById(R.id.avatarImage);
        mAvatarImage.setOnClickListener(mClickListener);
        mGenderImage = (ImageView) this.findViewById(R.id.genderImage);
        mLevelText = (TextView) this.findViewById(R.id.levelText);
        mNickNameText = (TextView) this.findViewById(R.id.nickanmeText);
        mPostTimeText = (TextView) this.findViewById(R.id.postTimeText);
        mTitleText = (TextView) this.findViewById(R.id.titleText);
        mContentText = (TextView) this.findViewById(R.id.contentText);

        mLikeAuthor1Image = (ImageView) this
                .findViewById(R.id.likeAuthor1Image);
        mLikeAuthor2Image = (ImageView) this
                .findViewById(R.id.likeAuthor2Image);
        mLikeAuthor3Image = (ImageView) this
                .findViewById(R.id.likeAuthor3Image);
        mLikeAuthor4Image = (ImageView) this
                .findViewById(R.id.likeAuthor4Image);
        mLikeAuthor5Image = (ImageView) this
                .findViewById(R.id.likeAuthor5Image);

        mTopicImageGridView = (MyGridView) this
                .findViewById(R.id.topicImageGridView);
        mTopicImageAdapter = new TopicImageAdapter(this);
        mTopicImageAdapter.setListener(this);
        mTopicImageGridView.setAdapter(mTopicImageAdapter);
        mTopicImageGridView.setOnItemClickListener(mTopicImageAdapter);

        mTopicPostListView = (MyListView) this.findViewById(R.id.topicpostList);
        mTopicPostListAdapter = new TopicPostListAdapter(mContext);
        mTopicPostListAdapter.setListener(this);
        mTopicPostListView.setAdapter(mTopicPostListAdapter);
        // mTopicPostListView.setOnItemClickListener(mTopicPostListAdapter);
        mTopicPostListView.setOnItemLongClickListener(mTopicPostListAdapter);

        mTopicDetailScrollView = (PullToRefreshScrollView) this
                .findViewById(R.id.topicDetailScrollView);
        mTopicDetailScrollView.setMode(Mode.BOTH);
        mTopicDetailScrollView.setRefreshing();
        mTopicDetailScrollView
                .setOnRefreshListener(new OnRefreshListener2<ScrollView>()
                {
                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ScrollView> refreshView)
                    {
                        mNextCursor = "";
                        initData();
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ScrollView> refreshView)
                    {
                        doTopicPostList(mIsReverse, mTopicType, mTopicId,
                                mNextCursor);
                    }
                });
    }

    @Override
    protected void onHeaderRightClick()
    {
        if (mTopicDetailModel == null)
        {
            return;
        }

        if (mShareDialog == null)
        {
            ShareParam param = new ShareParam();
            param.setTopicType(mTopicDetailModel.getTopic_type());
            param.setTopicId(mTopicDetailModel.get_id());
            param.setTitle(mTopicDetailModel.getTitle());
            param.setContent(mTopicDetailModel.getDescript());
            AttachmentModel attachment = mTopicDetailModel.getAttachments();
            if (attachment != null && attachment.getImages() != null
                    && attachment.getImages().size() > 0)
            {
                param.setImageUrl(attachment.getImages().get(0));
            }
            if (mTopicDetailModel.getAuthor_uid().equals(
                    AppApplication.getLoginId()))
            {
                param.setShowDelete(true);
            }
            mShareDialog = new ShareDialog(mContext, param);
            mShareDialog.setListener(this);
        }

        mShareDialog.show();

        // to do list!!!
        // if (mTopicOperateLayout.getVisibility() == View.GONE)
        // {
        // mTopicOperateLayout.setVisibility(View.VISIBLE);
        // }
        // else
        // {
        // mTopicOperateLayout.setVisibility(View.GONE);
        // }
    }

    private void initData()
    {
        showLoadingView();
        doTopicDetail(mTopicType, mTopicId, mBoardId);

        if (mTopicType == TopicType.GENERAL_TOPIC)
        {
            doTopicLikeList(mBoardId, mTopicId, mTopicType);
        }

        doTopicPostList(mIsReverse, mTopicType, mTopicId, "");
    }

    private OnClickListener mClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if(!AppApplication.checkLoginState((Activity) mContext))
            {
                return;
            }

            switch (v.getId())
            {
                case R.id.avatarImage:
                    if (mTopicDetailModel != null)
                    {
                        UIHelper.showUserCenterActivity(mContext,
                                mTopicDetailModel.getAuthor_uid());
                    }
                    break;
                case R.id.likeImage:
                    onLikeClick();
                    break;
                case R.id.applyLayout:
                    doActivityApplyRequest(!mApplyActivity, mTopicDetailModel);
                    break;
                case R.id.sortText:
                    mTopicOperateLayout.setVisibility(View.GONE);
                    onSortClick();
                    break;
                case R.id.favoriteText:
                    mTopicOperateLayout.setVisibility(View.GONE);
                    doTopicFavorite(mTopicType, mTopicId, mBoardId);
                    break;
                case R.id.reportText:
                    mTopicOperateLayout.setVisibility(View.GONE);
                    if (AppApplication.getLoginId().equals(
                            mTopicDetailModel.getAuthor_uid()))
                    {
                        DialogUtil.createConfirmDialog(mContext,
                                getString(R.string.general_tips),
                                getString(R.string.delete_topic_confirm),
                                getString(R.string.general_confirm),
                                getString(R.string.general_cancel),
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog,
                                            int which)
                                    {
                                        dialog.dismiss();
                                        doTopicDeleteRequest(mTopicType,
                                                mTopicId, mBoardId);
                                    }
                                }, new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog,
                                            int which)
                                    {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                    else
                    {
                        doTopicReport(mTopicType, mTopicId, mBoardId);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void onSortClick()
    {
        mIsReverse = !mIsReverse;
        mSortText.setText(mIsReverse ? getString(R.string.up_sort)
                : getString(R.string.down_sort));
        mNextCursor = "";
        doTopicPostList(mIsReverse, mTopicType, mTopicId, mNextCursor);
    }

    private void onLikeClick()
    {
        if (mLikeTopic)
        {
            doTopicDisLike(mTopicType, mTopicId, mBoardId);
        }
        else
        {
            doTopicLike(mTopicType, mTopicId, mBoardId);
        }
    }

    private void doTopicLike(int topicType, String topicId, String boardId)
    {
        TopicLikeRequest request = new TopicLikeRequest(topicType, topicId,
                boardId, new ResponseEventHandler<RespLikeResultModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespLikeResultModel> request,
                            RespLikeResultModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mLikeTopic = true;
                            int count = result.getData().getCount();

                            updateLikeIconAndNumber(mLikeTopic, count);

                            // refresh like list avatar
                            doTopicLikeList(mBoardId, mTopicId, mTopicType);
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

    private void doTopicDisLike(int topicType, String topicId, String boardId)
    {
        TopicDislikeRequest request = new TopicDislikeRequest(topicType,
                topicId, boardId,
                new ResponseEventHandler<RespLikeResultModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespLikeResultModel> request,
                            RespLikeResultModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mLikeTopic = false;
                            int count = result.getData().getCount();

                            updateLikeIconAndNumber(mLikeTopic, count);

                            // refresh like list avatar
                            doTopicLikeList(mBoardId, mTopicId, mTopicType);
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

    private void doTopicDetail(int topicType, String topicId, String boardId)
    {
        TopicDetailRequest request = new TopicDetailRequest(topicType, topicId,
                boardId, new ResponseEventHandler<RespTopicDetailModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespTopicDetailModel> request,
                            RespTopicDetailModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            if (mTopicType == TopicType.GENERAL_TOPIC)
                            {
                                mTopicDetailModel = result.getData().getTopic();
                            }
                            else if (mTopicType == TopicType.ACTIVITY_TOPIC)
                            {
                                mTopicDetailModel = result.getData()
                                        .getActivity();
                            }
                            else
                            {
                                mTopicDetailModel = result.getData()
                                        .getLiving_service();
                            }

                            if (mTopicDetailModel == null)
                            {
                                return;
                            }

                            setImage(mAvatarImage,
                                    getImageUrl(mTopicDetailModel.getAuthor()
                                            .getAvatar())  + GlobalConfig.IMAGE_STYLE90_90,
                                    R.drawable.icon_default_avatar);
                            if (mTopicDetailModel.getAuthor().getGender() == GenderType.MAN)
                            {
                                mGenderImage
                                        .setImageResource(R.drawable.icon_man);
                            }
                            else
                            {
                                mGenderImage
                                        .setImageResource(R.drawable.icon_woman);
                            }

                            if (mTopicDetailModel.getMember() != null)
                            {
                                mLevelText.setVisibility(View.VISIBLE);
                                mLevelText.setText(mTopicDetailModel
                                        .getMember().getIntegration_level()
                                        + "");
                            }
                            else
                            {
                                mLevelText.setVisibility(View.GONE);
                            }
                            mNickNameText.setText(mTopicDetailModel.getAuthor()
                                    .getNickname());
                            mPostTimeText.setText(TimeUtil
                                    .getTimeStr(mTopicDetailModel.getCrts() * 1000));
                            mTitleText.setText(mTopicDetailModel.getTitle());
                            mContentText.setText(mTopicDetailModel.getContent());

                            List<String> images = mTopicDetailModel
                                    .getAttachments().getImages();
                            updateImageGridViewPara(images.size());

                            mTopicImageAdapter.clear();
                            mTopicImageAdapter.addAll(images);
                            mTopicImageAdapter.notifyDataSetChanged();

                            // update report or delete topic
                            if (AppApplication.getLoginId().equals(
                                    mTopicDetailModel.getAuthor_uid()))
                            {
                                mReportText.setText(getString(R.string.delete));
                            }
                            else
                            {
                                mReportText.setText(getString(R.string.report));
                            }

                            updateCategoryInfo(mTopicDetailModel);

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
                        LogUtil.i(error.getMessage());
                        showLoadFailView(R.string.load_failed_retry);
                    }

                });
        executeRequest(request);
    }

    private void doTopicLikeList(String boardId, String topicId, int topicType)
    {
        TopicLikeListRequest request = new TopicLikeListRequest(boardId,
                topicId, topicType,
                new ResponseEventHandler<RespLikeListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespLikeListModel> request,
                            RespLikeListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            LikeListModel likeListModel = result.getData();

                            mLikeTopic = likeListModel.isLike();
                            updateLikeIconAndNumber(mLikeTopic, -1);

                            updateLikeAuthorImage(likeListModel);
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

    private void doTopicPostList(boolean isReverse, int topicType,
            String topicId, String cursor)
    {
        TopicPostListRequest request = new TopicPostListRequest(isReverse,
                topicType, topicId, cursor,
                new ResponseEventHandler<RespTopicPostListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespTopicPostListModel> request,
                            RespTopicPostListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            if (mNextCursor.equals(""))
                            {
                                mTopicPostListAdapter.clear();
                            }

                            if (result.getData().getPosts() == null
                                    || result.getData().getPosts().size() == 0)
                            {
                                // ToastUtils.showToastShort("没有数据了哦~");
                            }
                            else
                            {
                                mNextCursor = result.getData().getNext_cursor();
                                mTopicPostListAdapter.addAll(result.getData()
                                        .getPosts());
                                mTopicPostListAdapter.notifyDataSetChanged();
                            }
                        }

                        mTopicDetailScrollView.onRefreshComplete();
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                        mTopicDetailScrollView.onRefreshComplete();
                    }

                });
        executeRequest(request);
    }

    private void doTopicReply(int topicType, String topicId, String boardId,
            String content)
    {
        TopicReplyRequest request = new TopicReplyRequest(topicType, topicId,
                boardId, content, new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request, BaseModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            ToastUtil.showToastShort(R.string.reply_success);

                            mCurPostModel = null;
                            mFooterView
                                    .setHintText(getString(R.string.reply_topic));
                            mFooterView.setImfooterGone();

                            // refresh ui
                            mNextCursor = "";
                            doTopicPostList(mIsReverse, mTopicType, mTopicId,
                                    mNextCursor);
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

    private void doActivityApplyRequest(boolean apply, TopicModel model)
    {
        if (model == null)
        {
            return;
        }

        ActivityApplyRequest request = new ActivityApplyRequest(apply,
                model.get_id(),
                new ResponseEventHandler<RespApplyResultModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespApplyResultModel> request,
                            RespApplyResultModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mApplyActivity = !mApplyActivity;

                            if (mApplyActivity)
                            {
                                ToastUtil
                                        .showToastShort(R.string.apply_success);
                            }
                            else
                            {
                                ToastUtil
                                        .showToastShort(R.string.cancel_apply_success);
                            }

                            mApplyCountText.setText(result.getData().getCount()
                                    + getString(R.string.apply_unit_desc));

                            // query apply list
                            doActivityApplyList(mTopicId);
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

    private void doActivityApplyList(String activityId)
    {
        ActivityApplyListRequest request = new ActivityApplyListRequest(
                activityId, new ResponseEventHandler<RespApplyListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespApplyListModel> request,
                            RespApplyListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mApplyActivity = result.getData().isApplyed();
                            if (mApplyActivity)
                            {
                                mApplyText
                                        .setText(getString(R.string.cancel_apply));
                                mApplyLayout.setClickable(true);
                            }
                            else
                            {
                                mApplyText
                                        .setText(getString(R.string.to_apply));
                                mApplyLayout.setClickable(true);
                            }

                            updateApplyAuthorImage(result.getData());
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
                            ToastUtil
                                    .showToastShort(getString(R.string.favorite_success));
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

    private void doTopicReport(int topicType, String topicId, String boardId)
    {
        TopicReportRequest request = new TopicReportRequest(topicType, topicId,
                boardId, new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request, BaseModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            ToastUtil
                                    .showToastShort(getString(R.string.report_success));
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

    private void doTopicDeleteReply(int topicType, String topicId,
            String boardId, String postId)
    {
        TopicDeleteReplyRequest request = new TopicDeleteReplyRequest(
                topicType, topicId, boardId, postId,
                new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request, BaseModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            ToastUtil
                                    .showToastShort(getString(R.string.delete_success));

                            mTopicPostListAdapter.remove(mCurPostModel);
                            mTopicPostListAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                        }
                        mCurPostModel = null;
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        mCurPostModel = null;
                        ToastUtil.showToastShort(error.getMessage());
                    }

                });
        executeRequest(request);
    }

    private void doTopicDeleteRequest(int topicType, String topicId,
            String boardId)
    {
        TopicDeleteRequest request = new TopicDeleteRequest(topicType, topicId,
                boardId, new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request, BaseModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            ToastUtil
                                    .showToastShort(getString(R.string.delete_success));
                            setResult(RESULT_OK);
                            finish();
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

    private void updateLikeAuthorImage(LikeListModel model)
    {
        final ImageView imgArr[] = { mLikeAuthor1Image, mLikeAuthor2Image,
                mLikeAuthor3Image, mLikeAuthor4Image, mLikeAuthor5Image };
        List<LikeModel> likeList = model.getLikes();
        for (int i = 0; i < 5; i++)
        {
            if (likeList.size() > i)
            {
                imgArr[i].setVisibility(View.VISIBLE);
                this.setImage(imgArr[i], getImageUrl(likeList.get(i)
                        .getAvatar()), R.drawable.icon_default_avatar);
            }
            else
            {
                imgArr[i].setVisibility(View.GONE);
            }
        }
    }

    private void updateApplyAuthorImage(ApplyListModel model)
    {
        final ImageView imgArr[] = { mLikeAuthor1Image, mLikeAuthor2Image,
                mLikeAuthor3Image, mLikeAuthor4Image, mLikeAuthor5Image };
        List<ApplyModel> applysList = model.getApplys();
        for (int i = 0; i < 5; i++)
        {
            if (applysList.size() > i)
            {
                imgArr[i].setVisibility(View.VISIBLE);
                this.setImage(imgArr[i], getImageUrl(applysList.get(i)
                        .getAvatar()), R.drawable.icon_default_avatar);
            }
            else
            {
                imgArr[i].setVisibility(View.GONE);
            }
        }
    }

    private void updateLikeIconAndNumber(boolean isLike, int number)
    {
        if (mLikeImage == null || mLikeCountText == null)
        {
            return;
        }

        if (mLikeTopic)
        {
            mLikeImage.setImageResource(R.drawable.icon_big_liked);
        }
        else
        {
            mLikeImage.setImageResource(R.drawable.icon_big_like);
        }

        if (number == -1)
        {
            return;
        }

        if (number > 0)
        {
            mLikeCountText.setText(number + getString(R.string.like_unit_desc));
        }
        else
        {
            mLikeCountText.setText("快来抢帖子的第一个赞吧~");
        }
    }

    private void updateImageGridViewPara(int size)
    {
        LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) mTopicImageGridView
                .getLayoutParams();

        if (size == 1)
        {
            int width = p.width;

            p.width = LayoutParams.WRAP_CONTENT;
            p.height = LayoutParams.WRAP_CONTENT;
            mTopicImageGridView.setLayoutParams(p);

            mTopicImageGridView.setNumColumns(1);
            mTopicImageGridView.setColumnWidth(width);
            mTopicImageAdapter.setImageDisplaySize(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
        }
        else
        {
            mTopicImageGridView.setNumColumns(3);
            mTopicImageGridView.setColumnWidth(p.width / 3);
            mTopicImageAdapter.setImageDisplaySize(p.width / 3, p.width / 3);
        }

    }

    private void updateCategoryInfo(TopicModel model)
    {
        if (model.getTopic_type() == TopicType.GENERAL_TOPIC)
        {
            updateLikeIconAndNumber(mLikeTopic,
                    mTopicDetailModel.getLike_count());
        }
        else if (model.getTopic_type() == TopicType.ACTIVITY_TOPIC)
        {
            showActivityInfo(model);
        }
        else if (model.getTopic_type() == TopicType.CATEGORY_SECONDHAND_TOPIC)
        {
            showSecondHandInfo(model);
        }
        else if (model.getTopic_type() == TopicType.CATEGORY_RENTHOUSE_TOPIC)
        {
            showRentHouseInfo(model);
        }
        else if (model.getTopic_type() == TopicType.CATEGORY_MARRIAGE_TOPIC)
        {
            showMarriageDatingInfo(model);
        }
        else if (model.getTopic_type() == TopicType.CATEGORY_PET_TOPIC)
        {
            showPetInfo(model);
        }
        else if (model.getTopic_type() == TopicType.CATEGORY_TOGETHER_TOPIC)
        {
            showTogetherInfo(model);
        }
        else if (model.getTopic_type() == TopicType.CATEGORY_CARPOOL_TOPIC)
        {
            showCarPoolInfo(model);
        }
    }

    private void showActivityInfo(TopicModel model)
    {
        // set button enable ?
        // private Button mApplyBtn;
        // private TextView mActivityTimeText;
        // private TextView mActivityLocationText;

        mActivityTimeText
                .setText(Html.fromHtml(getString(
                        R.string.service_content,
                        FunctionUtils.getString(R.string.time),
                        TimeUtil.getDateTimeStr(model.getStart_time() * 1000)
                                + "~"
                                + TimeUtil.getDateTimeStr(model.getEnd_time() * 1000))));
        mActivityLocationText
                .setText(Html.fromHtml(getString(R.string.service_content,
                        FunctionUtils.getString(R.string.address),
                        model.getLocation())));

        mApplyCountText.setText(model.getApply_num()
                + getString(R.string.apply_unit_desc));

        // query apply list
        doActivityApplyList(model.get_id());
    }

    private void showSecondHandInfo(TopicModel model)
    {
        SecondHandListItem.setItemValue(model, mContext, conditionText,
                oldPriceText, newPriceText);
    }

    private void showRentHouseInfo(TopicModel model)
    {
        RentHouseListItem.setItemValue(model, mContext, communityText,
                houseTypeText, rentMoneyText);
    }

    private void showMarriageDatingInfo(TopicModel model)
    {
        MarriageDatingListItem.setItemValue(model, mContext, genderText,
                jobText, ageText);
    }

    private void showPetInfo(TopicModel model)
    {
        PetListItem.setItemValue(model, mContext, varietyText, petAgeText,
                petGenderText);
    }

    private void showTogetherInfo(TopicModel model)
    {
        TogetherListItem.setItemValue(model, mContext, togetherTimeText, togetherAddressText);
    }

    private void showCarPoolInfo(TopicModel model)
    {
        CarPoolListItem.setItemValue(model, mContext, startAddressText,
                endAddressText, startTimeText);
    }

    private String buildReplyContent(TopicPostModel model, String content,
            String images[])
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();

        if (!TextUtils.isEmpty(mBoardId))
        {
            list.add(new BasicNameValuePair("bid", mBoardId));
        }

        if (mTopicType > TopicType.CATEGORY_TOPIC)
        {
            list.add(new BasicNameValuePair("service_type", String
                    .valueOf(mTopicType - TopicType.CATEGORY_TOPIC)));
        }

        list.add(new BasicNameValuePair("content", content));
        list.add(new BasicNameValuePair("longitude", String
                .valueOf(GlobalConfig.getLongitude())));
        list.add(new BasicNameValuePair("latitude", String.valueOf(GlobalConfig
                .getLatitude())));
        list.add(new BasicNameValuePair("topic_type", String
                .valueOf(mTopicType)));

        for (int i = 0; images != null && i < images.length; i++)
        {
            list.add(new BasicNameValuePair("images", images[i]));
        }

        if (model != null)
        {
            list.add(new BasicNameValuePair("ref_uid", model.getAuthor_uid()));
            list.add(new BasicNameValuePair("ref_pid", model.get_id()));
            list.add(new BasicNameValuePair("ref_descript", model.getDescript()));
            list.add(new BasicNameValuePair("ref_number", String.valueOf(model
                    .getNumber())));
        }

        return URLEncodedUtils.format(list, HTTP.UTF_8);
    }

    @Override
    public void onTopicPostAvatarClick(TopicPostModel model)
    {
        if(!AppApplication.checkLoginState((Activity)mContext))
        {
            return;
        }

        UIHelper.showUserCenterActivity(mContext, model.getAuthor_uid());
    }

    @Override
    public void onTopicPostClick(TopicPostModel model)
    {
        mCurPostModel = model;

        mFooterView.setHintText(getString(R.string.reply_floor,
                model.getNumber()));
        mFooterView.setImfooterVisible();
    }

    @Override
    public void onTopicPostItemClick(TopicPostModel model)
    {
        mCurPostModel = model;

        if (!model.getAuthor_uid().equals(AppApplication.getLoginId()))
        {
            return;
        }

        new CustomDialog.Builder(mContext)
                .setTitle(R.string.general_tips)
                .setMessage(R.string.delete_reply_confirm)
                .setPositiveButton(R.string.general_confirm,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which)
                            {
                                doTopicDeleteReply(mTopicType, mTopicId,
                                        mBoardId, mCurPostModel.get_id());
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

    private void publish(String content, String[] images)
    {
        String requestBody = buildReplyContent(mCurPostModel, content, images);
        doTopicReply(mTopicType, mTopicId, mBoardId, requestBody);
    }

    @Override
    public boolean onPublish(String content)
    {
        if (!DeviceInfoUtil.isHaveInternet(mContext))
        {
            ToastUtil.showToastShort(R.string.network_error);
            return false;
        }

        if(!AppApplication.checkLoginState((Activity)mContext))
        {
            return false;
        }

        publish(content, null);
        return true;
    }

    @Override
    public void onTouch()
    {

    }

    @Override
    public void onImageItemClick(int position, String url)
    {
        ArrayList<String> photoList = new ArrayList<String>();
        photoList.addAll(mTopicImageAdapter.getAll());

        UIHelper.showPhotoGalleryActivity(mContext, photoList, position);
    }

    @Override
    public void onUploadPhotoSuccess(String imagename)
    {
        publish("", new String[] { imagename });
    }

    @Override
    public void onUploadPhotoFailed(String errorMsg)
    {

    }

    @Override
    public void onSelectImageMenu(int index)
    {
        if(!AppApplication.checkLoginState((Activity)mContext))
        {
            return;
        }

        if (index == 0)
        {
            openCamera();
        }
        else
        {
            openAlbum();
        }
    }

    @Override
    public boolean onLongClick(View v)
    {
        switch (v.getId())
        {
            case R.id.topContainerLayout:
                onDeleteTopic();
                break;
            default:
                break;
        }
        return false;
    }

    private void onDeleteTopic()
    {
        if (!mTopicDetailModel.getAuthor_uid().equals(
                AppApplication.getLoginId()))
        {
            return;
        }

        new CustomDialog.Builder(mContext)
                .setTitle(R.string.general_tips)
                .setMessage(R.string.delete_topic_confirm)
                .setPositiveButton(R.string.general_confirm,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which)
                            {
                                doTopicDeleteRequest(mTopicType, mTopicId,
                                        mBoardId);
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
    public void onTopicPostImageClick(TopicPostModel model)
    {
        ArrayList<String> photoList = new ArrayList<String>();
        photoList.addAll(model.getAttachments().getImages());

        UIHelper.showPhotoGalleryActivity(mContext, photoList, 0);
    }

    @Override
    public void onDeleteTopicResult(int result)
    {
        if (result == ErrorCode.SUCCESS)
        {
            setResult(RESULT_OK);
            finish();
        }
    }
}
