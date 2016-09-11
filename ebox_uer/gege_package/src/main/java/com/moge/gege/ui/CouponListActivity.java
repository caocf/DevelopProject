package com.moge.gege.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.moge.gege.R;
import com.moge.gege.model.CouponInfoModel;
import com.moge.gege.model.CouponModel;
import com.moge.gege.model.RespCouponInfoModel;
import com.moge.gege.model.RespUserCouponListModel;
import com.moge.gege.model.UserCouponListModel;
import com.moge.gege.model.UserCouponModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.CouponInfoRequest;
import com.moge.gege.network.request.CouponListRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.CouponListAdapter;
import com.moge.gege.util.FunctionUtils;
import com.moge.gege.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CouponListActivity extends BaseActivity implements
        CouponListAdapter.CouponListListener
{
    private Context mContext;
    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private CouponListAdapter mAdapter;
    private String mNextCursor = "";
    public static final String TagFromCenter = "center";

    @InjectView(R.id.inputCouponLayout) LinearLayout mInputCouponLayout;
    @InjectView(R.id.couponCodeEdit) EditText mCouponCodeEdit;
    @InjectView(R.id.couponFeeText) TextView mCouponFeeText;
    @InjectView(R.id.cancelCouponBtn) Button mCancelCouponButton;
    @InjectView(R.id.useCouponBtn) Button mUseCouponButton;

    private String mUserCouponId;
    private String mCouponCode;
    private int mCouponFee;
    private boolean mShowCouponList = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couponlist);
        ButterKnife.inject(this);

        mUserCouponId = getIntent().getStringExtra("user_coupon_id");
        mCouponCode = getIntent().getStringExtra("coupon_code");
        mCouponFee = getIntent().getIntExtra("coupon_fee", 0);

        mContext = CouponListActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();
        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(R.string.my_coupon);


        mPullRefreshListView = (PullToRefreshListView) this
                .findViewById(R.id.couponList);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        // mPullRefreshListView.setRefreshing();
        mPullRefreshListView.setEmptyView(getLoadingView());
        mListView = mPullRefreshListView.getRefreshableView();
        mAdapter = new CouponListAdapter(mContext);
        mAdapter.setListener(this);
        mAdapter.setSelectCoupon(mUserCouponId);
        mListView.setAdapter(mAdapter);
        if (!mUserCouponId.equalsIgnoreCase(TagFromCenter))
        {
            this.setHeaderRightTitle(R.string.input_code_title);
            mListView.setOnItemClickListener(mAdapter);

            mCouponCodeEdit.setText(mCouponCode);
            mCouponFeeText.setText(getString(R.string.coupon_fee,
                    FunctionUtils.getDouble(mCouponFee * 1.0 / 100)));
        }

        mListView.setAdapter(mAdapter);
        mPullRefreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>()
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
                        doCouponListRequest(mNextCursor);
                    }
                });
    }

    @Override
    protected void onHeaderRightClick()
    {
        if (mUserCouponId.equalsIgnoreCase(TagFromCenter))
        {
            return;
        }

        mShowCouponList = !mShowCouponList;
        if(mShowCouponList)
        {
            this.setHeaderRightTitle(R.string.input_code_title);
            mInputCouponLayout.setVisibility(View.GONE);
            mPullRefreshListView.setVisibility(View.VISIBLE);
        }
        else
        {
            this.setHeaderRightTitle(R.string.select_coupon_title);
            mInputCouponLayout.setVisibility(View.VISIBLE);
            mPullRefreshListView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onHeaderLeftClick()
    {
        onCancelCouponBtnClick();
    }

    private void initData()
    {
        showLoadingView();
        doCouponListRequest(mNextCursor = "");
    }

    private void doCouponListRequest(String cursor)
    {
        CouponListRequest request = new CouponListRequest(cursor,
                new ResponseEventHandler<RespUserCouponListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespUserCouponListModel> request,
                            RespUserCouponListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            if (TextUtils.isEmpty(mNextCursor))
                            {
                                mAdapter.clear();
                            }

                            UserCouponListModel listModel = result.getData();
                            if (listModel != null
                                    && listModel.getCoupons() != null
                                    && listModel.getCoupons().size() > 0)
                            {
                                mNextCursor = listModel.getNext_cursor();
                                mAdapter.addAll(listModel.getCoupons());
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
                            ToastUtil.showToastShort(result.getMsg());
                            showLoadFailView(null);
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

    private void doCouponInfoRequest(String couponCode)
    {
        CouponInfoRequest request = new CouponInfoRequest(couponCode,
                new ResponseEventHandler<RespCouponInfoModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespCouponInfoModel> request,
                            RespCouponInfoModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            CouponInfoModel couponInfo = result.getData().getCoupon();
                            CouponModel coupon = couponInfo.getCoupon();
                            if (coupon == null)
                            {
                                ToastUtil
                                        .showToastShort(R.string.coupon_invalid);
                                return;
                            }
                            if(couponInfo.isExpried())
                            {
                                ToastUtil
                                        .showToastShort(R.string.coupon_have_expired);
                                return;
                            }
                            useCoupon("", couponInfo.get_id(), coupon.get_id(), coupon.getCode(),
                                    coupon.getFee());
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
            default:
                break;
        }
    }

    @Override
    public void onCouponItemClick(UserCouponModel model)
    {
        CouponModel coupon = model.getCoupon();
        if (model == null || coupon == null)
        {
            ToastUtil.showToastShort(R.string.coupon_invalid);
            return;
        }

        if (model.isExpried())
        {
            ToastUtil.showToastShort(R.string.coupon_have_expired);
            return;
        }

        useCoupon(model.get_id(), "", model.getCoupon_id(), model.getCode(),
                coupon.getFee());
    }

    private void useCoupon(String userCouponId, String cardCouponId, String couponId,
            String couponCode, int couponFee)
    {
        Intent intent = new Intent();
        intent.putExtra("user_coupon_id", userCouponId);
        intent.putExtra("card_coupon_id", cardCouponId);
        intent.putExtra("coupon_id", couponId);
        intent.putExtra("coupon_code", couponCode);
        intent.putExtra("coupon_fee", couponFee);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.cancelCouponBtn)
    public void onCancelCouponBtnClick()
    {
        useCoupon("", "", "", "", 0);
    }

    @OnClick(R.id.useCouponBtn)
    public void onUseCouponBtnClick()
    {
        String couponCode = mCouponCodeEdit.getText().toString().trim();
        if (TextUtils.isEmpty(couponCode))
        {
            ToastUtil.showToastShort(R.string.input_coupon_code);
            return;
        }

        this.doCouponInfoRequest(couponCode);
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            this.onHeaderLeftClick();
        }

        return false;
    }

}
