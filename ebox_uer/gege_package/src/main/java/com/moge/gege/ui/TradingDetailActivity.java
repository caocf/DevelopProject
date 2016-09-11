package com.moge.gege.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.android.volley.VolleyError;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.data.dao.ShopcartDAO;
import com.moge.gege.model.*;
import com.moge.gege.model.enums.LoginFromType;
import com.moge.gege.model.enums.PromotionStyleType;
import com.moge.gege.model.enums.TopicType;
import com.moge.gege.model.enums.TradingType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.ChannelPromotionRequest;
import com.moge.gege.network.request.TradingBuyListRequest;
import com.moge.gege.network.request.TradingDetailRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.AvatarImageAdapter;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.ui.widget.ImagePageView;
import com.moge.gege.ui.widget.ImagePageView.ImagePageViewListener;
import com.moge.gege.ui.widget.NumberEditView;
import com.moge.gege.ui.widget.ShareDialog;
import com.moge.gege.util.FunctionUtils;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.widget.horizontalListview.widget.HListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class TradingDetailActivity extends BaseActivity implements
        OnClickListener, ImagePageViewListener, NumberEditView.OnNumberViewChangeListener
{
    private Activity mContext;

    @InjectView(R.id.tradingView) ImagePageView mTradingView;
    @InjectView(R.id.priceLayout) LinearLayout mPriceLayout;
    @InjectView(R.id.priceText) TextView mPriceText;
    @InjectView(R.id.titleText) TextView mTitleText;
    @InjectView(R.id.praiseInfoLayout) RelativeLayout mPraiseInfoLayout;
    @InjectView(R.id.userBuyCountText) TextView mUserBuyCountText;
    @InjectView(R.id.countInfoText) TextView mCountInfoText;
    @InjectView(R.id.praiseText) TextView mPraiseText;
    private HListView mUserBuyListView;
    private AvatarImageAdapter mAdapter;
    private List<String> mAvatarList = new ArrayList<String>();

    @InjectView(R.id.userImage) ImageView mUserImage;
    @InjectView(R.id.sellerNameText) TextView mSellerNameText;
    @InjectView(R.id.sellerCommunityText) TextView mSellerCommunityText;
    @InjectView(R.id.chatText) TextView mChatText;
    @InjectView(R.id.infoText) TextView mInfoText;
    @InjectView(R.id.imageDetailLayout) RelativeLayout mImageDetailLayout;
    @InjectView(R.id.addToShoppingCardText) TextView mAddToShoppingCardText;
    @InjectView(R.id.buyNowText) TextView mBuyNowText;
    @InjectView(R.id.buyLayout) RelativeLayout mBuyLayout;
    @InjectView(R.id.secondKillBtn) Button mSecondKillBtn;

    @InjectView(R.id.fixPromotionLayout) LinearLayout mFixPromotionLayout;
    @InjectView(R.id.leftInfoText) TextView mLeftInfoText;
    @InjectView(R.id.leftTimeText) TextView mLeftTimeText;

    @InjectView(R.id.goodNumberEdit) NumberEditView mGoodNumberEdit;

    private String mTradingId;
    private TradingDetailModel mTradingDetailModel;
    private int mShoppingCartGoodsNum = 0;
    private int mGoodBuyNum = 1;

    private String mPromotionId;
    private ShareDialog mShareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradingdetail);
        ButterKnife.inject(this);

        // receive external params
        mTradingId = getIntent().getStringExtra("trading_id");

        mContext = TradingDetailActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(R.string.trading_detail);
        this.setHeaderRightImage(R.drawable.icon_option);
        this.setHeaderRightOptionImage(R.drawable.icon_shoppingcart);


        mTradingView.setListener(this);

        mUserBuyListView = (HListView) this.findViewById(R.id.userBuyListView);
        mAdapter = new AvatarImageAdapter(this);
        mUserBuyListView.setAdapter(mAdapter);

        mUserImage.setOnClickListener(this);
        mChatText.setOnClickListener(this);

        mAddToShoppingCardText.setOnClickListener(this);
        mBuyNowText.setOnClickListener(this);

        mSecondKillBtn.setOnClickListener(this);

        mGoodNumberEdit.setOnNumberViewChangeListener(this);
    }

    private void initData()
    {
        // 购物车数据
        mShoppingCartGoodsNum = ShopcartDAO.instance().getShopcartSize(
                AppApplication.getLoginId());
        updateShoppingCartStat();
        // mShoppingCardText.setText(getString(R.string.goods_count,
        // mShoppingCartGoodsNum));

        showLoadingView();
        doTradingDetailRequest(mTradingId);
        doTradingBuyList(mTradingId);
    }

    @Override
    protected void onHeaderRightClick()
    {
        if (mTradingDetailModel == null)
        {
            return;
        }

        if(AppApplication.isLogin() && TextUtils.isEmpty(mPromotionId))
        {
            doChannelPromotionRequest();
        }
        else
        {
            showShareDialog();
        }
    }

    private void showShareDialog()
    {
        if (mShareDialog == null)
        {
            ShareParam param = new ShareParam();
            param.setTopicType(TopicType.BUSINESS_TOPIC);
            param.setTopicId(mTradingDetailModel.get_id());
            param.setTitle(mTradingDetailModel.getTitle());
            param.setContent(mTradingDetailModel.getTitle());
            AttachmentModel attachment = mTradingDetailModel.getAttachments();
            if (attachment != null && attachment.getImages() != null
                    && attachment.getImages().size() > 0)
            {
                param.setImageUrl(attachment.getImages().get(0));
            }
            param.setPromotionId(mPromotionId);
            mShareDialog = new ShareDialog(mContext, param);
        }

        mShareDialog.show();
    }

    @Override
    protected void onHeaderRightOptionClick()
    {
        gotoShoppingCartActivity();
    }


    @OnClick(R.id.imageDetailLayout)
    public void onImageDetailLayoutClick()
    {
        if(mTradingDetailModel != null) {
            Intent intent = new Intent(this, WebPageActivity.class);
            intent.putExtra("web_html", mTradingDetailModel.getContent());
            startActivity(intent);
        }
    }

    private void doTradingDetailRequest(String tradingId)
    {
        TradingDetailRequest request = new TradingDetailRequest(tradingId,
                new ResponseEventHandler<RespTradingDetailModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespTradingDetailModel> request,
                            RespTradingDetailModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mTradingDetailModel = result.getData().getTrading();
                            showTradingImage(mTradingView, mTradingDetailModel);

                            mPriceText.setText(getString(R.string.money,
                                    FunctionUtils.getDouble(mTradingDetailModel
                                            .getDiscount_price() * 1.0 / 100)));
                            mTitleText.setText(mTradingDetailModel.getTitle());

                            int goodLeftCount = mTradingDetailModel.getNum()
                                    - mTradingDetailModel
                                    .getSale_num();
                            mCountInfoText
                                    .setText(getString(
                                            R.string.trading_left_info,
                                            goodLeftCount));
                            mGoodNumberEdit.setMaxValue(goodLeftCount);
                            if(goodLeftCount <= 0)
                            {
                                mBuyNowText.setEnabled(false);
                                mAddToShoppingCardText.setEnabled(false);
                            }

                            String praisePercent = "100";
                            if (mTradingDetailModel.getAppraise_count() != 0)
                            {
                                praisePercent = FunctionUtils
                                        .getDouble((double) (mTradingDetailModel
                                                .getFavorable_count() * 1.0
                                                / mTradingDetailModel
                                                .getAppraise_count()) * 100);
                            }

                            mPraiseText.setText(getString(R.string.praise)
                                    + praisePercent + "%");

                            UserModel userModel = mTradingDetailModel
                                    .getAuthor();

                            setImage(mUserImage,
                                    getImageUrl(userModel.getAvatar()),
                                    R.drawable.icon_default);
                            mSellerNameText.setText(userModel.getNickname());

                            showTradingInfo(mTradingDetailModel.getInfo());


                            if (mTradingDetailModel.getTrading_type()
                                    == TradingType.SECONDKILL)
                            {
                                mBuyLayout.setVisibility(View.GONE);
                                mSecondKillBtn.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                mBuyLayout.setVisibility(View.VISIBLE);
                                mSecondKillBtn.setVisibility(View.GONE);
                            }

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

    private void doTradingBuyList(String tradingId)
    {
        TradingBuyListRequest request = new TradingBuyListRequest(tradingId,
                new ResponseEventHandler<RespTradingBuyListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespTradingBuyListModel> request,
                            RespTradingBuyListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mUserBuyCountText.setText(getString(
                                    R.string.buy_count, result.getData()
                                            .getUser_buy_count()));

                            mAvatarList.clear();
                            List<UserModel> listModel = result.getData()
                                    .getUsers();
                            for (int i = 0; i < 5 && i < listModel.size(); i++)
                            {
                                mAvatarList.add(listModel.get(i).getAvatar());
                            }
                            mAdapter.addAll(mAvatarList);
                            mAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            // ToastUtil.showToastShort(result.getMsg());
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        // ToastUtil.showToastShort(error.getMessage());
                    }

                });
        executeRequest(request);
    }

    private void doChannelPromotionRequest()
    {
        ChannelPromotionRequest request = new ChannelPromotionRequest(
                new ResponseEventHandler<RespChannelPromotionModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespChannelPromotionModel> request,
                            RespChannelPromotionModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mPromotionId = result.getData().getPromotion().get_id();
                        }

                        showShareDialog();
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        showShareDialog();
                    }

                });
        executeRequest(request);
    }

    private void showTradingInfo(List<TradingInfoModel> listModel)
    {
        if (listModel == null || listModel.size() == 0)
        {
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (TradingInfoModel model : listModel)
        {
            sb.append("<font color='#444444' size='20'>"
                    + model.getName() + " : "
                    + "</font>").append(model.getInfo())
                    .append("<br>");
//            sb.append(model.getName()).append(" : ").append(model.getInfo())
//                    .append("\n");
        }

        sb.deleteCharAt(sb.length() - 1);

        mInfoText.setText(Html.fromHtml(sb.toString()));
    }

    private void showTradingImage(ImagePageView imagePageView,
            TradingDetailModel tradingDetailModel)
    {
        AttachmentModel attachment = tradingDetailModel.getAttachments();
        if (attachment == null || attachment.getImages() == null
                || attachment.getImages().size() == 0)
        {
            return;
        }

        String style = "";
        StyleInfoModel styleInfo = null;
        if (tradingDetailModel.getTrading_type() == TradingType.SECONDKILL)
        {
            style = PromotionStyleType.CUSTOM_SECOND_KILL;
            styleInfo = new StyleInfoModel();
            styleInfo.setX(-1);
            styleInfo.setY(-1);
            styleInfo.setStart_time(tradingDetailModel.getAllow_start_time());
            styleInfo.setEnd_time(tradingDetailModel.getAllow_end_time());

            // update other ui
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mPriceLayout
                    .getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mPriceLayout.setLayoutParams(params);
            mFixPromotionLayout.setVisibility(View.VISIBLE);
            mPraiseInfoLayout.setVisibility(View.GONE);
        }

        List<ImageModel> listImage = new ArrayList<ImageModel>();
        for (String image : attachment.getImages())
        {
            TradingPromotionModel imageModel = new TradingPromotionModel();
            imageModel.setImage(image);
            imageModel.setUrl("");
            imageModel.setStyle(style);
            imageModel.setStyle_info(styleInfo);
            listImage.add(imageModel);
        }
        imagePageView.setDataSource(listImage);
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
            case GlobalConfig.INTENT_SHOPPING_CART:
                if (data != null)
                {
                    // to do list!!!
                    mShoppingCartGoodsNum = data.getIntExtra(
                            "shoppingcart_num", 0);
                    updateShoppingCartStat();
                    // mShoppingCardText.setText(getString(R.string.goods_count,
                    // mShoppingCartGoodsNum));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.userImage:
                gotoPersionalCenter();
                break;
            case R.id.chatText:
//                gotoChatActivity();
                break;
            case R.id.addToShoppingCardText:
                onAddToShoppingCart();
                break;
            case R.id.buyNowText:
            case R.id.secondKillBtn:
                gotoPayActivity();
                break;
            default:
                break;
        }
    }

    private void gotoPersionalCenter()
    {
        if (mTradingDetailModel == null)
        {
            return;
        }

        String uid = mTradingDetailModel.getAuthor_uid();
        UIHelper.showUserCenterActivity(mContext, uid);
    }

    private void gotoChatActivity()
    {
        if (mTradingDetailModel == null)
        {
            return;
        }

        String uid = mTradingDetailModel.getAuthor_uid();

        if (!AppApplication.checkLoginState(mContext))
        {
            return;
        }

        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    private void gotoPayActivity()
    {
        if (mTradingDetailModel == null)
        {
            return;
        }

        if (!AppApplication.checkLoginState(mContext))
        {
            return;
        }

        Intent intent = new Intent(mContext, TradingPayActivity.class);
        mTradingDetailModel.setSelected(true);
        mTradingDetailModel.setBuyNum(mGoodBuyNum);
        intent.putExtra("goods", mTradingDetailModel);
        startActivity(intent);
    }

    private void gotoShoppingCartActivity()
    {
        Intent intent = new Intent(mContext, ShoppingCartActivity.class);
        this.startActivityForResult(intent, GlobalConfig.INTENT_SHOPPING_CART);
    }

    private void onAddToShoppingCart()
    {
        if (mTradingDetailModel == null)
        {
            return;
        }

        int leftGoodNum = mTradingDetailModel.getNum() - mTradingDetailModel.getSale_num() - ShopcartDAO.instance().getGoodBuyNum(AppApplication.getLoginId(), mTradingDetailModel.get_id()) - mGoodBuyNum;
        if(leftGoodNum < 0)
        {
            ToastUtil.showToastShort(R.string.no_left_good);
            return;
        }

        mTradingDetailModel.setSelected(true);
        mTradingDetailModel.setBuyNum(mGoodBuyNum);

        if (ShopcartDAO.instance().insertShopcart(mTradingDetailModel,
                AppApplication.getLoginId()))
        {
            // to do list!!!
            mShoppingCartGoodsNum++;
            updateShoppingCartStat();
            // mShoppingCardText.setText(getString(R.string.goods_count,
            // mShoppingCartGoodsNum));

            ToastUtil.showToastShort(R.string.add_to_shoppingcard_success);
        }
        else
        {
            ToastUtil.showToastShort(R.string.add_to_shoppingcard_failed);
        }
    }

    private void updateShoppingCartStat()
    {
        if (mShoppingCartGoodsNum > 0)
        {
            this.setHeaderRightOptionImage(R.drawable.icon_shoppingcart_new);
        }
        else
        {
            this.setHeaderRightOptionImage(R.drawable.icon_shoppingcart);
        }
    }

    @Override
    public void onImagePageClick(ImageModel model)
    {
    }

    @Override
    public void onImagePromotionFinish()
    {
        mSecondKillBtn.setEnabled(false);
        mSecondKillBtn.setText(R.string.promotion_end);

        mLeftInfoText.setText(R.string.promotion_end);
        mLeftTimeText.setText(String.format("%02d:%02d:%02d", 0, 0, 0));
    }

    @Override
    public void onImagePromotionUnStart(int hour, int minute, int second)
    {
        mSecondKillBtn.setEnabled(false);
        mSecondKillBtn.setText(R.string.promotion_unstart);

        mLeftInfoText.setText(R.string.promotion_start_left_time);
        mLeftTimeText
                .setText(String.format("%02d:%02d:%02d", hour, minute, second));
    }

    @Override
    public void onImagePromotionIng(int hour, int minute, int second)
    {
        mSecondKillBtn.setEnabled(true);
        mSecondKillBtn.setText(R.string.secondkill_now);

        mLeftInfoText.setText(R.string.promotion_end_left_time);
        mLeftTimeText
                .setText(String.format("%02d:%02d:%02d", hour, minute, second));
    }

    @Override
    protected void onLoginResult(int from, int result)
    {
        if (result != ErrorCode.SUCCESS)
        {
            return;
        }

        switch (from)
        {
            case LoginFromType.FROM_TRADING_CHAT:
                this.gotoChatActivity();
                break;
            case LoginFromType.FROM_TRADING_BUY_NOW:
                this.gotoPayActivity();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNumberChange(int value)
    {
        mGoodBuyNum = value;
    }
}
