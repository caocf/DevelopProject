package com.moge.gege.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.moge.gege.AppApplication;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.AlbumItemModel;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.BaseOptionModel;
import com.moge.gege.model.HouseTypeOptionModel;
import com.moge.gege.model.OptionBuilder;
import com.moge.gege.model.RespUploadTokenModel;
import com.moge.gege.model.TopicPublishModel;
import com.moge.gege.model.enums.ServiceType;
import com.moge.gege.model.enums.TopicType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.GetUploadTokenRequest;
import com.moge.gege.network.request.TopicPublishRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.network.util.qiniu.auth.JSONObjectRet;
import com.moge.gege.network.util.qiniu.io.IO;
import com.moge.gege.network.util.qiniu.io.PutExtra;
import com.moge.gege.network.util.qiniu.utils.InputStreamAt;
import com.moge.gege.network.util.qiniu.utils.QiniuException;
import com.moge.gege.service.AppService;
import com.moge.gege.ui.customview.OptionPopupWindow;
import com.moge.gege.ui.customview.OptionPopupWindow.OnOptionListener;
import com.moge.gege.ui.fragment.PhotoListFragment;
import com.moge.gege.ui.fragment.PhotoListFragment.PhotoListChangeListener;
import com.moge.gege.ui.widget.DateTimePopupWindow;
import com.moge.gege.ui.widget.DateTimePopupWindow.OnDateTimeListener;
import com.moge.gege.ui.widget.PhotoListView;
import com.moge.gege.util.BitmapUtil;
import com.moge.gege.util.DeviceInfoUtil;
import com.moge.gege.util.DialogUtil;
import com.moge.gege.util.FunctionUtils;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.TimeUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.widget.MyRadioButton;
import com.moge.gege.util.widget.RoundedImageView;
import com.moge.gege.util.widget.chat.EmoticonView;
import com.moge.gege.util.widget.chat.EmoticonView.OnEmoticonListener;

public class ServicePublishActivity extends BaseActivity implements
        OnDateTimeListener, OnOptionListener, PhotoListChangeListener,
        OnEmoticonListener
{
    private Activity mContext;
    private TextView mHeaderLeftText;
    private TextView mHeaderRightText;
    private LinearLayout mContainerLayout;

    private EditText mTitleEdit;
    private EditText mContentEdit;

    private EditText mActivityNameEdit;
    private EditText mAddressEdit;

    private RadioButton mFindCarBtn;
    private RadioButton mFindPassengerBtn;
    private EditText mStartAddressEdit;
    private EditText mEndAddressEdit;
    private TextView mStartTimeText;

    private RadioButton mMarriageBtn;
    private RadioButton mDatingBtn;
    private RadioButton mManBtn;
    private RadioButton mWomanBtn;
    private EditText mAgeEdit;
    private EditText mJobEdit;

    private RadioButton mAdoptionBtn;
    private TextView mVarietyText;
    private String mVarietyId = "";

    private RadioButton mBuyBtn;
    private RadioButton mSellBtn;
    private TextView mCagetoryText;
    private TextView mConditionText;
    private EditText mOldPriceEdit;
    private EditText mNewPriceEdit;
    private String mCategoryId;
    private int mConditionValue;

    private RadioButton mRentHouseBtn;
    private RadioButton mLeaseHouseBtn;
    private TextView mCommunityText;
    private TextView mHouseTypeText;
    private EditText mRentMoneyEdit;
    private String mCommunityId = "";
    private int mRoom = 0;
    private int mHall = 0;
    private int mWashRoom = 0;

    private RadioButton mFitnessBtn;
    private RadioButton mPlayBtn;
    private TextView mEndTimeText;
    private RadioButton mIndoorBtn;
    private RadioButton mOutdoorBtn;

    private RoundedImageView mAvatarImage;
    private ImageView mLikeImage;
    private ImageView mLikeAuthor1Image;
    private ImageView mLikeAuthor2Image;
    private ImageView mLikeAuthor3Image;
    private ImageView mLikeAuthor4Image;
    private TextView mLikeCountText;

    private DateTimePopupWindow mDateTimeCtrl;
    private boolean mSelectStartFlag = false;
    private long mSelectStartTime = 0;
    private long mSelectEndTime = 0;

    private OptionPopupWindow mOptionPopWin;
    private Dialog mSelectHouseTypeDialog;
    private Dialog mVarietyDialog;
    private Dialog mCategoryDialog;
    private Dialog mConditionDialog;

    private String[] mServiceNameStrings = new String[8];

    // external params
    private String mBoardId;
    private int mTopicType;
    private int mServiceType;

    private PhotoListView mPhotoListView;
    private List<AlbumItemModel> mSelectAlbumList;
    private List<String> mUploadPhotoList = new ArrayList<String>();
    private int mTotalUploadNum = 0;
    private int mTotalUploadSuccessNum = 0;
    private int mTotalUploadFailedNum = 0;

    private ImageView mEmoticonImage;
    private ImageView mPhotoImage;
    private TextView mPhotoCountText;
    private EmoticonView mEmoticonView;
    private PhotoListFragment mPhotoListFragment;
    private Dialog mProgressDialog;
    private InputMethodManager mInputManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicepublish);

        // receive external params
        mBoardId = getIntent().getStringExtra("board_id");
        mTopicType = getIntent().getIntExtra("topic_type",
                TopicType.GENERAL_TOPIC);
        mServiceType = getIntent().getIntExtra("service_type",
                ServiceType.CARPOOL_SERVICE);

        mContext = ServicePublishActivity.this;
        initView();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_green_back);
        this.setHeaderRightTitle(R.string.publish);

        mProgressDialog = DialogUtil.createProgressDialog(mContext,
                getString(R.string.publishing));

        // init common view
        mHeaderLeftText = (TextView) this.findViewById(R.id.headerLeftText);
        mHeaderLeftText.setOnClickListener(mClickListener);
        mHeaderRightText = (TextView) this.findViewById(R.id.headerRightText);
        mHeaderRightText.setOnClickListener(mClickListener);

        // init titile
        mServiceNameStrings = getResources()
                .getStringArray(R.array.service_des);
        if (mServiceType == ServiceType.TOGETHER_SERVICE)
        {
            mHeaderLeftText.setText(getString(R.string.together));
        }
        else if (mServiceType == ServiceType.MARRIAGE_SERVICE)
        {
            mHeaderLeftText.setText(getString(R.string.marriage_dating));
        }
        else
        {
            mHeaderLeftText.setText(mServiceNameStrings[mServiceType]);
        }

        mDateTimeCtrl = new DateTimePopupWindow(mContext, this);
        mOptionPopWin = new OptionPopupWindow(this, this);

        mEmoticonImage = (ImageView) this.findViewById(R.id.emoticonImage);
        mEmoticonImage.setOnClickListener(mClickListener);
        mPhotoImage = (ImageView) this.findViewById(R.id.photoImage);
        mPhotoImage.setOnClickListener(mClickListener);
        mPhotoCountText = (TextView) this.findViewById(R.id.photoCountText);
        mEmoticonView = (EmoticonView) this.findViewById(R.id.emoticonView);
        mEmoticonView.setListener(this);
        mPhotoListFragment = (PhotoListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.photoListFragment);
        mPhotoListFragment.setPhotoNumChangeListener(this);
        showPhotoFragment(false);

        mInputManager = ((InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE));

        // init layout
        mContainerLayout = (LinearLayout) this
                .findViewById(R.id.containerLayout);

        switch (mServiceType)
        {
            case ServiceType.GENERAL_SERVICE:
                initGeneralTopicView();
                break;
            case ServiceType.ACTIVITY_SERVICE:
                initActivityTopicView();
                break;
            case ServiceType.CARPOOL_SERVICE:
                initCarpoolTopicView();
                break;
            case ServiceType.MARRIAGE_SERVICE:
                initMarriageTopicView();
                break;
            case ServiceType.TOGETHER_SERVICE:
                initTogetherTopicView();
                break;
            case ServiceType.PET_SERVICE:
                initPetTopicView();
                break;
            case ServiceType.SECONDHAND_SERVICE:
                initSecondHandTopicView();
                break;
            case ServiceType.RENTHOUSE_SERVICE:
                initRentHouseTopicView();
                break;
            default:
                break;
        }
    }

    private void initGeneralTopicView()
    {
        View v = LayoutInflater.from(mContext).inflate(
                R.layout.topicpublish_general, null);
        mContainerLayout.addView(v, 0);

        mTitleEdit = (EditText) v.findViewById(R.id.titleEdit);
        mContentEdit = (EditText) v.findViewById(R.id.contentEdit);

    }

    private void initActivityTopicView()
    {
        View v = LayoutInflater.from(mContext).inflate(
                R.layout.topicpublish_activity, null);
        mContainerLayout.addView(v, 0);

        mActivityNameEdit = (EditText) v.findViewById(R.id.activityNameEdit);
        mStartTimeText = (TextView) v.findViewById(R.id.startTimeText);
        mEndTimeText = (TextView) v.findViewById(R.id.endTimeText);
        mAddressEdit = (EditText) v.findViewById(R.id.addressEdit);
        mContentEdit = (EditText) v.findViewById(R.id.contentEdit);

        mStartTimeText.setOnClickListener(mClickListener);
        mEndTimeText.setOnClickListener(mClickListener);
    }

    private void initCarpoolTopicView()
    {
        View v = LayoutInflater.from(mContext).inflate(
                R.layout.topicpublish_carpool, null);
        mContainerLayout.addView(v, 0);

        mFindCarBtn = (RadioButton) v.findViewById(R.id.findCarBtn);
        mFindPassengerBtn = (RadioButton) v.findViewById(R.id.findPassengerBtn);
        mStartAddressEdit = (EditText) v.findViewById(R.id.startAddressEdit);
        mEndAddressEdit = (EditText) v.findViewById(R.id.endAddressEdit);
        mStartTimeText = (TextView) v.findViewById(R.id.startTimeText);
        mContentEdit = (EditText) v.findViewById(R.id.contentEdit);

        mStartTimeText.setOnClickListener(mClickListener);
    }

    private void initMarriageTopicView()
    {
        View v = LayoutInflater.from(mContext).inflate(
                R.layout.topicpublish_marriage_dating, null);
        mContainerLayout.addView(v, 0);

        mMarriageBtn = (RadioButton) v.findViewById(R.id.marriageBtn);
        mDatingBtn = (RadioButton) v.findViewById(R.id.datingBtn);
        mManBtn = (RadioButton) v.findViewById(R.id.manBtn);
        mWomanBtn = (RadioButton) v.findViewById(R.id.womanBtn);
        mAgeEdit = (EditText) v.findViewById(R.id.ageEdit);
        mJobEdit = (EditText) v.findViewById(R.id.jobEdit);
        mTitleEdit = (EditText) v.findViewById(R.id.titleEdit);
        mContentEdit = (EditText) v.findViewById(R.id.contentEdit);
    }

    private void initTogetherTopicView()
    {
        View v = LayoutInflater.from(mContext).inflate(
                R.layout.topicpublish_together, null);
        mContainerLayout.addView(v, 0);

        mFitnessBtn = (RadioButton) v.findViewById(R.id.fitnessBtn);
        mPlayBtn = (RadioButton) v.findViewById(R.id.playBtn);
        mStartTimeText = (TextView) v.findViewById(R.id.startTimeText);
        mEndTimeText = (TextView) v.findViewById(R.id.endTimeText);
        mIndoorBtn = (RadioButton) v.findViewById(R.id.indoorBtn);
        mOutdoorBtn = (RadioButton) v.findViewById(R.id.outdoorBtn);
        mTitleEdit = (EditText) v.findViewById(R.id.titleEdit);
        mContentEdit = (EditText) v.findViewById(R.id.contentEdit);

        mStartTimeText.setOnClickListener(mClickListener);
        mEndTimeText.setOnClickListener(mClickListener);
    }

    private void initPetTopicView()
    {
        View v = LayoutInflater.from(mContext).inflate(
                R.layout.topicpublish_pet, null);
        mContainerLayout.addView(v, 0);

        mAdoptionBtn = (RadioButton) v.findViewById(R.id.adoptionBtn);
        mDatingBtn = (RadioButton) v.findViewById(R.id.datingBtn);
        mManBtn = (RadioButton) v.findViewById(R.id.manBtn);
        mWomanBtn = (RadioButton) v.findViewById(R.id.womanBtn);
        mVarietyText = (TextView) v.findViewById(R.id.varietyText);
        mAgeEdit = (EditText) v.findViewById(R.id.ageEdit);
        mTitleEdit = (EditText) v.findViewById(R.id.titleEdit);
        mContentEdit = (EditText) v.findViewById(R.id.contentEdit);

        mVarietyText.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    if (mVarietyDialog == null)
                    {
                        mVarietyDialog = DialogUtil.createSingleChoiceDialog(
                                mContext, "请选择品种", OptionBuilder.instance()
                                        .getPetBreedArray(),
                                new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                            int which)
                                    {
                                        BaseOptionModel optionModel = (BaseOptionModel) OptionBuilder
                                                .instance().getPetBreedList()
                                                .get(which);
                                        mVarietyText.setText(optionModel
                                                .getName());
                                        mVarietyId = optionModel.get_id();

                                        mVarietyDialog.dismiss();
                                    }

                                });
                    }
                    mVarietyDialog.show();
                }
                return false;
            }

        });
    }

    private void initSecondHandTopicView()
    {
        View v = LayoutInflater.from(mContext).inflate(
                R.layout.topicpublish_secondhand, null);
        mContainerLayout.addView(v, 0);

        mBuyBtn = (RadioButton) v.findViewById(R.id.buyBtn);
        mSellBtn = (RadioButton) v.findViewById(R.id.sellBtn);
        mCagetoryText = (TextView) v.findViewById(R.id.cagetoryText);
        mConditionText = (TextView) v.findViewById(R.id.conditionEdit);
        mOldPriceEdit = (EditText) v.findViewById(R.id.oldPriceEdit);
        mNewPriceEdit = (EditText) v.findViewById(R.id.newPriceEdit);
        mTitleEdit = (EditText) v.findViewById(R.id.titleEdit);
        mContentEdit = (EditText) v.findViewById(R.id.contentEdit);

        mCagetoryText.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    if (mCategoryDialog == null)
                    {
                        mCategoryDialog = DialogUtil.createSingleChoiceDialog(
                                mContext, "请选择分类", OptionBuilder.instance()
                                        .getGoodClassifyArray(),
                                new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                            int which)
                                    {
                                        BaseOptionModel optionModel = (BaseOptionModel) OptionBuilder
                                                .instance()
                                                .getGoodClassifyList()
                                                .get(which);
                                        mCagetoryText.setText(optionModel
                                                .getName());
                                        mCategoryId = optionModel.get_id();

                                        mCategoryDialog.dismiss();
                                    }

                                });
                    }
                    mCategoryDialog.show();
                }
                return false;
            }
        });

        mConditionText.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    if (mConditionDialog == null)
                    {
                        mConditionDialog = DialogUtil.createSingleChoiceDialog(
                                mContext, "请选择新旧程度", OptionBuilder.instance()
                                        .getGoodConditionArray(),
                                new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                            int which)
                                    {
                                        mConditionText
                                                .setText(OptionBuilder
                                                        .instance()
                                                        .getGoodConditionArray()[which]);
                                        mConditionValue = 10 - which;
                                        mConditionDialog.dismiss();
                                    }

                                });
                    }
                    mConditionDialog.show();
                }
                return false;
            }

        });
    }

    private void initRentHouseTopicView()
    {
        View v = LayoutInflater.from(mContext).inflate(
                R.layout.topicpublish_renthouse, null);
        mContainerLayout.addView(v, 0);

        mRentHouseBtn = (RadioButton) v.findViewById(R.id.rentHouseBtn);
        mLeaseHouseBtn = (RadioButton) v.findViewById(R.id.leaseHouseBtn);
        mCommunityText = (TextView) v.findViewById(R.id.communityText);
        mHouseTypeText = (TextView) v.findViewById(R.id.houseTypeText);
        mRentMoneyEdit = (EditText) v.findViewById(R.id.rentMoneyEdit);
        mTitleEdit = (EditText) v.findViewById(R.id.titleEdit);
        mContentEdit = (EditText) v.findViewById(R.id.contentEdit);

        mHouseTypeText.setOnClickListener(mClickListener);
        mCommunityText.setOnClickListener(mClickListener);
    }

    private OnClickListener mClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            // hide keyboard
            FunctionUtils.hideInputMethod(mContext);

            switch (v.getId())
            {
                case R.id.startTimeText:
                    mSelectStartFlag = true;
                    mDateTimeCtrl.showPopupWindow(v);
                    break;
                case R.id.endTimeText:
                    mSelectStartFlag = false;
                    mDateTimeCtrl.showPopupWindow(v);
                    break;
                case R.id.communityText:
                    toSelectCommunity();
                    break;
                case R.id.houseTypeText:

                    if (mSelectHouseTypeDialog == null)
                    {
                        mSelectHouseTypeDialog = DialogUtil
                                .createSingleChoiceDialog(mContext, "请选择户型",
                                        OptionBuilder.instance()
                                                .getHouseTypeArray(),
                                        new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which)
                                            {
                                                HouseTypeOptionModel houseTypeModel = (HouseTypeOptionModel) OptionBuilder
                                                        .instance()
                                                        .getHouseTypeList()
                                                        .get(which);
                                                mHouseTypeText
                                                        .setText(houseTypeModel
                                                                .getName());
                                                mRoom = houseTypeModel
                                                        .getRoom();
                                                mHall = houseTypeModel
                                                        .getHall();
                                                mWashRoom = houseTypeModel
                                                        .getWashroom();

                                                mSelectHouseTypeDialog
                                                        .dismiss();
                                            }

                                        });
                    }
                    mSelectHouseTypeDialog.show();
                    break;
                case R.id.emoticonImage:
                    onEmoticonImageClick();
                    break;
                case R.id.photoImage:
                    onPhotoImageClick();
                    break;
                case R.id.headerLeftText:
                    finish();
                    break;
                case R.id.headerRightText:
                    onHeaderRightClick();
                    break;
                default:
                    break;
            }
        }
    };

    private void onEmoticonImageClick()
    {
        mContentEdit.requestFocus();

        showEmotIconView(true);
        showPhotoFragment(false);
    }

    private void onPhotoImageClick()
    {
        showEmotIconView(false);
        showPhotoFragment(true);
    }

    private void showEmotIconView(boolean show)
    {
        if (show)
        {
            mInputManager.hideSoftInputFromWindow(
                    mContentEdit.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            mEmoticonView.setVisibility(View.VISIBLE);
        }
        else
        {
            mEmoticonView.setVisibility(View.GONE);
        }

    }

    private void showPhotoFragment(boolean show)
    {
        if (show && mPhotoListFragment.isVisible())
        {
            mInputManager.hideSoftInputFromWindow(
                    mContentEdit.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            return;
        }

        if (!show && mPhotoListFragment.isHidden())
        {
            return;
        }

        FragmentTransaction ft = this.getSupportFragmentManager()
                .beginTransaction();
        // ft.setCustomAnimations(android.R.animator.fade_in,
        // android.R.animator.fade_out);

        if (show)
        {
            mInputManager.hideSoftInputFromWindow(
                    mContentEdit.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            ft.show(mPhotoListFragment);
        }
        else
        {
            ft.hide(mPhotoListFragment);
        }
        ft.commit();
    }

    @Override
    protected void onHeaderRightClick()
    {
        FunctionUtils.hideInputMethod(mContext);

        if (!DeviceInfoUtil.isHaveInternet(mContext))
        {
            ToastUtil.showToastShort(R.string.network_exception);
            return;
        }

        List<NameValuePair> paramList = buildPostParamList();
        if (paramList == null)
        {
            return;
        }

        if(!AppApplication.checkLoginState(mContext))
        {
            return;
        }

        publishTopicByBack();
    }

    private void publishTopicByBack()
    {
        TopicPublishModel requestModel = new TopicPublishModel();
        requestModel.setTopicType(mTopicType);
        requestModel.setImageList(mPhotoListFragment.getSelectPhotoList());
        requestModel.setParamList(buildPostParamList());

        if (AppService.instance() != null)
        {
            AppService.instance().pushRequestToService(requestModel);
        }
        finish();
    }

    private List<NameValuePair> buildPostParamList()
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();

        boolean result = false;
        switch (mServiceType)
        {
            case ServiceType.GENERAL_SERVICE:
                result = buildGeneralTopicParam(list);
                break;
            case ServiceType.ACTIVITY_SERVICE:
                result = buildActivityTopicParam(list);
                break;
            case ServiceType.CARPOOL_SERVICE:
                result = buildCarPoolTopicParam(list);
                break;
            case ServiceType.MARRIAGE_SERVICE:
                result = buildMarriageDatingTopicParam(list);
                break;
            case ServiceType.TOGETHER_SERVICE:
                result = buildTogetherTopicParam(list);
                break;
            case ServiceType.PET_SERVICE:
                result = buildPetTopicParam(list);
                break;
            case ServiceType.SECONDHAND_SERVICE:
                result = buildSecondHandTopicParam(list);
                break;
            case ServiceType.RENTHOUSE_SERVICE:
                result = buildRentHouseTopicParam(list);
                break;
            default:
                break;
        }

        if (!result)
        {
            return null;
        }

        list.add(new BasicNameValuePair("bid", mBoardId));

        for (int i = 0; i < mUploadPhotoList.size(); i++)
        {
            list.add(new BasicNameValuePair("images", mUploadPhotoList.get(i)));
        }

        // to do list!!!
        // list.add(new BasicNameValuePair("descript", ""));
        list.add(new BasicNameValuePair("longitude", String
                .valueOf(GlobalConfig.getLongitude())));
        list.add(new BasicNameValuePair("latitude", String.valueOf(GlobalConfig
                .getLatitude())));
        return list;
    }

    private String buildPostParam()
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();

        boolean result = false;
        switch (mServiceType)
        {
            case ServiceType.GENERAL_SERVICE:
                result = buildGeneralTopicParam(list);
                break;
            case ServiceType.ACTIVITY_SERVICE:
                result = buildActivityTopicParam(list);
                break;
            case ServiceType.CARPOOL_SERVICE:
                result = buildCarPoolTopicParam(list);
                break;
            case ServiceType.MARRIAGE_SERVICE:
                result = buildMarriageDatingTopicParam(list);
                break;
            case ServiceType.TOGETHER_SERVICE:
                result = buildTogetherTopicParam(list);
                break;
            case ServiceType.PET_SERVICE:
                result = buildPetTopicParam(list);
                break;
            case ServiceType.SECONDHAND_SERVICE:
                result = buildSecondHandTopicParam(list);
                break;
            case ServiceType.RENTHOUSE_SERVICE:
                result = buildRentHouseTopicParam(list);
                break;
            default:
                break;
        }

        if (!result)
        {
            return null;
        }

        list.add(new BasicNameValuePair("bid", mBoardId));

        for (int i = 0; i < mUploadPhotoList.size(); i++)
        {
            list.add(new BasicNameValuePair("images", mUploadPhotoList.get(i)));
        }

        // to do list!!!
        // list.add(new BasicNameValuePair("descript", ""));
        list.add(new BasicNameValuePair("longitude", String
                .valueOf(GlobalConfig.getLongitude())));
        list.add(new BasicNameValuePair("latitude", String.valueOf(GlobalConfig
                .getLatitude())));
        return URLEncodedUtils.format(list, HTTP.UTF_8);
    }

    private boolean buildGeneralTopicParam(List<NameValuePair> listParam)
    {
        String title = mTitleEdit.getText().toString().trim();
        if (TextUtils.isEmpty(title))
        {
            ToastUtil.showToastShort(R.string.please_input_title);
            return false;
        }

        String content = mContentEdit.getText().toString().trim();
        if (TextUtils.isEmpty(content))
        {
            ToastUtil.showToastShort(R.string.please_input_content);
            return false;
        }

        listParam.add(new BasicNameValuePair("title", title));
        listParam.add(new BasicNameValuePair("content", content));
        return true;
    }

    private boolean buildActivityTopicParam(List<NameValuePair> listParam)
    {
        String activityName = mActivityNameEdit.getText().toString().trim();
        if (TextUtils.isEmpty(activityName))
        {
            ToastUtil.showToastShort(R.string.please_input_activityname);
            return false;
        }

        String startTime = mStartTimeText.getText().toString().trim();
        if (TextUtils.isEmpty(startTime))
        {
            // ToastUtils.showToastShort(R.string.please_input_job);
            // return false;
        }

        String endTime = mEndTimeText.getText().toString().trim();
        if (TextUtils.isEmpty(endTime))
        {
            // ToastUtils.showToastShort(R.string.please_input_job);
            // return false;
        }

        String address = mAddressEdit.getText().toString().trim();
        if (TextUtils.isEmpty(address))
        {
            ToastUtil.showToastShort(R.string.please_input_address);
            return false;
        }

        String content = mContentEdit.getText().toString().trim();
        if (TextUtils.isEmpty(content))
        {
            ToastUtil.showToastShort(R.string.please_input_content);
            return false;
        }

        listParam.add(new BasicNameValuePair("start_time", String
                .valueOf(mSelectStartTime)));
        listParam.add(new BasicNameValuePair("end_time", String
                .valueOf(mSelectEndTime)));
        listParam.add(new BasicNameValuePair("location", address));
        listParam.add(new BasicNameValuePair("apply_start_time", "0"));
        listParam.add(new BasicNameValuePair("apply_end_time", "0"));
        listParam.add(new BasicNameValuePair("num", "0"));
        listParam.add(new BasicNameValuePair("fee", "0"));

        listParam.add(new BasicNameValuePair("title", activityName));
        listParam.add(new BasicNameValuePair("content", content));
        return true;
    }

    private boolean buildCarPoolTopicParam(List<NameValuePair> listParam)
    {
        int needType = mFindCarBtn.isChecked() ? 1 : 2;

        String startAddress = mStartAddressEdit.getText().toString().trim();
        if (TextUtils.isEmpty(startAddress))
        {
            ToastUtil.showToastShort(R.string.please_input_start_address);
            return false;
        }

        String endAddress = mEndAddressEdit.getText().toString().trim();
        if (TextUtils.isEmpty(endAddress))
        {
            ToastUtil.showToastShort(R.string.please_input_end_address);
            return false;
        }

        String startTime = mStartTimeText.getText().toString().trim();
        if (TextUtils.isEmpty(startTime))
        {
            ToastUtil.showToastShort(R.string.please_input_start_time);
            return false;
        }

        String content = mContentEdit.getText().toString().trim();
        if (TextUtils.isEmpty(content))
        {
            ToastUtil.showToastShort(R.string.please_input_content);
            return false;
        }

        listParam.add(new BasicNameValuePair("service_type", String
                .valueOf(mServiceType - 1)));
        listParam.add(new BasicNameValuePair("need_type", String
                .valueOf(needType)));
        listParam.add(new BasicNameValuePair("start_time", String
                .valueOf(mSelectStartTime)));
        listParam.add(new BasicNameValuePair("end_time", "0"));
        listParam.add(new BasicNameValuePair("price", "0"));
        listParam.add(new BasicNameValuePair("start_location", startAddress));
        listParam.add(new BasicNameValuePair("end_location", endAddress));
        listParam.add(new BasicNameValuePair("title", ""));
        listParam.add(new BasicNameValuePair("content", content));
        return true;
    }

    private boolean buildMarriageDatingTopicParam(List<NameValuePair> listParam)
    {
        int needType = mMarriageBtn.isChecked() ? 1 : 2;
        int gender = mManBtn.isChecked() ? 1 : 0;

        String age = mAgeEdit.getText().toString().trim();
        if (TextUtils.isEmpty(age))
        {
            ToastUtil.showToastShort(R.string.please_input_age);
            return false;
        }

        String job = mJobEdit.getText().toString().trim();
        if (TextUtils.isEmpty(job))
        {
            ToastUtil.showToastShort(R.string.please_input_job);
            return false;
        }

        String title = mTitleEdit.getText().toString().trim();
        if (TextUtils.isEmpty(title))
        {
            ToastUtil.showToastShort(R.string.please_input_title);
            return false;
        }

        String content = mContentEdit.getText().toString().trim();
        if (TextUtils.isEmpty(content))
        {
            ToastUtil.showToastShort(R.string.please_input_content);
            return false;
        }

        listParam.add(new BasicNameValuePair("service_type", String
                .valueOf(mServiceType - 1)));
        listParam.add(new BasicNameValuePair("need_type", String
                .valueOf(needType)));
        listParam.add(new BasicNameValuePair("start_time", "0"));
        listParam.add(new BasicNameValuePair("end_time", "0"));
        listParam.add(new BasicNameValuePair("price", "0"));
        listParam.add(new BasicNameValuePair("start_location", ""));
        listParam.add(new BasicNameValuePair("end_location", ""));
        listParam.add(new BasicNameValuePair("title", title));
        listParam.add(new BasicNameValuePair("content", content));

        listParam.add(new BasicNameValuePair("gender", String.valueOf(gender)));
        listParam.add(new BasicNameValuePair("age", age));
        listParam.add(new BasicNameValuePair("profession", job));
        return true;
    }

    private boolean buildTogetherTopicParam(List<NameValuePair> listParam)
    {
        int needType = mFitnessBtn.isChecked() ? 1 : 2;
        int indoor = mIndoorBtn.isChecked() ? 1 : 2;

        String startTime = mStartTimeText.getText().toString().trim();
        if (TextUtils.isEmpty(startTime))
        {
            startTime = "";
            // ToastUtils.showToastShort(R.string.please_input_job);
            // return false;
        }

        String endTime = mEndTimeText.getText().toString().trim();
        if (TextUtils.isEmpty(endTime))
        {
            endTime = "";
            // ToastUtils.showToastShort(R.string.please_input_job);
            // return false;
        }

        String title = mTitleEdit.getText().toString().trim();
        if (TextUtils.isEmpty(title))
        {
            ToastUtil.showToastShort(R.string.please_input_title);
            return false;
        }

        String content = mContentEdit.getText().toString().trim();
        if (TextUtils.isEmpty(content))
        {
            ToastUtil.showToastShort(R.string.please_input_content);
            return false;
        }

        listParam.add(new BasicNameValuePair("service_type", String
                .valueOf(mServiceType - 1)));
        listParam.add(new BasicNameValuePair("need_type", String
                .valueOf(needType)));
        listParam.add(new BasicNameValuePair("start_time", String
                .valueOf(mSelectStartTime)));
        listParam.add(new BasicNameValuePair("end_time", String
                .valueOf(mSelectEndTime)));
        listParam.add(new BasicNameValuePair("indoor", String.valueOf(indoor)));
        listParam.add(new BasicNameValuePair("price", "0"));
        listParam.add(new BasicNameValuePair("start_location", ""));
        listParam.add(new BasicNameValuePair("end_location", ""));
        listParam.add(new BasicNameValuePair("title", title));
        listParam.add(new BasicNameValuePair("content", content));

        return true;
    }

    private boolean buildPetTopicParam(List<NameValuePair> listParam)
    {
        int needType = mAdoptionBtn.isChecked() ? 1 : 2;
        int gender = mManBtn.isChecked() ? 1 : 0;

        String variety = mVarietyText.getText().toString().trim();
        if (TextUtils.isEmpty(variety))
        {
            ToastUtil.showToastShort(R.string.please_input_variety);
            return false;
        }

        String age = mAgeEdit.getText().toString().trim();
        if (TextUtils.isEmpty(age))
        {
            ToastUtil.showToastShort(R.string.please_input_age);
            return false;
        }

        String title = mTitleEdit.getText().toString().trim();
        if (TextUtils.isEmpty(title))
        {
            ToastUtil.showToastShort(R.string.please_input_title);
            return false;
        }

        String content = mContentEdit.getText().toString().trim();
        if (TextUtils.isEmpty(content))
        {
            ToastUtil.showToastShort(R.string.please_input_content);
            return false;
        }

        listParam.add(new BasicNameValuePair("service_type", String
                .valueOf(mServiceType - 1)));
        listParam.add(new BasicNameValuePair("need_type", String
                .valueOf(needType)));
        listParam.add(new BasicNameValuePair("start_time", "0"));
        listParam.add(new BasicNameValuePair("end_time", "0"));
        listParam.add(new BasicNameValuePair("price", "0"));
        listParam.add(new BasicNameValuePair("start_location", ""));
        listParam.add(new BasicNameValuePair("end_location", ""));
        listParam.add(new BasicNameValuePair("title", title));
        listParam.add(new BasicNameValuePair("content", content));

        listParam.add(new BasicNameValuePair("gender", String.valueOf(gender)));
        listParam.add(new BasicNameValuePair("age", age));
        listParam.add(new BasicNameValuePair("breed", mVarietyId));
        return true;
    }

    private boolean buildSecondHandTopicParam(List<NameValuePair> listParam)
    {
        int needType = this.mBuyBtn.isChecked() ? 1 : 2;

        String category = mCagetoryText.getText().toString().trim();
        if (TextUtils.isEmpty(category))
        {
            ToastUtil.showToastShort(R.string.please_input_category);
            return false;
        }

        String condition = mConditionText.getText().toString().trim();
        if (TextUtils.isEmpty(condition))
        {
            ToastUtil.showToastShort(R.string.please_input_condition);
            return false;
        }

        String oldPrice = mOldPriceEdit.getText().toString().trim();
        if (TextUtils.isEmpty(oldPrice))
        {
            ToastUtil.showToastShort(R.string.please_input_oldprice);
            return false;
        }

        String newPrice = mNewPriceEdit.getText().toString().trim();
        if (TextUtils.isEmpty(newPrice))
        {
            ToastUtil.showToastShort(R.string.please_input_newprice);
            return false;
        }

        String title = mTitleEdit.getText().toString().trim();
        if (TextUtils.isEmpty(title))
        {
            ToastUtil.showToastShort(R.string.please_input_title);
            return false;
        }

        String content = mContentEdit.getText().toString().trim();
        if (TextUtils.isEmpty(content))
        {
            ToastUtil.showToastShort(R.string.please_input_content);
            return false;
        }

        listParam.add(new BasicNameValuePair("service_type", String
                .valueOf(mServiceType - 1)));
        listParam.add(new BasicNameValuePair("need_type", String
                .valueOf(needType)));
        listParam.add(new BasicNameValuePair("start_time", "0"));
        listParam.add(new BasicNameValuePair("end_time", "0"));
        listParam.add(new BasicNameValuePair("price", newPrice));
        listParam.add(new BasicNameValuePair("start_location", ""));
        listParam.add(new BasicNameValuePair("end_location", ""));
        listParam.add(new BasicNameValuePair("title", title));
        listParam.add(new BasicNameValuePair("content", content));

        listParam.add(new BasicNameValuePair("classify", mCategoryId));
        listParam.add(new BasicNameValuePair("recency", String
                .valueOf(mConditionValue)));
        listParam.add(new BasicNameValuePair("original_price", oldPrice));
        return true;
    }

    private boolean buildRentHouseTopicParam(List<NameValuePair> listParam)
    {
        int needType = this.mRentHouseBtn.isChecked() ? 1 : 2;

        String title = mTitleEdit.getText().toString().trim();
        if (TextUtils.isEmpty(title))
        {
            ToastUtil.showToastShort(R.string.please_input_title);
            return false;
        }

        String community = mCommunityText.getText().toString().trim();
        if (TextUtils.isEmpty(community))
        {
            ToastUtil.showToastShort(R.string.please_input_community);
            return false;
        }

        String houseType = mHouseTypeText.getText().toString().trim();
        if (TextUtils.isEmpty(houseType))
        {
            ToastUtil.showToastShort(R.string.please_input_housetype);
            return false;
        }

        String rentMoney = mRentMoneyEdit.getText().toString().trim();
        if (TextUtils.isEmpty(rentMoney))
        {
            ToastUtil.showToastShort(R.string.please_input_rentmoney);
            return false;
        }

        String content = mContentEdit.getText().toString().trim();
        if (TextUtils.isEmpty(content))
        {
            ToastUtil.showToastShort(R.string.please_input_content);
            return false;
        }

        listParam.add(new BasicNameValuePair("service_type", String
                .valueOf(mServiceType - 1)));
        listParam.add(new BasicNameValuePair("need_type", String
                .valueOf(needType)));
        listParam.add(new BasicNameValuePair("start_time", "0"));
        listParam.add(new BasicNameValuePair("end_time", "0"));
        listParam.add(new BasicNameValuePair("price", rentMoney));
        listParam.add(new BasicNameValuePair("start_location", ""));
        listParam.add(new BasicNameValuePair("end_location", ""));
        listParam.add(new BasicNameValuePair("title", title));
        listParam.add(new BasicNameValuePair("content", content));

        listParam.add(new BasicNameValuePair("community", mCommunityId));
        listParam.add(new BasicNameValuePair("apartment_room", String
                .valueOf(mRoom)));
        listParam.add(new BasicNameValuePair("apartment_hall", String
                .valueOf(mHall)));
        listParam.add(new BasicNameValuePair("apartment_washroom", String
                .valueOf(mWashRoom)));
        return true;
    }

    private void doUploadPhotoList(String token)
    {
        for (int i = 0; i < mSelectAlbumList.size(); i++)
        {
            AlbumItemModel itemModel = mSelectAlbumList.get(i);
            doUploadPhoto(itemModel.getPath(), token);
        }

        mUploadPhotoList.clear();
        mTotalUploadNum = mSelectAlbumList.size();
        mTotalUploadSuccessNum = 0;
        mTotalUploadFailedNum = 0;

        doCheckUploadFinish();
    }

    private void doCheckUploadFinish()
    {
        if (mTotalUploadSuccessNum + mTotalUploadFailedNum >= mTotalUploadNum)
        {
            this.doTopicPublish();
        }
        else
        {
            new Handler().postDelayed(new Runnable()
            {
                public void run()
                {
                    doCheckUploadFinish();
                }
            }, 500);
        }
    }

    private void doGetUploadToken()
    {
        GetUploadTokenRequest request = new GetUploadTokenRequest(
                new ResponseEventHandler<RespUploadTokenModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespUploadTokenModel> request,
                            RespUploadTokenModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            String uploadToken = result.getData().getToken();
                            doUploadPhotoList(uploadToken);
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                        mProgressDialog.dismiss();
                    }

                });
        executeRequest(request);
    }

    private void doUploadPhoto(String filename, String upToken)
    {
        String key = IO.UNDEFINED_KEY;
        PutExtra extra = new PutExtra();
        extra.params = new HashMap<String, String>();
        extra.params.put("x:source", "headImage");
        extra.mimeType = "image/jpeg";

        InputStreamAt imageStream = new InputStreamAt(
                BitmapUtil.compressImage(filename));

        IO.put(upToken, key, imageStream, extra, new JSONObjectRet()
        // Uri uri = Uri.fromFile(new File(filename));
        // IO.putFile(this, upToken, key, uri, extra, new
        // JSONObjectRet()
                {
                    @Override
                    public void onProcess(long current, long total)
                    {
                    }

                    @Override
                    public void onSuccess(JSONObject resp)
                    {
                        try
                        {
                            if (resp.has("success"))
                            {
                                if (resp.getBoolean("success"))
                                {
                                    mUploadPhotoList.add(resp.getString("name"));
                                    mTotalUploadSuccessNum++;
                                    return;
                                }
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        mTotalUploadFailedNum++;
                    }

                    @Override
                    public void onFailure(QiniuException ex)
                    {
                        LogUtil.e("upload avatar failed");
                    }
                });
    }

    private void doTopicPublish()
    {
        String params = buildPostParam();
        if (TextUtils.isEmpty(params))
        {
            return;
        }

        doTopicPublishRequest(mTopicType, params);
    }

    private void doTopicPublishRequest(int topicType, String param)
    {
        TopicPublishRequest request = new TopicPublishRequest(topicType, param,
                new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request, BaseModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            ToastUtil.showToastShort("发布成功~");
                            setResult(RESULT_OK);
                            finish();
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                        }

                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                        mProgressDialog.dismiss();
                    }

                });
        executeRequest(request);
    }

    private void toSelectCommunity()
    {
        Intent intent = new Intent(mContext, CommunityActivity.class);
        startActivityForResult(intent, GlobalConfig.INTENT_SELECT_COMMUNITY);
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
            case GlobalConfig.INTENT_LOGIN:
                break;
            case GlobalConfig.INTENT_SELECT_COMMUNITY:
                mCommunityId = data.getStringExtra("community_id");
                mCommunityText.setText(data.getStringExtra("community_name"));
                break;
            default:
                break;
        }
    }

    @Override
    public void onDateTimeResult(long time, String timeStr)
    {
        if (mSelectStartFlag)
        {
            mSelectStartTime = time / 1000;
            if (mServiceType == ServiceType.TOGETHER_SERVICE)
            {
                mStartTimeText.setText(TimeUtil.getTimeStr(time));
            }
            else
            {
                mStartTimeText.setText(timeStr);
            }

        }
        else
        {
            mSelectEndTime = time / 1000;

            if (mServiceType == ServiceType.TOGETHER_SERVICE)
            {
                mEndTimeText.setText(TimeUtil.getTimeStr(time));
            }
            else
            {
                mEndTimeText.setText(timeStr);
            }
        }
    }

    @Override
    public void onOptionItemClick(BaseOptionModel model)
    {
        HouseTypeOptionModel houseTypeModel = (HouseTypeOptionModel) model;
        mHouseTypeText.setText(houseTypeModel.getName());
        mRoom = houseTypeModel.getRoom();
        mHall = houseTypeModel.getHall();
        mWashRoom = houseTypeModel.getWashroom();
    }

    @Override
    public void onPhotoNumChanged(int selectNum)
    {
        if (selectNum > 0)
        {
            mPhotoCountText.setVisibility(View.VISIBLE);
            mPhotoCountText.setText(String.valueOf(selectNum));
        }
        else
        {
            mPhotoCountText.setVisibility(View.GONE);
        }
    }

    private int getEditTextCursorIndex(EditText editText)
    {
        return editText.getSelectionStart();
    }

    private void insertText(EditText editText, CharSequence text)
    {
        editText.getText().insert(getEditTextCursorIndex(editText), text);
    }

    @Override
    public void onEmoticonClick(CharSequence cs)
    {
        mContentEdit.requestFocus();
        insertText(mContentEdit, cs);
    }

    @Override
    public void onEmoticonDelete()
    {
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action, code);
        mContentEdit.onKeyDown(KeyEvent.KEYCODE_DEL, event);
    }

    @Override
    protected void onLoginResult(int from, int result)
    {
        if (result != ErrorCode.SUCCESS)
        {
            return;
        }

        publishTopicByBack();
    }
}
